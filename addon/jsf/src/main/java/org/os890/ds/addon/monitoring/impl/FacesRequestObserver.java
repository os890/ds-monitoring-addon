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

import org.apache.deltaspike.core.api.lifecycle.Destroyed;
import org.os890.ds.addon.monitoring.spi.MonitoredMethodInvocationStorage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

/*
for supporting monitoring of other @Destroyed FacesContext observers FacesContextWrapper#release would be needed - e.g.:

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
@ApplicationScoped
public class FacesRequestObserver
{
    protected void onDestroy(@Observes @Destroyed FacesContext facesContext,
                             MonitoredMethodInvocationStorage monitoredMethodInvocationStorage)
    {
        if (facesContext.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE)
        {
            //process after rendering (and not in case of a release for a redirect)
            monitoredMethodInvocationStorage.restartMonitoring();
        }
    }
}
