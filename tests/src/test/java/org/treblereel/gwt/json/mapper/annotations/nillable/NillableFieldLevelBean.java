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
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class NillableFieldLevelBean {

  @JsonbNillable private String nillableName;

  private String regularName;

  @JsonbNillable private Integer nillableNumber;

  private Integer regularNumber;

  @JsonbNillable private List<String> nillableList;

  private List<String> regularList;

  @JsonbNillable private Color nillableColor;

  private Color regularColor;

  public String getNillableName() {
    return nillableName;
  }

  public void setNillableName(String nillableName) {
    this.nillableName = nillableName;
  }

  public String getRegularName() {
    return regularName;
  }

  public void setRegularName(String regularName) {
    this.regularName = regularName;
  }

  public Integer getNillableNumber() {
    return nillableNumber;
  }

  public void setNillableNumber(Integer nillableNumber) {
    this.nillableNumber = nillableNumber;
  }

  public Integer getRegularNumber() {
    return regularNumber;
  }

  public void setRegularNumber(Integer regularNumber) {
    this.regularNumber = regularNumber;
  }

  public List<String> getNillableList() {
    return nillableList;
  }

  public void setNillableList(List<String> nillableList) {
    this.nillableList = nillableList;
  }

  public List<String> getRegularList() {
    return regularList;
  }

  public void setRegularList(List<String> regularList) {
    this.regularList = regularList;
  }

  public Color getNillableColor() {
    return nillableColor;
  }

  public void setNillableColor(Color nillableColor) {
    this.nillableColor = nillableColor;
  }

  public Color getRegularColor() {
    return regularColor;
  }

  public void setRegularColor(Color regularColor) {
    this.regularColor = regularColor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NillableFieldLevelBean that = (NillableFieldLevelBean) o;
    return Objects.equals(nillableName, that.nillableName)
        && Objects.equals(regularName, that.regularName)
        && Objects.equals(nillableNumber, that.nillableNumber)
        && Objects.equals(regularNumber, that.regularNumber)
        && Objects.equals(nillableList, that.nillableList)
        && Objects.equals(regularList, that.regularList)
        && nillableColor == that.nillableColor
        && regularColor == that.regularColor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        nillableName,
        regularName,
        nillableNumber,
        regularNumber,
        nillableList,
        regularList,
        nillableColor,
        regularColor);
  }
}
