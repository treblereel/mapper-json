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

package org.treblereel.gwt.json.mapper.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class MyBeanWithEnumCollections {

  private Set[] array;
  private java.util.Set<Set> set;

  private List<Type> list;

  public Set[] getArray() {
    return array;
  }

  public void setArray(Set[] array) {
    this.array = array;
  }

  public java.util.Set<Set> getSet() {
    return set;
  }

  public void setSet(java.util.Set<Set> set) {
    this.set = set;
  }

  public List<Type> getList() {
    return list;
  }

  public void setList(List<Type> list) {
    this.list = list;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyBeanWithEnumCollections that = (MyBeanWithEnumCollections) o;
    return Arrays.equals(array, that.array)
        && Objects.equals(set, that.set)
        && Objects.equals(list, that.list);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(set, list);
    result = 31 * result + Arrays.hashCode(array);
    return result;
  }
}
