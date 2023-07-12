create TABLE w_users
(
    id                       serial           NOT NULL constraint w_users_pk PRIMARY KEY ,
    username                 varchar(32)      NOT NULL,
    telegram_chat_id         char(255)        NOT NULL,
    city                     varchar(32)

);