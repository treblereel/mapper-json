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

package org.treblereel.gwt.json.mapper.chainedsetters;

import java.util.Arrays;
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class MyBean {

  private String name;
  private int type;
  private String description;
  private String[] tags;

  public String getName() {
    return name;
  }

  public MyBean setName(String name) {
    this.name = name;
    return this;
  }

  public int getType() {
    return type;
  }

  public MyBean setType(int type) {
    this.type = type;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public MyBean setDescription(String description) {
    this.description = description;
    return this;
  }

  public String[] getTags() {
    return tags;
  }

  public MyBean setTags(String[] tags) {
    this.tags = tags;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MyBean myBean = (MyBean) o;
    return type == myBean.type
        && Objects.equals(name, myBean.name)
        && Objects.equals(description, myBean.description)
        && Arrays.equals(tags, myBean.tags);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(name, type, description);
    result = 31 * result + Arrays.hashCode(tags);
    return result;
  }
}
