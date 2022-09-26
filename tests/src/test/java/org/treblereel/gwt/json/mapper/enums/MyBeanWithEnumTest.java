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

package org.treblereel.gwt.json.mapper.enums;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

@J2clTestInput(MyBeanWithEnumTest.class)
public class MyBeanWithEnumTest {

  private static final MyBeanWithEnum_JsonMapperImpl mapper = new MyBeanWithEnum_JsonMapperImpl();
  private static final MyBeanWithEnumCollections_JsonMapperImpl mapper2 =
      new MyBeanWithEnumCollections_JsonMapperImpl();

  @Test
  public void test() {
    MyBeanWithEnum tested = new MyBeanWithEnum();
    assertEquals("{}", mapper.toJSON(tested));
    assertEquals(tested, mapper.fromJSON("{}"));

    tested.setType(Type.TYPE_TWO);
    String expected = "{\"type\":\"TYPE_TWO\"}";
    assertEquals(expected, mapper.toJSON(tested));
    assertEquals(tested, mapper.fromJSON(mapper.toJSON(tested)));
    assertEquals(expected, mapper.toJSON(mapper.fromJSON(mapper.toJSON(tested))));
  }

  @Test
  public void test2() {
    Set[] arr = new Set[] {Set.ONE, Set.TWO, Set.ONE};
    MyBeanWithEnumCollections tested = new MyBeanWithEnumCollections();
    assertEquals("{}", mapper2.toJSON(tested));
    assertEquals(tested, mapper2.fromJSON("{}"));
    tested.setArray(arr);
    assertEquals("{\"array\":[\"ONE\",\"TWO\",\"ONE\"]}", mapper2.toJSON(tested));
    assertEquals(tested, mapper2.fromJSON(mapper2.toJSON(tested)));

    List<Type> typeList = new ArrayList<>();
    typeList.add(Type.TYPE_ONE);
    typeList.add(Type.TYPE_ONE);
    typeList.add(Type.TYPE_TWO);
    typeList.add(Type.TYPE_ONE);
    typeList.add(Type.TYPE_ONE);
    tested.setList(typeList);

    assertEquals(
        "{\"array\":[\"ONE\",\"TWO\",\"ONE\"],\"list\":[\"TYPE_ONE\",\"TYPE_ONE\",\"TYPE_TWO\",\"TYPE_ONE\",\"TYPE_ONE\"]}",
        mapper2.toJSON(tested));
    assertEquals(tested, mapper2.fromJSON(mapper2.toJSON(tested)));
  }
}
