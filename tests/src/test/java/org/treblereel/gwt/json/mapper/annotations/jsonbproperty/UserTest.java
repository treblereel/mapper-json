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

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;

@J2clTestInput(UserTest.class)
public class UserTest {

  User_JsonMapperImpl mapper = User_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    User tested = new User();

    tested.setName("John");
    tested.setSurname("Doe");
    tested.setEmail("any@one.com");
    tested.setScores(new int[] {1, 2, 3});
    tested.setFriends(Arrays.asList("John", "Jane"));
    tested.setProjects(
        new HashSet<String>() {
          {
            add("Project");
          }
        });

    assertEquals(
        "{\"_name\":\"John\",\"_secondName\":\"Doe\",\"__email\":\"any@one.com\",\"values\":[1,2,3],\"FRIENDS\":[\"John\",\"Jane\"],\"PROJECTS\":[\"Project\"]}",
        mapper.toJSON(tested));

    assertEquals(tested, mapper.fromJSON(mapper.toJSON(tested)));
  }
}
