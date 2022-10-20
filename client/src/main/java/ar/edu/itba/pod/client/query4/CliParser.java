package ar.edu.itba.pod.client.query4;

import ar.edu.itba.pod.client.utils.BaseParser;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

public class CliParser extends BaseParser {
    @Override
    protected Arguments createArgumentObject() {
        return new Arguments();
    }
    
    
    @Override
    protected void RegisterOptions(Options options){
        super.RegisterOptions(options);
        options.addRequiredOption("Dn","Dn",true,"Top n sensors");
        options.addRequiredOption("Dyear","Dyear",true,"Year");
    }
    
    @Override
    protected ar.edu.itba.pod.client.models.Arguments getArguments(CommandLine cmd){
        Arguments args = (Arguments) super.getArguments(cmd);
        
        try {
            args.setN(Integer.parseInt(cmd.getOptionValue("Dn")));
            args.setYear(Integer.parseInt(cmd.getOptionValue("Dyear")));
        } catch (NumberFormatException e) {
           logger.error("Invalid number format");
        }
        
        return args;
    } 
    

    public static class Arguments extends ar.edu.itba.pod.client.models.Arguments{
        @Getter @Setter
        private int n = 0;
        
        @Getter @Setter
        private int year = 0;
        
        @Override
        public boolean isValid() {
            return super.isValid() && n > 0 && year > 0;
        }
    }
}
