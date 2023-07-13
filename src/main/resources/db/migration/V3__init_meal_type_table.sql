create TABLE w_meal (
    id serial not null constraint meal_pk primary key,
    meal_label VARCHAR(20) NOT NULL
);

create TABLE w_users_meals(
    id serial constraint w_users_meals_pk primary key not null,
    user_id integer,
    meal_id integer,
    foreign key (user_id) references w_users (id),
    foreign key (meal_id) references w_meal (id)
)