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

import jakarta.interceptor.InvocationContext;

import org.apache.deltaspike.core.util.ProxyUtils;
import org.os890.ds.addon.monitoring.api.MethodInvocationDescriptor;

import java.lang.reflect.Method;

/**
 * Default implementation of {@link MethodInvocationDescriptor} that captures
 * the full method signature, timing data, and optionally parameter values.
 */
public class DefaultMethodInvocationDescriptor implements MethodInvocationDescriptor {

    private static final long serialVersionUID = 1L;

    private final String targetMethodDetails;
    private final long timestamp;
    private final long executionTime;
    private final Throwable throwable;

    /**
     * Creates a new descriptor by extracting method details from the invocation context.
     *
     * @param invocationContext          the interceptor invocation context
     * @param timestamp                  the invocation start timestamp
     * @param executionTime              the elapsed execution time in milliseconds
     * @param throwable                  the exception thrown, or {@code null}
     * @param ignoreMethodParameterValues whether to omit parameter values
     */
    public DefaultMethodInvocationDescriptor(InvocationContext invocationContext,
                                             long timestamp,
                                             long executionTime,
                                             Throwable throwable,
                                             boolean ignoreMethodParameterValues) {
        Method targetMethod = invocationContext.getMethod();
        String className = ProxyUtils.getUnproxiedClass(targetMethod.getDeclaringClass()).getName();
        StringBuilder methodDetails = new StringBuilder();
        methodDetails.append(className).append("#").append(targetMethod.getName()).append("(");

        boolean isFirstParameter = true;
        for (Class<?> paramType : targetMethod.getParameterTypes()) {
            if (isFirstParameter) {
                isFirstParameter = false;
            } else {
                methodDetails.append(", ");
            }
            methodDetails.append(paramType.getName());
        }
        methodDetails.append(")");

        if (!ignoreMethodParameterValues) {
            for (Object parameterValue : invocationContext.getParameters()) {
                methodDetails.append(":");
                if (parameterValue != null) {
                    try {
                        methodDetails.append("'").append(parameterValue).append("'");
                    } catch (Throwable t) {
                        methodDetails.append("'unknown due to ").append(t.getClass().getName())
                            .append(" in ").append(t.getStackTrace()[0]).append("'");
                    }
                } else {
                    methodDetails.append("'null'");
                }
            }
        }

        this.targetMethodDetails = methodDetails.toString();
        this.timestamp = timestamp;
        this.executionTime = executionTime;
        this.throwable = throwable;
    }

    @Override
    public String getMethodDetails() {
        return this.targetMethodDetails;
    }

    @Override
    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public long getExecutionTime() {
        return this.executionTime;
    }

    @Override
    public Throwable getException() {
        return this.throwable;
    }

    @Override
    public String toString() {
        if (this.throwable == null) {
            return "{methodDetails='" + targetMethodDetails + '\''
                    + ", timestamp=" + timestamp
                    + ", executionTime=" + executionTime
                    + '}';
        }
        return "{methodDetails='" + targetMethodDetails + '\''
                + ", timestamp=" + timestamp
                + ", executionTime=" + executionTime
                + ", throwable=" + throwable
                + '}';
    }
}
