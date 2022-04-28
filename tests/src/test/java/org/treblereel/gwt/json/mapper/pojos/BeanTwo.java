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

public class BeanTwo {

  private String name;
  private Integer age;
  private String address;
  private BeanThree beanThree;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public BeanThree getBeanThree() {
    return beanThree;
  }

  public void setBeanThree(BeanThree beanThree) {
    this.beanThree = beanThree;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BeanTwo beanTwo = (BeanTwo) o;
    return age == beanTwo.age
        && Objects.equals(name, beanTwo.name)
        && Objects.equals(address, beanTwo.address)
        && Objects.equals(beanThree, beanTwo.beanThree);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, address, beanThree);
  }

  @Override
  public String toString() {
    return "BeanTwo{"
        + "name='"
        + name
        + '\''
        + ", age="
        + age
        + ", address='"
        + address
        + '\''
        + ", beanThree="
        + beanThree
        + '}';
  }
}
