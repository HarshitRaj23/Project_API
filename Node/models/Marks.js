const mongoose = require("mongoose");

const subjectSchema = new mongoose.Schema({
  marks: Number,
  credit: Number,
});

const subjectMarksSchema = new mongoose.Schema({
  subject1: subjectSchema,
  subject2: subjectSchema,
  subject3: subjectSchema,
  subject4: subjectSchema,
  subject5: subjectSchema,
});

const marksSchema = new mongoose.Schema({
  uid: { type: String, required: true },
  semester: {
    sem1: subjectMarksSchema,
    sem2: subjectMarksSchema,
    sem3: subjectMarksSchema,
    sem4: subjectMarksSchema,
    sem5: subjectMarksSchema,
    sem6: subjectMarksSchema,
    sem7: subjectMarksSchema,
    sem8: subjectMarksSchema,
  }
});

module.exports = mongoose.models.Marks || mongoose.model("Marks", marksSchema);
