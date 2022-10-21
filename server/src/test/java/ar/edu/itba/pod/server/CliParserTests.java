package ar.edu.itba.pod.server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class CliParserTests {
    private CliParser parser;
    
    @BeforeEach
    public void setup() {
        parser = new CliParser();
    }
    
    @Test
    public void validParameters_ShouldSucceed(){
        // Arrange
        var username = "username";
        var password = "password";
        var networkInterfaces = "192.168.0.*";
        var args = new String[]{
            "-DhazelcastUsername=" + username,
            "-DhazelcastPassword="+ password,
            "-DnetworkInterfaces=" + networkInterfaces
        };
        
        // Act
        var arguments = parser.parse(args);
        
        // Assert
        arguments.ifPresentOrElse(
            (parsedArguments) -> {
                assertThat(parsedArguments.getHazelcastUsername()).isEqualTo(username);
                assertThat(parsedArguments.getHazelcastPassword()).isEqualTo(password);
                assertThat(parsedArguments.getNetworkInterfaces()).containsExactly(networkInterfaces);
            },
            () -> fail("Arguments should be parsed")
        );
    }

    @ParameterizedTest()
    @ValueSource( strings = {
            "just text",
            "1.1",
            "1,11,11,11",
            "1234.123.123.123"
    })
    public void invalidNetworkInterfaces_ShouldFail(String networkInterface){
        // Arrange
        var args = new String[]{
                "-DnetworkInterfaces=" + networkInterface
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> assertThat(parsedArguments.getNetworkInterfaces()).isNullOrEmpty(),
                () -> {}
        );
    }

    @ParameterizedTest()
    @ValueSource( strings = {
            "localhost",
            "1.1.1.1",
            "192.168.0.*",
            "192.168.*.*",
            "192.*.*.*",
            "123.123.123.123"
    })
    public void validNetworkInterfaces_ShouldSucceed(String networkInterface){
        // Arrange
        var args = new String[]{
                "-DnetworkInterfaces=" + networkInterface
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> assertThat(parsedArguments.getNetworkInterfaces()).containsExactly(networkInterface),
                () -> fail("Arguments should be parsed")
        );
    }

    @Test
    public void multipleInterfaces_ShouldSucceed(){
        // Arrange
        var interfaces = new String[]{
                "localhost",
                "1.1.1.1",
                "192.168.0.*",
                "192.168.*.*",
                "192.*.*.*",
                "123.123.123.123"
        };
        
        var args = new String[]{
                "-DnetworkInterfaces=" + String.join(";", interfaces)
        };
        
        // Act
        var arguments = parser.parse(args);
        
        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> assertThat(parsedArguments.getNetworkInterfaces()).containsExactly(interfaces),
                () -> fail("Arguments should be parsed")
        );
    }
    
    @Test
    public void noParameters_ShouldSucceed(){
        // Arrange
        var args = new String[]{};

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> assertThat(parsedArguments).isNotNull(),
                () -> fail("Arguments should be parsed")
        );
    }
}
