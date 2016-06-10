package edu.ncsu.csc216.course_manager.courses;



import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.users.Faculty;

/**
 * Tests for Course.  
 * @author SarahHeckman
 */
public class CourseTest {

	private static final String FIRST_NAME = "first";
	private static final String LAST_NAME = "last";
	private static final String ID = "id";
	private static final String EMAIL = "first_last@ncsu.edu";
	private static final String PW = "hashedPassword";
	Faculty f = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);


	/**
	 * Tests the Course constructor.
	 */
	@Test
	public void testCourse() {
		//Test correct path
		Course c = new Course("CSC216", 3, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(3, c.getCredits());
		assertEquals(10, c.getCapacity());
		
		//Test null name
		c = null;
		try {
			c = new Course(null, 3, 10);
			fail("A course with a null name should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		
		//Test empty string name
		c = null;
		try {
			c = new Course("", 3, 10);
			fail("A course with a null name should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		
		//Test credit min boundary
		c = null;
		try {
			c = new Course("CSC216", Course.MIN_HOURS - 1, 10);
			fail("A course with a null name should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		
		c = new Course("CSC216", Course.MIN_HOURS, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(Course.MIN_HOURS, c.getCredits());
		assertEquals(10, c.getCapacity());
		
		//Test credit max boundary
		c = null;
		try {
			c = new Course("CSC216", Course.MAX_HOURS + 1, 10);
			fail("A course with a null name should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}
		
		c = new Course("CSC216", Course.MAX_HOURS, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(Course.MAX_HOURS, c.getCredits());
		assertEquals(10, c.getCapacity());
		
		//Test negative capacity
		c = null;
		try {
			c = new Course("CSC216", 3, -1);
			fail("A negative capacity should throw an IllegalArgumentException.");
		} catch (IllegalArgumentException e) {
			assertNull(c);
		}	
	}
	
	/**
	 * Tests setName() after a valid course object is created.
	 */
	@Test
	public void testSetName() {
		//Test correct path
		Course c = new Course("CSC216", 3, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(3, c.getCredits());
		assertEquals(10, c.getCapacity());
		
		//Set name to null
		try {
			c.setName(null);
			fail();
		} catch (IllegalArgumentException e) {
			//No changes
			assertEquals("CSC216", c.getName());
			assertEquals(3, c.getCredits());
			assertEquals(10, c.getCapacity());
		}
		
		//Set name to empty string
		try {
			c.setName("");
			fail();
		} catch (IllegalArgumentException e) {
			//No changes
			assertEquals("CSC216", c.getName());
			assertEquals(3, c.getCredits());
			assertEquals(10, c.getCapacity());
		}
		
		//Change name
		c.setName("CSC116");
		assertEquals("CSC116", c.getName());
		assertEquals(3, c.getCredits());
		assertEquals(10, c.getCapacity());
	}
	
	/**
	 * Tests setCredits() after a valid course object is created.
	 */
	@Test
	public void testSetCredits() {
		//Test correct path
		Course c = new Course("CSC216", 3, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(3, c.getCredits());
		assertEquals(10, c.getCapacity());
		
		//Set credits to MIN_HOURS - 1
		try {
			c.setCredits(Course.MIN_HOURS - 1);
			fail();
		} catch (IllegalArgumentException e) {
			//No changes
			assertEquals("CSC216", c.getName());
			assertEquals(3, c.getCredits());
			assertEquals(10, c.getCapacity());
		}
		
		//Set credits to MAX_HOURS + 1
		try {
			c.setCredits(Course.MAX_HOURS + 1);
			fail();
		} catch (IllegalArgumentException e) {
			//No changes
			assertEquals("CSC216", c.getName());
			assertEquals(3, c.getCredits());
			assertEquals(10, c.getCapacity());
		}
		
		//Change credits to MIN_HOURS
		c.setCredits(Course.MIN_HOURS);
		assertEquals("CSC216", c.getName());
		assertEquals(Course.MIN_HOURS, c.getCredits());
		assertEquals(10, c.getCapacity());
		
		//Change credits to MAX_HOURS
		c.setCredits(Course.MAX_HOURS);
		assertEquals("CSC216", c.getName());
		assertEquals(Course.MAX_HOURS, c.getCredits());
		assertEquals(10, c.getCapacity());
	}
	
	/**
	 * Test setCapacity() after a valid course object is created.
	 */
	@Test
	public void testSetCapacity() {
		//Test correct path
		Course c = new Course("CSC216", 3, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(3, c.getCredits());
		
		//Set capacity to 0
		try {
			c.setCapacity(0);
			fail();
		} catch (IllegalArgumentException e) {
			//No changes
			assertEquals("CSC216", c.getName());
			assertEquals(3, c.getCredits());
			assertEquals(10, c.getCapacity());
		}
	}
	
	/**
	 * Test hashCode and equals.
	 */
	@Test
	public void testEquals() {
		Course c1 = new Course("CSC216", 4, 10);
		Course c2 = new Course("CSC216", 4, 10);
		Course c3 = new Course("CSC216", 3, 10);
		Course c4 = new Course("CSC116", 4, 10);
		
		assertTrue(c1.equals(c2));
		assertTrue(c1.equals(c3));
		assertFalse(c1.equals(c4));
		assertTrue(c1.equals(c1));
		
		assertEquals(c1.hashCode(), c2.hashCode());
		assertEquals(c1.hashCode(), c3.hashCode());
		assertNotEquals(c1.hashCode(), c4.hashCode());
	}

	/** 
	 * Tests the the CanAddFaculty method
	 */
	public void testCanAddFaculty() {
		Course c1 = new Course("CSC216", 4, 10);
		Course c2 = new Course("CSC217", 3, 10);
		
		assertTrue(c1.canAddFaculty());
		assertFalse(c2.canAddFaculty());
	}
	
	/**
	 * Tests the addFaculty method
	 * @param f faculty to be added
	 */
	public void testAddFaculty(Faculty f) {
		Course c1 = new Course("CSC216", 4, 10);
		Course c2 = new Course("CSC217", 3, 10, f);
		
		assertFalse(c2.addFaculty(f));
		assertTrue(c1.addFaculty(f));
	}
	
	/**
	 * Tests the removeFaculty method
	 */
	public void testRemoveFaculty() {
		Faculty f1 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);
		Course c1 = new Course("CSC217", 3, 10, f1);
		
		assertFalse(c1.canAddFaculty());
		c1.removeFaculty();
		assertTrue(c1.canAddFaculty());		
	}
	
	/**
	 * Tests the getFaculty method
	 */
	public void testGetFaculty() {
		Faculty f2 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);
		Course c1 = new Course("CSC217", 3, 10, f2);
		
		assertEquals(f2, c1.getFaculty());
	}
		
	
}