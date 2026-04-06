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

package org.os890.ds.addon.monitoring.api;

import jakarta.enterprise.util.Nonbinding;
import jakarta.interceptor.InterceptorBinding;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interceptor binding annotation that marks a method or type for invocation monitoring.
 *
 * <p>When applied to a method or class, all invocations will be recorded and made
 * available through the {@link org.os890.ds.addon.monitoring.spi.MonitoredMethodInvocationStorage}.
 * At the end of a request, the collected invocations are fired as a
 * {@link org.os890.ds.addon.monitoring.api.event.MonitoredMethodInvocationsEvent} CDI event.</p>
 */
@InterceptorBinding
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface InvocationMonitored {

    /**
     * If {@code true}, only invocations that result in an exception are recorded.
     *
     * @return whether to monitor exceptions only
     */
    @Nonbinding boolean exceptionsOnly() default false;

    /**
     * If {@code true}, method parameter values are omitted from the recorded details.
     *
     * @return whether to ignore method parameter values
     */
    @Nonbinding boolean ignoreMethodParameterValues() default true;
}
