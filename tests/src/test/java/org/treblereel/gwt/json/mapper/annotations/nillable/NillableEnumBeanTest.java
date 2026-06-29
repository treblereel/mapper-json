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

package org.treblereel.gwt.json.mapper.annotations.nillable;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(NillableEnumBeanTest.class)
public class NillableEnumBeanTest {

  NillableEnumBean_JsonMapperImpl mapper = NillableEnumBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testNullEnum() {
    NillableEnumBean bean = new NillableEnumBean();
    String result = mapper.toJSON(bean);
    assertEquals("{\"color\":null}", result);
  }

  @Test
  public void testWithValue() {
    NillableEnumBean bean = new NillableEnumBean();
    bean.setColor(Color.GREEN);
    String result = mapper.toJSON(bean);
    assertEquals("{\"color\":\"GREEN\"}", result);
    assertEquals(bean, mapper.fromJSON(result));
  }

  @Test
  public void testRoundTripNull() {
    NillableEnumBean bean = new NillableEnumBean();
    String result = mapper.toJSON(bean);
    NillableEnumBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
