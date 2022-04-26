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
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;

public class BasicTypeFieldDefinition extends FieldDefinition {

  protected BasicTypeFieldDefinition(TypeMirror property, GenerationContext context) {
    super(property, context);
  }

  @Override
  public Statement getFieldDeserializer(PropertyDefinition field, CompilationUnit cu) {
    String setter = field.getSetter().getSimpleName().toString();
    Expression jsonGetter = getPropertyAccessor(field);

    MethodCallExpr method = new MethodCallExpr(new NameExpr("bean"), setter);
    method.addArgument(jsonGetter);
    return new ExpressionStmt(method);
  }

  @Override
  public Statement getFieldSerializer(PropertyDefinition field, CompilationUnit cu) {
    return new ExpressionStmt(
        new MethodCallExpr(new NameExpr("generator"), "write")
            .addArgument(new StringLiteralExpr(field.getName()))
            .addArgument(
                new MethodCallExpr(
                    new NameExpr("bean"), field.getGetter().getSimpleName().toString())));
  }

  private Expression getPropertyAccessor(PropertyDefinition field) {
    TypeElement deser = context.getTypeRegistry().getDeserializer(field.getType());

    System.out.println("DESER " + deser);
    NameExpr jsonObject = new NameExpr("jsonObject");
    StringLiteralExpr name = new StringLiteralExpr(field.getName());

    /*
        TypeUtils.BoxedTypes boxedTypes = context.getTypeUtils().getBoxedTypes();
        TypeMirror type = field.getType();
        NameExpr jsonObject = new NameExpr("jsonObject");
        StringLiteralExpr name = new StringLiteralExpr(field.getName());
        if (boxedTypes.isString(type)) {
          return new MethodCallExpr(jsonObject, "getString").addArgument(name);
        }
        if (boxedTypes.isBoolean(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getBoolean").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getBooleanBoxed").addArgument(name);
        }
        if (boxedTypes.isInt(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getInt").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getInteger").addArgument(name);
        }

        if (boxedTypes.isLong(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getLong").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getLongBoxed").addArgument(name);
        }

        if (boxedTypes.isDouble(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getDouble").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getDoubleBoxed").addArgument(name);
        }

        if (boxedTypes.isChar(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getChar").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getCharBoxed").addArgument(name);
        }
        if (boxedTypes.isFloat(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getFloat").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getFloatBoxed").addArgument(name);
        }
        if (boxedTypes.isShort(type)) {
          if (type.getKind().isPrimitive()) {
            return new MethodCallExpr(jsonObject, "getShort").addArgument(name);
          }
          return new MethodCallExpr(jsonObject, "getShortBoxed").addArgument(name);
        }
    */

    return new MethodCallExpr(
            new ObjectCreationExpr().setType(deser.getQualifiedName().toString()), "deserialize")
        .addArgument(new MethodCallExpr(jsonObject, "get").addArgument(name))
        .addArgument(new NameExpr("ctx"));
  }
}
