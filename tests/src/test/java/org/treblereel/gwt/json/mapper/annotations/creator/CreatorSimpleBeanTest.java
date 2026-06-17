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

package org.treblereel.gwt.json.mapper.annotations.creator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(CreatorSimpleBeanTest.class)
public class CreatorSimpleBeanTest {

  CreatorSimpleBean_JsonMapperImpl mapper = CreatorSimpleBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testSerialize() {
    CreatorSimpleBean bean = new CreatorSimpleBean("Alice", 30);
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":\"Alice\""));
    assertTrue(result.contains("\"age\":30"));
  }

  @Test
  public void testDeserialize() {
    String json = "{\"name\":\"Bob\",\"age\":25}";
    CreatorSimpleBean bean = mapper.fromJSON(json);
    assertEquals("Bob", bean.getName());
    assertEquals(25, bean.getAge());
  }

  @Test
  public void testRoundTrip() {
    CreatorSimpleBean bean = new CreatorSimpleBean("Charlie", 42);
    String json = mapper.toJSON(bean);
    CreatorSimpleBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testNullName() {
    String json = "{\"age\":10}";
    CreatorSimpleBean bean = mapper.fromJSON(json);
    assertEquals(null, bean.getName());
    assertEquals(10, bean.getAge());
  }

  @Test
  public void testDefaultAge() {
    String json = "{\"name\":\"test\"}";
    CreatorSimpleBean bean = mapper.fromJSON(json);
    assertEquals("test", bean.getName());
    assertEquals(0, bean.getAge());
  }
}
