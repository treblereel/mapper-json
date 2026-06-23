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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class MainTest {

  @Test
  void failsWithNoArgs() {
    PrintStream origErr = System.err;
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));
    try {
      int exitCode = Main.run(new String[] {});
      assertEquals(1, exitCode);
      assertTrue(errContent.toString().contains("Usage:"));
    } finally {
      System.setErr(origErr);
    }
  }

  @Test
  void failsWithMissingSchemaFile() {
    PrintStream origErr = System.err;
    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));
    try {
      int exitCode = Main.run(new String[] {"/nonexistent.yaml", "com.example", "/tmp/out"});
      assertEquals(1, exitCode);
      assertTrue(errContent.toString().contains("Schema file not found"));
    } finally {
      System.setErr(origErr);
    }
  }

  @Test
  void generatesFilesFromSchema(@TempDir Path tempDir) throws Exception {
    Path schemaPath =
        Path.of(MainTest.class.getClassLoader().getResource("schemas/simple.yaml").toURI());

    int exitCode =
        Main.run(new String[] {schemaPath.toString(), "com.example", tempDir.toString()});

    assertEquals(0, exitCode);
    assertTrue(Files.exists(tempDir.resolve("com/example/Root.java")));
    assertTrue(Files.exists(tempDir.resolve("com/example/Address.java")));

    String rootContent = Files.readString(tempDir.resolve("com/example/Root.java"));
    assertTrue(rootContent.contains("@JSONMapper"));
    assertTrue(rootContent.contains("private String name;"));
    assertTrue(rootContent.contains("private Address address;"));
  }
}
