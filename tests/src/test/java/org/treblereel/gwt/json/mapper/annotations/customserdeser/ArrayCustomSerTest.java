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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import jakarta.json.JsonValue;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.internal.deserializer.array.StringArrayJsonDeserializer;

@J2clTestInput(ArrayCustomSerTest.class)
public class ArrayCustomSerTest {

  private static final ArrayCustomSerTest_ArrayCustomSerHolder_JsonMapperImpl JSON_MAPPER =
      ArrayCustomSerTest_ArrayCustomSerHolder_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    Bean bean1 = new Bean("bean1");
    Bean bean2 = new Bean("bean2");
    Bean bean3 = new Bean("bean3");
    Bean bean4 = new Bean("bean4");

    Bean[] beans = new Bean[] {bean1, bean2, bean3, bean4};

    ArrayCustomSerHolder holder = new ArrayCustomSerHolder();
    holder.setBeans(beans);
    String json = JSON_MAPPER.toJSON(holder);
    assertEquals("{\"beans\":[\"bean1\",\"bean2\",\"bean3\",\"bean4\"]}", json);
    assertArrayEquals((Bean[]) holder.beans, (Bean[]) JSON_MAPPER.fromJSON(json).beans);
  }

  @JSONMapper
  public static class ArrayCustomSerHolder {

    @JsonbTypeSerializer(BeanTypeSerializer.class)
    @JsonbTypeDeserializer(BeanTypeSerializer.class)
    private Object beans;

    public Object getBeans() {
      return beans;
    }

    public void setBeans(Object beans) {
      this.beans = beans;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ArrayCustomSerHolder that = (ArrayCustomSerHolder) o;
      return Objects.equals(beans, that.beans);
    }

    @Override
    public int hashCode() {
      return Objects.hash(beans);
    }
  }

  @JSONMapper
  public static class Bean {

    private String value;

    public Bean() {}

    public Bean(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Bean bean = (Bean) o;
      return Objects.equals(value, bean.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static class BeanTypeSerializer
      implements JsonbDeserializer<Object>, JsonbSerializer<Object> {

    @Override
    public Object deserialize(
        JsonParser jsonParser, DeserializationContext deserializationContext, Type type) {
      if (jsonParser.getValue().getValueType().equals(JsonValue.ValueType.ARRAY)) {
        String[] objects =
            new StringArrayJsonDeserializer().deserialize(jsonParser, deserializationContext, type);
        return Arrays.stream(objects).map(a -> new Bean(a)).toArray(Bean[]::new);
      }
      return null;
    }

    @Override
    public void serialize(
        Object obj, JsonGenerator jsonGenerator, SerializationContext serializationContext) {
      if (obj instanceof Bean[]) {
        for (Bean bean : (Bean[]) obj) {
          jsonGenerator.write(bean.getValue());
        }
      }
    }
  }
}
