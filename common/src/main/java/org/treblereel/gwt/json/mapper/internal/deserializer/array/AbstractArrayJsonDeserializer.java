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

package org.treblereel.gwt.json.mapper.internal.deserializer.array;

import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import jakarta.json.bind.serializer.DeserializationContext;
import java.util.ArrayList;
import java.util.List;
import org.treblereel.gwt.json.mapper.internal.deserializer.JsonDeserializer;

public abstract class AbstractArrayJsonDeserializer<T> extends JsonDeserializer<T> {

  protected <T> List<T> deserializeIntoList(
      JsonValue json, JsonDeserializer<T> internalDeser, DeserializationContext ctx) {
    List<T> collection = new ArrayList<>();
    JsonArray array = json.asJsonArray();
    for (int i = 0; i < array.size(); i++) {
      collection.add(internalDeser.deserialize(array.get(i), ctx));
    }
    return collection;
  }
}
