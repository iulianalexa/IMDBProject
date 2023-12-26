import com.fasterxml.jackson.annotation.JsonProperty;

public enum ProductionType {
    @JsonProperty("Movie")
    MOVIE,
    @JsonProperty("Series")
    SERIES
}
