/*
 * Copyright © 2023 Treblereel
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

package org.treblereel.gwt.json.mapper.annotations.propertyorder;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import java.util.Objects;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@J2clTestInput(JsonbPropertyOrderTest.class)
public class JsonbPropertyOrderTest {

  private static final JsonbPropertyOrderTest_ValueHolder_JsonMapperImpl mapper =
      JsonbPropertyOrderTest_ValueHolder_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    ValueHolder valueHolder = new ValueHolder();
    assertEquals("{}", mapper.toJSON(valueHolder));
    assertEquals(valueHolder, mapper.fromJSON(mapper.toJSON(valueHolder)));
  }

  @Test
  public void test2() {
    ValueHolder valueHolder = new ValueHolder("a", "b", "c", "d", "e", "f", "g");
    String expected =
        "{\"value3\":\"c\",\"value2\":\"b\",\"value\":\"a\",\"value4\":\"d\",\"value5\":\"e\",\"value6\":\"f\",\"value7\":\"g\"}";
    System.out.println(valueHolder);

    assertEquals(expected, mapper.toJSON(valueHolder));
    assertEquals(valueHolder, mapper.fromJSON(mapper.toJSON(valueHolder)));
  }

  @JSONMapper
  @JsonbPropertyOrder({"value3", "value2", "value", "scores", "friends", "projects"})
  public static class ValueHolder {
    private String value;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private String value6;
    private String value7;

    public ValueHolder() {}

    public ValueHolder(
        String value,
        String value2,
        String value3,
        String value4,
        String value5,
        String value6,
        String value7) {
      this.value = value;
      this.value2 = value2;
      this.value3 = value3;
      this.value4 = value4;
      this.value5 = value5;
      this.value6 = value6;
      this.value7 = value7;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public String getValue2() {
      return value2;
    }

    public void setValue2(String value2) {
      this.value2 = value2;
    }

    public String getValue3() {
      return value3;
    }

    public void setValue3(String value3) {
      this.value3 = value3;
    }

    public String getValue4() {
      return value4;
    }

    public void setValue4(String value4) {
      this.value4 = value4;
    }

    public String getValue5() {
      return value5;
    }

    public void setValue5(String value5) {
      this.value5 = value5;
    }

    public String getValue6() {
      return value6;
    }

    public void setValue6(String value6) {
      this.value6 = value6;
    }

    public String getValue7() {
      return value7;
    }

    public void setValue7(String value7) {
      this.value7 = value7;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ValueHolder that = (ValueHolder) o;
      return Objects.equals(value, that.value)
          && Objects.equals(value2, that.value2)
          && Objects.equals(value3, that.value3)
          && Objects.equals(value4, that.value4)
          && Objects.equals(value5, that.value5)
          && Objects.equals(value6, that.value6)
          && Objects.equals(value7, that.value7);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value, value2, value3, value4, value5, value6, value7);
    }

    @Override
    public String toString() {
      return "ValueHolder{"
          + "value='"
          + value
          + '\''
          + ", value2='"
          + value2
          + '\''
          + ", value3='"
          + value3
          + '\''
          + ", value4='"
          + value4
          + '\''
          + ", value5='"
          + value5
          + '\''
          + ", value6='"
          + value6
          + '\''
          + ", value7='"
          + value7
          + '\''
          + '}';
    }
  }
}
