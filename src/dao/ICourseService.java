package dao;

import java.util.List;

import entity.Course;
import entity.Enrollment;
import entity.Teacher;
import exception.CourseNotFoundException;
import exception.InvalidCourseDataException;
import exception.TeacherNotFoundException;

public interface ICourseService {
	
	boolean addCourse(Course course) throws InvalidCourseDataException;
    boolean updateCourse(Course course, String courseName, String courseCode, int teacherId, String instructorName) throws CourseNotFoundException;
    boolean deleteCourse(int courseId) throws CourseNotFoundException;
    boolean assignCourseToTeacher(Course course, Teacher teacher) throws CourseNotFoundException, TeacherNotFoundException;
    Course getCourseById(int courseId) throws CourseNotFoundException;
    Course getCourseByCode(String courseCode) throws CourseNotFoundException; // Task 9 & 11
    List<Course> getAllCourses();
}
