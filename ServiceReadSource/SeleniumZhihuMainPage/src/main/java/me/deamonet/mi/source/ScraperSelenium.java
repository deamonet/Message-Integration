package me.deamonet.mi.source;

import com.google.gson.JsonSyntaxException;
import me.deamonet.mi.entity.Article;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.SessionNotCreatedException;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public interface ScraperSelenium {
    public List<Cookie> prepareCookie() throws  IOException, JsonSyntaxException;
    public WebDriver prepareDriver() throws SessionNotCreatedException;
    public Article scrapeOne(int i) throws NoSuchElementException;
    public List<? extends Article> scrapeOnePage();
    public void nextPage();
    public void quitDriver();
    public List<? extends Article> getArticles() throws IOException;

    public List<? extends Article> getArticles(int amount) throws IOException;
}
