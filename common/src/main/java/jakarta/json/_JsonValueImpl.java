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

import elemental2.core.JsObject;

public class _JsonValueImpl implements JsonValue {

  JsObject holder;

  public _JsonValueImpl(Object holder) {
    this(new JsObject(holder));
  }

  public _JsonValueImpl(JsObject holder) {
    this.holder = holder;
  }

  @Override
  public ValueType getValueType() {
    throw new UnsupportedOperationException();
  }
}
