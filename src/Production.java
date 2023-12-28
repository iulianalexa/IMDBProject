import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
abstract public class Production implements Comparable<Object> {
    private String title, plot;
    private final List<String> directors = new ArrayList<>();

    // Users that have rated this production at some point
    private final List<User<?>> awardedUsers = new ArrayList<>();

    @JsonDeserialize(using = CustomProductionDeserializer.class)
    private List<Actor> actors = new ArrayList<>();
    private final List<Genre> genres = new ArrayList<>();
    private final List<Rating> ratings = new ArrayList<>();
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
        return new ArrayList<>(ratings);
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

    public void removeRating(Rating rating) {
        this.ratings.remove(rating);
    }

    public List<User<?>> getAwardedUsers() {
        return new ArrayList<>(awardedUsers);
    }

    public void addAwardedUser(User<?> user) {
        awardedUsers.add(user);
    }
}

class CustomProductionDeserializer extends JsonDeserializer<List<Actor>> {
    @Override
    public List<Actor> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
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