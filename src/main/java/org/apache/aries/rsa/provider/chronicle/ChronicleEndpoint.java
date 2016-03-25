/**
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
package org.apache.aries.rsa.provider.chronicle;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.aries.rsa.spi.Endpoint;
import org.apache.aries.rsa.util.StringPlus;
import org.osgi.framework.Constants;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteConstants;

import net.openhft.chronicle.queue.ChronicleQueue;

public class ChronicleEndpoint implements Endpoint {
    private EndpointDescription epd;
    private ChronicleServer server;
    private ChronicleQueue queue;
    
    public ChronicleEndpoint(Object service, Map<String, Object> effectiveProperties) {
        List<String> ifaces = StringPlus.normalize(effectiveProperties.get(Constants.OBJECTCLASS));
        String queueName = UUID.randomUUID().toString();
        queue = QueueHelper.createQueue(queueName);
        server = new ChronicleServer(service, queue);
        String endpointId = String.format("chronicle://localhost/%s", queueName);
        effectiveProperties.put(RemoteConstants.ENDPOINT_ID, endpointId);
        effectiveProperties.put(RemoteConstants.SERVICE_EXPORTED_CONFIGS, "");
        this.epd = new EndpointDescription(effectiveProperties);
        this.epd.getInterfaces().iterator().next();
    }
    

    private Integer getInt(Map<String, Object> effectiveProperties, String key, String defaultValue) {
        return Integer.parseInt(getString(effectiveProperties, key, defaultValue));
    }
    
    private String getString(Map<String, Object> effectiveProperties, String key, String defaultValue) {
        String value = (String)effectiveProperties.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public EndpointDescription description() {
        return this.epd;
    }


    @Override
    public void close() throws IOException {
        queue.close();
        server.close();
    }
}
