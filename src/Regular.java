import java.util.ArrayList;
import java.util.List;

public class Regular<T extends Comparable<Object>> extends User<T> implements RequestsManager {
    public Regular(User.UnknownUser unknownUser) {
        super(unknownUser);
    }

    @Override
    public void createRequest(Request r) {
        IMDB.getInstance().addRequest(r);
        r.subscribe("author", this);
        User<?> assignedUser = IMDB.getInstance().getUser(r.getAssignedUsername());
        if ((r.getType() == RequestType.MOVIE_ISSUE || r.getType() == RequestType.ACTOR_ISSUE) && assignedUser instanceof Staff<?> assignedStaff) {
            r.subscribe("assigned", assignedUser);
            r.sendNotification("assigned", String.format(
                    "You received a new request by %s!", this.getUsername()
            ));
            assignedStaff.addRequest(r);
        } else {
            RequestsHolder.addAdminRequest(r);
        }
    }

    @Override
    public void removeRequest(Request r) {
        IMDB.getInstance().removeRequest(r);
        User<?> assignedUser = IMDB.getInstance().getUser(r.getAssignedUsername());
        if (assignedUser instanceof Staff<?> assignedStaff) {
            assignedStaff.removeRequest(r);
        } else {
            RequestsHolder.removeAdminRequest(r);
        }
    }

    @Override
    public List<Request> getRequests() {
        List<Request> list = new ArrayList<>();
        for (Request r : IMDB.getInstance().getRequestList()) {
            if (r.getAuthorUsername().equals(this.getUsername())) {
                list.add(r);
            }
        }

        return list;
    }

    void rate(Production production, Integer score, String comment) {
        Rating rating = new Rating(this.getUsername(), comment, score);
        rating.subscribe("regular", this);
        User<?> adder = IMDB.getInstance().getAdder(production);
        if (adder != null) {
            rating.subscribe("contributor", adder);
        }

        for (Rating existingRating : production.getRatings()) {
            existingRating.sendNotification("regular", String.format(
                    "The production you rated, %s, received a new rating! (%d)",
                    production.getTitle(), score
            ));
        }

        rating.sendNotification("contributor", String.format(
                "The production you added, %s, received a new rating! (%d)",
                production.getTitle(), score
        ));

        if (!production.getAwardedUsers().contains(this)) {
            production.addAwardedUser(this);
            this.awardExperience(new NewRatingExperienceStrategy());
        }

        production.addRating(rating);
    }
}
