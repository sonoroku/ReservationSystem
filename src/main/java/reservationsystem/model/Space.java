package reservationsystem.model;

import java.util.List;

public class Space {
    private final int id;
    private final String name;
    private final String building;
    private final int capacity;
    private final List<String> features;

    public Space(int id, String name, String building, int capacity, List<String> features) {
        this.id = id;
        this.name = name;
        this.building = building;
        this.capacity = capacity;
        this.features = features;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBuilding() {
        return building;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<String> getFeatures() {
        return features;
    }
}