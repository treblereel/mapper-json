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

package org.treblereel.gwt.json.mapper;

import static org.junit.Assert.assertEquals;

import jakarta.json.JsonObject;
import org.junit.Test;

public class FirstTest {

  @Test
  public void test() {
    /*        JsonObject json = Json.createObjectBuilder()
            .add("name", "Falco")
            .add("age", BigDecimal.valueOf(3))
            .add("biteable", Boolean.FALSE)
            .build();
    String result = json.toString();*/

    Bean bean = new Bean();
    bean.setName("Falco");
    bean.setAge(3);
    bean.setBboolean(true);
    bean.setLlong(10l);
    bean.setDdouble(10d);
    bean.setCchar('a');
    bean.setFfloat(10f);

    Bean_JsonMapperImpl mapper = Bean_JsonMapperImpl.INSTANCE;
    JsonObject result = mapper.toJSON(bean);
    System.out.println("result: " + result.toString());

    Bean parsed = mapper.fromJSON(result);

    assertEquals(bean, parsed);
    assertEquals(bean.isBboolean(), parsed.isBboolean());
    assertEquals(bean.getLlong(), parsed.getLlong());
    assertEquals(bean.getDdouble(), parsed.getDdouble(), 0.0);
    assertEquals(bean.getCchar(), parsed.getCchar());
    assertEquals(bean.getFfloat(), parsed.getFfloat(), 0.1);
  }
}
