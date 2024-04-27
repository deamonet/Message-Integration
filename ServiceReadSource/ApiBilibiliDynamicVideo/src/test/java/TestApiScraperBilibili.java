import me.deamonet.mi.configuration.MainConfiguration;
import me.deamonet.mi.entity.Article;
import me.deamonet.mi.source.api.ApiScraper;
import me.deamonet.mi.source.api.implentation.ApiScraperBilibili;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfiguration.class)
public class TestApiScraperBilibili {


    @ParameterizedTest
    @ValueSource(strings = {"https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/dynamic_new?uid=199954543&type=8"})
    public void testGetUpdate(String url) throws IOException {
        ApiScraper apiScraper = new ApiScraperBilibili();
        String response = apiScraper.getUpdate(url);
        System.out.println(response);
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    public void testParseJSON(String json){
        ApiScraper apiScraper = new ApiScraperBilibili();
        List<? extends Article> articleList = apiScraper.parseJson(json);
        for(Article article: articleList){
            System.out.println(article);
        }
    }

    static Stream<String> stringProvider() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("http-api-response/bilibili-dynamic-api-response.json");
        try (Reader reader = Files.newBufferedReader(classPathResource.getFile().toPath());){
            char[] arr=new char[8 * 1024];
            StringBuilder buffer = new StringBuilder();
            int numCharsRead;
            while ((numCharsRead = reader.read(arr, 0, arr.length)) != -1) {
                buffer.append(arr, 0, numCharsRead);
            }
            return Stream.of(buffer.toString());
        }
    }

}
