package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Course;
import entity.Enrollment;
import entity.Teacher;
import exception.CourseNotFoundException;
import exception.InvalidCourseDataException;
import exception.TeacherNotFoundException;
import util.DBConnUtil;

public class CourseService implements ICourseService{
	
	private Connection con;
	
	public CourseService() {
		// TODO Auto-generated constructor stub
		super();
		con = DBConnUtil.getDbConnection();
	}

//	@Override
//	public void assignTeacher(Course course, Teacher teacher) {
//		// TODO Auto-generated method stub
//		try {
//			PreparedStatement pstmt = con.prepareStatement("update courses set teacher_id=? where course_id=?");
//			pstmt.setInt(1, teacher.getTeacherId());
//			pstmt.setInt(2, course.getCourseId());
//			pstmt.executeUpdate();
//		}
//		catch(SQLException e) {
//			System.out.println("Error assigning teacher :" + e.getMessage());
//		}
//	}
//
//	@Override
//	public void updateCourseInfo(Course course, String courseName, int credits) {
//		// TODO Auto-generated method stub
//		try {
//			PreparedStatement pstmt = con.prepareStatement("update courses set course_name=?, credits=? where course_id=?");
//			pstmt.setString(1, courseName);
//			pstmt.setInt(2, credits);
//			pstmt.setInt(3, course.getCourseId());
//			pstmt.executeUpdate();
//		}
//		catch(SQLException e) {
//			System.out.println("Error updating course info :" + e.getMessage());
//		}
//	}
//
//	@Override
//	public void displayCourseInfo(Course course) {
//		// TODO Auto-generated method stub
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select * from courses where course_id=?");
//			pstmt.setInt(1, course.getCourseId());
//			ResultSet rs = pstmt.executeQuery();
//			
//			if(rs.next()) {	
//				course.setCourseId(rs.getInt("course_id"));
//				course.setCourseName(rs.getString("course_name"));
//				course.setCredits(rs.getInt("credits"));
//				course.setTeacherId(rs.getInt("teacher_id"));
//	            
//	            System.out.println("Course ID: " + course.getCourseId());
//	            System.out.println("Course Name: " + course.getCourseName());
//	            System.out.println("Course Credits: " + course.getCredits());
//	            System.out.println("Teacher ID: " + course.getTeacherId());
//			}
//			else {
//				System.out.println("Course not found");
//			}
//		}
//		catch(SQLException e) {
//			System.err.println("Error displayig course info :" + e.getMessage());
//		}
//	}
//
//	@Override
//	public List<Enrollment> getEnrollments(Course course) {
//		// TODO Auto-generated method stub
//		List<Enrollment> enrollments = new ArrayList<>();
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select * from enrollments where course_id=?");
//			pstmt.setInt(1, course.getCourseId());
//			ResultSet rs = pstmt.executeQuery();
//			while(rs.next()) {
//				Enrollment enrollment = new Enrollment(rs.getInt("enrollment_id"), rs.getInt("student_id"), rs.getInt("course_id"), rs.getDate("enrollment_date").toLocalDate());
//                enrollments.add(enrollment);
//			}
//		}
//		catch(SQLException e) {
//			System.out.println("Error getting enrollments :" + e.getMessage());
//		}
//		return enrollments;
//	}
//
//	@Override
//	public Teacher getTeacher(Course course) {
//		// TODO Auto-generated method stub
//		Teacher teacher = null;
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select t.* from teachers t join courses c on t.teacher_id = c.teacher_id where c.course_id=?");
//			pstmt.setInt(1, course.getCourseId());
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()) {
//				teacher = new Teacher(rs.getInt("teacher_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
//			}
//		}
//		catch(SQLException e) {
//			System.out.println("Error in getting teacher :" + e.getMessage());
//		}
//		return teacher;
//	}

	@Override
	public boolean addCourse(Course course) throws InvalidCourseDataException{
		// TODO Auto-generated method stub
		try {
			if (course.getCourseId() <= 0 || course.getCourseCode() == null || course.getCourseName() == null)
                throw new InvalidCourseDataException("Invalid course data provided.");
			
			PreparedStatement pstmt = con.prepareStatement("insert into course (course_name, course_code, teacher_id, instructor_name) values (?, ?, ?, ?)");
			pstmt.setString(1, course.getCourseName());
            pstmt.setString(2, course.getCourseCode());
            pstmt.setInt(3, course.getTeacherId());
            pstmt.setString(4, course.getInstructorName());
            pstmt.executeUpdate();
            return true;
		} catch (SQLException e) {
            System.err.println("Error adding course: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean updateCourse(Course course, String courseName, String courseCode, int teacherId, String instructorName) throws CourseNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("update course set course_name=?, course_code=?, teacher_id=?, instructor_name=? where course_id=?");
			pstmt.setString(1, courseName);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, teacherId);
            pstmt.setString(4, instructorName);
            pstmt.setInt(5, course.getCourseId());
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new CourseNotFoundException("Course not found with ID: " + course.getCourseId());
            }
            return true;
		}catch (SQLException e) {
            System.err.println("Error updating course: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deleteCourse(int courseId) throws CourseNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("delete from course where course_id=?");
		 	pstmt.setInt(1, courseId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new CourseNotFoundException("Course not found with ID: " + courseId);
            }
            return true;
		} catch (SQLException e) {
            System.err.println("Error deleting course: " + e.getMessage());
		}
		return false;
	}

	@Override
	public Course getCourseById(int courseId) throws CourseNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from course where course_id=?");
			pstmt.setInt(1, courseId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getInt("teacher_id"),
                    rs.getString("instructor_name")
                );
            }
            else {
                throw new CourseNotFoundException("Course not found with ID: " + courseId);
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving course: " + e.getMessage());
        }
		return null;
	}

	@Override
	public Course getCourseByCode(String courseCode) throws CourseNotFoundException {
		// TODO Auto-generated method stub
		Course course = null;
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from course where course_code=?");
			pstmt.setString(1, courseCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                course = new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getString("course_code"),
                    rs.getInt("teacher_id"),
                    rs.getString("instructor_name")
                );
            }else {
            	throw new CourseNotFoundException("Course with code " + courseCode + " not found.");
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving course: " + e.getMessage());
        }
		return course;
	}

	@Override
	public List<Course> getAllCourses() {
		// TODO Auto-generated method stub
		List<Course> courses = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from course");
			ResultSet rs = pstmt.executeQuery(); 
	            while (rs.next()) {
	                courses.add(new Course(
	                    rs.getInt("course_id"),
	                    rs.getString("course_name"),
	                    rs.getString("course_code"),
	                    rs.getInt("teacher_id"),
	                    rs.getString("instructor_name")
	                ));
	            }
		}catch (SQLException e) {
            System.err.println("Error fetching courses: " + e.getMessage());
        }
		return courses;
	}

	@Override
	public boolean assignCourseToTeacher(Course course, Teacher teacher)
			throws CourseNotFoundException, TeacherNotFoundException {
		// TODO Auto-generated method stub
		try {
	        PreparedStatement teacherCheck = con.prepareStatement("select * from teacher where teacher_id = ?");
	        teacherCheck.setInt(1, teacher.getTeacherId());
	        ResultSet teacherRs = teacherCheck.executeQuery();
	        if (!teacherRs.next()) {
	            throw new TeacherNotFoundException("Teacher not found with ID: " + teacher.getTeacherId());
	        }

	        PreparedStatement courseCheck = con.prepareStatement("select * from course where course_id = ?");
	        courseCheck.setInt(1, course.getCourseId());
	        ResultSet courseRs = courseCheck.executeQuery();
	        if (!courseRs.next()) {
	            throw new CourseNotFoundException("Course not found with ID: " + course.getCourseId());
	        }

	        PreparedStatement pstmt = con.prepareStatement(
	                "update course set teacher_id = ?, instructor_name = ? where course_id = ?");
	        pstmt.setInt(1, teacher.getTeacherId());
	        pstmt.setString(2, teacher.getFirstName() + " " + teacher.getLastName());
	        pstmt.setInt(3, course.getCourseId());

	        int rowsUpdated = pstmt.executeUpdate();
	        return rowsUpdated > 0;

	    } catch (SQLException e) {
	        System.err.println("Error assigning course to teacher: " + e.getMessage());
	    }
	    return false;
	}
}
