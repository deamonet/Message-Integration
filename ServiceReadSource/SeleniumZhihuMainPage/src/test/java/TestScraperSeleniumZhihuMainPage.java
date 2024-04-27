import me.deamonet.mi.entity.Article;
import me.deamonet.mi.source.ScraperSelenium;
import me.deamonet.mi.source.implentation.ScraperSeleniumZhihuMainPage;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;

import java.lang.NullPointerException;

import java.io.IOException;
import java.util.List;
import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class TestScraperSeleniumZhihuMainPage {

    @Test
    public void testPrepareCookie() {
        ScraperSelenium scraperSelenium = new ScraperSeleniumZhihuMainPage();
        try {
            List<Cookie> cookies = scraperSelenium.prepareCookie();
            for (Cookie cookie: cookies){
                System.out.println(cookie.getExpiry());
                assertTrue(Objects.isNull(cookie.getExpiry()) || cookie.getExpiry().after(new Date()));
            }
        } catch (IOException | JsonSyntaxException e){
            fail(e.getMessage(), e.getCause());
        }
    }


    @Test
    public void testPrepareDriver() {
        ScraperSelenium scraper = new ScraperSeleniumZhihuMainPage();
        Exception exception = assertThrows(NullPointerException.class, () -> scraper.prepareDriver());
        assertEquals("Cannot invoke \"java.util.List.iterator()\" because \"this.cookies\" is null", exception.getMessage());
    }



    @Test
    public void testScrapeOne(){
        ScraperSelenium scraper = new ScraperSeleniumZhihuMainPage();
        try{
            scraper.prepareCookie();
            scraper.prepareDriver();
            Article article = scraper.scrapeOne(2);
            assert article.getContent().length() != 0;
        } catch (IOException | JsonSyntaxException e){
            fail(e.getMessage(), e.getCause());
        }
    }

    @ParameterizedTest()
    @ValueSource(ints = {20})
    public void testGetArticles(int amount) {
        ScraperSelenium scraperSelenium = new ScraperSeleniumZhihuMainPage();
        try{
            List<Article> articleList2 = (List<Article>) scraperSelenium.getArticles(amount);
            if (amount > 48){amount = 48;}
            assertEquals(articleList2.size(), amount);
        } catch (IOException | JsonSyntaxException | ClassCastException e){
            fail(e.getMessage(), e.getCause());
        }
    }
}
