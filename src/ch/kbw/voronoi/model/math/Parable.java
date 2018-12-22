package ch.kbw.voronoi.model.math;

import java.util.ArrayList;

public class Parable {

    private ArrayList<Integer[]> parable;

    public Parable() {
        parable = new ArrayList<>();
    }

    public ArrayList<Integer[]> getParable() {
        return parable;
    }

    public void addLeft(Integer[] point) {
        parable.add(0, point);
    }
    public void addRight(Integer[] point) {
        parable.add(parable.size(), point);
    }

    public Integer[] get(int i) {
        return parable.get(i);
    }

    public Integer[] getLast() {
        if (parable.size() > 0) {
            return parable.get(parable.size() - 1);
        }
        return null;
    }
}
