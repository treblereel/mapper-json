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

package org.treblereel.gwt.json.mapper.annotations.customserdeser.iface;

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
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.internal.deserializer.StringJsonDeserializer;
import org.treblereel.gwt.json.mapper.internal.serializer.StringJsonSerializer;

@J2clTestInput(BeanWithIfacePropertyTest2.class)
public class BeanWithIfacePropertyTest2 {

  @JSONMapper
  public static class BeanWithIfaceProperty {

    private Iface iface;

    private Iface[] ifaces;

    private Set<Iface> ifaceSet;

    public Iface getIface() {
      return iface;
    }

    public void setIface(Iface iface) {
      this.iface = iface;
    }

    public Iface[] getIfaces() {
      return ifaces;
    }

    public void setIfaces(Iface[] ifaces) {
      this.ifaces = ifaces;
    }

    public Set<Iface> getIfaceSet() {
      return ifaceSet;
    }

    public void setIfaceSet(Set<Iface> ifaceSet) {
      this.ifaceSet = ifaceSet;
    }
  }

  public static class IfaceMapper implements JsonbSerializer<Iface>, JsonbDeserializer<Iface> {

    private static final StringJsonSerializer stringJsonSerializer = new StringJsonSerializer();
    private static final StringJsonDeserializer stringJsonDeserializer =
        new StringJsonDeserializer();

    @Override
    public Iface deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
      if (parser.getValue().getValueType() == JsonValue.ValueType.STRING) {
        return new IfaceImpl(stringJsonDeserializer.deserialize(parser, ctx, rtType));
      }
      return new IfaceImpl(parser.getObject().getString("iface"));
    }

    @Override
    public void serialize(Iface obj, JsonGenerator generator, SerializationContext ctx) {
      if (generator instanceof jakarta.json.stream.ContextedJsonGenerator) {
        stringJsonSerializer.serialize(obj.getValue(), generator, ctx);
      } else {
        stringJsonSerializer.serialize(obj.getValue(), "iface", generator, ctx);
      }
    }
  }

  @JsonbTypeSerializer(IfaceMapper.class)
  @JsonbTypeDeserializer(IfaceMapper.class)
  public interface Iface {

    String getValue();

    void setValue(String value);
  }

  public static class IfaceImpl implements Iface {

    private String value;

    public IfaceImpl(String value) {
      this.value = value;
    }

    @Override
    public String getValue() {
      return value;
    }

    @Override
    public void setValue(String value) {
      this.value = value;
    }
  }

  private static final BeanWithIfacePropertyTest2_BeanWithIfaceProperty_JsonMapperImpl mapper =
      BeanWithIfacePropertyTest2_BeanWithIfaceProperty_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {

    BeanWithIfaceProperty bean = new BeanWithIfaceProperty();
    bean.setIface(new IfaceImpl("test"));
    String json = mapper.toJSON(bean);
    assertEquals("{\"iface\":\"test\"}", json);
    assertEquals(bean.getIface().getValue(), mapper.fromJSON(json).getIface().getValue());

    bean.setIfaces(new Iface[] {new IfaceImpl("test1"), new IfaceImpl("test2")});
    json = mapper.toJSON(bean);
    assertEquals(
        "{\"iface\":\"test\",\"ifaces\":[{\"iface\":\"test1\"},{\"iface\":\"test2\"}]}", json);
    assertEquals(bean.getIfaces()[0].getValue(), mapper.fromJSON(json).getIfaces()[0].getValue());
    assertEquals(bean.getIfaces()[1].getValue(), mapper.fromJSON(json).getIfaces()[1].getValue());

    Set<Iface> ifaceSet = new HashSet<>();
    ifaceSet.add(new IfaceImpl("_test1"));
    ifaceSet.add(new IfaceImpl("_test2"));
    bean.setIfaceSet(ifaceSet);

    json = mapper.toJSON(bean);
    assertEquals(
        "{\"iface\":\"test\",\"ifaces\":[{\"iface\":\"test1\"},{\"iface\":\"test2\"}],\"ifaceSet\":[{\"iface\":\"_test1\"},{\"iface\":\"_test2\"}]}",
        json);

    assertEquals(
        bean.getIfaceSet().iterator().next().getValue(),
        mapper.fromJSON(json).getIfaceSet().iterator().next().getValue());
    assertEquals(
        bean.getIfaceSet().iterator().next().getValue(),
        mapper.fromJSON(json).getIfaceSet().iterator().next().getValue());
  }
}
