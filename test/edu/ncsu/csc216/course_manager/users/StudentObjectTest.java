package edu.ncsu.csc216.course_manager.users;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import edu.ncsu.csc216.course_manager.users.Student;
import edu.ncsu.csc216.course_manager.users.User;
/**
 * Tests the object methods of the student class
 * @author sarahweaver
 *
 */
public class StudentObjectTest {

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