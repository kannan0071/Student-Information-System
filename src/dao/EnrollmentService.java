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
import entity.Enrollment;
import entity.Student;
import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.InvalidEnrollmentDataException;
import exception.StudentNotFoundException;
import util.DBConnUtil;

public class EnrollmentService implements IEnrollmentService{
	
	private Connection con;
	
	public EnrollmentService() {
		// TODO Auto-generated constructor stub
		super();
		con = DBConnUtil.getDbConnection();
	}

//	@Override
//	public Student getStudent(Enrollment enrollment) {
//		// TODO Auto-generated method stub
//		Student student = null;
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select * from students where student_id=?");
//			pstmt.setInt(1, enrollment.getStudentId());
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()) {
//				student = new Student(rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("date_of_birth").toLocalDate(), rs.getString("email"), rs.getString("phone_number"));
//			}
//		}
//		catch(SQLException e) {
//			System.out.println("Error getting student for enrollment:" + e.getMessage());
//		}
//		return student;
//	}
//
//	@Override
//	public Course getCourse(Enrollment enrollment) {
//		// TODO Auto-generated method stub
//		Course course = null;
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select * from courses where course_id=?");
//			pstmt.setInt(1, enrollment.getCourseId());
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()) {
//				course = new Course(rs.getInt("course_id"), rs.getString("course_name"), rs.getInt("credits"), rs.getInt("teacher_id"));
//			}
//		}
//		catch(SQLException e) {
//			System.out.println("Error getting course :" + e.getMessage());
//		}
//		return course;
//	}

	@Override
	public boolean addEnrollment(Enrollment enrollment) throws InvalidEnrollmentDataException{
		// TODO Auto-generated method stub
		try {
			if (enrollment.getStudent() == null || enrollment.getCourse() == null) {
                throw new InvalidEnrollmentDataException("Student or Course cannot be null.");
            }
			PreparedStatement pstmt = con.prepareStatement("insert into enrollment (student_id, course_id, enrollment_date) values (?, ?, ?)");
		 	pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setDate(3, Date.valueOf(enrollment.getEnrollmentDate()));
            pstmt.executeUpdate();
            return true;
		}catch (SQLException e) {
            System.err.println("Error adding enrollment: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean removeEnrollment(int enrollmentId) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("delete from enrollment where enrollment_id = ?");
			pstmt.setInt(1, enrollmentId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
		}catch (SQLException e) {
            System.err.println("Error deleting enrollment: " + e.getMessage());
		}
		return false;
	}

	@Override
	public List<Enrollment> getEnrollmentsForStudent(Student student) {
		// TODO Auto-generated method stub
		List<Enrollment> list = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from enrollment where student_id = ?");
			pstmt.setInt(1, student.getStudentId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                rs.getDate("enrollment_date").toLocalDate()
                ));
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving enrollments for student: " + e.getMessage());
		}
		return list;
	}

	@Override
	public List<Enrollment> getAllEnrollments() {
		// TODO Auto-generated method stub
		List<Enrollment> enrollments = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from enrollment");
			ResultSet rs = pstmt.executeQuery();
        	while (rs.next()) {
        		enrollments.add(new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                rs.getDate("enrollment_date").toLocalDate()
				));
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving all enrollments: " + e.getMessage());
        }

		return enrollments;
	}

	@Override
	public List<Enrollment> getEnrollmentsByCourse(String courseName) {
		// TODO Auto-generated method stub
		List<Enrollment> list = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select e.* from enrollment e join course c on e.course_id = c.course_id where c.course_name = ?");
			pstmt.setString(1, courseName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Enrollment(
                rs.getInt("enrollment_id"),
                rs.getInt("student_id"),
                rs.getInt("course_id"),
                rs.getDate("enrollment_date").toLocalDate()
                ));
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving enrollments by course name: " + e.getMessage());
        }
		return list;
	}
	
	@Override
	public Enrollment getEnrollmentById(int enrollmentId) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from enrollment where enrollment_id = ?");
			pstmt.setInt(1, enrollmentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Enrollment(
                        rs.getInt("enrollment_id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getDate("enrollment_date").toLocalDate()
                );
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving enrollment by ID: " + e.getMessage());
        }

		return null;
	}

	@Override
	public boolean enrollStudentInCourse(Student student, Course course, LocalDate enrollmentDate) throws StudentNotFoundException, CourseNotFoundException, DuplicateEnrollmentException{
		// TODO Auto-generated method stub
		try {
			// Validate student existence
            PreparedStatement checkStudent = con.prepareStatement("select * from student where student_id = ?");
            checkStudent.setInt(1, student.getStudentId());
            ResultSet rs1 = checkStudent.executeQuery();
            if (!rs1.next()) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }

            // Validate course existence
            PreparedStatement checkCourse = con.prepareStatement("select * from course where course_id = ?");
            checkCourse.setInt(1, course.getCourseId());
            ResultSet rs2 = checkCourse.executeQuery();
            if (!rs2.next()) {
                throw new CourseNotFoundException("Course not found with ID: " + course.getCourseId());
            }

            // Check if already enrolled
            PreparedStatement checkDuplicate = con.prepareStatement(
                    "select * from enrollment where student_id = ? and course_id = ?");
            checkDuplicate.setInt(1, student.getStudentId());
            checkDuplicate.setInt(2, course.getCourseId());
            ResultSet rs3 = checkDuplicate.executeQuery();
            if (rs3.next()) {
                throw new DuplicateEnrollmentException("Student already enrolled in this course.");
            }
            
			PreparedStatement pstmt = con.prepareStatement("insert into enrollment (student_id, course_id, enrollment_date) values (?, ?, ?)");
			pstmt.setInt(1, student.getStudentId());
            pstmt.setInt(2, course.getCourseId());
            pstmt.setDate(3, Date.valueOf(enrollmentDate));
            pstmt.executeUpdate();
            return true;
		}catch (SQLException e) {
            System.err.println("Error enrolling student in course: " + e.getMessage());
		}
		return false;
	}

	@Override
	public List<Enrollment> getEnrollmentReport(String courseName) throws CourseNotFoundException{
		// TODO Auto-generated method stub
		List<Enrollment> enrollments = new ArrayList<>();
		try {
        	PreparedStatement courseStmt = con.prepareStatement(
                "select * from course where lower(course_name) = lower(?)"
            );
            courseStmt.setString(1, courseName.trim());
            ResultSet courseRs = courseStmt.executeQuery();

            if (!courseRs.next()) {
                throw new CourseNotFoundException("Course not found with name: " + courseName);
            }
            int courseId = courseRs.getInt("course_id");
            PreparedStatement stmt = con.prepareStatement(
                "select e.*, s.first_name, s.last_name, s.email, s.date_of_birth, s.phone_number from enrollment e " +
                "join student s on e.student_id = s.student_id " +
                "where e.course_id = ?"
            );
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Enrollment e = new Enrollment();
                e.setEnrollmentId(rs.getInt("enrollment_id"));
                e.setStudentId(rs.getInt("student_id"));
                e.setCourseId(rs.getInt("course_id"));
                e.setEnrollmentDate(rs.getDate("enrollment_date").toLocalDate());

                // Populate Student
                Student student = new Student();
                student.setStudentId(rs.getInt("student_id"));
                student.setFirstName(rs.getString("first_name"));
                student.setLastName(rs.getString("last_name"));
                student.setEmail(rs.getString("email"));
                student.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
                student.setPhoneNumber(rs.getString("phone_number"));

                e.setStudent(student);
                enrollments.add(e);
            }
		}catch (SQLException e) {
	        System.err.println("Error fetching enrollments for course: " + e.getMessage());
	    }
		return enrollments;
	}
}
