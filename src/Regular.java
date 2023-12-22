public class Regular extends User implements RequestsManager {
    public Regular(User.UnknownUser unknownUser) {
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

    void rate(Production production, Integer score, String comment) {
        Rating rating = new Rating(this.getUsername(), comment, score);
        production.addRating(rating);
    }
}
