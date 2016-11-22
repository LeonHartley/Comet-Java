package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.configuration.Configuration;
import org.springframework.data.repository.CrudRepository;

public interface ConfigurationRepository extends CrudRepository<Configuration, String> {
}
