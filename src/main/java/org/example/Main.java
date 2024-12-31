import java.net.URI;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        LinkShortener linkShortener = new LinkShortener();
        NotificationService notificationService = new NotificationService();
        Scanner scanner = new Scanner(System.in);

        User user = new User();
        System.out.println("Your UUID: " + user.getUserId());

        while (true) {
            System.out.println("Enter command (shorten, redirect, exit):");
            String command = scanner.nextLine();

            if (command.equalsIgnoreCase("shorten")) {
                System.out.println("Enter URL to shorten:");
                String url = scanner.nextLine();
                System.out.println("Enter click limit:");
                int clickLimit = Integer.parseInt(scanner.nextLine());
                String shortUrl = linkShortener.shortenLink(url, user, clickLimit);
                System.out.println("Short URL: " + shortUrl);
            } else if (command.equalsIgnoreCase("redirect")) {
                System.out.println("Enter short URL:");
                String shortUrl = scanner.nextLine();
                String originalUrl = linkShortener.redirect(shortUrl);
                if (originalUrl != null) {
                    java.awt.Desktop.getDesktop().browse(new URI(originalUrl));
                } else {
                    notificationService.notifyUser(user, "Link is expired or click limit reached.");
                }
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            }

            linkShortener.cleanUpExpiredLinks();
        }
    }
}