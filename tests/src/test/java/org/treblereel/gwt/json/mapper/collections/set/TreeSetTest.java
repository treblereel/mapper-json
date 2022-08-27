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

package org.treblereel.gwt.json.mapper.collections.set;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.TreeSet;
import org.junit.Test;
import org.treblereel.gwt.json.mapper.collections.User;

@J2clTestInput(TreeSetTest.class)
public class TreeSetTest {

  public static final UsersTreeSet_JsonMapperImpl INSTANCE1 = new UsersTreeSet_JsonMapperImpl();

  @Test
  public void test1() {
    UsersTreeSet users = new UsersTreeSet();
    assertEquals("{}", INSTANCE1.toJSON(users));
    assertEquals(users, INSTANCE1.fromJSON("{}"));
  }

  @Test
  public void test2() {
    UsersTreeSet users = new UsersTreeSet();

    User user = new User();
    user.setFirstName("John");
    user.setSecondName("Doe");
    user.setIsMarried(true);

    TreeSet<User> usersSet = new TreeSet<>();
    usersSet.add(user);

    users.setUsers(usersSet);

    String rez = INSTANCE1.toJSON(users);

    assertEquals(
        "{\"users\":[{\"firstName\":\"John\",\"secondName\":\"Doe\",\"isMarried\":true}]}", rez);
    assertEquals(users, INSTANCE1.fromJSON(rez));
  }

  @Test
  public void test3() {
    UsersTreeSet users = new UsersTreeSet();

    User user = new User();
    user.setFirstName("John");
    user.setSecondName("Doe");
    user.setIsMarried(true);

    User user1 = new User();
    user1.setFirstName("Bob");
    user1.setSecondName("Green");
    user1.setIsMarried(false);

    TreeSet<User> usersSet = new TreeSet<>();
    usersSet.add(user);
    usersSet.add(user1);

    users.setUsers(usersSet);

    String rez = INSTANCE1.toJSON(users);

    assertEquals(
        "{\"users\":[{\"firstName\":\"John\",\"secondName\":\"Doe\",\"isMarried\":true},{\"firstName\":\"Bob\",\"secondName\":\"Green\",\"isMarried\":false}]}",
        rez);
    assertEquals(users, INSTANCE1.fromJSON(rez));
  }
}
