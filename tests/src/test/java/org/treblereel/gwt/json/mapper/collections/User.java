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

package org.treblereel.gwt.json.mapper.collections;

import java.util.Objects;

public class User implements Comparable<User> {

  private String firstName;
  private String secondName;
  private boolean isMarried;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public boolean getIsMarried() {
    return isMarried;
  }

  public void setIsMarried(boolean married) {
    isMarried = married;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return isMarried == user.isMarried
        && Objects.equals(firstName, user.firstName)
        && Objects.equals(secondName, user.secondName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, secondName, isMarried);
  }

  @Override
  public int compareTo(User o) {
    return o.getFirstName().compareTo(firstName);
  }
}
