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

package org.treblereel.gwt.json.mapper.annotations.jsonmappers;

import java.util.Objects;

public class ExternalWithNestedBean {

  private String title;
  private ExternalInnerBean inner;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public ExternalInnerBean getInner() {
    return inner;
  }

  public void setInner(ExternalInnerBean inner) {
    this.inner = inner;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExternalWithNestedBean that = (ExternalWithNestedBean) o;
    return Objects.equals(title, that.title) && Objects.equals(inner, that.inner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, inner);
  }
}
