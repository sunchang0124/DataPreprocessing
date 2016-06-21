import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Scanner;

public class PreprocessData {

	// Initialize variables to keep track of what i'm working with
	private static ArrayList<Student> studentList;
	private static ArrayList<Date> dateList;
	private static ArrayList<Exam> examsInRange;
	static String student;
	static String course;
	static String grade;
	static String date;
	static int studentIndex;
	static int courseIndex;
	// static String[] dateStudentStarted = new String[500];
	// static String[] dateStudentFinished = new String[500];
	//
	// // Initialize data matrices
	// static String[][] data = new String[500][150];
	// static int[][] dataSparsity = new int[500][150];

	// Initialize an empty students and courses arrayList
	static ArrayList<String> students = new ArrayList<String>();
	static ArrayList<String> courses = new ArrayList<String>();

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {

		studentList = new ArrayList<Student>();
		dateList = new ArrayList<Date>();

		// // Initialise the sparsity matrices to be completely zero
		// for (int i = 0; i < 500; i++) {
		// for (int j = 0; j < 150; j++) {
		// dataSparsity[i][j] = 0;
		// }
		// }

		// Path to Kurt's inputfile
		File file = new File("C:/Users/zwabb/Desktop/Data.txt");
		Scanner input = new Scanner(file);

		// INPUT
		// Loop over input file
		while (input.hasNext()) {
			// Get info out of each line
			String nextLine = input.nextLine();
			System.out.println(nextLine);
			String[] parts = nextLine.split(",");
			student = parts[0];
			course = parts[1];
			grade = parts[2];
			date = parts[3];

			// Parse the String date into Date parsedDate
			Date parsedDate = parseDate(date);

			if (!dateList.contains(parsedDate)) {
				dateList.add(parsedDate);
			}

			// Insert method to filter out courses that are NOT considered for
			// this project
			if (filter(course)) {
				continue;
			}
			// Print line to check if the input file is parsed correctly
			// System.out.println(student + "\t" + course + "\t" + grade + "\t"
			// + date);

			Exam currentExam = new Exam(course, parsedDate, grade);

			boolean hasBeenFound = false;
			Student currentStudent = new Student(student);
			for (Student stud : studentList) {
				if (stud.getID().equals(student)) {
					hasBeenFound = true;
					currentStudent = stud;
				}
			}
			if (!hasBeenFound) {
				studentList.add(currentStudent);
			}

			currentStudent.addExam(currentExam);

			// // Check whether this student has already been processed before
			// if (!students.contains(student)) {
			// // If not, add it to the student ArrayList
			// students.add(student);
			// }
			// // Get the right index for the current student
			// for (int i = 0; i < students.size(); i++) {
			// if (students.get(i).equals(student)) {
			// studentIndex = i;
			// }
			// }
			// Check whether this course has already been processed before
			if (!courses.contains(course)) {
				// If not, add course to the course ArrayList
				courses.add(course);
			}
			// // Get the right index for the current course
			// for (int i = 0; i < courses.size(); i++) {
			// if (courses.get(i).equals(course)) {
			// courseIndex = i;
			// }
			// }
			//
			// // Put the info from the file in the right place within data
			// if (dataSparsity[studentIndex][courseIndex] == 0) {
			// data[studentIndex][courseIndex] = grade;
			// dataSparsity[studentIndex][courseIndex] = 1;
			// } else {
			// // TODO: This is a RESIT
			// // Student needs to be added to the end of the list, and all
			// // courses need to be filled in again
			// // Former grade needs to be replaced with the resit grade
			// }
		}
		input.close();

		Collections.sort(dateList);
		for (Date date : dateList) {
			System.out.println(date.toString());
		}

		ArrayList<Date> cutoffPoints = new ArrayList<Date>();
		cutoffPoints.add(new Date(110, 1, 20));
		cutoffPoints.add(new Date(110, 3, 20));
		cutoffPoints.add(new Date(110, 6, 10));
		cutoffPoints.add(new Date(110, 9, 10));
		
		cutoffPoints.add(new Date(111, 1, 20));
		cutoffPoints.add(new Date(111, 3, 20));
		cutoffPoints.add(new Date(111, 6, 10));
		cutoffPoints.add(new Date(111, 9, 10));

		cutoffPoints.add(new Date(112, 1, 20));
		cutoffPoints.add(new Date(112, 3, 20));
		cutoffPoints.add(new Date(112, 6, 10));
		cutoffPoints.add(new Date(112, 9, 10));

		cutoffPoints.add(new Date(113, 1, 20));
		cutoffPoints.add(new Date(113, 3, 20));
		cutoffPoints.add(new Date(113, 6, 10));
		cutoffPoints.add(new Date(113, 9, 10));

		cutoffPoints.add(new Date(114, 1, 20));
		cutoffPoints.add(new Date(114, 3, 20));
		cutoffPoints.add(new Date(114, 6, 10));
		cutoffPoints.add(new Date(114, 9, 10));

		cutoffPoints.add(new Date(115, 1, 20));
		cutoffPoints.add(new Date(115, 3, 20));
		cutoffPoints.add(new Date(115, 6, 10));
		cutoffPoints.add(new Date(115, 9, 10));

		// OUTPUT
		// Initialise output files
		File listCourses = new File("C:/Users/zwabb/Desktop/Courses.txt");
		File studentsXcourses = new File("C:/Users/zwabb/Desktop/OutputData.txt");
		File sparsity = new File("C:/Users/zwabb/Desktop/Sparsity.txt");

		// Add filewriters for every file
		FileWriter coursefw = new FileWriter(listCourses.getAbsoluteFile());
		FileWriter studentsXcoursesfw = new FileWriter(studentsXcourses.getAbsoluteFile());
		FileWriter sparsityfw = new FileWriter(sparsity.getAbsoluteFile());

		// Add BufferedWriters for every FileWriter
		BufferedWriter cbw = new BufferedWriter(coursefw);
		BufferedWriter dbw = new BufferedWriter(studentsXcoursesfw);
		BufferedWriter sbw = new BufferedWriter(sparsityfw);

		// Write the output in the files
		for (String course : courses) {
			cbw.write(course + ";");
		}

		for (Date point : cutoffPoints) {
			for (Student student : studentList) {
				if (!student.isFinished()) {
					examsInRange = findExamsInRange(student, point);
					String[] grades = putExamsInOrder(examsInRange);
					for (String grade : grades) {
						dbw.write(grade + ";");
					}
					dbw.write("\n");
				}
				if (student.getFinalExam().before(point)) {
					// System.out.println("Final exam: " +
					// student.getFinalExam() + " Cutoff Point: " + point);
					student.setFinished();
				}
			}
			int cnt = 0;
			for (Student student : studentList) {
				if (!student.isFinished()) {
					cnt++;
				}
			}
			System.out.println(cnt);
			//dbw.write("\n\n\n\n\n");
		}
		// for (int student = 0; student < students.size(); student++) {
		// for (int course = 0; course < courses.size(); course++) {
		// dbw.write(data[student][course] + ";");
		// sbw.write(dataSparsity[student][course] + ";");
		// }
		// dbw.write("\n");
		// sbw.write("\n");
		// }

		// Close all BufferedWriters
		cbw.close();
		dbw.close();
		sbw.close();

	}

	// Method to parse a String into a type Date
	static Date parseDate(String date) {
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yy");
		// System.out.print(date + " Parses as ");
		Date parsedDate = null;
		try {
			parsedDate = ft.parse(date);
		} catch (ParseException e) {
			System.out.println("Unparseable using " + ft);
		}
		return parsedDate;
	}

	public static ArrayList<Exam> findExamsInRange(Student student, Date cutoffPoint) {
		ArrayList<Exam> examsInRange = new ArrayList<Exam>();
		// for (Student student : studentList) {

		for (Exam exam : student.getExams()) {
			if (exam.getDate().before(cutoffPoint)) {
				examsInRange.add(exam);
			}
		}
		// }

		return examsInRange;
	}

	public static String[] putExamsInOrder(ArrayList<Exam> exams) {
		String[] result = new String[courses.size()];
		int index = 0;
		for (String res : result) {
			res = "";
		}
		for (Exam exam : exams) {
			for (int i = 0; i < courses.size(); i++) {
				if (courses.get(i).equals(exam.getCourse())) {
					index = i;
				}
			}
			result[index] = exam.getGrade();
		}
		return result;
	}

	static boolean filter(String course) {
		// If course is not from DKE, but from another faculty, do not consider
		// it
		if (!course.contains("KEN")) {
			return true;
		} else {
			return false;
		}

		// Maybe more filters should be added
	}
}
