/*
 * Copyright © 2026 Treblereel
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
package org.treblereel.gwt.json.generator;

import java.util.Objects;
import java.util.Set;

public final class NamingUtils {

  private static final Set<String> JAVA_KEYWORDS =
      Set.of(
          "abstract",
          "assert",
          "boolean",
          "break",
          "byte",
          "case",
          "catch",
          "char",
          "class",
          "const",
          "continue",
          "default",
          "do",
          "double",
          "else",
          "enum",
          "extends",
          "final",
          "finally",
          "float",
          "for",
          "goto",
          "if",
          "implements",
          "import",
          "instanceof",
          "int",
          "interface",
          "long",
          "native",
          "new",
          "package",
          "private",
          "protected",
          "public",
          "return",
          "short",
          "static",
          "strictfp",
          "super",
          "switch",
          "synchronized",
          "this",
          "throw",
          "throws",
          "transient",
          "try",
          "void",
          "volatile",
          "while");

  private NamingUtils() {}

  public static String toCamelCase(String input) {
    Objects.requireNonNull(input, "input must not be null");
    StringBuilder sb = new StringBuilder();
    boolean capitalizeNext = false;
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == '-' || c == '_' || c == ' ') {
        capitalizeNext = true;
      } else if (capitalizeNext) {
        sb.append(Character.toUpperCase(c));
        capitalizeNext = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static String toPascalCase(String input) {
    String camel = toCamelCase(input);
    if (camel.isEmpty()) return camel;
    return Character.toUpperCase(camel.charAt(0)) + camel.substring(1);
  }

  public static String toUpperSnakeCase(String input) {
    Objects.requireNonNull(input, "input must not be null");
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      if (c == '-' || c == '_' || c == ':' || c == '.' || c == ' ') {
        sb.append('_');
      } else if (!Character.isLetterOrDigit(c)) {
        sb.append('_');
      } else if (Character.isUpperCase(c)
          && i > 0
          && Character.isLetterOrDigit(input.charAt(i - 1))
          && !Character.isUpperCase(input.charAt(i - 1))) {
        sb.append('_');
        sb.append(c);
      } else {
        sb.append(Character.toUpperCase(c));
      }
    }
    String result = sb.toString().replaceAll("_+", "_");
    if (!result.isEmpty() && result.charAt(0) == '_') result = result.substring(1);
    if (!result.isEmpty() && result.charAt(result.length() - 1) == '_')
      result = result.substring(0, result.length() - 1);
    if (!result.isEmpty() && Character.isDigit(result.charAt(0))) result = "_" + result;
    return result;
  }

  public static String sanitizeFieldName(String name) {
    String camel = toCamelCase(name);
    if (JAVA_KEYWORDS.contains(camel)) {
      return camel + "_";
    }
    return camel;
  }

  public static boolean needsJsonbProperty(String schemaName, String fieldName) {
    Objects.requireNonNull(schemaName, "schemaName must not be null");
    Objects.requireNonNull(fieldName, "fieldName must not be null");
    return !schemaName.equals(fieldName);
  }

  public static String extractRefName(String ref) {
    Objects.requireNonNull(ref, "ref must not be null");
    return ref.substring(ref.lastIndexOf('/') + 1);
  }
}
