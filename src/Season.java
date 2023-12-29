import java.util.ArrayList;
import java.util.List;

public class Season {
    private String name;
    private List<Episode> episodes;

    public Season(String name, List<Episode> episodes) {
        this.name = name;
        this.episodes = episodes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    @Override
    public String toString() {
        return name;
    }
}
