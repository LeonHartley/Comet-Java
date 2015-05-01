package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.customers.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findOneByEmailAndPassword(String email, String password);

    Customer findByEmail(String email);

    Customer findByName(String name);
}
