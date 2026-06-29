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

import java.io.File;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class IntegrationTest {

  @Test
  void simpleSchemaRoundTrip(@TempDir Path tempDir) throws Exception {
    generateAndCompile("schemas/simple.yaml", "com.example", tempDir);

    try (URLClassLoader loader = classLoader(tempDir)) {
      Class<?> rootClass = loader.loadClass("com.example.Root");
      Class<?> addressClass = loader.loadClass("com.example.Address");
      Object mapper = loadMapper(loader, "com.example.Root_JsonMapperImpl");

      Object address = addressClass.getDeclaredConstructor().newInstance();
      addressClass.getMethod("setStreet", String.class).invoke(address, "123 Main St");
      addressClass.getMethod("setCity", String.class).invoke(address, "Springfield");

      Object root = rootClass.getDeclaredConstructor().newInstance();
      rootClass.getMethod("setName", String.class).invoke(root, "John");
      rootClass.getMethod("setAddress", addressClass).invoke(root, address);

      String json = toJSON(mapper, root);
      assertTrue(json.contains("\"name\":\"John\""));
      assertTrue(json.contains("\"street\":\"123 Main St\""));

      Object deserialized = fromJSON(mapper, json);
      assertEquals(json, toJSON(mapper, deserialized));
    }
  }

  @Test
  void complexSchemaRoundTrip(@TempDir Path tempDir) throws Exception {
    generateAndCompile("schemas/complex.yaml", "com.example", tempDir);

    try (URLClassLoader loader = classLoader(tempDir)) {
      Class<?> rootClass = loader.loadClass("com.example.Root");
      Class<?> addressClass = loader.loadClass("com.example.Address");
      Class<?> statusClass = loader.loadClass("com.example.Status");
      Object mapper = loadMapper(loader, "com.example.Root_JsonMapperImpl");

      Object address = addressClass.getDeclaredConstructor().newInstance();
      addressClass.getMethod("setStreet", String.class).invoke(address, "42 Oak Ave");
      addressClass.getMethod("setCity", String.class).invoke(address, "Shelbyville");

      Object statusActive = statusClass.getMethod("valueOf", String.class).invoke(null, "ACTIVE");

      Object root = rootClass.getDeclaredConstructor().newInstance();
      rootClass.getMethod("setName", String.class).invoke(root, "Jane");
      rootClass.getMethod("setAge", Integer.class).invoke(root, 30);
      rootClass.getMethod("setAddress", addressClass).invoke(root, address);
      rootClass.getMethod("setStatus", statusClass).invoke(root, statusActive);
      rootClass.getMethod("setTags", List.class).invoke(root, List.of("dev", "admin"));

      String json = toJSON(mapper, root);
      assertTrue(json.contains("\"name\":\"Jane\""));
      assertTrue(json.contains("\"age\":30"));
      assertTrue(json.contains("\"status\":\"ACTIVE\""));
      assertTrue(json.contains("\"tags\":[\"dev\",\"admin\"]"));

      Object deserialized = fromJSON(mapper, json);
      assertEquals(json, toJSON(mapper, deserialized));
    }
  }

  @Test
  void fullSchemaRoundTrip(@TempDir Path tempDir) throws Exception {
    generateAndCompile("schemas/full.yaml", "com.example", tempDir);

    try (URLClassLoader loader = classLoader(tempDir)) {
      Class<?> rootClass = loader.loadClass("com.example.TestUserSchema");
      Class<?> roleClass = loader.loadClass("com.example.Role");
      Object mapper = loadMapper(loader, "com.example.TestUserSchema_JsonMapperImpl");

      Object roleUser = roleClass.getMethod("valueOf", String.class).invoke(null, "USER");

      Object root = rootClass.getDeclaredConstructor().newInstance();
      rootClass.getMethod("setId", String.class).invoke(root, "abc-123");
      rootClass.getMethod("setName", String.class).invoke(root, "Alice");
      rootClass.getMethod("setEmail", String.class).invoke(root, "alice@example.com");
      rootClass.getMethod("setRole", roleClass).invoke(root, roleUser);
      rootClass.getMethod("setAge", int.class).invoke(root, 25);
      rootClass.getMethod("setSalary", double.class).invoke(root, 50000.0);
      rootClass.getMethod("setTags", List.class).invoke(root, List.of("java", "maven"));

      String json = toJSON(mapper, root);
      assertTrue(json.contains("\"name\":\"Alice\""));
      assertTrue(json.contains("\"role\":\"USER\""));
      assertTrue(json.contains("\"age\":25"));

      Object deserialized = fromJSON(mapper, json);
      assertEquals(json, toJSON(mapper, deserialized));
    }
  }

  @Test
  void oneOfSerializerFilesGenerated(@TempDir Path tempDir) throws Exception {
    Path schemaPath = resourcePath("schemas/full.yaml");
    Main.run(new String[] {schemaPath.toString(), "com.example", tempDir.toString()});

    Path packageDir = tempDir.resolve("com/example");
    assertTrue(Files.exists(packageDir.resolve("PaymentSerializer.java")));
    assertTrue(Files.exists(packageDir.resolve("PaymentDeserializer.java")));

    String serializer = Files.readString(packageDir.resolve("PaymentSerializer.java"));
    assertTrue(serializer.contains("extends JsonbSubtypeSerializer<Object>"));
    assertTrue(
        serializer.contains(
            "new Info(\"CreditCard\", CreditCard.class, new CreditCard_JsonSerializerImpl())"));

    String deserializer = Files.readString(packageDir.resolve("PaymentDeserializer.java"));
    assertTrue(deserializer.contains("extends JsonbSubtypeDeserializer<Object>"));
    assertTrue(
        deserializer.contains("new Pair<>(\"CreditCard\", new CreditCard_JsonDeserializerImpl())"));
  }

  @Test
  void oneOfSubtypeClassesCompileAndLoad(@TempDir Path tempDir) throws Exception {
    generateAndCompile("schemas/full.yaml", "com.example", tempDir);

    try (URLClassLoader loader = classLoader(tempDir)) {
      Class<?> creditCardClass = loader.loadClass("com.example.CreditCard");
      Class<?> bankTransferClass = loader.loadClass("com.example.BankTransfer");

      Object card = creditCardClass.getDeclaredConstructor().newInstance();
      creditCardClass.getMethod("setCardNumber", String.class).invoke(card, "4111111111111111");
      creditCardClass.getMethod("setExpiry", String.class).invoke(card, "12/25");
      assertEquals("4111111111111111", creditCardClass.getMethod("getCardNumber").invoke(card));

      Object transfer = bankTransferClass.getDeclaredConstructor().newInstance();
      bankTransferClass
          .getMethod("setIban", String.class)
          .invoke(transfer, "DE89370400440532013000");
      bankTransferClass.getMethod("setBic", String.class).invoke(transfer, "COBADEFFXXX");
      assertEquals(
          "DE89370400440532013000", bankTransferClass.getMethod("getIban").invoke(transfer));

      Class<?> serializerClass = loader.loadClass("com.example.PaymentSerializer");
      Class<?> deserializerClass = loader.loadClass("com.example.PaymentDeserializer");
      assertNotNull(serializerClass.getDeclaredConstructor().newInstance());
      assertNotNull(deserializerClass.getDeclaredConstructor().newInstance());
    }
  }

  @Test
  void oneOfSubtypeSerializationIncludesTypeDiscriminator(@TempDir Path tempDir) throws Exception {
    generateAndCompile("schemas/full.yaml", "com.example", tempDir);

    try (URLClassLoader loader = classLoader(tempDir)) {
      Class<?> creditCardClass = loader.loadClass("com.example.CreditCard");
      Object cardMapper = loadMapper(loader, "com.example.CreditCard_JsonMapperImpl");

      Object card = creditCardClass.getDeclaredConstructor().newInstance();
      creditCardClass.getMethod("setCardNumber", String.class).invoke(card, "4111111111111111");
      creditCardClass.getMethod("setExpiry", String.class).invoke(card, "12/25");

      String json = toJSON(cardMapper, card);
      assertTrue(
          json.contains("\"cardNumber\":\"4111111111111111\""),
          "CreditCard JSON should contain cardNumber: " + json);
      assertTrue(
          json.contains("\"expiry\":\"12/25\""), "CreditCard JSON should contain expiry: " + json);

      Object deserialized = fromJSON(cardMapper, json);
      assertEquals(
          json, toJSON(cardMapper, deserialized), "CreditCard round-trip should be lossless");
    }
  }

  @Test
  void oneOfRuntimeRoundTrip(@TempDir Path tempDir) throws Exception {
    generateAndCompile("schemas/full.yaml", "com.example", tempDir);

    try (URLClassLoader loader = classLoader(tempDir)) {
      Class<?> rootClass = loader.loadClass("com.example.TestUserSchema");
      Class<?> creditCardClass = loader.loadClass("com.example.CreditCard");
      Class<?> roleClass = loader.loadClass("com.example.Role");
      Object mapper = loadMapper(loader, "com.example.TestUserSchema_JsonMapperImpl");

      Object card = creditCardClass.getDeclaredConstructor().newInstance();
      creditCardClass.getMethod("setCardNumber", String.class).invoke(card, "4111111111111111");
      creditCardClass.getMethod("setExpiry", String.class).invoke(card, "12/25");

      Object roleUser = roleClass.getMethod("valueOf", String.class).invoke(null, "USER");

      Object root = rootClass.getDeclaredConstructor().newInstance();
      rootClass.getMethod("setId", String.class).invoke(root, "abc-123");
      rootClass.getMethod("setName", String.class).invoke(root, "Bob");
      rootClass.getMethod("setEmail", String.class).invoke(root, "bob@example.com");
      rootClass.getMethod("setRole", roleClass).invoke(root, roleUser);
      rootClass.getMethod("setPayment", Object.class).invoke(root, card);

      String json = toJSON(mapper, root);
      assertTrue(json.contains("\"payment\":{"), "payment should be nested as object: " + json);
      assertTrue(
          json.contains("\"@type\":\"CreditCard\""),
          "JSON should contain @type discriminator: " + json);
      assertTrue(
          json.contains("\"cardNumber\":\"4111111111111111\""),
          "JSON should contain cardNumber: " + json);

      Object deserialized = fromJSON(mapper, json);
      String roundTripped = toJSON(mapper, deserialized);
      assertEquals(json, roundTripped, "Round-tripped JSON should match original");

      Object deserializedPayment = rootClass.getMethod("getPayment").invoke(deserialized);
      assertNotNull(deserializedPayment, "Deserialized payment should not be null");
      assertTrue(
          creditCardClass.isInstance(deserializedPayment),
          "Deserialized payment should be CreditCard, was: "
              + deserializedPayment.getClass().getName());
      assertEquals(
          "4111111111111111",
          creditCardClass.getMethod("getCardNumber").invoke(deserializedPayment));
    }
  }

  private void generateAndCompile(String schemaResource, String packageName, Path tempDir)
      throws Exception {
    Path schemaPath = resourcePath(schemaResource);
    int exitCode = Main.run(new String[] {schemaPath.toString(), packageName, tempDir.toString()});
    assertEquals(0, exitCode, "Code generation failed");

    Path packageDir = tempDir.resolve(packageName.replace('.', '/'));

    List<String> serFiles = new ArrayList<>();
    List<String> rootFiles = new ArrayList<>();
    List<String> leafFiles = new ArrayList<>();

    try (var stream = Files.walk(packageDir)) {
      stream
          .filter(p -> p.toString().endsWith(".java"))
          .forEach(
              p -> {
                String name = p.getFileName().toString();
                if (name.contains("Serializer") || name.contains("Deserializer")) {
                  serFiles.add(p.toString());
                } else if (referencesSerializer(p)) {
                  rootFiles.add(p.toString());
                } else {
                  leafFiles.add(p.toString());
                }
              });
    }

    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    assertNotNull(compiler, "Java compiler not available");

    String fullCp = System.getProperty("java.class.path");
    String compileCp = buildCompileClasspath(fullCp, tempDir);
    String processorPath = buildProcessorPath(fullCp);

    // Pass 1: compile leaf beans with AP (generates *_JsonSerializerImpl etc.)
    List<String> pass1 = new ArrayList<>(leafFiles);
    if (serFiles.isEmpty()) pass1.addAll(rootFiles);
    int result = compile(compiler, compileCp, processorPath, tempDir, pass1);
    assertEquals(0, result, "Compilation failed (pass 1)");

    if (!serFiles.isEmpty()) {
      String extendedCp = compileCp + File.pathSeparator + tempDir;

      // Pass 2: compile serializer/deserializer files (reference AP-generated impls)
      int result2 = compileNoProc(compiler, extendedCp, tempDir, serFiles);
      assertEquals(0, result2, "Serializer compilation failed (pass 2)");

      // Pass 3: compile root beans with AP (reference serializer classes)
      int result3 = compile(compiler, extendedCp, processorPath, tempDir, rootFiles);
      assertEquals(0, result3, "Root compilation failed (pass 3)");
    }
  }

  private URLClassLoader classLoader(Path tempDir) throws Exception {
    return new URLClassLoader(new URL[] {tempDir.toUri().toURL()}, getClass().getClassLoader());
  }

  private Object loadMapper(URLClassLoader loader, String className) throws Exception {
    Class<?> mapperClass = loader.loadClass(className);
    return mapperClass.getField("INSTANCE").get(null);
  }

  private String toJSON(Object mapper, Object obj) throws Exception {
    Method method = mapper.getClass().getMethod("toJSON", Object.class);
    return (String) method.invoke(mapper, obj);
  }

  private Object fromJSON(Object mapper, String json) throws Exception {
    Method method = mapper.getClass().getMethod("fromJSON", String.class);
    return method.invoke(mapper, json);
  }

  private boolean referencesSerializer(Path file) {
    try {
      String content = Files.readString(file);
      return content.contains("@JsonbTypeSerializer") || content.contains("@JsonbTypeDeserializer");
    } catch (Exception e) {
      return false;
    }
  }

  private int compile(
      JavaCompiler compiler,
      String classpath,
      String processorPath,
      Path tempDir,
      List<String> sourceFiles) {
    List<String> args = new ArrayList<>();
    args.add("-classpath");
    args.add(classpath);
    args.add("--processor-path");
    args.add(processorPath);
    args.add("-sourcepath");
    args.add("");
    args.add("-d");
    args.add(tempDir.toString());
    args.add("-s");
    args.add(tempDir.toString());
    args.addAll(sourceFiles);

    java.io.ByteArrayOutputStream errStream = new java.io.ByteArrayOutputStream();
    int result = compiler.run(null, null, errStream, args.toArray(new String[0]));
    if (result != 0) {
      System.err.println("Compilation errors:\n" + errStream);
    }
    return result;
  }

  private int compileNoProc(
      JavaCompiler compiler, String classpath, Path tempDir, List<String> sourceFiles) {
    List<String> args = new ArrayList<>();
    args.add("-classpath");
    args.add(classpath);
    args.add("-d");
    args.add(tempDir.toString());
    args.add("-proc:none");
    args.addAll(sourceFiles);

    java.io.ByteArrayOutputStream errStream = new java.io.ByteArrayOutputStream();
    int result = compiler.run(null, null, errStream, args.toArray(new String[0]));
    if (result != 0) {
      System.err.println("Compilation errors:\n" + errStream);
    }
    return result;
  }

  private String buildCompileClasspath(String fullCp, Path tempDir) throws Exception {
    StringBuilder sb = new StringBuilder();
    Path cleanLibs = tempDir.resolve("__cleanlibs__");
    for (String entry : fullCp.split(File.pathSeparator)) {
      String name = Path.of(entry).getFileName().toString();
      if (name.contains("processor")) {
        continue;
      }
      if (hasEmbeddedSources(entry)) {
        Path cleanDir = cleanLibs.resolve(name);
        extractClassesOnly(Path.of(entry), cleanDir);
        if (sb.length() > 0) sb.append(File.pathSeparator);
        sb.append(cleanDir);
      } else {
        if (sb.length() > 0) sb.append(File.pathSeparator);
        sb.append(entry);
      }
    }
    return sb.toString();
  }

  private boolean hasEmbeddedSources(String jarPath) {
    if (!jarPath.endsWith(".jar")) return false;
    try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarPath)) {
      return jar.stream().anyMatch(e -> e.getName().endsWith(".java"));
    } catch (Exception e) {
      return false;
    }
  }

  private void extractClassesOnly(Path jarPath, Path destDir) throws Exception {
    Files.createDirectories(destDir);
    try (java.util.jar.JarFile jar = new java.util.jar.JarFile(jarPath.toFile())) {
      var entries = jar.entries();
      while (entries.hasMoreElements()) {
        var entry = entries.nextElement();
        if (entry.isDirectory() || entry.getName().endsWith(".java")) continue;
        Path target = destDir.resolve(entry.getName());
        Files.createDirectories(target.getParent());
        try (var is = jar.getInputStream(entry)) {
          Files.copy(is, target);
        }
      }
    }
  }

  private String buildProcessorPath(String fullCp) {
    StringBuilder sb = new StringBuilder();
    for (String entry : fullCp.split(File.pathSeparator)) {
      String name = Path.of(entry).getFileName().toString();
      if (name.contains("processor")
          || name.contains("auto-common")
          || name.contains("guava")
          || name.contains("javaparser")
          || name.contains("common-0")
          || name.contains("jsonb")
          || name.contains("json-impl")
          || name.contains("elemental2")
          || name.contains("jsinterop")
          || name.contains("auto-service")
          || name.contains("failureaccess")
          || name.contains("jakarta")) {
        if (sb.length() > 0) sb.append(File.pathSeparator);
        sb.append(entry);
      }
    }
    return sb.toString();
  }

  private Path resourcePath(String name) throws URISyntaxException {
    return Path.of(IntegrationTest.class.getClassLoader().getResource(name).toURI());
  }
}
