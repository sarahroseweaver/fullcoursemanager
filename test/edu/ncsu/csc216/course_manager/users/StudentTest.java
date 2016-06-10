package edu.ncsu.csc216.course_manager.users;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.users.Student;
import edu.ncsu.csc216.course_manager.users.User;

/**
 * Tests the Student object.
 * @author SarahHeckman
 */
public class StudentTest {
	
	/** Student for testing */
	private Student s;
	/** Courses for testing */
	private Course c1, c2, c3, c4, c5, c6, c7;
	/** Test user's first name. */
	private String firstName = "first";
	/** Test user's last name */
	private String lastName = "last";
	/** Test user's id */
	private String id = "flast";
	/** Test user's email */
	private String email = "first_last@ncsu.edu";
	/** Test user's hashed password */
	private String hashPW;
	/** Hashing algorithm */
	private static final String HASH_ALGORITHM = "SHA-256";
	
	//This is a block of code that is executed when the UserTest object is
	//created by JUnit.  Since we only need to generate the hashed version
	//of the plaintext password once, we want to create it as the object is
	//constructed.  By automating the hash of the plaintext password, we are
	//not tied to a specific hash implementation.  We can change the algorithm
	//easily.
	{
		try {
			String plaintextPW = "password";
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(plaintextPW.getBytes());
			this.hashPW = new String(digest.digest());
		} catch (NoSuchAlgorithmException e) {
			fail("An unexpected NoSuchAlgorithmException was thrown.");
		}
	}
	
	/**
	 * Sets up a student object for testing.
	 */
	@Before
	public void setUp() {
		s = new Student(firstName, lastName, id, email, hashPW);
		c1 = new Course("CSC116", 3, 10);
		c2 = new Course("CSC216", 3, 10);
		c3 = new Course("CSC226", 3, 10);
		c4 = new Course("CSC230", 3, 10);
		c5 = new Course("CSC236", 3, 10);
		c6 = new Course("CSC246", 3, 10);
		c7 = new Course("CSC316", 3, 10);
	}
	
	/**
	 * Helper method for comparing courses.
	 */
	private void compareCourses(Course [] expected, Course [] actual) {
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], actual[i]);
		}
	}

	/**
	 * Tests that a Student is constructed correctly.
	 */
	@Test
	public void testStudent() {
		assertEquals(firstName, s.getFirstName());
		assertEquals(lastName, s.getLastName());
		assertEquals(id, s.getId());
		assertEquals(email, s.getEmail());
		assertEquals(hashPW, s.getPassword());
		assertEquals(Student.MAX_CREDITS, s.getMaxCredits());
		assertEquals(0, s.getCourses().length);
		assertEquals(0, s.getCurrentCredits());
		
		//Use constructor with number of credits
		Student s1 = new Student(firstName, lastName, id, email, hashPW, 15);
		assertEquals(firstName, s1.getFirstName());
		assertEquals(lastName, s1.getLastName());
		assertEquals(id, s1.getId());
		assertEquals(email, s1.getEmail());
		assertEquals(hashPW, s1.getPassword());
		assertEquals(15, s1.getMaxCredits());
		assertEquals(0, s1.getCourses().length);
		assertEquals(0, s1.getCurrentCredits());
	}
	
	/**
	 * Tests adding a course to a student.
	 */
	@Test
	public void testAddCourse() {
		assertTrue(s.addCourse(c1));
		assertEquals(1, s.getCourses().length);
		assertEquals(3, s.getCurrentCredits());
		Course [] expCourses1 = {c1};
		compareCourses(expCourses1, s.getCourses());
		
		//Can't add because duplicate
		assertFalse(s.addCourse(c1));
		assertEquals(1, s.getCourses().length);
		assertEquals(3, s.getCurrentCredits());
		compareCourses(expCourses1, s.getCourses());
		
		assertTrue(s.addCourse(c2));
		assertEquals(2, s.getCourses().length);
		assertEquals(6, s.getCurrentCredits());
		Course [] expCourses2 = {c1, c2};
		compareCourses(expCourses2, s.getCourses());
		
		assertTrue(s.addCourse(c3));
		assertEquals(3, s.getCourses().length);
		assertEquals(9, s.getCurrentCredits());
		Course [] expCourses3 = {c1, c2, c3};
		compareCourses(expCourses3, s.getCourses());
		
		assertTrue(s.addCourse(c4));
		assertEquals(4, s.getCourses().length);
		assertEquals(12, s.getCurrentCredits());
		Course [] expCourses4 = {c1, c2, c3, c4};
		compareCourses(expCourses4, s.getCourses());
		
		assertTrue(s.addCourse(c5));
		assertEquals(5, s.getCourses().length);
		assertEquals(15, s.getCurrentCredits());
		Course [] expCourses5 = {c1, c2, c3, c4, c5};
		compareCourses(expCourses5, s.getCourses());
		
		assertTrue(s.addCourse(c6));
		assertEquals(6, s.getCourses().length);
		assertEquals(18, s.getCurrentCredits());
		Course [] expCourses6 = {c1, c2, c3, c4, c5, c6};
		compareCourses(expCourses6, s.getCourses());
		
		//Can't add due to credit hours
		assertFalse(s.addCourse(c7));
		assertEquals(6, s.getCourses().length);
		assertEquals(18, s.getCurrentCredits());
	}

	/**
	 * Tests removing a course from a student.
	 */
	@Test
	public void testRemoveCourse() {
		//Can't remove because no courses
		assertFalse(s.removeCourse(c1));
		
		//Add courses
		s.addCourse(c1);
		s.addCourse(c2);
		s.addCourse(c3);
		s.addCourse(c4);
		assertEquals(4, s.getCourses().length);
		assertEquals(12, s.getCurrentCredits());
		Course [] expCourses0 = {c1, c2, c3, c4};
		compareCourses(expCourses0, s.getCourses());
		
		//Remove last
		assertTrue(s.removeCourse(c4));
		assertEquals(3, s.getCourses().length);
		assertEquals(9, s.getCurrentCredits());
		Course [] expCourses1 = {c1, c2, c3};
		compareCourses(expCourses1, s.getCourses());
		
		//Remove middle
		assertTrue(s.removeCourse(c2));
		assertEquals(2, s.getCourses().length);
		assertEquals(6, s.getCurrentCredits());
		Course [] expCourses2 = {c1, c3};
		compareCourses(expCourses2, s.getCourses());
		
		//Remove first
		assertTrue(s.removeCourse(c1));
		assertEquals(1, s.getCourses().length);
		assertEquals(3, s.getCurrentCredits());
		Course [] expCourses3 = {c3};
		compareCourses(expCourses3, s.getCourses());
		
		//Remove only
		assertTrue(s.removeCourse(c3));
		assertEquals(0, s.getCourses().length);
		assertEquals(0, s.getCurrentCredits());
		Course [] expCourses4 = {};
		compareCourses(expCourses4, s.getCourses());
	}

	/**
	 * Tests setting the max credits.  Also tests getting the credits.
	 */
	@Test
	public void testMaxCredits() {
		//Test if max credits are less than 0
		try {
			s.setMaxCredits(-1);
			fail("Cannot have negative max credits.");
		} catch (IllegalArgumentException e) {
			assertEquals(firstName, s.getFirstName());
			assertEquals(lastName, s.getLastName());
			assertEquals(id, s.getId());
			assertEquals(email, s.getEmail());
			assertEquals(hashPW, s.getPassword());
			assertEquals(Student.MAX_CREDITS, s.getMaxCredits());
			assertEquals(0, s.getCourses().length);
			assertEquals(0, s.getCurrentCredits());
		}
		
		//Test if max credits are greater than MAX_CREDITS
		try {
			s.setMaxCredits(Student.MAX_CREDITS + 1);
			fail("Cannot exceed MAX_CREDITS");
		} catch (IllegalArgumentException e) {
			assertEquals(firstName, s.getFirstName());
			assertEquals(lastName, s.getLastName());
			assertEquals(id, s.getId());
			assertEquals(email, s.getEmail());
			assertEquals(hashPW, s.getPassword());
			assertEquals(Student.MAX_CREDITS, s.getMaxCredits());
			assertEquals(0, s.getCourses().length);
			assertEquals(0, s.getCurrentCredits());
		}
		
		//Add a course and then attempt to lower max credits to 2.
		s.addCourse(c1);
		try {
			s.setMaxCredits(2);
			fail("Cannot lower max credits to a value less than current registered credits.");
		} catch (IllegalArgumentException e) {
			assertEquals(firstName, s.getFirstName());
			assertEquals(lastName, s.getLastName());
			assertEquals(id, s.getId());
			assertEquals(email, s.getEmail());
			assertEquals(hashPW, s.getPassword());
			assertEquals(Student.MAX_CREDITS, s.getMaxCredits());
			assertEquals(1, s.getCourses().length);
			assertEquals(3, s.getCurrentCredits());
		}
	}
	
	/**
	 * Tests equals() and hashCode() methods.
	 */
	@Test
	public void testEqualsHashCode() {
		User u1 = new Student(firstName, lastName, id, email, hashPW);
		User u2 = new Student(firstName, lastName, id, email, hashPW);
		User u3 = new Student(firstName + "1", lastName, id, email, hashPW);
		User u4 = new Student(firstName, lastName + "1", id, email, hashPW);
		User u5 = new Student(firstName, lastName, id + "1", email, hashPW);
		User u6 = new Student(firstName, lastName, id, email + "1", hashPW);
		
		assertTrue(u1.equals(u2));
		assertFalse(u1.equals(u3));
		assertFalse(u1.equals(u4));
		assertFalse(u1.equals(u5));
		assertFalse(u1.equals(u6));
		
		assertEquals(u1.hashCode(), u2.hashCode());
		assertNotEquals(u1.hashCode(), u3.hashCode());
		assertNotEquals(u1.hashCode(), u4.hashCode());
		assertNotEquals(u1.hashCode(), u5.hashCode());
		assertNotEquals(u1.hashCode(), u6.hashCode());
	}
	
	/**
	 * Test toString() method.
	 */
	@Test
	public void testToString() {
		User u1 = new Student(firstName, lastName, id, email, hashPW);
		assertEquals("first,last,flast,first_last@ncsu.edu," + hashPW + ",18", u1.toString());
	}

}