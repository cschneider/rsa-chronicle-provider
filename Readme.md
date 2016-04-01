Export these package from system bundle
    sun.reflect, \
    sun.nio.ch, \
    com.sun.jna, \
    com.sun.jna.ptr, \

Install these packages:

```
install -s mvn:org.apache.aries.rsa.provider/org.apache.aries.rsa.provider.chronicle
install -s  mvn:net.openhft/chronicle-bytes/1.2.5
install -s  mvn:net.openhft/chronicle-core/1.3.8
install -s  mvn:net.openhft/chronicle-queue/4.1.0
install -s  mvn:net.openhft/chronicle-threads/1.3.1
install -s  mvn:net.openhft/affinity/3.0.3
install -s  mvn:net.openhft/chronicle-wire/1.3.7
```

Add these packages to the system bundles exports in config.properties in karaf
```
    sun.misc, \
    sun.reflect, \
    sun.nio.ch, \
    com.sun.jna, \
    com.sun.jna.ptr, \
```
