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

package org.os890.ds.addon.monitoring;

import jakarta.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.os890.cdi.addon.dynamictestbean.EnableTestBeans;
import org.os890.ds.addon.monitoring.api.MethodInvocationDescriptor;
import org.os890.ds.addon.monitoring.spi.MonitoredMethodInvocationStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * CDI integration tests for the {@code @InvocationMonitored} interceptor.
 * Uses dynamic-cdi-test-bean-addon as a Jakarta CDI test container
 * (replacing DeltaSpike test-control).
 *
 * @see <a href="https://github.com/os890/dynamic-cdi-test-bean-addon">dynamic-cdi-test-bean-addon</a>
 */
@EnableTestBeans
class InvocationMonitoredTest {

    @Inject
    MonitoredBean monitoredBean;

    @Inject
    MonitoredMethodInvocationStorage storage;

    @BeforeEach
    void resetStorage() {
        storage.restartMonitoring();
    }

    @Test
    void monitoredMethodRecordsInvocation() {
        String result = monitoredBean.doWork("hello");

        assertEquals("result:hello", result);

        List<MethodInvocationDescriptor> invocations = storage.getMethodInvocations();
        assertEquals(1, invocations.size());

        MethodInvocationDescriptor descriptor = invocations.get(0);
        assertTrue(descriptor.getMethodDetails().contains("doWork"));
        assertTrue(descriptor.getExecutionTime() >= 0);
        assertTrue(descriptor.getTimestamp() > 0);
        assertNull(descriptor.getException());
    }

    @Test
    void exceptionsOnlySkipsSuccessfulInvocation() {
        String result = monitoredBean.doWorkExceptionsOnly("test");

        assertEquals("ok:test", result);

        List<MethodInvocationDescriptor> invocations = storage.getMethodInvocations();
        assertTrue(invocations.isEmpty(),
                "Successful invocation should not be recorded with exceptionsOnly=true");
    }

    @Test
    void exceptionsOnlyRecordsFailedInvocation() {
        assertThrows(IllegalStateException.class,
                () -> monitoredBean.doWorkWithException("test"));

        List<MethodInvocationDescriptor> invocations = storage.getMethodInvocations();
        assertEquals(1, invocations.size());

        MethodInvocationDescriptor descriptor = invocations.get(0);
        assertNotNull(descriptor.getException());
        assertTrue(descriptor.getException() instanceof IllegalStateException);
        assertEquals("test-error", descriptor.getException().getMessage());
    }

    @Test
    void parameterValuesIncludedWhenNotIgnored() {
        String result = monitoredBean.doWorkWithParams("abc", 42);

        assertEquals("abc42", result);

        List<MethodInvocationDescriptor> invocations = storage.getMethodInvocations();
        assertEquals(1, invocations.size());

        String details = invocations.get(0).getMethodDetails();
        assertTrue(details.contains("doWorkWithParams"));
        assertTrue(details.contains("'abc'"),
                "Parameter value 'abc' should be in method details");
        assertTrue(details.contains("'42'"),
                "Parameter value '42' should be in method details");
    }

    @Test
    void multipleInvocationsAreAccumulated() {
        monitoredBean.doWork("first");
        monitoredBean.doWork("second");
        monitoredBean.doWork("third");

        List<MethodInvocationDescriptor> invocations = storage.getMethodInvocations();
        assertEquals(3, invocations.size());
    }

    @Test
    void descriptorToStringContainsExpectedFields() {
        monitoredBean.doWork("test");

        MethodInvocationDescriptor descriptor = storage.getMethodInvocations().get(0);
        String toString = descriptor.toString();

        assertTrue(toString.contains("methodDetails="));
        assertTrue(toString.contains("timestamp="));
        assertTrue(toString.contains("executionTime="));
    }
}
