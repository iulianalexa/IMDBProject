import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Episode {
    private String episodeName, duration;

    @Override
    public String toString() {
        return "Episode{" +
                "episodeName='" + episodeName + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
