import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Request {
    private RequestType type;
    @JsonDeserialize(using = CustomRequestsDeserializer.class)
    private LocalDateTime createdDate;
    private String description;

    @JsonProperty("username")
    private String authorUsername;
    @JsonProperty("to")
    private String assignedUsername;

    private String targetName;
    private boolean hasAssigned = false;

    private Request() {}

    public Request(RequestType type, LocalDateTime createdDate, String description, String authorUsername, String assignedUsername, boolean hasAssigned) {
        this.type = type;
        this.createdDate = createdDate;
        this.description = description;
        this.authorUsername = authorUsername;
        this.assignedUsername = assignedUsername;
        this.hasAssigned = hasAssigned;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getAssignedUsername() {
        return assignedUsername;
    }

    public String getDescription() {
        return description;
    }

    public boolean getHasAssigned() {
        return this.hasAssigned;
    }

    public RequestType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "TYPE: " + type + '\n' +
                "BY: " + authorUsername + '\n' +
                "TO: " + assignedUsername + '\n' +
                "DESCRIPTION: " + description;
    }

    @JsonAnySetter
    private void setTargetName(String key, String titleOrName) {
        if (key.equals("actorName") || key.equals("movieTitle")) {
            this.targetName = titleOrName;
        }
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }
}

class CustomRequestsDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String text = jsonParser.getText();
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
    }
}