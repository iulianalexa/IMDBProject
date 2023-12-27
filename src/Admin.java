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
        if (user instanceof Staff<?> staff) {
            for (Object contribution : staff.getContributions()) {
                if (contribution instanceof Production production) {
                    @SuppressWarnings("unchecked")
                    Staff<Production> productionStaff = (Staff<Production>) staff;
                    productionStaff.removeContribution(production);
                } else if (contribution instanceof Actor actor) {
                    @SuppressWarnings("unchecked")
                    Staff<Actor> actorStaff = (Staff<Actor>) staff;
                    actorStaff.removeContribution(actor);
                }
            }
        }

        // Remove user
        IMDB.getInstance().removeUser(user);
    }
}
