package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import entity.Payment;
import entity.Student;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;
import util.DBConnUtil;

public class PaymentService implements IPaymentService{
	
	private Connection con;
	public PaymentService() {
		// TODO Auto-generated constructor stub
		super();
		con = DBConnUtil.getDbConnection();
	}
//	@Override
//	public Student getStudent(Payment payment) {
//		// TODO Auto-generated method stub
//		Student student = null;
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select * from students where student_id=?");
//			pstmt.setInt(1, payment.getStudentId());
//			ResultSet rs = pstmt.executeQuery();
//			if(rs.next()) {
//				student = new Student(rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getDate("date_of_birth").toLocalDate(), rs.getString("email"), rs.getString("phone_number"));
//			}
//		}
//		catch(SQLException e) {
//			System.out.println("Error getting student for payment:" + e.getMessage());
//		}
//		return student;
//	}
//	@Override
//	public double getPaymentAmount(Payment payment) {
//		// TODO Auto-generated method stub
//		double amount = 0.0;
//		try {
//			 PreparedStatement pstmt = con.prepareStatement("select amount from payments where payment_id = ?");
//	         pstmt.setInt(1, payment.getPaymentId());
//	         ResultSet rs = pstmt.executeQuery();
//	         if (rs.next()) {
//	             amount = rs.getDouble("amount");
//	         }
//		}
//		catch (SQLException e) {
//            System.out.println("Error getting payment amount: " + e.getMessage());
//        }
//		return amount;
//	}
//	@Override
//	public LocalDate getPaymentDate(Payment payment) {
//		// TODO Auto-generated method stub
//		LocalDate date = null;
//		try {
//			PreparedStatement pstmt = con.prepareStatement("select payment_date from payments where payment_id = ?");
//            pstmt.setInt(1, payment.getPaymentId());
//            ResultSet rs = pstmt.executeQuery();
//            if (rs.next()) {
//                date = rs.getDate("payment_date").toLocalDate();
//            }
//		}
//		catch (SQLException e) {
//            System.out.println("Error getting payment date: " + e.getMessage());
//        }
//		return date;
//	}
	@Override
	public boolean addPayment(Payment payment) throws PaymentValidationException {
		// TODO Auto-generated method stub
		try {
			if (payment.getAmount() <= 0 || payment.getPaymentDate() == null || payment.getStudent() == null) {
                throw new PaymentValidationException("Invalid payment details.");
            }
			PreparedStatement pstmt = con.prepareStatement("insert into payment (student_id, amount, payment_date) values (?, ?, ?)");
			pstmt.setInt(1, payment.getStudentId());
            pstmt.setDouble(2, payment.getAmount());
            pstmt.setDate(3, Date.valueOf(payment.getPaymentDate()));
            pstmt.executeUpdate();
            return true;
		}catch (SQLException e) {
            System.err.println("Error adding payment: " + e.getMessage());
		}
		return false;
	}
	@Override
	public boolean removePayment(int paymentId) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("delete from payment where payment_id = ?");
			pstmt.setInt(1, paymentId);
            int rows = pstmt.executeUpdate();
            return rows > 0;	
		}catch (SQLException e) {
            System.err.println("Error removing payment: " + e.getMessage());
		}
		return false;
	}
	@Override
	public boolean recordPayment(Student student, double amount, LocalDate paymentDate) throws StudentNotFoundException, PaymentValidationException{
		// TODO Auto-generated method stub
		try {
			if (amount <= 0 || paymentDate == null) {
                throw new PaymentValidationException("Invalid amount or payment date.");
            }

            // Check student existence
            PreparedStatement checkStudent = con.prepareStatement("select * from student where student_id = ?");
            checkStudent.setInt(1, student.getStudentId());
            ResultSet rs = checkStudent.executeQuery();
            if (!rs.next()) {
                throw new StudentNotFoundException("Student not found with ID: " + student.getStudentId());
            }
			PreparedStatement pstmt = con.prepareStatement("insert into payment (student_id, amount, payment_date) values (?, ?, ?)");
			pstmt.setInt(1, student.getStudentId());
            pstmt.setDouble(2, amount);
            pstmt.setDate(3, Date.valueOf(paymentDate));
            pstmt.executeUpdate();
            return true;
		}catch (SQLException e) {
            System.err.println("Error recording payment: " + e.getMessage());
		}
		return false;
	}
	@Override
	public List<Payment> getPaymentsByStudent(Student student) {
		// TODO Auto-generated method stub
		List<Payment> payments = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from payment where student_id = ?");
			pstmt.setInt(1, student.getStudentId());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("student_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date").toLocalDate()
                ));
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving payments for student: " + e.getMessage());
        }

		return payments;
	}
	@Override
	public List<Payment> getAllPayments() {
		// TODO Auto-generated method stub
		List<Payment> payments = new ArrayList<>();
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from payment");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("student_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date").toLocalDate()
                ));
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving all payments: " + e.getMessage());
        }
		return payments;
	}
	@Override
	public Payment getPaymentById(int paymentId) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement pstmt = con.prepareStatement("select * from payment where payment_id = ?");
			pstmt.setInt(1, paymentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Payment(
                        rs.getInt("payment_id"),
                        rs.getInt("student_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date").toLocalDate()
                );
            }
		}catch (SQLException e) {
            System.err.println("Error retrieving payment by ID: " + e.getMessage());
        }
		return null;
	}

}
