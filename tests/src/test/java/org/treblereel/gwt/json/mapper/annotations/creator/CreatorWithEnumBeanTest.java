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

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(CreatorWithEnumBeanTest.class)
public class CreatorWithEnumBeanTest {

  CreatorWithEnumBean_JsonMapperImpl mapper = CreatorWithEnumBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testRoundTrip() {
    CreatorWithEnumBean bean = new CreatorWithEnumBean("test", CreatorColor.GREEN);
    String json = mapper.toJSON(bean);
    CreatorWithEnumBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testNullEnum() {
    String json = "{\"name\":\"no color\"}";
    CreatorWithEnumBean bean = mapper.fromJSON(json);
    assertEquals("no color", bean.getName());
    assertNull(bean.getColor());
  }

  @Test
  public void testAllEnumValues() {
    for (CreatorColor color : CreatorColor.values()) {
      CreatorWithEnumBean bean = new CreatorWithEnumBean(color.name(), color);
      String json = mapper.toJSON(bean);
      CreatorWithEnumBean from = mapper.fromJSON(json);
      assertEquals(bean, from);
    }
  }
}
