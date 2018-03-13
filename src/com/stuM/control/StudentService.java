package com.stuM.control;

import java.util.*;
import javax.swing.table.AbstractTableModel;

import com.stuM.domain.Student;
import com.tools.sqlhelper.SQLHelper;

public class StudentService extends AbstractTableModel{

	/**
	 * 操作数据库-学生表
	 */
	private static final long serialVersionUID = 1L;
	//rowData用来存放行数据
	//columnNames用来存放列名
	Vector<Student> rowData;
	Vector<String> columnNames;
	
	private void updateStu(String sql,String []pargs) {
		SQLHelper sqlHelper=new SQLHelper();
		sqlHelper.executeUpdate(sql, pargs);
		sqlHelper.close();
	}
	
	private void queryStu(String sql, String []pargs){	
		
		SQLHelper sqlHelper=new SQLHelper();
		sqlHelper.executeQuery(sql, pargs);
		try {
			while(sqlHelper.getRs().next())
			{
				Student student=new Student(sqlHelper.getRs().getString(1),
						sqlHelper.getRs().getString(2),
						sqlHelper.getRs().getString(3),
						sqlHelper.getRs().getInt(4),
						sqlHelper.getRs().getString(5),
						sqlHelper.getRs().getString(6));
				rowData.add(student);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}finally{
			sqlHelper.close();
		}
	}
	
	public StudentService queryAll() {
		queryStu("select stuNum,stuName,stuSex,stuAge,stuDept,stuClass from student order by stuNum", null);
		return this;
	}
	public StudentService queryAllByName(String name) {
		//判断姓名为空或空格
		if(name.trim().isEmpty()){
			return queryAll();
		}
		String pargs[]={name};
		queryStu("select stuNum,stuName,stuSex,stuAge,stuDept,stuClass from student where stuName=?", pargs);
		return this;
	}
	public StudentService add(Student student) {
		String pargs[]={student.getStuNum(),student.getStuName(),
				student.getStuSex(),String.valueOf(student.getStuAge()),
				student.getStuDept(),student.getStuClass()};
		updateStu("insert into student values(stuSeq.nextval,?,?,?,?,?,?)", pargs);
		return this;
	}
	public StudentService modify(Student student) {
		String pargs[]={student.getStuName(),student.getStuSex(),
				String.valueOf(student.getStuAge()),student.getStuDept(),
				student.getStuClass(),student.getStuNum()};
		updateStu("update student set stuName=?,stuSex=?,stuAge=?,stuDept=?,stuClass=? where stuNum=?", pargs);
		return this;
	}
	public StudentService delete(int rowNum) {
		String []pargs={(String)getValueAt(rowNum, 0)};
		updateStu("delete from student where stuNum=?", pargs);
		return this;
	}
	
	public Student getStudent(int rowNum) {
		return new Student(getValueAt(rowNum, 0).toString(), 
				getValueAt(rowNum, 1).toString(), 
				getValueAt(rowNum, 2).toString(), 
				Integer.parseInt(getValueAt(rowNum, 3).toString()), 
				getValueAt(rowNum, 4).toString(), 
				getValueAt(rowNum, 5).toString());
	}	
	
	//构造函数
	public StudentService() {
		columnNames=new Vector<String>();
		//设置列名
		columnNames.add("学号");
		columnNames.add("名字");
		columnNames.add("性别");
		columnNames.add("年龄");
		columnNames.add("学院");
		columnNames.add("班级");
		
		rowData=new Vector<Student>();
	}
	
	/*下面的函数会自动调用*/
	/*重写getColumnName方法,得到列名*/
	@Override
	public String getColumnName(int column) {
		return this.columnNames.get(column);
	}
	//得到共有多少行
	@Override
	public int getRowCount() {
		return this.rowData.size();
	}
	//得到共有多少列
	@Override
	public int getColumnCount() {
		return this.columnNames.size();
	}
	//得到某行某列的数据
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return this.rowData.get(rowIndex).get(columnIndex);
	}

}
