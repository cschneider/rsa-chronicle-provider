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
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;

public class ChronicleInvocationHandler implements InvocationHandler {
    private ExcerptAppender appender;
    private ChronicleQueue queue;

    public ChronicleInvocationHandler(ClassLoader cl, String queueName) {
        queue = QueueHelper.createQueue(queueName);
        this.appender = queue.createAppender();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Bytes<ByteBuffer> message = Bytes.elasticByteBuffer();
        message.clear();
        writeToMessage(method, args, message);
        appender.writeBytes(message);
        return null;
    }

    private void writeToMessage(Method method, Object[] args, final Bytes<ByteBuffer> message)
        throws IOException {
        try (
            ObjectOutputStream out = new ObjectOutputStream(message.outputStream())
            ) {
            out.writeObject(method.getName());
            out.writeObject(args);
        } catch (Exception  e) {
            throw new RuntimeException("Error calling " + method.getName(), e);
        }
    }

}
