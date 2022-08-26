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

package org.treblereel.gwt.json.mapper.collections.list;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.HashSet;
import org.junit.Test;

@J2clTestInput(BoxedTypesTest.class)
public class BoxedTypesTest {

  public static final BoxedTypes_JsonMapperImpl INSTANCE = new BoxedTypes_JsonMapperImpl();

  @Test
  public void test1() {
    BoxedTypes boxedTypes = new BoxedTypes();
    assertEquals("{}", INSTANCE.toJSON(boxedTypes));
    boxedTypes.setDoubleList(java.util.Arrays.asList(1.0, 2.0, 3.0));
    boxedTypes.setBooleanList(java.util.Arrays.asList(true, false, true));
    boxedTypes.setIntList(java.util.Arrays.asList(1, 2, 3));
    boxedTypes.setStringList(java.util.Arrays.asList("a", "b", "c"));
    boxedTypes.setCharList(java.util.Arrays.asList('a', 'b', 'c'));
    boxedTypes.setByteList(java.util.Arrays.asList((byte) 1, (byte) 2, (byte) 3));
    boxedTypes.setShortList(java.util.Arrays.asList((short) 1, (short) 2, (short) 3));
    boxedTypes.setLongList(java.util.Arrays.asList(1L, 2L, 3L));
    boxedTypes.setFloatList(java.util.Arrays.asList(1.0f, 2.0f, 3.0f));

    HashSet setByteSet = new HashSet();
    setByteSet.addAll(java.util.Arrays.asList((byte) 1, (byte) 2, (byte) 3));
    boxedTypes.setByteSet(setByteSet);

    HashSet setShortSet = new HashSet();
    setShortSet.addAll(java.util.Arrays.asList((short) 1, (short) 2, (short) 3));
    boxedTypes.setShortSet(setShortSet);

    HashSet setIntegerSet = new HashSet();
    setIntegerSet.addAll(java.util.Arrays.asList(1, 2, 3));
    boxedTypes.setIntSet(setIntegerSet);

    HashSet setLongSet = new HashSet();
    setLongSet.addAll(java.util.Arrays.asList(1L, 2L, 3L));
    boxedTypes.setLongSet(setLongSet);

    HashSet setFloatSet = new HashSet();
    setFloatSet.addAll(java.util.Arrays.asList(1.0f, 2.0f, 3.0f));
    boxedTypes.setFloatSet(setFloatSet);

    HashSet setDoubleSet = new HashSet();
    setDoubleSet.addAll(java.util.Arrays.asList(1.0, 2.0, 3.0));
    boxedTypes.setDoubleSet(setDoubleSet);

    HashSet setBooleanSet = new HashSet();
    setBooleanSet.addAll(java.util.Arrays.asList(true, false, true));
    boxedTypes.setBooleanSet(setBooleanSet);

    HashSet setStringSet = new HashSet();
    setStringSet.addAll(java.util.Arrays.asList("a", "b", "c"));
    boxedTypes.setStringSet(setStringSet);

    HashSet setCharSet = new HashSet();
    setCharSet.addAll(java.util.Arrays.asList('a', 'b', 'c'));
    boxedTypes.setCharSet(setCharSet);

    assertEquals(boxedTypes, INSTANCE.fromJSON(INSTANCE.toJSON(boxedTypes)));
  }
}
