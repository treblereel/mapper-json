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

package org.treblereel.gwt.json.mapper.annotations.jsonbproperty;

import jakarta.json.bind.annotation.JsonbProperty;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class User {

  @JsonbProperty("_name")
  private String name;

  @JsonbProperty("_secondName")
  private String surname;

  @JsonbProperty("__email")
  private String email;

  @JsonbProperty("values")
  private int[] scores;

  @JsonbProperty("FRIENDS")
  private List<String> friends;

  @JsonbProperty("PROJECTS")
  private Set<String> projects;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public int[] getScores() {
    return scores;
  }

  public void setScores(int[] scores) {
    this.scores = scores;
  }

  public List<String> getFriends() {
    return friends;
  }

  public void setFriends(List<String> friends) {
    this.friends = friends;
  }

  public Set<String> getProjects() {
    return projects;
  }

  public void setProjects(Set<String> projects) {
    this.projects = projects;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(name, user.name)
        && Objects.equals(surname, user.surname)
        && Objects.equals(email, user.email)
        && Arrays.equals(scores, user.scores)
        && Objects.equals(friends, user.friends)
        && Objects.equals(projects, user.projects);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(name, surname, email, friends, projects);
    result = 31 * result + Arrays.hashCode(scores);
    return result;
  }
}
