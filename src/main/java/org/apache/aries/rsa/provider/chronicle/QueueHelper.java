package org.apache.aries.rsa.provider.chronicle;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.aries.rsa.util.StringPlus;
import org.osgi.framework.Constants;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.WireType;

public class QueueHelper {
    public static ChronicleQueue createQueue(String queueName) {
        String tempDir = System.getProperty("java.io.tmpdir");
        File rsaDir = new File(tempDir, "rsa");
        rsaDir.mkdirs();
        File queueDir = new File(rsaDir, queueName);
        queueDir.mkdirs();
        return new SingleChronicleQueueBuilder(queueDir).wireType(WireType.BINARY).build();
    }
    
}
