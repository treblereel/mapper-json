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

package jakarta.json.stream;

import jakarta.json.JsonObject;
import jakarta.json.JsonObjectDecorator;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AbstractBeanJsonDeserializer<T> implements JsonbDeserializer<T> {

  protected List<BiConsumer<T, JsonObjectDecorator>> properties = new ArrayList<>();

  @Override
  public T deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
    JsonParser.Event event = parser.next();
    if (event == JsonParser.Event.START_OBJECT) {
      return deserialize(parser.getObject(), ctx);
    }
    return null;
  }

  private T deserialize(JsonObject jsonObject, DeserializationContext ctx) {
    T instance = newInstance();
    JsonObjectDecorator jsonObjectDecorator = new JsonObjectDecorator(jsonObject);
    properties.forEach(p -> p.accept(instance, jsonObjectDecorator));
    return instance;
  }

  protected abstract T newInstance();
}
