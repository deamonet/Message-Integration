package me.deamonet.mi.source.api;

import com.google.gson.JsonSyntaxException;
import me.deamonet.mi.entity.Article;
import org.apache.hc.client5.http.cookie.BasicCookieStore;

import java.io.IOException;
import java.util.List;

public interface ApiScraper {
    public String getUpdate(String url) throws IOException;
    public BasicCookieStore prepareCookie() throws IOException, JsonSyntaxException;

    public List<? extends Article> parseJson(String json);
    public List<? extends Article> getArticles();
}
