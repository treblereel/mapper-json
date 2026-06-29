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
public class CreatorWithNestedBean {

  private final String title;
  private final CreatorInnerBean inner;

  @JsonbCreator
  public CreatorWithNestedBean(
      @JsonbProperty("title") String title, @JsonbProperty("inner") CreatorInnerBean inner) {
    this.title = title;
    this.inner = inner;
  }

  public String getTitle() {
    return title;
  }

  public CreatorInnerBean getInner() {
    return inner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CreatorWithNestedBean that = (CreatorWithNestedBean) o;
    return Objects.equals(title, that.title) && Objects.equals(inner, that.inner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, inner);
  }
}
