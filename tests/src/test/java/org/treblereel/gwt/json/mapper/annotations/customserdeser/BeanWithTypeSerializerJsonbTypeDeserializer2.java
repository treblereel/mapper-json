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
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.stream.JsonParser;
import java.lang.reflect.Type;

public class BeanWithTypeSerializerJsonbTypeDeserializer2
    implements JsonbDeserializer<BeanWithTypeSerializer> {

  @Override
  public BeanWithTypeSerializer deserialize(
      JsonParser parser, DeserializationContext ctx, Type rtType) {
    BeanWithTypeSerializer holder = new BeanWithTypeSerializer();
    holder.setValue(((JsonObject) parser.getValue()).getString("__value2__"));
    return holder;
  }
}
