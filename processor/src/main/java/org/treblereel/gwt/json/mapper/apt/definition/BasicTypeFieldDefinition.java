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
import javax.lang.model.type.TypeMirror;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.apt.utils.TypeUtils;

public class BasicTypeFieldDefinition extends FieldDefinition {

  protected BasicTypeFieldDefinition(TypeMirror property, GenerationContext context) {
    super(property, context);
  }

  @Override
  public Expression getFieldDeserializer(PropertyDefinition field, CompilationUnit cu) {
    String setter = field.getSetter().getSimpleName().toString();
    Expression jsonGetter = getPropertyAccessor(field);

    MethodCallExpr method = new MethodCallExpr(new NameExpr("bean"), setter);
    method.addArgument(jsonGetter);
    return method;
  }

  @Override
  public Expression getFieldSerializer(PropertyDefinition field, CompilationUnit cu) {
    return new MethodCallExpr(new NameExpr("generator"), "write")
        .addArgument(new StringLiteralExpr(field.getName()))
        .addArgument(
            new MethodCallExpr(new NameExpr("bean"), field.getGetter().getSimpleName().toString()));
  }

  private Expression getPropertyAccessor(PropertyDefinition field) {
    TypeUtils.BoxedTypes boxedTypes = context.getTypeUtils().getBoxedTypes();
    TypeMirror type = field.getType();
    NameExpr jsonObject = new NameExpr("jsonObject");
    StringLiteralExpr name = new StringLiteralExpr(field.getName());
    if (boxedTypes.isString(type)) {
      return new MethodCallExpr(jsonObject, "getString").addArgument(name);
    }
    if (boxedTypes.isBoolean(type)) {
      return new MethodCallExpr(jsonObject, "getBoolean").addArgument(name);
    }
    if (boxedTypes.isInt(type)) {
      return new MethodCallExpr(jsonObject, "getInt").addArgument(name);
    }

    if (boxedTypes.isLong(type)) {
      return new MethodCallExpr(
          new MethodCallExpr(jsonObject, "getJsonNumber").addArgument(name), "longValue");
    }

    if (boxedTypes.isDouble(type)) {
      return new MethodCallExpr(
          new MethodCallExpr(jsonObject, "getJsonNumber").addArgument(name), "doubleValue");
    }

    if (boxedTypes.isChar(type)) {
      return new CastExpr()
          .setType(char.class)
          .setExpression(new MethodCallExpr(jsonObject, "getInt").addArgument(name));
    }
    if (boxedTypes.isFloat(type)) {
      return new MethodCallExpr(
          new MethodCallExpr(
              new MethodCallExpr(jsonObject, "getJsonNumber").addArgument(name), "bigDecimalValue"),
          "floatValue");
    }
    if (boxedTypes.isShort(type)) {
      return new CastExpr()
          .setType(short.class)
          .setExpression(new MethodCallExpr(jsonObject, "getInt").addArgument(name));
    }

    throw new IllegalArgumentException("Unsupported type " + type);
  }
}
