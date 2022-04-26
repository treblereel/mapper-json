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

package org.treblereel.gwt.json.mapper.apt.definition;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.util.HashMap;
import java.util.Map;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.internal.deserializer.array.ArrayJsonDeserializer;
import org.treblereel.gwt.json.mapper.internal.serializer.array.ArrayJsonSerializer;

public class ArrayBeanFieldDefinition extends FieldDefinition {

  private static Map<String, String> mapper =
      new HashMap<String, String>() {
        {
          put(Integer.class.getCanonicalName(), "Integer");
          put(Long.class.getCanonicalName(), "Long");
          put(Byte.class.getCanonicalName(), "Byte");
          put(Character.class.getCanonicalName(), "Character");
          put(Float.class.getCanonicalName(), "Float");
          put(Double.class.getCanonicalName(), "Double");
          put(Short.class.getCanonicalName(), "Short");
          put(String.class.getCanonicalName(), "String");
          put(Boolean.class.getCanonicalName(), "Boolean");

          put(int.class.getCanonicalName(), "Integer");
          put(long.class.getCanonicalName(), "Long");
          put(byte.class.getCanonicalName(), "Byte");
          put(char.class.getCanonicalName(), "Character");
          put(float.class.getCanonicalName(), "Float");
          put(double.class.getCanonicalName(), "Double");
          put(short.class.getCanonicalName(), "Short");
          put(boolean.class.getCanonicalName(), "Boolean");
        }
      };

  public ArrayBeanFieldDefinition(TypeMirror property, GenerationContext context) {
    super(property, context);
  }

  @Override
  public Statement getFieldDeserializer(PropertyDefinition field, CompilationUnit cu) {
    cu.addImport(ArrayJsonDeserializer.ArrayCreator.class);
    cu.addImport(ArrayJsonDeserializer.class);

    ArrayType arrayType = (ArrayType) property;

    if (arrayType.getComponentType().getKind().isPrimitive()) {
      return generatePrimitiveArrayDeserCall(field, arrayType);
    }

    TypeElement deser =
        context.getTypeRegistry().getDeserializer(arrayType.getComponentType().toString());

    ObjectCreationExpr arrayJsonDeserializer = new ObjectCreationExpr();
    ClassOrInterfaceType type = new ClassOrInterfaceType();
    type.setName(ArrayJsonDeserializer.class.getSimpleName());
    type.setTypeArguments(
        new ClassOrInterfaceType().setName(arrayType.getComponentType().toString()));
    arrayJsonDeserializer.setType(type);

    return new ExpressionStmt(
        new MethodCallExpr(new NameExpr("bean"), field.getSetter().getSimpleName().toString())
            .addArgument(
                new MethodCallExpr(
                        arrayJsonDeserializer
                            .addArgument(
                                new ObjectCreationExpr()
                                    .setType(deser.getQualifiedName().toString()))
                            .addArgument(createArrayCreatorCall(arrayType.getComponentType())),
                        "deserialize")
                    .addArgument(
                        new MethodCallExpr(new NameExpr("jsonObject"), "getJsonArray")
                            .addArgument(new StringLiteralExpr(field.getName())))
                    .addArgument(new NameExpr("ctx"))));
  }

  private Expression createArrayCreatorCall(TypeMirror array) {
    ClassOrInterfaceType typeOf =
        new ClassOrInterfaceType()
            .setName(ArrayJsonDeserializer.ArrayCreator.class.getSimpleName())
            .setTypeArguments(new ClassOrInterfaceType().setName(array.toString()));

    return new CastExpr()
        .setType(typeOf)
        .setExpression(
            new NameExpr(context.getProcessingEnv().getTypeUtils().erasure(array) + "[]::new"));
  }

  private ExpressionStmt generatePrimitiveArrayDeserCall(
      PropertyDefinition field, ArrayType array) {
    TypeElement deser = context.getTypeRegistry().getDeserializer(array.toString());

    return new ExpressionStmt(
        new MethodCallExpr(new NameExpr("bean"), field.getSetter().getSimpleName().toString())
            .addArgument(
                new MethodCallExpr(
                        new ObjectCreationExpr().setType(deser.getQualifiedName().toString()),
                        "deserialize")
                    .addArgument(
                        new MethodCallExpr(new NameExpr("jsonObject"), "getJsonArray")
                            .addArgument(new StringLiteralExpr(field.getName())))
                    .addArgument(new NameExpr("ctx"))));
  }

  @Override
  public Statement getFieldSerializer(PropertyDefinition field, CompilationUnit cu) {
    cu.addImport(ArrayJsonSerializer.class);

    ArrayType arrayType = (ArrayType) property;

    if (arrayType.getComponentType().getKind().isPrimitive()) {
      return generatePrimitiveArraySerCall(field, arrayType);
    }

    TypeElement ser =
        context.getTypeRegistry().getSerializer(arrayType.getComponentType().toString());

    ObjectCreationExpr arrayJsonSerializer = new ObjectCreationExpr();
    ClassOrInterfaceType type = new ClassOrInterfaceType();
    type.setName(ArrayJsonSerializer.class.getSimpleName());
    type.setTypeArguments(
        new ClassOrInterfaceType().setName(arrayType.getComponentType().toString()));
    arrayJsonSerializer.setType(type);

    return new ExpressionStmt(
        new MethodCallExpr(
                arrayJsonSerializer.addArgument(
                    new ObjectCreationExpr().setType(ser.getQualifiedName().toString())),
                "serialize")
            .addArgument(
                new MethodCallExpr(
                    new NameExpr("bean"), field.getGetter().getSimpleName().toString()))
            .addArgument(new StringLiteralExpr(field.getName()))
            .addArgument(new NameExpr("generator"))
            .addArgument(new NameExpr("ctx")));
  }

  private Statement generatePrimitiveArraySerCall(PropertyDefinition field, ArrayType array) {
    TypeElement ser = context.getTypeRegistry().getSerializer(array.toString());

    return new ExpressionStmt(
        new MethodCallExpr(
                new ObjectCreationExpr().setType(ser.getQualifiedName().toString()), "serialize")
            .addArgument(
                new MethodCallExpr(
                    new NameExpr("bean"), field.getGetter().getSimpleName().toString()))
            .addArgument(new StringLiteralExpr(field.getName()))
            .addArgument(new NameExpr("generator"))
            .addArgument(new NameExpr("ctx")));
  }
}
