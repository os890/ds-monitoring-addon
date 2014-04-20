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
package org.os890.ds.addon.monitoring.api.literal;

import org.apache.deltaspike.core.util.metadata.AnnotationInstanceProvider;
import org.os890.ds.addon.monitoring.api.InvocationMonitored;

import javax.enterprise.util.AnnotationLiteral;

/**
 * Literal for the {@link InvocationMonitoredLiteral} annotation.
 */
public class InvocationMonitoredLiteral extends AnnotationLiteral<InvocationMonitored> implements InvocationMonitored
{
    private static final long serialVersionUID = -7623640277155878657L;
    private static final InvocationMonitored DEFAULT_INVOCATION_MONITORED =
        AnnotationInstanceProvider.of(InvocationMonitored.class);

    private final boolean exceptionsOnly;
    private final boolean ignoreMethodParameterValues;

    public InvocationMonitoredLiteral()
    {
        this.exceptionsOnly = DEFAULT_INVOCATION_MONITORED.exceptionsOnly();
        this.ignoreMethodParameterValues = DEFAULT_INVOCATION_MONITORED.ignoreMethodParameterValues();
    }

    public InvocationMonitoredLiteral(boolean exceptionsOnly, boolean ignoreMethodParameterValues)
    {
        this.exceptionsOnly = exceptionsOnly;
        this.ignoreMethodParameterValues = ignoreMethodParameterValues;
    }

    @Override
    public boolean exceptionsOnly()
    {
        return this.exceptionsOnly;
    }

    @Override
    public boolean ignoreMethodParameterValues()
    {
        return this.ignoreMethodParameterValues;
    }
}