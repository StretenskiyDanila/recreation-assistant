package ru.recreation.recreationassistant.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "w_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    @Column(name = "city")
    private String city;

    @Column(name = "current_state")
    private String currentState;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "w_users_healths",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "health_id")})
    private Set<Health> healthTags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "w_users_dish",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "dish_id")})
    private Set<Dish> dishTags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "w_users_meals",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "meal_id")})
    private Set<Meal> mealTags;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "w_users_cuisine",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "cuisine_id")})
    private Set<Cuisine> cuisineTags;

    public User(String username, String telegramChatId) {
        this.username = username;
        this.telegramChatId = telegramChatId;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelegramChatId() {
        return telegramChatId;
    }

    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Set<Health> getHealthTags() {
        return healthTags;
    }

    public void setHealthTags(Set<Health> healthTags) {
        this.healthTags = healthTags;
    }

    public Set<Dish> getDishTags() {
        return dishTags;
    }

    public void setDishTags(Set<Dish> dishTags) {
        this.dishTags = dishTags;
    }

    public Set<Meal> getMealTags() {
        return mealTags;
    }

    public void setMealTags(Set<Meal> mealTags) {
        this.mealTags = mealTags;
    }

    public Set<Cuisine> getCuisineTags() {
        return cuisineTags;
    }

    public void setCuisineTags(Set<Cuisine> cuisineTags) {
        this.cuisineTags = cuisineTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(telegramChatId, user.telegramChatId) && Objects.equals(city, user.city) && Objects.equals(currentState, user.currentState) && Objects.equals(healthTags, user.healthTags) && Objects.equals(dishTags, user.dishTags) && Objects.equals(mealTags, user.mealTags) && Objects.equals(cuisineTags, user.cuisineTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, telegramChatId, city, currentState, healthTags, dishTags, mealTags, cuisineTags);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", telegramChatId='" + telegramChatId + '\'' +
                ", city='" + city + '\'' +
                ", currentState='" + currentState + '\'' +
                ", healthTags=" + healthTags +
                ", dishTags=" + dishTags +
                ", mealTags=" + mealTags +
                ", cuisineTags=" + cuisineTags +
                '}';
    }
}
