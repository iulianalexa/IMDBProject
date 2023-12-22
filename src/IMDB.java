import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class IMDB {
    static IMDB obj = new IMDB();

    public static IMDB getInstance() {
        return obj;
    }

    public void run() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<User.UnknownUser> unknownUserList = mapper.readValue(new File("input/accounts.json"), new TypeReference<List<User.UnknownUser>>() {});
        for (User.UnknownUser unknownUser : unknownUserList) {
            User user = UserFactory.factory(unknownUser);
            // TODO: Add users
        }
    }
}
