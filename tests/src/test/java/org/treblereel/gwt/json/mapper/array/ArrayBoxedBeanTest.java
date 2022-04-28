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

import org.junit.Test;

public class ArrayBoxedBeanTest {

  ArrayBoxedBean_JsonMapperImpl mapper = ArrayBoxedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    ArrayBoxedBean bean = new ArrayBoxedBean();
    assertEquals("{}", mapper.toJSON(bean));

    bean.set_boolean(new Boolean[] {true, false, true});
    bean.set_char(new Character[] {'c', 'c', 'z', 0});
    bean.set_double(new Double[] {20.2d, 333.2d, 1777d});
    bean.set_float(new Float[] {45f, 1111f, 213123f});
    bean.set_int(new Integer[] {234, 234, 554, 88, 544, 2223, 323});
    bean.set_long(new Long[] {23423432l, 234234234l, 234234234l});
    bean.set_short(new Short[] {34, 56, 23, 5});
    bean.set_string(new String[] {"AAAA", "BBBB", "CCCC", "DDDD"});

    String rez = mapper.toJSON(bean);
    assertEquals(
        "{\"_int\":[234,234,554,88,544,2223,323],\"_long\":[23423432,234234234,234234234],\"_double\":[20.2,333.2,1777.0],\"_char\":[99,99,122,0],\"_float\":[45.0,1111.0,213123.0],\"_boolean\":[true,false,true],\"_short\":[34,56,23,5],\"_string\":[\"AAAA\",\"BBBB\",\"CCCC\",\"DDDD\"]}",
        rez);

    assertEquals(bean, mapper.fromJSON(rez));
  }
}
