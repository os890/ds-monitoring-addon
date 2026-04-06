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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Destroyed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.event.Observes;
import jakarta.servlet.ServletRequest;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseId;

import org.os890.ds.addon.monitoring.spi.MonitoredMethodInvocationStorage;

/*
for supporting monitoring of other @Destroyed observers FacesContextWrapper#release would be needed - e.g.:

    @Override
    public void release()
    {
        try
        {
            wrappedFacesContext.release();
        }
        finally
        {
            if (getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
            {
                this.monitoredMethodInvocationStorage.restartMonitoring();
            }
        }
    }
 */

/**
 * CDI observer that triggers monitoring output at the end of a JSF request.
 *
 * <p>Observes the standard CDI {@link Destroyed} event for {@link RequestScoped}
 * and restarts monitoring only after the render-response phase has completed
 * (not during a redirect).</p>
 */
@ApplicationScoped
public class FacesRequestObserver {

    /**
     * Restarts monitoring when the request scope is destroyed after a
     * render-response phase, causing the accumulated invocation data to be
     * fired as a CDI event.
     *
     * @param servletRequest                   the destroyed servlet request
     * @param monitoredMethodInvocationStorage the storage to restart
     */
    protected void onDestroy(
            @Observes @Destroyed(RequestScoped.class) ServletRequest servletRequest,
            MonitoredMethodInvocationStorage monitoredMethodInvocationStorage) {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        if (facesContext != null && facesContext.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            //process after rendering (and not in case of a release for a redirect)
            monitoredMethodInvocationStorage.restartMonitoring();
        }
    }
}
