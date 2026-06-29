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

package org.treblereel.gwt.json.mapper.annotations.jsonmappers.packagelevel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(PackageExternalBeanTest.class)
public class PackageExternalBeanTest {

  PackageExternalBean_JsonMapperImpl mapper = PackageExternalBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testSerialize() {
    PackageExternalBean bean = new PackageExternalBean();
    bean.setData("hello");
    bean.setCount(5);
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"data\":\"hello\""));
    assertTrue(result.contains("\"count\":5"));
  }

  @Test
  public void testDeserialize() {
    String json = "{\"data\":\"world\",\"count\":10}";
    PackageExternalBean bean = mapper.fromJSON(json);
    assertEquals("world", bean.getData());
    assertEquals(10, bean.getCount());
  }

  @Test
  public void testRoundTrip() {
    PackageExternalBean bean = new PackageExternalBean();
    bean.setData("test");
    bean.setCount(42);
    String json = mapper.toJSON(bean);
    PackageExternalBean from = mapper.fromJSON(json);
    assertEquals(bean, from);
  }

  @Test
  public void testEmptyBean() {
    PackageExternalBean bean = new PackageExternalBean();
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"count\":0"));
  }
}
