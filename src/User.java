import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

enum AccountType {
    @JsonProperty("Regular")
    REGULAR,
    @JsonProperty("Contributor")
    CONTRIBUTOR,
    @JsonProperty("Admin")
    ADMIN
}

abstract public class User<T extends Comparable<Object>> implements Observer {
    User(UnknownUser unknownUser) {
        this.username = unknownUser.username;
        this.experience = unknownUser.experience;
        this.information = unknownUser.information;
        this.accountType = unknownUser.userType;
        this.notificationList = unknownUser.notifications;
        this.favorites = new TreeSet<>();

        outerloop:
        for (String productionTitle : unknownUser.favoriteProductions) {
            for (Production currentProduction : IMDB.getInstance().getMovieList()) {
                if (currentProduction.getTitle().equals(productionTitle)) {
                    ((User<Production>) this).favorites.add(currentProduction);
                    break outerloop;
                }
            }

            for (Production currentProduction : IMDB.getInstance().getSeriesList()) {
                if (currentProduction.getTitle().equals(productionTitle)) {
                    ((User<Production>) this).favorites.add(currentProduction);
                    break outerloop;
                }
            }
        }

        outerloop:
        for (String actorName : unknownUser.favoriteActors) {
            for (Actor currentActor : IMDB.getInstance().getActors()) {
                if (currentActor.getName().equals(actorName)) {
                    ((User<Actor>) this).favorites.add(currentActor);
                    break outerloop;
                }
            }
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class UnknownUser {
        private String username;
        private int experience = 0;
        private Information information;
        private AccountType userType;
        private List<String> productionsContribution = new ArrayList<>();
        private List<String> actorsContribution = new ArrayList<>();
        private List<String> favoriteProductions = new ArrayList<>();
        private List<String> favoriteActors = new ArrayList<>();
        private List<String> notifications = new ArrayList<>();

        public List<String> getProductionsContribution() {
            return productionsContribution;
        }

        public List<String> getActorsContribution() {
            return actorsContribution;
        }

        public AccountType getUserType() {
            return this.userType;
        }

        private UnknownUser() {}

        public UnknownUser(String username, Information information, AccountType userType) {
            this();
            this.username = username;
            this.information = information;
            this.userType = userType;
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonDeserialize(builder = Information.InformationBuilder.class)
    public static class Information {
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        public static class Credentials {
            private String email, password;

            private Credentials() {}

            public Credentials(String email, String password) {
                this.email = email;
                this.password = password;
            }
        }

        private Credentials credentials;
        private String name, country;
        LocalDateTime birthDate;
        private String gender;
        private Integer age;

        private Information(Credentials credentials, String name, String country, String gender, Integer age, LocalDateTime birthDate) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.gender = gender;
            this.age = age;
            this.birthDate = birthDate;
        }

        @JsonPOJOBuilder(withPrefix = "")
        public static class InformationBuilder {
            private Credentials credentials;
            private String name, country, gender;
            LocalDateTime birthDate;
            private Integer age;

            public InformationBuilder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            public InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            public InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            public InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            public InformationBuilder age(Integer age) {
                this.age = age;
                return this;
            }

            public InformationBuilder birthDate(String birthDate) throws InvalidInformationException {
                try {
                    this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
                } catch (DateTimeParseException e) {
                    throw new InvalidInformationException(e.getMessage());
                }

                return this;
            }

            public Information build() throws InformationIncompleteException {
                if (credentials == null) {
                    throw new InformationIncompleteException("Credentials are null!");
                }

                if (name == null) {
                    throw new InformationIncompleteException("Name is null!");
                }

                return new Information(credentials, name, country, gender, age, birthDate);
            }
        }
    }

    private Information information;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notificationList;
    private SortedSet<T> favorites;

    public Boolean checkPassword(String password) {
        return this.information.credentials.password.equals(password);
    }

    public Information getInformation() {
        return information;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public String getUsername() {
        return username;
    }

    public int getExperience() {
        return experience;
    }

    public List<String> getNotificationList() {
        return notificationList;
    }

    public SortedSet<T> getFavorites() {
        return favorites;
    }

    public void addToFavourites(T toAdd) {
        this.favorites.add(toAdd);
    }

    public void removeFromFavourites(T toRemove) {
        this.favorites.remove(toRemove);
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void logout() {
        IMDB.getInstance().logout();
    }

    @Override
    public void update(String message) {
        notificationList.add(message);
    }
}
