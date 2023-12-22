public class Rating {
    private String username, comment;
    private Integer rating;

    public Rating() {}

    public Rating(String username, String comment, Integer rating) {
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "username='" + username + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }
}
