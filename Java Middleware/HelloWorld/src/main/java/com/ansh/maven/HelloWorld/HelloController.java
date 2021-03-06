package com.ansh.maven.HelloWorld;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.h2.util.IntArray;
import org.hibernate.validator.internal.util.privilegedactions.GetAnnotationParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
	
	public final String webOrigin = "http://localhost:8081";
	
	@Autowired
	BookService bookService;
	
	@Autowired
	StudentService studentService;
	
	@Autowired
	RecordService recordService;
	
	//Returns all books in the book database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/books")
	public List<Book> getAllBooks() {
		System.out.println("Method getAllBooks() called");
		return bookService.getAllBooks();
	}
	
	//Returns all subcategories given a specific category
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/subcategories")
	public List<String> getSubcategories(@RequestParam("Category") String category) {
		System.out.println("Method getSubcategories() called for category " + category);
		return bookService.getSubcategories(category);
	}
	
	//Returns all titles given a specific subcategory
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/titles")
	public List<String> getTitles(@RequestParam("Subcategory") String subcategory) {
		System.out.println("Method getTitles() called for subcategory " + subcategory);
		return bookService.getTitles(subcategory);
	}
	
	//Returns all books between two sequence large values given a category
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/booksInRange")
	public List<Book> getBooksInRange(@RequestParam("Category") String category, @RequestParam("StartSequence") int startSequence, @RequestParam("EndSequence") int endSequence) {
		System.out.println("Method getBooksInRange() called for category " + category + " and range " + startSequence + " to " + endSequence);
		return bookService.getBooksInRange(category, startSequence, endSequence);
	}
	
	//Returns all students in the student database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/students")
	public List<Student> getAllStudents() {
		System.out.println("Method getAllStudents() called");
		return studentService.getAllStudents();
	}
	
	//Returns all data for a student given the student's ID
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/student")
	public Student getStudentById(@RequestParam("Id") int StudentId) {
		System.out.println("Method getStudentById() called for Student ID " + StudentId);
		return studentService.getStudentById(StudentId);
	}
	
	//Returns all students who have records in the record database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/dataStudents")
	public List<Student> getStudentsWithData() {
		System.out.println("Method getStudentsWithData() called");
		return studentService.getStudentsWithData();
	}
	
	//Returns all categories that a student is working in
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/categoriesByStudent")
	public List<String> getCategories(@RequestParam("Id") int StudentId) {
		System.out.println("Method getCategories() called for Student ID " + StudentId);
		return studentService.getCategories(StudentId);
	}
	
	//Returns grade of the student as an integer value
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/gradeOfStudent")
	public int getGrade(@RequestParam("Id") int StudentId) {
		System.out.println("Method getGrade() called for Student ID " + StudentId);
		return studentService.getGrade(StudentId);
	}
	
	//Adds a new student to the students database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/addStudent")
	public int addStudent(@RequestBody(required=false) StudentMaster student) {
		System.out.println("Method addStudent() called" );
		int a;
		try {
			a = studentService.addStudent(student.getClient(), student.getGrade(), student.getGender());
		} catch (java.lang.RuntimeException e) {
			System.out.println("Method addStudent() failed:");
			e.printStackTrace();
			a = -1;
		}
		return a;
	}
	
	//Edits student data in the students database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/updateStudent")
	public int updateStudent(@RequestParam("studentId") String studentId, @RequestParam("client") String client, @RequestParam("email") String email, @RequestParam("phone") String phone, @RequestParam("address") String address, @RequestParam("grade") String grade, @RequestParam("gender") String gender, @RequestParam("currentPasses") String currentPasses) {
		System.out.println("Method updateStudent() called");
		int a;
		try {
			a = studentService.updateStudent(studentId, client, email, phone, address, grade, gender, currentPasses);
		} catch (java.lang.RuntimeException e) {
			System.out.println("Method updateStudent() failed");
			e.printStackTrace();
			a = -1;
		}
		return a;
	}
	
	//Returns all records in the record database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/records")
	public List<Record> getAllRecords() {
		System.out.println("Method getAllRecords() called");
		return recordService.getAllRecords();
	}
	
	//Returns all records in the record database that do not have end dates
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/incompleteRecords")
	public List<Record> getIncompleteRecords() {
		System.out.println("Method getIncompleteRecords() called");
		return recordService.getIncompleteRecords();
	}
	
	//Returns all records for a given student and a given category with time and repetition constraints (NOTE: includes one record before the start time, useful for graphing)
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/recordsById")
	@ResponseBody
	public List<Record> getRecordsById(@RequestParam("StudentId") int StudentId, @RequestParam("Category") String category, @RequestParam("Months") int months, @RequestParam("Reps") String whichReps, @RequestParam("Until") int until) {
		System.out.println("Method getRecordsById() called for Student ID " + StudentId + ", category " + category + ", month number " + months + ", and rep number " + whichReps + ", and until " + until);
		return recordService.getRecordsById(StudentId, category, months, whichReps, until);
	}
	
	//Returns all records for a given student and a given category
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/allRecordById")
	public List<Record> getAllRecordsById(@RequestParam("StudentId") int StudentId, @RequestParam("Category") String category) {
		System.out.println("Method getRecordsById() called for Student ID " + StudentId + " and category " + category);
		return recordService.getAllRecordsById(StudentId, category);
	}
	
	//Adds a new record to the database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/addRecord")
	public int addRecord(@RequestBody(required=false) Master master) {
		System.out.println("Method addRecord() called");
		int a;
		try {
			a = recordService.addRecord(master.getId(), master.getCategory(), master.getSubcategory(), master.getTitle(), master.getStartDate(), master.getRep());
		} catch (java.lang.RuntimeException e) {
			System.out.println("Method addRecord() failed:");
			e.printStackTrace();
			a = -1;
		}
		return a;
	}
	
	//Updates an existing record in the database
	@CrossOrigin(origins = webOrigin)
	@RequestMapping("/updateRecord")
	public int updateRecord(@RequestParam("recordId") int recordId, @RequestParam("endDate") Date endDate, @RequestParam("testTime") int testTime, @RequestParam("mistakes") int mistakes) {
		System.out.println("Method updateRecord() called");
		System.out.println(testTime + " " + mistakes);
		int a; 
		try {
			a = recordService.updateRecord(recordId, endDate, testTime, mistakes);
		} catch (java.lang.RuntimeException e) {
			System.out.println("Method updateRecord() failed:");
			e.printStackTrace();
			a = -1;
		}
		return a;
	}
	

	
}
