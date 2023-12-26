public class Contributor<T extends Comparable<Object>> extends Staff<T> implements RequestsManager {
    public Contributor(User.UnknownUser unknownUser) {
        super(unknownUser);
    }

    @Override
    public void createRequest(Request r) {
        // TODO
    }

    @Override
    public void removeRequest(Request r) {
        // TODO
    }
}
