package com.cometproject.storage.mysql.data.results;

public interface ResultReaderConsumer {
    void accept(IResultReader data) throws Exception;
}
