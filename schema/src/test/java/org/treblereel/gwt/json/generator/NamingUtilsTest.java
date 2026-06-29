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

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class NamingUtilsTest {

  @Test
  void toCamelCaseSimple() {
    assertEquals("name", NamingUtils.toCamelCase("name"));
  }

  @Test
  void toCamelCaseHyphenated() {
    assertEquals("zipCode", NamingUtils.toCamelCase("zip-code"));
  }

  @Test
  void toCamelCaseUnderscored() {
    assertEquals("firstName", NamingUtils.toCamelCase("first_name"));
  }

  @Test
  void toCamelCaseMultipleSeparators() {
    assertEquals("myLongName", NamingUtils.toCamelCase("my-long-name"));
  }

  @Test
  void toPascalCaseSimple() {
    assertEquals("Address", NamingUtils.toPascalCase("address"));
  }

  @Test
  void toPascalCaseHyphenated() {
    assertEquals("MyType", NamingUtils.toPascalCase("my-type"));
  }

  @Test
  void toUpperSnakeCaseSimple() {
    assertEquals("ACTIVE", NamingUtils.toUpperSnakeCase("active"));
  }

  @Test
  void toUpperSnakeCaseCamelCase() {
    assertEquals("MY_VALUE", NamingUtils.toUpperSnakeCase("myValue"));
  }

  @Test
  void toUpperSnakeCaseHyphenated() {
    assertEquals("SOME_VALUE", NamingUtils.toUpperSnakeCase("some-value"));
  }

  @Test
  void sanitizeFieldNameReservedKeyword() {
    assertEquals("class_", NamingUtils.sanitizeFieldName("class"));
  }

  @Test
  void sanitizeFieldNameNonKeyword() {
    assertEquals("name", NamingUtils.sanitizeFieldName("name"));
  }

  @Test
  void sanitizeFieldNameAllKeywords() {
    for (String kw :
        new String[] {
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
          "while"
        }) {
      assertEquals(kw + "_", NamingUtils.sanitizeFieldName(kw), "keyword: " + kw);
    }
  }

  @Test
  void needsJsonbPropertyWhenDifferent() {
    assertTrue(NamingUtils.needsJsonbProperty("zip-code", "zipCode"));
  }

  @Test
  void needsJsonbPropertyWhenSame() {
    assertFalse(NamingUtils.needsJsonbProperty("name", "name"));
  }

  @Test
  void extractRefName() {
    assertEquals("Address", NamingUtils.extractRefName("#/$defs/Address"));
  }

  @Test
  void extractRefNameNested() {
    assertEquals("MyType", NamingUtils.extractRefName("#/$defs/MyType"));
  }

  @Test
  void toPascalCaseHandlesSpaces() {
    assertEquals("TestUserSchema", NamingUtils.toPascalCase("Test User Schema"));
  }

  @Test
  void toCamelCaseHandlesSpaces() {
    assertEquals("testUserSchema", NamingUtils.toCamelCase("test user schema"));
  }
}
