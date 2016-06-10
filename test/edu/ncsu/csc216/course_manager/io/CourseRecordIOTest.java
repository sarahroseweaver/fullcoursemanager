package edu.ncsu.csc216.course_manager.io;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc216.course_manager.courses.Course;
import edu.ncsu.csc216.course_manager.io.CourseRecordIO;

/**
 * Tests CouresRecordIO.
 * 
 * @author SarahHeckman
 */
public class CourseRecordIOTest {

	private final String validTestFile = "test-files/course_records.txt";
	private final String invalidTestFile = "test-files/invalid_course_records.txt";

	private final String validCourse1 = "CSC116,3,7";
	private final String validCourse2 = "CSC216,3,10";
	private final String validCourse3 = "CSC226,3,10";
	private final String validCourse4 = "CSC230,3,4";
	private final String validCourse5 = "CSC236,3,10";
	private final String validCourse6 = "CSC246,3,1";
	private final String validCourse7 = "CSC316,3,1";
	private final String validCourse8 = "CSC379,1,10";

	private final String[] validCourses = { validCourse1, validCourse2, validCourse3, validCourse4, validCourse5,
			validCourse6, validCourse7, validCourse8 };

	/**
	 * Resets student_records.txt for use in other tests.
	 */
	@Before
	public void setUp() throws Exception {
		// Reset student_records.txt so that it's fine for other needed tests
		Path sourcePath = FileSystems.getDefault().getPath("test-files", "expected_full_course_records.txt");
		Path destinationPath = FileSystems.getDefault().getPath("test-files", "course_records.txt");
		try {
			Files.deleteIfExists(destinationPath);
			Files.copy(sourcePath, destinationPath);
		} catch (IOException e) {
			fail("Unable to reset files");
		}
	}

	/**
	 * Tests readValidCourseRecords().
	 */
	@Test
	public void testReadValidCourseRecords() {
		try {
			List<Course> courses = CourseRecordIO.readCourseRecords(validTestFile);
			assertEquals(8, courses.size());

			for (int i = 0; i < validCourses.length; i++) {
				assertEquals(validCourses[i], courses.get(i).toString());
			}
		} catch (FileNotFoundException e) {
			fail("Unexpected error reading " + validTestFile);
		}
	}

	/**
	 * Tests readInvalidCourseRecords().
	 */
	@Test
	public void testReadInvalidCourseRecords() {
		List<Course> courses;
		try {
			courses = CourseRecordIO.readCourseRecords(invalidTestFile);
			assertEquals(0, courses.size());
		} catch (FileNotFoundException e) {
			fail("Unexpected FileNotFoundException");
		}
	}

	/**
	 * Tests writeCourseRecords()
	 */
	@Test
	public void testWriteCourseRecords() {
		ArrayList<Course> courses = new ArrayList<Course>();
		courses.add(new Course("CSC116", 3, 10));
		courses.add(new Course("CSC216", 3, 10));
		courses.add(new Course("CSC230", 3, 10));

		try {
			CourseRecordIO.writeCourseRecords("test-files/actual_course_records.txt", courses);
		} catch (IOException e) {
			fail("Cannot write to course records file");
		}

		checkFiles("test-files/expected_course_records.txt", "test-files/actual_course_records.txt");
	}

	/**
	 * Helper method to compare two files for the same contents
	 * 
	 * @param expFile
	 *            expected output
	 * @param actFile
	 *            actual output
	 */
	private void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new FileInputStream(expFile));
				Scanner actScanner = new Scanner(new FileInputStream(actFile));) {

			while (expScanner.hasNextLine()) {
				assertEquals(expScanner.nextLine(), actScanner.nextLine());
			}

			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}

}