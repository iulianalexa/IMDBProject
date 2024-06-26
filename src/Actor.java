import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.ArrayList;
import java.util.List;

public class Actor implements Comparable<Object> {
    @Override
    public int compareTo(Object object) {
        if (object instanceof Production production) {
            return this.name.compareTo(production.getTitle());
        }

        if (object instanceof Actor actor) {
            return this.name.compareTo(actor.name);
        }

        throw new RuntimeException();
    }

    @JsonCreator
    private Actor() {}

    public Actor(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }

    private String name, biography;
    private List<Performance> performances = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public List<Performance> getPerformances() {
        return new ArrayList<>(performances);
    }

    public void addPerformance(String title, ProductionType type) {
        this.performances.add(new Performance(title, type));
    }

    public void setPerformances(List<Performance> performances) {
        this.performances = performances;
    }

    @Override
    public String toString() {
        return name;
    }

    public void displayFullInfo() {
        StringBuilder s = new StringBuilder();

        s.append("Name: ").append(this.name).append('\n');
        if (this.biography != null) {
            s.append("Biography: ").append(this.biography).append('\n');
        }

        if (!this.performances.isEmpty()) {
            s.append("Performances: " + '\n');
            for (Performance performance : this.performances) {
                s.append(performance.getTitle()).append(" (").append(performance.getType()).append(")\n");
            }
        }

        System.out.println(s);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void removePerformance(Performance performance) {
        this.performances.remove(performance);
    }

    public String getBiography() {
        return biography;
    }
}
