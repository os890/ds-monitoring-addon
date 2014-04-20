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

import org.apache.deltaspike.core.util.ExceptionUtils;
import org.os890.ds.addon.monitoring.api.InvocationMonitored;
import org.os890.ds.addon.monitoring.api.MethodInvocationDescriptor;
import org.os890.ds.addon.monitoring.api.literal.InvocationMonitoredLiteral;
import org.os890.ds.addon.monitoring.spi.InvocationMonitoredStrategy;
import org.os890.ds.addon.monitoring.spi.MonitoredMethodInvocationStorage;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.interceptor.InvocationContext;

public class DefaultInvocationMonitoredStrategy implements InvocationMonitoredStrategy
{
    //needed for supporting dyn. added annotations (at least with default values)
    private static final boolean MONITOR_EXCEPTIONS_ONLY_DEFAULT =
        new InvocationMonitoredLiteral().exceptionsOnly();
    //needed for supporting dyn. added annotations (at least with default values)
    private static final boolean IGNORE_METHOD_PARAMETER_VALUES_DEFAULT =
        new InvocationMonitoredLiteral().ignoreMethodParameterValues();

    @Inject
    private MonitoredMethodInvocationStorage monitoredMethodInvocationStorage;

    @Inject
    private BeanManager beanManager;

    @Override
    public Object execute(InvocationContext invocationContext) throws Exception
    {
        long startAt = System.currentTimeMillis();

        Throwable throwable = null;

        try
        {
            return invocationContext.proceed();
        }
        catch (Throwable t)
        {
            throwable = t;
            throw ExceptionUtils.throwAsRuntimeException(t);
        }
        finally
        {
            long executionTime = System.currentTimeMillis() - startAt;

            boolean monitorExceptionsOnly = MONITOR_EXCEPTIONS_ONLY_DEFAULT;
            boolean ignoreMethodParameterValues = IGNORE_METHOD_PARAMETER_VALUES_DEFAULT;

            InvocationMonitored invocationMonitored = getInvocationMonitored(invocationContext);
            if (invocationMonitored != null)
            {
                monitorExceptionsOnly = invocationMonitored.exceptionsOnly();
                ignoreMethodParameterValues = invocationMonitored.ignoreMethodParameterValues();
            }

            if (!monitorExceptionsOnly || throwable != null)
            {
                MethodInvocationDescriptor methodInvocationDescriptor = createMethodInvocationEntry(
                    invocationContext, startAt, executionTime, throwable, ignoreMethodParameterValues);
                this.monitoredMethodInvocationStorage.addMethodInvocation(methodInvocationDescriptor);
            }
        }
    }

    protected InvocationMonitored getInvocationMonitored(InvocationContext invocationContext)
    {
        InvocationMonitored invocationMonitored = AnnotationUtils.extractAnnotationFromMethodOrClass(
                this.beanManager, invocationContext.getMethod(), InvocationMonitored.class);

        if (invocationMonitored != null)
        {
            return invocationMonitored;
        }
        return null;
    }

    protected MethodInvocationDescriptor createMethodInvocationEntry(InvocationContext invocationContext,
                                                                     long timestamp,
                                                                     long executionTime,
                                                                     Throwable throwable,
                                                                     boolean ignoreMethodParameterValues)
    {
        return new DefaultMethodInvocationDescriptor(
            invocationContext, timestamp, executionTime, throwable, ignoreMethodParameterValues);
    }
}
