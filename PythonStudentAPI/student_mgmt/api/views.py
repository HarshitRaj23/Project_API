from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
from bson import ObjectId
from .db import students_collection, marks_collection


# Utility: recursively convert ObjectId to string
def convert_objectid(obj):
    if isinstance(obj, ObjectId):
        return str(obj)
    elif isinstance(obj, dict):
        return {k: convert_objectid(v) for k, v in obj.items()}
    elif isinstance(obj, list):
        return [convert_objectid(item) for item in obj]
    return obj


# Student APIs

@csrf_exempt
def create_student(request):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            if students_collection.find_one({"uid": data["uid"]}):
                return JsonResponse({"error": "UID already exists"}, status=400)

            inserted_id = students_collection.insert_one(data).inserted_id
            data["_id"] = str(inserted_id)
            return JsonResponse({"message": "Student created", "data": data}, status=201)
        except Exception as e:
            return JsonResponse({"error": str(e)}, status=500)
    return JsonResponse({"error": "Invalid request method"}, status=405)


def get_students(request):
    if request.method == 'GET':
        data = list(students_collection.find({}, {"_id": 0}))
        return JsonResponse(data, safe=False)
    return JsonResponse({"error": "Invalid request method"}, status=405)


def get_student(request, uid):
    if request.method == 'GET':
        student = students_collection.find_one({"uid": uid}, {"_id": 0})
        return JsonResponse(student or {"error": "Not Found"}, status=200 if student else 404)
    return JsonResponse({"error": "Invalid request method"}, status=405)


@csrf_exempt
def update_student(request, uid):
    if request.method == 'PUT':
        data = json.loads(request.body)
        result = students_collection.update_one({"uid": uid}, {"$set": data})
        return JsonResponse({"msg": "Updated"} if result.modified_count else {"error": "Not Found"}, status=200)
    return JsonResponse({"error": "Invalid request method"}, status=405)


# Marks APIs

@csrf_exempt
def create_marks(request):
    if request.method == 'POST':
        try:
            data = json.loads(request.body)
            if marks_collection.find_one({"uid": data["uid"]}):
                return JsonResponse({"error": "Marks for this UID already exist"}, status=400)

            inserted_id = marks_collection.insert_one(data).inserted_id
            return JsonResponse({
                "message": "Marks received",
                "inserted_id": str(inserted_id)
            }, status=201)
        except Exception as e:
            return JsonResponse({"error": str(e)}, status=500)
    return JsonResponse({"error": "Invalid request method"}, status=405)


def get_marks(request, uid):
    if request.method == 'GET':
        marks = marks_collection.find_one({"uid": uid}, {"_id": 0})
        return JsonResponse(marks or {"error": "Not Found"}, status=200 if marks else 404)
    return JsonResponse({"error": "Invalid request method"}, status=405)


@csrf_exempt
def update_marks(request, uid):
    if request.method == 'PUT':
        data = json.loads(request.body)
        result = marks_collection.update_one({"uid": uid}, {"$set": data})
        return JsonResponse({"msg": "Updated"} if result.modified_count else {"error": "Not Found"}, status=200)
    return JsonResponse({"error": "Invalid request method"}, status=405)


# CGPA Calculation

def calculate_cgpa(semesters):
    total_points = 0
    total_credits = 0

    for sem in semesters.values():
        if not sem:
            continue
        for subject in sem.values():
            marks = subject.get("marks", 0)
            credit = subject.get("credit", 0)

            # Convert marks to grade points
            if marks >= 90:
                grade_point = 10
            elif marks >= 80:
                grade_point = 9
            elif marks >= 70:
                grade_point = 8
            elif marks >= 60:
                grade_point = 7
            elif marks >= 50:
                grade_point = 6
            elif marks >= 40:
                grade_point = 5
            else:
                grade_point = 0

            total_points += grade_point * credit
            total_credits += credit

    if total_credits == 0:
        return 0.0

    return round(total_points / total_credits, 2)


def get_cgpa(request, uid):
    if request.method == 'GET':
        doc = marks_collection.find_one({"uid": uid}, {"_id": 0})
        if not doc:
            return JsonResponse({"error": "No marks found"}, status=404)
        cgpa = calculate_cgpa(doc.get("semester", {}))
        return JsonResponse({"uid": uid, "cgpa": cgpa})
    return JsonResponse({"error": "Invalid request method"}, status=405)
