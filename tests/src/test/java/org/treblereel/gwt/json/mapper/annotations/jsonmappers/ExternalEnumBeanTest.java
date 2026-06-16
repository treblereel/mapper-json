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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(ExternalEnumBeanTest.class)
public class ExternalEnumBeanTest {

  ExternalEnumBean_JsonMapperImpl mapper = ExternalEnumBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testSerialize() {
    ExternalEnumBean bean = new ExternalEnumBean();
    bean.setLabel("favorite");
    bean.setColor(ExternalColor.BLUE);

    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"label\":\"favorite\""));
    assertTrue(result.contains("\"color\":\"BLUE\""));
  }

  @Test
  public void testRoundTrip() {
    ExternalEnumBean bean = new ExternalEnumBean();
    bean.setLabel("test");
    bean.setColor(ExternalColor.RED);

    String json = mapper.toJSON(bean);
    ExternalEnumBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testNullEnum() {
    ExternalEnumBean bean = new ExternalEnumBean();
    bean.setLabel("no color");
    String json = mapper.toJSON(bean);
    ExternalEnumBean from = mapper.fromJSON(json);
    assertEquals("no color", from.getLabel());
    assertNull(from.getColor());
  }

  @Test
  public void testAllEnumValues() {
    for (ExternalColor color : ExternalColor.values()) {
      ExternalEnumBean bean = new ExternalEnumBean();
      bean.setLabel(color.name());
      bean.setColor(color);
      String json = mapper.toJSON(bean);
      ExternalEnumBean from = mapper.fromJSON(json);
      assertEquals(bean, from);
    }
  }
}
