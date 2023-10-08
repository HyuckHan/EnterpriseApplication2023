import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.Date;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.text.SimpleDateFormat;

import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZookeeperConnection {
    private static final Logger logger = Logger.getLogger(ZookeeperConnection.class.getName());
    public static final int TIMEOUT_MS = 5000;
    static{
        logger.setLevel(Level.INFO);
    }
    private ZooKeeper zoo;
    public void ZookeeperConnection() {
    }

    /**
     * Connects to a zookeeper ensemble in zkUriStr.
     * serverIp and portStr are the IP/Port of this server.
     */
    public boolean connect(String zkUriStr, String serverIp, String portStr)
            throws IOException,InterruptedException {
            final CountDownLatch connectedSignal = new CountDownLatch(1);
            String zkhostport;
            try {
                URI zkUri = new URI(zkUriStr);
                zkhostport = zkUri.getHost().toString() + ":" + Integer.toString(zkUri.getPort());
            } catch (Exception e) {
                logger.severe("Could not parse zk URI " + zkUriStr);
                return false;
            }

            zoo = new ZooKeeper(zkhostport, TIMEOUT_MS, new Watcher() {
                public void process(WatchedEvent we) {
                    if (we.getState() == KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }
            });
            /* Wait for zookeeper connection */
            connectedSignal.await();

            String path = "/grpc_hello_world_service";
            Stat stat;
            String currTime = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
            try {
                stat = zoo.exists(path, true);
                if (stat == null) {
                    zoo.create(path, currTime.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }
            } catch (Exception e) {
                logger.severe("Failed to create path");
                return false;
            }

            String server_addr = path + "/" + serverIp + ":" + portStr;
            try {
                stat = zoo.exists(server_addr, true);
                if (stat == null) {
                    try {
                        zoo.create(server_addr, currTime.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    } catch (Exception e) {
                        logger.severe("Failed to create server_data");
                        return false;
                    }
                } else {
                    try {
                        zoo.setData(server_addr, currTime.getBytes(), stat.getVersion());
                    } catch (Exception e) {
                        logger.severe("Failed to update server_data");
                        return false;
                    }
                }
            } catch (Exception e) {
                logger.severe("Failed to add server_data");
                return false;
            }
            return true;
    }

    // Method to disconnect from zookeeper server
    public void close() throws InterruptedException {
        zoo.close();
    }
}

