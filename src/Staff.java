import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

abstract public class Staff<T extends Comparable<Object>> extends User<T> implements StaffInterface {
    List<Request> requestList = new ArrayList<>();
    SortedSet<Object> contributions = new TreeSet<>();

    public Staff(UnknownUser unknownUser) {
        super(unknownUser);

        outerloop:
        for (String productionTitle : unknownUser.getProductionsContribution()) {
            for (Production currentProduction : IMDB.getInstance().getMovieList()) {
                if (currentProduction.getTitle().equals(productionTitle)) {
                    this.contributions.add(currentProduction);
                    break outerloop;
                }
            }

            for (Production currentProduction : IMDB.getInstance().getSeriesList()) {
                if (currentProduction.getTitle().equals(productionTitle)) {
                    this.contributions.add(currentProduction);
                    break outerloop;
                }
            }
        }

        outerloop:
        for (String actorName : unknownUser.getActorsContribution()) {
            for (Actor currentActor : IMDB.getInstance().getActors()) {
                if (currentActor.getName().equals(actorName)) {
                    this.contributions.add(currentActor);
                    break outerloop;
                }
            }
        }
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
