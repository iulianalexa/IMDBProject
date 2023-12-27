import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Performance {
    private String title;
    private ProductionType type;

    @Override
    public String toString() {
        return title;
    }

    private Performance() {}

    public Performance(String title, ProductionType type) {
        this();
        this.title = title;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public ProductionType getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}