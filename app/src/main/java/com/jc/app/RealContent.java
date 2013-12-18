package com.jc.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing content for user interfaces created by
 * Android template wizards. Populates the ItemList
 * <p>
 * TODO: Replace all uses of DummyContent with this class before publishing your app.
 */
public class RealContent {

    /**
     * An array of tasks.
     */
    public static List<Task> ITEMS = new ArrayList<Task>();

    /**
     * A map of tasks, by ID.
     */
    public static Map<String, Task> ITEM_MAP = new HashMap<String, Task>();

//    static {
//        // Add 3 sample items.
//        Task item1 = new Task("Winner","true","00:47",47,23,);
//        addItem(item1);
//    }

    static void addItem(Task item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;

        public DummyItem(String id, String content) {
            this.id = id;
            this.content = content;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
