/**
 * 
 */
package edu.ncsu.csc216.course_manager.users;

import java.util.ArrayList;

import edu.ncsu.csc216.course_manager.courses.Course;

/**
 * Class representing a student 
 * @author sarahweaver
 *
 */
public class Student extends User {

	/**Array of student's courses **/
	private ArrayList<Course> courses;
	/** the max credits that a student can take. */
	private int maxCredits;
	/** the max credits possible for any student. **/
	public static final int MAX_CREDITS = 18;

	/** Constructor for the student, based on the User superclass.
	 * 
	 * @param firstName of student
	 * @param lastName of student
	 * @param id of student
	 * @param email of student
	 * @param password of student
	 * @param maxCredits possible for a student
	 */
	public Student(String firstName, String lastName, String id, String email, String password, int maxCredits) {
		super(firstName, lastName, id, email, password);
		setMaxCredits(maxCredits);
		courses = new ArrayList<Course>();
	}
	
   /**
    * Constructor for the student
    * @param firstName student's first name
    * @param lastName student's last name
    * @param id student's id
    * @param email student's email
    * @param password student's password
    */
	public Student(String firstName, String lastName, String id, String email, String password) {
		this(firstName, lastName, id, email, password, MAX_CREDITS);
	}

	/** Determines whether a student can add another course
	 * @param c course to be added
	 * @return true if a course can be added
	 */
	@Override
	public boolean canAddCourse(Course c) {
		int count = 0;
		for (int i = 0; i < courses.size(); i++) {
			Course d = courses.get(i);
			count += d.getCredits();
			if (d.equals(c)) {
				return false;
			}
	    }
	    if (count + c.getCredits() <= this.maxCredits) {
		    return true;
	    }
		return false;
	}

	/**
	 * Adds a new course for a student
	 * @param c course to be added
	 * @return true if the course was added
	 */
	@Override
	public boolean addCourse(Course c) {
		if (canAddCourse(c)){
		    courses.add(c);
		    return true;
		}
		return false;
	}

	/**
	 * Removes a course from a student
	 * @param c course to be removed 
	 */
	@Override
	public boolean removeCourse(Course c) {
		return courses.remove(c);
	}

	
    /**
     * Returns the list of courses as an array
     * @return courses as an array
     */
	@Override
	public Course[] getCourses() {
		Course[] c = new Course[courses.size()];	
		return courses.toArray(c);
	}
	
	/**
	 * Returns the amount of credits a student is enrolled in
	 * @return count of all of the student's credits
	 */
	public int getCurrentCredits() {
		int count = 0;
		for (int i = 0; i < courses.size(); i++) {
			Course d = courses.get(i);
			count += d.getCredits();
		}
		return count;
	}
    
	/**
	 * Returns the maximum number of credits a student can be enrolled in
	 * @return max credits for student
	 */
	public int getMaxCredits() {
		return maxCredits;
	}
    
	/**
	 * Sets the maximum amount of credits a student can be enrolled in.
	 * @param maxCredits for that student
	 * @throws IllegalArgumentException if the amount of credits for the student is 
	 * attempting to be set to less than zero, if it is greater than the maximum allowed 
	 * credits, or if it is less than the student's already existing maximum credits.
	 */
	public void setMaxCredits(int maxCredits) {
		if (maxCredits < 0 || maxCredits > MAX_CREDITS || maxCredits < this.maxCredits) {
			throw new IllegalArgumentException();
		}
		this.maxCredits = maxCredits;
	}

	
    /**
     * Returns a string of the student's information
     * @return string of student's info, the student's max credits, 
     * and the student's courses.
     */
	@Override
	public String toString() {
		String temp = super.toString() + "," + maxCredits;
		for (int i = 0; i < courses.size(); i++) {
			Course c = courses.get(i);
			temp += ",";
			temp += c.getName();
		}
		return temp;
	}

	/**
	 * generates a hashcode
	 * @return hashcode
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((courses == null) ? 0 : courses.hashCode());
		result = prime * result + maxCredits;
		return result;
	}

	
    /**
     * Determines whether a student object is equal to 
     * another student object.
     * @param obj second student object
     * @return true if they are the same
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (courses == null) {
			if (other.courses != null)
				return false;
		} else if (!courses.equals(other.courses))
			return false;
		if (maxCredits != other.maxCredits)
			return false;
		return true;
	}

}
