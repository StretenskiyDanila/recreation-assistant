create TABLE w_meal (
    id serial not null constraint meal_pk primary key,
    meal_label VARCHAR(20) NOT NULL
);

insert into w_meal(id, meal_label)
values (1, 'breakfast'), (2, 'brunch'), (3, 'lunch'), (4, 'dinner'), (5, 'snack'), (6, 'teatime');

create table w_users_meals(
    id serial constraint w_users_meals_pk primary key not null,
    user_id integer,
    meal_id integer,
    foreign key (user_id) references w_users (id),
    foreign key (meal_id) references w_meal (id)
)