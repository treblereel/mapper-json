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

package org.treblereel.gwt.json.mapper.subtype;

import static org.junit.Assert.assertEquals;

import com.google.j2cl.junit.apt.J2clTestInput;
import org.junit.Test;

@J2clTestInput(CradleTest.class)
public class CradleTest {

  private final Cradle_JsonMapperImpl mapper = Cradle_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    Cradle cradle = new Cradle();
    Cat cat = new Cat();
    cat.setIsCat(true);
    cradle.setAnimal(cat);
    assertEquals("{\"animal\":{\"isCat\":true,\"@type\":\"cat\"}}", mapper.toJSON(cradle));

    Cradle cradle1 = mapper.fromJSON(mapper.toJSON(cradle));
    assertEquals(cradle, cradle1);
    assertEquals(mapper.toJSON(cradle), mapper.toJSON(cradle1));
  }
}
