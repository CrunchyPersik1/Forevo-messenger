package com.example.forevo;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin
public class AuthController {

    // Временная база (в памяти)
    private final List<User> usersDB = new ArrayList<>();

    static class User {
        String email, password;
        Profile profile;
    }

    static class Profile {
        String nick, username, avatar, theme;
    }

    // Регистрация
    @PostMapping("/api/register")
    public Map<String, Object> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        // Проверяем, есть ли уже такой email
        boolean exists = usersDB.stream().anyMatch(u -> u.email.equals(email));
        if (exists) {
            return Map.of("success", false, "error", "Пользователь уже существует");
        }

        User user = new User();
        user.email = email;
        user.password = password;
        user.profile = null; // пока пусто

        usersDB.add(user);

        System.out.println("✅ Зарегистрирован: " + email);
        return Map.of("success", true);
    }

    // Вход
    @PostMapping("/api/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        User user = usersDB.stream()
                .filter(u -> u.email.equals(email) && u.password.equals(password))
                .findFirst().orElse(null);

        if (user == null) {
            return Map.of("success", false, "error", "Неверный логин или пароль");
        }

        return Map.of(
            "success", true,
            "user", Map.of(
                "email", user.email,
                "profile", user.profile
            )
        );
    }

    // Сохранение профиля
    @PostMapping("/api/profile")
    public Map<String, Object> saveProfile(@RequestBody Map<String, String> body) {
        String email = getCurrentUserEmail(); // Упрощённо — в реальности: из сессии
        User user = usersDB.stream().filter(u -> u.email.equals(email)).findFirst().orElse(null);
        if (user != null) {
            Profile p = new Profile();
            p.nick = body.get("nick");
            p.username = body.get("username");
            p.avatar = body.get("avatar");
            p.theme = body.get("theme");
            user.profile = p;
        }
        return Map.of("success", true);
    }

    // Для примера — просто возвращаем первый email
    // В реальности: используй Security или Session
    private String getCurrentUserEmail() {
        return usersDB.isEmpty() ? "test@test.com" : usersDB.get(0).email;
    }
}
