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

	static String student;
	static String course;
	static String grade;
	static String date;
	static int studentIndex;
	static int courseIndex;
	static String[] dateStudentStarted = new String[500];
	static String[] dateStudentFinished = new String[500];

	static String[][][] examData = new String[500][150][2];
	static int[][] examSparsity = new int[500][150];
	static String[][][] resit1Data = new String[500][150][2];
	static int[][] resit1Sparsity = new int[500][150];
	static String[][][] resit2Data = new String[500][150][2];
	static int[][] resit2Sparsity = new int[500][150];

	static ArrayList<String> students = new ArrayList<String>();
	static ArrayList<String> courses = new ArrayList<String>();

	public static void main(String[] args) throws IOException {

		int secondresits = 0;
		
		// Initialise the sparsity matrices
		for (int i = 0; i < 500; i++) {
			for (int j = 0; j < 150; j++) {
				examSparsity[i][j] = 0;
				resit1Sparsity[i][j] = 0;
				resit2Sparsity[i][j] = 0;
			}
		}

		File file = new File("C:/Users/Mara/Desktop/Project/Anonimised DKE grade data.txt");
		Scanner input = new Scanner(file);

		// Loop over input file
		while (input.hasNext()) {
			// Get info out of each line
			String nextLine = input.nextLine();
			String[] parts = nextLine.split(",");
			student = parts[0];
			course = parts[1];
			grade = parts[2];
			date = parts[3];

			//System.out.println(student + "\t" + course + "\t" + grade + "\t" + date);

			//Parse the String date into Date parsedDate
			Date parsedDate = parseDate(date);

			// Check whether this student has already been processed before
			if (!students.contains(student)) {
				students.add(student);
			}
			for (int i = 0; i < students.size(); i++) {
				if (students.get(i).equals(student)) {
					studentIndex = i;
				}
			}
			// Check whether this course has already been processed before
			if (!courses.contains(course)) {
				courses.add(course);
			}
			for (int i = 0; i < courses.size(); i++) {
				if (courses.get(i).equals(course)) {
					courseIndex = i;
				}
			}
			

			// Assign grade entry to the correct matrix
			if (examData[studentIndex][courseIndex][0] == null) {
				examData[studentIndex][courseIndex][0] = grade;
				examData[studentIndex][courseIndex][1] = date;
				examSparsity[studentIndex][courseIndex] = 1;
			} else {
				Date parsedExamDate = parseDate(examData[studentIndex][courseIndex][1]);
				if (parsedExamDate.before(parsedDate)) {
					if (resit1Data[studentIndex][courseIndex][0] == null) {
						resit1Data[studentIndex][courseIndex][0] = grade;
						resit1Data[studentIndex][courseIndex][1] = date;
						resit1Sparsity[studentIndex][courseIndex] = 1;
					} else {
						Date parsedResit1Date = parseDate(resit1Data[studentIndex][courseIndex][1]);
						if (parsedResit1Date.before(parsedDate)) {
							if (resit2Data[studentIndex][courseIndex][0] == null) {
								resit2Data[studentIndex][courseIndex][0] = grade;
								resit2Data[studentIndex][courseIndex][1] = date;
								resit2Sparsity[studentIndex][courseIndex] = 1;
							} else {
								System.err.println("3rd RESIT FOUND, 4th MATRIX NEEDED1111");
								secondresits++;
							}
						} else if (resit2Data[studentIndex][courseIndex][0] == null) {
							resit2Data[studentIndex][courseIndex][0] = resit1Data[studentIndex][courseIndex][0];
							resit2Data[studentIndex][courseIndex][1] = resit1Data[studentIndex][courseIndex][1];
							resit2Sparsity[studentIndex][courseIndex] = 1;
							resit1Data[studentIndex][courseIndex][0] = grade;
							resit1Data[studentIndex][courseIndex][1] = date;
						} else {
							System.err.println("3rd RESIT FOUND, 4th MATRIX NEEDED2222");
							secondresits++;
						}
					}
				} else if (resit1Data[studentIndex][courseIndex][0] == null) {
					resit1Data[studentIndex][courseIndex][0] = examData[studentIndex][courseIndex][0];
					resit1Data[studentIndex][courseIndex][1] = examData[studentIndex][courseIndex][1];
					resit1Sparsity[studentIndex][courseIndex] = 1;
					examData[studentIndex][courseIndex][0] = grade;
					examData[studentIndex][courseIndex][1] = date;
				} else if (resit2Data[studentIndex][courseIndex][0] == null){
					resit2Data[studentIndex][courseIndex][0] = resit1Data[studentIndex][courseIndex][0];
					resit2Data[studentIndex][courseIndex][1] = resit1Data[studentIndex][courseIndex][1];
					resit1Data[studentIndex][courseIndex][0] = examData[studentIndex][courseIndex][0];
					resit1Data[studentIndex][courseIndex][1] = examData[studentIndex][courseIndex][1];
					examData[studentIndex][courseIndex][0] = grade;
					examData[studentIndex][courseIndex][1] = date;
				} else {
					System.err.println("3rd RESIT FOUND, 4th MATRIX NEEDED3333");
					secondresits++;
				}
			}
			
			
			/*
			if (examData[studentIndex][courseIndex] == null) {
				examData[studentIndex][courseIndex] = grade;
				examSparsity[studentIndex][courseIndex] = 1;
			} else {
				double gradeInt = Double.parseDouble(grade);
				double gradePreprocessed = Double.parseDouble(examData[studentIndex][courseIndex]);
				if (gradeInt > gradePreprocessed) {
					examData[studentIndex][courseIndex] = grade;
				}
			}*/

			// Figure out which date each student started and finished
			/*boolean yes = (dateStudentStarted[studentIndex] == null);
			System.out.println(yes);

			if (dateStudentStarted[studentIndex] == null) {
				dateStudentStarted[studentIndex] = date;
			} else {
				String date2 = dateStudentStarted[studentIndex];
				System.out.print(date + " Parses as ");
				Date parsedDate2 = null;
				try {
					parsedDate2 = ft.parse(date2);
					System.out.println(parsedDate2);
				} catch (ParseException e) {
					System.out.println("Unparseable using " + ft);
				}
				if (parsedDate2.after(parsedDate)) {
					dateStudentStarted[studentIndex] = date;
				}
			}

			if (dateStudentFinished[studentIndex] == null) {
				dateStudentFinished[studentIndex] = date;
			} else {
				String date2 = dateStudentStarted[studentIndex];
				System.out.print(date + " Parses as ");
				Date parsedDate2 = null;
				try {
					parsedDate2 = ft.parse(date2);
					System.out.println(parsedDate2);
				} catch (ParseException e) {
					System.out.println("Unparseable using " + ft);
				}
				if (parsedDate2.before(parsedDate)) {
					dateStudentStarted[studentIndex] = date;
				}
			}*/

		}
		input.close();

		File coursefile = new File("C:/Users/Mara/Desktop/Project/Courses.txt");
		File exam = new File("C:/Users/Mara/Desktop/Project/FirstExamData.txt");
		File examsparsity = new File("C:/Users/Mara/Desktop/Project/FirstExamSparsity.txt");
		File resit1 = new File("C:/Users/Mara/Desktop/Project/Resit1Data.txt");
		File resit1sparsity = new File("C:/Users/Mara/Desktop/Project/Resit1Sparsity.txt");
		File resit2 = new File("C:/Users/Mara/Desktop/Project/Resit2Data.txt");
		File resit2sparsity = new File("C:/Users/Mara/Desktop/Project/Resit2Sparsity.txt");

		FileWriter fw = new FileWriter(coursefile.getAbsoluteFile());
		FileWriter examfile = new FileWriter(exam.getAbsoluteFile());
		FileWriter examsparse = new FileWriter(examsparsity.getAbsoluteFile());
		FileWriter resit1file = new FileWriter(resit1.getAbsoluteFile());
		FileWriter resit1sparse = new FileWriter(resit1sparsity.getAbsoluteFile());
		FileWriter resit2file = new FileWriter(resit2.getAbsoluteFile());
		FileWriter resit2sparse = new FileWriter(resit2sparsity.getAbsoluteFile());
		
		BufferedWriter bw = new BufferedWriter(fw);
		BufferedWriter e = new BufferedWriter(examfile);
		BufferedWriter es = new BufferedWriter(examsparse);
		BufferedWriter r1 = new BufferedWriter(resit1file);
		BufferedWriter r1s = new BufferedWriter(resit1sparse);
		BufferedWriter r2 = new BufferedWriter(resit2file);
		BufferedWriter r2s = new BufferedWriter(resit2sparse);

		//bw.write("Student");
		//bw.write(",Date Started");
		//bw.write(",Date Finished");
		for (String course : courses) {
			bw.write(course + "; ");
			System.out.println(course);
		}
		
		
		/*
		 * static String[][][] examData = new String[500][150][2];
	static int[][] examSparsity = new int[500][150];
	static String[][][] resit1Data = new String[500][150][2];
	static int[][] resit1Sparsity = new int[500][150];
	static String[][][] resit2Data = new String[500][150][2];
	static int[][] resit2Sparsity = new int[500][150];
		 */

		for (int j = 0; j < students.size(); j++) {
			//bw.write(students.get(j));
			//bw.write("," + dateStudentStarted[j]);
			//bw.write("," + dateStudentFinished[j]);
			for (int i = 0; i < courses.size(); i++) {
				e.write(examData[j][i][0] + ";");
				es.write(examSparsity[j][i] + ";");
				r1.write(resit1Data[j][i][0] + ";");
				r1s.write(resit1Sparsity[j][i] + ";");
				r2.write(resit2Data[j][i][0] + ";");
				r2s.write(resit2Sparsity[j][i] + ";");
			}
			e.write("\n");
			es.write("\n");
			r1.write("\n");
			r1s.write("\n");
			r2.write("\n");
			r2s.write("\n");
		}
		bw.close();
		e.close();
		es.close();
		r1.close();
		r1s.close();
		r2.close();
		r2s.close();
		
		
		System.out.println("Number of third Resits: " + secondresits);
	}
	
	static Date parseDate(String date) {
		SimpleDateFormat ft = new SimpleDateFormat("dd/MM/yy");
		//System.out.print(date + " Parses as ");
		Date parsedDate = null;
		try {
			parsedDate = ft.parse(date);
		} catch (ParseException e) {
			System.out.println("Unparseable using " + ft);
		}
		return parsedDate;
	}
}
