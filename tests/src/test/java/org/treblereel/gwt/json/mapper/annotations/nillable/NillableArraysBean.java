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
import java.util.Arrays;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
@JsonbNillable
public class NillableArraysBean {

  private int[] intArray;
  private String[] stringArray;
  private Integer[] boxedIntArray;
  private Color[] colorArray;

  public int[] getIntArray() {
    return intArray;
  }

  public void setIntArray(int[] intArray) {
    this.intArray = intArray;
  }

  public String[] getStringArray() {
    return stringArray;
  }

  public void setStringArray(String[] stringArray) {
    this.stringArray = stringArray;
  }

  public Integer[] getBoxedIntArray() {
    return boxedIntArray;
  }

  public void setBoxedIntArray(Integer[] boxedIntArray) {
    this.boxedIntArray = boxedIntArray;
  }

  public Color[] getColorArray() {
    return colorArray;
  }

  public void setColorArray(Color[] colorArray) {
    this.colorArray = colorArray;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NillableArraysBean that = (NillableArraysBean) o;
    return Arrays.equals(intArray, that.intArray)
        && Arrays.equals(stringArray, that.stringArray)
        && Arrays.equals(boxedIntArray, that.boxedIntArray)
        && Arrays.equals(colorArray, that.colorArray);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(intArray);
    result = 31 * result + Arrays.hashCode(stringArray);
    result = 31 * result + Arrays.hashCode(boxedIntArray);
    result = 31 * result + Arrays.hashCode(colorArray);
    return result;
  }
}
