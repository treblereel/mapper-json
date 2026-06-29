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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.treblereel.gwt.json.generator.model.*;

class JavaEmitterTest {

  @Test
  void rendersSimpleClass() {
    JavaClass jc =
        new JavaClass(
            "Person",
            "com.example",
            List.of(
                new JavaField("name", "name", "String", true, false, false, null),
                new JavaField("age", "age", "int", false, false, false, null)),
            List.of());

    String result = new JavaEmitter(Path.of("/tmp")).renderClass(jc);

    assertTrue(result.contains("package com.example;"));
    assertTrue(result.contains("import org.treblereel.gwt.json.mapper.annotation.JSONMapper;"));
    assertTrue(result.contains("@JSONMapper"));
    assertTrue(result.contains("public class Person {"));
    assertTrue(result.contains("private String name;"));
    assertTrue(result.contains("private int age;"));
    assertTrue(result.contains("public String getName()"));
    assertTrue(result.contains("public void setName(String name)"));
    assertTrue(result.contains("public int getAge()"));
    assertTrue(result.contains("public void setAge(int age)"));
  }

  @Test
  void rendersJsonbPropertyAnnotation() {
    JavaClass jc =
        new JavaClass(
            "Address",
            "com.example",
            List.of(new JavaField("zipCode", "zip-code", "String", false, false, true, null)),
            List.of());

    String result = new JavaEmitter(Path.of("/tmp")).renderClass(jc);

    assertTrue(result.contains("import jakarta.json.bind.annotation.JsonbProperty;"));
    assertTrue(result.contains("@JsonbProperty(\"zip-code\")"));
  }

  @Test
  void doesNotImportJsonbPropertyWhenNotNeeded() {
    JavaClass jc =
        new JavaClass(
            "Simple",
            "com.example",
            List.of(new JavaField("name", "name", "String", false, false, false, null)),
            List.of());

    String result = new JavaEmitter(Path.of("/tmp")).renderClass(jc);

    assertFalse(result.contains("JsonbProperty"));
  }

  @Test
  void importsListWhenUsed() {
    JavaClass jc =
        new JavaClass(
            "Container",
            "com.example",
            List.of(new JavaField("items", "items", "List<String>", false, false, false, null)),
            List.of());

    String result = new JavaEmitter(Path.of("/tmp")).renderClass(jc);

    assertTrue(result.contains("import java.util.List;"));
  }

  @Test
  void rendersEnum() {
    JavaEnum je = new JavaEnum("Status", "com.example", List.of("ACTIVE", "INACTIVE", "PENDING"));

    String result = new JavaEmitter(Path.of("/tmp")).renderEnum(je);

    assertTrue(result.contains("package com.example;"));
    assertTrue(result.contains("public enum Status {"));
    assertTrue(result.contains("ACTIVE,"));
    assertTrue(result.contains("INACTIVE,"));
    assertTrue(result.contains("PENDING;"));
  }

  @Test
  void rendersOneOfFieldWithSerializerAnnotations() {
    JavaClass jc =
        new JavaClass(
            "Order",
            "com.example",
            List.of(
                new JavaField(
                    "payment",
                    "payment",
                    "Object",
                    false,
                    false,
                    false,
                    null,
                    List.of("CreditCard", "BankTransfer"))),
            List.of());

    String result = new JavaEmitter(Path.of("/tmp")).renderClass(jc);

    assertTrue(result.contains("import jakarta.json.bind.annotation.JsonbTypeSerializer;"));
    assertTrue(result.contains("import jakarta.json.bind.annotation.JsonbTypeDeserializer;"));
    assertTrue(result.contains("@JsonbTypeSerializer(PaymentSerializer.class)"));
    assertTrue(result.contains("@JsonbTypeDeserializer(PaymentDeserializer.class)"));
    assertTrue(result.contains("private Object payment;"));
  }

  @Test
  void rendersOneOfSerializer() {
    JavaEmitter emitter = new JavaEmitter(Path.of("/tmp"));
    String result =
        emitter.renderOneOfSerializer(
            "com.example", "payment", List.of("CreditCard", "BankTransfer"));

    assertTrue(result.contains("package com.example;"));
    assertTrue(
        result.contains(
            "import org.treblereel.gwt.json.mapper.internal.serializer.JsonbSubtypeSerializer;"));
    assertTrue(
        result.contains("public class PaymentSerializer extends JsonbSubtypeSerializer<Object>"));
    assertTrue(result.contains("super(\"@type\""));
    assertTrue(
        result.contains(
            "new Info(\"CreditCard\", CreditCard.class, new CreditCard_JsonSerializerImpl())"));
    assertTrue(
        result.contains(
            "new Info(\"BankTransfer\", BankTransfer.class, new BankTransfer_JsonSerializerImpl())"));
  }

  @Test
  void rendersOneOfDeserializer() {
    JavaEmitter emitter = new JavaEmitter(Path.of("/tmp"));
    String result =
        emitter.renderOneOfDeserializer(
            "com.example", "payment", List.of("CreditCard", "BankTransfer"));

    assertTrue(result.contains("package com.example;"));
    assertTrue(
        result.contains(
            "import org.treblereel.gwt.json.mapper.internal.deserializer.JsonbSubtypeDeserializer;"));
    assertTrue(result.contains("import org.treblereel.gwt.json.mapper.internal.Pair;"));
    assertTrue(
        result.contains(
            "public class PaymentDeserializer extends JsonbSubtypeDeserializer<Object>"));
    assertTrue(result.contains("super(\"@type\""));
    assertTrue(
        result.contains("new Pair<>(\"CreditCard\", new CreditCard_JsonDeserializerImpl())"));
    assertTrue(
        result.contains("new Pair<>(\"BankTransfer\", new BankTransfer_JsonDeserializerImpl())"));
  }

  @Test
  void emitWritesSerializerFilesForOneOfField(@TempDir Path tempDir) throws IOException {
    GeneratorModel model =
        new GeneratorModel(
            List.of(
                new JavaClass(
                    "Order",
                    "com.example",
                    List.of(
                        new JavaField(
                            "payment",
                            "payment",
                            "Object",
                            false,
                            false,
                            false,
                            null,
                            List.of("CreditCard", "BankTransfer"))),
                    List.of())),
            List.of());

    new JavaEmitter(tempDir).emit(model);

    assertTrue(Files.exists(tempDir.resolve("com/example/PaymentSerializer.java")));
    assertTrue(Files.exists(tempDir.resolve("com/example/PaymentDeserializer.java")));
  }

  @Test
  void writesFilesToOutputDir(@TempDir Path tempDir) throws IOException {
    GeneratorModel model =
        new GeneratorModel(
            List.of(
                new JavaClass(
                    "Person",
                    "com.example",
                    List.of(new JavaField("name", "name", "String", true, false, false, null)),
                    List.of())),
            List.of(new JavaEnum("Status", "com.example", List.of("ACTIVE", "INACTIVE"))));

    new JavaEmitter(tempDir).emit(model);

    Path personFile = tempDir.resolve("com/example/Person.java");
    Path statusFile = tempDir.resolve("com/example/Status.java");
    assertTrue(Files.exists(personFile));
    assertTrue(Files.exists(statusFile));
    String personContent = Files.readString(personFile);
    assertTrue(personContent.contains("public class Person"));
    String statusContent = Files.readString(statusFile);
    assertTrue(statusContent.contains("public enum Status"));
  }
}
