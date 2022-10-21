package ar.edu.itba.pod.server;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


public class CliParser {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CommandLineParser parser = new DefaultParser();
    private final Options options = new Options();

    public CliParser(){
        RegisterOptions(options);
    }

    public Optional<Arguments> parse(String[] args){
        try {
            return Optional.of(getArguments(parser.parse(options, args)));
        }
        catch (ParseException e) {
            logger.error("Error parsing arguments: {}", e.getMessage());
            return Optional.empty();
        }
    }

    protected void RegisterOptions(Options options){
        options.addOption("DnetworkInterfaces","DnetworkInterfaces",true,"Interface to bind hazelcast to");
        options.addOption("DhazelcastUsername","DhazelcastUsername",true,"Hazelcast username");
        options.addOption("DhazelcastPassword","DhazelcastPassword",true,"Hazelcast password");
    }
    
    protected Arguments getArguments(CommandLine cmd) {
        var args = new Arguments();

        if (cmd.hasOption("DnetworkInterfaces")) 
            args.setHazelcastServersNetworkInterfaces(cmd.getOptionValue("DnetworkInterfaces"));
        
        if (cmd.hasOption("DhazelcastUsername"))
            args.setHazelcastUsername(cmd.getOptionValue("DhazelcastUsername"));

        if (cmd.hasOption("DhazelcastPassword"))
            args.setHazelcastPassword(cmd.getOptionValue("DhazelcastPassword"));

        return args;
    }
    
    public static class Arguments {
        private Pattern INTERFACE_PATTERN = Pattern.compile("^localhost|(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)\\.(\\d{1,3}|\\*)$");
        protected final Logger logger = LoggerFactory.getLogger(getClass());
        @Getter
        private List<String> networkInterfaces = null;
        
        @Getter @Setter
        private String hazelcastUsername = null;
        @Getter @Setter
        private String hazelcastPassword = null;

        public void setHazelcastServersNetworkInterfaces(String serverAddresses) {
            var list = new ArrayList<String>();
            
            for (var address: serverAddresses.split(";")) {
                if (INTERFACE_PATTERN.matcher(address).matches()) {
                    list.add(address);
                }
                else {
                    logger.error("Invalid network interface: {}", address);
                }
            }
            
            if (list.size() > 0) {
                networkInterfaces = list;
            }
        }
        
    }
}
