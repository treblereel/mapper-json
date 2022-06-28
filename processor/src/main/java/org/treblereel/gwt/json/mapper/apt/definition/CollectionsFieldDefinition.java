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
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.google.auto.common.MoreTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.internal.serializer.collection.CollectionJsonSerializer;

public class CollectionsFieldDefinition extends FieldDefinition {

  protected CollectionsFieldDefinition(TypeMirror property, GenerationContext context) {
    super(property, context);
  }

  @Override
  public Statement getFieldDeserializer(PropertyDefinition field, CompilationUnit cu) {
    TypeElement deserializer =
        context
            .getTypeRegistry()
            .getDeserializer(context.getProcessingEnv().getTypeUtils().erasure(property));

    cu.addImport(deserializer.getQualifiedName().toString());

    System.out.println("FieldDefinition.getFieldDeserializer: " + deserializer.getQualifiedName());

    TypeMirror typeMirror = MoreTypes.asDeclared(field.getType()).getTypeArguments().get(0);

    String deser =
        context
            .getTypeUtils()
            .getJsonDeserializerImplQualifiedName(MoreTypes.asTypeElement(typeMirror));

    ClassOrInterfaceType type = new ClassOrInterfaceType();
    type.setName(deserializer.getSimpleName().toString());
    type.setTypeArguments(new ClassOrInterfaceType().setName(typeMirror.toString()));
    ObjectCreationExpr deserializerCreationExpr = new ObjectCreationExpr();
    deserializerCreationExpr.setType(type);

    return new ExpressionStmt(
        new MethodCallExpr(new NameExpr("bean"), field.getSetter().getSimpleName().toString())
            .addArgument(
                new MethodCallExpr(
                        deserializerCreationExpr.addArgument(
                            new ObjectCreationExpr().setType(deser)),
                        "deserialize")
                    .addArgument(
                        new MethodCallExpr(new NameExpr("jsonObject"), "getJsonArray")
                            .addArgument(new StringLiteralExpr(field.getName())))
                    .addArgument(new NameExpr("ctx"))));
  }

  @Override
  public Statement getFieldSerializer(PropertyDefinition field, CompilationUnit cu) {
    cu.addImport(CollectionJsonSerializer.class);

    ObjectCreationExpr serializerCreationExpr = new ObjectCreationExpr();
    ClassOrInterfaceType type = new ClassOrInterfaceType();

    TypeMirror typeMirror = MoreTypes.asDeclared(field.getType()).getTypeArguments().get(0);

    String ser =
        context
            .getTypeUtils()
            .getJsonSerializerImplQualifiedName(MoreTypes.asTypeElement(typeMirror));

    type.setName(CollectionJsonSerializer.class.getSimpleName());
    type.setTypeArguments(new ClassOrInterfaceType().setName(typeMirror.toString()));
    ObjectCreationExpr deserializerCreationExpr = new ObjectCreationExpr();
    deserializerCreationExpr.setType(type);
    type.setTypeArguments(new ClassOrInterfaceType().setName(typeMirror.toString()));

    serializerCreationExpr.setType(type);

    return new ExpressionStmt(
        new MethodCallExpr(
                serializerCreationExpr.addArgument(
                    new ObjectCreationExpr().setType(new ClassOrInterfaceType().setName(ser))),
                "serialize")
            .addArgument(
                new MethodCallExpr(
                    new NameExpr("bean"), field.getGetter().getSimpleName().toString()))
            .addArgument(new StringLiteralExpr(field.getName()))
            .addArgument(new NameExpr("generator"))
            .addArgument(new NameExpr("ctx")));
  }
}
