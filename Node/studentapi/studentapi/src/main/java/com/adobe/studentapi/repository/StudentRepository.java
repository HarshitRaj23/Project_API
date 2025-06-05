package com.adobe.studentapi.repository;

import com.adobe.studentapi.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, String> {
    Student findByUid(String uid);
}
