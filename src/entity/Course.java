package entity;

import java.util.ArrayList;
import java.util.List;

public class Course {
	
	private int courseId;
    private String courseName;
    private String courseCode;
    private int teacherId; 
    private String instructorName;
	private Teacher teacher;
	private List<Enrollment> enrollments;
	
	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	

	public Course(String courseName, String courseCode, int teacherId, String instructorName) {
		super();
		this.courseName = courseName;
		this.courseCode = courseCode;
		this.teacherId = teacherId;
		this.instructorName = instructorName;
	}

	public Course(int courseId, String courseName, String courseCode, int teacherId, String instructorName) {
		super();
		this.courseId = courseId;
		this.courseName = courseName;
		this.courseCode = courseCode;
		this.teacherId = teacherId;
		this.instructorName = instructorName;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getInstructorName() {
		return instructorName;
	}

	public void setInstructorName(String instructorName) {
		this.instructorName = instructorName;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	
	public void assignTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void updateCourseInfo(String courseCode, String courseName, String instructorName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.instructorName = instructorName;
    }

    public void displayCourseInfo() {
        System.out.println("Course: " + courseName + ", Code: " + courseCode + ", Instructor: " + instructorName);
    }

    public void addEnrollment(Enrollment enrollment) {
        if (!enrollments.contains(enrollment)) {
				enrollments.add(enrollment);
		  }
    }
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCourseId() {
        return courseId;
    }
	
    @Override
    public String toString() {
        return "Course [ID=" + courseId + 
               ", Name=" + courseName + 
               ", Code=" + courseCode + 
               ", Teacher ID=" + teacherId + 
               ", Instructor=" + instructorName + "]";
    }

}
