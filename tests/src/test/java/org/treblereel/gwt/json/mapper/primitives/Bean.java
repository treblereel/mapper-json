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

package org.treblereel.gwt.json.mapper.primitives;

import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class Bean {
  private String name;
  private int _int;
  private long _long;
  private double _double;
  private char _char;
  private float _float;
  private boolean _boolean;
  private short _short;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int get_int() {
    return _int;
  }

  public void set_int(int _int) {
    this._int = _int;
  }

  public long get_long() {
    return _long;
  }

  public void set_long(long _long) {
    this._long = _long;
  }

  public double get_double() {
    return _double;
  }

  public void set_double(double _double) {
    this._double = _double;
  }

  public char get_char() {
    return _char;
  }

  public void set_char(char _char) {
    this._char = _char;
  }

  public float get_float() {
    return _float;
  }

  public void set_float(float _float) {
    this._float = _float;
  }

  public boolean is_boolean() {
    return _boolean;
  }

  public void set_boolean(boolean _boolean) {
    this._boolean = _boolean;
  }

  public short get_short() {
    return _short;
  }

  public void set_short(short _short) {
    this._short = _short;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bean bean = (Bean) o;
    return _int == bean._int
        && _long == bean._long
        && Double.compare(bean._double, _double) == 0
        && _char == bean._char
        && Float.compare(bean._float, _float) == 0
        && _boolean == bean._boolean
        && _short == bean._short
        && Objects.equals(name, bean.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, _int, _long, _double, _char, _float, _boolean, _short);
  }
}
