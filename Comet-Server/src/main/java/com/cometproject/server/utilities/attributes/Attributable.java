package com.cometproject.server.utilities.attributes;

public interface Attributable {
    @Deprecated
    void setAttribute(String attributeKey, Object attributeValue);

    @Deprecated
    Object getAttribute(String attributeKey);

    @Deprecated
    boolean hasAttribute(String attributeKey);

    @Deprecated
    void removeAttribute(String attributeKey);
}
