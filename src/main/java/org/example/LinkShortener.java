import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LinkShortener {
    private Map<String, ShortLink> shortLinks;
    private Map<UUID, Map<String, ShortLink>> userLinks;

    public LinkShortener() {
        this.shortLinks = new HashMap<>();
        this.userLinks = new HashMap<>();
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