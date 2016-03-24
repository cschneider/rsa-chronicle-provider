package org.apache.aries.rsa.provider.chronicle;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.aries.rsa.util.StringPlus;
import org.osgi.framework.Constants;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

public class QueueHelper {
    public static String getqueueName(Map<String, Object> effectiveProperties) {
        List<String> ifaces = StringPlus.normalize(effectiveProperties.get(Constants.OBJECTCLASS));
        return ifaces.iterator().next();
    }

    public static ChronicleQueue createQueue(String queueName) {
        String tempDir = System.getProperty("java.io.tmpdir");
        File queueDir = new File(tempDir, queueName);
        queueDir.mkdirs();
        return SingleChronicleQueueBuilder.binary(queueDir).build();
    }
}
