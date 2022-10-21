package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.models.Arguments;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public abstract class BaseParser {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final CommandLineParser parser = new DefaultParser();
    private final Options options = new Options();
    
    public BaseParser(){
        RegisterOptions(options);
    }
    
    public Optional<Arguments> parse(String[] args){
        try {
            var parsedArguments = getArguments(parser.parse(options, args));
            return parsedArguments.isValid() ? Optional.of(parsedArguments) : Optional.empty();
        }
        catch (ParseException e) {
            logger.error("Error parsing arguments: {}", e.getMessage());
            return Optional.empty();
        }
    }
    
    protected void RegisterOptions(Options options){
        options.addRequiredOption("Daddresses","Daddresses",true,"semicolon-separated list of Hazelcast server addresses");
        options.addRequiredOption("DinPath","DinPath",true,"path to the input file");
        options.addRequiredOption("DoutPath","DoutPath",true,"path to the output file");
        options.addOption("Dusername","Dusername",true,"Hazelcast username");
        options.addOption("Dpassword","Dpassword",true,"Hazelcast password");
    }
    
    protected abstract Arguments createArgumentObject();

    protected Arguments getArguments(CommandLine cmd) {
        var args = createArgumentObject();
        
        args.setHazelcastServersAddress(cmd.getOptionValue("Daddresses"));
        args.setInPath(cmd.getOptionValue("DinPath"));
        args.setOutPath(cmd.getOptionValue("DoutPath"));
        
        if (cmd.hasOption("Dusername")) 
            args.setHazelcastUsername(cmd.getOptionValue("Dusername"));
        
        if (cmd.hasOption("Dpassword")) 
            args.setHazelcastPassword(cmd.getOptionValue("Dpassword"));
        
        return args;
    }
}
