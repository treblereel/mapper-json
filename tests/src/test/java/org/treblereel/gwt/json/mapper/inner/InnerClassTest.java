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

package org.treblereel.gwt.json.mapper.inner;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@J2clTestInput(InnerClassTest.class)
public class InnerClassTest {

  private final InnerClassTest_Bean_JsonMapperImpl mapper =
      new InnerClassTest_Bean_JsonMapperImpl();

  @Test
  public void test() {
    Bean tested = new Bean();
    assertEquals("{}", mapper.toJSON(tested));
    tested.setTestEnum(TestEnum.two);
    tested.setType("test");
    assertEquals("{\"type\":\"test\",\"testEnum\":\"two\"}", mapper.toJSON(tested));
  }

  @JSONMapper
  public static class Bean {

    private String type;

    private TestEnum testEnum;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public TestEnum getTestEnum() {
      return testEnum;
    }

    public void setTestEnum(TestEnum testEnum) {
      this.testEnum = testEnum;
    }
  }

  public static enum TestEnum {
    one,
    two;
  }
}
