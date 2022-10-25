package ar.edu.itba.pod.client.models;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Arguments {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final static Pattern SERVER_ADDRESS_PATTERN = Pattern.
            compile("^(?<host>localhost|\\d?\\d?\\d(?:\\.\\d{1,3}){3}):(?<port>\\d{1,4})$");
    @Getter
    private final List<String> hazelcastServers = new ArrayList<>();

    @Getter
    @Setter
    private String inPath;
    
    @Getter
    @Setter
    private String outPath;
    
    @Getter
    @Setter
    private String hazelcastUsername = null;
    
    @Getter
    @Setter
    private String hazelcastPassword = null;

    
    public boolean isValid(){
        return hazelcastServers.size() > 0 
                && !inPath.isEmpty() 
                && Files.exists(java.nio.file.Path.of(inPath))
                && Files.isDirectory(java.nio.file.Path.of(inPath))
                && !outPath.isEmpty()
                && Files.exists(java.nio.file.Path.of(outPath))
                && Files.isDirectory(java.nio.file.Path.of(outPath));
    }

    public void setHazelcastServersAddress(String serverAddresses) {

        for (var address: serverAddresses.split(";")) {
            var matcher = SERVER_ADDRESS_PATTERN.matcher(address);
            if (matcher.matches()){
                hazelcastServers.add(address);
            }
            else{
                logger.error("The server address {} is not valid!",address);
            }
        }
    }
}
