package com.cometproject.manager.repositories.customers;

import com.cometproject.manager.repositories.InstanceRepository;
import com.cometproject.manager.repositories.customers.roles.CustomerRole;
import com.cometproject.manager.repositories.instances.Instance;
import jersey.repackaged.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;

import java.util.List;

public class Customer {
    /**
     * The unique identifier of the customer
     */
    @Id
    private String id;

    /**
     * The email of the customer, used for authentication and messaging purposes.
     */
    private String email;

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

    /**
     * Is the customer suspended?
     */
    private boolean suspended;

    /**
     * The roles of the customer
     */
    private List<CustomerRole> roles;

    public Customer(String email, String id, String password, String name, List<String> instances, boolean suspended, List<CustomerRole> roles) {
        this.email = email;
        this.id = id;
        this.password = password;
        this.name = name;
        this.instances = instances;
        this.suspended = suspended;
        this.roles = roles;
    }

    public List<Instance> getInstances(InstanceRepository instanceRepository) {
        List<Instance> instances = Lists.newArrayList();

        for (String instanceId : this.instances) {
            Instance instance = instanceRepository.findOne(instanceId);

            if (instance != null) {
                instances.add(instance);
            }
        }

        return instances;
    }

    public List<String> getInstanceIds() {
        return this.instances;
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

    public void setInstances(List<String> instances) {
        this.instances = instances;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public List<CustomerRole> getRoles() {
        return roles;
    }

    public void setRoles(List<CustomerRole> roles) {
        this.roles = roles;
    }

    public boolean hasRole(CustomerRole role) {
        return this.roles.contains(role);
    }
}
