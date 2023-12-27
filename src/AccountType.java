import com.fasterxml.jackson.annotation.JsonProperty;

public enum AccountType {
    @JsonProperty("Regular")
    REGULAR,
    @JsonProperty("Contributor")
    CONTRIBUTOR,
    @JsonProperty("Admin")
    ADMIN
}
