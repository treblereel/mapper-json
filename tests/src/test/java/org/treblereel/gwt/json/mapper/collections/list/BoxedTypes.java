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

package org.treblereel.gwt.json.mapper.collections.list;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;

@JSONMapper
public class BoxedTypes {

  private List<Double> doubleList;

  private List<Float> floatList;

  private List<Long> longList;

  private List<Integer> intList;

  private List<Short> shortList;

  private List<Byte> byteList;

  private List<Character> charList;

  private List<Boolean> booleanList;

  private List<String> stringList;

  private Set<Double> doubleSet;

  private Set<Float> floatSet;

  private Set<Long> longSet;

  private Set<Integer> intSet;

  private Set<Short> shortSet;

  private Set<Byte> byteSet;

  private Set<Character> charSet;

  private Set<Boolean> booleanSet;

  private Set<String> stringSet;

  public List<Double> getDoubleList() {
    return doubleList;
  }

  public void setDoubleList(List<Double> doubleList) {
    this.doubleList = doubleList;
  }

  public List<Float> getFloatList() {
    return floatList;
  }

  public void setFloatList(List<Float> floatList) {
    this.floatList = floatList;
  }

  public List<Long> getLongList() {
    return longList;
  }

  public void setLongList(List<Long> longList) {
    this.longList = longList;
  }

  public List<Integer> getIntList() {
    return intList;
  }

  public void setIntList(List<Integer> intList) {
    this.intList = intList;
  }

  public List<Short> getShortList() {
    return shortList;
  }

  public void setShortList(List<Short> shortList) {
    this.shortList = shortList;
  }

  public List<Byte> getByteList() {
    return byteList;
  }

  public void setByteList(List<Byte> byteList) {
    this.byteList = byteList;
  }

  public List<Character> getCharList() {
    return charList;
  }

  public void setCharList(List<Character> charList) {
    this.charList = charList;
  }

  public List<Boolean> getBooleanList() {
    return booleanList;
  }

  public void setBooleanList(List<Boolean> booleanList) {
    this.booleanList = booleanList;
  }

  public List<String> getStringList() {
    return stringList;
  }

  public void setStringList(List<String> stringList) {
    this.stringList = stringList;
  }

  public Set<Double> getDoubleSet() {
    return doubleSet;
  }

  public void setDoubleSet(Set<Double> doubleSet) {
    this.doubleSet = doubleSet;
  }

  public Set<Float> getFloatSet() {
    return floatSet;
  }

  public void setFloatSet(Set<Float> floatSet) {
    this.floatSet = floatSet;
  }

  public Set<Long> getLongSet() {
    return longSet;
  }

  public void setLongSet(Set<Long> longSet) {
    this.longSet = longSet;
  }

  public Set<Integer> getIntSet() {
    return intSet;
  }

  public void setIntSet(Set<Integer> intSet) {
    this.intSet = intSet;
  }

  public Set<Short> getShortSet() {
    return shortSet;
  }

  public void setShortSet(Set<Short> shortSet) {
    this.shortSet = shortSet;
  }

  public Set<Byte> getByteSet() {
    return byteSet;
  }

  public void setByteSet(Set<Byte> byteSet) {
    this.byteSet = byteSet;
  }

  public Set<Character> getCharSet() {
    return charSet;
  }

  public void setCharSet(Set<Character> charSet) {
    this.charSet = charSet;
  }

  public Set<Boolean> getBooleanSet() {
    return booleanSet;
  }

  public void setBooleanSet(Set<Boolean> booleanSet) {
    this.booleanSet = booleanSet;
  }

  public Set<String> getStringSet() {
    return stringSet;
  }

  public void setStringSet(Set<String> stringSet) {
    this.stringSet = stringSet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BoxedTypes that = (BoxedTypes) o;
    return Objects.equals(doubleList, that.doubleList)
        && Objects.equals(floatList, that.floatList)
        && Objects.equals(longList, that.longList)
        && Objects.equals(intList, that.intList)
        && Objects.equals(shortList, that.shortList)
        && Objects.equals(byteList, that.byteList)
        && Objects.equals(charList, that.charList)
        && Objects.equals(booleanList, that.booleanList)
        && Objects.equals(stringList, that.stringList)
        && Objects.equals(doubleSet, that.doubleSet)
        && Objects.equals(floatSet, that.floatSet)
        && Objects.equals(longSet, that.longSet)
        && Objects.equals(intSet, that.intSet)
        && Objects.equals(shortSet, that.shortSet)
        && Objects.equals(byteSet, that.byteSet)
        && Objects.equals(charSet, that.charSet)
        && Objects.equals(booleanSet, that.booleanSet)
        && Objects.equals(stringSet, that.stringSet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        doubleList,
        floatList,
        longList,
        intList,
        shortList,
        byteList,
        charList,
        booleanList,
        stringList,
        doubleSet,
        floatSet,
        longSet,
        intSet,
        shortSet,
        byteSet,
        charSet,
        booleanSet,
        stringSet);
  }
}
