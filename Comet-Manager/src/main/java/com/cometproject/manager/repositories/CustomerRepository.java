package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.customers.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, String> {
    Customer findCustomerByEmailAndPassword(String email, String password);

}
