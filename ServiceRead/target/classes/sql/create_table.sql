create table article
(
    aid           int auto_increment,
    title         varchar(500)          not null,
    abstract      varchar(1000)         null,
    content       varchar(3000)         not null,
    author        varchar(100)          null,
    cover         varchar(3000)         null,
    link          varchar(3000)         null,
    from_site     varchar(1000)         null,
    first_publish DATETIME              not null,
    last_edit     DATETIME              null,
    last_read     DATETIME              null,
    progress      FLOAT   default 0     null,
    is_read       BOOLEAN default false null,
    is_liked      BOOLEAN default false null,
    is_collected  BOOLEAN default false null,
    views         int                   null,
    likes         int                   null,
    dislikes      int                   null,
    constraint article_pk
        primary key (aid)
);

create unique index article_aid_uindex
    on article (aid);

create unique index article_content_uindex
    on article (content);

create unique index article_title_uindex
    on article (title);

