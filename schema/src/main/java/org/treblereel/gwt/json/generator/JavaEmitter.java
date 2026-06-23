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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.treblereel.gwt.json.generator.model.*;

public class JavaEmitter {

  private final Path outputDir;

  public JavaEmitter(Path outputDir) {
    this.outputDir = outputDir;
  }

  public void emit(GeneratorModel model) throws IOException {
    Set<String> packagesNeedingObjectSerializer = new java.util.HashSet<>();
    for (JavaClass jc : model.classes()) {
      writeFile(jc.packageName(), jc.className(), renderClass(jc));
      for (JavaEnum je : jc.enums()) {
        writeFile(je.packageName(), je.enumName(), renderEnum(je));
      }
      for (JavaField field : jc.fields()) {
        if (!field.oneOfTypes().isEmpty()) {
          String capName = NamingUtils.toPascalCase(field.fieldName());
          writeFile(
              jc.packageName(),
              capName + "Serializer",
              renderOneOfSerializer(jc.packageName(), field.fieldName(), field.oneOfTypes()));
          writeFile(
              jc.packageName(),
              capName + "Deserializer",
              renderOneOfDeserializer(jc.packageName(), field.fieldName(), field.oneOfTypes()));
        }
      }
      boolean hasObjectFields =
          jc.fields().stream()
              .anyMatch(
                  f ->
                      f.oneOfTypes().isEmpty()
                          && (f.javaType().equals("Object") || f.javaType().contains("<Object>")));
      if (hasObjectFields) {
        packagesNeedingObjectSerializer.add(jc.packageName());
      }
    }
    for (String pkg : packagesNeedingObjectSerializer) {
      writeFile(pkg, "ObjectJsonSerializer", renderObjectSerializer(pkg));
      writeFile(pkg, "ObjectJsonDeserializer", renderObjectDeserializer(pkg));
    }
    for (JavaEnum je : model.enums()) {
      writeFile(je.packageName(), je.enumName(), renderEnum(je));
    }
  }

  String renderClass(JavaClass jc) {
    Set<String> imports = collectImports(jc);
    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(jc.packageName()).append(";\n\n");
    for (String imp : imports) {
      sb.append("import ").append(imp).append(";\n");
    }
    if (!imports.isEmpty()) sb.append("\n");
    sb.append("@JSONMapper\n");
    sb.append("public class ").append(jc.className()).append(" {\n\n");
    for (JavaField field : jc.fields()) {
      if (field.needsJsonbProperty()) {
        sb.append("    @JsonbProperty(\"").append(field.schemaName()).append("\")\n");
      }
      if (!field.oneOfTypes().isEmpty()) {
        String capName = NamingUtils.toPascalCase(field.fieldName());
        sb.append("    @JsonbTypeSerializer(").append(capName).append("Serializer.class)\n");
        sb.append("    @JsonbTypeDeserializer(").append(capName).append("Deserializer.class)\n");
      } else if (field.javaType().equals("Object") || field.javaType().contains("<Object>")) {
        sb.append("    @JsonbTypeSerializer(ObjectJsonSerializer.class)\n");
        sb.append("    @JsonbTypeDeserializer(ObjectJsonDeserializer.class)\n");
      }
      sb.append("    private ")
          .append(field.javaType())
          .append(" ")
          .append(field.fieldName())
          .append(";\n\n");
    }
    for (JavaField field : jc.fields()) {
      String capName =
          Character.toUpperCase(field.fieldName().charAt(0)) + field.fieldName().substring(1);
      String getterPrefix = field.javaType().equals("boolean") ? "is" : "get";
      sb.append("    public ")
          .append(field.javaType())
          .append(" ")
          .append(getterPrefix)
          .append(capName)
          .append("() {\n");
      sb.append("        return ").append(field.fieldName()).append(";\n");
      sb.append("    }\n\n");
      sb.append("    public void set")
          .append(capName)
          .append("(")
          .append(field.javaType())
          .append(" ")
          .append(field.fieldName())
          .append(") {\n");
      sb.append("        this.")
          .append(field.fieldName())
          .append(" = ")
          .append(field.fieldName())
          .append(";\n");
      sb.append("    }\n\n");
    }
    sb.append("}\n");
    return sb.toString();
  }

  String renderEnum(JavaEnum je) {
    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(je.packageName()).append(";\n\n");
    sb.append("public enum ").append(je.enumName()).append(" {\n");
    List<String> values = je.values();
    for (int i = 0; i < values.size(); i++) {
      sb.append("    ").append(values.get(i));
      sb.append(i < values.size() - 1 ? ",\n" : ";\n");
    }
    sb.append("}\n");
    return sb.toString();
  }

  String renderOneOfSerializer(String packageName, String fieldName, List<String> oneOfTypes) {
    String capName = NamingUtils.toPascalCase(fieldName);
    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(packageName).append(";\n\n");
    sb.append(
        "import org.treblereel.gwt.json.mapper.internal.serializer.JsonbSubtypeSerializer;\n\n");
    sb.append("public class ")
        .append(capName)
        .append("Serializer extends JsonbSubtypeSerializer<Object> {\n\n");
    sb.append("    public ").append(capName).append("Serializer() {\n");
    sb.append("        super(\"@type\"");
    for (String typeName : oneOfTypes) {
      sb.append(",\n            new Info(\"")
          .append(typeName)
          .append("\", ")
          .append(typeName)
          .append(".class, new ")
          .append(typeName)
          .append("_JsonSerializerImpl())");
    }
    sb.append("\n        );\n");
    sb.append("    }\n");
    sb.append("}\n");
    return sb.toString();
  }

  String renderOneOfDeserializer(String packageName, String fieldName, List<String> oneOfTypes) {
    String capName = NamingUtils.toPascalCase(fieldName);
    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(packageName).append(";\n\n");
    sb.append(
        "import org.treblereel.gwt.json.mapper.internal.deserializer.JsonbSubtypeDeserializer;\n");
    sb.append("import org.treblereel.gwt.json.mapper.internal.Pair;\n\n");
    sb.append("public class ")
        .append(capName)
        .append("Deserializer extends JsonbSubtypeDeserializer<Object> {\n\n");
    sb.append("    public ").append(capName).append("Deserializer() {\n");
    sb.append("        super(\"@type\"");
    for (String typeName : oneOfTypes) {
      sb.append(",\n            new Pair<>(\"")
          .append(typeName)
          .append("\", new ")
          .append(typeName)
          .append("_JsonDeserializerImpl())");
    }
    sb.append("\n        );\n");
    sb.append("    }\n");
    sb.append("}\n");
    return sb.toString();
  }

  private Set<String> collectImports(JavaClass jc) {
    Set<String> imports = new LinkedHashSet<>();
    imports.add("org.treblereel.gwt.json.mapper.annotation.JSONMapper");
    boolean needsJsonbProperty = jc.fields().stream().anyMatch(JavaField::needsJsonbProperty);
    if (needsJsonbProperty) {
      imports.add("jakarta.json.bind.annotation.JsonbProperty");
    }
    boolean needsTypeSerializer =
        jc.fields().stream()
            .anyMatch(
                f ->
                    !f.oneOfTypes().isEmpty()
                        || f.javaType().equals("Object")
                        || f.javaType().contains("<Object>"));
    if (needsTypeSerializer) {
      imports.add("jakarta.json.bind.annotation.JsonbTypeSerializer");
      imports.add("jakarta.json.bind.annotation.JsonbTypeDeserializer");
    }

    boolean needsList = jc.fields().stream().anyMatch(f -> f.javaType().startsWith("List<"));
    if (needsList) {
      imports.add("java.util.List");
    }
    return imports;
  }

  String renderObjectSerializer(String packageName) {
    StringBuilder sb = new StringBuilder();
    sb.append("package ").append(packageName).append(";\n\n");
    sb.append("import jakarta.json.*;\n");
    sb.append("import jakarta.json.bind.serializer.SerializationContext;\n");
    sb.append("import jakarta.json.stream.JsonGenerator;\n");
    sb.append("import jakarta.json.stream.ContextedJsonGenerator;\n");
    sb.append("import org.treblereel.gwt.json.mapper.internal.serializer.JsonSerializer;\n\n");
    sb.append("public class ObjectJsonSerializer extends JsonSerializer<Object> {\n\n");
    // Named variant: called for direct object fields (e.g. Root.metadata)
    sb.append("    @Override\n");
    sb.append(
        "    public void serialize(Object obj, String property, JsonGenerator generator, SerializationContext ctx) {\n");
    sb.append("        if (obj == null) return;\n");
    sb.append("        if (obj instanceof JsonObject jo) {\n");
    sb.append("            JsonGenerator nested = generator.writeStartObject(property);\n");
    sb.append("            jo.forEach((k, v) -> writeNamedJsonValue(k, v, nested));\n");
    sb.append("            nested.writeEnd();\n");
    sb.append("        } else if (obj instanceof JsonArray ja) {\n");
    sb.append("            JsonGenerator arr = generator.writeStartArray(property);\n");
    sb.append("            ja.forEach(v -> writeArrayJsonValue(v, arr));\n");
    sb.append("            arr.writeEnd();\n");
    sb.append("        } else if (obj instanceof JsonValue jv) {\n");
    sb.append("            writeNamedJsonPrimitive(property, jv, generator);\n");
    sb.append("        } else {\n");
    sb.append("            generator.write(property, String.valueOf(obj));\n");
    sb.append("        }\n");
    sb.append("    }\n\n");
    // Positional variant: called from CollectionJsonSerializer (fresh decorator)
    // or from SerializerJsonbTypeSerializerWrapper (ContextedJsonGenerator)
    sb.append("    @Override\n");
    sb.append(
        "    public void serialize(Object obj, JsonGenerator generator, SerializationContext ctx) {\n");
    sb.append("        if (obj == null) return;\n");
    sb.append("        if (obj instanceof JsonObject jo) {\n");
    sb.append("            if (generator instanceof ContextedJsonGenerator) {\n");
    sb.append("                JsonGenerator nested = generator.writeStartObject();\n");
    sb.append("                jo.forEach((k, v) -> writeNamedJsonValue(k, v, nested));\n");
    sb.append("                nested.writeEnd();\n");
    sb.append("            } else {\n");
    sb.append("                jo.forEach((k, v) -> writeNamedJsonValue(k, v, generator));\n");
    sb.append("            }\n");
    sb.append("        } else if (generator instanceof ContextedJsonGenerator) {\n");
    sb.append("            if (obj instanceof JsonString js) generator.write(js.getString());\n");
    sb.append("            else if (obj instanceof JsonNumber jn) {\n");
    sb.append("                if (jn.isIntegral()) generator.write(jn.longValue());\n");
    sb.append("                else generator.write(jn.bigDecimalValue());\n");
    sb.append("            } else if (obj instanceof JsonValue jv) {\n");
    sb.append("                switch (jv.getValueType()) {\n");
    sb.append("                    case TRUE -> generator.write(true);\n");
    sb.append("                    case FALSE -> generator.write(false);\n");
    sb.append("                    case NULL -> generator.writeNull();\n");
    sb.append("                    case ARRAY -> generator.write(jv);\n");
    sb.append("                    default -> generator.write(jv.toString());\n");
    sb.append("                }\n");
    sb.append("            } else {\n");
    sb.append("                generator.write(String.valueOf(obj));\n");
    sb.append("            }\n");
    sb.append("        } else if (obj instanceof JsonValue jv) {\n");
    sb.append("            generator.write(\"value\", jv.toString());\n");
    sb.append("        } else {\n");
    sb.append("            generator.write(\"value\", String.valueOf(obj));\n");
    sb.append("        }\n");
    sb.append("    }\n\n");
    // Named writes for object context (JsonGeneratorDecorator / JsonNestedObjecGenerator)
    sb.append(
        "    private void writeNamedJsonValue(String name, JsonValue value, JsonGenerator generator) {\n");
    sb.append("        switch (value.getValueType()) {\n");
    sb.append("            case OBJECT -> {\n");
    sb.append("                JsonGenerator nested = generator.writeStartObject(name);\n");
    sb.append(
        "                value.asJsonObject().forEach((k, v) -> writeNamedJsonValue(k, v, nested));\n");
    sb.append("                nested.writeEnd();\n");
    sb.append("            }\n");
    sb.append("            case ARRAY -> {\n");
    sb.append("                JsonGenerator arr = generator.writeStartArray(name);\n");
    sb.append("                value.asJsonArray().forEach(v -> writeArrayJsonValue(v, arr));\n");
    sb.append("                arr.writeEnd();\n");
    sb.append("            }\n");
    sb.append(
        "            case STRING -> generator.write(name, ((JsonString) value).getString());\n");
    sb.append("            case NUMBER -> {\n");
    sb.append("                JsonNumber n = (JsonNumber) value;\n");
    sb.append("                if (n.isIntegral()) generator.write(name, n.longValue());\n");
    sb.append("                else generator.write(name, n.bigDecimalValue());\n");
    sb.append("            }\n");
    sb.append("            case TRUE -> generator.write(name, true);\n");
    sb.append("            case FALSE -> generator.write(name, false);\n");
    sb.append("            case NULL -> generator.writeNull(name);\n");
    sb.append("        }\n");
    sb.append("    }\n\n");
    // Positional writes for array context (JsonArrayGenerator)
    sb.append("    private void writeArrayJsonValue(JsonValue value, JsonGenerator generator) {\n");
    sb.append("        switch (value.getValueType()) {\n");
    sb.append("            case OBJECT, ARRAY -> generator.write(value);\n");
    sb.append("            case STRING -> generator.write(((JsonString) value).getString());\n");
    sb.append("            case NUMBER -> {\n");
    sb.append("                JsonNumber n = (JsonNumber) value;\n");
    sb.append("                if (n.isIntegral()) generator.write(n.longValue());\n");
    sb.append("                else generator.write(n.bigDecimalValue());\n");
    sb.append("            }\n");
    sb.append("            case TRUE -> generator.write(true);\n");
    sb.append("            case FALSE -> generator.write(false);\n");
    sb.append("            case NULL -> generator.writeNull();\n");
    sb.append("        }\n");
    sb.append("    }\n\n");
    sb.append(
        "    private void writeNamedJsonPrimitive(String name, JsonValue value, JsonGenerator generator) {\n");
    sb.append("        switch (value.getValueType()) {\n");
    sb.append(
        "            case STRING -> generator.write(name, ((JsonString) value).getString());\n");
    sb.append("            case NUMBER -> {\n");
    sb.append("                JsonNumber n = (JsonNumber) value;\n");
    sb.append("                if (n.isIntegral()) generator.write(name, n.longValue());\n");
    sb.append("                else generator.write(name, n.bigDecimalValue());\n");
    sb.append("            }\n");
    sb.append("            case TRUE -> generator.write(name, true);\n");
    sb.append("            case FALSE -> generator.write(name, false);\n");
    sb.append("            case NULL -> generator.writeNull(name);\n");
    sb.append("            default -> generator.write(name, value.toString());\n");
    sb.append("        }\n");
    sb.append("    }\n");
    sb.append("}\n");
    return sb.toString();
  }

  String renderObjectDeserializer(String packageName) {
    return "package "
        + packageName
        + ";\n\n"
        + "import jakarta.json.JsonValue;\n"
        + "import jakarta.json.bind.serializer.DeserializationContext;\n"
        + "import org.treblereel.gwt.json.mapper.internal.deserializer.JsonbDeserializer;\n\n"
        + "public class ObjectJsonDeserializer extends JsonbDeserializer<Object> {\n\n"
        + "    @Override\n"
        + "    public Object deserialize(JsonValue value, DeserializationContext ctx) {\n"
        + "        return value;\n"
        + "    }\n"
        + "}\n";
  }

  private void writeFile(String packageName, String typeName, String content) throws IOException {
    Path packageDir = outputDir.resolve(packageName.replace('.', '/'));
    Files.createDirectories(packageDir);
    Files.writeString(packageDir.resolve(typeName + ".java"), content);
  }
}
