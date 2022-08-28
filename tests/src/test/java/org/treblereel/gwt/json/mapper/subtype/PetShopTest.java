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
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

@J2clTestInput(PetShopTest.class)
public class PetShopTest {

  private final PetShop_JsonMapperImpl mapper = PetShop_JsonMapperImpl.INSTANCE;

  @Test
  public void test() {
    Cat cat = new Cat();
    cat.setIsCat(true);

    Rat rat = new Rat();
    rat.setIsRat(true);

    Dog dog = new Dog();
    dog.setIsDog(false);

    List<Animal> animalList = new ArrayList<>();
    animalList.add(cat);
    animalList.add(rat);
    animalList.add(dog);

    PetShop shop = new PetShop();
    shop.setAnimalList(animalList);

    assertEquals(
        "{\"animalList\":[{\"isCat\":true,\"@type\":\"cat\"},{\"isRat\":true,\"@type\":\"rat\"},{\"isDog\":false,\"@type\":\"dog\"}]}",
        mapper.toJSON(shop));

    assertEquals(mapper.toJSON(shop), mapper.toJSON(mapper.fromJSON(mapper.toJSON(shop))));
  }
}
