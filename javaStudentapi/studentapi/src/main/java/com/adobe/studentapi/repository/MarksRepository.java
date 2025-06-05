package com.adobe.studentapi.repository;

import com.adobe.studentapi.model.Marks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MarksRepository extends MongoRepository<Marks, String> {
    Marks findByUid(String uid);
}
