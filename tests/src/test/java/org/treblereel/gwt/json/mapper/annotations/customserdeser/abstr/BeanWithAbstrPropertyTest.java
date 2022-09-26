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

package org.treblereel.gwt.json.mapper.annotations.customserdeser.abstr;

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
import java.util.Objects;
import java.util.Set;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.internal.deserializer.StringJsonDeserializer;
import org.treblereel.gwt.json.mapper.internal.serializer.StringJsonSerializer;

@J2clTestInput(BeanWithAbstrPropertyTest.class)
public class BeanWithAbstrPropertyTest {

  @JSONMapper
  public static class BeanWithIfaceProperty {

    @JsonbTypeSerializer(IfaceMapper.class)
    @JsonbTypeDeserializer(IfaceMapper.class)
    private AbstractBean iface;

    @JsonbTypeSerializer(IfaceMapper.class)
    @JsonbTypeDeserializer(IfaceMapper.class)
    private AbstractBean[] ifaces;

    @JsonbTypeSerializer(IfaceMapper.class)
    @JsonbTypeDeserializer(IfaceMapper.class)
    private Set<AbstractBean> ifaceSet;

    public AbstractBean getIface() {
      return iface;
    }

    public void setIface(AbstractBean iface) {
      this.iface = iface;
    }

    public AbstractBean[] getIfaces() {
      return ifaces;
    }

    public void setIfaces(AbstractBean[] ifaces) {
      this.ifaces = ifaces;
    }

    public Set<AbstractBean> getIfaceSet() {
      return ifaceSet;
    }

    public void setIfaceSet(Set<AbstractBean> ifaceSet) {
      this.ifaceSet = ifaceSet;
    }
  }

  public static class IfaceMapper
      implements JsonbSerializer<AbstractBean>, JsonbDeserializer<AbstractBean> {

    private static final StringJsonSerializer stringJsonSerializer = new StringJsonSerializer();
    private static final StringJsonDeserializer stringJsonDeserializer =
        new StringJsonDeserializer();

    @Override
    public AbstractBean deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
      if (parser.getValue().getValueType() == JsonValue.ValueType.STRING) {
        return new AbstractBeanImpl(stringJsonDeserializer.deserialize(parser, ctx, rtType));
      }
      return new AbstractBeanImpl(parser.getObject().getString("iface"));
    }

    @Override
    public void serialize(AbstractBean obj, JsonGenerator generator, SerializationContext ctx) {
      if (generator instanceof jakarta.json.stream.ContextedJsonGenerator) {
        stringJsonSerializer.serialize(obj.getValue(), generator, ctx);
      } else {
        stringJsonSerializer.serialize(obj.getValue(), "iface", generator, ctx);
      }
    }
  }

  public abstract static class AbstractBean {

    abstract String getValue();

    abstract void setValue(String value);
  }

  public static class AbstractBeanImpl extends AbstractBean {

    private String value;

    public AbstractBeanImpl(String value) {
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

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      AbstractBeanImpl that = (AbstractBeanImpl) o;
      return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  private static final BeanWithAbstrPropertyTest_BeanWithIfaceProperty_JsonMapperImpl mapper =
      BeanWithAbstrPropertyTest_BeanWithIfaceProperty_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {

    BeanWithIfaceProperty bean = new BeanWithIfaceProperty();
    bean.setIface(new AbstractBeanImpl("test"));
    String json = mapper.toJSON(bean);
    assertEquals("{\"iface\":\"test\"}", json);

    assertEquals(bean.getIface().getValue(), mapper.fromJSON(json).getIface().getValue());
    bean.setIfaces(
        new AbstractBean[] {new AbstractBeanImpl("test1"), new AbstractBeanImpl("test2")});
    json = mapper.toJSON(bean);
    assertEquals(
        "{\"iface\":\"test\",\"ifaces\":[{\"iface\":\"test1\"},{\"iface\":\"test2\"}]}", json);
    assertEquals(bean.getIfaces()[0].getValue(), mapper.fromJSON(json).getIfaces()[0].getValue());
    assertEquals(bean.getIfaces()[1].getValue(), mapper.fromJSON(json).getIfaces()[1].getValue());

    Set<AbstractBean> ifaceSet = new HashSet<>();
    ifaceSet.add(new AbstractBeanImpl("_test1"));
    ifaceSet.add(new AbstractBeanImpl("_test2"));
    bean.setIfaceSet(ifaceSet);

    json = mapper.toJSON(bean);
    assertEquals(
        "{\"iface\":\"test\",\"ifaces\":[{\"iface\":\"test1\"},{\"iface\":\"test2\"}],\"ifaceSet\":[{\"iface\":\"_test1\"},{\"iface\":\"_test2\"}]}",
        json);

    assertEquals(bean.getIfaceSet(), mapper.fromJSON(mapper.toJSON(bean)).getIfaceSet());
  }
}
