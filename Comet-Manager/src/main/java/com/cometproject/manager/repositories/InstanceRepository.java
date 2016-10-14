package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.instances.Instance;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InstanceRepository extends CrudRepository<Instance, String> {
    Instance findOneByIdAndAuthKey(String id, String authKey);
}
