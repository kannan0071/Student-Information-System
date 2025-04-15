package entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.InvalidCourseDataException;
import exception.InvalidEnrollmentDataException;
import exception.InvalidStudentDataException;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;
import exception.TeacherNotFoundException;

public class SIS {
	
	private List<Student> students;
    private List<Course> courses;
    private List<Teacher> teachers;
    private List<Enrollment> enrollments;
    private List<Payment> payments;
    
	public SIS() {
		// TODO Auto-generated constructor stub
		super();
		students = new ArrayList<>();
        courses = new ArrayList<>();
        teachers = new ArrayList<>();
        enrollments = new ArrayList<>();
        payments = new ArrayList<>();
	}
	
	public void enrollStudentInCourse(Student student, Course course) throws DuplicateEnrollmentException{
		for (Enrollment e : enrollments) {
	        if (e.getStudent().equals(student) && e.getCourse().equals(course)) {
	            throw new DuplicateEnrollmentException("Student is already enrolled in this course.");
	        }
	    }
	    student.enrollInCourse(course);
	    Enrollment enrollment = new Enrollment(new Random().nextInt(1000), student, course, LocalDate.now());
	    course.addEnrollment(enrollment);
	    enrollments.add(enrollment); 
	}

    public void assignTeacherToCourse(Teacher teacher, Course course) throws TeacherNotFoundException, CourseNotFoundException{
    	if (!teachers.contains(teacher)) {
            throw new TeacherNotFoundException("Teacher not found in the system.");
        }
    	if (!courses.contains(course)) {
            throw new CourseNotFoundException("Course not found in the system.");
        }
    	course.assignTeacher(teacher);
        teacher.addAssignedCourse(course);
        System.out.println("Assigned " + teacher.getFirstName()+" "+teacher.getLastName() + " to course: " + course.getCourseName());
    }

    public void recordPayment(Student student, double amount, LocalDate paymentDate) throws StudentNotFoundException, PaymentValidationException{
    	if (!students.contains(student)) {
            throw new StudentNotFoundException("Student not found in the system.");
        }
    	if (amount <= 0 || paymentDate.isAfter(LocalDate.now())) {
            throw new PaymentValidationException("Payment amount must be positive.");
        }
        student.makePayment(amount, paymentDate);
        System.out.println("Payment of Rs." + amount + " recorded for student: " + student.getFirstName()+" "+ student.getLastName());
    }

    public void generateEnrollmentReport(Course course) throws InvalidEnrollmentDataException{
        System.out.println("Enrollment Report for course: " + course.getCourseName());
        if (course == null) {
        	throw new InvalidEnrollmentDataException("Course data is missing for enrollment.");
        }
        List<Enrollment> enrollments = course.getEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No students enrolled yet.");
        } 
        else {
            for (Enrollment e : enrollments) {
                e.getStudent().displayStudentInfo();;
                //System.out.println("- " + student.getFirstName() + " " + student.getLastName());
            }
        }
    }

    public void generatePaymentReport(Student student) {
        System.out.println("Payment Report for student: " + student.getFirstName());
        List<Payment> payments = student.getPaymentHistory();
        if (payments.isEmpty()) {
            System.out.println("No payments recorded.");
        } 
        else {
            for (Payment p : payments) {
                System.out.println("Paid Rs." + p.getPaymentAmount() + " on " + p.getPaymentDate());
            }
        }
    }

    public void calculateCourseStatistics(Course course)throws InvalidCourseDataException{
        List<Enrollment> enrollments = course.getEnrollments();
        if (course.getCourseCode() == null || course.getCourseCode().isEmpty()) {
            throw new InvalidCourseDataException("Course code is invalid.");
        }
        int totalEnrolled = enrollments.size();
        double totalPayments = 0;

        for (Enrollment e : enrollments) {
            for (Payment p : e.getStudent().getPaymentHistory()) {
                totalPayments += p.getPaymentAmount();
            }
        }

        System.out.println("Course Statistics for: " + course.getCourseName());
        System.out.println("Total Enrollments: " + totalEnrolled);
        System.out.println("Total Payments by Enrolled Students: Rs." + totalPayments);
    }
    
    public void addEnrollment(Student student, Course course, LocalDate enrollmentDate) throws DuplicateEnrollmentException, StudentNotFoundException, CourseNotFoundException{
    	if (!students.contains(student)) {
            throw new StudentNotFoundException("Student not found: " + student.getStudentId());
        }

        if (!courses.contains(course)) {
            throw new CourseNotFoundException("Course not found: " + course.getCourseId());
        }

        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student) && e.getCourse().equals(course)) {
                throw new DuplicateEnrollmentException("Student already enrolled in this course.");
            }
        }

    	Enrollment enrollment = new Enrollment(new Random().nextInt(1000), student, course, enrollmentDate);
        student.getEnrollments().add(enrollment);
        course.getEnrollments().add(enrollment);
        enrollments.add(enrollment);
        System.out.println("Enrollment added successfully.");
    }
	
    public void assignCourseToTeacher(Course course, Teacher teacher) throws TeacherNotFoundException, CourseNotFoundException{
    	if (!teachers.contains(teacher)) {
            throw new TeacherNotFoundException("Teacher not found.");
        }

        if (!courses.contains(course)) {
            throw new CourseNotFoundException("Course not found.");
        }
        course.assignTeacher(teacher);
        teacher.addAssignedCourse(course);
        System.out.println("Course " + course.getCourseName() + " assigned to " + teacher.getFirstName() + " " + teacher.getLastName());
    }
    
    public void addPayment(Student student, double amount, LocalDate paymentDate) throws StudentNotFoundException, PaymentValidationException{
    	if (!students.contains(student)) {
            throw new StudentNotFoundException("Student not found.");
        }
        if (amount <= 0) {
            throw new PaymentValidationException("Invalid payment amount.");
        }
        Payment payment = new Payment(new Random().nextInt(1000), student, amount, paymentDate);
        student.getPaymentHistory().add(payment);
        payments.add(payment);
        System.out.println("Payment of Rs." + amount + " added for " + student.getFirstName());
    }
    
    public List<Enrollment> getEnrollmentsForStudent(Student student) {
        return student.getEnrollments();
    }
    
    public List<Course> getCoursesForTeacher(Teacher teacher) {
        return teacher.getAssignedCourses();
    }
    public void registerStudent(Student student) throws InvalidStudentDataException{
    	if (student.getEmail() == null || !student.getEmail().contains("@")) {
            throw new InvalidStudentDataException("Invalid email for student.");
        }

        if (student.getDateOfBirth() == null || student.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new InvalidStudentDataException("Invalid date of birth.");
        }
        students.add(student);
    }

    public void registerCourse(Course course) throws InvalidCourseDataException{
    	if (course.getCourseName() == null || course.getCourseName().isEmpty()) {
            throw new InvalidCourseDataException("Course name is required.");
        }
        courses.add(course);
    }

    public void registerTeacher(Teacher teacher) {
        teachers.add(teacher);
    }
}
