package com.ansh.maven.HelloWorld;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.aop.interceptor.ExposeBeanNameAdvisors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties.Jdbc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.module.SimpleAbstractTypeResolver;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers.StringKeySerializer;

@Transactional
@Repository
public class RecordDaoImpl implements RecordDao{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	//Retrieves all records from every student in the database
	@Override
	public List<Record> getAllRecords() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM records INNER JOIN book ON records.BookId = book.book_id INNER JOIN students ON records.StudentId = students.StudentId";
		RowMapper<Record> rowMapper = new RecordRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	//Returns all records of a student in a specific category and a specific repetition number
	@Override
	public List<Record> getRecordsById(int RecordId, String category, String whichReps) {
		// TODO Auto-generated method stub		
		String sql = "SELECT * FROM records INNER JOIN book ON records.BookId = book.book_id INNER JOIN students ON records.StudentId ="
				+ " students.StudentId WHERE records.StudentId = ? AND book.category = ? # ORDER BY StartDate";

		RowMapper<Record> rowMapper = new RecordRowMapper();
		List<Record> output;
		
		if(whichReps.equalsIgnoreCase("All")) {
			sql = sql.replace("#","");
			output = this.jdbcTemplate.query(sql, rowMapper, RecordId, category);
		} else {
			sql = sql.replace("#", "AND records.Rep = ?");
			output = this.jdbcTemplate.query(sql, rowMapper, RecordId, category, whichReps);
		}
		return output;
	}

	//Unwritten and unused: returns whether or not a record exists given its ID
	@Override
	public boolean recordExists(int RecordId) {
		// TODO Auto-generated method stub
		return false;
	}

	//Adds a new record to the record database with all of the following information, formats the appropriate SQL string
	@Override
	public int addRecord(int studentId, String category, String subcategory, String title, Date startDate, int rep) {
		int bookId = 0;
		boolean test = false;
		
		// get book id
		String booksql = "Select * FROM book";
		RowMapper<Book> bookRowMapper = new BookRowMapper();
		List<Book> bookList = this.jdbcTemplate.query(booksql, bookRowMapper);
		
		for(Book b: bookList) {
			if(b.getTitle().equals(title) && b.getCategory().equals(category)) {
				bookId = b.getBook_id();
				if(b.getTest() > 0) {
					test = true;
				}
			}
		}
		
		String cap = ", #, null, null)";
		if (test)
			cap = cap.replace("#", "1");
		else
			cap = cap.replace("#", "0");
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(startDate);
		String recordsql = "INSERT INTO records (StudentId, BookId, StartDate, EndDate, Rep, Test, TestTime, Mistakes) VALUES (?, ?, ?, null, ?" + cap;
		System.out.println(recordsql);
		this.jdbcTemplate.update(recordsql, studentId, bookId, formatted, rep);
		return 0;
	}

	//Updates an already existing record
	@Override
	public int updateRecord(int recordId, Date endDate, int testTime, int mistakes) {	
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		String formatted = format1.format(endDate);
		
		String updateSql = "UPDATE records SET endDate = ?, #  WHERE RecordId = ?";
		
		if(testTime < 0 || mistakes < 0) {
			updateSql = updateSql.replace("#", "TestTime = null, mistakes = null");
			System.out.println(updateSql);
			this.jdbcTemplate.update(updateSql, formatted, recordId);
		} else {
			updateSql = updateSql.replace("#", "TestTime = ?, mistakes = ?");
			System.out.println(updateSql);
			this.jdbcTemplate.update(updateSql, formatted, testTime, mistakes, recordId);
		}
		return 0;
	}

	//Returns all records that have start dates, but do not have end dates
	@Override
	public List<Record> getIncompleteRecords() {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM records INNER JOIN book ON records.BookId = book.book_id INNER JOIN students ON records.StudentId = students.StudentId WHERE records.EndDate IS NULL";
		RowMapper<Record> rowMapper = new RecordRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper);
	}

	//Returns all records for a certain student and a certain category
	@Override
	public List<Record> getAllRecordsById(int StudentId, String category) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM records INNER JOIN students ON records.StudentId = students.StudentId INNER JOIN book ON records.BookId = book.book_id WHERE students.StudentId = ? AND book.category = ? AND records.rep = 1;";
		System.out.println(sql);
		RowMapper<Record> rowMapper = new RecordRowMapper();
		return this.jdbcTemplate.query(sql, rowMapper, StudentId, category);
	}
	
}
