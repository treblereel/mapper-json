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

package org.treblereel.gwt.json.mapper.cornercases;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(UnexpectedJsonValueTest.class)
public class UnexpectedJsonValueTest {

  Data_JsonMapperImpl mapper = Data_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    Data bean = new Data();
    bean.setName("name");
    bean.setValue("value");
    bean.setType("type");
    String result = mapper.toJSON(bean);

    assertEquals("{\"name\":\"name\",\"value\":\"value\",\"type\":\"type\"}", result);

    assertEquals(
        bean,
        mapper.fromJSON(
            "{\"name\":\"name\",\"value\":\"value\",\"type\":\"type\", \"unknown\":\"unknown\"}"));
  }
}
