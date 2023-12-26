import java.util.ArrayList;
import java.util.List;

public interface RequestsManager {
    public void createRequest(Request r);
    public void removeRequest(Request r);
    public List<Request> getRequests();
}
