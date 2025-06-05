package com.adobe.studentapi.controller;

import com.adobe.studentapi.model.*;
import com.adobe.studentapi.repository.MarksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/cgpa")
public class CgpaController {

    @Autowired
    private MarksRepository marksRepo;

    @GetMapping("/{uid}")
    public double calculateCgpa(@PathVariable String uid) {
        Marks marks = marksRepo.findByUid(uid);
        if (marks == null) return 0;

        List<Semester> semesters = Arrays.asList(
                marks.getSem1(), marks.getSem2(), marks.getSem3(), marks.getSem4(),
                marks.getSem5(), marks.getSem6(), marks.getSem7(), marks.getSem8()
        );

        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Semester semester : semesters) {
            if (semester == null) continue;

            List<Subject> subjects = Arrays.asList(
                    semester.getSubject1(), semester.getSubject2(),
                    semester.getSubject3(), semester.getSubject4(),
                    semester.getSubject5()
            );

            for (Subject subject : subjects) {
                if (subject != null) {
                    int credit = subject.getCredit();
                    int gradePoint = getGradePoint(subject.getMarks());
                    totalGradePoints += credit * gradePoint;
                    totalCredits += credit;
                }
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0;
    }

    private int getGradePoint(int mark) {
        if (mark >= 90) return 10;
        else if (mark >= 80) return 9;
        else if (mark >= 70) return 8;
        else if (mark >= 60) return 7;
        else if (mark >= 50) return 6;
        else return 0;
    }
}
