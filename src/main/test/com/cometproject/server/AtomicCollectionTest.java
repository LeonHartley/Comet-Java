package com.cometproject.server;

import javolution.util.FastMap;
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Map;

public class AtomicCollectionTest extends TestCase {
    private FastMap<String, String> testMap = new FastMap<String, String>().atomic();

    @Override
    public void setUp() throws Exception {
        this.testMap.add("123", "321");
        this.testMap.add("343", "616");
    }

    @Override
    public void tearDown() throws Exception {
        this.testMap = null;
    }

    public void testClear() {
        assertEquals(testMap.size(), 2);

        Iterator<Map.Entry<String, String>> it = this.testMap.entrySet().iterator();
        while (it.hasNext()) {
            this.testMap.remove(it.next().getKey());
        }

        assertEquals(testMap.size(), 0);
    }
}