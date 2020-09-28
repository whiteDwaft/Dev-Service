package com.whitedwaft.devservice.repositories;

import com.whitedwaft.devservice.dao.RoleDao;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RolesRepository extends MongoRepository<RoleDao, String> {
    RoleDao findByRole(String role);
}
