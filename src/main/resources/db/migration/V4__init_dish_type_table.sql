create TABLE w_dish (
    id serial not null constraint dish_pk primary key,
    dish_label VARCHAR(20) NOT NULL
);

create TABLE w_users_dish(
    id serial constraint w_users_dish_pk primary key not null,
    user_id integer,
    dish_id integer,
    foreign key (user_id) references w_users (id),
    foreign key (dish_id) references w_dish (id)
)