import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class Production implements Comparable<Object> {
    private String title, plot;
    private List<String> directors = new ArrayList<>();

    // Users that have rated this production at some point
    private List<String> awardedUsernames = new ArrayList<>();

    @JsonSerialize(using = CustomProductionActorListSerializer.class)
    @JsonDeserialize(using = CustomProductionDeserializer.class)
    private List<Actor> actors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private List<Rating> ratings = new ArrayList<>();
    private Double averageRating = 0.0;
    private String trailerLink = null;
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
        List<Rating> newRatings = new ArrayList<>(ratings);
        newRatings.sort(null);
        newRatings = newRatings.reversed();

        return newRatings;
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

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    public String getTrailerLink() {
        return this.trailerLink;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
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
        this.updateScore();
    }

    public List<String> getAwardedUsernames() {
        return new ArrayList<>(awardedUsernames);
    }

    public void addAwardedUser(User<?> user) {
        awardedUsernames.add(user.getUsername());
    }

    public void copyNonUpdatableInformationOver(Production production) {
        production.averageRating = this.averageRating;
        production.ratings = this.ratings;
        production.awardedUsernames = this.awardedUsernames;
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

class CustomProductionActorListSerializer extends JsonSerializer<List<Actor>> {
    @Override
    public void serialize(List<Actor> actors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<String> actorNames = new ArrayList<>();
        for (Actor actor : actors) {
            actorNames.add(actor.getName());
        }

        jsonGenerator.writeObject(actorNames);
    }
}