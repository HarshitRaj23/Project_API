const express = require("express");
const router = express.Router();
const Student = require('../models/student');


// CREATE
router.post("/", async (req, res) => {
    try {
        const student = new Student(req.body);
        await student.save();
        res.status(201).json(student);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

// READ all
router.get("/", async (req, res) => {
    try {
        const students = await Student.find();
        res.json(students);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// READ one
router.get("/:uid", async (req, res) => {
    try {
        const student = await Student.findOne({ uid: req.params.uid });
        if (!student) return res.status(404).json({ error: "Student not found" });
        res.json(student);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

// UPDATE
router.put("/:uid", async (req, res) => {
    try {
        const updated = await Student.findOneAndUpdate(
            { uid: req.params.uid },
            req.body,
            { new: true }
        );
        if (!updated) return res.status(404).json({ error: "Student not found" });
        res.json(updated);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
});

module.exports = router;