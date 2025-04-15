package entity;

import java.time.LocalDate;

public class Enrollment {
	
	private int enrollmentId;
	private int studentId;
	private int courseId;
    private Student student;
    private Course course;
    private LocalDate enrollmentDate;
    
    //private static int idCounter = 1;
    
	public Enrollment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public Enrollment(int enrollmentId, int studentId, int courseId, LocalDate enrollmentDate) {
		super();
		this.enrollmentId = enrollmentId;
		this.studentId = studentId;
		this.courseId = courseId;
		this.enrollmentDate = enrollmentDate;
	}

	public Enrollment(int enrollmentId, Student student, Course course, LocalDate enrollmentDate) {
		super();
		this.enrollmentId = enrollmentId;
		this.student = student;
		this.course = course;
		this.enrollmentDate = enrollmentDate;
	}

	public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

	public int getEnrollmentId() {
		return enrollmentId;
	}

	public void setEnrollmentId(int enrollmentId) {
		this.enrollmentId = enrollmentId;
	}

	public LocalDate getEnrollmentDate() {
		return enrollmentDate;
	}

	public void setEnrollmentDate(LocalDate enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setCourse(Course course) {
		this.course = course;
	}
	
	@Override
	public String toString() {
	    return "Enrollment [Enrollment ID=" + enrollmentId + 
	           ", Student ID=" + studentId + 
	           ", Student Name=" + (student != null ? student.getFirstName() + " " + student.getLastName() : "N/A") +
	           ", Course ID=" + courseId + 
	           ", Enrollment Date=" + enrollmentDate + "]";
	}

	
}
