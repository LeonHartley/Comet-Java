package com.cometproject.manager.repositories;

import com.cometproject.manager.repositories.instances.Version;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VersionRepository extends CrudRepository<Version, String> {
    Version findOneByVersion(String version);
}
