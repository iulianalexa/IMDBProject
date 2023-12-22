import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IMDB {
    static IMDB obj = new IMDB();

    List<Regular> regulars = new ArrayList<>();
    List<Contributor> contributors = new ArrayList<>();
    List<Admin> admins = new ArrayList<>();

    List<Actor> actors = new ArrayList<>();
    List<Request> requestList = new ArrayList<>();
    List<Movie> movieList = new ArrayList<>();
    List<Series> seriesList = new ArrayList<>();

    public static IMDB getInstance() {
        return obj;
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

    private void readUsers() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User.UnknownUser> unknownUserList = mapper.readValue(new File("input/accounts.json"), new TypeReference<List<User.UnknownUser>>() {});
        for (User.UnknownUser unknownUser : unknownUserList) {
            User user = UserFactory.factory(unknownUser);
        }
    }

    public void run() throws IOException {
        this.readProductions();
        this.readUsers();
    }
}
