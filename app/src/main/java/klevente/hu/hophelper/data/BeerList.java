package klevente.hu.hophelper.data;

import java.util.ArrayList;
import java.util.List;

public class BeerList {
    private static final List<Beer> list = new ArrayList<>();

    public static void add(Beer e) {
        synchronized (list) {
            list.add(e);
        }
    }

    public static void add(int i, Beer e) {
        synchronized (list) {
            list.add(i, e);
        }
    }

    public static Beer get(int i) {
        synchronized (list) {
            return list.get(i);
        }
    }

    public static int size() {
        synchronized (list) {
            return list.size();
        }
    }

    public static Beer remove(int i) {
        synchronized (list) {
            return list.remove(i);
        }
    }

    public static boolean remove(Beer e) {
        synchronized (list) {
            return list.remove(e);
        }
    }
}
