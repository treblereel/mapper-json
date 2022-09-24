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

package org.treblereel.gwt.json.mapper.annotations.customserdeser;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(BeanWithTypeSerializerTest.class)
public class BeanWithTypeSerializerTest {

  private static final BeanWithTypeSerializerHolder_JsonMapperImpl mapper =
      BeanWithTypeSerializerHolder_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    BeanWithTypeSerializerHolder holder = new BeanWithTypeSerializerHolder();
    holder.setHolder(new BeanWithTypeSerializer("BeanWithTypeSerializer"));
    holder.setHolder2(new BeanWithTypeSerializer("BeanWithTypeSerializer2"));
    assertEquals(
        "{\"holder\":{\"__value__\":\"BeanWithTypeSerializer\"},\"holder2\":{\"__value2__\":\"BeanWithTypeSerializer2\"}}",
        mapper.toJSON(holder));
    assertEquals(
        "{\"holder\":{\"__value__\":\"BeanWithTypeSerializer\"},\"holder2\":{\"__value2__\":\"BeanWithTypeSerializer2\"}}",
        mapper.toJSON(mapper.fromJSON(mapper.toJSON(holder))));
  }
}
