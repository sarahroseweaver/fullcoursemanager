package edu.ncsu.csc216.course_manager.courses;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.users.Student;

/**
 * Tests the enroll and drop methods of Course.
 * @author SarahHeckman
 */
public class FullCourseTest {

	/**
	 * Tests enroll().
	 */
	@Test
	public void testEnroll() {
		//Add a student to a course
		Course c = new Course("CSC216", 3, 1);
		Student s1 = new Student("first", "last", "flast", "first_last@ncsu.edu", "pw");
		assertTrue(c.enroll(s1));
		
		//Set capacity to 1 so that the attempt to enroll a second student should fail
		c.setCapacity(1);
		Student s2 = new Student("first", "last", "flast", "first_last@ncsu.edu", "pw");
		assertFalse(c.enroll(s2));
		
		//Attempt to enroll a student in the same course
		c.setCapacity(2);
		assertFalse(c.enroll(s1));
		
		//canEnroll() is tested through enroll()
	}
	
	/**
	 * Tests drop().
	 */
	@Test
	public void testDrop() {
		//Attempt to drop a student not enrolled in the course
		Course c = new Course("CSC216", 3, 1);
		Student s1 = new Student("first", "last", "flast", "first_last@ncsu.edu", "pw");
		assertFalse(c.drop(s1));
		
		assertTrue(c.enroll(s1));
		assertEquals(1, c.getEnrolledStudents().length);
		assertTrue(c.drop(s1));
		assertEquals(0, c.getEnrolledStudents().length);
	}
	
	/**
	 * Test changing the capacity when considering enrollments.
	 */
	@Test
	public void testSetCapacity() {
		//Test correct path
		Course c = new Course("CSC216", 3, 10);
		assertEquals("CSC216", c.getName());
		assertEquals(3, c.getCredits());
		
		//Test setting the capacity to a size smaller than the number
		//of enrolled students
		Student s1 = new Student("first", "last", "flast", "first_last@ncsu.edu", "pw");
		assertTrue(c.enroll(s1));
		Student s2 = new Student("first", "last", "flast2", "first_last@ncsu.edu", "pw");
		assertTrue(c.enroll(s2));
		
		try {
			c.setCapacity(1);
			fail();
		} catch (IllegalArgumentException e) {
			//No changes
			assertEquals("CSC216", c.getName());
			assertEquals(3, c.getCredits());
			assertEquals(10, c.getCapacity());
			assertEquals(2, c.getEnrolledStudents().length);
		}
	}
	
	/**
	 * Tests equals() and hashCode() methods.
	 */
	@Test
	public void testEqualsHashCode() {
		Course c1 = new Course("CSC216", 3, 3);
		Course c2 = new Course("CSC216", 3, 1);
		Course c3 = new Course("CSC116", 3, 3);
		Course c4 = new Course("CSC216", 4, 3);
		
		assertTrue(c1.equals(c2));
		assertFalse(c1.equals(c3));
		assertTrue(c1.equals(c4));
		
		assertEquals(c1.hashCode(), c2.hashCode());
		assertNotEquals(c1.hashCode(), c3.hashCode());
		assertEquals(c1.hashCode(), c4.hashCode());
	}
	
	/**
	 * Test toString() method.
	 */
	@Test
	public void testToString() {
		Course c1 = new Course("CSC216", 3, 3);
		assertEquals("CSC216,3,3", c1.toString());
	}

}