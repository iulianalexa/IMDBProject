import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

abstract public class Staff<T extends Comparable<Object>> extends User<T> implements StaffInterface {
    private List<Request> requestList = new ArrayList<>();
    private SortedSet<Object> contributions = new TreeSet<>();

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
        if (p instanceof Movie movie) {
            IMDB.getInstance().addMovie(movie);
        } else if (p instanceof Series series) {
            IMDB.getInstance().addSeries(series);
        }
    }

    @Override
    public void addActorSystem(Actor a) {
        IMDB.getInstance().addActor(a);
    }

    @Override
    public void removeProductionSystem(String name) {
        Production production = IMDB.getInstance().searchForProduction(name);
        if (production == null) {
            return;
        }

        if (production instanceof Movie movie) {
            IMDB.getInstance().removeMovie(movie);
        } else if (production instanceof Series series) {
            IMDB.getInstance().removeSeries(series);
        }
    }

    @Override
    public void removeActorSystem(String name) {
        Actor actor = IMDB.getInstance().searchForActor(name);
        if (actor == null) {
            return;
        }

        IMDB.getInstance().removeActor(actor);
    }

    @Override
    public void updateProduction(Production p) {
        Production currentProd = IMDB.getInstance().searchForProduction(p.getTitle());
        if (currentProd == null) {
            return;
        }

        if (p instanceof Movie m && currentProd instanceof Movie currentMovie) {
            IMDB.getInstance().removeMovie(currentMovie);
            IMDB.getInstance().addMovie(m);
        } else if (p instanceof Series s && currentProd instanceof Series currentSeries) {
            IMDB.getInstance().removeSeries(currentSeries);
            IMDB.getInstance().addSeries(s);
        }
    }

    @Override
    public void updateActor(Actor a) {
        Actor currentActor = IMDB.getInstance().searchForActor(a.getName());
        if (currentActor == null) {
            return;
        }

        IMDB.getInstance().removeActor(currentActor);
        IMDB.getInstance().addActor(a);
    }

    @Override
    public void closeRequest(Request request) {
        // TODO: Notify
        this.requestList.remove(request);
        IMDB.getInstance().removeRequest(request);
        RequestsHolder.removeAdminRequest(request);
    }

    @Override
    public void solveRequest(Request request) {
        // TODO: Award experience
        this.closeRequest(request);
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public void addRequest(Request request) {
        this.requestList.add(request);
    }

    public void removeRequest(Request request) {
        this.requestList.remove(request);
    }
}
