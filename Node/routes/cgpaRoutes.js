const express = require("express");
const router = express.Router();
const Marks = require("../models/Marks");

// Convert marks to points according to your scale
function calculatePoint(marks) {
  if (marks >= 90) return 10;
  if (marks >= 80) return 9;
  if (marks >= 70) return 8;
  if (marks >= 60) return 7;
  if (marks >= 50) return 6;
  if (marks >= 40) return 5;
  return 4;
}

// Calculate SGPA for a single semester
function calculateSGPA(semester) {
  let totalPointsTimesCredits = 0;
  let totalCredits = 0;

  for (const subjectKey in semester) {
    const subject = semester[subjectKey];
    if (subject && typeof subject.marks === "number" && typeof subject.credit === "number") {
      const point = calculatePoint(subject.marks);
      totalPointsTimesCredits += point * subject.credit;
      totalCredits += subject.credit;
    }
  }

  if (totalCredits === 0) return 0;
  return (totalPointsTimesCredits / totalCredits) * 10;
}

// Calculate CGPA across all semesters
function calculateCGPA(semesters) {
  let sgpaSum = 0;
  let semesterCount = 0;

  for (const semKey in semesters) {
    const sgpa = calculateSGPA(semesters[semKey]);
    if (sgpa > 0) {
      sgpaSum += sgpa;
      semesterCount++;
    }
  }

  if (semesterCount === 0) return 0;
  return sgpaSum / semesterCount;
}

// GET /cgpa/:uid - Calculate and return CGPA for a student UID
router.get("/:uid", async (req, res) => {
  try {
    const uid = req.params.uid;
    const marksDoc = await Marks.findOne({ uid });

    if (!marksDoc) {
      return res.status(404).json({ error: "Marks not found for this uid" });
    }

    const cgpa = calculateCGPA(marksDoc.semester);

    res.json({
      uid,
      cgpa: cgpa.toFixed(2),
    });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

module.exports = router;
