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

package org.treblereel.gwt.json.mapper.apt.processor;

import com.google.auto.common.MoreTypes;
import jakarta.json.bind.annotation.JsonbTransient;
import java.util.*;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.apt.exception.GenerationException;
import org.treblereel.gwt.json.mapper.apt.generator.MapperGenerator;
import org.treblereel.gwt.json.mapper.apt.logger.TreeLogger;
import org.treblereel.gwt.json.mapper.apt.utils.TypeUtils;

public class BeanProcessor {

  private final GenerationContext context;
  private final TreeLogger logger;
  private final Set<TypeElement> annotatedBeans;
  private final Set<TypeElement> beans = new HashSet<>();

  private final TypeUtils typeUtils;

  private final MapperGenerator mapperGenerator;

  public BeanProcessor(GenerationContext context, TreeLogger logger, Set<TypeElement> beans) {
    this.context = context;
    this.logger = logger;
    this.annotatedBeans = beans;
    this.typeUtils = context.getTypeUtils();
    this.mapperGenerator = new MapperGenerator(context, logger);
  }

  public void process() {
    annotatedBeans.forEach(this::processBean);
    beans.forEach(context::addBeanDefinition);

    context.getBeans().stream().forEach(mapperGenerator::generate);
  }

  private void processBean(TypeElement bean) {
    if (!beans.contains(bean)) {
      beans.add(checkBean(bean));
      context.getTypeUtils().getAllFieldsIn(bean).forEach(this::processField);
    }
  }

  private void processField(VariableElement field) {
    if (checkField(field)) {
      // Ensure the serializer/deserializer is generated for the fields concrete type, considering
      // XmlElement and XmlElementRef types
      TypeMirror typeMirror = field.asType();
      checkTypeAndAdd(typeMirror);
    }
  }

  private void checkTypeAndAdd(TypeMirror type) {
    if (type.getKind().isPrimitive() || typeUtils.isBoxedTypeOrString(type)) {
      return;
    }

    if (context.getTypeUtils().isAssignableFrom(type, Map.class)) {
      DeclaredType collection = (DeclaredType) type;
      collection.getTypeArguments().forEach(this::checkTypeAndAdd);
    } else if (context.getTypeUtils().isAssignableFrom(type, Collection.class)) {
      DeclaredType collection = (DeclaredType) type;
      collection.getTypeArguments().forEach(this::checkTypeAndAdd);
    } else if (context.getTypeUtils().isAssignableFrom(type, Iterable.class)) {
      DeclaredType collection = (DeclaredType) type;
      collection.getTypeArguments().forEach(this::checkTypeAndAdd);
    } else if (!beans.contains(context.getProcessingEnv().getTypeUtils().erasure(type))) {
      processBean(MoreTypes.asTypeElement(context.getProcessingEnv().getTypeUtils().erasure(type)));
    } else if (type.getKind().equals(TypeKind.ARRAY)) {
      ArrayType arrayType = (ArrayType) type;
      processBean(MoreTypes.asTypeElement(arrayType.getComponentType()));
    }
  }

  private boolean checkField(VariableElement field) {
    if (field.getModifiers().contains(Modifier.STATIC)
        || field.getModifiers().contains(Modifier.TRANSIENT)
        || field.getAnnotation(JsonbTransient.class) != null
        || field.getModifiers().contains(Modifier.FINAL)) {
      return false;
    }
    if (!field.getModifiers().contains(Modifier.PRIVATE)
        || typeUtils.hasGetter(field) && typeUtils.hasSetter(field)) {
      return true;
    }

    if (!typeUtils.hasGetter(field)) {
      throw new GenerationException(
          String.format(
              "Unable to find suitable getter for [%s] in [%s].",
              field.getSimpleName(), field.getEnclosingElement()));
    }

    if (!typeUtils.hasSetter(field)) {
      throw new GenerationException(
          String.format(
              "Unable to find suitable setter for [%s] in [%s]",
              field.getSimpleName(), field.getEnclosingElement()));
    }

    throw new GenerationException(
        String.format(
            "Unable to process [%s] in [%s]", field.getSimpleName(), field.getEnclosingElement()));
  }

  private TypeElement checkBean(TypeElement type) {
    if (!type.getKind().isClass()) {
      throw new GenerationException("A @JSONMapper bean [" + type + "] must be class");
    }

    if (type.getModifiers().contains(Modifier.ABSTRACT)) {
      throw new GenerationException("A @JSONMapper bean [" + type + "] must be non abstract");
    }

    if (type.getModifiers().contains(Modifier.PRIVATE)) {
      throw new GenerationException("A @JSONMapper bean [" + type + "] must be public");
    }

    if (type.getModifiers().contains(Modifier.STATIC)) {
      throw new GenerationException("A @JSONMapper bean [" + type + "] must not be static");
    }

    List<ExecutableElement> constructors = ElementFilter.constructorsIn(type.getEnclosedElements());
    if (!constructors.isEmpty()) {
      long nonArgConstructorCount =
          constructors.stream()
              .filter(constr -> !constr.getModifiers().contains(Modifier.PRIVATE))
              .filter(constr -> constr.getParameters().isEmpty())
              .count();
      if (nonArgConstructorCount != 1) {
        throw new GenerationException(
            "A @JSONMapper bean [" + type + "] must have a non-private non-arg constructor");
      }
    }
    return type;
  }
}