package com.adobe.studentapi.controller;

import com.adobe.studentapi.model.Student;
import com.adobe.studentapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepo;

    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody Student student) {
        Student existing = studentRepo.findByUid(student.getUid());
        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Student with UID '" + student.getUid() + "' already exists.");
        }
        Student saved = studentRepo.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepo.findAll();
    }

    @GetMapping("/{uid}")
    public Student getStudent(@PathVariable String uid) {
        return studentRepo.findByUid(uid);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<?> updateStudent(@PathVariable String uid, @RequestBody Student updatedStudent) {
        Student existing = studentRepo.findByUid(uid);
        if (existing != null) {
            updatedStudent.setId(existing.getId());
            updatedStudent.setUid(uid); // ensure UID doesn't change
            return ResponseEntity.ok(studentRepo.save(updatedStudent));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<String> deleteStudent(@PathVariable String uid) {
        Student student = studentRepo.findByUid(uid);
        if (student != null) {
            studentRepo.delete(student);
            return ResponseEntity.ok("Deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }
}
