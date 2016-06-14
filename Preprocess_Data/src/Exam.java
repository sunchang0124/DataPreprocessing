import java.util.Date;

public class Exam {

	private String course;
	private Date date;
	private String grade;
	
	public Exam(String course, Date date, String grade) {
		this.course = course;
		this.date = date;
		this.grade = grade;
	}
	
	public Date getDate() {
		return date;
	}
	
	public String getCourse() {
		return course;
	}
	
	public String getGrade() {
		return grade;
	}
}
