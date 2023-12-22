import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Series extends Production {
    private Integer releaseYear, numSeasons;

    @JsonProperty("seasons")
    private Map<String, List<Episode>> episodes;

    @Override
    public void displayInfo() {
        System.out.println(
                "Title: " + super.getTitle() + '\n' +
                "Plot: " + super.getPlot() + '\n' +
                "Directors: " + super.getDirectors() + '\n' +
                "Actors: " + super.getActors() + '\n' +
                "Genres: " + super.getGenres() + '\n' +
                "Average Rating: " + super.getAverageRating() + '\n' +
                "Type: " + super.getType() + '\n' +
                "Release Year: " + releaseYear + '\n' +
                "Number of seasons: " + numSeasons + '\n'
        );
    }
}
