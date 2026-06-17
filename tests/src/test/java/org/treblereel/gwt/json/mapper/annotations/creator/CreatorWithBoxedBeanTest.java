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

@J2clTestInput(CreatorWithBoxedBeanTest.class)
public class CreatorWithBoxedBeanTest {

  CreatorWithBoxedBean_JsonMapperImpl mapper = CreatorWithBoxedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testRoundTrip() {
    CreatorWithBoxedBean bean = new CreatorWithBoxedBean("test", 42, 3.14, true);
    String json = mapper.toJSON(bean);
    CreatorWithBoxedBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testNullBoxedValues() {
    String json = "{\"label\":\"only label\"}";
    CreatorWithBoxedBean bean = mapper.fromJSON(json);
    assertEquals("only label", bean.getLabel());
    assertNull(bean.getCount());
    assertNull(bean.getScore());
    assertNull(bean.getActive());
  }

  @Test
  public void testAllNulls() {
    String json = "{}";
    CreatorWithBoxedBean bean = mapper.fromJSON(json);
    assertNull(bean.getLabel());
    assertNull(bean.getCount());
    assertNull(bean.getScore());
    assertNull(bean.getActive());
  }
}
