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
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(NillableArraysBeanTest.class)
public class NillableArraysBeanTest {

  NillableArraysBean_JsonMapperImpl mapper = NillableArraysBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    NillableArraysBean bean = new NillableArraysBean();
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"intArray\":null"));
    assertTrue(result.contains("\"stringArray\":null"));
    assertTrue(result.contains("\"boxedIntArray\":null"));
    assertTrue(result.contains("\"colorArray\":null"));
  }

  @Test
  public void testPartialNulls() {
    NillableArraysBean bean = new NillableArraysBean();
    bean.setIntArray(new int[] {1, 2, 3});
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"intArray\":[1,2,3]"));
    assertTrue(result.contains("\"stringArray\":null"));
    assertTrue(result.contains("\"boxedIntArray\":null"));
    assertTrue(result.contains("\"colorArray\":null"));
  }

  @Test
  public void testWithValues() {
    NillableArraysBean bean = new NillableArraysBean();
    bean.setIntArray(new int[] {1, 2, 3});
    bean.setStringArray(new String[] {"a", "b"});
    bean.setBoxedIntArray(new Integer[] {10, 20});
    bean.setColorArray(new Color[] {Color.RED, Color.BLUE});

    String result = mapper.toJSON(bean);
    NillableArraysBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testRoundTripAllNulls() {
    NillableArraysBean bean = new NillableArraysBean();
    String result = mapper.toJSON(bean);
    NillableArraysBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
