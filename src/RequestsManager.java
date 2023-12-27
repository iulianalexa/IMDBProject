import java.util.List;

public interface RequestsManager {
    void createRequest(Request r);
    void removeRequest(Request r);
    List<Request> getRequests();
}
