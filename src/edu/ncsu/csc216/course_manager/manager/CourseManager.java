package edu.ncsu.csc216.course_manager.manager;

import java.util.*;
import java.io.*;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.io.*;
import edu.ncsu.csc216.course_manager.users.Faculty;
import edu.ncsu.csc216.course_manager.users.Student;
import edu.ncsu.csc216.course_manager.users.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
/**
 * Manages the courses and the students. 
 * @author sarahweaver
 *
 */

public class CourseManager {
	/** CourseManager singleton instance */
	private static CourseManager manager;
	/** List of all Courses in the system */
	private ArrayList<Course> courses;
	/** List of all Students in the system */
	private ArrayList<Student> students;
	/** List of all Faculty in the system */
	private ArrayList<Faculty> faculty;
	/** Currently logged in User */
	private User currentUser;
	/** Course records file name */
	private String courseFileName;
	/** Student records file name */
	private String studentFileName;
	/** Faculty records file name */
	private String facultyFileName;
	/** Hashing algorithm */
	private static final String HASH_ALGORITHM = "SHA-256";

	/**
	 * Constructor for CourseManager.  It's private so that it can
	 * only be created inside of CourseManager and we can ensure
	 * only a single instance of the class is created.  This makes it 
	 * very easy to work with the CourseManager throughout the system.
	 */
	private CourseManager() {
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
		faculty = new ArrayList<Faculty>();
	}

	/**
	 * Returns the singleton instance of CourseManager.  If the instance 
	 * doesn't exist, it will be created.
	 * @return singleton instance
	 */
	public static CourseManager getInstance() {
		if (manager == null) {
			manager = new CourseManager();
		}
		return manager;
	}

	
	/**
	 * Log user into the system if there is no one else logged in.
	 * @param id user's id
	 * @param password user's password
	 * @return true if user is logged in
	 */
	public boolean login(String id, String password) {
		if (currentUser != null) {
			return false;
		}
		for (Student s: students) {
			if (s.getId().equals(id)) {
				try {
					MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
					digest.update(password.getBytes());
					String hashPW = new String(digest.digest());
					if (s.getPassword().equals(hashPW)) {
						currentUser = s;
						return true;
					}
					return false;
				} catch (NoSuchAlgorithmException e) {
					throw new IllegalArgumentException();
				}	
			}
		}
		for (Faculty f: faculty) {
			if (f.getId().equals(id)) {
				try {
					MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
					digest.update(password.getBytes());
					String hashPW = new String(digest.digest());
					if (f.getPassword().equals(hashPW)) {
						currentUser = f;
						return true;
					}
					return false;
				} catch (NoSuchAlgorithmException e) {
					throw new IllegalArgumentException();
				}	
			}
		}
		return false;
	}
	
	/**
	 * Log current user out of the system.
	 */
	public void logout() {
		currentUser = null;
	}
	
	/**
	 * Returns the current logged in user or null if there is 
	 * no logged in user.
	 * @return logged in user
	 */
	public User getCurrentUser() {
		return currentUser;
	}
	/**
	 * Returns a list of all Courses associated with the current User.
	 * @return list of User's courses
	 */
	public Course[] listUserCourses() {
		if (currentUser == null) {
			throw new IllegalArgumentException("User is not logged in.");
		}
		return currentUser.getCourses();
	}

	/**
	 * Returns a list of all Courses in the system.
	 * @return list of all Courses
	 */
	public Course[] listAllCourses() {
		Course [] allCourses = new Course[courses.size()];
		return courses.toArray(allCourses);
	}
	
	/**
	 * Returns true if the Course is added to the current User's
	 * list of courses.
	 * @param course Course to add
	 * @return true if added to the User
	 */
	public boolean addUserToCourse(Course course) {
		if (currentUser == null) {
			throw new IllegalArgumentException("User is not logged in.");
		}
		if (currentUser.canAddCourse(course) && currentUser instanceof Student) {
			Student s = (Student)currentUser;
			if (course.canEnroll(s)) {
				currentUser.addCourse(course);
				course.enroll(s);
				return true;
			}
		}
		
		else if (currentUser.canAddCourse(course) && currentUser instanceof Faculty) {
			Faculty f = (Faculty)currentUser;
			if (course.canAddFaculty()) {
				currentUser.addCourse(course);
				course.addFaculty(f);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns true if the Course is removed from the current User's
	 * list of courses.
	 * @param course Course to remove
	 * @return true if removed from the User
	 */
	public boolean removeUserFromCourse(Course course) {
		    if (currentUser == null) {
			    throw new IllegalArgumentException("User is not logged in.");
		    }
		    if (currentUser instanceof Student) {
		        course.drop(currentUser);
		        return currentUser.removeCourse(course);
		    }
            if (currentUser instanceof Faculty && currentUser.equals(course.getFaculty())) {
			        course.removeFaculty();
			        return currentUser.removeCourse(course);
            	}
		
		return false;
	}

	
	/**
	 * Clears all course and student data from the Course manager 
	 * without saving.
	 */
	public void clearData() {
		courses = new ArrayList<Course>();
		students = new ArrayList<Student>();
		faculty = new ArrayList<Faculty>();
		currentUser = null;
		setCourseFileName(null);
		setStudentFileName(null);
		setFacultyFileName(null);
	}

	/**
	 * Returns the student's file name
	 * @return studentFileName
	 */
	public String getStudentFileName() {
		return studentFileName;
	}

	/**
	 * Sets the student's file name
	 * @param studentFileName name of the student file
	 */
	public void setStudentFileName(String studentFileName) {
		this.studentFileName = studentFileName;
	}
	
	/** 
	 * Sets the faculty file name
	 * @param facultyFileName name of the faculty file
	 */
	public void setFacultyFileName(String facultyFileName) {
		this.facultyFileName = facultyFileName;
	}
 
	/**
	 * Gets the course file name
	 * @return courseFileName
	 */
	public String getCourseFileName() {
		return courseFileName;
	}

	/**
	 * Sets the course file name
	 * @param courseFileName name of file with course in it
	 */
	public void setCourseFileName(String courseFileName) {
		this.courseFileName = courseFileName;
	}
	
	/**
	 * Returns a course's name
	 * @param course name of course
	 * @return courses.get(i);
	 */
	public Course getCourseByName(String course) {
		for (int i = 0; i < courses.size(); i++) {
			if (courses.get(i).getName().equals(course)) {
				return courses.get(i);
			}
	   }
	   return null;
	}
	
	/**
	 * accepts a file and saves it
	 * @param studentFileName of file of students.
	 * @throws IllegalArgumentException if the file is invalid
	 */
	public void loadStudents(String studentFileName) {
		this.studentFileName = studentFileName;
		try{
			ArrayList<Student> studentsFromFile = StudentRecordIO.readStudentRecords(studentFileName);
			for (Student s : studentsFromFile) {
				addStudent(s);
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	
	}
	/**
	 * Adds a student to the list of students. 
	 * @param student Student to add
	 */
	public void addStudent(Student student) {
		for (Student s: students) {
			if (s.equals(student)) {
				return;
			}
		}
		students.add(student);
	}
	
	/**
	 * Writes the Student records to a file.
	 * @throws IllegalArgumentException if CourseRecordIO cannot write the course record. 
	 */
	public void saveStudents() {
		try {
		PrintStream printStream = new PrintStream(new File(studentFileName));
		for (int i = 0; i < students.size(); i++){
			printStream.println(students.get(i).toString());
		}
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	/**
	 * Loads the list of Courses from the given file.
	 * @param fileName name of file containing courses
	 */
	public void loadCourses(String fileName) {
		this.courseFileName = fileName;
		try {
			List<Course> coursesFromFile = CourseRecordIO.readCourseRecords(courseFileName);
			for (Course c : coursesFromFile) {
				addCourse(c);
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Adds a course to the list of courses. 
	 * @param course Course to add
	 * @return true if the course already exists. 
	 */
	public boolean addCourse(Course course) {
		for (Course c: courses) {
			if (c.equals(course)) {
				return false;
			}
		}
		courses.add(course);
		return true;
	}
	
	/**
	 * Writes the list of Courses to the courseFileName.
	 */
	public void saveCourses() {
		try {
			CourseRecordIO.writeCourseRecords(courseFileName, courses);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Loads the list of Faculty from the given file.
	 * @param fileName name of file containing courses
	 */
	public void loadFaculty(String fileName) {
		this.facultyFileName = fileName;
		try {
			List<Faculty> facultyFromFile = FacultyRecordIO.readFacultyRecords(facultyFileName);
			for (Faculty f : facultyFromFile) {
				addFaculty(f);
			}
		} catch (FileNotFoundException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	
	/**
	 * Writes the faculty records to a file.
	 * @throws IllegalArgumentException if CourseRecordIO cannot write the course record. 
	 */
	public void saveFaculty() {
		try {
		PrintStream printStream = new PrintStream(new File(facultyFileName));
		for (int i = 0; i < faculty.size(); i++){
			printStream.println(faculty.get(i).toString());
		}
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Adds a faculty member to a course
	 * @param f faculty member to be added
	 */
    public void addFaculty(Faculty f) {
				faculty.add(f);
			}
    
    /**
     * REmoves a course.
     * @param c course
     * @return courses.remove(c)
     */
    
    public boolean removeCourse(Course c) {
		if (c == null) return false; 
        if (c.getFaculty() != null) {
        	c.getFaculty().removeCourse(c);   
        }
        for (int i = 0; i < students.size(); i++){
			c.drop(students.get(i));
		}
		return courses.remove(c);	
	}

}
