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
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
@JsonbNillable
public class NillableOverrideBean {

  private String nillableName;

  @JsonbNillable(false)
  private String nonNillableName;

  private Integer nillableNumber;

  @JsonbNillable(false)
  private Integer nonNillableNumber;

  private Color nillableColor;

  @JsonbNillable(false)
  private Color nonNillableColor;

  public String getNillableName() {
    return nillableName;
  }

  public void setNillableName(String nillableName) {
    this.nillableName = nillableName;
  }

  public String getNonNillableName() {
    return nonNillableName;
  }

  public void setNonNillableName(String nonNillableName) {
    this.nonNillableName = nonNillableName;
  }

  public Integer getNillableNumber() {
    return nillableNumber;
  }

  public void setNillableNumber(Integer nillableNumber) {
    this.nillableNumber = nillableNumber;
  }

  public Integer getNonNillableNumber() {
    return nonNillableNumber;
  }

  public void setNonNillableNumber(Integer nonNillableNumber) {
    this.nonNillableNumber = nonNillableNumber;
  }

  public Color getNillableColor() {
    return nillableColor;
  }

  public void setNillableColor(Color nillableColor) {
    this.nillableColor = nillableColor;
  }

  public Color getNonNillableColor() {
    return nonNillableColor;
  }

  public void setNonNillableColor(Color nonNillableColor) {
    this.nonNillableColor = nonNillableColor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NillableOverrideBean that = (NillableOverrideBean) o;
    return Objects.equals(nillableName, that.nillableName)
        && Objects.equals(nonNillableName, that.nonNillableName)
        && Objects.equals(nillableNumber, that.nillableNumber)
        && Objects.equals(nonNillableNumber, that.nonNillableNumber)
        && nillableColor == that.nillableColor
        && nonNillableColor == that.nonNillableColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        nillableName,
        nonNillableName,
        nillableNumber,
        nonNillableNumber,
        nillableColor,
        nonNillableColor);
  }
}
