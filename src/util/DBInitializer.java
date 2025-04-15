package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBInitializer {

	public static void initialize() {
		try (Connection con = DBConnUtil.getDbConnection(); Statement stmt = con.createStatement()) {

            // Student table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Student (
                    student_id INT PRIMARY KEY,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    date_of_birth DATE NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    phone_number VARCHAR(15) UNIQUE NOT NULL
                );
            """);
            
            // Teacher table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Teacher (
                    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
                    first_name VARCHAR(50) NOT NULL,
                    last_name VARCHAR(50) NOT NULL,
                    email VARCHAR(100) UNIQUE NOT NULL,
                    expertise VARCHAR(50) NOT NULL
                );
            """);
            
            // Course table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Course (
                    course_id INT PRIMARY KEY AUTO_INCREMENT,
                    course_name VARCHAR(100),
                    course_code VARCHAR(20),
                    teacher_id INT,
                    instructor_name VARCHAR(50),
                    FOREIGN KEY(teacher_id) REFERENCES Teacher(teacher_id) ON DELETE SET NULL
                );
            """);

            // Enrollment table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Enrollment (
                    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
                    student_id INT,
                    course_id INT,
                    enrollment_date DATE NOT NULL,
                    FOREIGN KEY(student_id) REFERENCES Student(student_id) ON DELETE CASCADE,
                    FOREIGN KEY(course_id) REFERENCES Course(course_id)ON DELETE CASCADE
                );
            """);

            // Payment table
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Payment (
                    payment_id INT PRIMARY KEY AUTO_INCREMENT,
                    student_id INT,
                    amount DOUBLE NOT NULL,
                    payment_date DATE NOT NULL,
                    FOREIGN KEY(student_id) REFERENCES Student(student_id) ON DELETE CASCADE
                );
            """);

            System.out.println("Database tables created successfully (if not already present).");

        } catch (SQLException e) {
            System.err.println("Error during table creation.");
            e.printStackTrace();
        }
	}
}
