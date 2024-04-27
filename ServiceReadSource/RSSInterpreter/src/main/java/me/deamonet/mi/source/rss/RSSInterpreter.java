package me.deamonet.nar.source;

import me.deamonet.mi.entity.Article;
import org.dom4j.DocumentException;

import java.text.ParseException;
import java.util.List;

public interface RSSInterpreter {
    String getUpdate(String feedUrl);
    List<Article> parseXML(String rssXML) throws DocumentException, ParseException;
    List<Article> getArticles(String feedUrl) throws DocumentException, ParseException;
}
