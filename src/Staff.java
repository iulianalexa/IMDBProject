import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

abstract public class Staff<T extends Comparable<Object>> extends User<T> implements StaffInterface {
    private final List<Request> requestList = new ArrayList<>();
    private final SortedSet<T> contributions = new TreeSet<>();

    public Staff(UnknownUser unknownUser) {
        super(unknownUser);

        for (String productionTitle : unknownUser.getProductionsContribution()) {
            Production production = IMDB.getInstance().searchForProduction(productionTitle);
            if (production != null) {
                @SuppressWarnings("unchecked")
                SortedSet<Production> productionSortedSet = (SortedSet<Production>) contributions;
                productionSortedSet.add(production);
            }
        }

        for (String actorName : unknownUser.getActorsContribution()) {
            Actor actor = IMDB.getInstance().searchForActor(actorName);
            if (actor != null) {
                @SuppressWarnings("unchecked")
                SortedSet<Actor> actorSortedSet = (SortedSet<Actor>) contributions;
                actorSortedSet.add(actor);
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

        @SuppressWarnings("unchecked")
        SortedSet<Production> productionSortedSet = (SortedSet<Production>) contributions;
        productionSortedSet.add(p);

        for (Actor actor : p.getActors()) {
            actor.addPerformance(p.getTitle(), p.getType());
        }

        this.awardExperience(new NewContributionExperienceStrategy());
    }

    @Override
    public void addActorSystem(Actor a) {
        IMDB.getInstance().addActor(a);

        @SuppressWarnings("unchecked")
        SortedSet<Actor> actorSortedSet = (SortedSet<Actor>) contributions;
        actorSortedSet.add(a);
        this.awardExperience(new NewContributionExperienceStrategy());
    }

    @Override
    public void removeProductionSystem(Production production) {
        if (production instanceof Movie movie) {
            IMDB.getInstance().removeMovie(movie);
        } else if (production instanceof Series series) {
            IMDB.getInstance().removeSeries(series);
        }

        for (Actor actor : production.getActors()) {
            for (Performance performance : actor.getPerformances()) {
                if (performance.getTitle().equals(production.getTitle())) {
                    actor.removePerformance(performance);
                }
            }
        }

        @SuppressWarnings("unchecked")
        SortedSet<Production> productionSortedSet = (SortedSet<Production>) contributions;
        productionSortedSet.remove(production);
    }

    @Override
    public void removeActorSystem(Actor actor) {
        @SuppressWarnings("unchecked")
        SortedSet<Actor> actorSortedSet = (SortedSet<Actor>) contributions;
        actorSortedSet.remove(actor);
        IMDB.getInstance().removeActor(actor);
    }

    @Override
    public void updateProduction(Production p) {
        Production currentProd = IMDB.getInstance().searchForProduction(p.getTitle());
        if (currentProd == null) {
            return;
        }

        if (p.getType() == currentProd.getType()) {
            this.removeProductionSystem(currentProd);
            this.addProductionSystem(p);
        }
    }

    @Override
    public void updateActor(Actor a) {
        Actor currentActor = IMDB.getInstance().searchForActor(a.getName());
        if (currentActor == null) {
            return;
        }

        this.removeActorSystem(currentActor);
        this.addActorSystem(a);
    }

    @Override
    public void closeRequest(Request request, boolean solved) {
        if (solved) {
            request.sendNotification("author",
                    "One of your requests has been accepted and closed!");
        } else {
            request.sendNotification("author",
                    "One of your requests has been rejected and closed.");
        }
        this.requestList.remove(request);
        IMDB.getInstance().removeRequest(request);
        RequestsHolder.removeAdminRequest(request);
    }

    @Override
    public void solveRequest(Request request) {
        User<?> author = IMDB.getInstance().getUser(request.getAuthorUsername());
        if (author != null && (request.getType() == RequestType.ACTOR_ISSUE || request.getType() == RequestType.MOVIE_ISSUE)) {
            author.awardExperience(new SolvedIssueExperienceStrategy());
        }

        this.closeRequest(request, true);
    }

    public List<Request> getRequestList() {
        return new ArrayList<>(requestList);
    }

    public List<T> getContributions() {
        return new ArrayList<>(contributions);
    }

    public void addRequest(Request request) {
        this.requestList.add(request);
    }

    public void removeRequest(Request request) {
        this.requestList.remove(request);
    }

    public boolean contributedTo(Production production) {
        for (Object object : this.contributions) {
            if (object instanceof Production currentProduction) {
                if (currentProduction.getTitle().equals(production.getTitle())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean contributedTo(Actor actor) {
        for (Object object : this.contributions) {
            if (object instanceof Actor currentActor) {
                if (currentActor.getName().equals(actor.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeContribution(T contribution) {
        contributions.remove(contribution);
    }
}
