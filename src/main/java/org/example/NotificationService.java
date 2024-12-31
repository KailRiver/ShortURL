public class NotificationService {
    public void notifyUser(User user, String message) {
        System.out.println("User " + user.getUserId() + ": " + message);
    }
}