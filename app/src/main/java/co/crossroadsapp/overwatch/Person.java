package co.crossroadsapp.overwatch;

/**
 * Created by sharmha on 10/12/16.
 */
import java.io.Serializable;

public class Person implements Serializable {
    private String name;
    private String email;

    public Person(String n, String e) {
        name = n;
        email = e;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() { return name; }
}
