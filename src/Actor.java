import com.fasterxml.jackson.annotation.JsonAutoDetect;

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
    }

    private Actor() {}

    public Actor(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }
    private String name, biography;
    private List<Performance> performances;

    public String getName() {
        return this.name;
    }
}
