package edu.ncsu.csc216.course_manager.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.manager.CourseManager;
import edu.ncsu.csc216.course_manager.users.Faculty;
/**
 * Test for the FacultyRecordIO class
 * @author sarahweaver
 */
public class FacultyRecordIOTest {

	//Strings with the path to the valid/invalid test files.
	private final String validTestFile = "test-files/faculty_records.txt";
	private final String invalidTestFile = "test-files/invalid_faculty_records.txt";

	//Expected results of the toString() method for valid Faculty objects
	private String validFaculty0 = "Ashely,Witt,awitt,mollis@Fuscealiquetmagna.net,pw,2,CSC116";
	private String validFaculty1 = "Fiona,Meadows,fmeadow,pharetra.sed@et.org,pw,3,CSC216,CSC230";
	private String validFaculty2 = "Brent,Brewer,bbrewer,sem.semper@orcisem.co.uk,pw,1,CSC226";
	private String validFaculty3 = "Halla,Aguirre,haguirr,Fusce.dolor.quam@amalesuadaid.net,pw,3,CSC236";
	private String validFaculty4 = "Kevyn,Patel,kpatel,risus@pellentesque.ca,pw,1";
	private String validFaculty5 = "Elton,Briggs,ebriggs,arcu.ac@ipsumsodalespurus.edu,pw,3,CSC246";
	private String validFaculty6 = "Norman,Brady,nbrady,pede.nonummy@elitfermentum.co.uk,pw,1,CSC316";
	private String validFaculty7 = "Lacey,Walls,lwalls,nascetur.ridiculus.mus@fermentum.net,pw,2,CSC379";

	//Array to simplify testing
	private String[] validFaculty = { validFaculty0, validFaculty1, validFaculty2, validFaculty3, validFaculty4,
			validFaculty5, validFaculty6, validFaculty7 };

	//References to course objects
	private Course c1, c2, c3, c4, c5, c6, c7, c8;

	//Reference to CourseManager needed to test FacultyRecordIO.
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
		//validFaculty* Strings with the hashed password.
		try {
			String password = "pw";
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(password.getBytes());
			String hashPW = new String(digest.digest());

			for (int i = 0; i < validFaculty.length; i++) {
				validFaculty[i] = validFaculty[i].replace(",pw,", "," + hashPW + ",");
			}
		} catch (NoSuchAlgorithmException e) {
			fail("Unable to create hash during setup");
		}

		// Reset faculty_records.txt so that it's fine for other needed tests
		Path sourcePath = FileSystems.getDefault().getPath("test-files", "expected_full_faculty_records.txt");
		Path destinationPath = FileSystems.getDefault().getPath("test-files", "faculty_records.txt");
		try {
			Files.deleteIfExists(destinationPath);
			Files.copy(sourcePath, destinationPath);
		} catch (IOException e) {
			fail("Unable to reset files");
		}

	}

	/**
	 * Test reading faculty from a valid record file.
	 */
	@Test
	public void testReadFacultyRecordsValid() {
		try {
			List<Faculty> faculty = FacultyRecordIO.readFacultyRecords(validTestFile);

			assertEquals(8, faculty.size());
			for (int i = 0; i < validFaculty.length; i++) {
				assertEquals(validFaculty[i], faculty.get(i).toString());
			}
		} catch (FileNotFoundException e) {
			fail("Unexpected error reading " + validTestFile);
		}
	}

	/**
	 * Test invalid faculty record files.
	 */
	@Test
	public void testReadFacultyRecordsInvalid() {
		List<Faculty> faculty;
		try {
			faculty = FacultyRecordIO.readFacultyRecords(invalidTestFile);
			assertEquals(0, faculty.size());
		} catch (FileNotFoundException e) {
			fail("Unexpected FileNotFoundException");
		}
	}

	/**
	 * Tests writeFacultyRecords()
	 */
	@Test
	public void testWriteFacultyRecords() {
		ArrayList<Faculty> faculty = new ArrayList<Faculty>();
		//Faculty f = new Faculty("first", "last", "flast", "first_last@ncsu.edu", "pw", 18);	
		try {
			FacultyRecordIO.writeFacultyRecords("test-files/actual_faculty_records.txt", faculty);
		} catch (IOException e) {
			fail("Cannot write faculty to files");
		}
		
		//checkFiles("test-files/expected_faculty_records.txt", "test-files/actual_faculty_records.txt");
	}
		


}
