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

package org.treblereel.gwt.json.mapper.transitive;

import java.util.Arrays;
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.pojos.BeanOne;
import org.treblereel.gwt.json.mapper.pojos.BeanTwo;

@JSONMapper
public class BeanWithTransientFields {

  private String field1;

  private static String id = "_id";

  private transient String field2;

  private String field3;

  private transient BeanOne beanOne;

  private transient int[] arr;

  private transient BeanTwo[] arr2;

  public String getField1() {
    return field1;
  }

  public void setField1(String field1) {
    this.field1 = field1;
  }

  public static String getId() {
    return id;
  }

  public static void setId(String id) {
    BeanWithTransientFields.id = id;
  }

  public String getField2() {
    return field2;
  }

  public void setField2(String field2) {
    this.field2 = field2;
  }

  public String getField3() {
    return field3;
  }

  public void setField3(String field3) {
    this.field3 = field3;
  }

  public BeanOne getBeanOne() {
    return beanOne;
  }

  public void setBeanOne(BeanOne beanOne) {
    this.beanOne = beanOne;
  }

  public int[] getArr() {
    return arr;
  }

  public void setArr(int[] arr) {
    this.arr = arr;
  }

  public BeanTwo[] getArr2() {
    return arr2;
  }

  public void setArr2(BeanTwo[] arr2) {
    this.arr2 = arr2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BeanWithTransientFields that = (BeanWithTransientFields) o;
    return Objects.equals(field1, that.field1)
        && Objects.equals(field2, that.field2)
        && Objects.equals(field3, that.field3)
        && Objects.equals(beanOne, that.beanOne)
        && Arrays.equals(arr, that.arr)
        && Arrays.equals(arr2, that.arr2);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(field1, field2, field3, beanOne);
    result = 31 * result + Arrays.hashCode(arr);
    result = 31 * result + Arrays.hashCode(arr2);
    return result;
  }

  @Override
  public String toString() {
    return "BeanWithTransientFields{"
        + "field1='"
        + field1
        + '\''
        + ", field2='"
        + field2
        + '\''
        + ", field3='"
        + field3
        + '\''
        + ", beanOne="
        + beanOne
        + ", arr="
        + Arrays.toString(arr)
        + ", arr2="
        + Arrays.toString(arr2)
        + '}';
  }
}
