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

package org.os890.ds.addon.monitoring.spi;

import org.os890.ds.addon.monitoring.api.MethodInvocationDescriptor;

import java.util.List;

/**
 * Storage abstraction for monitored method invocations collected during a
 * request or monitoring cycle.
 *
 * <p>Implementations are responsible for accumulating invocation records and
 * publishing them as a CDI event when {@link #restartMonitoring()} is called.</p>
 */
public interface MonitoredMethodInvocationStorage {

    /**
     * Adds a method invocation descriptor to the current monitoring cycle.
     *
     * @param methodInvocationDescriptor the descriptor to add
     */
    void addMethodInvocation(MethodInvocationDescriptor methodInvocationDescriptor);

    /**
     * Returns the list of method invocation descriptors collected so far.
     *
     * @return the list of invocation descriptors
     */
    List<MethodInvocationDescriptor> getMethodInvocations();

    /**
     * Fires a {@link org.os890.ds.addon.monitoring.api.event.MonitoredMethodInvocationsEvent}
     * with the collected invocations and resets the storage for the next cycle.
     */
    void restartMonitoring();
}
