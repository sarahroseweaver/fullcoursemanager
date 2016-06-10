/**
 * 
 */
package edu.ncsu.csc216.course_manager.courses;

import java.util.ArrayList;

import edu.ncsu.csc216.course_manager.users.Faculty;
import edu.ncsu.csc216.course_manager.users.Student;
import edu.ncsu.csc216.course_manager.users.User;

/**
 * Course is a description of each course.
 * @author sarahweaver
 *
 */
public class Course implements Enrollable {
	/**
	 * @param name
	 * @param credits
	 * @param capacity
	 */

	/** Course's name */
	private String name;
	/** Course's credits */
	private int credits;
	/** Course's capacity */
	private int capacity;
	/** Faculty teaching the course */
	private Faculty professor = null;
	
	
	/** minimum credit hours */
	public static final int MIN_HOURS = 1;
	/** maximum credit hours */
	public static final int MAX_HOURS = 4;
	
	/** Students enrolled in the course */
	private ArrayList<User> enrolledStudents;
	
	/**
	 * Constructor for a course object
	 * @param name name of course
	 * @param credits the course is worth
	 * @param capacity of the course
	 */
	public Course(String name, int credits, int capacity) {
		super();
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException();
		}
		if (credits < MIN_HOURS || credits > MAX_HOURS ) {
			throw new IllegalArgumentException();
		}
		if (capacity <= 0) {
			throw new IllegalArgumentException();
		}
		enrolledStudents = new ArrayList<User>();
		setName(name);
		setCredits(credits);
		setCapacity(capacity);
	}

	/**
	 * Secondary constructor for course containing a faculty member.
	 * @param string title of course
	 * @param i number of credits
	 * @param j capacity
	 * @param f faculty member teaching course
	 */
	public Course(String string, int i, int j, Faculty f) {
		super();
		this.professor = f;
	}

	/**
	 * Returns the name of the course
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the course
	 * @param name the name to set
	 */
	public void setName(String name) throws IllegalArgumentException {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.name = name;
	}
	
	/**
	 * Returns the credits in the course
	 * @return the credits
	 */
	public int getCredits() {
		return credits;
	}
	/**
	 * Sets the credits in the course
	 * @param credits the credits to set
	 * @throws IllegalArgumentException if the credits are less than 
	 * MIN_HOURS or more than MAX_HOURS
	 */
	public void setCredits(int credits) {
		if (credits < MIN_HOURS || credits > MAX_HOURS ) {
			throw new IllegalArgumentException();
		}
		this.credits = credits;
	}
	/**
	 * Returns the capacity of the course
	 * @return the capacity
	 */
	public int getCapacity() {
		return capacity;
	}
	/**
	 * Sets the capacity of the course
	 * @param capacity the capacity to set
	 * @throws IllegalArgumentException if the capacity is less than 0 or 
	 * less than the amount of enrolled students.
	 */
	public void setCapacity(int capacity) {
		if (capacity <= 0 || capacity < enrolledStudents.size()) {
			throw new IllegalArgumentException();
		}
		this.capacity = capacity;
	}
	
	/** generates a hashcode
	 * @return the hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * determines whether two courses are equal
	 * @param obj that is the second course
	 * @return true if they are equal
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/**
	 * Returns a string of the course's information
	 * @return a string of a course's name, credits and capacity.
	 */
	@Override
	public String toString() {
		return name + "," + credits + "," + capacity;
	}	


	/**
	 * Enroll the user in the course if there is room.
	 * @param user user to enroll
	 * @return true if user is enrolled.
	 */
	public boolean enroll(User user) {
		return canEnroll(user) && enrolledStudents.add(user);
	}

	/**
	 * Returns the enrolled students as an array.
	 * @return enrolled students
	 */
	public Student [] getEnrolledStudents() {
		Student [] s = new Student[enrolledStudents.size()];
		return enrolledStudents.toArray(s);
	}

	/**
	 * Returns true if there is capacity to add a user to the course and the 
	 * user is not already enrolled.
	 * @param user User to add to the course
	 * @return true if there is capacity
	 */
	public boolean canEnroll(User user) {
		if (enrolledStudents.size() < capacity) {
			if (user instanceof Student) {
				Student s = (Student) user;
				for (int i = 0; i < enrolledStudents.size(); i++) {
					if (enrolledStudents.get(i).equals(s)) {
						return false;
					}
				}
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Drops the student from the course.
	 * @param user student to drop
	 * @return true if the student is dropped
	 */
	public boolean drop(User user) {
		return enrolledStudents.remove(user);
	}

	/**
	 * Determines whether a faculty member can be added
	 * @return true if a faculty member can be added
	 */
	public boolean canAddFaculty() {
		if (this.professor == null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Adds a faculty member.
	 * @param faculty faculty member.
	 * @return true if the member is added
	 */
	public boolean addFaculty(Faculty faculty) {
		if (!canAddFaculty()) {
			return false;
		}
		this.professor = faculty;
		return true;	
	}
	
	/**
	 * Removes a faculty member.
	 */
	public void removeFaculty() {
		this.professor = null;
	}
	
	/**
	 * Returns the faculty member instance variable value.
	 * @return this.professor
	 */
	public Faculty getFaculty() {
		return this.professor;
	}
	
}
