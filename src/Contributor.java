import java.util.ArrayList;
import java.util.List;

public class Contributor<T extends Comparable<Object>> extends Staff<T> implements RequestsManager {
    public Contributor(User.UnknownUser unknownUser) {
        super(unknownUser);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.getInstance().addRequest(r);
        r.subscribe("author", this);
        User<?> assignedUser = IMDB.getInstance().getUser(r.getAssignedUsername());
        if ((r.getType() == RequestType.MOVIE_ISSUE || r.getType() == RequestType.ACTOR_ISSUE) && assignedUser instanceof Staff<?> assignedStaff) {
            r.subscribe("assigned", assignedUser);
            r.sendNotification("assigned", String.format(
                    "You received a new request by %s!", this.getUsername()
            ));
            assignedStaff.addRequest(r);
        } else {
            RequestsHolder.addAdminRequest(r);
        }
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.getInstance().removeRequest(r);
        User<?> assignedUser = IMDB.getInstance().getUser(r.getAssignedUsername());
        if (assignedUser instanceof Staff<?> assignedStaff) {
            assignedStaff.removeRequest(r);
        } else {
            RequestsHolder.removeAdminRequest(r);
        }
    }

    @Override
    public List<Request> getRequests() {
        List<Request> list = new ArrayList<>();
        for (Request r : IMDB.getInstance().getRequestList()) {
            if (r.getAuthorUsername().equals(this.getUsername())) {
                list.add(r);
            }
        }

        return list;
    }
}
