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

@J2clTestInput(NillableFieldLevelBeanTest.class)
public class NillableFieldLevelBeanTest {

  NillableFieldLevelBean_JsonMapperImpl mapper = NillableFieldLevelBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    NillableFieldLevelBean bean = new NillableFieldLevelBean();
    String result = mapper.toJSON(bean);
    // @JsonbNillable fields should appear as null
    assertTrue(result.contains("\"nillableName\":null"));
    assertTrue(result.contains("\"nillableNumber\":null"));
    assertTrue(result.contains("\"nillableList\":null"));
    assertTrue(result.contains("\"nillableColor\":null"));
    // regular fields should NOT appear when null
    assertFalse(result.contains("\"regularName\""));
    assertFalse(result.contains("\"regularNumber\""));
    assertFalse(result.contains("\"regularList\""));
    assertFalse(result.contains("\"regularColor\""));
  }

  @Test
  public void testMixedValues() {
    NillableFieldLevelBean bean = new NillableFieldLevelBean();
    bean.setNillableName("hello");
    bean.setRegularNumber(42);
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"nillableName\":\"hello\""));
    assertTrue(result.contains("\"regularNumber\":42"));
    // nillable fields that are still null
    assertTrue(result.contains("\"nillableNumber\":null"));
    assertTrue(result.contains("\"nillableList\":null"));
    assertTrue(result.contains("\"nillableColor\":null"));
    // regular fields that are still null — should not appear
    assertFalse(result.contains("\"regularName\""));
    assertFalse(result.contains("\"regularList\""));
    assertFalse(result.contains("\"regularColor\""));
  }

  @Test
  public void testRoundTrip() {
    NillableFieldLevelBean bean = new NillableFieldLevelBean();
    bean.setNillableName("test");
    bean.setRegularName("regular");
    bean.setNillableNumber(10);
    bean.setRegularNumber(20);
    bean.setNillableColor(Color.RED);
    bean.setRegularColor(Color.BLUE);

    String result = mapper.toJSON(bean);
    NillableFieldLevelBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
