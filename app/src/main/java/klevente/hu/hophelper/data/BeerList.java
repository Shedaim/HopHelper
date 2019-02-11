package klevente.hu.hophelper.data;

import java.util.ArrayList;
import java.util.List;

public class BeerList {
    private static List<Beer> list = new ArrayList<>();
    private static final Object sync = new Object();

    public static void setItems(List<Beer> items) {
        synchronized (sync) {
            list = items;
        }
    }

    public static void add(Beer e) {
        synchronized (sync) {
            list.add(e);
        }
    }

    public static void add(int i, Beer e) {
        synchronized (sync) {
            list.add(i, e);
        }
    }

    public static Beer get(int i) {
        synchronized (sync) {
            return list.get(i);
        }
    }

    public static int size() {
        synchronized (sync) {
            return list.size();
        }
    }

    public static Beer remove(int i) {
        synchronized (sync) {
            return list.remove(i);
        }
    }

    public static boolean remove(Beer e) {
        synchronized (sync) {
            return list.remove(e);
        }
    }
}
