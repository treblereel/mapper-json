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
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.auto.common.MoreElements;
import jakarta.json.JsonObjectDecorator;
import jakarta.json.stream.AbstractBeanJsonDeserializer;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.apt.definition.BeanDefinition;
import org.treblereel.gwt.json.mapper.apt.definition.FieldDefinition;
import org.treblereel.gwt.json.mapper.apt.definition.FieldDefinitionFactory;
import org.treblereel.gwt.json.mapper.apt.logger.TreeLogger;

public class DeserializerGenerator extends AbstractGenerator {

  private static final String BEAN_JSON_DESERIALIZER_IMPL = "_JsonDeserializerImpl";

  private ConstructorDeclaration constructor;

  private final FieldDefinitionFactory fieldDefinitionFactory;

  public DeserializerGenerator(GenerationContext context, TreeLogger logger) {
    super(context, logger);
    fieldDefinitionFactory = new FieldDefinitionFactory(context);
  }

  @Override
  protected String getMapperName(TypeElement type) {
    return (type.getEnclosingElement().getKind().equals(ElementKind.PACKAGE)
            ? ""
            : MoreElements.asType(type.getEnclosingElement()).getSimpleName().toString() + "_")
        + type.getSimpleName()
        + BEAN_JSON_DESERIALIZER_IMPL;
  }

  @Override
  protected void configureClassType(BeanDefinition type) {
    cu.addImport(AbstractBeanJsonDeserializer.class);

    declaration
        .getExtendedTypes()
        .add(
            new ClassOrInterfaceType()
                .setName(AbstractBeanJsonDeserializer.class.getSimpleName())
                .setTypeArguments(
                    new ClassOrInterfaceType()
                        .setName(type.getElement().getSimpleName().toString())));

    constructor = declaration.addConstructor(Modifier.Keyword.PUBLIC);
  }

  @Override
  protected void init(BeanDefinition type) {
    logger.branch(
        TreeLogger.INFO, "Generating deserializer for " + type.getElement().getSimpleName());

    addStaticInstance(type);
    addNewInstance(declaration, type);

    type.getPropertyDefinitionsAsStream()
        .forEach(
            propertyDefinition -> {
              FieldDefinition fieldDefinition =
                  fieldDefinitionFactory.getFieldDefinition(propertyDefinition);
              addGetter(
                  type,
                  constructor.getBody(),
                  fieldDefinition.getFieldDeserializer(propertyDefinition, cu));
            });
  }

  private void addNewInstance(ClassOrInterfaceDeclaration declaration, BeanDefinition type) {
    MethodDeclaration methodDeclaration =
        declaration.addMethod("newInstance", Modifier.Keyword.PUBLIC);
    methodDeclaration.setType(
        new ClassOrInterfaceType().setName(type.getElement().getSimpleName().toString()));
    methodDeclaration
        .getBody()
        .get()
        .addAndGetStatement(
            new ReturnStmt(
                new ObjectCreationExpr().setType(type.getElement().getQualifiedName().toString())));
  }

  private void addGetter(BeanDefinition type, BlockStmt body, Expression call) {
    LambdaExpr lambda = new LambdaExpr();
    lambda.setEnclosingParameters(true);
    lambda
        .getParameters()
        .add(
            new Parameter()
                .setType(type.getElement().getQualifiedName().toString())
                .setName("bean"));
    lambda
        .getParameters()
        .add(
            new Parameter()
                .setType(JsonObjectDecorator.class.getCanonicalName())
                .setName("jsonObject"));
    lambda.setBody(new ExpressionStmt(call));

    body.addStatement(new MethodCallExpr(new NameExpr("properties"), "add").addArgument(lambda));
  }
}
