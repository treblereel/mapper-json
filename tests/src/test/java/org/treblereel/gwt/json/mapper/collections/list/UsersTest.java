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

package org.treblereel.gwt.json.mapper.collections.list;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.collections.User;

public class UsersTest {

  public static final Users_JsonMapperImpl INSTANCE1 = new Users_JsonMapperImpl();
  public static final UsersWithArrayList_JsonMapperImpl INSTANCE2 =
      new UsersWithArrayList_JsonMapperImpl();

  @Test
  public void test1() {
    Users users = new Users();
    assertEquals("{}", INSTANCE1.toJSON(users));
    assertEquals(users, INSTANCE1.fromJSON("{}"));
  }

  @Test
  public void test2() {
    Users users = new Users();

    User user = new User();
    user.setFirstName("John");
    user.setSecondName("Doe");
    user.setIsMarried(true);

    users.setUsers(java.util.Arrays.asList(user));

    String rez = INSTANCE1.toJSON(users);

    assertEquals(
        "{\"users\":[{\"firstName\":\"John\",\"secondName\":\"Doe\",\"isMarried\":true}]}", rez);
    assertEquals(users, INSTANCE1.fromJSON(rez));
  }

  @Test
  public void test3() {
    Users users = new Users();

    User user = new User();
    user.setFirstName("John");
    user.setSecondName("Doe");
    user.setIsMarried(true);

    User user1 = new User();
    user1.setFirstName("Bob");
    user1.setSecondName("Green");
    user1.setIsMarried(false);

    users.setUsers(java.util.Arrays.asList(user, user1));

    String rez = INSTANCE1.toJSON(users);

    assertEquals(
        "{\"users\":[{\"firstName\":\"John\",\"secondName\":\"Doe\",\"isMarried\":true},{\"firstName\":\"Bob\",\"secondName\":\"Green\",\"isMarried\":false}]}",
        rez);
    assertEquals(users, INSTANCE1.fromJSON(rez));
  }

  @Test
  public void test4() {
    UsersWithArrayList users = new UsersWithArrayList();
    assertEquals("{}", INSTANCE2.toJSON(users));
    assertEquals(users, INSTANCE2.fromJSON("{}"));
  }

  @Test
  public void test5() {
    UsersWithArrayList users = new UsersWithArrayList();

    User user = new User();
    user.setFirstName("John");
    user.setSecondName("Doe");
    user.setIsMarried(true);

    ArrayList list = new ArrayList();
    list.add(user);
    users.setUsers(list);

    String rez = INSTANCE2.toJSON(users);

    assertEquals(
        "{\"users\":[{\"firstName\":\"John\",\"secondName\":\"Doe\",\"isMarried\":true}]}", rez);
    assertEquals(users, INSTANCE2.fromJSON(rez));
  }

  @Test
  public void test6() {
    UsersWithArrayList users = new UsersWithArrayList();

    User user = new User();
    user.setFirstName("John");
    user.setSecondName("Doe");
    user.setIsMarried(true);

    User user1 = new User();
    user1.setFirstName("Bob");
    user1.setSecondName("Green");
    user1.setIsMarried(false);

    ArrayList list = new ArrayList();
    list.add(user);
    list.add(user1);
    users.setUsers(list);

    String rez = INSTANCE2.toJSON(users);

    assertEquals(
        "{\"users\":[{\"firstName\":\"John\",\"secondName\":\"Doe\",\"isMarried\":true},{\"firstName\":\"Bob\",\"secondName\":\"Green\",\"isMarried\":false}]}",
        rez);
    assertEquals(users, INSTANCE2.fromJSON(rez));
  }
}
