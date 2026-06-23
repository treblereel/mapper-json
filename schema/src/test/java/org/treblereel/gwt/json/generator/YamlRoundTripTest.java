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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.treblereel.gwt.json.generator.workflow.Root;
import org.treblereel.gwt.json.generator.workflow.Root_JsonMapperImpl;

class YamlRoundTripTest {

  private static final ObjectMapper YAML_MAPPER = new ObjectMapper(new YAMLFactory());
  private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

  static Stream<String> yamlFiles() throws IOException {
    Path dir = Paths.get("src/test/resources/workflows");
    try (Stream<Path> paths = Files.list(dir)) {
      return paths
          .filter(p -> p.toString().endsWith(".yaml"))
          .map(p -> p.getFileName().toString())
          .sorted()
          .toList()
          .stream();
    }
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("yamlFiles")
  void roundTrip(String yamlFile) throws Exception {
    JsonNode originalTree = loadYamlAsJson(yamlFile);
    String originalJson = JSON_MAPPER.writeValueAsString(originalTree);

    Root deserialized = Root_JsonMapperImpl.INSTANCE.fromJSON(originalJson);
    assertNotNull(deserialized, "Deserialization returned null for " + yamlFile);

    String roundTrippedJson = Root_JsonMapperImpl.INSTANCE.toJSON(deserialized);
    JsonNode roundTrippedTree = JSON_MAPPER.readTree(roundTrippedJson);

    Set<String> originalPaths = new TreeSet<>();
    collectPaths(originalTree, "", originalPaths);

    Set<String> roundTrippedPaths = new TreeSet<>();
    collectPaths(roundTrippedTree, "", roundTrippedPaths);

    Set<String> preserved = new TreeSet<>(originalPaths);
    preserved.retainAll(roundTrippedPaths);

    Set<String> lost = new TreeSet<>(originalPaths);
    lost.removeAll(roundTrippedPaths);

    Set<String> added = new TreeSet<>(roundTrippedPaths);
    added.removeAll(originalPaths);

    System.out.println("=== " + yamlFile + " ===");
    System.out.println("Original paths:      " + originalPaths.size());
    System.out.println("Preserved paths:     " + preserved.size());
    System.out.println("Lost paths:          " + lost.size());
    if (!lost.isEmpty()) {
      System.out.println("  LOST:");
      lost.forEach(p -> System.out.println("    - " + p));
    }
    if (!added.isEmpty()) {
      System.out.println("  ADDED:");
      added.forEach(p -> System.out.println("    - " + p));
    }

    double coverage =
        originalPaths.isEmpty() ? 100.0 : (preserved.size() * 100.0 / originalPaths.size());
    System.out.printf("Coverage:            %.1f%%%n%n", coverage);

    assertTrue(lost.isEmpty(), yamlFile + ": lost paths:\n  " + String.join("\n  ", lost));
    assertEquals(
        originalTree,
        roundTrippedTree,
        yamlFile + ": round-tripped JSON tree differs from original");
  }

  private JsonNode loadYamlAsJson(String yamlFile) throws IOException {
    try (InputStream is = getClass().getResourceAsStream("/workflows/" + yamlFile)) {
      assertNotNull(is, "YAML file not found: " + yamlFile);
      return YAML_MAPPER.readTree(is);
    }
  }

  private void collectPaths(JsonNode node, String prefix, Set<String> paths) {
    if (node.isObject()) {
      Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> field = fields.next();
        String path = prefix.isEmpty() ? field.getKey() : prefix + "." + field.getKey();
        paths.add(path);
        collectPaths(field.getValue(), path, paths);
      }
    } else if (node.isArray()) {
      for (int i = 0; i < node.size(); i++) {
        String path = prefix + "[" + i + "]";
        paths.add(path);
        collectPaths(node.get(i), path, paths);
      }
    } else {
      paths.add(prefix);
    }
  }
}
