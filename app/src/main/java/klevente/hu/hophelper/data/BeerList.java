package klevente.hu.hophelper.data;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Deprecated
    public static void debugAddTestBeers() {
        Beer beer1 = new Beer(0, "Uradalmi Intro", "nagyon finom", "IPA", 1041, 1012, 5.2);
        beer1.addMalt("Pils", 3.0);
        beer1.addMalt("Wheat", 0.5);
        beer1.addMalt("Caramunich", 0.1);
        beer1.addHop("Citra", 40.0);
        beer1.addHop("Centennial", 10.0);
        beer1.yeast = "Safale US-05";
        BeerList.add(beer1);

        Beer beer2 = new Beer(1, "Ugar Stróman", "hazy af", "New England IPA", 1053, 1020, 5.6);
        beer2.addMalt("Pils", 3.0);
        beer2.addMalt("Wheat", 0.5);
        beer2.addMalt("Caramunich", 0.1);
        beer2.addHop("Citra", 40.0);
        beer2.addHop("Centennial", 10.0);
        beer2.yeast = "Safale US-05";
        BeerList.add(beer2);
    }
}
