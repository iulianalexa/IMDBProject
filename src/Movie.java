import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Movie extends Production {
    private Integer releaseYear;
    private String duration;

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
                "Duration: " + duration + '\n'
        );
    }
}
