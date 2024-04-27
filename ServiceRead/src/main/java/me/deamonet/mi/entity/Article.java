package me.deamonet.mi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Article {
    private int aid;
    private String title;
    private String articleAbstract;
    private String content;
    private String author;
    private String cover;
    private String link;
    private String fromSite;
    private Date firstPublish;
    private Date lastEdit;
    private Date lastRead;
    private float progress;
    private boolean isRead;
    private boolean isLiked;
    private boolean isCollected;
    private int views;
    private int likes;
    private int dislikes;
    private ArticleType articleType;
}