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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(NillableOverrideBeanTest.class)
public class NillableOverrideBeanTest {

  NillableOverrideBean_JsonMapperImpl mapper = NillableOverrideBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    NillableOverrideBean bean = new NillableOverrideBean();
    String result = mapper.toJSON(bean);
    // class-level @JsonbNillable fields should appear as null
    assertTrue(result.contains("\"nillableName\":null"));
    assertTrue(result.contains("\"nillableNumber\":null"));
    assertTrue(result.contains("\"nillableColor\":null"));
    // @JsonbNillable(false) fields should NOT appear when null
    assertFalse(result.contains("\"nonNillableName\""));
    assertFalse(result.contains("\"nonNillableNumber\""));
    assertFalse(result.contains("\"nonNillableColor\""));
  }

  @Test
  public void testWithValues() {
    NillableOverrideBean bean = new NillableOverrideBean();
    bean.setNillableName("hello");
    bean.setNonNillableName("world");
    bean.setNillableNumber(1);
    bean.setNonNillableNumber(2);
    bean.setNillableColor(Color.RED);
    bean.setNonNillableColor(Color.BLUE);

    String result = mapper.toJSON(bean);
    NillableOverrideBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testRoundTripWithNulls() {
    NillableOverrideBean bean = new NillableOverrideBean();
    bean.setNillableName("test");
    // leave nillableNumber and nillableColor as null
    String result = mapper.toJSON(bean);
    NillableOverrideBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
