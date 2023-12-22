import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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

        this.favourites = new TreeSet<>();
        for (String productionName : unknownUser.productionsContribution) {
            // TODO: Continue
        }
    }

    public static class UnknownUser {
        public String username, experience;
        public Information information;
        public AccountType userType;
        public List<String> productionsContribution, actorsContribution, favoriteProductions, favoriteActors, notifications;
    }

    @JsonDeserialize(builder = Information.InformationBuilder.class)
    private static class Information {
        private static class Credentials {
            public String email, password;

            public Credentials() {}

            public Credentials(String email, String password) {
                this.email = email;
                this.password = password;
            }
        }

        public Credentials credentials;
        public String name, country, birthDate;
        public String gender;
        public Integer age;

        private Information(Credentials credentials, String name, String country, String gender, Integer age, String birthDate) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.gender = gender;
            this.age = age;
            this.birthDate = birthDate;
        }

        public Credentials getCredentials() {
            return this.credentials;
        }

        public String getName() {
            return this.name;
        }

        public String getCountry() {
            return this.country;
        }

        public String getGender() {
            return this.gender;
        }

        public Integer getAge() {
            return this.age;
        }

        @JsonPOJOBuilder(withPrefix = "")
        public static class InformationBuilder {
            Credentials credentials;
            String name, country, birthDate, gender;
            Integer age;

            void credentials(Credentials credentials) {
                this.credentials = credentials;
            }

            void name(String name) {
                this.name = name;
            }

            void country(String country) {
                this.country = country;
            }

            void gender(String gender) {
                this.gender = gender;
            }

            void age(Integer age) {
                this.age = age;
            }

            void birthDate(String birthDate) {
                this.birthDate = birthDate;
            }

            Information build() {
                return new Information(credentials, name, country, gender, age, birthDate);
            }
        }
    }

    Information information;
    AccountType accountType;
    String username;
    int experience;
    List<String> notificationList;
    SortedSet<Object> favourites;

    void addToFavourites(Object toAdd) {
        this.favourites.add(toAdd);
    }

    void removeFromFavourites(Object toRemove) {
        this.favourites.remove(toRemove);
    }

    void setExperience(int experience) {
        this.experience = experience;
    }

    void logout() {
        // TODO
    }
}
