public class Regular extends User implements RequestsManager {
    @Override
    public void createRequest(Request r) {
        // TODO
    }

    @Override
    public void removeRequest(Request r) {
        // TODO
    }

    void rate(Production production, Integer score, String comment) {
        Rating rating = new Rating(this.username, comment, score);
        production.ratings.add(rating);
        production.updateScore();
    }
}
