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

package org.treblereel.gwt.json.mapper.boxed;

import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class BoxedBean {

  private Integer _int;

  private Long _long;

  private Double _double;

  private Character _char;

  private Float _float;

  private Boolean _boolean;

  private Short _short;

  public Integer get_int() {
    return _int;
  }

  public void set_int(Integer _int) {
    this._int = _int;
  }

  public Long get_long() {
    return _long;
  }

  public void set_long(Long _long) {
    this._long = _long;
  }

  public Double get_double() {
    return _double;
  }

  public void set_double(Double _double) {
    this._double = _double;
  }

  public Character get_char() {
    return _char;
  }

  public void set_char(Character _char) {
    this._char = _char;
  }

  public Float get_float() {
    return _float;
  }

  public void set_float(Float _float) {
    this._float = _float;
  }

  public Boolean get_boolean() {
    return _boolean;
  }

  public void set_boolean(Boolean _boolean) {
    this._boolean = _boolean;
  }

  public Short get_short() {
    return _short;
  }

  public void set_short(Short _short) {
    this._short = _short;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BoxedBean boxedBean = (BoxedBean) o;
    return Objects.equals(_int, boxedBean._int)
        && Objects.equals(_long, boxedBean._long)
        && Objects.equals(_double, boxedBean._double)
        && Objects.equals(_char, boxedBean._char)
        && Objects.equals(_float, boxedBean._float)
        && Objects.equals(_boolean, boxedBean._boolean)
        && Objects.equals(_short, boxedBean._short);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_int, _long, _double, _char, _float, _boolean, _short);
  }
}
