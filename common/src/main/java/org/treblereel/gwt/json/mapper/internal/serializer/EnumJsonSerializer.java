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

package org.treblereel.gwt.json.mapper.internal.serializer;

import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import java.util.function.Function;

public class EnumJsonSerializer<E extends Enum<E>> extends JsonSerializer<E> {

  private final Function<E, String> typeStringFunction;

  public EnumJsonSerializer(Function<E, String> typeStringFunction) {
    this.typeStringFunction = typeStringFunction;
  }

  @Override
  public void serialize(E obj, String property, JsonGenerator generator, SerializationContext ctx) {
    generator.write(property, typeStringFunction.apply(obj));
  }

  public void serialize(E obj, JsonGenerator generator, SerializationContext ctx) {
    generator.write(typeStringFunction.apply(obj));
  }
}
