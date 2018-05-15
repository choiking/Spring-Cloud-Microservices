package xcode.springcloud.locationservice;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Driver {

    private long id;

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private String firstName;

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String lastName;

    public Driver(){
    }

    public Driver(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    public void setId(long id){
        this.id = id;
    }
}
