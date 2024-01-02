import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Request implements Subject {
    private final Map<String, List<String>> observerUsernames = new HashMap<>();
    private RequestType type;

    @JsonSerialize(using = CustomRequestsSerializer.class)
    @JsonDeserialize(using = CustomRequestsDeserializer.class)
    private LocalDateTime createdDate;
    private String description;

    @JsonProperty("username")
    private String authorUsername;
    @JsonProperty("to")
    private String assignedUsername;


    private String targetName = null;

    @JsonCreator
    private Request() {}

    public Request(RequestType type, LocalDateTime createdDate, String description, String authorUsername, String assignedUsername) {
        this.type = type;
        this.createdDate = createdDate;
        this.description = description;
        this.authorUsername = authorUsername;
        this.assignedUsername = assignedUsername;
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

    public RequestType getType() {
        return type;
    }

    public String getTargetName() {
        return targetName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(
                "ON:" + createdDate + '\n' +
                "TYPE: " + type + '\n' +
                "BY: " + authorUsername + '\n' +
                "TO: " + assignedUsername + '\n');

        if (targetName != null) {
            s.append("ABOUT: ").append(targetName).append('\n');
        }

        s.append("DESCRIPTION: ").append(description);
        return s.toString();
    }

    @JsonAnySetter
    private void setTargetName(String key, String titleOrName) {
        if (key.equals("actorName") || key.equals("movieTitle")) {
            this.targetName = titleOrName;
        }
    }

    @JsonAnyGetter
    private Map<String, String> getTargetNameMap() {
        Map<String, String> map = new HashMap<>();
        if (type == RequestType.MOVIE_ISSUE) {
            map.put("movieTitle", targetName);
            return map;
        } else if (type == RequestType.ACTOR_ISSUE) {
            map.put("actorName", targetName);
            return map;
        } else {
            return null;
        }
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    @Override
    public void subscribe(String observerType, Observer observer) {
        if (!observerUsernames.containsKey(observerType)) {
            observerUsernames.put(observerType, new ArrayList<>());
        }

        List<String> subObserverUsernames = observerUsernames.get(observerType);
        subObserverUsernames.add(((User<?>) observer).getUsername());
    }

    @Override
    public void unsubscribe(String observerType, Observer observer) {
        List<String> subObserverUsernames = observerUsernames.get(observerType);
        if (subObserverUsernames != null) {
            subObserverUsernames.remove(((User<?>) observer).getUsername());
            if (subObserverUsernames.isEmpty()) {
                observerUsernames.remove(observerType);
            }
        }
    }

    @Override
    public void sendNotification(String notificationType, String message) {
        List<String> subObserverUsernames = observerUsernames.get(notificationType);
        if (subObserverUsernames != null) {
            for (String observerUsername : subObserverUsernames) {
                Observer observer = IMDB.getInstance().getUser(observerUsername);
                if (observerUsername != null) {
                    observer.update(message);
                }
            }
        }
    }
}

class CustomRequestsDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String text = jsonParser.getText();
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_DATE_TIME);
    }
}

class CustomRequestsSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(localDateTime.format(DateTimeFormatter.ISO_DATE_TIME));
    }
}