import java.util.ArrayList;
import java.util.Date;
import java.text.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class PreprocessData {

	// Initialize variables to keep track of what i'm working with
	static String student;
	static String course;
	static String grade;
	static String date;
	static int studentIndex;
	static int courseIndex;
	static String[] dateStudentStarted = new String[500];
	static String[] dateStudentFinished = new String[500];

	// Initialize data matrices
	static String[][] data = new String[500][150];
	static int[][] dataSparsity = new int[500][150];

	// Initialize an empty students and courses arrayList
	static ArrayList<String> students = new ArrayList<String>();
	static ArrayList<String> courses = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		// Initialise the sparsity matrices to be completely zero
		for (int i = 0; i < 500; i++) {
			for (int j = 0; j < 150; j++) {
				dataSparsity[i][j] = 0;
			}
		}

		// Path to Kurt's inputfile
		File file = new File("C:/Users/Mara/Desktop/Project/Anonimised DKE grade data.txt");
		Scanner input = new Scanner(file);

		// INPUT
		// Loop over input file
		while (input.hasNext()) {
			// Get info out of each line
			String nextLine = input.nextLine();
			String[] parts = nextLine.split(",");
			student = parts[0];
			course = parts[1];
			grade = parts[2];
			date = parts[3];

			// Insert method to filter out courses that are NOT considered for this project
			if (filter(course)) {
				break;
			}
			// Print line to check if the input file is parsed correctly
			// System.out.println(student + "\t" + course + "\t" + grade + "\t"
			// + date);

			// Parse the String date into Date parsedDate
			Date parsedDate = parseDate(date);

			// Check whether this student has already been processed before
			if (!students.contains(student)) {
				// If not, add it to the student ArrayList
				students.add(student);
			}
			// Get the right index for the current student
			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).equals(student)) {
					studentIndex = i;
				}
			}
			// Check whether this course has already been processed before
			if (!courses.contains(course)) {
				// If not, add course to the course ArrayList
				courses.add(course);
			}
			// Get the right index for the current course
			for (int i = 0; i < courses.size(); i++) {
				if (courses.get(i).equals(course)) {
					courseIndex = i;
				}
			}

			// Put the info from the file in the right place within data
			if (dataSparsity[studentIndex][courseIndex] == 0) {
				data[studentIndex][courseIndex] = grade;
				dataSparsity[studentIndex][courseIndex] = 1;
			} else {
				// Student needs to be added to the end of the list, and all courses need to be filled in again
				// Except resits, they need to be edited
			}
		}
		input.close();

		// OUTPUT
		// Initialise output files
		File listCourses = new File("C:/Users/Mara/Desktop/Project/Courses.txt");
		File studentsXcourses = new File("C:/Mara/Desktop/Project/Data.txt");
		File sparsity = new File("C:/Mara/Desktop/Project/Sparsity.txt");

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

		for (int student = 0; student < students.size(); student++) {
			for (int course = 0; course < courses.size(); course++) {
				dbw.write(data[student][course] + ";");
				sbw.write(dataSparsity[student][course] + ";");
			}
			dbw.write("\n");
			sbw.write("\n");
		}

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
	
	static boolean filter(String course) {
		// If course is not from DKE, but from another faculty, do not consider it
		if (!course.contains("KEN")) {
			return true;
		} else {
			return false;
		}
		
		// Maybe more filters should be added
	}
}
