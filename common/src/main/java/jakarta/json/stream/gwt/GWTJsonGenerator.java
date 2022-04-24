/*
 * Copyright 2022 Dmitrii Tikhomirov
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

package jakarta.json.stream.gwt;

import jakarta.json.JsonValue;
import jakarta.json.stream.JsonGenerator;
import java.math.BigDecimal;
import java.math.BigInteger;

public class GWTJsonGenerator implements JsonGenerator {

  @Override
  public JsonGenerator writeStartObject() {
    return null;
  }

  @Override
  public JsonGenerator writeStartObject(String name) {
    return null;
  }

  @Override
  public JsonGenerator writeKey(String name) {
    return null;
  }

  @Override
  public JsonGenerator writeStartArray() {
    return null;
  }

  @Override
  public JsonGenerator writeStartArray(String name) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, JsonValue value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, String value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, BigInteger value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, BigDecimal value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, int value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, long value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, double value) {
    return null;
  }

  @Override
  public JsonGenerator write(String name, boolean value) {
    return null;
  }

  @Override
  public JsonGenerator writeNull(String name) {
    return null;
  }

  @Override
  public JsonGenerator writeEnd() {
    return null;
  }

  @Override
  public JsonGenerator write(JsonValue value) {
    return null;
  }

  @Override
  public JsonGenerator write(String value) {
    return null;
  }

  @Override
  public JsonGenerator write(BigDecimal value) {
    return null;
  }

  @Override
  public JsonGenerator write(BigInteger value) {
    return null;
  }

  @Override
  public JsonGenerator write(int value) {
    return null;
  }

  @Override
  public JsonGenerator write(long value) {
    return null;
  }

  @Override
  public JsonGenerator write(double value) {
    return null;
  }

  @Override
  public JsonGenerator write(boolean value) {
    return null;
  }

  @Override
  public JsonGenerator writeNull() {
    return null;
  }

  @Override
  public void close() {}

  @Override
  public void flush() {}
}
