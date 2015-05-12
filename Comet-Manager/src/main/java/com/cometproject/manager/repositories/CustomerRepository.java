package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.customers.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer getCustomerByEmailAndPassword(String email, String password);
}
