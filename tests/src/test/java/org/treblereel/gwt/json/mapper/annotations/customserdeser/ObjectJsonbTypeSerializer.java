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

import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.ContextedJsonGenerator;
import jakarta.json.stream.JsonGenerator;

public class ObjectJsonbTypeSerializer implements JsonbSerializer<Object> {

  Translation_JsonSerializerImpl translation_JsonSerializerImpl =
      new Translation_JsonSerializerImpl();

  @Override
  public void serialize(Object obj, JsonGenerator generator, SerializationContext ctx) {
    if (obj instanceof Boolean) {
      generator.write(((Boolean) obj));
    } else if (obj instanceof Translation) {
      if (generator instanceof ContextedJsonGenerator) { // TODO well, this is a hack
        JsonGenerator gen = generator.writeStartObject();
        translation_JsonSerializerImpl.serialize((Translation) obj, gen, ctx);
        gen.writeEnd();
      } else {
        translation_JsonSerializerImpl.serialize((Translation) obj, generator, ctx);
      }
    }
  }
}
