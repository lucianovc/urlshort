package url.shortener.messaging;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

    private final String shortUrl;
    private final String longUrl;

    @JsonCreator
    public Message(@JsonProperty("shortUrl") String shortUrl, @JsonProperty("longUrl") String longUrl){
        this.shortUrl = shortUrl;
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
