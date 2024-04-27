package me.deamonet.mi.source.implentation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.deamonet.mi.auxiliary.Cookie;
import me.deamonet.mi.entity.ZhihuAnswer;
import me.deamonet.mi.source.ScraperSelenium;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.SessionNotCreatedException;
import org.springframework.core.io.ClassPathResource;
import org.openqa.selenium.NoSuchElementException;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class ScraperSeleniumZhihuMainPage implements ScraperSelenium {

    public static String zhihuMainPageUrl = "https://www.zhihu.com";
    public static String xpath_expand_full_article =
            "//*[@id=\"TopstoryContent\"]/div/div/div/div[%d]/div/div/div/div[1]/span/div/button";
    public static String[] xpath_question          = {
            "//*[@id=\"TopstoryContent\"]/div/div/div/div[%d]/div/div/div/h2/div/a",
            "//*[@id=\"TopstoryContent\"]/div/div/div/div[%d]/div/div/div/h2/span/a"
    };
    public static String xpath_answer              =
            "//*[@id=\"TopstoryContent\"]/div/div/div/div[%d]/div/div/div/div[2]/span[1]/div/div/span";
    public static String[] xpath_votes_figure      = {
            "//*[@id=\"TopstoryContent\"]/div/div/div/div[%d]/div/div/div/div[2]/div[3]/div/span/button[1]",
            "//*[@id=\"TopstoryContent\"]/div/div/div/div[%d]/div/div/div/div[2]/div[3]/span/button[1]"
    };

    public static String firefoxBinPath =  "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    public static String geckoDriver = "D:\\WebDeveloment\\Selenium-Driver\\geckodriver.exe";
    public WebDriver driver;
    public List<org.openqa.selenium.Cookie> cookies;
    public List<org.openqa.selenium.Cookie> prepareCookie() throws IOException, JsonSyntaxException{
        ClassPathResource classPathResource = new ClassPathResource("cookie/zhihu-cookie.json");
        Reader reader = Files.newBufferedReader(classPathResource.getFile().toPath());
        JsonReader jsonReader = new JsonReader(reader);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()*1000));
        Gson gson = builder.create();
        Type listOfCookie = new TypeToken<ArrayList<Cookie>>() {}.getType();
        List<Cookie> list = gson.fromJson(jsonReader, listOfCookie);
        cookies = list.stream().map(cookie -> {
            if(Objects.isNull(cookie.getSameSite())){
                cookie.setSameSite("None");
            }
            return new org.openqa.selenium.Cookie(
                            cookie.getName(),
                            cookie.getValue(),
                            cookie.getDomain(),
                            cookie.getPath(),
                            cookie.getExpirationDate(),
                            cookie.isSecure(),
                            cookie.isHttpOnly(),
                            cookie.getSameSite()
                    );
        }).collect(Collectors.toList());
        return cookies;
    }

    public WebDriver prepareDriver() throws SessionNotCreatedException{
//        System.setProperty("webdriver.chrome.driver","D:\\WebDeveloment\\Selenium-Driver\\chromedriver.exe");
//        driver = new ChromeDriver();
        System.setProperty("webdriver.firefox.driver", geckoDriver);
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(firefoxBinPath);
        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        driver.get(zhihuMainPageUrl);
        for (org.openqa.selenium.Cookie cookie: cookies){ driver.manage().addCookie(cookie); }
        driver.get(zhihuMainPageUrl);
        return driver;
    }

    public ZhihuAnswer scrapeOne(int i) throws NoSuchElementException{
        String answerUrl = "";
        String questionTxt = "";
        String votesFigureTxt = "";
        String answerTxt;

        driver.findElement(By.xpath(String.format(xpath_expand_full_article, i))).click();
        for (String xpath: xpath_question){
            try{
                WebElement question = driver.findElement(By.xpath(String.format(xpath, i)));
                questionTxt = question.getText();
                answerUrl = question.getAttribute("href");
                break;
            } catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }
        }

        for (String xpath: xpath_votes_figure){
            try{
                WebElement votes_figure = driver.findElement(By.xpath(String.format(xpath, i)));
                votesFigureTxt = votes_figure.getText();
                break;
            } catch (NoSuchElementException e){
                System.out.println(e.getMessage());
            }
        }

        WebElement answer = driver.findElement(By.xpath(String.format(xpath_answer, i)));
        List<WebElement> paragraphs = answer.findElements(By.tagName("p"));
        answerTxt = paragraphs.stream().map(WebElement::getText).collect(Collectors.joining("\n"));
        return new ZhihuAnswer(answerUrl, questionTxt, answerTxt, votesFigureTxt);
    }

    public List<ZhihuAnswer> scrapeOnePage(){
        List<ZhihuAnswer> answerList = new LinkedList<>();
        for(int i=1; i<14; i++){
            try{
                answerList.add(this.scrapeOne(i));
            } catch (NoSuchElementException e) {
                System.err.println(e.getMessage());
            }
        }
        return answerList;
    }

    public void nextPage(){
        driver.navigate().refresh();
    }

    public void quitDriver(){
        driver.quit();
    }

    public List<ZhihuAnswer> getArticles() throws JsonSyntaxException, IOException {
        ScraperSeleniumZhihuMainPage scraperSeleniumZhihuMainPage = new ScraperSeleniumZhihuMainPage();
        scraperSeleniumZhihuMainPage.prepareCookie();
        scraperSeleniumZhihuMainPage.prepareDriver();
        List<ZhihuAnswer> answerList = scraperSeleniumZhihuMainPage.scrapeOnePage();
        scraperSeleniumZhihuMainPage.quitDriver();
        return answerList;
    }

    public List<ZhihuAnswer> getArticles(int amount) throws JsonSyntaxException, IOException{
        if (amount <= 0){ return Collections.emptyList();
        } else if ( amount > 48){ amount = 48; }

        List<ZhihuAnswer> answerList = new LinkedList<>();
        List<ZhihuAnswer> tempList = new LinkedList<>();
        ScraperSeleniumZhihuMainPage scraperSeleniumZhihuMainPage = new ScraperSeleniumZhihuMainPage();
        scraperSeleniumZhihuMainPage.prepareCookie();
        scraperSeleniumZhihuMainPage.prepareDriver();
        while (amount > answerList.size()) {
//            不使用 tempList 这个变量，新的爬取结果就不会插入到 answerList 当中。这是为什么？？？
            tempList = (List<ZhihuAnswer>) scraperSeleniumZhihuMainPage.scrapeOnePage();
            answerList.addAll(tempList);
            scraperSeleniumZhihuMainPage.nextPage();
        }
        List<ZhihuAnswer> resAnswerList = answerList.stream().limit(amount).collect(Collectors.toList());
        scraperSeleniumZhihuMainPage.quitDriver();
        return resAnswerList;
    }
}
