import java.util.UUID;

public class User {
    private UUID userId;

    public User() {
        this.userId = UUID.randomUUID();
    }

    public UUID getUserId() {
        return userId;
    }
}