const mongoose = require("mongoose");

// Student model
const studentSchema = new mongoose.Schema({
  uid: { type: String, required: true, unique: true }, 
  name: { type: String, required: true },
  phone: String,
  fatherName: String,
  motherName: String,
});

const Student = mongoose.models.Student || mongoose.model("Student", studentSchema);

module.exports = Student;
