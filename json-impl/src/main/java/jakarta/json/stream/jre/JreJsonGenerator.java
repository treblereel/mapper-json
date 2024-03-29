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

package jakarta.json.stream.jre;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonArrayGenerator;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonGeneratorDecorator;
import org.treblereel.gwt.utils.GwtIncompatible;

public class JreJsonGenerator extends JsonGeneratorDecorator {

  public JreJsonGenerator(JsonObjectBuilder builder, SerializationContext ctx) {
    super(builder, ctx);
  }

  @GwtIncompatible
  @Override
  public JsonGenerator writeStartArray(String name) {
    JsonArrayBuilder arrayBuilder = jakarta.json.Json.createArrayBuilder();
    return new JsonArrayGenerator(arrayBuilder, builder, name, ctx);
  }
}
