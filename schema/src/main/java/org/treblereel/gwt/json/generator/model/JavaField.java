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
package org.treblereel.gwt.json.generator.model;

import java.util.List;

public record JavaField(
    String fieldName,
    String schemaName,
    String javaType,
    boolean required,
    boolean nullable,
    boolean needsJsonbProperty,
    String description,
    List<String> oneOfTypes) {
  public JavaField {
    if (fieldName == null || fieldName.isBlank())
      throw new IllegalArgumentException("fieldName must not be blank");
    if (schemaName == null || schemaName.isBlank())
      throw new IllegalArgumentException("schemaName must not be blank");
    if (javaType == null || javaType.isBlank())
      throw new IllegalArgumentException("javaType must not be blank");
    if (oneOfTypes == null) oneOfTypes = List.of();
  }

  public JavaField(
      String fieldName,
      String schemaName,
      String javaType,
      boolean required,
      boolean nullable,
      boolean needsJsonbProperty,
      String description) {
    this(
        fieldName,
        schemaName,
        javaType,
        required,
        nullable,
        needsJsonbProperty,
        description,
        List.of());
  }
}
