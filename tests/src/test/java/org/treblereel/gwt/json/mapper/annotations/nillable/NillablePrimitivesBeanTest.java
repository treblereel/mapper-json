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

@J2clTestInput(NillablePrimitivesBeanTest.class)
public class NillablePrimitivesBeanTest {

  NillablePrimitivesBean_JsonMapperImpl mapper = NillablePrimitivesBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testDefaultValues() {
    NillablePrimitivesBean bean = new NillablePrimitivesBean();
    String result = mapper.toJSON(bean);
    // primitives serialize with their default values (0, false, etc.)
    // String is null and should appear as null thanks to @JsonbNillable
    assertTrue(result.contains("\"_string\":null"));
  }

  @Test
  public void testNullStringSerializedAsNull() {
    NillablePrimitivesBean bean = new NillablePrimitivesBean();
    bean.set_int(42);
    bean.set_boolean(true);
    String result = mapper.toJSON(bean);
    // _string is null, with @JsonbNillable it should be serialized as null
    assertTrue(result.contains("\"_string\":null"));
    assertTrue(result.contains("\"_int\":42"));
    assertTrue(result.contains("\"_boolean\":true"));
  }

  @Test
  public void testWithValues() {
    NillablePrimitivesBean bean = new NillablePrimitivesBean();
    bean.set_int(3);
    bean.set_long(10L);
    bean.set_double(10d);
    bean.set_char('a');
    bean.set_float(10f);
    bean.set_boolean(true);
    bean.set_short((short) 5);
    bean.set_string("hello");

    String result = mapper.toJSON(bean);
    NillablePrimitivesBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testRoundTripWithNullString() {
    NillablePrimitivesBean bean = new NillablePrimitivesBean();
    bean.set_int(7);
    // _string is null
    String result = mapper.toJSON(bean);
    NillablePrimitivesBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
