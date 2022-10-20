package ar.edu.itba.pod.client.query5;

import ar.edu.itba.pod.client.utils.Hazelcast;
import com.hazelcast.client.HazelcastClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {

    private final static Logger logger = LoggerFactory.getLogger(Client.class);
    public static void main(String[] args) {
        logger.info("Query 1 client starting...");
        
        var parser = new CliParser();
        var arguments = parser.parse(args);
        
        if (arguments.isEmpty()) {
            logger.error("Invalid arguments");
            return;
        }
        
        try {
            var hazelcast = Hazelcast.connect(arguments.get());
        }
        finally {
            // Shutdown
            HazelcastClient.shutdownAll();
        }
    }
}
