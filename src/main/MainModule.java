package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import dao.CourseService;
import dao.EnrollmentService;
import dao.PaymentService;
import dao.StudentService;
import dao.TeacherService;
import entity.Course;
import entity.Enrollment;
import entity.Payment;
import entity.SIS;
import entity.Student;
import entity.Teacher;
import exception.CourseNotFoundException;
import exception.DuplicateEnrollmentException;
import exception.InsufficientFundsException;
import exception.InvalidCourseDataException;
import exception.InvalidEnrollmentDataException;
import exception.InvalidStudentDataException;
import exception.InvalidTeacherDataException;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;
import exception.TeacherNotFoundException;
import util.DBConnUtil;
import util.DBInitializer;
import util.DBPropertyUtil;

public class MainModule {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection con = DBConnUtil.getDbConnection();
		if(con != null) {
			System.out.println(con);
			System.out.println("Connected!");
		}
		else {
			System.out.println("failed to connect");
		}
//		DBInitializer.initialize();//to create tables if it does not exist
		
        Scanner sc = new Scanner(System.in);

        StudentService studentService = new StudentService();
        TeacherService teacherService = new TeacherService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService();
        PaymentService paymentService = new PaymentService();
        
        while (true) {
            System.out.println("\n--- Student Information System ---");
            System.out.println("1. Add New Student");
            System.out.println("2. Update Student Info");
            System.out.println("3. Delete Student");
            System.out.println("4. Enroll Student in Course");
            System.out.println("5. Add New Course");
            System.out.println("6. Add New Teacher");
            System.out.println("7. Assign Course to Teacher");
            System.out.println("8. Record Student Payment");
            System.out.println("9. Display Student Details");
            System.out.println("10. Display Teacher Details");
            System.out.println("11. Display Course Details");
            System.out.println("12. Get Enrollments by Student");
            System.out.println("13. Get Payments by Student");
            System.out.println("14. Get Enrollments by Course Name");
            System.out.println("15. Generate Enrollment Report for Course");
            System.out.println("16. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> {
                    try {
                        sc.nextLine();
                        System.out.print("Student ID: ");
                        int studentId = sc.nextInt();
                        sc.nextLine();
                        System.out.print("First Name: ");
                        String firstName = sc.nextLine();
                        System.out.print("Last Name: ");
                        String lastName = sc.nextLine();
                        System.out.print("Date of Birth (yyyy-mm-dd): ");
                        LocalDate dob = LocalDate.parse(sc.nextLine());
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Phone: ");
                        String phone = sc.nextLine();

                        Student student = new Student(studentId, firstName, lastName, dob, email, phone);
                        studentService.addStudent(student);
                        System.out.println("Student added successfully.");
                    } catch (InvalidStudentDataException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                
                case 2 -> {
                    try {
                        System.out.print("Enter Student ID to update: ");
                        int sid = sc.nextInt();
                        sc.nextLine(); 
                        Student student = studentService.getStudentById(sid);

                        System.out.print("Enter new First Name: ");
                        String firstName = sc.nextLine();
                        System.out.print("Enter new Last Name: ");
                        String lastName = sc.nextLine();
                        System.out.print("Enter new Date of Birth (yyyy-mm-dd): ");
                        LocalDate dob = LocalDate.parse(sc.nextLine());
                        System.out.print("Enter new Email: ");
                        String email = sc.nextLine();
                        System.out.print("Enter new Phone: ");
                        String phone = sc.nextLine();

                        boolean updated = studentService.updateStudentInfo(student, firstName, lastName, dob, email, phone);
                        if (updated) {
                            System.out.println("Student information updated successfully.");
                        } else {
                            System.out.println("Failed to update student information.");
                        }

                    } catch (StudentNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                
                case 3 -> {
                    try {
                        System.out.print("Enter Student ID to delete: ");
                        int sid = sc.nextInt();
                        if (studentService.deleteStudent(sid)) {
                            System.out.println("Student deleted successfully.");
                        } else {
                            System.out.println("Student not found or could not be deleted.");
                        }
                    } catch (StudentNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 4 -> {
                    try {
                        System.out.print("Enter Student ID: ");
                        int sid = sc.nextInt();
                        Student student = studentService.getStudentById(sid);

                        System.out.print("Enter Course ID: ");
                        int cid = sc.nextInt();
                        Course course = courseService.getCourseById(cid);

                        if (enrollmentService.enrollStudentInCourse(student, course, LocalDate.now())) {
                            System.out.println("Student enrolled successfully.");
                        }
                    } catch (StudentNotFoundException | CourseNotFoundException | DuplicateEnrollmentException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                
                case 5 -> {
                    try {
                        sc.nextLine();
                        System.out.print("Course Name: ");
                        String code = sc.nextLine();
                        System.out.print("Course Code: ");
                        String name = sc.nextLine();
                        System.out.print("Teacher ID: ");
                        int id = sc.nextInt();
                        System.out.print("Instructor Name: ");
                        String i_name = sc.nextLine();

                        Course course = new Course(name, code, id, i_name);
                        if (courseService.addCourse(course)) {
                            System.out.println("Course added successfully.");
                        } else {
                            System.out.println("Failed to add course.");
                        }
                    } catch (InvalidCourseDataException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }
                
                case 6 -> {
                    try {
                        sc.nextLine();
                        System.out.print("First Name: ");
                        String firstName = sc.nextLine();
                        System.out.print("Last Name: ");
                        String lastName = sc.nextLine();
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        System.out.print("Expertise: ");
                        String expertise = sc.nextLine();

                        Teacher teacher = new Teacher(firstName, lastName, email, expertise);
                        if (teacherService.addTeacher(teacher)) {
                            System.out.println("Teacher added successfully.");
                        } else {
                            System.out.println("Failed to add teacher.");
                        }
                    } catch (InvalidTeacherDataException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 7 -> {
                    try {
                        System.out.print("Enter Teacher ID: ");
                        int tid = sc.nextInt();
                        Teacher teacher = teacherService.getTeacherById(tid);

                        System.out.print("Enter Course ID: ");
                        int cid = sc.nextInt();
                        Course course = courseService.getCourseById(cid);

                        if (teacherService.assignCourseToTeacher(course, teacher)) {
                            System.out.println("Course assigned to teacher successfully.");
                        }
                    } catch (TeacherNotFoundException | CourseNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 8 -> {
                    try {
                        System.out.print("Enter Student ID: ");
                        int sid = sc.nextInt();
                        Student student = studentService.getStudentById(sid);

                        System.out.print("Enter Payment Amount: ");
                        double amount = sc.nextDouble();
                        sc.nextLine();

                        System.out.print("Enter Payment Date (yyyy-mm-dd): ");
                        LocalDate date = LocalDate.parse(sc.nextLine());

                        if (paymentService.recordPayment(student, amount, date)) {
                            System.out.println("Payment recorded successfully.");
                        }
                    } catch (StudentNotFoundException | PaymentValidationException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 9 -> {
                    try {
                        System.out.print("Enter Student ID: ");
                        int sid = sc.nextInt();
                        Student student = studentService.getStudentById(sid);
                        System.out.println("Student Info: " + student);
                    } catch (StudentNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 10 -> {
                    try {
                        System.out.print("Enter Teacher ID: ");
                        int tid = sc.nextInt();
                        Teacher teacher = teacherService.getTeacherById(tid);
                        System.out.println("Teacher Info: " + teacher);
                    } catch (TeacherNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 11 -> {
                	try {
                        sc.nextLine();
                        System.out.print("Enter Course code: ");
                        String courseCode = sc.nextLine();
                        Course course = courseService.getCourseByCode(courseCode);
                        System.out.println("Course Info: " + course);
                    } catch (CourseNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 12 -> {
                    try {
                        System.out.print("Enter Student ID: ");
                        int sid = sc.nextInt();
                        Student student = studentService.getStudentById(sid);
                        List<Enrollment> enrollments = enrollmentService.getEnrollmentsForStudent(student);
                        for (Enrollment e : enrollments) {
                            System.out.println(e);
                        }
                    } catch (StudentNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 13 -> {
                    try {
                        System.out.print("Enter Student ID: ");
                        int sid = sc.nextInt();
                        Student student = studentService.getStudentById(sid);
                        List<Payment> payments = paymentService.getPaymentsByStudent(student);
                        for (Payment p : payments) {
                            System.out.println(p);
                        }
                    } catch (StudentNotFoundException e) {
                        System.err.println("Error: " + e.getMessage());
                    }
                }

                case 14 -> {
                    sc.nextLine();
                    System.out.print("Enter Course Name: ");
                    String courseName = sc.nextLine();
                    List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourse(courseName);
                    for (Enrollment e : enrollments) {
                        System.out.println(e);
                    }
                }
                
                case 15 -> {
                	System.out.print("Enter Course Name for Enrollment Report: ");
                	sc.nextLine();
                    String courseNameForReport = sc.nextLine();

                    try {
                        List<Enrollment> reportEnrollments = enrollmentService.getEnrollmentReport(courseNameForReport);

                        if (reportEnrollments.isEmpty()) {
                            System.out.println("No enrollments found for the course: " + courseNameForReport);
                        } else {
                            System.out.println("--- Enrollment Report for Course: " + courseNameForReport + " ---");
                            for (Enrollment e : reportEnrollments) {
                                System.out.println(e);
                            }
                            System.out.print("Do you want to save the report to a file? (yes/no): ");
                            String saveToFile = sc.nextLine();
                            if (saveToFile.equalsIgnoreCase("yes")) {
                                try (PrintWriter writer = new PrintWriter("Enrollment_Report_" + courseNameForReport.replace(" ", "_") + ".txt")) {
                                    for (Enrollment e : reportEnrollments) {
                                        writer.println(e);
                                    }
                                    System.out.println("Report saved successfully.");
                                } catch (IOException ex) {
                                    System.err.println("Error saving report: " + ex.getMessage());
                                }
                            }
                        }
                    } catch (CourseNotFoundException ex) {
                        System.err.println("Course not found: " + ex.getMessage());
                    }
                }
                case 16 -> {
                    System.out.println("Exiting from Student Information System...");
                    sc.close();
                    System.exit(0);
                }

                default -> System.out.println("Invalid choice. Please select again.");
            }
        }
	}
}

////Enroll Student + Record Payment as One Transaction
//Student student = new Student(100, "Test", "Student", LocalDate.of(2000, 1, 1), "test@student.com", "9876543210");
//Course course = new Course(100, "Java Fundamentals", "CS101", 15, "John Doe");
//try {
//  studentService.addStudent(student);
//} catch (Exception e) {
//  System.out.println("Student may already exist: " + e.getMessage());
//}
//
//try {
//  courseService.addCourse(course);
//} catch (Exception e) {
//  System.out.println("Course may already exist: " + e.getMessage());
//}
//System.out.println("------ Testing Transaction: Enroll Student + Record Payment ------");
//try {
//  boolean success = studentService.enrollStudentWithPayment(student, course, 5000.0, LocalDate.now());
//  System.out.println("Transaction status: " + (success ? "Success" : "Failed"));
//
//} catch (Exception e) {
//  System.err.println("Error during enrollment with payment: " + e.getMessage());
//}
//
////Dynamic Query Builder for Student Search
//System.out.println("\n------ Testing Dynamic Query Builder for Students ------");
//List<String> columns = List.of("student_id", "first_name", "email"); 
//String condition = "student_id > 0"; // condition
//String orderBy = "first_name"; //  order
//
//List<Student> students = studentService.queryStudents(columns, condition, orderBy);
//for (Student s : students) {
//  System.out.println(s);
//}
