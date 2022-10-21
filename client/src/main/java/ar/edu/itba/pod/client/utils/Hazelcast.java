package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.models.Arguments;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;

import java.util.Optional;

public class Hazelcast {
    
    public static HazelcastInstance connect(Arguments arguments){
        // Client Config
        final ClientConfig clientConfig = new ClientConfig();

        // Group Config
        final GroupConfig groupConfig = new GroupConfig()
                .setName(Optional.ofNullable(arguments.getHazelcastUsername()).orElse("g3"))
                .setPassword(Optional.ofNullable(arguments.getHazelcastPassword()).orElse("g3-pass"));
        
        clientConfig.setGroupConfig(groupConfig);
        
        // Client Network Config
        final ClientNetworkConfig clientNetworkConfig = new ClientNetworkConfig()
                .setAddresses(arguments.getHazelcastServers());


        clientConfig.setNetworkConfig(clientNetworkConfig);

        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
