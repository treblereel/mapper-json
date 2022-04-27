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

package org.treblereel.gwt.json.mapper.pojos;

import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class BeanOne {

  private String name;
  private String value;
  private BeanTwo beanTwo;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public BeanTwo getBeanTwo() {
    return beanTwo;
  }

  public void setBeanTwo(BeanTwo beanTwo) {
    this.beanTwo = beanTwo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BeanOne beanOne = (BeanOne) o;
    return Objects.equals(name, beanOne.name)
        && Objects.equals(value, beanOne.value)
        && Objects.equals(beanTwo, beanOne.beanTwo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, value, beanTwo);
  }

  @Override
  public String toString() {
    return "BeanOne{"
        + "name='"
        + name
        + '\''
        + ", value='"
        + value
        + '\''
        + ", beanTwo="
        + beanTwo
        + '}';
  }
}
