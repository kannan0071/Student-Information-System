package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Student {
	
	private int studentId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    //private List<Course> enrolledCourses = new ArrayList<>();
    //private List<Payment> payments = new ArrayList<>();
    
    private List<Enrollment> enrollments;
    private List<Payment> paymentHistory;
    
	public Student() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Student(int studentId, String firstName, String lastName, LocalDate dateOfBirth, String email,
			String phoneNumber) {
		super();
		this.studentId = studentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.enrollments = new ArrayList<>();
        this.paymentHistory = new ArrayList<>();
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

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public void enrollInCourse(Course course) {
		Enrollment enrollment = new Enrollment(new Random().nextInt(1000), this, course, LocalDate.now());
	    enrollments.add(enrollment);
    }

    public void updateStudentInfo(String firstName, String lastName, LocalDate dateOfBirth, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public void makePayment(double amount, LocalDate paymentDate) {
    	Payment payment = new Payment(new Random().nextInt(10000),this, amount, paymentDate);
        paymentHistory.add(payment);
        System.out.println("Payment of Rs." + amount + " recorded.");
    }

    public void displayStudentInfo() {
        System.out.println("ID: " + studentId + ", Name: " + firstName + " " + lastName + ", DOB: " + dateOfBirth + ", Email: " + email + ", Phone: " + phoneNumber);
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Payment> getPaymentHistory() {
        return paymentHistory;
    }

    public int getStudentId() {
        return studentId;
    }
	
    @Override
    public String toString() {
        return "Student [ID=" + studentId + 
               ", First Name=" + firstName + 
               ", Last Name=" + lastName + 
               ", Email=" + email + 
               ", DOB=" + dateOfBirth + 
               ", Phone=" + phoneNumber + "]";
    }
}
