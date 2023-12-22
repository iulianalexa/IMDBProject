import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Series extends Production {
    private Integer releaseYear, seasonCount, numSeasons;

    @JsonProperty("seasons")
    private Map<String, List<Episode>> episodes;

    @Override
    public void displayInfo() {
        // TODO
    }
}
