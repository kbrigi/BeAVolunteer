package edu.bbte.beavolunteerbackend.validator;

import edu.bbte.beavolunteerbackend.model.User;
import edu.bbte.beavolunteerbackend.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserValidator implements Validator<User> {
    private static final String regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$";
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<String> validate(User user) {
        if (user.getPassword().matches(regexPassword)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            errorList.add("password must contain minimum eight characters, at least one uppercase letter, one lowercase letter and one number");
        }

        if (userRepository.existsUsername(user.getUserName()) > 0) {
            errorList.add("username is already taken!");
        }

        if (userRepository.existsEmail(user.getEmail()) > 0) {
            errorList.add("email is already taken!");
        }
        return errorList;
    }

    @Override
    public String validatePassword(User user, String password) {
        if (password.matches(regexPassword)) {
            user.setPassword(passwordEncoder.encode(password));
            return null;
        } else {
            return "password must contain minimum eight characters, at least one uppercase letter, one lowercase letter and one number:";
        }
    }
}
