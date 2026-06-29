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

@J2clTestInput(NillableBoxedBeanTest.class)
public class NillableBoxedBeanTest {

  NillableBoxedBean_JsonMapperImpl mapper = NillableBoxedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    NillableBoxedBean bean = new NillableBoxedBean();
    String result = mapper.toJSON(bean);
    // all fields are null, with @JsonbNillable they should all appear as null
    assertTrue(result.contains("\"_int\":null"));
    assertTrue(result.contains("\"_long\":null"));
    assertTrue(result.contains("\"_double\":null"));
    assertTrue(result.contains("\"_char\":null"));
    assertTrue(result.contains("\"_float\":null"));
    assertTrue(result.contains("\"_boolean\":null"));
    assertTrue(result.contains("\"_short\":null"));
  }

  @Test
  public void testPartialNulls() {
    NillableBoxedBean bean = new NillableBoxedBean();
    bean.set_int(42);
    bean.set_boolean(true);
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"_int\":42"));
    assertTrue(result.contains("\"_boolean\":true"));
    // other fields should be serialized as null
    assertTrue(result.contains("\"_long\":null"));
    assertTrue(result.contains("\"_double\":null"));
    assertTrue(result.contains("\"_char\":null"));
    assertTrue(result.contains("\"_float\":null"));
    assertTrue(result.contains("\"_short\":null"));
  }

  @Test
  public void testWithValues() {
    NillableBoxedBean bean = new NillableBoxedBean();
    bean.set_int(1234);
    bean.set_long(1000L);
    bean.set_double(10d);
    bean.set_char('c');
    bean.set_float(1122f);
    bean.set_boolean(true);
    bean.set_short((short) 17222);

    String result = mapper.toJSON(bean);
    NillableBoxedBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testRoundTripAllNulls() {
    NillableBoxedBean bean = new NillableBoxedBean();
    String result = mapper.toJSON(bean);
    NillableBoxedBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
