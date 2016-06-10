package edu.ncsu.csc216.course_manager.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.manager.CourseManager;
import edu.ncsu.csc216.course_manager.users.Faculty;

/**
 * Class that allows a faculty record to be inputted and outputted.
 * @author sarahweaver
 *
 */
public class FacultyRecordIO {
	/**
	 * Reads Faculty records from the given file.  If the file doesn't exist
	 * a FileNotFoundException is thrown.  A line with a format error will be
	 * ignored.
	 * @param fileName name of file to read
	 * @return Faculty records
	 * @throws FileNotFoundException if the file doesn't exist
	 */
	public static ArrayList<Faculty> readFacultyRecords(String fileName) throws FileNotFoundException {
		ArrayList<Faculty> faculty = new ArrayList<Faculty>();
		Scanner fileScan;
        try {
		    fileScan = new Scanner(new FileInputStream(fileName));
        
		    while (fileScan.hasNextLine()) {
			   try {
				  Faculty f = processFaculty(fileScan.nextLine());
				  faculty.add(f);
			    } catch (IllegalArgumentException e) {
			    	//if the exception is thrown ignore the line. 
			    }
		    }
        } catch (IOException e) {
        	throw new FileNotFoundException();
        }
		fileScan.close();
		return faculty;
	}
	
	/**
	 * Creates a Faculty object from the String record.  An IllegalArgumentException is thrown
	 * if one of the items is missing or if the Faculty cannot be constructed.
	 * @param line the line to process
	 * @return a valid Faculty 
	 */
	private static Faculty processFaculty(String line) {
		Scanner lineScanner = new Scanner(line);
		try {
		    lineScanner.useDelimiter(",");
		    String firstName = lineScanner.next();
		    String lastName = lineScanner.next();
		    String id = lineScanner.next();
		    String email = lineScanner.next();
		    String password = lineScanner.next();
		    int maxCredits = lineScanner.nextInt();
	        Faculty f = new Faculty(firstName, lastName, id, email, password, maxCredits);
		    while (lineScanner.hasNext()) {
		    	String courseName = lineScanner.next();
		        Course c = CourseManager.getInstance().getCourseByName(courseName);
		        if (c == null) {
		    	    lineScanner.close();
		    	    throw new IllegalArgumentException();
		        }
		        if (!f.canAddCourse(c)) {
			        lineScanner.close();
			        throw new IllegalArgumentException();
		        } 
		        if (f.canAddCourse(c)) {
		        	f.addCourse(c);
		        	c.addFaculty(f);
		        }
		    }
		    lineScanner.close();
		    return f;
	      } catch (NoSuchElementException e) {
	    	  lineScanner.close();
		      throw new IllegalArgumentException();
	      }
     }    
	
	/**
	 * Writes the information about the faculty members to the given file.
	 * @param fileName file name to record data
	 * @param faculty list of faculty
	 * @throws IOException if cannot write to file
	 */
	public static void writeFacultyRecords(String fileName, List<Faculty> faculty) throws IOException{
    	PrintWriter fileOut = new PrintWriter(new FileWriter(fileName));
    	
    	for (Faculty f : faculty) {
    		fileOut.println(f.toString());
    	}
    	fileOut.close();
    }
}
