import java.util.ArrayList;
import java.util.List;

public class RequestsHolder {
    private static final List<Request> adminRequests = new ArrayList<>();

    public static void addAdminRequest(Request r) {
        adminRequests.add(r);
    }

    public static void removeAdminRequest(Request r) {
        adminRequests.remove(r);
    }
}
