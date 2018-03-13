package com.tools.sqlhelper;

import java.sql.*;

import org.junit.Test;


/**
 * 使用说明:
 * @author zpxuz
 * 1.改配置文件dbinfo.properties到对应数据库.
 * 2.Oracle需要把所有的use mydb3;去掉
 * 3.假设我们希望rs结果，可以滚动（可以向前，亦可向后）
 *   st=ct.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
 * 	  如果我希望重新使用rs,把rs置回到开始处  rs.beforeFirst();
 * 4.函数和select调用类似,    select 函数名(参数) from 表名
 * 
 */
public class SQLHelperTest {
	
		//创建测试用的表
		@Test
		public void createTable_for_mysql_sqlserver() {
			SQLHelper sqlHelper=new SQLHelper();
			try {
				sqlHelper.getCt().prepareStatement("create database mydb3").execute();
				sqlHelper.getCt().prepareStatement("use mydb3").execute();
				sqlHelper.getCt().prepareStatement("create table dept(deptno int,dname varchar(20))").execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				sqlHelper.close();
			}
		}
		@Test
		public void createTable_for_oracle() {
			SQLHelper sqlHelper=new SQLHelper();
			try {
				sqlHelper.getCt().prepareStatement("create table dept(deptno number,dname varchar2(20))").execute();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				sqlHelper.close();
			}
		}
		
		//测试SQLHelper工具的executeUpdate
		@Test
		public void executeUpdate_test1() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql="insert into dept values(1,'daming')";
//			String sql="use mydb3;insert into dept values(1,'daming')";
			sqlHelper.executeUpdate(sql, null);
			sqlHelper.close();
		}
		@Test
		public void executeUpdate_test2() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql="insert into dept values(?,?)";
//			String sql="use mydb3;insert into dept values(?,?)";
			String[] parameters = {"2","xiaoming"};
			sqlHelper.executeUpdate(sql, parameters);
			sqlHelper.close();
		}

		//测试SQLHelper工具的executeUpdate2
		@Test
		public void executeUpdate2_test1() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql1="insert into dept values(3,'lili')";
			String sql2="insert into dept values(4,'xiaowang')";
			String sql3="insert into dept values(5,'susu')";
//			String sql1="use mydb3;insert into dept values(3,'lili')";
//			String sql2="use mydb3;insert into dept values(4,'xiaowang')";
//			String sql3="use mydb3;insert into dept values(5,'susu')";
			String[] sql={sql1,sql2,sql3};
			sqlHelper.executeUpdate2(sql, null);
			sqlHelper.close();
		}
		@Test
		public void executeUpdate2_test2() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql1="insert into dept values(?,?)";
			String sql2="insert into dept values(?,?)";
			String sql3="insert into dept values(?,?)";
//			String sql1="use mydb3;insert into dept values(?,?)";
//			String sql2="use mydb3;insert into dept values(?,?)";
//			String sql3="use mydb3;insert into dept values(?,?)";
			String[] sql={sql1,sql2,sql3};
			String[] parameter1 = {"6","lisi"};
			String[] parameter2 = {"7","xiaoliu"};
			String[] parameter3 = {"8","susan"};
			String[][] parameters = {parameter1,parameter2,parameter3};
			sqlHelper.executeUpdate2(sql, parameters);
			sqlHelper.close();
		}
		//测试事务 ,故意写错,出错后所有操作都没有执行.当把事务注释掉,再次实验时,出错之前的sql都执行了.
		@Test
		public void executeUpdate2_test3() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql1="update dept set dname='uuuuu' where deptno=2";
//			String sql1="use mydb3;update dept set dname='uuuuu' where deptno=2";
			String sql2="sssss";
			String sql3="update dept set dname='sssss' where deptno=4";
//			String sql3="use mydb3;update dept set dname='sssss' where deptno=4";
			String[] sql={sql1,sql2,sql3};
			sqlHelper.executeUpdate2(sql, null);
			sqlHelper.close();
		}
		
		//测试SQLHelper工具的executeQuery
		@Test
		public void executeQuery_test1() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql="select stuNum,stuName,stuSex,stuAge,stuDept,stuClass from student";
			sqlHelper.executeQuery(sql, null);
			try {
				while(sqlHelper.getRs().next()){
					System.out.println(sqlHelper.getRs().getString("stuNum")
							+"\t"+sqlHelper.getRs().getString("stuName"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				sqlHelper.close();
			}
		}
		@Test
		public void executeQuery_test2() {
			SQLHelper sqlHelper=new SQLHelper();
			String sql="select * from dept where dname=?";
//			String sql="use mydb3;select * from dept where dname=?";
			String[] parameters = {"uuuuu"};
			ResultSet rs=sqlHelper.executeQuery(sql, parameters);
			try {
				while(rs.next()){
					System.out.println(rs.getString("deptno")
							+"\t"+rs.getString("dname"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				sqlHelper.close();
			}
		}
		
		//测试SQLHelper工具的executeProcedure
		//SQLserver未测试通过,总是报call语法错误,不知该怎么调用.
		@Test
		public void executeProcedure_test1() {
			int a=6,b=11;
			SQLHelper sqlHelper=new SQLHelper();
			String sql="call pr_add(?,?,?) ";
//			String sql="use mydb3;  call pr_add(?,?,?) ";
			Object[] parameters_in = {a,b};
			int[] parameters_out = {Types.INTEGER};
			sqlHelper.executeProcedure(sql, parameters_in,parameters_out);
			try {
				System.out.println(a+"+"+b+"="+sqlHelper.getCs().getString(3));
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				sqlHelper.close();
			}
		}
		/**
		 * [mysql]
			create PROCEDURE pr_add(in a int,in b int ,out c int)
			begin
				set c=a+b;
			end;
		 * 
		 */
		
		/**
		 * [oracle]
	      create or replace procedure pr_add  (a in number, b in number, c out number)
	      as
	      begin 
	          c:=a+b;
	      end;
		 * 
		 */
		
		/**
		 * [sqlserver]
			CREATE PROCEDURE pr_add  @a INT, @b INT, @c INT OUTPUT
			AS
			BEGIN 
				SET @c=@a+@b
			GO
		 * 
		 */
		//调用分页存储过程,只在Oracle上测试过
		/**
		 * 要求:请大家编写一个存储过程,
		 *      要求可以输入表名、每页显示记录数、当前页，排序字段(deptno降序),
		 *      返回总记录数,总页数和返回的结果集.
		 */
		@Test
		public void executeProcedure_test2() {
			//传入: 表名,每页显示多少条记录,显示第几页.
			String tablename="dept";
			int pagesize=3;
			int pagenow=3;
			//返回: 结果集,共有多少条记录,共有几页.
			ResultSet rs=null;
			int rowCount=0;
			int pageCount=0;
			SQLHelper sqlHelper=new SQLHelper();
			String sql="{call my_paging(?,?,?,?,?,?)}";
			Object[] parameters_in = {tablename,pagesize,pagenow};
			int[] parameters_out = {oracle.jdbc.OracleTypes.CURSOR,
					oracle.jdbc.OracleTypes.INTEGER,oracle.jdbc.OracleTypes.INTEGER};
			sqlHelper.executeProcedure(sql, parameters_in,parameters_out);
			try {
				/*这里是关键所在,java没有接收结果集的get方法,
				 * 所以只能用getObject来接收结果集,
				 * 接收到后需要使用ResultSet强转才可以.*/
				rs=(ResultSet)sqlHelper.getCs().getObject(4);
				//循环取出
				while(rs.next()){
					System.out.println(rs.getString("deptno")+" "+rs.getString("dname"));
				}
				//取出总记录数
				rowCount=sqlHelper.getCs().getInt(5);
				//取出总页数
				pageCount=sqlHelper.getCs().getInt(6);
				System.out.println("共有"+rowCount+"条记录!   "+"共有"+pageCount+"页!");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				sqlHelper.close();
			}
		}
		/**
create or replace package my_pkg is
  type my_cursor is ref cursor;
end;

create or replace procedure my_paging
  (v_in_table in varchar2,v_in_pagesize in number,v_in_pagenow in number,
  v_out_result out my_pkg.my_cursor,v_out_rows out number,v_out_pagecount out number) is
--定义需要的变量
  v_sql varchar2(4000);
  v_sql_select varchar2(4000);
  v_start number;
  v_end number;
--执行代码
begin
--计算v_start和v_end是多少
  v_start:=v_in_pagesize*(v_in_pagenow-1)+1;
  v_end:=v_in_pagesize*v_in_pagenow;
  v_sql:='select t2.* from (select t1.*,rownum rn from (select * from '||v_in_table||') t1 where rownum<='||v_end||') t2 where rn>='||v_start;
--打开游标，让游标指向结果集
  open v_out_result for v_sql;
--查询共有多少条记录
  v_sql_select:='select count(*) from '||v_in_table;
  execute immediate v_sql_select into v_out_rows;
--统计多少页记录
  if mod(v_out_rows,v_in_pagesize)=0 then
    v_out_pagecount:=v_out_rows/v_in_pagesize;
  else
		v_out_pagecount:=v_out_rows/v_in_pagesize+1;
  end if;
end;
		 */
}
