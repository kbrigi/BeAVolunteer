package edu.bbte.beavolunteerbackend.validator;

import java.util.ArrayList;
import java.util.List;

public interface Validator<T> {
    List<String> errorList = new ArrayList<>();
    List<String> validate(T t);
    String validatePassword(T t, String password);
}
