import java.util.List;
import java.util.SortedSet;

abstract public class Staff extends User implements StaffInterface {
    List<Request> requestList;
    SortedSet<Object> contributions;

    public Staff(UnknownUser unknownUser) {
        super(unknownUser);
    }


    @Override
    public void addProductionSystem(Production p) {
        // TODO
    }

    @Override
    public void addActorSystem(Actor a) {
        // TODO
    }

    @Override
    public void removeProductionSystem(String name) {
        // TODO
    }

    @Override
    public void removeActorSystem(String name) {
        // TODO
    }

    @Override
    public void updateProduction(Production p) {
        // TODO
    }

    @Override
    public void updateActor(Actor a) {
        // TODO
    }

    @Override
    public void solveRequests() {
        // TODO
    }
}
