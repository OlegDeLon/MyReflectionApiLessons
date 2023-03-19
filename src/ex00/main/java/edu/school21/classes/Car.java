package edu.school21.classes;

import java.util.StringJoiner;

public class Car {
    private String color;

    public Car() {
        this.color = "white";
    }

    public Car(String color) {
        this.color = color;
    }

    public void Paint(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", User.class.getSimpleName() + "[", "]")
                .add("color='" + color + "'").toString();
    }
}
