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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.treblereel.gwt.json.generator.model.GeneratorModel;
import org.treblereel.yaml.schema.Parser;
import org.treblereel.yaml.schema.model.SchemaDefinition;

public class Main {

  public static void main(String[] args) {
    int exitCode = run(args);
    if (exitCode != 0) {
      throw new RuntimeException("Code generation failed (exit code " + exitCode + ")");
    }
  }

  static int run(String[] args) {
    if (args.length != 3) {
      System.err.println("Usage: Main <schema.yaml> <package-name> <output-dir>");
      return 1;
    }

    String schemaPath = args[0];
    String packageName = args[1];
    String outputDir = args[2];

    File schemaFile = new File(schemaPath);
    if (!schemaFile.exists()) {
      System.err.println("Schema file not found: " + schemaPath);
      return 1;
    }

    Path outPath = Path.of(outputDir);
    if (outPath.toFile().exists() && !Files.isWritable(outPath)) {
      System.err.println("Cannot write to output directory: " + outputDir);
      return 1;
    }

    try {
      SchemaDefinition schema = Parser.builder().build().parse(schemaFile);
      GeneratorModel model = new SchemaCollector(packageName).collect(schema);
      new JavaEmitter(outPath).emit(model);
      return 0;
    } catch (Exception e) {
      e.printStackTrace(System.err);
      return 1;
    }
  }
}
