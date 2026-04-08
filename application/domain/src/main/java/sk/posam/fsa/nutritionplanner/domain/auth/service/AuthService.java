package sk.posam.fsa.nutritionplanner.domain.auth.service;

public class AuthService {

    public String login(String email, String password) {
        if ("admin@example.com".equals(email) && "admin".equals(password)) {
            return "ADMIN";
        }

        if ("user@example.com".equals(email) && "user".equals(password)) {
            return "USER";
        }

        return null;
    }
}