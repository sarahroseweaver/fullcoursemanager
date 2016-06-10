package edu.ncsu.csc216.course_manager.io;

import java.io.*;
import java.util.*;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.manager.CourseManager;
import edu.ncsu.csc216.course_manager.users.Student;

/**
 * Class that allows a student record to be inputted and outputted.
 * @author sarahweaver
 *
 */

public class StudentRecordIO {
	
	/**
	 * Reads a file line by line and adds a Student to the list if the student is valid
	 * @param fileName of the file to be read
	 * @return the student
	 * @throws FileNotFoundException e
	 */
	public static ArrayList<Student> readStudentRecords(String fileName) throws FileNotFoundException {
		ArrayList<Student> students = new ArrayList<Student>();
		Scanner fileScan;
        try {
		    fileScan = new Scanner(new FileInputStream(fileName));
        
		    while (fileScan.hasNextLine()) {
			   try {
				  Student s = processStudent(fileScan.nextLine());
				  students.add(s);
			    } catch (IllegalArgumentException e) {
				    // ignore lines with errors
			    }
		    }
        } catch (IOException e) {
        	throw new FileNotFoundException();
        }
		fileScan.close();
		return students;
	}

	/** Reads each element of a Student,Adds a Course to the Student - student 
	 * enrolled in the course. 
	 * 
	 * @param line of student information
	 * @return the student
	 * @throw IllegalArgumentException if the course is null or the student cannot enroll in the course.	 * 
	 */
	private static Student processStudent(String line) {
		
		Scanner lineScanner = new Scanner(line);
		try {
		    lineScanner.useDelimiter(",");
		    String firstName = lineScanner.next();
		    String lastName = lineScanner.next();
		    String id = lineScanner.next();
		    String email = lineScanner.next();
		    String password = lineScanner.next();
		    int maxCredits = lineScanner.nextInt();
	        Student s = new Student(firstName, lastName, id, email, password, maxCredits);	
		    while (lineScanner.hasNext()) {
		    	String courseName = lineScanner.next();
		        Course c = CourseManager.getInstance().getCourseByName(courseName);
		        if (c == null) {
		    	    lineScanner.close();
		    	    throw new IllegalArgumentException();
		        }
		        if (!s.canAddCourse(c)) {
			        lineScanner.close();
			        throw new IllegalArgumentException();
		        } 
		        if (s.addCourse(c)) {
		        	c.enroll(s);
		        }
		    }
		    lineScanner.close();
		    return s;
	      } catch (NoSuchElementException e) {
	    	  lineScanner.close();
		      throw new IllegalArgumentException();
	      }
     }    	

	/** 
	 * Opens a file and writes the Student records to it
	 * @param fileName name of the file
	 * @param students list of students
	 * @throws IOException if there are any errors opening the file for writing
	 */
    public static void writeStudentRecords(String fileName, List<Student> students) throws IOException{
    	PrintWriter fileOut = new PrintWriter(new FileWriter(fileName));
    	
    	for (Student s : students) {
    		fileOut.println(s.toString());
    	}
    	fileOut.close();
    }
  	
	
}
