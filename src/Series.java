import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Series extends Production {
    private Integer releaseYear, numSeasons;

    @JsonProperty("seasons")
    private Map<String, List<Episode>> episodes = new HashMap<>();

    @JsonCreator
    Series() {
        this("", "", 0, 0);
    }

    public Series(String title, String description, int releaseYear, int numSeasons) {
        super(title, description, ProductionType.SERIES);
        this.releaseYear = releaseYear;
        this.numSeasons = numSeasons;
    }

    @Override
    public void displayInfo() {
        StringBuilder s =
                new StringBuilder("Title: " + super.getTitle() + '\n' +
                        "Plot: " + super.getPlot() + '\n' +
                        "Directors: " + super.getDirectors() + '\n' +
                        "Actors: " + super.getActors() + '\n' +
                        "Genres: " + super.getGenres() + '\n' +
                        "Average Rating: " + super.getAverageRating() + '\n' +
                        "Type: " + super.getType() + '\n' +
                        "Release Year: " + releaseYear + '\n' +
                        "Number of seasons: " + numSeasons + '\n' + '\n');

        for (String season : episodes.keySet()) {
            s.append(season).append('\n');
            for (Episode episode : episodes.get(season)) {
                s.append(episode.getEpisodeName()).append('\n');
            }
            s.append('\n');
        }

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

    public void setNumSeasons(Integer numSeasons) {
        this.numSeasons = numSeasons;
    }

    public void addSeason(String seasonName) {
        this.episodes.put(seasonName, new ArrayList<>());
    }

    public HashMap<String, List<Episode>> getSeasons() {
        return new HashMap<>(this.episodes);
    }

    public void changeSeasonName(String currentName, String newName) {
        Map<String, List<Episode>> newSeasonMap = new HashMap<>();
        for (String key : this.episodes.keySet()) {
            List<Episode> list = this.episodes.get(key);
            if (key.equals(currentName)) {
                key = newName;
            }
            newSeasonMap.put(key, list);
        }
        this.episodes = newSeasonMap;
    }
}
