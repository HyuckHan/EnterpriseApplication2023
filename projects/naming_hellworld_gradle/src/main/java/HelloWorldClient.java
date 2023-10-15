import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogManager;
import java.util.logging.Handler;
import java.util.concurrent.CountDownLatch;
import java.util.List;

//import org.apache.zookeeper.AsyncCallback.StatCallback;
//import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.apache.zookeeper.KeeperException;
//import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
//import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import helloworld.*;

/**
 * A simple client that requests a greeting from the {@link HelloWorldServer}.
 */
public class HelloWorldClient {
    private static final Logger logger = Logger.getLogger(HelloWorldClient.class.getName());
    public static final String PATH = "/grpc_hello_world_service";
    public static final int TIMEOUT_MS = 2000;

    private static GreeterGrpc.GreeterBlockingStub blockingStub;

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */
    public static void main(String[] args) throws Exception {
        String user = "world";
        String target = null;

        ZooKeeper zoo = null;
        final CountDownLatch connectedSignal = new CountDownLatch(1);
        try {
            String zkaddr = "zk://localhost:2181";
            logger.info("Connecting to Zookeeper Address " + zkaddr);

            zoo = new ZooKeeper(zkaddr, TIMEOUT_MS, new Watcher() {
                public void process(WatchedEvent we) {
                    if (we.getState() == KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }
            });
            connectedSignal.await();
            logger.info("Connected!");
        } catch (Exception e) {
            logger.info("Failed to connect");
            return;
        }

        try {
            Stat stat = zoo.exists(PATH, true);
            if (stat == null) {
                logger.info("PATH does not exist.");
                return;
            } else {
                logger.info("PATH exists");
            }
        } catch (Exception e) {
            logger.info("Failed to get stat");
            return;
        }

        try {
            final CountDownLatch connectedSignal1 = new CountDownLatch(1);
            List<String> servers = zoo.getChildren(PATH, false);
            for( String server : servers) {
                logger.info(server);
                // Access a service running on the machine that zookeeper replies
                target = server;
            }
        } catch(Exception e) {
            logger.info(e.getMessage());
            return;
        }

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        //
        // For the example we use plaintext insecure credentials to avoid needing TLS certificates. To
        // use TLS, use TlsChannelCredentials instead.
        ManagedChannel channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
            .build();
        try {
            blockingStub = GreeterGrpc.newBlockingStub(channel);

            /** Say hello to server. */
            logger.info("Will try to greet " + user + " ...");
            HelloRequest request = HelloRequest.newBuilder().setName(user).build();
            HelloReply response;
            try {
                response = blockingStub.sayHello(request);
            } catch (StatusRuntimeException e) {
                logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
                return;
            }
            logger.info("Greeting: " + response.getMessage());
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    public static void setDebugLevel(Level newLvl) {
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        rootLogger.setLevel(newLvl);
        for (Handler h : handlers) {
            h.setLevel(newLvl);
        }
    }

}
