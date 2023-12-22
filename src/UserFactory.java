public class UserFactory {
    public static User factory(User.UnknownUser unknownUser) {
        switch (unknownUser.userType) {
            case CONTRIBUTOR -> {
                Contributor contributor = new Contributor(unknownUser);
                IMDB.getInstance().contributors.add(contributor);
                return contributor;
            }

            case REGULAR -> {
                Regular regular = new Regular(unknownUser);
                IMDB.getInstance().regulars.add(regular);
                return regular;
            }

            case ADMIN -> {
                Admin admin = new Admin(unknownUser);
                IMDB.getInstance().admins.add(admin);
                return admin;
            }
        }

        return null;
    }
}
