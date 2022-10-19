package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("tpe2-g3 Server Starting ...");

        // Config
        final Config config = new Config();

        // Group Config
        final GroupConfig groupConfig = new GroupConfig()
                .setName("g3")
                .setPassword("g3-pass");
        config.setGroupConfig(groupConfig);

        // Network Config
        final MulticastConfig multicastConfig = new MulticastConfig();

        final JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(multicastConfig);

        final InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(List.of("192.168.1.*"))
                .setEnabled(true);

        final NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);

        config.setNetworkConfig(networkConfig);

        // Start Cluster
        Hazelcast.newHazelcastInstance(config);
    }
}
