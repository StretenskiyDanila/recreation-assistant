create TABLE w_health (
    id serial not null constraint health_pk primary key,
    health_label VARCHAR(20) NOT NULL
);

insert into w_health(id, health_label)
values (1, 'Alcohol-Cocktail'), (2, 'Alcohol-Free'), (3, 'Vegetarian');

create table w_users_healths(
    id serial constraint w_users_healths_pk primary key not null,
    user_id integer,
    health_id integer,
    foreign key (user_id) references w_users (id),
    foreign key (health_id) references w_health (id)
)