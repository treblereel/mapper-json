/*
 * Copyright © 2023 Treblereel
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

import static org.junit.Assert.*;

import com.google.j2cl.junit.apt.J2clTestInput;
import jakarta.json.JsonArray;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@J2clTestInput(StringOrObjectCustomSerTest.class)
public class StringOrObjectCustomSerTest {

  private static final StringOrObjectCustomSerTest_StringOrObjectCustomSerTestHolder_JsonMapperImpl
      mapper =
          StringOrObjectCustomSerTest_StringOrObjectCustomSerTestHolder_JsonMapperImpl.INSTANCE;

  private static final StringOrObjectCustomSerTest_Holder_JsonMapperImpl holderMapper =
      StringOrObjectCustomSerTest_Holder_JsonMapperImpl.INSTANCE;

  @Test
  public void testNonValue() {
    StringOrObjectCustomSerTestHolder tested = new StringOrObjectCustomSerTestHolder();
    String json = mapper.toJSON(tested);
    assertEquals("{}", json);
    assertNull(mapper.fromJSON(json).getField());
  }

  @Test
  public void testString() {
    StringOrObjectCustomSerTestHolder tested = new StringOrObjectCustomSerTestHolder();
    tested.setField("tested");
    String json = mapper.toJSON(tested);
    assertEquals("{\"field\":\"tested\"}", json);
    assertEquals(tested.getField(), mapper.fromJSON(json).getField());
  }

  @Test
  public void testHolder() {
    StringOrObjectCustomSerTestHolder tested = new StringOrObjectCustomSerTestHolder();
    tested.setField(new Holder("tested"));
    String json = mapper.toJSON(tested);
    assertEquals("{\"field\":{\"value\":\"tested\"}}", json);
    assertEquals(tested.getField(), mapper.fromJSON(json).getField());
  }

  @Test
  public void testHolderArray() {
    StringOrObjectCustomSerTestHolder tested = new StringOrObjectCustomSerTestHolder();
    tested.setField(
        new Holder[] {new Holder("tested"), new Holder("tested2"), new Holder("tested3")});
    String json = mapper.toJSON(tested);
    assertEquals(
        "{\"field\":[{\"value\":\"tested\"},{\"value\":\"tested2\"},{\"value\":\"tested3\"}]}",
        json);
    // assertArrayEquals((Holder[]) tested.getField(), (Holder[]) mapper.fromJSON(json).getField());
  }

  @Test
  public void testStringArray() {
    StringOrObjectCustomSerTestHolder tested = new StringOrObjectCustomSerTestHolder();
    tested.setField(new String[] {"tested", "tested2", "tested3"});
    String json = mapper.toJSON(tested);
    assertEquals("{\"field\":[\"tested\",\"tested2\",\"tested3\"]}", json);
    assertArrayEquals((String[]) tested.getField(), (String[]) mapper.fromJSON(json).getField());
  }

  @JSONMapper
  public static class StringOrObjectCustomSerTestHolder {

    @JsonbTypeSerializer(BeanTypeSerializer.class)
    @JsonbTypeDeserializer(BeanTypeSerializer.class)
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

    private String value;

    public Holder() {}

    public Holder(String value) {
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
      Holder holder = (Holder) o;
      return Objects.equals(value, holder.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  public static class BeanTypeSerializer implements JsonbDeserializer, JsonbSerializer {
    @Override
    public Object deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
      JsonValue value = parser.getValue();
      if (value.getValueType() == JsonValue.ValueType.STRING) {
        return parser.getString();
      } else if (value.getValueType() == JsonValue.ValueType.OBJECT) {
        return holderMapper.newDeserializer().deserialize(parser, ctx, rtType);
      } else if (value.getValueType() == JsonValue.ValueType.ARRAY) {
        List<Holder> result = new ArrayList<>();
        List<String> resultString = new ArrayList<>();
        JsonArray array = parser.getArray();
        for (int i = 0; i < array.size(); i++) {
          JsonValue value1 = array.get(i);
          if (value1.getValueType() == JsonValue.ValueType.STRING) {
            resultString.add(array.getString(i));
          } else {
            result.add(
                StringOrObjectCustomSerTest_Holder_JsonDeserializerImpl.INSTANCE.deserialize(
                    value1, ctx));
          }
        }
        if (resultString.size() > 0) {
          return resultString.toArray(new String[result.size()]);
        }
        return result.toArray(new Holder[result.size()]);
      }
      return null;
    }

    @Override
    public void serialize(Object obj, JsonGenerator generator, SerializationContext ctx) {
      if (obj instanceof String) {
        generator.write((String) obj);
      } else if (obj instanceof Holder) {
        JsonGenerator jsonGenerator = generator.writeStartObject();
        holderMapper.newSerializer().serialize((Holder) obj, jsonGenerator, ctx);
        jsonGenerator.writeEnd();
      } else if (obj instanceof Holder[]) {
        for (Holder holder : (Holder[]) obj) {
          JsonGenerator gen = generator.writeStartObject();
          holderMapper.newSerializer().serialize(holder, gen, ctx);
          gen.writeEnd();
        }
      } else if (obj instanceof String[]) {
        for (String s : (String[]) obj) {
          generator.write(s);
        }
      }
    }
  }
}
