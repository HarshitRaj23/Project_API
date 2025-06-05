const express = require("express");
const router = express.Router();
const Marks = require("../models/Marks");
const Student = require("../models/Student"); 

// CREATE marks only if student with uid exists
router.post("/", async (req, res) => {
  try {
    const { uid } = req.body;

    const studentExists = await Student.exists({ uid });
    if (!studentExists) {
      return res.status(400).json({ error: "No student with this uid exists" });
    }

    const marksExists = await Marks.exists({ uid });
    if (marksExists) {
      return res.status(400).json({ error: "Marks for this uid already exist" });
    }

    const marks = new Marks(req.body);
    await marks.save();

    res.status(201).json(marks);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

// READ all marks
router.get("/", async (req, res) => {
  try {
    const allMarks = await Marks.find();
    res.json(allMarks);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// READ marks by uid
router.get("/:uid", async (req, res) => {
  try {
    const marks = await Marks.findOne({ uid: req.params.uid });
    if (!marks) return res.status(404).json({ error: "Marks not found" });
    res.json(marks);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
});

// UPDATE marks by uid
router.put("/:uid", async (req, res) => {
  try {
    const updated = await Marks.findOneAndUpdate(
      { uid: req.params.uid },
      req.body,
      { new: true }
    );
    if (!updated) return res.status(404).json({ error: "Marks not found" });
    res.json(updated);
  } catch (error) {
    res.status(400).json({ error: error.message });
  }
});

module.exports = router;
