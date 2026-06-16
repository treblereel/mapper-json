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

import jakarta.json.bind.annotation.JsonbNillable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
@JsonbNillable
public class NillableCollectionsBean {

  private List<String> stringList;
  private List<Integer> integerList;
  private Set<String> stringSet;
  private List<Color> colorList;

  public List<String> getStringList() {
    return stringList;
  }

  public void setStringList(List<String> stringList) {
    this.stringList = stringList;
  }

  public List<Integer> getIntegerList() {
    return integerList;
  }

  public void setIntegerList(List<Integer> integerList) {
    this.integerList = integerList;
  }

  public Set<String> getStringSet() {
    return stringSet;
  }

  public void setStringSet(Set<String> stringSet) {
    this.stringSet = stringSet;
  }

  public List<Color> getColorList() {
    return colorList;
  }

  public void setColorList(List<Color> colorList) {
    this.colorList = colorList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NillableCollectionsBean that = (NillableCollectionsBean) o;
    return Objects.equals(stringList, that.stringList)
        && Objects.equals(integerList, that.integerList)
        && Objects.equals(stringSet, that.stringSet)
        && Objects.equals(colorList, that.colorList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stringList, integerList, stringSet, colorList);
  }
}
