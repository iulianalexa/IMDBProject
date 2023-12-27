import com.fasterxml.jackson.annotation.JsonProperty;

public enum Genre {
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
