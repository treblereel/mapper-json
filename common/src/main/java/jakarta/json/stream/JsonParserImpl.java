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

import java.math.BigDecimal;

public class JsonParserImpl implements JsonParser {

  public JsonParserImpl() {}

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public Event next() {
    return null;
  }

  @Override
  public String getString() {
    return null;
  }

  @Override
  public boolean isIntegralNumber() {
    return false;
  }

  @Override
  public int getInt() {
    return 0;
  }

  @Override
  public long getLong() {
    return 0;
  }

  @Override
  public BigDecimal getBigDecimal() {
    return null;
  }

  @Override
  public JsonLocation getLocation() {
    return null;
  }

  @Override
  public void close() {}
}
