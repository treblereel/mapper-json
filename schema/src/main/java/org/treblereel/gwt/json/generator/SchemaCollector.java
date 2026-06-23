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

import java.util.*;
import java.util.stream.Collectors;
import org.treblereel.gwt.json.generator.model.*;
import org.treblereel.yaml.schema.model.*;

public class SchemaCollector {

  private final String packageName;
  private final List<JavaClass> classes = new ArrayList<>();
  private final List<JavaEnum> topLevelEnums = new ArrayList<>();
  private final Set<String> collectedEnumNames = new HashSet<>();
  private final Set<String> collectedClassNames = new HashSet<>();

  public SchemaCollector(String packageName) {
    this.packageName = packageName;
  }

  public GeneratorModel collect(SchemaDefinition schema) {
    for (Map.Entry<String, ObjectType> entry : schema.definitions().entrySet()) {
      String name = entry.getKey();
      ObjectType objType = entry.getValue();
      if (!objType.properties().isEmpty()) {
        collectedClassNames.add(NamingUtils.toPascalCase(name));
        classes.add(collectObjectType(name, objType));
      } else if (objType.allOf().isPresent()) {
        collectedClassNames.add(NamingUtils.toPascalCase(name));
        classes.add(collectAllOfType(name, objType.allOf().get(), objType));
      } else if (objType.oneOf().isPresent() || objType.anyOf().isPresent()) {
        List<HasType> alternatives = objType.oneOf().orElseGet(() -> objType.anyOf().get());
        alternatives.forEach(this::extractTypeNames);
      }
    }

    String rootName = schema.title().orElse("Root");

    Optional<ObjectType> rootObj = schema.modelAsObject();
    if (rootObj.isPresent()) {
      ObjectType obj = rootObj.get();
      if (!obj.properties().isEmpty()) {
        classes.add(collectObjectType(rootName, obj));
      } else if (obj.allOf().isPresent()) {
        classes.add(collectAllOfType(rootName, obj.allOf().get(), obj));
      }
    }

    Optional<AllOfType> rootAllOf = schema.modelAsAllOf();
    if (rootAllOf.isPresent() && rootObj.isEmpty()) {
      classes.add(collectAllOfType(rootName, rootAllOf.get().getAllOf(), null));
    }

    return new GeneratorModel(classes, topLevelEnums);
  }

  private JavaClass collectObjectType(String name, ObjectType objType) {
    List<JavaField> fields = new ArrayList<>();
    List<JavaEnum> inlineEnums = new ArrayList<>();
    Set<String> requiredNames =
        objType.required().orElse(List.of()).stream().collect(Collectors.toSet());

    for (Map.Entry<String, HasType> prop : objType.properties().entrySet()) {
      String schemaName = prop.getKey();
      HasType propType = prop.getValue();
      String fieldName = NamingUtils.sanitizeFieldName(schemaName);
      boolean isRequired = requiredNames.contains(schemaName);
      String description = extractDescription(propType);

      TypeResult typeResult = resolveJavaType(propType, schemaName, inlineEnums);

      fields.add(
          new JavaField(
              fieldName,
              schemaName,
              typeResult.javaType,
              isRequired,
              typeResult.nullable,
              NamingUtils.needsJsonbProperty(schemaName, fieldName),
              description,
              typeResult.oneOfTypes));
    }

    mergeOneOfProperties(objType, fields, inlineEnums);

    return new JavaClass(NamingUtils.toPascalCase(name), packageName, fields, inlineEnums);
  }

  private void mergeOneOfProperties(
      ObjectType objType, List<JavaField> fields, List<JavaEnum> inlineEnums) {
    List<HasType> alternatives = objType.oneOf().orElse(null);
    if (alternatives == null) alternatives = objType.anyOf().orElse(null);
    if (alternatives == null) return;

    Set<String> existing = new HashSet<>();
    for (JavaField f : fields) existing.add(f.schemaName());

    for (HasType alt : alternatives) {
      ObjectType altObj = null;
      if (alt instanceof ObjectType ot) altObj = ot;
      else if (alt instanceof RefType rt && rt.resolve() instanceof ObjectType ot) altObj = ot;
      if (altObj == null) continue;
      for (Map.Entry<String, HasType> prop : altObj.properties().entrySet()) {
        String schemaName = prop.getKey();
        if (!existing.add(schemaName)) continue;
        HasType propType = prop.getValue();
        String fieldName = NamingUtils.sanitizeFieldName(schemaName);
        String description = extractDescription(propType);
        TypeResult typeResult = resolveJavaType(propType, schemaName, inlineEnums);
        fields.add(
            new JavaField(
                fieldName,
                schemaName,
                typeResult.javaType,
                false,
                typeResult.nullable,
                NamingUtils.needsJsonbProperty(schemaName, fieldName),
                description,
                typeResult.oneOfTypes));
      }
    }
  }

  private JavaClass collectAllOfType(
      String name, List<HasType> allOfEntries, ObjectType container) {
    List<JavaField> fields = new ArrayList<>();
    List<JavaEnum> inlineEnums = new ArrayList<>();
    Set<String> requiredNames = new HashSet<>();

    if (container != null) {
      container.required().ifPresent(requiredNames::addAll);
    }

    for (HasType entry : allOfEntries) {
      ObjectType resolved = null;
      if (entry instanceof RefType rt) {
        HasType r = rt.resolve();
        if (r instanceof ObjectType ot) resolved = ot;
      } else if (entry instanceof ObjectType ot) {
        resolved = ot;
      }
      if (resolved != null) {
        resolved.required().ifPresent(requiredNames::addAll);
        for (Map.Entry<String, HasType> prop : resolved.properties().entrySet()) {
          String schemaName = prop.getKey();
          HasType propType = prop.getValue();
          String fieldName = NamingUtils.sanitizeFieldName(schemaName);
          boolean isRequired = requiredNames.contains(schemaName);
          String description = extractDescription(propType);
          TypeResult typeResult = resolveJavaType(propType, schemaName, inlineEnums);
          fields.add(
              new JavaField(
                  fieldName,
                  schemaName,
                  typeResult.javaType,
                  isRequired,
                  typeResult.nullable,
                  NamingUtils.needsJsonbProperty(schemaName, fieldName),
                  description,
                  typeResult.oneOfTypes));
        }
      }
    }

    return new JavaClass(NamingUtils.toPascalCase(name), packageName, fields, inlineEnums);
  }

  private TypeResult resolveJavaType(HasType type, String contextName, List<JavaEnum> inlineEnums) {
    if (type instanceof StringType st) {
      if (st.enumValues().isPresent() && !st.enumValues().get().isEmpty()) {
        String enumName = NamingUtils.toPascalCase(contextName);
        if (collectedClassNames.contains(enumName)) {
          enumName = NamingUtils.toPascalCase(st.title().orElse(contextName));
        }
        JavaEnum javaEnum =
            new JavaEnum(
                enumName,
                packageName,
                st.enumValues().get().stream().map(NamingUtils::toUpperSnakeCase).toList());
        inlineEnums.add(javaEnum);
        return new TypeResult(enumName, false);
      }
      return new TypeResult("String", false);
    } else if (type instanceof IntegerType) {
      return new TypeResult("int", false);
    } else if (type instanceof NumberType) {
      return new TypeResult("double", false);
    } else if (type instanceof BooleanType) {
      return new TypeResult("boolean", false);
    } else if (type instanceof RefType rt) {
      return resolveRefType(rt);
    } else if (type instanceof ArrayType at) {
      return resolveArrayType(at, contextName, inlineEnums);
    } else if (type instanceof UnionType ut) {
      return resolveUnionType(ut, contextName, inlineEnums);
    } else if (type instanceof AllOfType at) {
      String className = NamingUtils.toPascalCase(contextName);
      classes.add(collectAllOfType(contextName, at.getAllOf(), null));
      return new TypeResult(className, false);
    } else if (type instanceof ObjectType ot && ot.allOf().isPresent()) {
      String className = NamingUtils.toPascalCase(contextName);
      classes.add(collectAllOfType(contextName, ot.allOf().get(), ot));
      return new TypeResult(className, false);
    } else if (type instanceof ObjectType ot && !ot.properties().isEmpty()) {
      String className = NamingUtils.toPascalCase(contextName);
      classes.add(collectObjectType(contextName, ot));
      return new TypeResult(className, false);
    } else if (type instanceof ObjectType ot
        && (ot.oneOf().isPresent() || ot.anyOf().isPresent())) {
      List<HasType> alternatives = ot.oneOf().orElseGet(() -> ot.anyOf().get());
      List<String> typeNames =
          alternatives.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
      boolean allRefs = alternatives.stream().allMatch(alt -> alt instanceof RefType);
      if (allRefs && typeNames.size() == alternatives.size() && !typeNames.isEmpty()) {
        return new TypeResult("Object", false, typeNames);
      }
      return new TypeResult("Object", false);
    } else if (type instanceof OneOfType oot) {
      List<HasType> alternatives = oot.getOneOf();
      List<String> typeNames =
          alternatives.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
      boolean allRefs = alternatives.stream().allMatch(alt -> alt instanceof RefType);
      if (allRefs && typeNames.size() == alternatives.size() && !typeNames.isEmpty()) {
        return new TypeResult("Object", false, typeNames);
      }
      return new TypeResult("Object", false);
    } else if (type instanceof AnyOfType aot) {
      List<HasType> alternatives = aot.getAnyOf();
      List<String> typeNames =
          alternatives.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
      boolean allRefs = alternatives.stream().allMatch(alt -> alt instanceof RefType);
      if (allRefs && typeNames.size() == alternatives.size() && !typeNames.isEmpty()) {
        return new TypeResult("Object", false, typeNames);
      }
      return new TypeResult("Object", false);
    }
    return new TypeResult("Object", false);
  }

  private TypeResult resolveRefType(RefType rt) {
    String refName = NamingUtils.extractRefName(rt.ref());
    HasType resolved = rt.resolve();
    if (resolved instanceof StringType st) {
      if (st.enumValues().isPresent() && !st.enumValues().get().isEmpty()) {
        String enumName = NamingUtils.toPascalCase(refName);
        if (collectedEnumNames.add(enumName)) {
          topLevelEnums.add(
              new JavaEnum(
                  enumName,
                  packageName,
                  st.enumValues().get().stream().map(NamingUtils::toUpperSnakeCase).toList()));
        }
        return new TypeResult(enumName, false);
      }
      return new TypeResult("String", false);
    } else if (resolved instanceof IntegerType) {
      return new TypeResult("int", false);
    } else if (resolved instanceof NumberType) {
      return new TypeResult("double", false);
    } else if (resolved instanceof BooleanType) {
      return new TypeResult("boolean", false);
    } else if (resolved instanceof ArrayType at) {
      return resolveArrayType(at, refName, new ArrayList<>());
    } else if (resolved instanceof ObjectType ot) {
      if (!ot.properties().isEmpty() || ot.allOf().isPresent()) {
        return new TypeResult(NamingUtils.toPascalCase(refName), false);
      }
      if (ot.oneOf().isPresent() || ot.anyOf().isPresent()) {
        List<HasType> alternatives = ot.oneOf().orElseGet(() -> ot.anyOf().get());
        List<String> typeNames =
            alternatives.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
        boolean allRefs = alternatives.stream().allMatch(alt -> alt instanceof RefType);
        if (allRefs && typeNames.size() == alternatives.size() && !typeNames.isEmpty()) {
          return new TypeResult("Object", false, typeNames);
        }
        return new TypeResult("Object", false);
      }
      return new TypeResult("Object", false);
    } else if (resolved instanceof OneOfType oot) {
      List<HasType> alternatives = oot.getOneOf();
      List<String> typeNames =
          alternatives.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
      boolean allRefs = alternatives.stream().allMatch(alt -> alt instanceof RefType);
      if (allRefs && typeNames.size() == alternatives.size() && !typeNames.isEmpty()) {
        return new TypeResult("Object", false, typeNames);
      }
      return new TypeResult("Object", false);
    } else if (resolved instanceof AnyOfType aot) {
      List<HasType> alternatives = aot.getAnyOf();
      List<String> typeNames =
          alternatives.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
      boolean allRefs = alternatives.stream().allMatch(alt -> alt instanceof RefType);
      if (allRefs && typeNames.size() == alternatives.size() && !typeNames.isEmpty()) {
        return new TypeResult("Object", false, typeNames);
      }
      return new TypeResult("Object", false);
    }
    return new TypeResult(NamingUtils.toPascalCase(refName), false);
  }

  private TypeResult resolveArrayType(
      ArrayType at, String contextName, List<JavaEnum> inlineEnums) {
    ArrayItemType[] items = at.getItems();
    if (items != null && items.length > 0) {
      HasType itemType = items[0].getType();
      TypeResult itemResult = resolveJavaType(itemType, contextName, inlineEnums);
      String boxed = boxPrimitive(itemResult.javaType);
      return new TypeResult("List<" + boxed + ">", false);
    }
    return new TypeResult("List<Object>", false);
  }

  private TypeResult resolveUnionType(
      UnionType ut, String contextName, List<JavaEnum> inlineEnums) {
    List<HasType> resolved = ut.getResolvedTypes();
    List<HasType> nonNull = resolved.stream().filter(t -> !(t instanceof NullType)).toList();
    if (nonNull.size() == 1) {
      TypeResult inner = resolveJavaType(nonNull.get(0), contextName, inlineEnums);
      String boxed = boxPrimitive(inner.javaType);
      return new TypeResult(boxed, true);
    }
    return new TypeResult("Object", ut.isNullable());
  }

  private String boxPrimitive(String type) {
    return switch (type) {
      case "int" -> "Integer";
      case "double" -> "Double";
      case "boolean" -> "Boolean";
      default -> type;
    };
  }

  private List<String> extractTypeNames(HasType type) {
    if (type instanceof RefType rt) {
      HasType resolved = rt.resolve();
      if (resolved instanceof ObjectType ot
          && (!ot.properties().isEmpty() || ot.allOf().isPresent())) {
        return List.of(NamingUtils.toPascalCase(NamingUtils.extractRefName(rt.ref())));
      }
      if (resolved instanceof ObjectType ot && (ot.oneOf().isPresent() || ot.anyOf().isPresent())) {
        List<HasType> nested = ot.oneOf().orElseGet(() -> ot.anyOf().get());
        return nested.stream().flatMap(alt -> extractTypeNames(alt).stream()).toList();
      }
    }
    if (type instanceof ObjectType ot) {
      String title = ot.title().orElse(null);
      if (title != null) {
        String className = NamingUtils.toPascalCase(title);
        if (collectedClassNames.add(className)) {
          if (ot.allOf().isPresent()) {
            classes.add(collectAllOfType(title, ot.allOf().get(), ot));
          } else if (!ot.properties().isEmpty()) {
            classes.add(collectObjectType(title, ot));
          }
        }
        return List.of(className);
      }
    }
    return List.of();
  }

  private String extractDescription(HasType type) {
    if (type instanceof StringType st) return st.description().orElse(null);
    if (type instanceof IntegerType it) return it.description().orElse(null);
    if (type instanceof NumberType nt) return nt.description().orElse(null);
    if (type instanceof BooleanType bt) return bt.description().orElse(null);
    if (type instanceof ObjectType ot) return ot.description().orElse(null);
    if (type instanceof ArrayType at) return at.description().orElse(null);
    return null;
  }

  private record TypeResult(String javaType, boolean nullable, List<String> oneOfTypes) {
    TypeResult(String javaType, boolean nullable) {
      this(javaType, nullable, List.of());
    }
  }
}
