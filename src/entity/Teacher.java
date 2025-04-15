package entity;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
	
	private int teacherId;
    private String firstName;
    private String lastName;
    private String email;
    private String expertise;
    
    private List<Course> assignedCourses;
    
	public Teacher() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Teacher(int teacherId, String firstName, String lastName, String email, String expertise) {
		super();
		this.teacherId = teacherId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.expertise = expertise;
	}

	public Teacher(String firstName, String lastName, String email, String expertise) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.expertise = expertise;
	}

	public Teacher(int teacherId, String firstName, String lastName, String email) {
		super();
		this.teacherId = teacherId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.assignedCourses = new ArrayList<>();
	}
	
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getExpertise() {
		return expertise;
	}

	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

	public void updateTeacherInfo(String name, String email, String expertise) {
        this.firstName = name;
        this.email = email;
        this.expertise = expertise;
	}
	
	public void displayTeacherInfo() {
	    System.out.println("ID: " + teacherId + "Teacher: " + firstName + " " + lastName + ", Email: " + email + ", Expertise: " + expertise);
	}
	
	public void addAssignedCourse(Course course) {
	    assignedCourses.add(course);
	}
	
	public List<Course> getAssignedCourses() {
	    return assignedCourses;
	}
	
	public int getTeacherId() {
	    return teacherId;
	}
	
	@Override
	public String toString() {
	    return "Teacher [ID=" + teacherId + 
	           ", First Name=" + firstName + 
	           ", Last Name=" + lastName + 
	           ", Email=" + email + 
	           ", Expertise=" + expertise + "]";
	}

	
	
}
