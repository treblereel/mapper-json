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
import java.util.HashMap;
import java.util.Map;

public class JsonbSubtypeSerializer<T> extends JsonSerializer<T> {
  private Map<Class, Info> types = new HashMap<>();

  private final String typeFieldName;

  public JsonbSubtypeSerializer(String typeFieldName, Info... infos) {
    this.typeFieldName = typeFieldName;
    for (Info info : infos) {
      types.put(info.clazz, info);
    }
  }

  @Override
  public void serialize(T obj, String property, JsonGenerator generator, SerializationContext ctx) {
    if (obj == null) {
      return;
    }
    Info info = types.get(obj.getClass());
    if (info != null) {
      JsonGenerator objBuilder = generator.writeStartObject(property);
      objBuilder.write(typeFieldName, info.alias);
      info.ser.serialize(obj, objBuilder, ctx);
      objBuilder.writeEnd();
    } else {
      throw new IllegalArgumentException("Unable to find ser for " + obj.getClass());
    }
  }

  @Override
  public void serialize(T obj, JsonGenerator generator, SerializationContext ctx) {
    if (obj == null) {
      return;
    }
    Info info = types.get(obj.getClass());
    if (info != null) {
      generator.write(typeFieldName, info.alias);
      info.ser.serialize(obj, generator, ctx);
    } else {
      throw new IllegalArgumentException("Unable to find ser for " + obj.getClass());
    }
  }

  public static class Info {

    private final String alias;
    private final Class clazz;
    private final JsonSerializer ser;

    public Info(String alias, Class clazz, JsonSerializer ser) {
      this.alias = alias;
      this.clazz = clazz;
      this.ser = ser;
    }
  }
}
