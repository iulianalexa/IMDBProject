import java.util.List;
import java.util.SortedSet;

enum AccountType {
    REGULAR,
    CONTRIBUTOR,
    ADMIN
}

abstract public class User {
    private static class Information {
        private static class Credentials {
            String email, password;

            public Credentials(String email, String password) {
                this.email = email;
                this.password = password;
            }
        }

        Credentials credentials;
        String name, country;
        Character gender;
        Integer age;

        private Information(Credentials credentials, String name, String country, Character gender, Integer age) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.gender = gender;
            this.age = age;
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

        public Character getGender() {
            return this.gender;
        }

        public Integer getAge() {
            return this.age;
        }

        public static class InformationBuilder {
            Credentials credentials;
            String name, country;
            Character gender;
            Integer age;

            public InformationBuilder(String email, String password) {
                this.credentials = new Credentials(email, password);
            }

            void name(String name) {
                this.name = name;
            }

            void country(String country) {
                this.country = country;
            }

            void gender(Character gender) {
                this.gender = gender;
            }

            void age(Integer age) {
                this.age = age;
            }

            Information build() {
                return new Information(credentials, name, country, gender, age);
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
