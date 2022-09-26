/*
 * Copyright © 2022 Treblereel
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

package org.treblereel.gwt.json.mapper.annotations.customserdeser;

import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;
import java.lang.reflect.Type;

public class ObjectJsonbTypeDeserializer implements JsonbDeserializer<Object> {

  Translation_JsonDeserializerImpl translation_JsonDeserializerImpl =
      new Translation_JsonDeserializerImpl();

  @Override
  public Object deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    JsonValue value = parser.getValue();
    if (value.getValueType() != JsonValue.ValueType.NULL) {
      if (value.getValueType() == JsonValue.ValueType.TRUE
          || value.getValueType() == JsonValue.ValueType.FALSE) {
        if (value.getValueType() == JsonValue.ValueType.TRUE) {
          return true;
        } else {
          return false;
        }
      } else if (value.getValueType() == JsonValue.ValueType.OBJECT) {
        JsonObject jsonObject =
            (value instanceof JsonObject) ? (JsonObject) value : value.asJsonObject();

        if (jsonObject.containsKey("holder")) {
          return translation_JsonDeserializerImpl.deserialize(jsonObject.get("holder"), ctx);
        }
        return translation_JsonDeserializerImpl.deserialize(value, ctx);
      }
    }
    return null;
  }
}
