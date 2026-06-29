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

package org.treblereel.gwt.json.mapper.annotations.nillable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Arrays;
import org.junit.Test;

@J2clTestInput(NillableCollectionsBeanTest.class)
public class NillableCollectionsBeanTest {

  NillableCollectionsBean_JsonMapperImpl mapper = NillableCollectionsBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    NillableCollectionsBean bean = new NillableCollectionsBean();
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"stringList\":null"));
    assertTrue(result.contains("\"integerList\":null"));
    assertTrue(result.contains("\"stringSet\":null"));
    assertTrue(result.contains("\"colorList\":null"));
  }

  @Test
  public void testPartialNulls() {
    NillableCollectionsBean bean = new NillableCollectionsBean();
    bean.setStringList(Arrays.asList("a", "b"));
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"stringList\":[\"a\",\"b\"]"));
    assertTrue(result.contains("\"integerList\":null"));
    assertTrue(result.contains("\"stringSet\":null"));
    assertTrue(result.contains("\"colorList\":null"));
  }

  @Test
  public void testWithValues() {
    NillableCollectionsBean bean = new NillableCollectionsBean();
    bean.setStringList(Arrays.asList("hello", "world"));
    bean.setIntegerList(Arrays.asList(1, 2, 3));
    bean.setColorList(Arrays.asList(Color.RED, Color.BLUE));

    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"stringSet\":null"));
    NillableCollectionsBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testRoundTripAllNulls() {
    NillableCollectionsBean bean = new NillableCollectionsBean();
    String result = mapper.toJSON(bean);
    NillableCollectionsBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
