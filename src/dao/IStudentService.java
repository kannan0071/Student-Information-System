package dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import entity.Course;
import entity.Payment;
import entity.Student;
import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.InvalidStudentDataException;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;

public interface IStudentService {
	
	boolean addStudent(Student student) throws InvalidStudentDataException;
    boolean updateStudentInfo(Student student, String firstName, String lastName, LocalDate dob, String email, String phoneNumber) throws StudentNotFoundException;
    boolean deleteStudent(int studentId) throws StudentNotFoundException;
    Student getStudentById(int studentId) throws StudentNotFoundException;
    List<Student> getAllStudents();
	void enrollInCourse(Student student, Course course) throws DuplicateEnrollmentException, StudentNotFoundException, CourseNotFoundException;
    void makePayment(Student student, double amount, LocalDate paymentDate) throws StudentNotFoundException, PaymentValidationException;
    void displayStudentInfo(Student student);
    boolean enrollStudentWithPayment(Student student, Course course, double amount, LocalDate paymentDate)throws SQLException, StudentNotFoundException, CourseNotFoundException, PaymentValidationException;
    List<Student> queryStudents(List<String> columns, String condition, String orderBy);

}
