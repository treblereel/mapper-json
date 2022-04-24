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

package org.treblereel.gwt.json.mapper;

import jakarta.json.JsonObject;
import jakarta.json.bind.serializer.*;
import jakarta.json.stream.JsonGeneratorImpl;
import jakarta.json.stream.JsonParser;

public abstract class AbstractObjectMapper<T> {

  protected abstract JsonbDeserializer<T> newDeserializer();

  protected abstract JsonbSerializer<T> newSerializer();

  public T fromJSON(JsonObject json) {
    return fromJSON(json, new JsonDeserializationContext());
  }

  public T fromJSON(JsonObject json, DeserializationContext context) {
    JsonParser parser = ((JsonDeserializationContext) context).createParser(json);
    return newDeserializer().deserialize(parser, context, null);
  }

  public JsonObject toJSON(T bean) {
    return toJSON(bean, new JsonSerializationContext());
  }

  public JsonObject toJSON(T bean, SerializationContext context) {
    JsonGeneratorImpl generator = ((JsonSerializationContext) context).createGenerator();
    newSerializer().serialize(bean, generator, context);
    return generator.builder().build();
  }
}
