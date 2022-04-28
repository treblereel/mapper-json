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

package org.treblereel.gwt.json.mapper.pojos;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PojosTest {

  BeanOne_JsonMapperImpl mapper = BeanOne_JsonMapperImpl.INSTANCE;

  @Test
  public void test1() {
    BeanOne bean = new BeanOne();
    assertEquals("{}", mapper.toJSON(bean));

    bean.setName("name");
    bean.setValue("value");

    String json = mapper.toJSON(bean);

    assertEquals("{\"name\":\"name\",\"value\":\"value\"}", json);
    assertEquals(bean, mapper.fromJSON(json));
  }

  @Test
  public void test2() {
    BeanOne bean = new BeanOne();
    assertEquals("{}", mapper.toJSON(bean));

    bean.setName("name");
    bean.setValue("value");

    BeanTwo two = new BeanTwo();
    bean.setBeanTwo(two);

    assertEquals("{\"name\":\"name\",\"value\":\"value\",\"beanTwo\":{}}", mapper.toJSON(bean));
    assertEquals(bean, mapper.fromJSON(mapper.toJSON(bean)));
  }

  @Test
  public void test3() {
    BeanOne bean = new BeanOne();
    assertEquals("{}", mapper.toJSON(bean));

    bean.setName("name");
    bean.setValue("value");

    BeanTwo two = new BeanTwo();
    bean.setBeanTwo(two);

    two.setName("simple_name");
    two.setAge(8888);
    two.setAddress("qwerty_asdfgh");

    assertEquals(
        "{\"name\":\"name\",\"value\":\"value\",\"beanTwo\":{\"name\":\"simple_name\",\"age\":8888,\"address\":\"qwerty_asdfgh\"}}",
        mapper.toJSON(bean));
  }

  @Test
  public void test4() {
    BeanOne bean = new BeanOne();
    assertEquals("{}", mapper.toJSON(bean));

    bean.setName("name");
    bean.setValue("value");

    BeanTwo two = new BeanTwo();
    bean.setBeanTwo(two);

    two.setName("simple_name");
    two.setAge(8888);
    two.setAddress("qwerty_asdfgh");

    BeanThree three = new BeanThree();
    three.setId("ID");
    two.setBeanThree(three);

    assertEquals(
        "{\"name\":\"name\",\"value\":\"value\",\"beanTwo\":{\"name\":\"simple_name\",\"age\":8888,\"address\":\"qwerty_asdfgh\",\"beanThree\":{\"id\":\"ID\"}}}",
        mapper.toJSON(bean));
  }
}
