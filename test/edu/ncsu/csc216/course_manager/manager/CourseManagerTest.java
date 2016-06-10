package edu.ncsu.csc216.course_manager.manager;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.manager.CourseManager;
import edu.ncsu.csc216.course_manager.users.Faculty;
import edu.ncsu.csc216.course_manager.users.Student;

/**
 * Tests the CourseManager class.  Because CourseManager
 * is a Singleton, the clearData() method must be called to 
 * ensure that each test starts with an empty CourseManager 
 * and that each test runs independently.
 * @author SarahHeckman
 */
public class CourseManagerTest {
	
	private final String validCourse1 = "CSC116,3,7";
	private final String validCourse2 = "CSC216,3,10";
	private final String validCourse3 = "CSC226,3,10";
	private final String validCourse4 = "CSC230,3,4";
	private final String validCourse5 = "CSC236,3,10";
	private final String validCourse6 = "CSC246,3,1";
	private final String validCourse7 = "CSC316,3,1";
	private final String validCourse8 = "CSC379,1,10";
	
	private final String [] validCourses = {validCourse1, validCourse2, validCourse3, 
			validCourse4, validCourse5, validCourse6, validCourse7, validCourse8};
	
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
	
	private String [] validStudents = {validStudent0, validStudent1, validStudent2, validStudent3, validStudent4, validStudent5,
			validStudent6, validStudent7, validStudent8, validStudent9};
	
	private String validFaculty0 = "Ashely,Witt,awitt,mollis@Fuscealiquetmagna.net,pw,2,CSC116";
	private String validFaculty1 = "Fiona,Meadows,fmeadow,pharetra.sed@et.org,pw,3,CSC216,CSC230";
	private String validFaculty2 = "Brent,Brewer,bbrewer,sem.semper@orcisem.co.uk,pw,1,CSC226";
	private String validFaculty3 = "Halla,Aguirre,haguirr,Fusce.dolor.quam@amalesuadaid.net,pw,3,CSC236";
	private String validFaculty4 = "Kevyn,Patel,kpatel,risus@pellentesque.ca,pw,1";
	private String validFaculty5 = "Elton,Briggs,ebriggs,arcu.ac@ipsumsodalespurus.edu,pw,3,CSC246";
	private String validFaculty6 = "Norman,Brady,nbrady,pede.nonummy@elitfermentum.co.uk,pw,1,CSC316";
	private String validFaculty7 = "Lacey,Walls,lwalls,nascetur.ridiculus.mus@fermentum.net,pw,2,CSC379";
	
	private String [] validFaculty = {validFaculty0, validFaculty1, validFaculty2, validFaculty3, validFaculty4, validFaculty5, validFaculty6, validFaculty7};
	
	private CourseManager manager;
	
	/** Hashing algorithm */
	private static final String HASH_ALGORITHM = "SHA-256";

	/**
	 * Sets up the CourseManager and clears the data.
	 * @throws Exception if error
	 */
	@Before
	public void setUp() throws Exception {
		manager = CourseManager.getInstance();
		manager.clearData();
		
		try {
			String password = "pw";
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(password.getBytes());
			String hashPW = new String(digest.digest());
			
			for (int i = 0; i < validStudents.length; i++) {
				validStudents[i] = validStudents[i].replace(",pw,", "," + hashPW + ",");
			}
			
			for (int i = 0; i < validFaculty.length; i++) {
				validFaculty[i] = validFaculty[i].replace(",pw,", "," + hashPW + ",");
			}
		} catch (NoSuchAlgorithmException e) {
			fail("Unable to create hash during setup");
		}
		
		//Reset student_records.txt so that it's fine for other needed tests
		Path studentSourcePath = FileSystems.getDefault().getPath("test-files", "expected_full_student_records.txt");
		Path studentDestinationPath = FileSystems.getDefault().getPath("test-files", "student_records.txt");
		resetFiles(studentSourcePath, studentDestinationPath);
		
		//Reset course_records.txt so that it's fine for other needed tests
		Path courseSourcePath = FileSystems.getDefault().getPath("test-files", "expected_full_course_records.txt");
		Path courseDestinationPath = FileSystems.getDefault().getPath("test-files", "course_records.txt");
		resetFiles(courseSourcePath, courseDestinationPath);
	}
	
	/**
	 * Helper method for resetting files.
	 * @param sourcePath path to source file - the correct copy
	 * @param destinationPath path to destination file - the one to overwrite for future tests
	 */
	public void resetFiles(Path sourcePath, Path destinationPath) {
		try {
			Files.deleteIfExists(destinationPath);
			Files.copy(sourcePath, destinationPath);
		} catch (IOException e) {
			fail("Unable to reset files");
		}
	}
	
	/**
	 * Tests the construction/reset of the CouresManager singleton.
	 */
	@Test
	public void testCourseManager() {
		assertNull(manager.getCourseByName("CSC116"));
		assertNull(manager.getCurrentUser());
		assertFalse(manager.login("first_last", "pw"));
		assertEquals(0, manager.listAllCourses().length);
	}

	/**
	 * Tests CourseManager.loadCourses().
	 */
	@Test
	public void testLoadCourses() {
		manager.loadCourses("test-files/course_records.txt");
		Course [] courses = manager.listAllCourses();
		assertEquals(8, courses.length);
		
		for (int i = 0; i < courses.length; i++) {
			assertEquals(validCourses[i], courses[i].toString());
		}
		
		manager.loadCourses("test-files/invalid_course_records.txt");
		courses = manager.listAllCourses();
		assertEquals(8, courses.length);
		
		for (int i = 0; i < courses.length; i++) {
			assertEquals(validCourses[i], courses[i].toString());
		}
	}

	/**
	 * Tests CourseManager.addCourse()
	 */
	@Test
	public void testAddCourse() {
		manager.addCourse(new Course("CSC116", 3, 7));
		Course [] courses = manager.listAllCourses();
		assertEquals(1, courses.length);
		
		for (int i = 0; i < courses.length; i++) {
			assertEquals(validCourses[i], courses[i].toString());
		}
		
		//Attempt to add the same course again
		manager.addCourse(new Course("CSC116", 3, 3));
		assertEquals(1, courses.length);
		
		for (int i = 0; i < courses.length; i++) {
			assertEquals(validCourses[i], courses[i].toString());
		}
	}

	/**
	 * Tests CourseManager.getCourseByName().
	 */
	@Test
	public void testGetCourseByName() {
		assertNull(manager.getCourseByName("CSC116"));
		
		Course c1 = new Course("CSC116", 3, 3);
		Course c2 = new Course("CSC216", 3, 3);
		Course c3 = new Course("CSC316", 3, 3);
		Course c4 = new Course("CSC326", 3, 3);
		
		manager.addCourse(c1);
		manager.addCourse(c2);
		manager.addCourse(c3);
		manager.addCourse(c4);
		
		assertEquals(c1, manager.getCourseByName("CSC116"));
		assertEquals(c2, manager.getCourseByName("CSC216"));
		assertEquals(c3, manager.getCourseByName("CSC316"));
		assertEquals(c4, manager.getCourseByName("CSC326"));
		assertNull(manager.getCourseByName("CSC226"));
	}

	/**
	 * Tests CourseManager.loadStudents().
	 */
	@Test
	public void testLoadStudents() {
		Course c1 = new Course("CSC116", 3, 10);
		Course c2 = new Course("CSC216", 3, 10);
		Course c3 = new Course("CSC226", 3, 10);
		Course c4 = new Course("CSC230", 3, 10);
		Course c5 = new Course("CSC236", 3, 10);
		Course c6 = new Course("CSC246", 3, 10);
		Course c7 = new Course("CSC316", 3, 10);
		Course c8 = new Course("CSC379", 1, 10);
		
		Course [] courses = {c1, c2, c3, c4, c5, c6, c7, c8};
		
		for (int i = 0; i < courses.length; i++) {
			manager.addCourse(courses[i]);
		}
				
		manager.loadStudents("test-files/student_records.txt");
		
		//Check that correct students are enrolled in CSC116
		Student [] students = c1.getEnrolledStudents();
		assertEquals(5, students.length);
		assertEquals(validStudents[1], students[0].toString());
		assertEquals(validStudents[3], students[1].toString());
		assertEquals(validStudents[4], students[2].toString());
		assertEquals(validStudents[7], students[3].toString());
		assertEquals(validStudents[8], students[4].toString());
		
		//Check that correct students are enrolled in CSC216
		students = c2.getEnrolledStudents();
		assertEquals(7, students.length);
		assertEquals(validStudents[0], students[0].toString());
		assertEquals(validStudents[2], students[1].toString());
		assertEquals(validStudents[3], students[2].toString());
		assertEquals(validStudents[4], students[3].toString());
		assertEquals(validStudents[5], students[4].toString());
		assertEquals(validStudents[7], students[5].toString());
		assertEquals(validStudents[8], students[6].toString());
		
		//Check that correct students are enrolled in CSC226
		students = c3.getEnrolledStudents();
		assertEquals(6, students.length);
		assertEquals(validStudents[0], students[0].toString());
		assertEquals(validStudents[2], students[1].toString());
		assertEquals(validStudents[3], students[2].toString());
		assertEquals(validStudents[4], students[3].toString());
		assertEquals(validStudents[7], students[4].toString());
		assertEquals(validStudents[8], students[5].toString());
		
		//Check that correct students are enrolled in CSC230
		students = c4.getEnrolledStudents();
		assertEquals(3, students.length);
		assertEquals(validStudents[3], students[0].toString());
		assertEquals(validStudents[4], students[1].toString());
		assertEquals(validStudents[7], students[2].toString());
		
		//Check that correct students are enrolled in CSC236
		students = c5.getEnrolledStudents();
		assertEquals(1, students.length);
		assertEquals(validStudents[3], students[0].toString());
		
		//Check that correct students are enrolled in CSC236
		students = c6.getEnrolledStudents();
		assertEquals(1, students.length);
		assertEquals(validStudents[3], students[0].toString());
		
		//Check that correct students are enrolled in CSC236
		students = c7.getEnrolledStudents();
		assertEquals(0, students.length);
		
		//Check that correct students are enrolled in CSC379
		students = c8.getEnrolledStudents();
		assertEquals(3, students.length);
		assertEquals(validStudents[2], students[0].toString());
		assertEquals(validStudents[8], students[1].toString());
		assertEquals(validStudents[9], students[2].toString());
	}
	
	/**
	 * Tests CourseManager.loadFaculty().
	 */
	@Test
	public void testLoadFaculty() {
		Course c1 = new Course("CSC116", 3, 10);
		Course c2 = new Course("CSC216", 3, 10);
		Course c3 = new Course("CSC226", 3, 10);
		Course c4 = new Course("CSC230", 3, 10);
		Course c5 = new Course("CSC236", 3, 10);
		Course c6 = new Course("CSC246", 3, 10);
		Course c7 = new Course("CSC316", 3, 10);
		Course c8 = new Course("CSC379", 1, 10);
		
		Course [] courses = {c1, c2, c3, c4, c5, c6, c7, c8};
		
		for (int i = 0; i < courses.length; i++) {
			manager.addCourse(courses[i]);
		}
				
		manager.loadFaculty("test-files/faculty_records.txt");
		
		//Check that correct faculty is listed as professor for CSC116.
			Faculty faculty = c1.getFaculty();
			assertEquals(validFaculty[0], faculty.toString());
			
		//Check that correct faculty is listed as professor for CSC216.
			Faculty faculty1 = c2.getFaculty();
			assertEquals(validFaculty[1], faculty1.toString());
			
		//check that correct faculty is listed as professor for CSC226.
			Faculty facutly2 = c3.getFaculty();
			assertEquals(validFaculty[2], facutly2.toString());
			
		//Check that correct faculty is listed for CSC230.
			Faculty faculty3 = c4.getFaculty();
			assertEquals(validFaculty[1], faculty3.toString());
	
		//Check that correct faculty is listed for CSC236.
			Faculty faculty4 = c5.getFaculty();
			assertEquals(validFaculty[3], faculty4.toString());	
	}

	/**
	 * Tests CourseManager.saveCourses().  Test is similar to CourseRecordsIOTest,
	 * but writes the courses stored in the CourseManager's list.
	 */
	@Test
	public void testSaveCourses() {
		//Create new empty file - we'll write the new courses to this file
		Path sourcePath = FileSystems.getDefault().getPath("test-files", "actual_course_records.txt");
		try {
			Files.deleteIfExists(sourcePath);
			Files.createFile(sourcePath);
		} catch (IOException e1) {
			fail("Test cannot run if empty file isn't created");
		}
		
		//Read the empty file, which sets up the filename for writing
		manager.loadCourses("test-files/actual_course_records.txt");
		
		manager.addCourse(new Course("CSC116", 3, 10));
		manager.addCourse(new Course("CSC216", 3, 10));
		manager.addCourse(new Course("CSC230", 3, 10));
		
		try {
			manager.saveCourses();
		} catch (IllegalArgumentException e) {
			fail("Cannot write to course records file");
		}
		
		checkFiles("test-files/expected_course_records.txt", "test-files/actual_course_records.txt");
	}

	/**
	 * Tests CourseManager.saveStudent().  Test is similar to StudentRecordsIOTest,
	 * but writes the students stored in the CourseManager's list.
	 */
	@Test
	public void testSaveStudents() {
		manager.loadCourses("test-files/course_records.txt");
		manager.loadStudents("test-files/student_records.txt");
		
		try {
			manager.saveStudents();
		} catch (IllegalArgumentException e) {
			fail("Cannot write students to files");
		}
		
		//If you mess up the student_records.txt file, you can copy expected_full_student_records.txt'ss
		//content into student_records.txt to fix.
		checkFiles("test-files/expected_full_student_records.txt", "test-files/student_records.txt");
	}
	
	/**
	 * Tests CourseManager.saveFaculty(). 
	 */
	@Test
	public void testSaveFaculty() {
		manager.loadCourses("test-files/course_records.txt");
		manager.loadFaculty("test-files/faculty_records.txt");
		
		
		try {
			manager.saveFaculty();
		} catch (IllegalArgumentException e) {
			fail("Cannot write faculty to files");
		}
		
		//If you mess up the student_records.txt file, you can copy expected_full_student_records.txt'ss
		//content into student_records.txt to fix.
		checkFiles("test-files/expected_full_faculty_records.txt", "test-files/faculty_records.txt");
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
			actScanner = new Scanner(new File (actFile));
			
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

	/**
	 * Tests the login method.
	 */
	@Test
	public void testLogin() {
		//Set up by adding courses/students
		manager.loadCourses("test-files/course_records.txt");
		manager.loadStudents("test-files/student_records.txt");
		
		assertNull(manager.getCurrentUser());
		
		assertFalse(manager.login("student", "pw"));
		assertNull(manager.getCurrentUser());
		
		assertFalse(manager.login("zking", "pw1"));
		assertNull(manager.getCurrentUser());
				
		assertTrue(manager.login("zking", "pw"));
		assertEquals("zking", manager.getCurrentUser().getId());
		
		assertFalse(manager.login("ahicks", "pw"));
		assertEquals("zking", manager.getCurrentUser().getId());
	}
	
	/**
	 * Tests the login method for faculty.
	 */
	@Test
	public void testLoginFaculty() {
		//Set up by adding courses/faculty
		manager.loadCourses("test-files/course_records.txt");
		manager.loadFaculty("test-files/faculty_records.txt");
		
		assertNull(manager.getCurrentUser());
		
		assertFalse(manager.login("faculty", "pw"));
		assertNull(manager.getCurrentUser());
		
		assertFalse(manager.login("awitt", "pw1"));
		assertNull(manager.getCurrentUser());
				
		assertTrue(manager.login("awitt", "pw"));
		assertEquals("awitt", manager.getCurrentUser().getId());
		
		assertFalse(manager.login("fmeadow", "pw"));
		assertEquals("awitt", manager.getCurrentUser().getId());
	}
	

	/**
	 * Tests the logout method.
	 */
	@Test
	public void testLogout() {
		//Set up by adding courses/students
		manager.loadCourses("test-files/course_records.txt");
		manager.loadStudents("test-files/student_records.txt");
		
		assertNull(manager.getCurrentUser());
		
		manager.logout();
		assertNull(manager.getCurrentUser());
		
		manager.login("zking", "pw");
		manager.logout();
		assertNull(manager.getCurrentUser());
	}

	/**
	 * Tests addUserToCourse().
	 */
	@Test
	public void testAddUserToCourse() {
		//Set up by adding courses/students
		manager.loadCourses("test-files/course_records.txt");
		manager.loadStudents("test-files/student_records.txt");
		
		try {
			manager.addUserToCourse(manager.getCourseByName("CSC116"));
			fail("IllegalArgumentException should be thrown if no user is logged in, but it was not");
		} catch (IllegalArgumentException e) {
			assertNull(manager.getCurrentUser());
		}
		
		//Courses as expected results
		Course c1 = new Course("CSC116", 3, 7);
		Course c2 = new Course("CSC216", 3, 10);
		Course c3 = new Course("CSC226", 3, 10);
		Course c4 = new Course("CSC230", 3, 4);
		Course c5 = new Course("CSC316", 3, 1);
		
		Course [] expected1 = {c1, c2, c3, c4};
		Course [] expected2 = {c1, c2, c3, c4, c5};
		
		//Login as Griffith Stone
		manager.login("gstone", "pw");
		
		//Try to add a course Griffith is already enrolled in
		assertFalse(manager.addUserToCourse(manager.getCourseByName("CSC116")));
		assertEquals(5, manager.getCourseByName("CSC116").getEnrolledStudents().length);
		Course [] actual = manager.listUserCourses();
		assertEquals(expected1.length, actual.length);
		for (int i = 0; i < expected1.length; i++)  {
			assertEquals(expected1[i], actual[i]);
		}
		
		//Try to add a course that is fully enrolled
		assertFalse(manager.addUserToCourse(manager.getCourseByName("CSC246")));
		assertEquals(1, manager.getCourseByName("CSC246").getEnrolledStudents().length);
		actual = manager.listUserCourses();
		assertEquals(expected1.length, actual.length);
		for (int i = 0; i < expected1.length; i++)  {
			assertEquals(expected1[i], actual[i]);
		}
		
		//Add a course to get to 15 credit hours
		assertTrue(manager.addUserToCourse(manager.getCourseByName("CSC316")));
		assertEquals(1, manager.getCourseByName("CSC316").getEnrolledStudents().length);
		actual = manager.listUserCourses();
		assertEquals(expected2.length, actual.length);
		for (int i = 0; i < expected2.length; i++)  {
			assertEquals(expected2[i], actual[i]);
		}
		
		//Attempt to add a course that exceed max credit hours of 17
		assertFalse(manager.addUserToCourse(manager.getCourseByName("CSC236")));
		assertEquals(1, manager.getCourseByName("CSC236").getEnrolledStudents().length);
		actual = manager.listUserCourses();
		assertEquals(expected2.length, actual.length);
		for (int i = 0; i < expected2.length; i++)  {
			assertEquals(expected2[i], actual[i]);
		}
	}

	
	/**
	 * Tests addUserToCourse().
	 */
	@Test
	public void testAddUserToCourseFaculty() {
		//Set up by adding courses/faculty
		manager.loadCourses("test-files/course_records.txt");
		manager.loadFaculty("test-files/faculty_records.txt");
		
		try {
			manager.addUserToCourse(manager.getCourseByName("CSC116"));
			fail("IllegalArgumentException should be thrown if no user is logged in, but it was not");
		} catch (IllegalArgumentException e) {
			assertNull(manager.getCurrentUser());
		}
		
		//Courses as expected results
		Course c1 = new Course("CSC450", 3, 7);
		

        manager.login("awitt", "pw");
		
		//Try to add a course Ashley is already the professor for
		assertFalse(manager.addUserToCourse(manager.getCourseByName("CSC116")));
		
		//Try to add a course that has a professor
		assertFalse(manager.addUserToCourse(manager.getCourseByName("CSC246")));
		
		// logout awitt
        manager.logout();
        
        // login fmeadow
        manager.login("fmeadow", "pw");
        
        // verify 2 courses
        assertEquals(2, manager.getCourseByName("CSC216").getFaculty().getCourses().length);
        
		// Create a 3rd course to attempt to add professor to. 
        assertTrue(manager.addUserToCourse(c1));
        
        // verify 3 courses via listUserCourses
        assertEquals(3, manager.listUserCourses().length);

        
	}
	
	/**
	 * Tests removeUserFromCourse().
	 */
	@Test
	public void testRemoveUserFromCourse() {
		//Set up by adding courses/students
		manager.loadCourses("test-files/course_records.txt");
		manager.loadStudents("test-files/student_records.txt");
		
		try {
			manager.removeUserFromCourse(manager.getCourseByName("CSC116"));
			fail("IllegalArgumentException should be thrown if no user is logged in, but it was not");
		} catch (IllegalArgumentException e) {
			assertNull(manager.getCurrentUser());
		}
		
		//Courses as expected results
		Course c1 = new Course("CSC116", 3, 7);
		Course c2 = new Course("CSC216", 3, 10);
		Course c3 = new Course("CSC226", 3, 10);
		Course c4 = new Course("CSC230", 3, 4);
		
		Course [] expected1 = {c1, c2, c3, c4};
		Course [] expected2 = {c1, c3, c4};
		
		//Login as Griffith Stone
		manager.login("gstone", "pw");
		
		//Attempt to remove a course that the user isn't enrolled in
		assertFalse(manager.removeUserFromCourse(manager.getCourseByName("CSC379")));
		assertEquals(3, manager.getCourseByName("CSC379").getEnrolledStudents().length);
		Course [] actual = manager.listUserCourses();
		assertEquals(expected1.length, actual.length);
		for (int i = 0; i < expected1.length; i++)  {
			assertEquals(expected1[i], actual[i]);
		}
		
		//Remove a course the user is enrolled in
		assertTrue(manager.removeUserFromCourse(manager.getCourseByName("CSC216")));
		assertEquals(6, manager.getCourseByName("CSC216").getEnrolledStudents().length);
		actual = manager.listUserCourses();
		assertEquals(expected2.length, actual.length);
		for (int i = 0; i < expected2.length; i++)  {
			assertEquals(expected2[i], actual[i]);
		}
	}
	
	/**
	 * Tests removeUserFromCourse() for faculty.
	 */
	@Test
	public void testRemoveUserFromCourseFaculty() {
		//Set up by adding courses/students
		manager.loadCourses("test-files/course_records.txt");
		manager.loadFaculty("test-files/faculty_records.txt");
		
		try {
			manager.removeUserFromCourse(manager.getCourseByName("CSC116"));
			fail("IllegalArgumentException should be thrown if no user is logged in, but it was not");
		} catch (IllegalArgumentException e) {
			assertNull(manager.getCurrentUser());
		}
				
		manager.login("awitt", "pw");
		
		//Attempt to remove a course that the user isn't enrolled in
		assertFalse(manager.removeUserFromCourse(manager.getCourseByName("CSC379")));
		assertEquals(validFaculty[7], manager.getCourseByName("CSC379").getFaculty().toString());
		
		//Remove a faculty member from a course.
		assertTrue(manager.removeUserFromCourse(manager.getCourseByName("CSC116"))); // was 216
		assertNull(manager.getCourseByName("CSC116").getFaculty());
	}

	
	/**
	 * Tests listUserCourses().  The main path is tested in addUserToCourse().
	 */
	@Test
	public void testListUserCourses() {
		//Set up by adding courses/students
		manager.loadCourses("test-files/course_records.txt");
		manager.loadStudents("test-files/student_records.txt");
		
		try {
			manager.listUserCourses();
			fail("IllegalArgumentException should be thrown if no user is logged in, but it was not");
		} catch (IllegalArgumentException e) {
			assertNull(manager.getCurrentUser());
		}
	}

}
