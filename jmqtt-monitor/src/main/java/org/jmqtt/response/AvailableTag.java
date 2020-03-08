package org.jmqtt.response;

import java.util.List;

public class AvailableTag {
    private String tag;
    private List<String> values;

    @Override
    public String toString() {
        return "AvailableTags{" +
                "tag='" + tag + '\'' +
                ", values=" + values +
                '}';
    }
}
