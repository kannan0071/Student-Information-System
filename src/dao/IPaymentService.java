package dao;

import java.time.LocalDate;
import java.util.List;

import entity.Payment;
import entity.Student;
import exception.PaymentValidationException;
import exception.StudentNotFoundException;

public interface IPaymentService {
	
	boolean addPayment(Payment payment) throws PaymentValidationException; //same as recordPayment
    boolean removePayment(int paymentId);
    boolean recordPayment(Student student, double amount, LocalDate paymentDate) throws StudentNotFoundException, PaymentValidationException; // Task 10
    List<Payment> getPaymentsByStudent(Student student);
    List<Payment> getAllPayments();
    Payment getPaymentById(int paymentId);
}
