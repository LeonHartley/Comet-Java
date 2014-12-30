package com.cometproject.test;

import com.cometproject.server.game.rooms.objects.misc.Position;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

public class PositionMapTest extends TestCase {
    private Map<Position, String> positionMap;

    public PositionMapTest() {
        this.positionMap = new HashMap<>();

        this.positionMap.put(new Position(1, 2), "Hey");
        this.positionMap.put(new Position(1, 3), "lala");
    }

    public void testMapContains() {
        Position pos = new Position(1, 3);
        Assert.assertTrue(this.positionMap.containsKey(pos));
        Assert.assertEquals(this.positionMap.get(pos), "lala");
    }
}
