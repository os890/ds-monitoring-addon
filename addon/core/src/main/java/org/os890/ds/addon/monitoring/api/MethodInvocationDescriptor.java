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

import java.io.Serializable;

/**
 * Describes a single monitored method invocation, including timing and exception details.
 */
public interface MethodInvocationDescriptor extends Serializable {

    /**
     * Returns a human-readable description of the invoked method, including
     * class name, method name, parameter types, and optionally parameter values.
     *
     * @return the method details string
     */
    String getMethodDetails();

    /**
     * Returns the wall-clock timestamp (milliseconds since epoch) when the invocation started.
     *
     * @return the invocation start timestamp
     */
    long getTimestamp();

    /**
     * Returns the elapsed execution time of the invocation in milliseconds.
     *
     * @return the execution time in milliseconds
     */
    long getExecutionTime();

    /**
     * Returns the exception thrown during the invocation, or {@code null} if the
     * invocation completed normally.
     *
     * @return the thrown exception, or {@code null}
     */
    Throwable getException();
}
