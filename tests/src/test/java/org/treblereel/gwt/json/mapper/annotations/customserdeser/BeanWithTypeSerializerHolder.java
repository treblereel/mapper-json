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

package org.treblereel.gwt.json.mapper.annotations.customserdeser;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class BeanWithTypeSerializerHolder {

  @JsonbTypeSerializer(BeanWithTypeSerializerJsonbTypeSerializer.class)
  @JsonbTypeDeserializer(BeanWithTypeSerializerJsonbTypeDeserializer.class)
  private BeanWithTypeSerializer holder;

  private BeanWithTypeSerializer holder2;

  public BeanWithTypeSerializer getHolder() {
    return holder;
  }

  public void setHolder(BeanWithTypeSerializer holder) {
    this.holder = holder;
  }

  public BeanWithTypeSerializer getHolder2() {
    return holder2;
  }

  public void setHolder2(BeanWithTypeSerializer holder2) {
    this.holder2 = holder2;
  }
}
