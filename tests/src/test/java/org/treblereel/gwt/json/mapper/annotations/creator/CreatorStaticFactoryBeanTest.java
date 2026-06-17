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

@J2clTestInput(CreatorStaticFactoryBeanTest.class)
public class CreatorStaticFactoryBeanTest {

  CreatorStaticFactoryBean_JsonMapperImpl mapper = CreatorStaticFactoryBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testSerialize() {
    CreatorStaticFactoryBean bean = CreatorStaticFactoryBean.create("test", 99);
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":\"test\""));
    assertTrue(result.contains("\"value\":99"));
  }

  @Test
  public void testDeserialize() {
    String json = "{\"name\":\"factory\",\"value\":7}";
    CreatorStaticFactoryBean bean = mapper.fromJSON(json);
    assertEquals("factory", bean.getName());
    assertEquals(7, bean.getValue());
  }

  @Test
  public void testRoundTrip() {
    CreatorStaticFactoryBean bean = CreatorStaticFactoryBean.create("round", 55);
    String json = mapper.toJSON(bean);
    CreatorStaticFactoryBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }
}
