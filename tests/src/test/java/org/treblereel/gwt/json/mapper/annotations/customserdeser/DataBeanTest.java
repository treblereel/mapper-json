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

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

@J2clTestInput(DataBeanTest.class)
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

    Translation translation2 = new Translation("POINT_11", "POINT_22");
    bean.setHolder2(translation2);

    assertEquals(
        "{\"holder\":{\"from\":\"POINT_1\",\"to\":\"POINT_2\"},\"holder2_qwerty\":{\"from\":\"POINT_11\",\"to\":\"POINT_22\"}}",
        mapper.toJSON(bean));
    assertEquals(bean, mapper.fromJSON(mapper.toJSON(bean)));
  }

  @Test
  public void test3() {
    DataBean bean = new DataBean();
    bean.setHolder(new Translation("POINT_0", "POINT_0"));
    Translation translation = new Translation("POINT_1", "POINT_2");
    Translation translation2 = new Translation("POINT_3", "POINT_4");
    bean.setHolder(new Translation("VALUE_0", "VALUE_1"));

    Translation[] arr = new Translation[] {new Translation("ARRAY_1", "ARRAY_2")};
    bean.setArray(arr);

    List<Object> list = Arrays.asList(translation, translation2);
    bean.setList(list);

    String json = mapper.toJSON(bean);

    // DomGlobal.console.log("json: " + json);

    assertEquals(
        "{\"holder\":{\"from\":\"VALUE_0\",\"to\":\"VALUE_1\"},\"array\":[{\"from\":\"ARRAY_1\",\"to\":\"ARRAY_2\"}],\"list\":[{\"from\":\"POINT_1\",\"to\":\"POINT_2\"},{\"from\":\"POINT_3\",\"to\":\"POINT_4\"}]}",
        json);

    assertEquals(new Translation("VALUE_0", "VALUE_1"), mapper.fromJSON(json).getHolder());
    assertEquals(new Translation("ARRAY_1", "ARRAY_2"), mapper.fromJSON(json).getArray()[0]);
    assertEquals(translation, mapper.fromJSON(json).getList().get(0));
    assertEquals(mapper.toJSON(bean), mapper.toJSON(mapper.fromJSON(json)));
    assertEquals(bean, mapper.fromJSON(json));
  }
}
