package edu.ncsu.csc216.course_manager.users;

import static org.junit.Assert.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import edu.ncsu.csc216.course_manager.users.Student;
import edu.ncsu.csc216.course_manager.users.User;

/**
 * Tests for the implemented User methods.
 * @author SarahHeckman
 */
public class UserTest {
	
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
	 * Tests the flows for the User's first name.
	 */
	@Test
	public void testFirstName() {
		//Test a valid first name with all other valid elements
		User u = new Student(firstName, lastName, id, email, hashPW);
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(hashPW, u.getPassword());

		
		//Test that a valid user's first name cannot be changed to null
		try {
			u.setFirstName(null);
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test that a valid user's first name cannot be changed to empty string
		try {
			u.setFirstName("");
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test a null first name
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			//Ignore the FB Dead store notification on this line.  If the user
			//is created, we want to know.
			u = new Student(null, lastName, id, email, hashPW);
			fail("User should not be created with a null first name");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
		
		//Test a empty string first name
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			u = new Student("", lastName, id, email, hashPW);
			fail("User should not be created with an empty string first name");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
	}
	
	/**
	 * Tests the flows for the User's last name.
	 */
	@Test
	public void testLastName() {
		//Test a valid last name with all other valid elements
		User u = new Student(firstName, lastName, id, email, hashPW);
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(hashPW, u.getPassword());
		
		//Test that a valid user's last name cannot be changed to null
		try {
			u.setLastName(null);
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test that a valid user's last name cannot be changed to empty string
		try {
			u.setLastName("");
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test a null last name
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			//Ignore the FB Dead store notification on this line.  If the user
			//is created, we want to know.
			u = new Student(firstName, null, id, email, hashPW);
			fail("User should not be created with a null last name");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
		//Test a empty string last name
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			u = new Student(firstName, "", id, email, hashPW);
			fail("User should not be created with an empty string last name");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
	}
	
	/**
	 * Tests the flows for the User's id
	 */
	@Test
	public void testId() {
		//Test a valid id with all other valid elements
		User u = new Student(firstName, lastName, id, email, hashPW);
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(hashPW, u.getPassword());
		
		//Can't test the setId method.  It's private
				
		//Test a null id
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			//Ignore the FB Dead store notification on this line.  If the user
			//is created, we want to know.
			u = new Student(firstName, lastName, null, email, hashPW);
			fail("User should not be created with a null id");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
		//Test a empty string id
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			u = new Student(firstName, lastName, "", email, hashPW);
			fail("User should not be created with an empty string id");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
	}
	
	/**
	 * Tests the flows for the User's email.
	 */
	@Test
	public void testEmail() {
		//Test a valid last name with all other valid elements
		User u = new Student(firstName, lastName, id, email, hashPW);
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(hashPW, u.getPassword());
		
		//Test that a valid user's email cannot be changed to null
		try {
			u.setEmail(null);
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test that a valid user's email cannot be changed to empty string
		try {
			u.setEmail("");
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test that a valid user's email cannot be changed to an invalid address
		try {
			u.setEmail("invalid.email");
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test a null email
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			//Ignore the FB Dead store notification on this line.  If the user
			//is created, we want to know.
			u = new Student(firstName, lastName, id, null, hashPW);
			fail("User should not be created with a null email");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
		//Test a empty string email
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			u = new Student(firstName, lastName, id, "", hashPW);
			fail("User should not be created with an empty string email");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
		//Test an invalid email
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			u = new Student(firstName, lastName, id, "invalid@email", hashPW);
			fail("User should not be created with an invalid email");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		}
	}
	
	/**
	 * Tests the flows for the User's password.
	 */
	@Test
	public void testPassword() {
		//Test a valid password with all other valid elements
		User u = new Student(firstName, lastName, id, email, hashPW);
		assertEquals(firstName, u.getFirstName());
		assertEquals(lastName, u.getLastName());
		assertEquals(id, u.getId());
		assertEquals(email, u.getEmail());
		assertEquals(hashPW, u.getPassword());
		
		//Test that a valid user's password cannot be changed to null
		try {
			u.setPassword(null);
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test that a valid user's password cannot be changed to empty string
		try {
			u.setPassword("");
			fail("An IllegalArgumentException should be thrown, but was not.");
		} catch (IllegalArgumentException e) {
			//Make sure user's state hasn't changed
			assertEquals(firstName, u.getFirstName());
			assertEquals(lastName, u.getLastName());
			assertEquals(id, u.getId());
			assertEquals(email, u.getEmail());
			assertEquals(hashPW, u.getPassword());
		}
		
		//Test a null password
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			//Ignore the FB Dead store notification on this line.  If the user
			//is created, we want to know.
			u = new Student(firstName, lastName, id, email, null);
			fail("User should not be created with a null password");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
		//Test a empty string password
		u = null; //Initialize to a null user to make sure the user isn't created.
		try {
			u = new Student(firstName, lastName, id, email, "");
			fail("User should not be created with an empty string password");
		} catch (IllegalArgumentException e) {
			assertNull(u);
			//Check that the user was NOT created
		} 
	}

}