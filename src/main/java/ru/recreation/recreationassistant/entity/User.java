package ru.recreation.recreationassistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Entity
@Table(name = "w_users")
@NoArgsConstructor
public class User {

    public User(String username, String telegramChatId) {
        this.username = username;
        this.telegramChatId = telegramChatId;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    @Column(name = "city")
    private String city;

    @ManyToMany
    @JoinTable(name = "w_users_healths",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "health_id")})
    private Set<Health> healthTags;

    @ManyToMany
    @JoinTable(name = "w_users_dish",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "dish_id")})
    private Set<Dish> dishTags;

    @ManyToMany
    @JoinTable(name = "w_users_meals",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "meal_id")})
    private Set<Meal> mealTags;

    @ManyToMany
    @JoinTable(name = "w_users_cuisine",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "cuisine_id")})
    private Set<Cuisine> cuisineTags;

}
