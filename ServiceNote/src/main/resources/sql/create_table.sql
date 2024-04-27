create table note
(
    id         int auto_increment,
    title      varchar(100)  not null,
    uid        int            not null,
    content    varchar(8000) not null,
    cover      varchar(8000) null,
    first_edit DATETIME       not null,
    last_edit  DATETIME       not null,
    category   varchar(50)   null,
    constraint note_pk
        primary key (id)
);

create unique index note_id_uindex
    on note (id);











