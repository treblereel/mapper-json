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

package jakarta.json.bind.serializer;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorImpl;

public class JsonSerializationContext implements SerializationContext {

  @Override
  public <T> void serialize(String key, T object, JsonGenerator generator) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public <T> void serialize(T object, JsonGenerator generator) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public JsonGeneratorImpl createGenerator() {
    JsonObjectBuilder builder = Json.createObjectBuilder();
    JsonGeneratorImpl generator = new JsonGeneratorImpl(builder);
    return generator;
  }
}
