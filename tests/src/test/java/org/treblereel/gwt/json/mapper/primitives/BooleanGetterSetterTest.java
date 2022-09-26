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

package org.treblereel.gwt.json.mapper.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Objects;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@J2clTestInput(BooleanGetterSetterTest.class)
public class BooleanGetterSetterTest {

  private static final BooleanGetterSetterTest_BooleanGetterSetter_JsonMapperImpl mapper =
      new BooleanGetterSetterTest_BooleanGetterSetter_JsonMapperImpl();

  @Test
  public void test() {
    BooleanGetterSetter tested = new BooleanGetterSetter();
    tested.setVal1(true);
    tested.setVal2(true);
    tested.setVal3(true);
    tested.setVal4(true);

    String json = mapper.toJSON(tested);
    assertEquals("{\"val1\":true,\"val2\":true,\"val3\":true,\"val4\":true}", json);
    BooleanGetterSetter fromJson = mapper.fromJSON(json);
    assertTrue(tested.equals(fromJson));
  }

  @JSONMapper
  public static class BooleanGetterSetter {

    private Boolean val1;
    private Boolean val2;
    private boolean val3;
    private boolean val4;

    public Boolean getVal1() {
      return val1;
    }

    public void setVal1(Boolean val1) {
      this.val1 = val1;
    }

    public Boolean isVal2() {
      return val2;
    }

    public void setVal2(Boolean val2) {
      this.val2 = val2;
    }

    public boolean getVal3() {
      return val3;
    }

    public void setVal3(boolean val3) {
      this.val3 = val3;
    }

    public boolean isVal4() {
      return val4;
    }

    public void setVal4(boolean val4) {
      this.val4 = val4;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      BooleanGetterSetter that = (BooleanGetterSetter) o;
      return val3 == that.val3
          && val4 == that.val4
          && Objects.equals(val1, that.val1)
          && Objects.equals(val2, that.val2);
    }

    @Override
    public int hashCode() {
      return Objects.hash(val1, val2, val3, val4);
    }
  }
}
