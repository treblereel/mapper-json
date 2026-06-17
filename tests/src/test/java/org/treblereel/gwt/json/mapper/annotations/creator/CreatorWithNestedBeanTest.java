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

package org.treblereel.gwt.json.mapper.annotations.creator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(CreatorWithNestedBeanTest.class)
public class CreatorWithNestedBeanTest {

  CreatorWithNestedBean_JsonMapperImpl mapper = CreatorWithNestedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testRoundTrip() {
    CreatorInnerBean inner = new CreatorInnerBean();
    inner.setValue("nested");
    inner.setNumber(42);
    CreatorWithNestedBean bean = new CreatorWithNestedBean("parent", inner);
    String json = mapper.toJSON(bean);
    CreatorWithNestedBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
    assertNotNull(from.getInner());
    assertEquals("nested", from.getInner().getValue());
    assertEquals(Integer.valueOf(42), from.getInner().getNumber());
  }

  @Test
  public void testNullNested() {
    String json = "{\"title\":\"solo\"}";
    CreatorWithNestedBean bean = mapper.fromJSON(json);
    assertEquals("solo", bean.getTitle());
    assertNull(bean.getInner());
  }

  @Test
  public void testSerialize() {
    CreatorInnerBean inner = new CreatorInnerBean();
    inner.setValue("v");
    inner.setNumber(1);
    CreatorWithNestedBean bean = new CreatorWithNestedBean("t", inner);
    String json = mapper.toJSON(bean);
    CreatorWithNestedBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }
}
