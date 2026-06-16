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

package org.treblereel.gwt.json.mapper.annotations.jsonmappers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;

@J2clTestInput(ExternalCollectionsBeanTest.class)
public class ExternalCollectionsBeanTest {

  ExternalCollectionsBean_JsonMapperImpl mapper = ExternalCollectionsBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testSerializeWithValues() {
    ExternalCollectionsBean bean = new ExternalCollectionsBean();
    bean.setItems(Arrays.asList("a", "b", "c"));
    bean.setNumbers(new HashSet<>(Arrays.asList(1, 2, 3)));

    String json = mapper.toJSON(bean);
    assertTrue(json.contains("\"items\""));
    assertTrue(json.contains("\"numbers\""));
  }

  @Test
  public void testRoundTrip() {
    ExternalCollectionsBean bean = new ExternalCollectionsBean();
    bean.setItems(Arrays.asList("hello", "world"));
    bean.setNumbers(new HashSet<>(Arrays.asList(10, 20)));

    String json = mapper.toJSON(bean);
    ExternalCollectionsBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testNullCollections() {
    ExternalCollectionsBean bean = new ExternalCollectionsBean();
    String json = mapper.toJSON(bean);
    ExternalCollectionsBean from = mapper.fromJSON(json);
    assertNull(from.getItems());
    assertNull(from.getNumbers());
  }

  @Test
  public void testSingleElementCollections() {
    ExternalCollectionsBean bean = new ExternalCollectionsBean();
    bean.setItems(Arrays.asList("only"));
    bean.setNumbers(new HashSet<>(Arrays.asList(42)));

    String json = mapper.toJSON(bean);
    ExternalCollectionsBean from = mapper.fromJSON(json);
    assertEquals(1, from.getItems().size());
    assertEquals("only", from.getItems().get(0));
    assertEquals(1, from.getNumbers().size());
    assertTrue(from.getNumbers().contains(42));
  }
}
