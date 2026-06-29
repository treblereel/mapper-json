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

package org.treblereel.gwt.json.mapper.annotations.creator;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class CreatorWithBoxedBean {

  private final String label;
  private final Integer count;
  private final Double score;
  private final Boolean active;

  @JsonbCreator
  public CreatorWithBoxedBean(
      @JsonbProperty("label") String label,
      @JsonbProperty("count") Integer count,
      @JsonbProperty("score") Double score,
      @JsonbProperty("active") Boolean active) {
    this.label = label;
    this.count = count;
    this.score = score;
    this.active = active;
  }

  public String getLabel() {
    return label;
  }

  public Integer getCount() {
    return count;
  }

  public Double getScore() {
    return score;
  }

  public Boolean getActive() {
    return active;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreatorWithBoxedBean that = (CreatorWithBoxedBean) o;
    return Objects.equals(label, that.label)
        && Objects.equals(count, that.count)
        && Objects.equals(score, that.score)
        && Objects.equals(active, that.active);
  }

  @Override
  public int hashCode() {
    return Objects.hash(label, count, score, active);
  }
}
