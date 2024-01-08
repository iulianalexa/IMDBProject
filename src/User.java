import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

abstract public class User<T extends Comparable<Object>> implements Observer {
    User(UnknownUser unknownUser) {
        this.username = unknownUser.username;
        this.experience = unknownUser.experience;
        this.information = unknownUser.information;
        this.accountType = unknownUser.userType;
        this.notificationList = unknownUser.notifications;
        this.favorites = new TreeSet<>();

        for (String productionTitle : unknownUser.favoriteProductions) {
            Production production = IMDB.getInstance().searchForProduction(productionTitle);
            if (production != null) {
                @SuppressWarnings("unchecked")
                User<Production> productionUser = (User<Production>) this;
                productionUser.favorites.add(production);
            }
        }

        for (String actorName : unknownUser.favoriteActors) {
            Actor actor = IMDB.getInstance().searchForActor(actorName);
            if (actor != null) {
                @SuppressWarnings("unchecked")
                User<Actor> actorUser = (User<Actor>) this;
                actorUser.favorites.add(actor);
            }
        }
    }

    public static class UnknownUser {
        private String username;
        private int experience = 0;
        private Information information;
        private AccountType userType;
        private final List<String> productionsContribution = new ArrayList<>();
        private final List<String> actorsContribution = new ArrayList<>();
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<String> favoriteProductions = new ArrayList<>();
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        private final List<String> favoriteActors = new ArrayList<>();
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

        public UnknownUser(User<?> user) {
            this();
            this.username = user.username;
            this.experience = user.experience;
            this.information = user.information;
            this.userType = user.accountType;

            if (user instanceof Staff<?> staff) {
                for (Object contribution : staff.getContributions()) {
                    if (contribution instanceof Production production) {
                        this.productionsContribution.add(production.getTitle());
                    } else if (contribution instanceof Actor actor) {
                        this.actorsContribution.add(actor.getName());
                    }
                }
            }

            for (Object favorite : user.favorites) {
                if (favorite instanceof Production production) {
                    this.favoriteProductions.add(production.getTitle());
                } else if (favorite instanceof Actor actor) {
                    this.favoriteActors.add(actor.getName());
                }
            }

            this.notifications = user.getNotificationList();
        }
    }

    @JsonDeserialize(builder = Information.InformationBuilder.class)
    public static class Information {
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

        @JsonSerialize(using = LocalDateCustomSerializer.class)
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

        @JsonAutoDetect(creatorVisibility = JsonAutoDetect.Visibility.ANY, setterVisibility = JsonAutoDetect.Visibility.ANY)
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
                    if (birthDate == null) {
                        this.birthDate = null;
                    } else {
                        this.birthDate = LocalDate.parse(birthDate, DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
                    }
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

    public void awardExperience(ExperienceStrategy strategy) {
        if (accountType != AccountType.ADMIN) {
            experience += strategy.calculateExperience();
        }
    }

    @Override
    public void update(String message) {
        notificationList.add(message);
    }
}

class LocalDateCustomSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (localDateTime == null) {
            jsonGenerator.writeNull();
        } else {
            jsonGenerator.writeString(localDateTime.toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
    }
}