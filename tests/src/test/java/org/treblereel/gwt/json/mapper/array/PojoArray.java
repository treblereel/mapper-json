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

package org.treblereel.gwt.json.mapper.array;

import java.util.Arrays;
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.pojos.BeanOne;

@JSONMapper
public class PojoArray {

  private BeanOne[] arr;
  private String id;

  public BeanOne[] getArr() {
    return arr;
  }

  public void setArr(BeanOne[] arr) {
    this.arr = arr;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PojoArray pojoArray = (PojoArray) o;
    return Arrays.equals(arr, pojoArray.arr) && Objects.equals(id, pojoArray.id);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(id);
    result = 31 * result + Arrays.hashCode(arr);
    return result;
  }

  @Override
  public String toString() {
    return "PojoArray{" + "arr=" + Arrays.toString(arr) + ", id='" + id + '\'' + '}';
  }
}
