import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.lang.reflect.Array;
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

abstract public class User {
    public User(UnknownUser unknownUser) {
        this.username = unknownUser.username;
        this.experience = unknownUser.experience == null ? 0 : Integer.parseInt(unknownUser.experience);
        this.information = unknownUser.information;
        this.accountType = unknownUser.userType;
        this.favorites = new TreeSet<>();

        outerloop:
        for (String productionTitle : unknownUser.favoriteProductions) {
            for (Production currentProduction : IMDB.getInstance().getMovieList()) {
                if (currentProduction.getTitle().equals(productionTitle)) {
                    this.favorites.add(currentProduction);
                    break outerloop;
                }
            }

            for (Production currentProduction : IMDB.getInstance().getSeriesList()) {
                if (currentProduction.getTitle().equals(productionTitle)) {
                    this.favorites.add(currentProduction);
                    break outerloop;
                }
            }
        }

        outerloop:
        for (String actorName : unknownUser.favoriteActors) {
            for (Actor currentActor : IMDB.getInstance().getActors()) {
                if (currentActor.getName().equals(actorName)) {
                    this.favorites.add(currentActor);
                    break outerloop;
                }
            }
        }
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class UnknownUser {
        private String username, experience;
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
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonDeserialize(builder = Information.InformationBuilder.class)
    private static class Information {
        @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
        private static class Credentials {
            private String email, password;

            private Credentials() {}

            public Credentials(String email, String password) {
                this.email = email;
                this.password = password;
            }
        }

        private Credentials credentials;
        private String name, country, birthDate;
        private String gender;
        private Integer age;

        private Information(Credentials credentials, String name, String country, String gender, Integer age, String birthDate) {
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
            private String name, country, birthDate, gender;
            private Integer age;

            InformationBuilder credentials(Credentials credentials) {
                this.credentials = credentials;
                return this;
            }

            InformationBuilder name(String name) {
                this.name = name;
                return this;
            }

            InformationBuilder country(String country) {
                this.country = country;
                return this;
            }

            InformationBuilder gender(String gender) {
                this.gender = gender;
                return this;
            }

            InformationBuilder age(Integer age) {
                this.age = age;
                return this;
            }

            InformationBuilder birthDate(String birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            Information build() {
                return new Information(credentials, name, country, gender, age, birthDate);
            }
        }
    }

    private Information information;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notificationList;
    private SortedSet<Object> favorites;

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

    public SortedSet<Object> getFavorites() {
        return favorites;
    }

    public void addToFavourites(Object toAdd) {
        this.favorites.add(toAdd);
    }

    public void removeFromFavourites(Object toRemove) {
        this.favorites.remove(toRemove);
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void logout() {
        // TODO
    }
}
