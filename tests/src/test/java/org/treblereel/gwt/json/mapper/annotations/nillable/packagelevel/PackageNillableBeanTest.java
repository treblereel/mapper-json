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

package org.treblereel.gwt.json.mapper.annotations.nillable.packagelevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Arrays;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotations.nillable.Color;

@J2clTestInput(PackageNillableBeanTest.class)
public class PackageNillableBeanTest {

  PackageNillableBean_JsonMapperImpl mapper = PackageNillableBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    PackageNillableBean bean = new PackageNillableBean();
    String result = mapper.toJSON(bean);
    // package-level @JsonbNillable should make all null fields appear
    assertTrue(result.contains("\"name\":null"));
    assertTrue(result.contains("\"number\":null"));
    assertTrue(result.contains("\"color\":null"));
    assertTrue(result.contains("\"items\":null"));
  }

  @Test
  public void testPartialValues() {
    PackageNillableBean bean = new PackageNillableBean();
    bean.setName("hello");
    bean.setColor(Color.RED);
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":\"hello\""));
    assertTrue(result.contains("\"color\":\"RED\""));
    assertTrue(result.contains("\"number\":null"));
    assertTrue(result.contains("\"items\":null"));
  }

  @Test
  public void testWithAllValues() {
    PackageNillableBean bean = new PackageNillableBean();
    bean.setName("test");
    bean.setNumber(42);
    bean.setColor(Color.GREEN);
    bean.setItems(Arrays.asList("a", "b"));

    String result = mapper.toJSON(bean);
    PackageNillableBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testRoundTripAllNulls() {
    PackageNillableBean bean = new PackageNillableBean();
    String result = mapper.toJSON(bean);
    PackageNillableBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
