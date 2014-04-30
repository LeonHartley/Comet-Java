package com.cometproject.server;

import javolution.util.FastMap;
import junit.framework.TestCase;

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
        Exception exc = null;

        try {
            this.testMap.clear();
        } catch (Exception e) {
            exc = e;
        } finally {
            assertNotNull(exc);

            exc.printStackTrace();
        }
    }
}