import java.util.UUID;

public class User {
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