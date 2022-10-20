package ar.edu.itba.pod.client.query5;

import ar.edu.itba.pod.client.models.Arguments;
import ar.edu.itba.pod.client.utils.BaseParser;

public class CliParser extends BaseParser {
    @Override
    protected Arguments createArgumentObject() {
        return new Arguments();
    }
}
