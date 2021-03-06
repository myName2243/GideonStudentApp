package com.ansh.maven.HelloWorld;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.aspectj.weaver.reflect.ReflectionBasedResolvedMemberImpl;
import org.springframework.jdbc.core.RowMapper;

public class RecordRowMapper implements RowMapper<Record>{

	@Override
	public Record mapRow(ResultSet row, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		Record record = new Record();
		record.setBookId(row.getInt("BookId"));
		record.setEndDate(row.getDate("EndDate"));
		record.setMistakes(row.getInt("Mistakes"));
		record.setRecordId(row.getInt("RecordId"));
		record.setRep(row.getInt("Rep"));
		record.setStartDate(row.getDate("StartDate"));
		record.setStudentId(row.getInt("StudentId"));
		record.setTest(row.getInt("Test"));
		record.setTestTime(row.getInt("TestTime"));
		record.setSequenceLarge(row.getInt("sequenceLarge"));
		record.setName(row.getString("Client"));
		record.setBookTitle(row.getString("title"));
		record.setSubcategory(row.getString("subcategory"));
		return record;
	}
	
}
