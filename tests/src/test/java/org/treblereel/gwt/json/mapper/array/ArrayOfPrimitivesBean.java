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
public class ArrayOfPrimitivesBean {

  private int[] _int;

  private long[] _long;

  private double[] _double;

  private char[] _char;

  private float[] _float;

  private boolean[] _boolean;

  private short[] _short;

  public int[] get_int() {
    return _int;
  }

  public void set_int(int[] _int) {
    this._int = _int;
  }

  public long[] get_long() {
    return _long;
  }

  public void set_long(long[] _long) {
    this._long = _long;
  }

  public double[] get_double() {
    return _double;
  }

  public void set_double(double[] _double) {
    this._double = _double;
  }

  public char[] get_char() {
    return _char;
  }

  public void set_char(char[] _char) {
    this._char = _char;
  }

  public float[] get_float() {
    return _float;
  }

  public void set_float(float[] _float) {
    this._float = _float;
  }

  public boolean[] get_boolean() {
    return _boolean;
  }

  public void set_boolean(boolean[] _boolean) {
    this._boolean = _boolean;
  }

  public short[] get_short() {
    return _short;
  }

  public void set_short(short[] _short) {
    this._short = _short;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ArrayOfPrimitivesBean that = (ArrayOfPrimitivesBean) o;
    return Arrays.equals(_int, that._int)
        && Arrays.equals(_long, that._long)
        && Arrays.equals(_double, that._double)
        && Arrays.equals(_char, that._char)
        && Arrays.equals(_float, that._float)
        && Arrays.equals(_boolean, that._boolean)
        && Arrays.equals(_short, that._short);
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
    return result;
  }
}
