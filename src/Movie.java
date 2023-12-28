import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Movie extends Production {
    private Integer releaseYear;
    private String duration;

    @JsonCreator
    Movie() {
        this("", "", "", 0);
    }

    public Movie(String title, String description, String duration, int releaseYear) {
        super(title, description, ProductionType.MOVIE);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        StringBuilder s = new StringBuilder(
                "Title: " + super.getTitle() + '\n' +
                "Plot: " + super.getPlot() + '\n' +
                "Directors: " + super.getDirectors() + '\n' +
                "Actors: " + super.getActors() + '\n' +
                "Genres: " + super.getGenres() + '\n' +
                "Average Rating: " + super.getAverageRating() + '\n' +
                "Type: " + super.getType() + '\n' +
                "Release Year: " + releaseYear + '\n' +
                "Duration: " + duration + '\n'
        );

        List<Rating> ratings = this.getRatings();
        ratings.sort(null);
        ratings = ratings.reversed();
        for (Rating rating : ratings) {
            s.append(rating);
            s.append('\n');
        }

        System.out.println(s);
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }
}
