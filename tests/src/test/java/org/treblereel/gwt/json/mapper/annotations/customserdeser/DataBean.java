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
import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class DataBean {

  @JsonbTypeSerializer(ObjectJsonbTypeSerializer.class)
  @JsonbTypeDeserializer(ObjectJsonbTypeDeserializer.class)
  private Object holder;

  public Object getHolder() {
    return holder;
  }

  public void setHolder(Object holder) {
    this.holder = holder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataBean dataBean = (DataBean) o;
    return Objects.equals(holder, dataBean.holder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(holder);
  }
}
