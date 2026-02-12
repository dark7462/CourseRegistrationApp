package com.dark.service;

import com.dark.entity.*;
import com.dark.util.JPAUtil;
import jakarta.persistence.*;

import java.util.List;

public class StudentService {
    public String registerStudentForCourse(String rollNumber, String courseId) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        String result = "";

        try {
            tx.begin();

            // 1. Find the Student and the Course
            Student student = em.find(Student.class, rollNumber);
            Course course = em.find(Course.class, courseId);

            if (student == null) return "Student not found.";
            if (course == null) return "Course not found.";

            // 2. Check if already registered
            if (student.getRegisteredCourses().contains(course)) {
                result = "Already registered for " + course.getCourseName();
            } else {
                // 3. Add course to student's list
                student.getRegisteredCourses().add(course);

                // 4. Update the student (Cascade will handle the join table)
                em.merge(student);
                result = "Successfully registered for " + course.getCourseName();
            }

            tx.commit();
        } catch (Exception e) {
            if(tx.isActive()) tx.rollback();
            System.out.println("Error in registerStudentForCourse : " + e.getMessage() );
            result = "Error during registration.";
        } finally {
            em.close();
        }
        return result;
    }
    public List<Course> getAllCourses() {
        try (EntityManager em = JPAUtil.getEntityManager()) {
            return em.createQuery("SELECT c FROM Course c", Course.class).getResultList();
        }
    }
}
