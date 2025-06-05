from pymongo import MongoClient

client = MongoClient("mongodb://localhost:27017/")
db = client["student_mgmt"]
students_collection = db["students"]
marks_collection = db["marks"]
