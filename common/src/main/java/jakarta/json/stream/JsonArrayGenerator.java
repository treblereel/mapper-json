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

package jakarta.json.stream;

import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonArrayGenerator implements JsonGenerator {

  private final JsonArrayBuilder arrayBuilder;
  private final JsonObjectBuilder builder;
  private final String name;

  public JsonArrayGenerator(JsonArrayBuilder arrayBuilder, JsonObjectBuilder builder, String name) {
    this.arrayBuilder = arrayBuilder;
    this.builder = builder;
    this.name = name;
  }

  @Override
  public JsonGenerator writeStartObject() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator writeStartObject(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator writeKey(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator writeStartArray() {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator writeStartArray(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, JsonValue value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, String value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, BigInteger value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, BigDecimal value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, int value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, long value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, double value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator write(String name, boolean value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator writeNull(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonGenerator writeEnd() {
    builder.add(name, arrayBuilder);
    return this;
  }

  @Override
  public JsonGenerator write(JsonValue value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(String value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(BigDecimal value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(BigInteger value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(int value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(long value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(double value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator write(boolean value) {
    arrayBuilder.add(value);
    return this;
  }

  @Override
  public JsonGenerator writeNull() {
    arrayBuilder.addNull();
    return this;
  }

  @Override
  public void close() {}

  @Override
  public void flush() {}
}
