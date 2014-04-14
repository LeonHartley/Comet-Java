package com.cometproject.server.utilities.attributes;

public interface Attributable {
    /*
        This interface will be implemented by lots of other classes in Comet
        and can be used to tie several objects to things such as
        room items, players and room entities.
     */
    public void setAttribute(String attributeKey, Object attributeValue);
    public Object getAttribute(String attributeKey);
    public boolean hasAttribute(String attributeKey);
    public void removeAttribute(String attributeKey);
}
