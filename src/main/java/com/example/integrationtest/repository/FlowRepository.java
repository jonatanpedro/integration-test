package com.example.integrationtest.repository;

import com.example.integrationtest.dto.Flow;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowRepository extends MongoRepository<Flow, String>{
}
