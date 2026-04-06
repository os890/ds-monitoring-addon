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

package org.os890.ds.addon.monitoring.api.event;

import org.os890.ds.addon.monitoring.api.MethodInvocationDescriptor;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CDI event that carries the list of monitored method invocations collected
 * during a request or monitoring cycle.
 *
 * <p>Observers of this event receive the accumulated invocation data when
 * monitoring is restarted (typically at the end of an HTTP request).</p>
 */
public class MonitoredMethodInvocationsEvent {

    private final List<MethodInvocationDescriptor> methodInvocationDescriptors =
        new CopyOnWriteArrayList<>();

    /**
     * Returns the thread-safe list of method invocation descriptors collected
     * during the current monitoring cycle.
     *
     * @return the list of method invocation descriptors
     */
    public List<MethodInvocationDescriptor> getMethodInvocationDescriptors() {
        return this.methodInvocationDescriptors;
    }
}
