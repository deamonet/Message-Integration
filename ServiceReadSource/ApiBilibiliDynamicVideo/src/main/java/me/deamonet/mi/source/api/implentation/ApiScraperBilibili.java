package me.deamonet.mi.source.api.implentation;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.deamonet.mi.auxiliary.Cookie;
import me.deamonet.mi.entity.Article;
import me.deamonet.mi.entity.BilibiliDynamic;
import me.deamonet.mi.source.api.ApiScraper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.*;

public class ApiScraperBilibili implements ApiScraper {
    public static String apiUrlDynamic = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid=199954543&type_list=268435455";
    public static String apiUrlVideoDynamic = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid=199954543&type=8";
    public BasicCookieStore cookieStore;

    public String getUpdate(String url) throws IOException {
        try(CloseableHttpClient client = HttpClientBuilder
                .create()
                .setDefaultCookieStore(cookieStore)
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/108.0.0.0 Safari/537.36 Edg/108.0.1462.54")
                .build()){

            HttpGet request = new HttpGet(url);
            BasicHttpClientResponseHandler handler = new BasicHttpClientResponseHandler();
            return client.execute(request, handler);
        }
    }

    public BasicCookieStore prepareCookie() throws IOException, JsonSyntaxException {
        ClassPathResource classPathResource = new ClassPathResource("cookies/bilibili-cookie.json");
        Reader reader = Files.newBufferedReader(classPathResource.getFile().toPath());
        JsonReader jsonReader = new JsonReader(reader);
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()*1000));
        Gson gson = builder.create();
        Type listOfCookie = new TypeToken<ArrayList<Cookie>>() {}.getType();
        List<Cookie> cookieList = gson.fromJson(jsonReader, listOfCookie);
        cookieStore = new BasicCookieStore();

        for (Cookie cookie: cookieList){
            BasicClientCookie basicClientCookie = new BasicClientCookie(cookie.getName(), cookie.getValue());
            basicClientCookie.setDomain(cookie.getDomain());
            basicClientCookie.setPath(cookie.getPath());
            basicClientCookie.setExpiryDate(cookie.getExpirationDate());
            basicClientCookie.setSecure(cookie.isSecure());
            basicClientCookie.setHttpOnly(cookie.isHttpOnly());
            cookieStore.addCookie(basicClientCookie);
        }
        return cookieStore;
    }


    public List<BilibiliDynamic> parseJson(String json) throws JsonSyntaxException{
        JsonObject response = (JsonObject) JsonParser.parseString(json);
        JsonObject data = (JsonObject) response.get("data");
        JsonArray cards= (JsonArray) data.get("cards");
        List<BilibiliDynamic> bilibiliDynamicList = new LinkedList<>();
        for (int i=0; i<cards.size(); i++){
            JsonObject card = cards.get(i).getAsJsonObject();
            JsonPrimitive cardPri =(JsonPrimitive) card.get("card");
            JsonObject cardP = (JsonObject) JsonParser.parseString(cardPri.getAsString());
            JsonObject owner = (JsonObject) cardP.get("owner");
            bilibiliDynamicList.add(new BilibiliDynamic(
                    cardP.get("short_link").getAsString(),
                    cardP.get("title").getAsString(),
                    owner.get("name").getAsString()
            ));
        }
        return bilibiliDynamicList;
    }

    public List<Article> getArticles() {
        return Collections.emptyList();
    }

}
