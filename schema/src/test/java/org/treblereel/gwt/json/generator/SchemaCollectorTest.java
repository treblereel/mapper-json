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

import java.util.List;
import org.junit.jupiter.api.Test;
import org.treblereel.gwt.json.generator.model.GeneratorModel;
import org.treblereel.gwt.json.generator.model.JavaClass;
import org.treblereel.gwt.json.generator.model.JavaField;
import org.treblereel.yaml.schema.Parser;
import org.treblereel.yaml.schema.model.SchemaDefinition;

class SchemaCollectorTest {

  private GeneratorModel collect(String yaml) {
    SchemaDefinition schema = new Parser().parseYaml(yaml);
    return new SchemaCollector("com.example").collect(schema);
  }

  @Test
  void collectsSimpleObjectWithStringField() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  name:
                    type: string
                """);
    assertEquals(1, model.classes().size());
    JavaClass root = model.classes().get(0);
    assertEquals("Root", root.className());
    assertEquals("com.example", root.packageName());
    assertEquals(1, root.fields().size());

    JavaField field = root.fields().get(0);
    assertEquals("name", field.fieldName());
    assertEquals("name", field.schemaName());
    assertEquals("String", field.javaType());
    assertFalse(field.needsJsonbProperty());
  }

  @Test
  void collectsIntegerField() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  age:
                    type: integer
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("int", field.javaType());
  }

  @Test
  void collectsNumberField() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  score:
                    type: number
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("double", field.javaType());
  }

  @Test
  void collectsBooleanField() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  active:
                    type: boolean
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("boolean", field.javaType());
  }

  @Test
  void tracksRequiredFields() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  name:
                    type: string
                  age:
                    type: integer
                required:
                  - name
                """);
    JavaClass root = model.classes().get(0);
    JavaField nameField =
        root.fields().stream().filter(f -> f.fieldName().equals("name")).findFirst().orElseThrow();
    JavaField ageField =
        root.fields().stream().filter(f -> f.fieldName().equals("age")).findFirst().orElseThrow();
    assertTrue(nameField.required());
    assertFalse(ageField.required());
  }

  @Test
  void sanitizesHyphenatedFieldName() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  zip-code:
                    type: string
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("zipCode", field.fieldName());
    assertEquals("zip-code", field.schemaName());
    assertTrue(field.needsJsonbProperty());
  }

  @Test
  void usesSchemaTitle() {
    GeneratorModel model =
        collect(
            """
                title: Person
                type: object
                properties:
                  name:
                    type: string
                """);
    assertEquals("Person", model.classes().get(0).className());
  }

  @Test
  void noRootClassWhenOnlyDefs() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Address:
                    type: object
                    properties:
                      city:
                        type: string
                """);
    assertEquals(1, model.classes().size());
    assertEquals("Address", model.classes().get(0).className());
  }

  @Test
  void collectsRefToObjectType() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Address:
                    type: object
                    properties:
                      city:
                        type: string
                type: object
                properties:
                  address:
                    $ref: '#/$defs/Address'
                """);
    assertEquals(2, model.classes().size());
    JavaField addressField =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Address", addressField.javaType());
  }

  @Test
  void collectsRefToStringEnum() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Status:
                    type: string
                    enum:
                      - active
                      - inactive
                type: object
                properties:
                  status:
                    $ref: '#/$defs/Status'
                """);
    assertEquals(1, model.enums().size());
    assertEquals("Status", model.enums().get(0).enumName());
    assertEquals(List.of("ACTIVE", "INACTIVE"), model.enums().get(0).values());

    JavaField statusField = model.classes().get(0).fields().get(0);
    assertEquals("Status", statusField.javaType());
  }

  @Test
  void collectsInlineEnum() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  color:
                    type: string
                    enum:
                      - red
                      - green
                      - blue
                """);
    JavaClass root = model.classes().get(0);
    assertEquals(1, root.enums().size());
    assertEquals("Color", root.enums().get(0).enumName());
    assertEquals(List.of("RED", "GREEN", "BLUE"), root.enums().get(0).values());
    assertEquals("Color", root.fields().get(0).javaType());
  }

  @Test
  void collectsArrayOfStrings() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  tags:
                    type: array
                    items:
                      type: string
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("List<String>", field.javaType());
  }

  @Test
  void collectsArrayOfRefs() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Item:
                    type: object
                    properties:
                      name:
                        type: string
                type: object
                properties:
                  items:
                    type: array
                    items:
                      $ref: '#/$defs/Item'
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("List<Item>", field.javaType());
  }

  @Test
  void collectsNullableInteger() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  age:
                    type:
                      - integer
                      - "null"
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("Integer", field.javaType());
    assertTrue(field.nullable());
  }

  @Test
  void collectsNullableString() {
    GeneratorModel model =
        collect(
            """
                type: object
                properties:
                  nickname:
                    type:
                      - string
                      - "null"
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("String", field.javaType());
    assertTrue(field.nullable());
  }

  @Test
  void deduplicatesEnumRefsUsedMultipleTimes() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Status:
                    type: string
                    enum:
                      - on
                      - off
                type: object
                properties:
                  status1:
                    $ref: '#/$defs/Status'
                  status2:
                    $ref: '#/$defs/Status'
                """);
    assertEquals(1, model.enums().size());
  }

  @Test
  void flattensAllOfProperties() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  NameFields:
                    type: object
                    properties:
                      first-name:
                        type: string
                      last-name:
                        type: string
                  AgeFields:
                    type: object
                    properties:
                      age:
                        type: integer
                type: object
                allOf:
                  - $ref: '#/$defs/NameFields'
                  - $ref: '#/$defs/AgeFields'
                  - type: object
                    properties:
                      email:
                        type: string
                """);
    JavaClass root =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow();
    assertEquals(4, root.fields().size());
    assertTrue(root.fields().stream().anyMatch(f -> f.fieldName().equals("firstName")));
    assertTrue(root.fields().stream().anyMatch(f -> f.fieldName().equals("lastName")));
    assertTrue(root.fields().stream().anyMatch(f -> f.fieldName().equals("age")));
    assertTrue(root.fields().stream().anyMatch(f -> f.fieldName().equals("email")));
  }

  @Test
  void collectsObjectWithAllOfInProperty() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  baseSettings:
                    type: object
                    properties:
                      language:
                        type: string
                      notifications:
                        type: boolean
                type: object
                properties:
                  settings:
                    type: object
                    allOf:
                      - $ref: '#/$defs/baseSettings'
                      - properties:
                          theme:
                            type: string
                            enum: [light, dark]
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Settings", field.javaType());

    JavaClass settingsClass =
        model.classes().stream()
            .filter(c -> c.className().equals("Settings"))
            .findFirst()
            .orElseThrow();
    assertEquals(3, settingsClass.fields().size());
    assertTrue(settingsClass.fields().stream().anyMatch(f -> f.fieldName().equals("language")));
    assertTrue(
        settingsClass.fields().stream().anyMatch(f -> f.fieldName().equals("notifications")));
    assertTrue(settingsClass.fields().stream().anyMatch(f -> f.fieldName().equals("theme")));

    assertEquals(1, settingsClass.enums().size());
    assertEquals("Theme", settingsClass.enums().get(0).enumName());
  }

  @Test
  void collectsRefToPrimitiveString() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  UserId:
                    type: string
                    description: User UUID
                type: object
                properties:
                  userId:
                    $ref: '#/$defs/UserId'
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("String", field.javaType());
  }

  @Test
  void collectsInlineAllOfProperty() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  UserRef:
                    type: object
                    required: [ id ]
                    properties:
                      id:
                        type: string
                type: object
                properties:
                  manager:
                    allOf:
                      - $ref: '#/$defs/UserRef'
                      - description: Manager of the user
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Manager", field.javaType());

    JavaClass managerClass =
        model.classes().stream()
            .filter(c -> c.className().equals("Manager"))
            .findFirst()
            .orElseThrow();
    assertEquals(1, managerClass.fields().size());
    assertEquals("id", managerClass.fields().get(0).fieldName());
    assertEquals("String", managerClass.fields().get(0).javaType());
  }

  @Test
  void collectsOneOfPropertyAsObjectWithTypes() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  CreditCard:
                    type: object
                    properties:
                      cardNumber:
                        type: string
                  BankTransfer:
                    type: object
                    properties:
                      iban:
                        type: string
                type: object
                properties:
                  payment:
                    oneOf:
                      - $ref: '#/$defs/CreditCard'
                      - $ref: '#/$defs/BankTransfer'
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Object", field.javaType());
    assertEquals(List.of("CreditCard", "BankTransfer"), field.oneOfTypes());
  }

  @Test
  void collectsAnyOfPropertyWithRefsAsObjectWithTypes() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  CreditCard:
                    type: object
                    properties:
                      cardNumber:
                        type: string
                  BankTransfer:
                    type: object
                    properties:
                      iban:
                        type: string
                type: object
                properties:
                  payment:
                    anyOf:
                      - $ref: '#/$defs/CreditCard'
                      - $ref: '#/$defs/BankTransfer'
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Object", field.javaType());
    assertEquals(List.of("CreditCard", "BankTransfer"), field.oneOfTypes());
  }

  @Test
  void collectsAnyOfPropertyWithMixedTypesAsPlainObject() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Duration:
                    type: object
                    properties:
                      days:
                        type: integer
                type: object
                properties:
                  timeout:
                    anyOf:
                      - $ref: '#/$defs/Duration'
                      - type: string
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Object", field.javaType());
    assertEquals(List.of(), field.oneOfTypes());
  }

  @Test
  void collectsOneOfPropertyWithMixedTypesAsPlainObject() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Inline:
                    type: object
                    properties:
                      value:
                        type: string
                type: object
                properties:
                  config:
                    oneOf:
                      - $ref: '#/$defs/Inline'
                      - type: string
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Object", field.javaType());
    assertEquals(List.of(), field.oneOfTypes());
  }

  @Test
  void collectsAnyOfInDefsAsObject() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Fast:
                    type: object
                    properties:
                      speed:
                        type: integer
                  Slow:
                    type: object
                    properties:
                      delay:
                        type: integer
                  mode:
                    anyOf:
                      - $ref: '#/$defs/Fast'
                      - $ref: '#/$defs/Slow'
                type: object
                properties:
                  runMode:
                    $ref: '#/$defs/mode'
                """);
    JavaField field =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow()
            .fields()
            .get(0);
    assertEquals("Object", field.javaType());
  }

  @Test
  void collectsAllOfRequiredFromMultipleEntries() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Base:
                    type: object
                    required: [id]
                    properties:
                      id:
                        type: string
                type: object
                allOf:
                  - $ref: '#/$defs/Base'
                  - type: object
                    required: [name]
                    properties:
                      name:
                        type: string
                      age:
                        type: integer
                """);
    JavaClass root =
        model.classes().stream()
            .filter(c -> c.className().equals("Root"))
            .findFirst()
            .orElseThrow();
    JavaField idField =
        root.fields().stream().filter(f -> f.fieldName().equals("id")).findFirst().orElseThrow();
    JavaField nameField =
        root.fields().stream().filter(f -> f.fieldName().equals("name")).findFirst().orElseThrow();
    JavaField ageField =
        root.fields().stream().filter(f -> f.fieldName().equals("age")).findFirst().orElseThrow();
    assertTrue(idField.required(), "id should be required (from Base)");
    assertTrue(nameField.required(), "name should be required (from inline allOf)");
    assertFalse(ageField.required(), "age should not be required");
  }

  @Test
  void collectsRefToPrimitiveInteger() {
    GeneratorModel model =
        collect(
            """
                $defs:
                  Count:
                    type: integer
                type: object
                properties:
                  count:
                    $ref: '#/$defs/Count'
                """);
    JavaField field = model.classes().get(0).fields().get(0);
    assertEquals("int", field.javaType());
  }
}
