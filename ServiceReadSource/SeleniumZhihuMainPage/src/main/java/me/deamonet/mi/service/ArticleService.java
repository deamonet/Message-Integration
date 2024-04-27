package me.deamonet.mi.service;

import me.deamonet.mi.entity.Article;

import java.util.List;

public interface ArticleService {
    public List<Article> getArticleUpdates();
    public int markArticle();
}
