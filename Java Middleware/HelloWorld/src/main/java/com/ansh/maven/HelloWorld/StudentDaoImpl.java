package com.ansh.maven.HelloWorld;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Transactional
@Repository
public class StudentDaoImpl implements StudentDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Student> getAllStudents() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM students";
		RowMapper<Student> rowMapper = new StudentRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public Student getStudentById(int StudentId) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM students WHERE StudentId = ?";
		RowMapper<Student> rowMapper = new StudentRowMapper();
		Student student= jdbcTemplate.queryForObject(sql, rowMapper, StudentId);
		return student;
	}

	@Override
	public boolean studentExists(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Student> getStudentsWithData() {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT students.StudentId, students.Client, students.Email, students.Phone, students.Address, students.ClientId, students.CurrentPasses, students.FirstName,\r\n" + 
				"students.Gender, students.Grade, students.LastName, students.MiddleName, students.PrimaryStaffMember\r\n" + 
				"FROM students RIGHT JOIN records ON records.StudentId = students.StudentId;";
		RowMapper<Student> rowMapper = new StudentRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	@Override
	public List<String> getCategories(int StudentId) {
		// TODO Auto-generated method stub
		String sql = "SELECT DISTINCT book.category FROM records INNER JOIN students ON records.StudentId = students.StudentId INNER JOIN book ON records.BookId = book.book_id WHERE students.StudentId = ?";
		return this.jdbcTemplate.queryForList(sql, String.class, StudentId + ";");
	
	}

	@Override
	public int addStudent(String Client, String Grade, String Gender) {
		// TODO Auto-generated method stub
		StudentMaster student1 = new StudentMaster(Client, Grade, Gender);
		String sql = "INSERT INTO students (Client, FirstName, LastName, Grade, Gender) VALUES (?, ?, ?, ?, ?);";
		this.jdbcTemplate.update(sql, Client, student1.getFirstName(), student1.getLastName(), student1.getGrade(), student1.getGender());
		//System.out.println(sql);
		return 0;
	}
	
	@Override
	public int updateStudent(String studentId, String client, String email, String phone, String address, String grade, String gender, String currentPasses) {
		if (client == null)
			client = "";
		if (email == null)
			email = "";
		if (phone == null)
			phone = "";
		if (address == null)
			address = "";
		if (currentPasses == null)
			currentPasses = "";
		
		String updateSql = "UPDATE students SET Client = ?, Email = ?, Phone = ?, Address = ?, Grade = ?, Gender = ?, CurrentPasses = ? WHERE StudentId = ?";
		System.out.println(updateSql);
		this.jdbcTemplate.update(updateSql, client, email, phone, address, grade, gender, currentPasses, studentId);
		return 0;
	}
}
