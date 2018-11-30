package klevente.hu.hophelper.data;

import java.util.ArrayList;
import java.util.HashMap;
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

    @Deprecated
    public static void debugAddTestBeers() {
        Beer beer1 = new Beer(0, "Uradalmi Intro", "nagyon finom", "IPA", 1041, 1012, 5.2);
        beer1.addMalt("Pils", 3.0);
        beer1.addMalt("Wheat", 0.5);
        beer1.addMalt("Caramunich", 0.1);
        beer1.addHop("Citra", 40.0);
        beer1.addHop("Centennial", 10.0);
        beer1.yeast = "Safale US-05";
        beer1.addMashTime(60, 55);
        beer1.addMashTime(10, 62);
        beer1.addMashTime(10, 72);
        beer1.addBoilTime("Centennial", 20, 60);
        beer1.addBoilTime("Citra", 10, 30);
        BeerList.add(beer1);

        Beer beer2 = new Beer(1, "Ugar Stróman", "hazy af", "New England IPA", 1053, 1020, 5.6);
        beer2.addMalt("Pils", 3.0);
        beer2.addMalt("Wheat", 0.5);
        beer2.addMalt("Caramunich", 0.1);
        beer2.addHop("Citra", 40.0);
        beer2.addHop("Centennial", 10.0);
        beer2.yeast = "Safale US-05";
        beer2.addMashTime(60, 55);
        beer2.addMashTime(10, 62);
        beer2.addMashTime(10, 72);
        beer2.addBoilTime("Centennial", 20, 60);
        beer2.addBoilTime("Citra", 10, 30);
        BeerList.add(beer2);
    }
}
