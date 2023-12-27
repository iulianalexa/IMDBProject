import java.util.ArrayList;

public class Admin<T extends Comparable<Object>> extends Staff<T> {
    public Admin(User.UnknownUser unknownUser) {
        super(unknownUser);
    }

    void addUser(User<?> user) {
        IMDB.getInstance().addUser(user);
    }

    void removeUser(User<?> user) {
        // Delete ratings
        ArrayList<Production> productions = new ArrayList<>(IMDB.getInstance().getMovieList());
        productions.addAll(IMDB.getInstance().getSeriesList());

        for (Production production : productions) {
            for (Rating rating : production.getRatings()) {
                if (rating.getUsername().equals(user.getUsername())) {
                    production.removeRating(rating);
                }
            }
        }

        // Delete requests
        for (Request request : IMDB.getInstance().getRequestList()) {
            if (request.getAuthorUsername().equals(user.getUsername())) {
                IMDB.getInstance().removeRequest(request);
            }
        }

        // Reassign contributions
        for (Production production : productions) {
            if (production.getAddedBy() != null && production.getAddedBy().equals(user.getUsername())) {
                production.setAddedBy(null);
            }
        }

        for (Actor actor : IMDB.getInstance().getActors()) {
            if (actor.getAddedBy() != null && actor.getAddedBy().equals(user.getUsername())) {
                actor.setAddedBy(null);
            }
        }

        // Remove user
        IMDB.getInstance().removeUser(user);
    }
}
