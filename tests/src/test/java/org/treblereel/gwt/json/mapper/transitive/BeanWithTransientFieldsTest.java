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

package org.treblereel.gwt.json.mapper.transitive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.treblereel.gwt.json.mapper.pojos.BeanOne;
import org.treblereel.gwt.json.mapper.pojos.BeanTwo;

public class BeanWithTransientFieldsTest {

  BeanWithTransientFields_JsonMapperImpl mapper = BeanWithTransientFields_JsonMapperImpl.INSTANCE;

  private final String expected = "{\"field1\":\"AAAA\",\"field3\":\"AAAA\"}";

  @Test
  public void test1() {
    BeanWithTransientFields beanWithTransientFields = new BeanWithTransientFields();
    beanWithTransientFields.setField1("AAAA");
    beanWithTransientFields.setField2("AAAA");
    beanWithTransientFields.setField3("AAAA");

    BeanOne beanOne = new BeanOne();
    beanOne.setName("WWWWW");
    beanOne.setValue("VVVVVV");

    beanWithTransientFields.setBeanOne(beanOne);

    BeanTwo beanTwo = new BeanTwo();
    beanTwo.setName("FFFF");
    beanTwo.setAge(1111);

    BeanTwo beanTwo2 = new BeanTwo();
    beanTwo2.setName("FFFF");
    beanTwo2.setAge(1111);

    beanWithTransientFields.setArr2(new BeanTwo[] {beanTwo, beanTwo2});

    beanWithTransientFields.setArr(new int[] {222, 333, 444, 55, 666});

    String result = mapper.toJSON(beanWithTransientFields);
    assertEquals(expected, result);
    assertEquals(expected, mapper.toJSON(mapper.fromJSON(mapper.toJSON(beanWithTransientFields))));
  }
}
