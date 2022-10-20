package ar.edu.itba.pod.client.utils;

import ar.edu.itba.pod.client.Utils;
import ar.edu.itba.pod.client.models.Arguments;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

class TestParser extends BaseParser{
    @Override
    protected Arguments createArgumentObject() {
        return new Arguments();
    }
}

public class BaseParserTests {
    
    private BaseParser parser;
    
    @BeforeEach
    public void setUp(){
        parser = new TestParser();
    }

    
    @Test
    public void allParameters_ShouldSucceed(){
        // Arrange
        var address = "192.168.0.5:5701";
        var inPath = Utils.getFolderPath("inPath/");
        var outPath = Utils.getFolderPath("outPath/");
        var username = "username";
        var password = "password";
        
        var args = new String[]{
                "-Daddresses=" + address,
                "-DinPath=" + inPath,
                "-DoutPath=" + outPath,
                "-Dusername=" + username,
                "-Dpassword=" + password
        };
        
        // Act
        var arguments = parser.parse(args);
        
        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> {
                    assertThat(parsedArguments.getHazelcastServers()).containsExactly(address);
                    assertThat(parsedArguments.getInPath()).isEqualTo(inPath);
                    assertThat(parsedArguments.getOutPath()).isEqualTo(outPath);
                    assertThat(parsedArguments.getHazelcastUsername()).isEqualTo(username);
                    assertThat(parsedArguments.getHazelcastPassword()).isEqualTo(password);
                },
                () -> fail("Arguments should be parsed")
        );
    }
    
    @Test
    public void onlyRequiredParameters_ShouldSucceed(){
        // Arrange
        var address = "192.168.0.5:5701";
        var inPath = Utils.getFolderPath("inPath/");
        var outPath = Utils.getFolderPath("outPath/");

        var args = new String[]{
                "-Daddresses=" + address,
                "-DinPath=" + inPath,
                "-DoutPath=" + outPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> {
                    assertThat(parsedArguments.getHazelcastServers()).containsExactly(address);
                    assertThat(parsedArguments.getInPath()).isEqualTo(inPath);
                    assertThat(parsedArguments.getOutPath()).isEqualTo(outPath);
                },
                () -> fail("Arguments should be parsed")
        );
    }
    
    @Test
    public void multipleHazelServers_ShouldSucceed(){
        // Arrange
        var address = new String[]{"192.168.0.5:5701","localhost:5701"};
        var inPath = Utils.getFolderPath("inPath/");
        var outPath = Utils.getFolderPath("outPath/");

        var args = new String[]{
                "-Daddresses=" + String.join(";", address),
                "-DinPath=" + inPath,
                "-DoutPath=" + outPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> {
                    assertThat(parsedArguments.getHazelcastServers()).containsExactly(address);
                    assertThat(parsedArguments.getInPath()).isEqualTo(inPath);
                    assertThat(parsedArguments.getOutPath()).isEqualTo(outPath);
                },
                () -> fail("Arguments should be parsed")
        );
    }
    
    @ParameterizedTest
    @ValueSource(strings = {
            "just text",
            "1.1",
            "localhost",
            "localhost:5701:5701",
            "192.168.0.1",
            "192.168.0.1:21332123",
            "1,11,11,11",
            "1234.123.123.123"
    })
    public void invalidServerAddress_ShouldFail(String serverAddress){
        // Arrange
        var address = new String[]{serverAddress};
        var inPath = Utils.getFolderPath("inPath/");
        var outPath = Utils.getFolderPath("outPath/");

        var args = new String[]{
                "-Daddresses=" + String.join(";", address),
                "-DinPath=" + inPath,
                "-DoutPath=" + outPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> { fail("Arguments should not be parsed"); },
                () -> {}
        );
    }
    
    @Test
    public void missingInPath_ShouldFail(){

        // Arrange
        var address = "192.168.0.5:5701";
        var outPath = Utils.getFolderPath("outPath/");

        var args = new String[]{
                "-Daddresses=" + address,
                "-DoutPath=" + outPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> fail("Arguments should not be parsed"),
                () -> {}
        );
    }
    
    @Test
    public void invalidInPath_ShouldFail(){
        // Arrange
        var address = "192.168.0.5:5701";
        var inPath = "/invalid/path";
        var outPath = Utils.getFolderPath("outPath/");

        var args = new String[]{
                "-Daddresses=" + address,
                "-DinPath=" + inPath,
                "-DoutPath=" + outPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> fail("Arguments should not be parsed"),
                () -> {}
        );
    }
    
    @Test
    public void missingOutPath_ShouldFail(){
        // Arrange
        var address = "192.168.0.5:5701";
        var inPath = Utils.getFolderPath("inPath/");

        var args = new String[]{
                "-Daddresses=" + address,
                "-DinPath=" + inPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> fail("Arguments should not be parsed"),
                () -> {}
        );
    }
    
    @Test
    public void invalidOutPath_ShouldFail() {
        // Arrange
        var address = "192.168.0.5:5701";
        var inPath = Utils.getFolderPath("inPath/");
        var outPath = "/invalid/path";

        var args = new String[]{
                "-Daddresses=" + address,
                "-DinPath=" + inPath,
                "-DoutPath=" + outPath
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> fail("Arguments should not be parsed"),
                () -> {}
        );
    }
    
    @Test
    public void emptyArguments_ShouldFail(){
        // Arrange
        var args = new String[]{};
        
        // Act
        var arguments = parser.parse(args);
        
        // Assert
        assertThat(arguments).isEmpty();
    }
    
}
