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

import org.os890.ds.addon.monitoring.api.MethodInvocationDescriptor;
import org.os890.ds.addon.monitoring.api.ViewAwareMethodInvocationDescriptor;

import javax.faces.context.FacesContext;

public class DefaultViewAwareMethodInvocationDescriptorWrapper implements ViewAwareMethodInvocationDescriptor
{
    private final MethodInvocationDescriptor wrapped;
    private final String viewId;

    public DefaultViewAwareMethodInvocationDescriptorWrapper(MethodInvocationDescriptor wrapped)
    {
        this.wrapped = wrapped;

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext != null && facesContext.getViewRoot() != null)
        {
            this.viewId = facesContext.getViewRoot().getViewId();
        }
        else
        {
            this.viewId = "unknown";
        }
    }

    @Override
    public String getMethodDetails()
    {
        return this.wrapped.getMethodDetails();
    }

    @Override
    public long getTimestamp()
    {
        return this.wrapped.getTimestamp();
    }

    @Override
    public long getExecutionTime()
    {
        return this.wrapped.getExecutionTime();
    }

    @Override
    public Throwable getException()
    {
        return this.wrapped.getException();
    }

    @Override
    public String getActiveViewId()
    {
        return this.viewId;
    }

    @Override
    public String toString()
    {
        return "[" + this.viewId + "] " + this.wrapped.toString();
    }
}
