package edu.ncsu.csc216.course_manager.courses;



import edu.ncsu.csc216.course_manager.users.Student;
import edu.ncsu.csc216.course_manager.users.User;

/**
 * Provides behavior for objects that represent something that
 * students can enroll in or drop out of.
 * @author SarahHeckman
 */
public interface Enrollable {
	
	/**
	 * Returns the enrolled students as an array.
	 * @return enrolled students
	 */
	public Student [] getEnrolledStudents();
	
	/**
	 * Returns true if there is capacity to add a user to the course and the 
	 * user is not already enrolled.
	 * @param user User to add to the course
	 * @return true if there is capacity
	 */
	public boolean canEnroll(User user);

	/**
	 * Enroll the user in the course if there is room.
	 * @param user user to enroll
	 * @return true if user is enrolled.
	 */
	boolean enroll(User user);
	
	/**
	 * Drops the student from the course.
	 * @param user student to drop
	 * @return true if the student is dropped
	 */
	boolean drop(User user);
}