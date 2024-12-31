import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L; // Уникальный идентификатор версии

    private UUID userId;
    private String name;

    public User(String name) {
        this.userId = UUID.randomUUID();
        this.name = name;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (ID: " + userId + ")";
    }
}