import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

enum ProductionType {
    @JsonProperty("Movie")
    MOVIE,
    @JsonProperty("Series")
    SERIES
}

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
    WAR
}
abstract public class Production implements Comparable<Production> {
    public String title, description;
    public List<String> directors, actors;
    public List<Genre> genres;
    public List<Rating> ratings;
    public Double score;
    public ProductionType type;

    public abstract void displayInfo();

    @Override
    public int compareTo(Production production) {
        return this.title.compareTo(production.title);
    }

    public void updateScore() {
        if (this.ratings.isEmpty()) {
            this.score = null;
        } else {
            Integer total = 0;
            for (Rating rating : this.ratings) {
                total += rating.rating;
            }
            this.score = (double) total / this.ratings.size();
        }
    }
}
