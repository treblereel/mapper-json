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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(CreatorMixedBeanTest.class)
public class CreatorMixedBeanTest {

  CreatorMixedBean_JsonMapperImpl mapper = CreatorMixedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testRoundTrip() {
    CreatorMixedBean bean = new CreatorMixedBean("item", 1);
    bean.setDescription("a description");
    bean.setEnabled(true);
    String json = mapper.toJSON(bean);
    CreatorMixedBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testCreatorFieldsOnly() {
    String json = "{\"name\":\"test\",\"id\":99}";
    CreatorMixedBean bean = mapper.fromJSON(json);
    assertEquals("test", bean.getName());
    assertEquals(99, bean.getId());
    assertNull(bean.getDescription());
    assertEquals(false, bean.isEnabled());
  }

  @Test
  public void testAllFields() {
    String json = "{\"name\":\"full\",\"id\":7,\"description\":\"desc\",\"enabled\":true}";
    CreatorMixedBean bean = mapper.fromJSON(json);
    assertEquals("full", bean.getName());
    assertEquals(7, bean.getId());
    assertEquals("desc", bean.getDescription());
    assertTrue(bean.isEnabled());
  }

  @Test
  public void testSerialize() {
    CreatorMixedBean bean = new CreatorMixedBean("ser", 5);
    bean.setDescription("hello");
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":\"ser\""));
    assertTrue(result.contains("\"id\":5"));
    assertTrue(result.contains("\"description\":\"hello\""));
  }
}
