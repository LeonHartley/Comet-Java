package com.cometproject.storage.mysql.data;

public interface ResultReaderConsumer {
    void accept(IResultReader data) throws Exception;
}
