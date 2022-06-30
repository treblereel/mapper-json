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

package org.treblereel.gwt.json.mapper.annotations.customserdeser;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DataBeanTest {

  DataBean_JsonMapperImpl mapper = DataBean_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    DataBean bean = new DataBean();
    bean.setHolder(false);
    String json = mapper.toJSON(bean);
    assertEquals("{\"holder\":false}", json);

    DataBean bean2 = mapper.fromJSON(json);
    assertEquals(bean, bean2);

    bean.setHolder(true);
    json = mapper.toJSON(bean);
    assertEquals("{\"holder\":true}", json);
    bean2 = mapper.fromJSON(json);
    assertEquals(bean, bean2);
  }

  @Test
  public void test2() {
    DataBean bean = new DataBean();

    Translation translation = new Translation("POINT_1", "POINT_2");
    bean.setHolder(translation);

    String json = mapper.toJSON(bean);

    assertEquals("{\"holder\":{\"from\":\"POINT_1\",\"to\":\"POINT_2\"}}", json);

    assertEquals(bean, mapper.fromJSON(json));
  }
}
