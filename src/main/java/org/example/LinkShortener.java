import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LinkShortener {
    private Map<String, ShortLink> shortLinks;
    private Map<UUID, Map<String, ShortLink>> userLinks;
    private ScheduledExecutorService scheduler;

    public LinkShortener() {
        this.shortLinks = new HashMap<>();
        this.userLinks = new HashMap<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        // Запускаем задачу каждые 5 минут
        this.scheduler.scheduleAtFixedRate(this::cleanUpExpiredLinks, 0, 5, TimeUnit.MINUTES);
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public String shortenLink(String originalUrl, User user, int clickLimit) {
        String shortUrl = generateShortUrl(originalUrl, user);
        ShortLink shortLink = new ShortLink(originalUrl, shortUrl, user.getUserId(), clickLimit);

        shortLinks.put(shortUrl, shortLink);
        userLinks.computeIfAbsent(user.getUserId(), k -> new HashMap<>()).put(shortUrl, shortLink);

        return shortUrl;
    }

    private String generateShortUrl(String originalUrl, User user) {
        // Простой алгоритм для генерации короткой ссылки
        return "clck.ru/" + originalUrl.hashCode() + user.getUserId().hashCode();
    }

    public String redirect(String shortUrl) {
        ShortLink shortLink = shortLinks.get(shortUrl);
        if (shortLink == null || shortLink.isExpired() || shortLink.isClickLimitReached()) {
            return null;
        }
        shortLink.incrementClicks();
        return shortLink.getOriginalUrl();
    }

    public void cleanUpExpiredLinks() {
        shortLinks.entrySet().removeIf(entry -> entry.getValue().isExpired());
        userLinks.values().forEach(map -> map.entrySet().removeIf(entry -> entry.getValue().isExpired()));
    }
}