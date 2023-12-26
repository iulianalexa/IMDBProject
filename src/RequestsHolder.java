import java.lang.reflect.Array;
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

    public static ArrayList<Request> getRequests() {
        return new ArrayList<>(adminRequests);
    }
}
