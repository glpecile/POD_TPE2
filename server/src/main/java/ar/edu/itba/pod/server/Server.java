package ar.edu.itba.pod.server;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;


public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        logger.info("tpe2-g3 Server Starting ...");
        
        var arguments = new CliParser().parse(args);
        
        if (arguments.isEmpty()){
            logger.error("Error parsing arguments");
            return;
        }
        
        var parsedArguments = arguments.get();
        

        // Config
        final Config config = new Config();

        // Group Config
        final GroupConfig groupConfig = new GroupConfig()
                .setName(Optional.ofNullable(parsedArguments.getHazelcastUsername()).orElse("g3"))
                .setPassword(Optional.ofNullable(parsedArguments.getHazelcastPassword()).orElse("g3-pass"));
        config.setGroupConfig(groupConfig);

        // Network Config
        final MulticastConfig multicastConfig = new MulticastConfig();

        final JoinConfig joinConfig = new JoinConfig()
                .setMulticastConfig(multicastConfig);

        final InterfacesConfig interfacesConfig = new InterfacesConfig()
                .setInterfaces(Optional.ofNullable(parsedArguments.getNetworkInterfaces()).orElse(List.of("192.168.0.*")))
                .setEnabled(true);

        final NetworkConfig networkConfig = new NetworkConfig()
                .setInterfaces(interfacesConfig)
                .setJoin(joinConfig);
        config.setNetworkConfig(networkConfig);
        
        Hazelcast.newHazelcastInstance(config);
    }
}
