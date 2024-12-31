import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String USERS_FILE = "users.dat"; // Файл для хранения пользователей

    public static void main(String[] args) throws Exception {
        LinkShortener linkShortener = new LinkShortener();
        NotificationService notificationService = new NotificationService();
        Scanner scanner = new Scanner(System.in);
        List<User> users = loadUsers(); // Загружаем пользователей из файла
        User currentUser = null;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveUsers(users); // Сохраняем пользователей при завершении программы
            linkShortener.shutdown();
        }));

        while (true) {
            if (currentUser == null) {
                System.out.println("No user selected. Create a new user or select an existing one.");
                System.out.println("1. Create new user");
                System.out.println("2. Select user");
                System.out.println("3. Exit");
                String choice = scanner.nextLine();

                if (choice.equals("1")) {
                    System.out.println("Enter user name:");
                    String userName = scanner.nextLine();
                    User newUser = new User(userName);
                    users.add(newUser);
                    currentUser = newUser;
                    System.out.println("User created: " + newUser);
                } else if (choice.equals("2")) {
                    if (users.isEmpty()) {
                        System.out.println("No users available. Please create a new user.");
                    } else {
                        System.out.println("Select a user:");
                        for (int i = 0; i < users.size(); i++) {
                            System.out.println((i + 1) + ". " + users.get(i));
                        }
                        int userIndex = Integer.parseInt(scanner.nextLine()) - 1;
                        if (userIndex >= 0 && userIndex < users.size()) {
                            currentUser = users.get(userIndex);
                            System.out.println("Selected user: " + currentUser);
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    }
                } else if (choice.equals("3")) {
                    saveUsers(users); // Сохраняем пользователей перед выходом
                    linkShortener.shutdown();
                    System.out.println("Exiting the program...");
                    System.exit(0);
                }
            } else {
                System.out.println("Current user: " + currentUser);
                System.out.println("Enter command (shorten, redirect, switch, exit):");
                String command = scanner.nextLine();

                if (command.equalsIgnoreCase("shorten")) {
                    System.out.println("Enter URL to shorten:");
                    String url = scanner.nextLine();
                    System.out.println("Enter click limit:");
                    int clickLimit = Integer.parseInt(scanner.nextLine());
                    String shortUrl = linkShortener.shortenLink(url, currentUser, clickLimit);
                    System.out.println("Short URL: " + shortUrl);
                } else if (command.equalsIgnoreCase("redirect")) {
                    System.out.println("Enter short URL:");
                    String shortUrl = scanner.nextLine();
                    String originalUrl = linkShortener.redirect(shortUrl);
                    if (originalUrl != null) {
                        java.awt.Desktop.getDesktop().browse(new URI(originalUrl));
                    } else {
                        notificationService.notifyUser(currentUser, "Link is expired or click limit reached.");
                    }
                } else if (command.equalsIgnoreCase("switch")) {
                    currentUser = null;
                } else if (command.equalsIgnoreCase("exit")) {
                    saveUsers(users); // Сохраняем пользователей перед выходом
                    linkShortener.shutdown();
                    System.out.println("Exiting the program...");
                    System.exit(0);
                }

                linkShortener.cleanUpExpiredLinks();
            }
        }
    }

    // Загрузка пользователей из файла
    private static List<User> loadUsers() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
            return (List<User>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("No users file found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
        return new ArrayList<>();
    }

    // Сохранение пользователей в файл
    private static void saveUsers(List<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
}