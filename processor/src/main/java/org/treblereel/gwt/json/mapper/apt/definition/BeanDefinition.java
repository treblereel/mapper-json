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

package org.treblereel.gwt.json.mapper.apt.definition;

import com.google.auto.common.MoreElements;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbTransient;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;

public class BeanDefinition {

  private final TypeElement element;
  private final GenerationContext context;

  public BeanDefinition(TypeElement asTypeElement, GenerationContext generationContext) {
    this.element = asTypeElement;
    this.context = generationContext;
  }

  public Stream<PropertyDefinition> getPropertyDefinitionsAsStream() {
    boolean hasCreator = hasJsonbCreator();
    Stream<VariableElement> asStream =
        context.getTypeUtils().getAllFieldsIn(element).stream()
            .filter(field -> !field.getModifiers().contains(Modifier.STATIC))
            .filter(field -> hasCreator || !field.getModifiers().contains(Modifier.FINAL))
            .filter(field -> !field.getModifiers().contains(Modifier.TRANSIENT))
            .filter(field -> field.getAnnotation(JsonbTransient.class) == null);

    if (element.getAnnotation(JsonbPropertyOrder.class) != null
        && element.getAnnotation(JsonbPropertyOrder.class).value() != null) {
      LinkedHashSet properties = new LinkedHashSet<>();
      String[] order = element.getAnnotation(JsonbPropertyOrder.class).value();
      Map<String, PropertyDefinition> asMap =
          asStream.collect(
              Collectors.toMap(
                  variableElement -> variableElement.getSimpleName().toString(),
                  variableElement -> new PropertyDefinition(variableElement, context),
                  (o1, o2) -> o1,
                  java.util.LinkedHashMap::new));

      for (String s : order) {
        if (asMap.containsKey(s)) {
          properties.add(asMap.remove(s));
        }
      }
      properties.addAll(asMap.values());
      return properties.stream();
    }
    return asStream.map(field -> new PropertyDefinition(field, context));
  }

  public String getPackageQualifiedName() {
    return MoreElements.getPackage(element).getQualifiedName().toString();
  }

  public TypeElement getElement() {
    return element;
  }

  public boolean hasJsonbCreator() {
    return getJsonbCreator() != null;
  }

  public ExecutableElement getJsonbCreator() {
    for (ExecutableElement constructor :
        ElementFilter.constructorsIn(element.getEnclosedElements())) {
      if (constructor.getAnnotation(JsonbCreator.class) != null) {
        return constructor;
      }
    }
    for (ExecutableElement method : ElementFilter.methodsIn(element.getEnclosedElements())) {
      if (method.getAnnotation(JsonbCreator.class) != null) {
        return method;
      }
    }
    return null;
  }

  public Set<String> getCreatorParameterNames() {
    ExecutableElement creator = getJsonbCreator();
    if (creator == null) {
      return Collections.emptySet();
    }
    Set<String> names = new HashSet<>();
    for (VariableElement param : creator.getParameters()) {
      JsonbProperty prop = param.getAnnotation(JsonbProperty.class);
      if (prop != null && !prop.value().isEmpty()) {
        names.add(prop.value());
      } else {
        names.add(param.getSimpleName().toString());
      }
    }
    return names;
  }
}
