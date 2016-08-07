package com.cometproject.server.game.groups.cache;

import com.cometproject.server.game.groups.types.Group;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class GroupCacheEventListener implements CacheEventListener {
    @Override
    public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
        if (element.getObjectValue() instanceof Group) {
            ((Group) element.getObjectValue()).dispose();
        }
    }

    @Override
    public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {

    }

    @Override
    public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {

    }

    @Override
    public void notifyElementExpired(Ehcache ehcache, Element element) {
        if (element.getObjectValue() instanceof Group) {
            ((Group) element.getObjectValue()).dispose();
        }
    }

    @Override
    public void notifyElementEvicted(Ehcache ehcache, Element element) {
        if (element.getObjectValue() instanceof Group) {
            ((Group) element.getObjectValue()).dispose();
        }
    }

    @Override
    public void notifyRemoveAll(Ehcache ehcache) {

    }

    @Override
    public void dispose() {

    }

    @Override
    public GroupCacheEventListener clone() {
        return new GroupCacheEventListener();
    }

}
