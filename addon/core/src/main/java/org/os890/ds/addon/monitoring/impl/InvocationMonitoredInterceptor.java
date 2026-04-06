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

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import org.os890.ds.addon.monitoring.api.InvocationMonitored;
import org.os890.ds.addon.monitoring.spi.InvocationMonitoredStrategy;

import java.io.Serializable;

/**
 * CDI interceptor that delegates monitored method invocations to the
 * {@link InvocationMonitoredStrategy}.
 *
 * <p>This interceptor is activated by the {@link InvocationMonitored} binding
 * and is globally enabled via {@link Priority}.</p>
 */
@Interceptor
@InvocationMonitored
@Priority(Interceptor.Priority.LIBRARY_BEFORE)
public class InvocationMonitoredInterceptor implements Serializable {

    private static final long serialVersionUID = 3787285444722371172L;

    @SuppressWarnings("serial")
    @Inject
    private InvocationMonitoredStrategy invocationMonitoredStrategy;

    /**
     * Intercepts the annotated method invocation and delegates to the strategy.
     *
     * @param invocationContext the interceptor invocation context
     * @return the result of the intercepted method
     * @throws Exception if the intercepted method throws an exception
     */
    @AroundInvoke
    public Object monitorInvocation(InvocationContext invocationContext) throws Exception {
        return this.invocationMonitoredStrategy.execute(invocationContext);
    }
}
