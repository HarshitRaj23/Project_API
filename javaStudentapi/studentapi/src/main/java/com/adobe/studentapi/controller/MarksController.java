package com.adobe.studentapi.controller;

import com.adobe.studentapi.model.Marks;
import com.adobe.studentapi.model.Student;
import com.adobe.studentapi.repository.MarksRepository;
import com.adobe.studentapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/marks")
public class MarksController {

    @Autowired
    private MarksRepository marksRepo;

    @Autowired
    private StudentRepository studentRepo;

    @PostMapping
    public ResponseEntity<?> createMarks(@RequestBody Marks marks) {
        Student student = studentRepo.findByUid(marks.getUid());
        if (student == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Student with UID " + marks.getUid() + " does not exist.");
        }

        Marks existing = marksRepo.findByUid(marks.getUid());
        if (existing != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Error: Marks already exist for UID " + marks.getUid());
        }

        Marks saved = marksRepo.save(marks);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{uid}")
    public ResponseEntity<?> getMarks(@PathVariable String uid) {
        Student student = studentRepo.findByUid(uid);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Student with UID " + uid + " does not exist.");
        }

        Marks marks = marksRepo.findByUid(uid);
        if (marks == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No marks found for UID " + uid);
        }

        return ResponseEntity.ok(marks);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<?> updateMarks(@PathVariable String uid, @RequestBody Marks updatedMarks) {
        Student student = studentRepo.findByUid(uid);
        if (student == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: Student with UID " + uid + " does not exist.");
        }

        Marks existing = marksRepo.findByUid(uid);
        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No marks found for UID " + uid);
        }

        updatedMarks.setId(existing.getId());
        updatedMarks.setUid(uid);

        Marks saved = marksRepo.save(updatedMarks);
        return ResponseEntity.ok(saved);
    }
}
