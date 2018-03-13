package com.stuM.domain;

/**
 * 学生信息表(Oracle)
  create table student(id number(20) primary key,
  stuNum varchar2(20) unique not null,
  stuName varchar2(20) not null,
  stuSex varchar2(2) check(stuSex in ('男','女'))  not null,
  stuAge number(3) check(stuAge>0 and stuAge<120)  not null,
  stuDept varchar2(20)  not null,
  stuClass varchar2(20)  not null ) ;
   序列 
  create sequence stuSeq
  start with 1
  increment by 1
  minvalue 1
  nomaxvalue
  nocycle
  nocache;
 */
public class Student {

	private String stuNum;
	private String stuName;
	private String stuSex;
	private int stuAge;
	private String stuDept;
	private String stuClass;
	
	public String getStuNum() {
		return stuNum;
	}
	public void setStuNum(String stuNum) {
		this.stuNum = stuNum;
	}
	public String getStuName() {
		return stuName;
	}
	public void setStuName(String stuName) {
		this.stuName = stuName;
	}
	public String getStuSex() {
		return stuSex;
	}
	public void setStuSex(String stuSex) {
		this.stuSex = stuSex;
	}
	public int getStuAge() {
		return stuAge;
	}
	public void setStuAge(int stuAge) {
		this.stuAge = stuAge;
	}
	public String getStuDept() {
		return stuDept;
	}
	public void setStuDept(String stuDept) {
		this.stuDept = stuDept;
	}
	public String getStuClass() {
		return stuClass;
	}
	public void setStuClass(String stuClass) {
		this.stuClass = stuClass;
	}
	
	public Student() {
	}
	public Student(String stuNum, String stuName, String stuSex, int stuAge,
			String stuDept, String stuClass) {
		super();
		this.stuNum = stuNum;
		this.stuName = stuName;
		this.stuSex = stuSex;
		this.stuAge = stuAge;
		this.stuDept = stuDept;
		this.stuClass = stuClass;
	}
	public Object get(int columnIndex) {
		Object object=null;
		switch (columnIndex) {
		case 0:
			object=this.stuNum;
			break;
		case 1:
			object=this.stuName;
			break;
		case 2:
			object=this.stuSex;
			break;
		case 3:
			object=this.stuAge;
			break;
		case 4:
			object=this.stuDept;
			break;
		case 5:
			object=this.stuClass;
			break;

		default:
			break;
		}
		return object;
	}
	
}
