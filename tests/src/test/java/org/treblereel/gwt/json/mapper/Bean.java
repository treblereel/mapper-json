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

package org.treblereel.gwt.json.mapper;

import java.util.Objects;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class Bean {
  private String name;
  private Integer age;

  private int age2;

  private long llong;

  private double ddouble;

  private char cchar;

  private float ffloat;

  private boolean bboolean;

  private short sshort;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public int getAge2() {
    return age2;
  }

  public void setAge2(int age2) {
    this.age2 = age2;
  }

  public long getLlong() {
    return llong;
  }

  public void setLlong(long llong) {
    this.llong = llong;
  }

  public double getDdouble() {
    return ddouble;
  }

  public void setDdouble(double ddouble) {
    this.ddouble = ddouble;
  }

  public char getCchar() {
    return cchar;
  }

  public void setCchar(char cchar) {
    this.cchar = cchar;
  }

  public float getFfloat() {
    return ffloat;
  }

  public void setFfloat(float ffloat) {
    this.ffloat = ffloat;
  }

  public boolean isBboolean() {
    return bboolean;
  }

  public void setBboolean(boolean bboolean) {
    this.bboolean = bboolean;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bean bean = (Bean) o;
    return age2 == bean.age2 && Objects.equals(name, bean.name) && Objects.equals(age, bean.age);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, age, age2);
  }

  public short getSshort() {
    return sshort;
  }

  public void setSshort(short sshort) {
    this.sshort = sshort;
  }
}
