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

package org.treblereel.gwt.json.mapper.chainedsetters;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(MyBeanTest.class)
public class MyBeanTest {

  @Test
  public void test() {
    MyBean bean =
        new MyBean()
            .setName("name")
            .setType(1)
            .setDescription("description")
            .setTags(new String[] {"tag1", "tag2"});

    String json = MyBean_JsonMapperImpl.INSTANCE.toJSON(bean);
    assertEquals(
        "{\"name\":\"name\",\"type\":1,\"description\":\"description\",\"tags\":[\"tag1\",\"tag2\"]}",
        json);
    assertEquals(bean, MyBean_JsonMapperImpl.INSTANCE.fromJSON(json));
  }
}
