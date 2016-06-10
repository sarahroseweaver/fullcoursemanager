package edu.ncsu.csc216.course_manager.users;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.users.Faculty;
import edu.ncsu.csc216.course_manager.users.User;
/**
 * Tests the Faculty Class
 * @author sarahweaver
 *
 */

public class FacultyTest {
	
	/** String representing a faculty's first name */
	private static final String FIRST_NAME = "first";
	/** String representing a faculty's last name */
	private static final String LAST_NAME = "last";
	/** String representing a faculty's id */
	private static final String ID = "id";
	/** String representing a faculty's email */
	private static final String EMAIL = "first_last@ncsu.edu";
	/** STring representing a faculty's password */
	private static final String PW = "hashedPassword";

	private Faculty f;
	private Course c1 = new Course("CSC116", 3, 10);
	private Course c3;
	private Course c2;

	/**
	 * Sets up the tests.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		f = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);
		c1 = new Course("CSC116", 3, 10);
		c2 = new Course("CSC216", 3, 10);
		c3 = new Course("CSC226", 3, 10);
	}


	/**
	 * Test method for {@link edu.ncsu.csc216.course_manager.users.Faculty.Faculty()}.
	 */
	@Test
	public void testFaculty() {
		//Construct a Faculty object with valid middle inputs
		//and ensure the fields are correct after construction.
		//2 courses
		Faculty f1 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);
		assertEquals(FIRST_NAME, f1.getFirstName());
		assertEquals(LAST_NAME, f1.getLastName());
		assertEquals(ID, f1.getId());
		assertEquals(EMAIL, f1.getEmail());
		assertEquals(PW, f1.getPassword());
		assertEquals(2, f1.getMaxCourses());

	//1 course
	Faculty f2 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 1);
	assertEquals(FIRST_NAME, f2.getFirstName());
	assertEquals(LAST_NAME, f2.getLastName());
	assertEquals(ID, f2.getId());
	assertEquals(EMAIL, f2.getEmail());
	assertEquals(PW, f2.getPassword());
	assertEquals(1, f2.getMaxCourses());
		
	//3 courses
	Faculty f3 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 3);
	assertEquals(FIRST_NAME, f3.getFirstName());
	assertEquals(LAST_NAME, f3.getLastName());
	assertEquals(ID, f3.getId());
	assertEquals(EMAIL, f3.getEmail());
	assertEquals(PW, f3.getPassword());
	assertEquals(3, f3.getMaxCourses());  
	
	//Test -5 in equivalence class of less than 1
	Faculty f4 = null;
	try {
		f4 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, -5);
		fail();
	} catch (IllegalArgumentException e) {
		assertNull(f4);
	}
	
	//Test 5 in equivalence class of greater than 3
	Faculty f5 = null;
	try {
		f5 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 5);
		fail();
	} catch (IllegalArgumentException e) {
		assertNull(f5);
	}
			
	//Test 0 boundary value
	Faculty f6 = null;
	try {
		f6 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 0);
		fail();
	} catch (IllegalArgumentException e) {
		assertNull(f6);
	}
			
	//Test 4 boundary value
	Faculty f7 = null;
	try {
		f7 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 4);
		fail();
	} catch (IllegalArgumentException e) {
		assertNull(f7);
	}
	
	}
	
	/**
	 * Tests canAddCourse.
	 */
	@Test
	public void testCanAddCourse() {		
		//Check that a course can be added if Faculty has no courses
		assertTrue(f.canAddCourse(c1));
	}
	/**
	 * Tests setMaxCourses
	 */
	public void testSetMaxCourses() {
		try {
			f.setMaxCourses(Faculty.MAX_COURSES + 1);
			fail("Cannot exceed MAX_CREDITS");
		} catch (IllegalArgumentException e) {
			assertEquals(FIRST_NAME, f.getFirstName());
		}
		
		try {
			f.setMaxCourses(Faculty.MIN_COURSES - 1);
			fail("Cannot have less than 1 min course.");
		} catch (IllegalArgumentException e) {
			assertEquals(FIRST_NAME, f.getFirstName());	
			}
	   
	    try {
		    f.setMaxCourses(f.getMaxCourses() - 1);
		    fail("Cannot have less than previously assigned max.");
	    } catch (IllegalArgumentException e) {
		    assertEquals(FIRST_NAME, f.getFirstName());
		 }
        }


	/**
	 * Tests addCourse().
	 */
	@Test
	public void testAddCourse() {		
		//Check that a course is added if no courses
		assertTrue(f.addCourse(c1));
		assertEquals(1, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		
		//Check that adding the same course again fails
		assertFalse(f.addCourse(c1));
		assertEquals(1, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		
		//Check that a second course may be added
		assertTrue(f.addCourse(c2));
		assertEquals(2, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		assertEquals(c2, f.getCourses()[1]);
		
		//Check that a third course is not added
		assertFalse(f.addCourse(c3));
		assertEquals(2, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		assertEquals(c2, f.getCourses()[1]);
		
		//Change max courses to 3 and add a third course
		f.setMaxCourses(3);
		assertTrue(f.addCourse(c3));
		assertEquals(3, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		assertEquals(c2, f.getCourses()[1]);
		assertEquals(c3, f.getCourses()[2]);
	}

	/**
	 * Tests removeCourse().
	 */
	@Test
	public void testRemoveCourse() {
		//Test that we cannot remove a course if not in the list
		assertFalse(f.removeCourse(c1));
		assertEquals(0, f.getCourses().length);
		
		//Add a course and remove, with a quick check that we can't remove another course
		assertTrue(f.addCourse(c1));
		assertEquals(1, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		
		assertFalse(f.removeCourse(c2));
		assertEquals(1, f.getCourses().length);
		assertEquals(c1, f.getCourses()[0]);
		
		assertTrue(f.removeCourse(c1));
		assertEquals(0, f.getCourses().length);
		
		assertFalse(f.removeCourse(c1));
		assertEquals(0, f.getCourses().length);
	}

	/**
	 * Tests hashCode().
	 */
	@Test
	public void testHashCode() {
		User u1 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 3);
		User u2 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 3);
		User u3 = new Faculty(FIRST_NAME + "1", LAST_NAME, ID, EMAIL, PW, 3);
		User u4 = new Faculty(FIRST_NAME, LAST_NAME + "1", ID, EMAIL, PW, 3);
		User u5 = new Faculty(FIRST_NAME, LAST_NAME, ID + "1", EMAIL, PW, 3);
		User u6 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL + "1", PW, 3);
		User u7 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);
		
		assertEquals(u1.hashCode(), u2.hashCode());
		assertNotEquals(u1.hashCode(), u3.hashCode());
		assertNotEquals(u1.hashCode(), u4.hashCode());
		assertNotEquals(u1.hashCode(), u5.hashCode());
		assertNotEquals(u1.hashCode(), u6.hashCode());
		assertNotEquals(u1.hashCode(), u7.hashCode());
		
		//Add courses and test for equality
		u1.addCourse(c1);
		u2.addCourse(c1);
		assertEquals(u1.hashCode(), u2.hashCode());
		
		u2.addCourse(c2);
		assertNotEquals(u1.hashCode(), u2.hashCode());
	}

	/**
	 * Tests equals().
	 */
	@Test
	public void testEqualsObject() {
		User u1 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 3);
		User u2 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 3);
		User u3 = new Faculty(FIRST_NAME + "1", LAST_NAME, ID, EMAIL, PW, 3);
		User u4 = new Faculty(FIRST_NAME, LAST_NAME + "1", ID, EMAIL, PW, 3);
		User u5 = new Faculty(FIRST_NAME, LAST_NAME, ID + "1", EMAIL, PW, 3);
		User u6 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL + "1", PW, 3);
		User u7 = new Faculty(FIRST_NAME, LAST_NAME, ID, EMAIL, PW, 2);
		
		assertTrue(u1.equals(u2));
		assertFalse(u1.equals(u3));
		assertFalse(u1.equals(u4));
		assertFalse(u1.equals(u5));
		assertFalse(u1.equals(u6));
		assertFalse(u1.equals(u7));
		
		//Add courses and test for equality
		u1.addCourse(c1);
		u2.addCourse(c1);
		assertTrue(u1.equals(u2));
			
		u2.addCourse(c2);
		assertFalse(u1.equals(u2));
	}

	/**
	 * Tests toString().
	 */
	@Test
	public void testToString() {
		//Check string with no courses
		assertEquals(FIRST_NAME + "," + LAST_NAME + "," + ID + "," + EMAIL + "," + PW + ",2", f.toString());
		
		//Add course and check
		f.addCourse(c1);
		assertEquals(FIRST_NAME + "," + LAST_NAME + "," + ID + "," + EMAIL + "," + PW + ",2,CSC116", f.toString());
		
		//Add second course and check
		f.addCourse(c2);
		assertEquals(FIRST_NAME + "," + LAST_NAME + "," + ID + "," + EMAIL + "," + PW + ",2,CSC116,CSC216", f.toString());
	}

}
