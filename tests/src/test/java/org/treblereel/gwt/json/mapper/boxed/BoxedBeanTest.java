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

package org.treblereel.gwt.json.mapper.boxed;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(BoxedBeanTest.class)
public class BoxedBeanTest {

  BoxedBean_JsonMapperImpl mapper = BoxedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testEmpty() {
    BoxedBean bean = new BoxedBean();
    String result = mapper.toJSON(bean);
    assertEquals("{}", result);

    BoxedBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testValues() {
    BoxedBean bean = new BoxedBean();
    bean.set_boolean(true);
    bean.set_char('c');
    bean.set_double(10d);
    bean.set_float(1122f);
    bean.set_short((short) 17222);
    bean.set_long(1000l);
    bean.set_int(1234);

    String result = mapper.toJSON(bean);
    BoxedBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
