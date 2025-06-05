from django.urls import path
from . import views

urlpatterns = [
    path('students/create/', views.create_student),
    path('students/', views.get_students),
    path('students/<str:uid>/', views.get_student),
    path('students/<str:uid>/update/', views.update_student),

    path('marks/create/', views.create_marks),
    path('marks/<str:uid>/', views.get_marks),
    path('marks/<str:uid>/update/', views.update_marks),

    path('cgpa/<str:uid>/', views.get_cgpa),
]
