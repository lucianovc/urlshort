package url.shortener.core;

public interface IUrlShortener {
    String create(String url);

    void add(String shortUrl, String longUrl);

}
