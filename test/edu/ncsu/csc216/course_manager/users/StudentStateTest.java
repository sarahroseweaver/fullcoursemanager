package edu.ncsu.csc216.course_manager.users;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.users.Student;

/**
 * Tests the methods of the student class, excluding the object methods
 * @author sarahweaver
 *
 */
public class StudentStateTest {

	/** Student for testing */
	private Student s;
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
		
		//Use constructor with number of credits
		Student s1 = new Student(firstName, lastName, id, email, hashPW, 15);
		assertEquals(firstName, s1.getFirstName());
		assertEquals(lastName, s1.getLastName());
		assertEquals(id, s1.getId());
		assertEquals(email, s1.getEmail());
		assertEquals(hashPW, s1.getPassword());
		assertEquals(15, s1.getMaxCredits());
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
		}
	}

}