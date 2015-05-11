package com.cometproject.manager.repositories.customers;

import org.springframework.data.annotation.Id;

import java.util.List;

public class Customer {
    /**
     * The email of the customer, used for authentication and messaging purposes.
     */
    @Id
    private String email;

    /**
     * The unique identifier of the customer
     */
    private String id;

    /**
     * The password of the customer, used for authentication
     */
    private String password;

    /**
     * The name of the client, this will be displayed on the website
     */
    private String name;

    /**
     * List of instances this customer has been granted access to.
     */
    private List<String> instances;

    public Customer(String email, String id, String password, String name, List<String> instances) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.name = name;
        this.instances = instances;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getInstances() {
        return instances;
    }

    public void setInstances(List<String> instances) {
        this.instances = instances;
    }
}
