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

package org.treblereel.gwt.json.mapper.array;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(ArrayOfPrimitivesBeanTest.class)
public class ArrayOfPrimitivesBeanTest {

  ArrayOfPrimitivesBean_JsonMapperImpl mapper = ArrayOfPrimitivesBean_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    ArrayOfPrimitivesBean bean = new ArrayOfPrimitivesBean();
    assertEquals("{}", mapper.toJSON(bean));

    bean.set_boolean(new boolean[] {true, false, true});
    bean.set_char(new char[] {'c', 'c', 'z', 0});
    bean.set_double(new double[] {20.2d, 333.2d, 1777.7d});
    bean.set_float(new float[] {45f, 1111f, 213123f});
    bean.set_int(new int[] {234, 234, 554, 88, 544, 2223, 323});
    bean.set_long(new long[] {23423432l, 234234234l, 234234234l});
    bean.set_short(new short[] {34, 56, 23, 5});

    String rez = mapper.toJSON(bean);
    assertEquals(bean, mapper.fromJSON(rez));
  }
}
