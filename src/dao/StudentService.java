package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Course;
import entity.Payment;
import entity.Student;
import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.InvalidStudentDataException;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;
import util.DBConnUtil;

public class StudentService implements IStudentService{
	
	private Connection con;
	public StudentService() {
		// TODO Auto-generated constructor stub
		super();
		con = DBConnUtil.getDbConnection();
	}

//	@Override
//	public List<Course> getEnrolledCourses(Student student) {
//		// TODO Auto-generated method stub
//		List<Course> courses = new ArrayList<>();
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select c.* from courses c join enrollments e on c.course_id = e.course_id where e.student_id=?");
//			pstmt.setInt(1, student.getStudentId());
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				Course course = new Course(rs.getInt("course_id"), rs.getString("course_name"), rs.getInt("credits"), rs.getInt("teacher_id"));
////				course.setCourseId(rs.getInt("course_id"));
////				course.setCourseName(rs.getString("course_name"));
////				course.setCredits(rs.getInt("credits"));
//				courses.add(course);
//			}
//		}
//		catch(SQLException e) {
//			System.err.println("Error in getting enrolled courses :" + e.getMessage());
//		}
//		return courses;
//	}

//	@Override
//	public List<Payment> getPaymentHistory(Student student) {
//		// TODO Auto-generated method stub
//		List<Payment> payments = new ArrayList<>();
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select * from payments where student_id = ?");
//			pstmt.setInt(1, student.getStudentId());
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				Payment payment = new Payment(rs.getInt("payment_id"), rs.getInt("student_id"), rs.getDouble("amount"), rs.getDate("payment_date").toLocalDate());
////				payment.setPaymentId(rs.getInt("payment_id"));
////				payment.setStudentId(rs.getInt("student_id"));
////				payment.setAmount(rs.getDouble("amount"));
////				payment.setPaymentDate(rs.getDate("payment_date").toLocalDate());
//				payments.add(payment);
//			}
//		}
//		catch(SQLException e) {
//			System.err.println("Error in getting payment history :" + e.getMessage());
//		}
//		return payments;
//	}

	@Override
	public boolean addStudent(Student student) throws InvalidStudentDataException{
		// TODO Auto-generated method stub
		try {
			if (student.getStudentId() <= 0 || student.getEmail() == null || student.getFirstName() == null) {
                throw new InvalidStudentDataException("Invalid student data provided.");
			}
			PreparedStatement pstmt = con.prepareStatement("insert into student (student_id, first_name, last_name, date_of_birth, email, phone_number) values (?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, student.getStudentId());
			pstmt.setString(2, student.getFirstName());
			pstmt.setString(3, student.getLastName());
			pstmt.setDate(4, Date.valueOf(student.getDateOfBirth()));
			pstmt.setString(5, student.getEmail());
			pstmt.setString(6, student.getPhoneNumber());
			
			int rows = pstmt.executeUpdate();
			return rows > 0;
		} catch (SQLException e) {
			System.err.println("Error adding student: " + e.getMessage());	
		}
		return false;
	}
	
	@Override
	public boolean updateStudentInfo(Student student, String firstName, String lastName, LocalDate dob, String email,
			String phoneNumber) throws StudentNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("update student set first_name=?, last_name=?, date_of_birth=?, email=?, phone_number=? where student_id=?");
			pstmt.setString(1, firstName);
			pstmt.setString(2, lastName);
			pstmt.setDate(3, Date.valueOf(dob));
			pstmt.setString(4, email);
			pstmt.setString(5, phoneNumber);
			pstmt.setInt(6, student.getStudentId());
			int rows = pstmt.executeUpdate();
			if (rows == 0) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }
			return true;
		}
		catch(SQLException e) {
			System.err.println("Error updating student :" + e.getMessage());
		}
		return false;
	}
	
	@Override
	public boolean deleteStudent(int studentId) throws StudentNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("delete from student where student_id = ?");
			pstmt.setInt(1, studentId);
			int rows = pstmt.executeUpdate();
			if (rows == 0) {
                throw new StudentNotFoundException("Student not found with ID: " + studentId);
            }
			return true;
		}catch (SQLException e) {
			System.err.println("Error deleting student: " + e.getMessage());
		}
		return false;
	}

	@Override
	public Student getStudentById(int studentId) throws StudentNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from student where student_id = ?");
			pstmt.setInt(1, studentId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return new Student(
					rs.getInt("student_id"),
					rs.getString("first_name"),
					rs.getString("last_name"),
					rs.getDate("date_of_birth").toLocalDate(),
					rs.getString("email"),
					rs.getString("phone_number")
				);
			}
			else {
                throw new StudentNotFoundException("Student not found with ID: " + studentId);
            }
		}catch (SQLException e) {
			System.err.println("Error retrieving student: " + e.getMessage());
		}
		return null;
	}

	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		List<Student> students = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from student");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				students.add(new Student(
					rs.getInt("student_id"),
					rs.getString("first_name"),
					rs.getString("last_name"),
					rs.getDate("date_of_birth").toLocalDate(),
					rs.getString("email"),
					rs.getString("phone_number")
				));
			}
		} catch (SQLException e) {
			System.err.println("Error retrieving all students: " + e.getMessage());
		}
		return students;
	}
	@Override
	public void enrollInCourse(Student student, Course course) throws DuplicateEnrollmentException, StudentNotFoundException, CourseNotFoundException{
		// TODO Auto-generated method stub
		try {
			// Check if student exists
            PreparedStatement studentCheck = con.prepareStatement("select * from student where student_id = ?");
            studentCheck.setInt(1, student.getStudentId());
            if (!studentCheck.executeQuery().next())
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());

            // Check if course exists
            PreparedStatement courseCheck = con.prepareStatement("select * from course where course_id = ?");
            courseCheck.setInt(1, course.getCourseId());
            if (!courseCheck.executeQuery().next())
                throw new CourseNotFoundException("Course not found with ID: " + course.getCourseId());

            // Check if already enrolled
            PreparedStatement enrollCheck = con.prepareStatement("select * from enrollment where student_id = ? and course_id = ?");
            enrollCheck.setInt(1, student.getStudentId());
            enrollCheck.setInt(2, course.getCourseId());
            if (enrollCheck.executeQuery().next())
                throw new DuplicateEnrollmentException("Student is already enrolled in this course.");
            
			PreparedStatement pstmt = con.prepareStatement("insert into enrollment(student_id, course_id, enrollment_date) values (?, ?, ?, ?)");
			//pstmt.setInt(1, enrollmentId);
			pstmt.setInt(2, student.getStudentId());
			pstmt.setInt(3, course.getCourseId());
			pstmt.setDate(4, Date.valueOf(LocalDate.now()));
			pstmt.executeUpdate();
			System.out.println("Student enrolled in course successfully.");
		}
		catch(SQLException e) {
			System.err.println("Error enrolling student :" + e.getMessage());
		}
	}
	@Override
	public void makePayment(Student student, double amount, LocalDate paymentDate) throws StudentNotFoundException, PaymentValidationException{
		// TODO Auto-generated method stub
		try {
			if (amount <= 0 || paymentDate == null) throw new PaymentValidationException("Invalid payment amount.");
            
            PreparedStatement studentCheck = con.prepareStatement("select * from student where student_id = ?");
            studentCheck.setInt(1, student.getStudentId());
            if (!studentCheck.executeQuery().next())
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            
			PreparedStatement pstmt = con.prepareStatement("insert into payments(student_id, amount, payment_date) values (?, ?, ?)");
			pstmt.setInt(1, student.getStudentId());
			pstmt.setDouble(2, amount);
			pstmt.setDate(3, Date.valueOf(paymentDate));
			pstmt.executeUpdate();
			System.out.println("Payment done successfully.");
		}
		catch(SQLException e) {
			System.err.println("Error in making payment :" + e.getMessage());
		}
	}
	@Override
	public void displayStudentInfo(Student student) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from students where student_id=?");
			pstmt.setInt(1, student.getStudentId());
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				student.setFirstName(rs.getString("first_name"));
	            student.setLastName(rs.getString("last_name"));
	            student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
	            student.setEmail(rs.getString("email"));
	            student.setPhoneNumber(rs.getString("phone_number"));
	            
	            System.out.println("Student ID: " + student.getStudentId());
	            System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
	            System.out.println("DOB: " + student.getDateOfBirth());
	            System.out.println("Email: " + student.getEmail());
	            System.out.println("Phone: " + student.getPhoneNumber());
			}
			else {
				System.out.println("Student not found");
			}
		}
		catch(SQLException e) {
			System.err.println("Error displayig student info :" + e.getMessage());
		}
		
	}

	@Override
	public boolean enrollStudentWithPayment(Student student, Course course, double amount, LocalDate paymentDate)
			throws SQLException, StudentNotFoundException, CourseNotFoundException, PaymentValidationException {
		// TODO Auto-generated method stub
		boolean flag = false;

	    try {
	        con.setAutoCommit(false); // Begin transaction

	        // Enroll in course
	        PreparedStatement enrollStmt = con.prepareStatement(
	            "insert into enrollment (student_id, course_id, enrollment_date) values(?, ?, ?)");
	        enrollStmt.setInt(1, student.getStudentId());
	        enrollStmt.setInt(2, course.getCourseId());
	        enrollStmt.setDate(3, Date.valueOf(LocalDate.now()));
	        enrollStmt.executeUpdate();

	        //Record payment
	        if (amount <= 0) {
	            throw new PaymentValidationException("Invalid payment amount.");
	        }
	        PreparedStatement paymentStmt = con.prepareStatement(
	            "insert into payment (student_id, amount, payment_date) values (?, ?, ?)");
	        paymentStmt.setInt(1, student.getStudentId());
	        paymentStmt.setDouble(2, amount);
	        paymentStmt.setDate(3, Date.valueOf(paymentDate));
	        paymentStmt.executeUpdate();

	        con.commit(); // Commit if all succeeded
	        flag = true;
	        System.out.println("Student enrolled and payment recorded successfully.");
	    } 
	    catch (SQLException | PaymentValidationException e) {
	        con.rollback(); // Rollback if anything fails
	        System.err.println("Transaction failed: " + e.getMessage());
	    } 
	    finally {
	        con.setAutoCommit(true); // Restore auto-commit mode
	    }

	    return flag;
	}
	//Dynamic Query Builder
	public List<Student> queryStudents(List<String> columns, String condition, String orderBy) {
	    List<Student> students = new ArrayList<>();
	    String columnPart = columns.isEmpty() ? "*" : String.join(", ", columns);
	    String sql = "select " + columnPart + " from student";

	    if (condition != null && !condition.isEmpty()) {
	        sql += " where " + condition;
	    }
	    if (orderBy != null && !orderBy.isEmpty()) {
	        sql += " order by " + orderBy;
	    }

	    try (PreparedStatement pstmt = con.prepareStatement(sql)) {
	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            Student s = new Student();
	            s.setStudentId(rs.getInt("student_id"));
	            if (columns.contains("first_name")) s.setFirstName(rs.getString("first_name"));
	            if (columns.contains("last_name")) s.setLastName(rs.getString("last_name"));
	            if (columns.contains("email")) s.setEmail(rs.getString("email"));
	            if (columns.contains("dob")) s.setDateOfBirth(rs.getDate("dob").toLocalDate());
	            if (columns.contains("phone_number")) s.setPhoneNumber(rs.getString("phone_number"));
	            students.add(s);
	        }
	    } catch (SQLException e) {
	        System.err.println("Error executing dynamic query: " + e.getMessage());
	    }

	    return students;
	}
	
}
