package org.jmqtt.model.param;

import java.util.List;

public class AvailableTag {
    private String tag;
    private List<String> values;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "AvailableTags{" +
                "tag='" + tag + '\'' +
                ", values=" + values +
                '}';
    }
}
