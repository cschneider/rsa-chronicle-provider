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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.aries.rsa.provider.tcp.myservice.MyService;
import org.apache.aries.rsa.provider.tcp.myservice.MyServiceImpl;
import org.apache.aries.rsa.spi.Endpoint;
import org.apache.aries.rsa.util.EndpointHelper;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;

public class ChronicleProviderTest {

    private static final int NUM_CALLS = 1000000;
    private MyService myServiceProxy;
    private Endpoint ep;
    
    @Before
    public void createServerAndProxy() {
        Class<?>[] exportedInterfaces = new Class[] {MyService.class};
        ChronicleProvider provider = new ChronicleProvider();
        Map<String, Object> props = new HashMap<String, Object>();
        EndpointHelper.addObjectClass(props, exportedInterfaces);
        MyService myService = new MyServiceImpl();
        BundleContext bc = EasyMock.mock(BundleContext.class);
        ep = provider.exportService(myService, bc, props, exportedInterfaces);
        myServiceProxy = (MyService)provider.importEndpoint(MyService.class.getClassLoader(), 
                                                            bc,
                                                            exportedInterfaces, 
                                                            ep.description());
    }

    @Test
    public void testPerf() throws IOException, InterruptedException {
        runPerfTest(myServiceProxy);
    }
    
    @Test
    public void testCall() throws IOException, InterruptedException {
        myServiceProxy.call("test");
    }
    
    @Test
    public void testCallOneway() throws IOException, InterruptedException {
        myServiceProxy.callOneWay("test");
    }
    
    @After
    public void close() throws IOException {
        ep.close();
    }

    private void runPerfTest(final MyService myServiceProxy2) throws InterruptedException {
        StringBuilder msg = new StringBuilder();
        for (int c = 0; c < 1; c++) {
            msg.append("testing123");
        }
        final String msg2 = msg.toString();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Runnable task = new Runnable() {
            
            @Override
            public void run() {
                myServiceProxy2.callOneWay(msg2);;
            }
        };
        long start = System.currentTimeMillis();
        for (int c = 0; c < NUM_CALLS; c++) {
            executor.execute(task);
        }
        executor.shutdown();
        executor.awaitTermination(100, TimeUnit.SECONDS);
        long tps = NUM_CALLS * 1000 / (System.currentTimeMillis() - start);
        System.out.println(tps + " tps");
    }
}
