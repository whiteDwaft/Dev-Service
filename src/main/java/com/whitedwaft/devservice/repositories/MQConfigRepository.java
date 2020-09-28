package com.whitedwaft.devservice.repositories;

import com.whitedwaft.devservice.dao.MQConfigDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MQConfigRepository extends CrudRepository<MQConfigDao, String> {
    List<MQConfigDao> findTop5ByOrderByTimeAsc();
}
