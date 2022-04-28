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

package org.treblereel.gwt.json.mapper.array;

import java.util.Arrays;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class ArrayBoxedBean {

  private Integer[] _int;

  private Long[] _long;

  private Double[] _double;

  private Character[] _char;

  private Float[] _float;

  private Boolean[] _boolean;

  private Short[] _short;

  private String[] _string;

  public Integer[] get_int() {
    return _int;
  }

  public void set_int(Integer[] _int) {
    this._int = _int;
  }

  public Long[] get_long() {
    return _long;
  }

  public void set_long(Long[] _long) {
    this._long = _long;
  }

  public Double[] get_double() {
    return _double;
  }

  public void set_double(Double[] _double) {
    this._double = _double;
  }

  public Character[] get_char() {
    return _char;
  }

  public void set_char(Character[] _char) {
    this._char = _char;
  }

  public Float[] get_float() {
    return _float;
  }

  public void set_float(Float[] _float) {
    this._float = _float;
  }

  public Boolean[] get_boolean() {
    return _boolean;
  }

  public void set_boolean(Boolean[] _boolean) {
    this._boolean = _boolean;
  }

  public Short[] get_short() {
    return _short;
  }

  public void set_short(Short[] _short) {
    this._short = _short;
  }

  public String[] get_string() {
    return _string;
  }

  public void set_string(String[] _string) {
    this._string = _string;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArrayBoxedBean that = (ArrayBoxedBean) o;
    return Arrays.equals(_int, that._int)
        && Arrays.equals(_long, that._long)
        && Arrays.equals(_double, that._double)
        && Arrays.equals(_char, that._char)
        && Arrays.equals(_float, that._float)
        && Arrays.equals(_boolean, that._boolean)
        && Arrays.equals(_short, that._short)
        && Arrays.equals(_string, that._string);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(_int);
    result = 31 * result + Arrays.hashCode(_long);
    result = 31 * result + Arrays.hashCode(_double);
    result = 31 * result + Arrays.hashCode(_char);
    result = 31 * result + Arrays.hashCode(_float);
    result = 31 * result + Arrays.hashCode(_boolean);
    result = 31 * result + Arrays.hashCode(_short);
    result = 31 * result + Arrays.hashCode(_string);
    return result;
  }

  @Override
  public String toString() {
    return "ArrayBoxedBean{"
        + "_int="
        + Arrays.toString(_int)
        + ", _long="
        + Arrays.toString(_long)
        + ", _double="
        + Arrays.toString(_double)
        + ", _char="
        + Arrays.toString(_char)
        + ", _float="
        + Arrays.toString(_float)
        + ", _boolean="
        + Arrays.toString(_boolean)
        + ", _short="
        + Arrays.toString(_short)
        + ", _short="
        + Arrays.toString(_string)
        + '}';
  }
}
