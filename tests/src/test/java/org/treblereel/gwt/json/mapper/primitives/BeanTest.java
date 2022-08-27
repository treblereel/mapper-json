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

package org.treblereel.gwt.json.mapper.primitives;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(BeanTest.class)
public class BeanTest {

  Bean_JsonMapperImpl mapper = Bean_JsonMapperImpl.INSTANCE;

  @Test
  public void testEmpty() {
    Bean bean = new Bean();
    Bean_JsonMapperImpl mapper = Bean_JsonMapperImpl.INSTANCE;
    assertEquals(bean, mapper.fromJSON(mapper.toJSON(bean)));
  }

  @Test
  public void testValues() {
    Bean bean = new Bean();
    bean.setName("Bean");
    bean.set_int(3);
    bean.set_boolean(true);
    bean.set_long(10l);
    bean.set_double(10d);
    bean.set_char('a');
    bean.set_float(10f);
    bean.set_string("testValues");

    String result = mapper.toJSON(bean);
    Bean parsed = mapper.fromJSON(result);
    assertEquals(bean, parsed);
  }
}
