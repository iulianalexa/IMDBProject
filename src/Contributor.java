import java.util.ArrayList;
import java.util.List;

public class Contributor<T extends Comparable<Object>> extends Staff<T> implements RequestsManager {
    public Contributor(User.UnknownUser unknownUser) {
        super(unknownUser);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.getInstance().addRequest(r);
        if (r.getHasAssigned()) {
            User<?> assignedUser = IMDB.getInstance().getUser(r.getAssignedUsername());
            if (!(assignedUser instanceof Staff<?> assignedStaff)) {
                return;
            }

            assignedStaff.addRequest(r);
        } else {
            RequestsHolder.addAdminRequest(r);
        }
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.getInstance().removeRequest(r);
        if (r.getHasAssigned()) {
            User<?> assignedUser = IMDB.getInstance().getUser(r.getAssignedUsername());
            if (!(assignedUser instanceof Staff<?> assignedStaff)) {
                return;
            }

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
