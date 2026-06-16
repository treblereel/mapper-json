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
import org.junit.Test;

@J2clTestInput(NillableNestedBeanTest.class)
public class NillableNestedBeanTest {

  NillableNestedBean_JsonMapperImpl mapper = NillableNestedBean_JsonMapperImpl.INSTANCE;

  @Test
  public void testAllNulls() {
    NillableNestedBean bean = new NillableNestedBean();
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":null"));
    assertTrue(result.contains("\"inner\":null"));
  }

  @Test
  public void testNullInner() {
    NillableNestedBean bean = new NillableNestedBean();
    bean.setName("test");
    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":\"test\""));
    assertTrue(result.contains("\"inner\":null"));
  }

  @Test
  public void testWithInner() {
    NillableNestedBean bean = new NillableNestedBean();
    bean.setName("outer");
    NillableInnerBean inner = new NillableInnerBean();
    inner.setValue("inner_val");
    inner.setNumber(99);
    bean.setInner(inner);

    String result = mapper.toJSON(bean);
    NillableNestedBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }

  @Test
  public void testWithInnerHavingNulls() {
    NillableNestedBean bean = new NillableNestedBean();
    bean.setName("outer");
    NillableInnerBean inner = new NillableInnerBean();
    // inner.value and inner.number are null
    // NillableInnerBean does NOT have @JsonbNillable, so its nulls are skipped
    bean.setInner(inner);

    String result = mapper.toJSON(bean);
    assertTrue(result.contains("\"name\":\"outer\""));
    assertTrue(result.contains("\"inner\":{}"));
  }

  @Test
  public void testRoundTripAllNulls() {
    NillableNestedBean bean = new NillableNestedBean();
    String result = mapper.toJSON(bean);
    NillableNestedBean from = mapper.fromJSON(result);
    assertEquals(bean, from);
  }
}
