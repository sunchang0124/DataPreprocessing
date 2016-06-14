import java.util.ArrayList;
import java.util.Date;

public class Student {

	private String ID;
	private Date finalExam;
	private boolean finished = false;
	private ArrayList<Exam> listOfExams;

	public Student(String ID) {
		this.ID = ID;
		this.listOfExams = new ArrayList<Exam>();
	}

	public void addExam(Exam exam) {
		listOfExams.add(exam);
		if ( finalExam == null || exam.getDate().after(finalExam)) {
			finalExam = exam.getDate();
		}
	}

	public String getID() {
		return ID;
	}

	public Date getFinalExam() {
		return finalExam;
	}

	public ArrayList<Exam> getExams() {
		return listOfExams;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished() {
		finished = true;
	}
}
