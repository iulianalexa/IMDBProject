public class UserFactory {
    public static User factory(User.UnknownUser unknownUser) {
        switch (unknownUser.getUserType()) {
            case CONTRIBUTOR -> {
                return new Contributor(unknownUser);
            }

            case REGULAR -> {
                return new Regular(unknownUser);
            }

            case ADMIN -> {
                return new Admin(unknownUser);
            }
        }

        return null;
    }
}
