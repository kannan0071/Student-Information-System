package dao;

import java.time.LocalDate;
import java.util.List;

import entity.Course;
import entity.Enrollment;
import entity.Student;
import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.InvalidEnrollmentDataException;
import exception.StudentNotFoundException;

public interface IEnrollmentService {
	 
	boolean addEnrollment(Enrollment enrollment) throws InvalidEnrollmentDataException;
    boolean removeEnrollment(int enrollmentId);
    List<Enrollment> getEnrollmentsForStudent(Student student);
    List<Enrollment> getAllEnrollments();
    List<Enrollment> getEnrollmentsByCourse(String courseName);
    List<Enrollment> getEnrollmentReport(String courseName) throws CourseNotFoundException;
    Enrollment getEnrollmentById(int enrollmentId);
    boolean enrollStudentInCourse(Student student, Course course, LocalDate enrollmentDate) throws StudentNotFoundException, CourseNotFoundException, DuplicateEnrollmentException;

}	
