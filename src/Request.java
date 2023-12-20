import java.time.LocalDateTime;

enum RequestType {
    DELETE_ACCOUNT, ACTOR_ISSUE, MOVIE_ISSUE, OTHERS
}

public class Request {
    private RequestType type;
    private LocalDateTime time;
    String name, description;
    String authorUsername, assignedUsername;
}
