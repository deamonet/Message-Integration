package me.deamonet.mi.mapper;


import me.deamonet.mi.entity.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper {
    Article selectArticleById(Integer id);
    List<Article> selectArticleByIds(List<Integer> nids);
    Integer insertArticle(Article article);
    Integer updateArticle(Article article);
    Integer deleteArticleById(Integer id);
}
