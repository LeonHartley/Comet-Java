package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.instances.Instance;
import org.springframework.data.repository.CrudRepository;

public interface InstanceRepository extends CrudRepository<Instance, String> {
    Instance findOneByIdAndAuthKey(String id, String authKey);
}
