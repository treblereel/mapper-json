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

package org.treblereel.gwt.json.mapper.annotations.jsonmappers.packagelevel;

import java.util.Objects;

public class PackageExternalBean {

  private String data;
  private int count;

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PackageExternalBean that = (PackageExternalBean) o;
    return count == that.count && Objects.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(data, count);
  }
}
