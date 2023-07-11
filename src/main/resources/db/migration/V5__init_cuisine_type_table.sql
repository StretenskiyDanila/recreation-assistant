create TABLE w_cuisine (
    id serial not null constraint cuisine_pk primary key,
    cuisine_label VARCHAR(20) NOT NULL
);

insert into w_cuisine(id, cuisine_label)
values (1, 'american'), (2, 'asian'), (3, 'chinese'), (4, 'chinese'), (5, 'french'), (6, 'world');

create table w_users_cuisine(
    id serial constraint w_users_cuisine_pk primary key not null,
    user_id integer,
    cuisine_id integer,
    foreign key (user_id) references w_users (id),
    foreign key (cuisine_id) references w_cuisine (id)
)