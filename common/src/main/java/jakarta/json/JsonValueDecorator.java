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

import elemental2.core.JsBoolean;
import elemental2.core.JsNumber;
import elemental2.core.JsObject;
import elemental2.core.JsString;
import java.math.BigDecimal;
import java.math.BigInteger;
import jsinterop.base.Js;
import org.treblereel.gwt.json.mapper.annotation.GwtIncompatible;

public class JsonValueDecorator {

  JreDecorator decorator = new JreDecorator();

  public JsonValueDecorator(Object object) {
    decorator.setDelegate(object);
  }

  public Integer asInteger() {
    return decorator.getInteger();
  }

  public Double asDouble() {
    return decorator.getDouble();
  }

  public Float asFloat() {
    return decorator.getFloat();
  }

  public Long asLong() {
    return decorator.getLong();
  }

  public Byte asByte() {
    return decorator.getByte();
  }

  public Short asShort() {
    return decorator.getShort();
  }

  public Character asCharacter() {
    return decorator.getCharacter();
  }

  public String asString() {
    return decorator.getString();
  }

  public Boolean asBoolean() {
    return decorator.getBoolean();
  }

  public BigInteger asBigInteger() {
    return decorator.getBigInteger();
  }

  public BigDecimal asBigDecimal() {
    return decorator.getBigDecimal();
  }

  private static class GWTDecorator {

    private JsObject delegate;

    private GWTDecorator setDelegate(Object delegate) {
      this.delegate = (JsObject) delegate;
      return this;
    };

    private Integer getInteger() {
      return ((Double) Js.<JsNumber>uncheckedCast(delegate).valueOf()).intValue();
    }

    private Double getDouble() {
      return Js.<JsNumber>uncheckedCast(delegate).valueOf();
    }

    private Float getFloat() {
      return ((Double) Js.<JsNumber>uncheckedCast(delegate).valueOf()).floatValue();
    }

    private Long getLong() {
      return ((Double) Js.<JsNumber>uncheckedCast(delegate).valueOf()).longValue();
    }

    private Byte getByte() {
      return ((Double) Js.<JsNumber>uncheckedCast(delegate).valueOf()).byteValue();
    }

    private Short getShort() {
      return (short) ((Double) Js.<JsNumber>uncheckedCast(delegate).valueOf()).intValue();
    }

    private Character getCharacter() {
      return (char) ((Double) Js.<JsNumber>uncheckedCast(delegate).valueOf()).intValue();
    }

    private String getString() {
      return Js.<JsString>uncheckedCast(delegate).toString_();
    }

    private Boolean getBoolean() {
      return Js.<JsBoolean>uncheckedCast(delegate).valueOf();
    }

    private BigInteger getBigInteger() {
      return BigDecimal.valueOf(Js.<JsNumber>uncheckedCast(delegate).valueOf()).toBigInteger();
    }

    private BigDecimal getBigDecimal() {
      return BigDecimal.valueOf(Js.<JsNumber>uncheckedCast(delegate).valueOf());
    }
  }

  private static class JreDecorator extends GWTDecorator {

    @GwtIncompatible private JsonValue delegate;

    @GwtIncompatible
    private JreDecorator setDelegate(Object delegate) {
      this.delegate = (JsonValue) delegate;
      return this;
    };

    @GwtIncompatible
    private Integer getInteger() {
      return ((JsonNumber) delegate).intValue();
    }

    @GwtIncompatible
    private Double getDouble() {
      return ((JsonNumber) delegate).doubleValue();
    }

    @GwtIncompatible
    private Float getFloat() {
      return ((JsonNumber) delegate).numberValue().floatValue();
    }

    @GwtIncompatible
    private Long getLong() {
      return ((JsonNumber) delegate).numberValue().longValue();
    }

    @GwtIncompatible
    private Byte getByte() {
      return ((JsonNumber) delegate).numberValue().byteValue();
    }

    @GwtIncompatible
    private Short getShort() {
      return (short) ((JsonNumber) delegate).intValue();
    }

    @GwtIncompatible
    private Character getCharacter() {
      return (char) ((JsonNumber) delegate).intValue();
    }

    @GwtIncompatible
    private String getString() {
      return ((JsonString) delegate).getString();
    }

    @GwtIncompatible
    private Boolean getBoolean() {
      return delegate.getValueType().equals(JsonValue.ValueType.TRUE) ? true : false;
    }

    @GwtIncompatible
    private BigInteger getBigInteger() {
      return ((JsonNumber) delegate).bigIntegerValue();
    }

    @GwtIncompatible
    private BigDecimal getBigDecimal() {
      return ((JsonNumber) delegate).bigDecimalValue();
    }
  }
}
