package klevente.hu.hophelper.data;

import java.util.ArrayList;
import java.util.List;

public class BeerList {
    private static List<Beer> list = new ArrayList<>();

    public void add(Beer e) {
        list.add(e);
    }

    public void add(int i, Beer e) {
        list.add(i, e);
    }

    public Beer get(int i) {
        return list.get(i);
    }

    public int size() {
        return list.size();
    }

    public Beer remove(int i) {
        return list.remove(i);
    }

    public boolean remove(Beer e) {
        return list.remove(e);
    }
}
