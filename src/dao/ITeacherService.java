package dao;

import java.sql.SQLException;
import java.util.List;

import entity.Course;
import entity.Teacher;
import exception.CourseNotFoundException;
import exception.InvalidTeacherDataException;
import exception.TeacherNotFoundException;

public interface ITeacherService {
 	boolean addTeacher(Teacher teacher) throws InvalidTeacherDataException;
    boolean updateTeacher(Teacher teacher, String firstName, String lastName, String email, String expertise) throws TeacherNotFoundException;
    boolean deleteTeacher(int teacherId) throws TeacherNotFoundException;
    boolean assignCourseToTeacher(Course course, Teacher teacher) throws TeacherNotFoundException, CourseNotFoundException;
    Teacher getTeacherById(int teacherId) throws TeacherNotFoundException;
    Teacher getTeacherByEmail(String email);
    List<Teacher> getAllTeachers();
   
}
