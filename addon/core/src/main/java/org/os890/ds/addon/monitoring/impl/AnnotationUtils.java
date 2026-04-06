/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.os890.ds.addon.monitoring.impl;

import jakarta.enterprise.inject.Typed;
import jakarta.enterprise.inject.spi.BeanManager;

import org.apache.deltaspike.core.util.ProxyUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Utility methods for extracting annotations from methods and classes,
 * including support for CDI stereotype resolution.
 */
@Typed()
public abstract class AnnotationUtils {

    private AnnotationUtils() {
        // prevent instantiation
    }

    /**
     * Extracts the given annotation type from the target method first, then
     * falls back to the declaring class (walking through stereotypes).
     *
     * @param <T>                  the annotation type
     * @param beanManager          the CDI bean manager
     * @param targetMethod         the method to inspect
     * @param targetAnnotationType the annotation class to look for
     * @return the annotation instance, or {@code null} if not found
     */
    public static <T extends Annotation> T extractAnnotationFromMethodOrClass(
            BeanManager beanManager, Method targetMethod, Class<T> targetAnnotationType) {

        T result = extractAnnotationFromMethod(beanManager, targetMethod, targetAnnotationType);

        if (result == null) {
            //see DELTASPIKE-517
            Class<?> targetClass = ProxyUtils.getUnproxiedClass(targetMethod.getDeclaringClass());
            result = findAnnotation(beanManager, targetClass.getAnnotations(), targetAnnotationType);
        }
        return result;
    }

    /**
     * Extracts the given annotation type from the target method only.
     *
     * @param <T>                  the annotation type
     * @param beanManager          the CDI bean manager
     * @param targetMethod         the method to inspect
     * @param targetAnnotationType the annotation class to look for
     * @return the annotation instance, or {@code null} if not found
     */
    public static <T extends Annotation> T extractAnnotationFromMethod(
            BeanManager beanManager, Method targetMethod, Class<T> targetAnnotationType) {

        return findAnnotation(beanManager, targetMethod.getAnnotations(), targetAnnotationType);
    }

    /**
     * Searches the given annotations (and their stereotypes) for the target annotation type.
     *
     * @param <T>                  the annotation type
     * @param beanManager          the CDI bean manager
     * @param annotations          the annotations to search
     * @param targetAnnotationType the annotation class to look for
     * @return the annotation instance, or {@code null} if not found
     */
    @SuppressWarnings("unchecked")
    public static <T extends Annotation> T findAnnotation(
            BeanManager beanManager, Annotation[] annotations, Class<T> targetAnnotationType) {

        for (Annotation annotation : annotations) {
            if (targetAnnotationType.equals(annotation.annotationType())) {
                return (T) annotation;
            }
            if (beanManager.isStereotype(annotation.annotationType())) {
                T result = findAnnotation(
                        beanManager, annotation.annotationType().getAnnotations(), targetAnnotationType);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
