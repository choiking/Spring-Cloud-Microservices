package xcode.springcloud.locationservice;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Account {
    private Long id;
    private String firstName;
    private String lastName;

    protected Account() {}

    public Account(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return String.format(
                "Account[id=%d, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}

