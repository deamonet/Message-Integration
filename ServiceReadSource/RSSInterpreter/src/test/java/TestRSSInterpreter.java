import me.deamonet.mi.configuration.MainConfiguration;
import me.deamonet.mi.entity.Article;
import me.deamonet.mi.source.rss.RSSInterpreter;
import me.deamonet.mi.source.rss.implentation.RSSInterpreterImpl;
import org.dom4j.DocumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MainConfiguration.class)
public class TestRSSInterpreter {
    @Test
    public String testGetUpdate(){
        RSSInterpreter rssInterpreter = new RSSInterpreterImpl();
        String feedUrl = "https://www.digforfire.net/?feed=rss2";
        String rss = rssInterpreter.getUpdate(feedUrl);
        System.out.println(rss);
        System.out.println(rss.length());
        return rss;
    }

    @ParameterizedTest()
    @MethodSource("rssProvider")
    public void testParseXML(String rssXML){
        RSSInterpreter interpreter = new RSSInterpreterImpl();
        try {
            List<Article> articleList = interpreter.parseXML(rssXML);
            for (Article article: articleList){
                System.out.println(article);
            }
        } catch (DocumentException |ParseException e){
            fail(e.getMessage(), e.getCause());
        }
    }


    public static Stream<String> rssProvider() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("rss-xml/dig-for-fire-rss.xml");
        return Stream.of(Files.readString(classPathResource.getFile().toPath(), StandardCharsets.UTF_8));
    }
}
