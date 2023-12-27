import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.io.InvalidClassException;
import java.util.ArrayList;
import java.util.List;

enum Genre {
    @JsonProperty("Action")
    ACTION,
    @JsonProperty("Adventure")
    ADVENTURE,
    @JsonProperty("Comedy")
    COMEDY,
    @JsonProperty("Drama")
    DRAMA,
    @JsonProperty("Horror")
    HORROR,
    @JsonProperty("SF")
    SF,
    @JsonProperty("Fantasy")
    FANTASY,
    @JsonProperty("Romance")
    ROMANCE,
    @JsonProperty("Mystery")
    MYSTERY,
    @JsonProperty("Thriller")
    THRILLER,
    @JsonProperty("Crime")
    CRIME,
    @JsonProperty("Biography")
    BIOGRAPHY,
    @JsonProperty("War")
    WAR,
    @JsonProperty("Cooking")
    COOKING
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
abstract public class Production implements Comparable<Object> {
    private String title, plot;
    private List<String> directors = new ArrayList<>();

    private String addedBy = null;

    @JsonDeserialize(using = CustomProductionDeserializer.class)
    private List<Actor> actors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();
    private Double averageRating = 0.0;
    private ProductionType type;

    public Integer getReviewCount() {
        return this.ratings.size();
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public List<String> getDirectors() {
        return new ArrayList<>(directors);
    }

    public List<Actor> getActors() {
        return new ArrayList<>(actors);
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public ProductionType getType() {
        return type;
    }

    public List<Genre> getGenres() {
        return new ArrayList<>(genres);
    }

    public abstract void displayInfo();

    Production() {}

    public Production(String title, String description, ProductionType type) {
        this();
        this.title = title;
        this.plot = description;
        this.type = type;
    }

    @Override
    public int compareTo(Object object) {
        if (object instanceof Production production) {
            return this.title.compareTo(production.title);
        }

        if (object instanceof Actor actor) {
            return this.title.compareTo(actor.getName());
        }

        throw new RuntimeException();
    }

    private void updateScore() {
        if (this.ratings.isEmpty()) {
            this.averageRating = null;
        } else {
            Integer total = 0;
            for (Rating rating : this.ratings) {
                total += rating.getRating();
            }
            this.averageRating = (double) total / this.ratings.size();
        }
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
        this.updateScore();
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void addDirector(String name) {
        this.directors.add(name);
    }

    public void removeDirector(String name) {
        this.directors.remove(name);
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    public void removeActor(Actor actor) {
        this.actors.remove(actor);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
    }
}

class CustomProductionDeserializer extends JsonDeserializer<List<Actor>> {
    @Override
    public List<Actor> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        List<Actor> actors = new ArrayList<>();
        String actorName;

        while ((actorName = jsonParser.nextTextValue()) != null) {
            Actor actor = IMDB.getInstance().searchForActor(actorName);
            if (actor == null) {
                actor = new Actor(actorName, null);
                IMDB.getInstance().addActor(actor);
            }

            actors.add(actor);
        }

        return actors;
    }
}