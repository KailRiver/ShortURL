import java.time.LocalDateTime;
import java.util.UUID;

public class ShortLink {
    private String originalUrl;
    private String shortUrl;
    private UUID userId;
    private int clickLimit;
    private int clicks;
    private LocalDateTime creationTime;

    public ShortLink(String originalUrl, String shortUrl, UUID userId, int clickLimit) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.userId = userId;
        this.clickLimit = clickLimit;
        this.clicks = 0;
        this.creationTime = LocalDateTime.now();
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public UUID getUserId() {
        return userId;
    }

    public int getClickLimit() {
        return clickLimit;
    }

    public int getClicks() {
        return clicks;
    }

    public void incrementClicks() {
        this.clicks++;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(creationTime.plusDays(1));
    }

    public boolean isClickLimitReached() {
        return clicks >= clickLimit;
    }
}