package entity;

import java.time.LocalDate;

public class Payment {
	
	private int paymentId;
	private int studentId;
	private Student student;
    private double amount;
    private LocalDate paymentDate;
    
    //private static int idCounter = 1;
    
	public Payment() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Payment(int paymentId, int studentId, double amount, LocalDate paymentDate) {
		super();
		this.paymentId = paymentId;
		this.setStudentId(studentId);
		this.amount = amount;
		this.paymentDate = paymentDate;
	}

	public Payment(int paymentId, Student student, double amount, LocalDate paymentDate) {
		super();
		this.paymentId = paymentId;
		this.student = student;
		this.amount = amount;
		this.paymentDate = paymentDate;
		
	}
	
	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Student getStudent() {
        return student;
	}

	public double getPaymentAmount() {
        return amount;
	}

	public LocalDate getPaymentDate() {
        return paymentDate;
	}


	public int getStudentId() {
		return studentId;
	}


	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}
	
	@Override
	public String toString() {
	    return "Payment [Payment ID=" + paymentId + 
	           ", Student ID=" + studentId + 
	           ", Amount=" + amount + 
	           ", Payment Date=" + paymentDate + "]";
	}

}
