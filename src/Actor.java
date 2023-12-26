import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
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

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static private class Performance {
        private String title;
        private ProductionType type;

        @Override
        public String toString() {
            return title;
        }
    }

    private String addedBy = null;

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

    public String getBiography() {
        return biography;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
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
                s.append(performance.title).append(" (").append(performance.type).append(")\n");
            }
        }

        System.out.println(s);
    }
}
