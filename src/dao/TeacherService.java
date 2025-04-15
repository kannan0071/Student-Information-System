package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Course;
import entity.Teacher;
import exception.CourseNotFoundException;
import exception.InvalidTeacherDataException;
import exception.TeacherNotFoundException;
import util.DBConnUtil;

public class TeacherService implements ITeacherService{
	private Connection con;
	
	public TeacherService() {
		// TODO Auto-generated constructor stub
		super();
		con = DBConnUtil.getDbConnection();
	}

	@Override
	public boolean addTeacher(Teacher teacher) throws InvalidTeacherDataException{
		// TODO Auto-generated method stub
		try {
			if (teacher.getTeacherId() <= 0 || teacher.getEmail() == null || teacher.getFirstName() == null)
                throw new InvalidTeacherDataException("Invalid teacher data provided.");
			
			PreparedStatement pstmt = con.prepareStatement("insert into teacher (first_name, last_name, email, expertise) VALUES (?, ?, ?, ?)");
	 		pstmt.setString(1, teacher.getFirstName());
            pstmt.setString(2, teacher.getLastName());
            pstmt.setString(3, teacher.getEmail());
            pstmt.setString(4, teacher.getExpertise());
            pstmt.executeUpdate();
            return true;
		}catch (SQLException e) {
            System.err.println("Error adding teacher: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean updateTeacher(Teacher teacher, String firstName, String lastName, String email, String expertise) throws TeacherNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("update teacher set first_name=?, last_name=?, email=?, expertise=? where teacher_id=?");
			pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, expertise);
            pstmt.setInt(5, teacher.getTeacherId());
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new TeacherNotFoundException("Teacher not found with ID: " + teacher.getTeacherId());
            }
            return true;
		}catch (SQLException e) {
            System.err.println("Error updating teacher: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("delete from teacher where teacher_id=?");
			pstmt.setInt(1, teacherId);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new TeacherNotFoundException("Teacher not found with ID: " + teacherId);
            }
            return true;	
		}catch (SQLException e) {
            System.err.println("Error deleting teacher: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean assignCourseToTeacher(Course course, Teacher teacher) throws TeacherNotFoundException, CourseNotFoundException{
		// TODO Auto-generated method stub
		try {
			// Check if teacher exists
            PreparedStatement teacherCheck = con.prepareStatement("select * from teacher where teacher_id = ?");
            teacherCheck.setInt(1, teacher.getTeacherId());
            if (!teacherCheck.executeQuery().next())
                throw new TeacherNotFoundException("Teacher not found with ID: " + teacher.getTeacherId());

            // Check if course exists
            PreparedStatement courseCheck = con.prepareStatement("select * from course where course_id = ?");
            courseCheck.setInt(1, course.getCourseId());
            if (!courseCheck.executeQuery().next())
                throw new CourseNotFoundException("Course not found with ID: " + course.getCourseId());
            
			PreparedStatement pstmt = con.prepareStatement("update course set teacher_id=?, instructor_name=? where course_id=?");
			pstmt.setInt(1, teacher.getTeacherId());
            pstmt.setString(2, teacher.getFirstName() + " " + teacher.getLastName());
            pstmt.setInt(3, course.getCourseId());
            int rows = pstmt.executeUpdate();
            return rows > 0;
		}catch (SQLException e) {
            System.err.println("Error assigning course to teacher: " + e.getMessage());
		}
		return false;
	}

	@Override
	public Teacher getTeacherById(int teacherId)  throws TeacherNotFoundException{
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from teacher where teacher_id=?");
			pstmt.setInt(1, teacherId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Teacher(
                    rs.getInt("teacher_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("expertise")
                );
            }
            else {
                throw new TeacherNotFoundException("Teacher not found with ID: " + teacherId);
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving teacher: " + e.getMessage());
        }
		return null;
	}

	@Override
	public List<Teacher> getAllTeachers() {
		// TODO Auto-generated method stub
		List<Teacher> teacher = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from teacher");
			ResultSet rs = pstmt.executeQuery(); 
	        while (rs.next()) {
                teacher.add(new Teacher(
                    rs.getInt("teacher_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("expertise")
                ));
            }
		}catch (SQLException e) {
            System.err.println("Error fetching teachers: " + e.getMessage());
        }
		return teacher;
	}

	@Override
	public Teacher getTeacherByEmail(String email) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from teacher where email = ?");
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            Teacher t = new Teacher();
	            t.setTeacherId(rs.getInt("teacher_id"));
	            t.setFirstName(rs.getString("first_name"));
	            t.setLastName(rs.getString("last_name"));
	            t.setEmail(rs.getString("email"));
	            t.setExpertise(rs.getString("expertise"));
	            return t;
	        }
		}catch(SQLException e) {
			System.err.println("Error fetching teacher by email: " + e.getMessage());
		}
		return null;
	}
}
