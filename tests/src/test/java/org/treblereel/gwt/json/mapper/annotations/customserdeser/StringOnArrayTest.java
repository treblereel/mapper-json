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

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import jakarta.json.JsonValue;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.internal.deserializer.JsonbDeserializer;
import org.treblereel.gwt.json.mapper.internal.deserializer.StringJsonDeserializer;
import org.treblereel.gwt.json.mapper.internal.deserializer.array.ArrayJsonDeserializer;
import org.treblereel.gwt.json.mapper.internal.serializer.StringJsonSerializer;
import org.treblereel.gwt.json.mapper.internal.serializer.array.ArrayBeanJsonSerializer;

@J2clTestInput(StringOnArrayTest.class)
public class StringOnArrayTest {

  private static final StringOnArrayTest_StringOnArray_JsonMapperImpl mapper =
      new StringOnArrayTest_StringOnArray_JsonMapperImpl();

  @Test
  public void test() {
    StringOnArray tested = new StringOnArray();
    assertEquals("{}", mapper.toJSON(tested));
    tested.setField("tested");
    assertEquals("{\"field\":\"tested\"}", mapper.toJSON(tested));

    assertEquals("{\"field\":\"tested\"}", mapper.toJSON(mapper.fromJSON(mapper.toJSON(tested))));

    Holder[] values = new Holder[] {new Holder("ONE"), new Holder("TWO")};
    tested.setField(values);
    assertEquals("{\"field\":[{\"value\":\"ONE\"},{\"value\":\"TWO\"}]}", mapper.toJSON(tested));
    assertEquals(
        "{\"field\":[{\"value\":\"ONE\"},{\"value\":\"TWO\"}]}",
        mapper.toJSON(mapper.fromJSON(mapper.toJSON(tested))));
  }

  @JSONMapper
  public static class StringOnArray {

    @JsonbTypeSerializer(StringOnArraySerializer.class)
    @JsonbTypeDeserializer(StringOnArrayDeserializer.class)
    private Object field;

    public Object getField() {
      return field;
    }

    public void setField(Object field) {
      this.field = field;
    }
  }

  @JSONMapper
  public static class Holder {

    public Holder() {}

    public Holder(String value) {
      setValue(value);
    }

    private String value;

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }
  }

  public static class StringOnArraySerializer implements JsonbSerializer {

    private static final StringJsonSerializer stringJsonSerializer = new StringJsonSerializer();
    private static final StringOnArrayTest_Holder_JsonSerializerImpl holderJsonSerializer =
        new StringOnArrayTest_Holder_JsonSerializerImpl();

    @Override
    public void serialize(Object obj, JsonGenerator generator, SerializationContext ctx) {
      if (obj instanceof String) {
        stringJsonSerializer.serialize((String) obj, "field", generator, ctx);
      } else if (obj instanceof Holder[]) {
        new ArrayBeanJsonSerializer<>(holderJsonSerializer)
            .serialize((Holder[]) obj, "field", generator, ctx);
      }
    }
  }

  public static class StringOnArrayDeserializer extends JsonbDeserializer {

    private static final StringJsonDeserializer stringJsonDeserializer =
        new StringJsonDeserializer();

    private static final StringOnArrayTest_Holder_JsonDeserializerImpl holderJsonDeserializer =
        new StringOnArrayTest_Holder_JsonDeserializerImpl();

    @Override
    public Object deserialize(JsonValue value, DeserializationContext ctx) {
      if (value.getValueType() != JsonValue.ValueType.NULL) {
        if (value.getValueType() == JsonValue.ValueType.STRING) {
          return stringJsonDeserializer.deserialize(value, ctx);
        } else if (value.getValueType() == JsonValue.ValueType.ARRAY) {
          return new ArrayJsonDeserializer<>(holderJsonDeserializer, Holder[]::new)
              .deserialize(value, ctx);
        }
      }
      return null;
    }
  }
}
