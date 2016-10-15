package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.hosts.Host;
import org.springframework.data.repository.CrudRepository;

public interface HostRepository extends CrudRepository<Host, String> {
    Host findOneByHostName(String hostName);
}
