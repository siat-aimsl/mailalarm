package org.jmqtt.store;

import org.jmqtt.common.bean.Message;

public interface WillMessageStore {

    Message getWillMessage(String clientId);

    boolean hasWillMessage(String clientId);

    void storeWillMessage(String clientId, Message message);

    Message removeWillMessage(String clientId);
}
