import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

public class IMDB {
    private static IMDB obj = new IMDB();
    private User<?> currentUser = null;

    private List<Regular<?>> regulars = new ArrayList<>();
    private List<Contributor<?>> contributors = new ArrayList<>();
    private List<Admin<?>> admins = new ArrayList<>();

    private List<Actor> actors = new ArrayList<>();
    private List<Request> requestList = new ArrayList<>();
    private List<Movie> movieList = new ArrayList<>();
    private List<Series> seriesList = new ArrayList<>();

    public static IMDB getInstance() {
        return obj;
    }

    public List<Regular<?>> getRegulars() {
        return regulars;
    }

    public List<Contributor<?>> getContributors() {
        return contributors;
    }

    public List<Admin<?>> getAdmins() {
        return admins;
    }

    public List<Request> getRequestList() {
        return requestList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public List<Series> getSeriesList() {
        return seriesList;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        this.actors.add(actor);
    }

    private void readProductions() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<JsonNode> jsonNodeList = mapper.readValue(new File("input/production.json"), new TypeReference<List<JsonNode>>() {});
        for (JsonNode jsonNode : jsonNodeList) {
            ProductionType type = mapper.treeToValue(jsonNode.get("type"), ProductionType.class);
            if (type == ProductionType.MOVIE) {
                Movie movie = mapper.treeToValue(jsonNode, Movie.class);
                movieList.add(movie);
            } else if (type == ProductionType.SERIES) {
                Series series = mapper.treeToValue(jsonNode, Series.class);
                seriesList.add(series);
            }
        }
    }

    private void readActors() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        this.actors = mapper.readValue(new File("input/actors.json"), new TypeReference<List<Actor>>() {});
    }

    public User<?> getCurrentUser() {
        return this.currentUser;
    }

    public void setCurrentUser(User<?> currentUser) {
        this.currentUser = currentUser;
    }

    private void readUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User.UnknownUser> unknownUserList = mapper.readValue(new File("input/accounts.json"), new TypeReference<List<User.UnknownUser>>() {});
        for (User.UnknownUser unknownUser : unknownUserList) {
            User<?> user = UserFactory.factory(unknownUser);
            switch (Objects.requireNonNull(user).getAccountType()) {
                case REGULAR -> {
                    this.regulars.add((Regular<?>) user);
                }

                case CONTRIBUTOR -> {
                    this.contributors.add((Contributor<?>) user);
                }

                case ADMIN -> {
                    this.admins.add((Admin<?>) user);
                }
            }
        }
    }

    User<?> getUser(String username) {
        List<User<?>> users = new ArrayList<User<?>>(regulars);
        users.addAll(contributors);
        users.addAll(admins);

        for (User<?> user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }

        return null;
    }

    Movie searchForMovie(String title) {
        for (Movie movie : movieList) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }

        return null;
    }

    Series searchForSeries(String title) {
        for (Series series : seriesList) {
            if (series.getTitle().equalsIgnoreCase(title)) {
                return series;
            }
        }

        return null;
    }

    Production searchForProduction(String title) {
        Movie movie = searchForMovie(title);
        if (movie != null) {
            return movie;
        }

        return searchForSeries(title);
    }

    Actor searchForActor(String name) {
        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(name)) {
                return actor;
            }
        }

        return null;
    }

    public void run() throws IOException {
        boolean noGui = true;

        // Load input data
        this.readActors();
        this.readProductions();
        this.readUsers();

        if (noGui) {
            ConsoleApp.runConsole();
        }
    }
}
