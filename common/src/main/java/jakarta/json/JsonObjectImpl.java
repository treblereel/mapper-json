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

package jakarta.json;

import elemental2.core.Global;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import jsinterop.base.Js;
import jsinterop.base.JsPropertyMap;

public class JsonObjectImpl implements JsonObject {

  private JsPropertyMap holder;

  public JsonObjectImpl(String json) {
    this(Global.JSON.parse(json));
  }

  public JsonObjectImpl(Object holder) {
    this(Js.asPropertyMap(holder));
  }

  public JsonObjectImpl(JsPropertyMap holder) {
    this.holder = holder;
  }

  @Override
  public JsonArray getJsonArray(String name) {
    holder.get(name);
    return null;
  }

  @Override
  public JsonObject getJsonObject(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonNumber getJsonNumber(String name) {
    return new JsonNumberImpl(holder.get(name));
  }

  @Override
  public JsonString getJsonString(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getString(String name) {
    return holder.get(name).toString();
  }

  @Override
  public String getString(String name, String defaultValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getInt(String name) {
    return Integer.valueOf(holder.get(name).toString());
  }

  @Override
  public int getInt(String name, int defaultValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean getBoolean(String name) {
    return Boolean.valueOf(holder.get(name).toString());
  }

  @Override
  public boolean getBoolean(String name, boolean defaultValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isNull(String name) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ValueType getValueType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isEmpty() {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean containsKey(Object key) {
    return holder.has(key.toString());
  }

  @Override
  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonValue get(Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonValue put(String key, JsonValue value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public JsonValue remove(Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void putAll(Map<? extends String, ? extends JsonValue> m) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<String> keySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<JsonValue> values() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Set<Entry<String, JsonValue>> entrySet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return Global.JSON.stringify(holder);
  }
}
