package ar.edu.itba.pod.client.query3;

import ar.edu.itba.pod.client.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class CliParserTests {
    private CliParser parser;
    
    @BeforeEach
    public void setUp() {
        parser = new CliParser();
    }
    
    @Test
    public void withRequiredParameters_ShouldSucceed(){
        // Arrange
        var min = "10000";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dmin=" + min
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        arguments.ifPresentOrElse(
                (parsedArguments) -> {
                    var arg = (CliParser.Arguments) parsedArguments;
                    assertThat(arg.getMin()).isEqualTo(Integer.parseInt(min));
                },
                () -> fail("Arguments should be parsed")
        );
    }
    
    @Test
    public void withoutRequiredParameters_ShouldFail(){
        // Arrange
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/")
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenMinIsText_ShouldFail(){
        // Arrange
        var min = "prueba";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dmin=" + min
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenMinIsNegative_ShouldFail(){
        // Arrange
        var min = "-10000";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dmin=" + min
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
    @Test
    public void whenMinIsZero_ShouldFail(){

        // Arrange
        var min = "0";
        var args = new String[]{
                "-Daddresses=192.168.0.5:5701",
                "-DinPath=" + Utils.getFolderPath("inPath/"),
                "-DoutPath=" + Utils.getFolderPath("outPath/"),
                "-Dmin=" + min
        };

        // Act
        var arguments = parser.parse(args);

        // Assert
        assertThat(arguments).isEmpty();
    }
    
}
