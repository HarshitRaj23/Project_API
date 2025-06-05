def calculate_point(marks):
    if marks >= 90: return 10
    if marks >= 80: return 9
    if marks >= 70: return 8
    if marks >= 60: return 7
    if marks >= 50: return 6
    if marks >= 40: return 5
    return 4

def calculate_sgpa(semester):
    total_pts = 0
    total_credits = 0
    for subj in semester.values():
        if subj:
            point = calculate_point(subj["marks"])
            total_pts += point * subj["credit"]
            total_credits += subj["credit"]
    return round((total_pts / total_credits) * 10, 2) if total_credits else 0

def calculate_cgpa(semesters):
    total_sgpa = 0
    count = 0
    for sem in semesters.values():
        sgpa = calculate_sgpa(sem)
        if sgpa:
            total_sgpa += sgpa
            count += 1
    return round(total_sgpa / count, 2) if count else 0
