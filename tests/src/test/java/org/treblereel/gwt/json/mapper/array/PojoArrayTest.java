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
import org.treblereel.gwt.json.mapper.pojos.BeanOne;
import org.treblereel.gwt.json.mapper.pojos.BeanThree;
import org.treblereel.gwt.json.mapper.pojos.BeanTwo;

public class PojoArrayTest {

  PojoArray_JsonMapperImpl mapper = PojoArray_JsonMapperImpl.INSTANCE;

  @Test
  public void test1() {
    PojoArray arr = new PojoArray();

    BeanOne beanOne1 = new BeanOne();
    beanOne1.setName("AAAAA");
    beanOne1.setValue("VVVVV");

    BeanOne beanOne2 = new BeanOne();
    beanOne2.setName("AAAAA2");
    beanOne2.setValue("VVVVV2");

    BeanOne beanOne3 = new BeanOne();
    beanOne3.setName("AAAAA3");
    beanOne3.setValue("VVVVV3");

    BeanOne beanOne4 = new BeanOne();
    beanOne4.setName("AAAAA4");
    beanOne4.setValue("VVVV4");

    arr.setId("ZZZZZZ");
    arr.setArr(new BeanOne[] {beanOne1, beanOne2, beanOne3, beanOne4});

    assertEquals(
        "{\"arr\":[{\"name\":\"AAAAA\",\"value\":\"VVVVV\"},{\"name\":\"AAAAA2\",\"value\":\"VVVVV2\"},{\"name\":\"AAAAA3\",\"value\":\"VVVVV3\"},{\"name\":\"AAAAA4\",\"value\":\"VVVV4\"}],\"id\":\"ZZZZZZ\"}",
        mapper.toJSON(arr));

    assertEquals(arr, mapper.fromJSON(mapper.toJSON(arr)));
  }

  @Test
  public void test2() {
    PojoArray arr = new PojoArray();

    BeanOne beanOne1 = new BeanOne();
    beanOne1.setName("AAAAA");
    beanOne1.setValue("VVVVV");

    BeanOne beanOne2 = new BeanOne();
    beanOne2.setName("AAAAA2");
    beanOne2.setValue("VVVVV2");

    BeanTwo beanTwo = new BeanTwo();
    beanTwo.setAge(111111);
    beanTwo.setName("BeanTwo");

    beanOne1.setBeanTwo(beanTwo);
    beanOne2.setBeanTwo(beanTwo);

    arr.setId("ZZZZZZ");
    arr.setArr(new BeanOne[] {beanOne1, beanOne2});

    String expected =
        "{\"arr\":[{\"name\":\"AAAAA\",\"value\":\"VVVVV\",\"beanTwo\":{\"name\":\"BeanTwo\",\"age\":111111}},{\"name\":\"AAAAA2\",\"value\":\"VVVVV2\",\"beanTwo\":{\"name\":\"BeanTwo\",\"age\":111111}}],\"id\":\"ZZZZZZ\"}";

    assertEquals(expected, mapper.toJSON(arr));
    assertEquals(expected, mapper.toJSON(mapper.fromJSON(mapper.toJSON(arr))));
  }

  @Test
  public void test3() {
    PojoArray arr = new PojoArray();

    BeanOne beanOne1 = new BeanOne();
    beanOne1.setName("AAAAA");
    beanOne1.setValue("VVVVV");

    BeanOne beanOne2 = new BeanOne();
    beanOne2.setName("AAAAA2");
    beanOne2.setValue("VVVVV2");

    BeanTwo beanTwo = new BeanTwo();
    beanTwo.setAge(111111);
    beanTwo.setName("BeanTwo");

    BeanThree beanThree = new BeanThree();
    beanThree.setId("my_id");

    beanTwo.setBeanThree(beanThree);

    beanOne1.setBeanTwo(beanTwo);
    beanOne2.setBeanTwo(beanTwo);

    arr.setId("id");
    arr.setArr(new BeanOne[] {beanOne1, beanOne2});

    System.out.println("JSON  " + mapper.toJSON(arr));

    String expected =
        "{\"arr\":[{\"name\":\"AAAAA\",\"value\":\"VVVVV\",\"beanTwo\":{\"name\":\"BeanTwo\",\"age\":111111,\"beanThree\":{\"id\":\"my_id\"}}},{\"name\":\"AAAAA2\",\"value\":\"VVVVV2\",\"beanTwo\":{\"name\":\"BeanTwo\",\"age\":111111,\"beanThree\":{\"id\":\"my_id\"}}}],\"id\":\"id\"}";
    assertEquals(expected, mapper.toJSON(arr));

    assertEquals(expected, mapper.toJSON(mapper.fromJSON(mapper.toJSON(arr))));
  }
}
