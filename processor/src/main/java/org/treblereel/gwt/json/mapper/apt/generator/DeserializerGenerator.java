/*
 * Copyright © 2022 Treblereel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.treblereel.gwt.json.mapper.apt.generator;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.UnknownType;
import com.google.auto.common.MoreTypes;
import jakarta.json.bind.annotation.JsonbProperty;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.apt.definition.BeanDefinition;
import org.treblereel.gwt.json.mapper.apt.definition.EnumBeanFieldDefinition;
import org.treblereel.gwt.json.mapper.apt.definition.FieldDefinition;
import org.treblereel.gwt.json.mapper.apt.definition.FieldDefinitionFactory;
import org.treblereel.gwt.json.mapper.apt.definition.PropertyDefinition;
import org.treblereel.gwt.json.mapper.apt.logger.TreeLogger;
import org.treblereel.gwt.json.mapper.internal.deserializer.AbstractBeanJsonDeserializer;
import org.treblereel.gwt.json.mapper.internal.deserializer.AbstractCreatorBeanJsonDeserializer;

public class DeserializerGenerator extends AbstractGenerator {

  private ConstructorDeclaration constructor;

  private final FieldDefinitionFactory fieldDefinitionFactory;

  public DeserializerGenerator(GenerationContext context, TreeLogger logger) {
    super(context, logger);
    fieldDefinitionFactory = context.getFieldDefinitionFactory();
  }

  @Override
  protected String getMapperName(TypeElement type) {
    return context.getTypeUtils().getJsonDeserializerImplName(type);
  }

  @Override
  protected void configureClassType(BeanDefinition type) {
    ClassOrInterfaceType typeArg =
        new ClassOrInterfaceType().setName(type.getElement().getQualifiedName().toString());

    if (type.hasJsonbCreator()) {
      cu.addImport(AbstractCreatorBeanJsonDeserializer.class);
      declaration
          .getExtendedTypes()
          .add(
              new ClassOrInterfaceType()
                  .setName(AbstractCreatorBeanJsonDeserializer.class.getSimpleName())
                  .setTypeArguments(typeArg));
    } else {
      cu.addImport(AbstractBeanJsonDeserializer.class);
      declaration
          .getExtendedTypes()
          .add(
              new ClassOrInterfaceType()
                  .setName(AbstractBeanJsonDeserializer.class.getSimpleName())
                  .setTypeArguments(typeArg));
    }

    constructor = declaration.addConstructor(Modifier.Keyword.PUBLIC);
  }

  @Override
  protected void init(BeanDefinition type) {
    logger.branch(
        TreeLogger.INFO, "Generating deserializer for " + type.getElement().getSimpleName());

    addStaticInstance(type);

    if (type.hasJsonbCreator()) {
      initCreatorBean(type);
    } else {
      addNewInstance(declaration, type);
      type.getPropertyDefinitionsAsStream()
          .forEach(
              propertyDefinition -> {
                FieldDefinition fieldDefinition =
                    fieldDefinitionFactory.getFieldDefinition(propertyDefinition);
                addGetter(
                    propertyDefinition,
                    constructor.getBody(),
                    fieldDefinition.getFieldDeserializer(propertyDefinition, cu));
              });
    }
  }

  private void initCreatorBean(BeanDefinition type) {
    ExecutableElement creator = type.getJsonbCreator();
    Set<String> creatorParamNames = type.getCreatorParameterNames();

    for (VariableElement param : creator.getParameters()) {
      JsonbProperty prop = param.getAnnotation(JsonbProperty.class);
      String paramName =
          (prop != null && !prop.value().isEmpty())
              ? prop.value()
              : param.getSimpleName().toString();
      addCreatorParam(paramName, param.asType(), constructor.getBody());
    }

    type.getPropertyDefinitionsAsStream()
        .filter(prop -> !creatorParamNames.contains(prop.getName()))
        .forEach(
            propertyDefinition -> {
              FieldDefinition fieldDefinition =
                  fieldDefinitionFactory.getFieldDefinition(propertyDefinition);
              addGetter(
                  propertyDefinition,
                  constructor.getBody(),
                  fieldDefinition.getFieldDeserializer(propertyDefinition, cu));
            });

    addCreateInstance(declaration, type, creator);
  }

  private void addCreatorParam(String paramName, TypeMirror paramType, BlockStmt body) {
    LambdaExpr lambda = new LambdaExpr();
    lambda.setEnclosingParameters(true);
    lambda.getParameters().add(new Parameter().setType(new UnknownType()).setName("jsonObject"));
    lambda.getParameters().add(new Parameter().setType(new UnknownType()).setName("ctx"));

    Expression deserExpr = getCreatorParamDeserExpr(paramName, paramType);
    lambda.setBody(new ExpressionStmt(deserExpr));

    body.addStatement(
        new MethodCallExpr(new NameExpr("creatorParams"), "put")
            .addArgument(new StringLiteralExpr(paramName))
            .addArgument(lambda));
  }

  private Expression getCreatorParamDeserExpr(String paramName, TypeMirror paramType) {
    TypeMirror erasedType = context.getProcessingEnv().getTypeUtils().erasure(paramType);

    if (context.getTypeUtils().isSimpleType(erasedType)) {
      TypeElement deser = context.getTypeRegistry().getDeserializer(paramType);
      return new MethodCallExpr(
              new ObjectCreationExpr().setType(deser.getQualifiedName().toString()), "deserialize")
          .addArgument(
              new MethodCallExpr(new NameExpr("jsonObject"), "get")
                  .addArgument(new StringLiteralExpr(paramName)))
          .addArgument(new NameExpr("ctx"));
    } else if (MoreTypes.asElement(erasedType).getKind().equals(ElementKind.ENUM)) {
      FieldDefinition fieldDef = fieldDefinitionFactory.getFieldDefinition(paramType);
      Expression enumDeser = ((EnumBeanFieldDefinition) fieldDef).getDeserializerCreationExpr(cu);
      return new CastExpr()
          .setType(new ClassOrInterfaceType().setName(paramType.toString()))
          .setExpression(
              new MethodCallExpr(enumDeser, "deserialize")
                  .addArgument(
                      new MethodCallExpr(new NameExpr("jsonObject"), "getJsonString")
                          .addArgument(new StringLiteralExpr(paramName)))
                  .addArgument(new NameExpr("ctx")));
    } else {
      String deser =
          context
              .getTypeUtils()
              .getJsonDeserializerImplQualifiedName(MoreTypes.asTypeElement(paramType), cu);
      return new MethodCallExpr(new ObjectCreationExpr().setType(deser), "deserialize")
          .addArgument(
              new MethodCallExpr(new NameExpr("jsonObject"), "getJsonObject")
                  .addArgument(new StringLiteralExpr(paramName)))
          .addArgument(new NameExpr("ctx"));
    }
  }

  private void addCreateInstance(
      ClassOrInterfaceDeclaration declaration, BeanDefinition type, ExecutableElement creator) {
    cu.addImport(Map.class);

    MethodDeclaration method = declaration.addMethod("createInstance", Modifier.Keyword.PROTECTED);
    method.setType(
        new ClassOrInterfaceType().setName(type.getElement().getQualifiedName().toString()));
    method.addParameter(
        new ClassOrInterfaceType()
            .setName("Map")
            .setTypeArguments(
                new ClassOrInterfaceType().setName("String"),
                new ClassOrInterfaceType().setName("Object")),
        "params");

    String qualifiedName = type.getElement().getQualifiedName().toString();

    Expression creatorCall;
    if (creator.getKind() == ElementKind.CONSTRUCTOR) {
      ObjectCreationExpr newExpr = new ObjectCreationExpr().setType(qualifiedName);
      for (VariableElement param : creator.getParameters()) {
        newExpr.addArgument(getCreateInstanceArgExpr(param));
      }
      creatorCall = newExpr;
    } else {
      MethodCallExpr factoryCall =
          new MethodCallExpr(new NameExpr(qualifiedName), creator.getSimpleName().toString());
      for (VariableElement param : creator.getParameters()) {
        factoryCall.addArgument(getCreateInstanceArgExpr(param));
      }
      creatorCall = factoryCall;
    }

    method.getBody().get().addStatement(new ReturnStmt(creatorCall));
  }

  private Expression getCreateInstanceArgExpr(VariableElement param) {
    JsonbProperty prop = param.getAnnotation(JsonbProperty.class);
    String paramName =
        (prop != null && !prop.value().isEmpty()) ? prop.value() : param.getSimpleName().toString();

    TypeMirror paramType = param.asType();
    Expression getCall =
        new MethodCallExpr(new NameExpr("params"), "get")
            .addArgument(new StringLiteralExpr(paramName));

    if (paramType.getKind().isPrimitive()) {
      Expression containsKey =
          new MethodCallExpr(new NameExpr("params"), "containsKey")
              .addArgument(new StringLiteralExpr(paramName));
      CastExpr cast =
          new CastExpr().setType(getPrimitiveType(paramType.getKind())).setExpression(getCall);
      return new ConditionalExpr(containsKey, cast, getDefaultValue(paramType.getKind()));
    } else {
      return new CastExpr()
          .setType(new ClassOrInterfaceType().setName(paramType.toString()))
          .setExpression(getCall);
    }
  }

  private PrimitiveType getPrimitiveType(TypeKind kind) {
    switch (kind) {
      case INT:
        return PrimitiveType.intType();
      case LONG:
        return PrimitiveType.longType();
      case DOUBLE:
        return PrimitiveType.doubleType();
      case FLOAT:
        return PrimitiveType.floatType();
      case BOOLEAN:
        return PrimitiveType.booleanType();
      case SHORT:
        return PrimitiveType.shortType();
      case BYTE:
        return PrimitiveType.byteType();
      case CHAR:
        return PrimitiveType.charType();
      default:
        return PrimitiveType.intType();
    }
  }

  private Expression getDefaultValue(TypeKind kind) {
    switch (kind) {
      case BOOLEAN:
        return new NameExpr("false");
      case LONG:
        return new NameExpr("0L");
      case DOUBLE:
        return new NameExpr("0.0");
      case FLOAT:
        return new NameExpr("0.0f");
      case CHAR:
        return new NameExpr("'\\0'");
      default:
        return new IntegerLiteralExpr("0");
    }
  }

  private void addNewInstance(ClassOrInterfaceDeclaration declaration, BeanDefinition type) {
    MethodDeclaration methodDeclaration =
        declaration.addMethod("newInstance", Modifier.Keyword.PUBLIC);
    methodDeclaration.setType(
        new ClassOrInterfaceType().setName(type.getElement().getQualifiedName().toString()));
    methodDeclaration
        .getBody()
        .get()
        .addAndGetStatement(
            new ReturnStmt(
                new ObjectCreationExpr().setType(type.getElement().getQualifiedName().toString())));
  }

  private void addGetter(PropertyDefinition propertyDefinition, BlockStmt body, Statement call) {

    LambdaExpr lambda = new LambdaExpr();
    lambda.setEnclosingParameters(true);
    lambda.getParameters().add(new Parameter().setType(new UnknownType()).setName("bean"));
    lambda.getParameters().add(new Parameter().setType(new UnknownType()).setName("jsonObject"));
    lambda.getParameters().add(new Parameter().setType(new UnknownType()).setName("ctx"));
    lambda.setBody(call);
    body.addStatement(
        new MethodCallExpr(new NameExpr("properties"), "put")
            .addArgument(new StringLiteralExpr(propertyDefinition.getName()))
            .addArgument(lambda));
  }
}
