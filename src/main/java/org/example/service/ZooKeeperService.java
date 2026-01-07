package org.example.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
public class ZooKeeperService {

    @Value("${zookeeper.host}")
    private String zookeeperHost;

    @Value("${zookeeper.counter.path}")
    private String counterPath;

    @Value("${zookeeper.session.timeout}")
    private int sessionTimeout;

    @Value("${zookeeper.connection.timeout}")
    private int connectionTimeout;

    private ZooKeeper zooKeeper;
    private CountDownLatch connectedSignal;

    @PostConstruct
    public void init() throws Exception {
        connectedSignal = new CountDownLatch(1);
        connect();
        ensurePathExists();
    }

    private void connect() throws Exception {
        zooKeeper = new ZooKeeper(zookeeperHost, sessionTimeout, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectedSignal.countDown();
            }
        });
        connectedSignal.await();
        log.info("Connected to ZooKeeper: {}", zookeeperHost);
    }

    private void ensurePathExists() throws Exception {
        String[] paths = counterPath.split("/");
        StringBuilder currentPath = new StringBuilder();

        for (String path : paths) {
            if (path.isEmpty()) continue;
            currentPath.append("/").append(path);

            if (zooKeeper.exists(currentPath.toString(), false) == null) {
                zooKeeper.create(currentPath.toString(), "0".getBytes(StandardCharsets.UTF_8),
                        ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        }
    }

    public long getAndIncrementCounter(long rangeSize) throws Exception {
        byte[] data = zooKeeper.getData(counterPath, false, null);
        long currentCounter = Long.parseLong(new String(data, StandardCharsets.UTF_8));
        long newCounter = currentCounter + rangeSize;

        zooKeeper.setData(counterPath, String.valueOf(newCounter).getBytes(StandardCharsets.UTF_8), -1);

        return currentCounter;
    }

    @PreDestroy
    public void close() throws InterruptedException {
        if (zooKeeper != null) {
            zooKeeper.close();
            log.info("ZooKeeper connection closed");
        }
    }
}

