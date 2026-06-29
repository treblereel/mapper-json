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

package org.treblereel.gwt.json.mapper.annotations.jsonmappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(ExternalWithNestedBeanTest.class)
public class ExternalWithNestedBeanTest {

  ExternalWithNestedBean_JsonMapperImpl mapper = ExternalWithNestedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testSerializeWithNested() {
    ExternalWithNestedBean bean = new ExternalWithNestedBean();
    bean.setTitle("parent");
    ExternalInnerBean inner = new ExternalInnerBean();
    inner.setValue("child");
    inner.setNumber(99);
    bean.setInner(inner);

    String json = mapper.toJSON(bean);
    ExternalWithNestedBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testSerializeNullNested() {
    ExternalWithNestedBean bean = new ExternalWithNestedBean();
    bean.setTitle("solo");
    String json = mapper.toJSON(bean);
    ExternalWithNestedBean from = mapper.fromJSON(json);
    assertEquals("solo", from.getTitle());
    assertNull(from.getInner());
  }

  @Test
  public void testTransitiveInnerBeanMapper() {
    // ExternalInnerBean is NOT listed in @JSONMappers but should have
    // a mapper generated transitively via ExternalWithNestedBean's field
    ExternalInnerBean_JsonMapperImpl innerMapper = ExternalInnerBean_JsonMapperImpl.INSTANCE;
    ExternalInnerBean inner = new ExternalInnerBean();
    inner.setValue("direct");
    inner.setNumber(7);

    String json = innerMapper.toJSON(inner);
    ExternalInnerBean from = innerMapper.fromJSON(json);
    assertEquals(inner, from);
  }

  @Test
  public void testRoundTrip() {
    ExternalWithNestedBean bean = new ExternalWithNestedBean();
    bean.setTitle("test");
    ExternalInnerBean inner = new ExternalInnerBean();
    inner.setValue("nested");
    inner.setNumber(123);
    bean.setInner(inner);

    String json = mapper.toJSON(bean);
    ExternalWithNestedBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
    assertNotNull(from.getInner());
    assertEquals("nested", from.getInner().getValue());
    assertEquals(Integer.valueOf(123), from.getInner().getNumber());
  }
}
