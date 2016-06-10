package edu.ncsu.csc216.course_manager.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.io.StudentRecordIO;
import edu.ncsu.csc216.course_manager.manager.CourseManager;
import edu.ncsu.csc216.course_manager.users.Student;

/**
 * Test the StudentRecordIO class.
 * 
 * @author SarahHeckman
 */
public class StudentRecordIOTest {

	//Strings with the path to the valid/invalid test files.
	private final String validTestFile = "test-files/student_records.txt";
	private final String invalidTestFile = "test-files/invalid_student_records.txt";

	//Expected results of the toString() method for valid Student objects
	private String validStudent0 = "Zahir,King,zking,orci.Donec@ametmassaQuisque.com,pw,15,CSC216,CSC226";
	private String validStudent1 = "Cassandra,Schwartz,cschwartz,semper@imperdietornare.co.uk,pw,4,CSC116";
	private String validStudent2 = "Shannon,Hansen,shansen,convallis.est.vitae@arcu.ca,pw,14,CSC216,CSC226,CSC379";
	private String validStudent3 = "Demetrius,Austin,daustin,Curabitur.egestas.nunc@placeratorcilacus.co.uk,pw,18,CSC116,CSC216,CSC226,CSC230,CSC236,CSC246";
	private String validStudent4 = "Raymond,Brennan,rbrennan,litora.torquent@pellentesquemassalobortis.ca,pw,12,CSC116,CSC216,CSC226,CSC230";
	private String validStudent5 = "Emerald,Frost,efrost,adipiscing@acipsumPhasellus.edu,pw,3,CSC216";
	private String validStudent6 = "Lane,Berg,lberg,sociis@non.org,pw,14";
	private String validStudent7 = "Griffith,Stone,gstone,porta@magnamalesuadavel.net,pw,17,CSC116,CSC216,CSC226,CSC230";
	private String validStudent8 = "Althea,Hicks,ahicks,Phasellus.dapibus@luctusfelis.com,pw,11,CSC116,CSC216,CSC226,CSC379";
	private String validStudent9 = "Dylan,Nolan,dnolan,placerat.Cras.dictum@dictum.net,pw,1,CSC379";

	//Array to simplify testing
	private String[] validStudents = { validStudent0, validStudent1, validStudent2, validStudent3, validStudent4,
			validStudent5, validStudent6, validStudent7, validStudent8, validStudent9 };

	//References to course objects
	private Course c1, c2, c3, c4, c5, c6, c7, c8;

	//Reference to CourseManager needed to test StudentRecordIO.
	private CourseManager manager;

	//Constant value for the hashing algorithm we are using.
	/** Hashing algorithm */
	private static final String HASH_ALGORITHM = "SHA-256";

	/**
	 * Sets up Courses used to help with testing the student
	 * file in isolation.
	 */
	@Before
	public void setUp() {
		c1 = new Course("CSC116", 3, 10);
		c2 = new Course("CSC216", 3, 10);
		c3 = new Course("CSC226", 3, 10);
		c4 = new Course("CSC230", 3, 10);
		c5 = new Course("CSC236", 3, 10);
		c6 = new Course("CSC246", 3, 10);
		c7 = new Course("CSC316", 3, 10);
		c8 = new Course("CSC379", 1, 10);

		//Create an array, which is helpful to setup.
		Course[] courses = { c1, c2, c3, c4, c5, c6, c7, c8 };

		// Set up the manager and clear out all data to start
		// test from scratch.
		manager = CourseManager.getInstance();
		manager.clearData();
		//Add all the courses to the CourseManager
		for (int i = 0; i < courses.length; i++) {
			manager.addCourse(courses[i]);
		}

		//Hash the String "pw" and replace all instances of "pw" in the 
		//validStudent* Strings with the hashed password.
		try {
			String password = "pw";
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(password.getBytes());
			String hashPW = new String(digest.digest());

			for (int i = 0; i < validStudents.length; i++) {
				validStudents[i] = validStudents[i].replace(",pw,", "," + hashPW + ",");
			}
		} catch (NoSuchAlgorithmException e) {
			fail("Unable to create hash during setup");
		}

		// Reset student_records.txt so that it's fine for other needed tests
		Path sourcePath = FileSystems.getDefault().getPath("test-files", "expected_full_student_records.txt");
		Path destinationPath = FileSystems.getDefault().getPath("test-files", "student_records.txt");
		try {
			Files.deleteIfExists(destinationPath);
			Files.copy(sourcePath, destinationPath);
		} catch (IOException e) {
			fail("Unable to reset files");
		}

	}

	/**
	 * Test reading students from a valid record file.
	 */
	@Test
	public void testReadStudentRecordsValid() {
		try {
			List<Student> students = StudentRecordIO.readStudentRecords(validTestFile);
			assertEquals(10, students.size());
			
			for (int i = 0; i < validStudents.length; i++) {
				assertEquals(validStudents[i], students.get(i).toString());
			}
		} catch (FileNotFoundException e) {
			fail("Unexpected error reading " + validTestFile);
		}
	}

	/**
	 * Test invalid student record files.
	 */
	@Test
	public void testReadStudentRecordsInvalid() {
		List<Student> students;
		try {
			students = StudentRecordIO.readStudentRecords(invalidTestFile);
			assertEquals(0, students.size());
		} catch (FileNotFoundException e) {
			fail("Unexpected FileNotFoundException");
		}
	}

	/**
	 * Tests writeStudentRecords()
	 */
	@Test
	public void testWriteStudentRecords() {
		ArrayList<Student> students = new ArrayList<Student>();
		students.add(new Student("first", "last", "flast", "first_last@ncsu.edu", "pw", 18));
		
		try {
			StudentRecordIO.writeStudentRecords("test-files/actual_student_records.txt", students);
		} catch (IOException e) {
			fail("Cannot write students to files");
		}
		
		checkFiles("test-files/expected_student_records.txt", "test-files/actual_student_records.txt");
	}
		
	/**
	 * Helper method to compare two files for the same contents
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		Scanner expScanner = null;
		Scanner actScanner = null;
		try {
			expScanner = new Scanner(new File (expFile));
			actScanner = new Scanner(new File(actFile));
			
			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}
			
			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			if (expScanner != null) expScanner.close();
			fail("Error reading files.");
		}
	}
}