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

package org.treblereel.gwt.json.mapper.apt;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.collect.Streams;
import jakarta.json.bind.annotation.JsonbSubtype;
import jakarta.json.bind.annotation.JsonbTypeInfo;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.MirroredTypesException;
import org.treblereel.gwt.json.mapper.annotation.JSONMapper;
import org.treblereel.gwt.json.mapper.annotation.JSONMappers;
import org.treblereel.gwt.json.mapper.apt.context.GenerationContext;
import org.treblereel.gwt.json.mapper.apt.logger.PrintWriterTreeLogger;
import org.treblereel.gwt.json.mapper.apt.logger.TreeLogger;
import org.treblereel.gwt.json.mapper.apt.processor.BeanProcessor;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class GenerationProcessor extends AbstractProcessor {

  private final Set<TypeElement> beans = new HashSet<>();

  private final TreeLogger logger = new PrintWriterTreeLogger();

  @Override
  public boolean process(
      Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
    if (!annotations.isEmpty()) {
      GenerationContext context = new GenerationContext(roundEnvironment, processingEnv);

      Stream<? extends Element> stream =
          Streams.concat(
              roundEnvironment.getElementsAnnotatedWith(JSONMapper.class).stream(),
              roundEnvironment.getElementsAnnotatedWith(JsonbTypeInfo.class).stream()
                  .map(type -> type.getAnnotation(JsonbTypeInfo.class))
                  .map(JsonbTypeInfo::value)
                  .flatMap(Arrays::stream)
                  .map(this::get),
              roundEnvironment.getElementsAnnotatedWith(JSONMappers.class).stream()
                  .map(element -> element.getAnnotation(JSONMappers.class))
                  .flatMap(this::getTypes));
      processJsonMapper(stream);
      new BeanProcessor(context, logger, beans).process();
    }
    beans.clear();
    return false;
  }

  private void processJsonMapper(Stream<? extends Element> stream) {
    stream.map(MoreElements::asType).forEach(beans::add);
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return supportedAnnotations().stream().map(Class::getCanonicalName).collect(Collectors.toSet());
  }

  private List<Class<?>> supportedAnnotations() {
    return Arrays.asList(JSONMapper.class, JSONMappers.class);
  }

  private TypeElement get(JsonbSubtype jsonbSubtype) {
    try {
      jsonbSubtype.type();
    } catch (MirroredTypeException e) {
      return MoreTypes.asTypeElement(e.getTypeMirror());
    }
    return null;
  }

  private Stream<TypeElement> getTypes(JSONMappers annotation) {
    try {
      annotation.value();
    } catch (MirroredTypesException e) {
      return e.getTypeMirrors().stream().map(MoreTypes::asTypeElement);
    }
    return Stream.empty();
  }
}
