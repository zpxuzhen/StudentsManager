package com.tools.sqlhelper;

import java.sql.*;

import org.junit.Test;


/**
 * ʹ��˵��:
 * @author zpxuz
 * 1.�������ļ�dbinfo.properties����Ӧ���ݿ�.
 * 2.Oracle��Ҫ�����е�use mydb3;ȥ��
 * 3.��������ϣ��rs��������Թ�����������ǰ��������
 *   st=ct.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
 * 	  �����ϣ������ʹ��rs,��rs�ûص���ʼ��  rs.beforeFirst();
 * 4.������select��������,    select ������(����) from ����
 * 
 */
public class SQLHelperTest {
	
		//���������õı�
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
		
		//����SQLHelper���ߵ�executeUpdate
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

		//����SQLHelper���ߵ�executeUpdate2
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
		//�������� ,����д��,��������в�����û��ִ��.��������ע�͵�,�ٴ�ʵ��ʱ,����֮ǰ��sql��ִ����.
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
		
		//����SQLHelper���ߵ�executeQuery
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
		
		//����SQLHelper���ߵ�executeProcedure
		//SQLserverδ����ͨ��,���Ǳ�call�﷨����,��֪����ô����.
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
		//���÷�ҳ�洢����,ֻ��Oracle�ϲ��Թ�
		/**
		 * Ҫ��:���ұ�дһ���洢����,
		 *      Ҫ��������������ÿҳ��ʾ��¼������ǰҳ�������ֶ�(deptno����),
		 *      �����ܼ�¼��,��ҳ���ͷ��صĽ����.
		 */
		@Test
		public void executeProcedure_test2() {
			//����: ����,ÿҳ��ʾ��������¼,��ʾ�ڼ�ҳ.
			String tablename="dept";
			int pagesize=3;
			int pagenow=3;
			//����: �����,���ж�������¼,���м�ҳ.
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
				/*�����ǹؼ�����,javaû�н��ս������get����,
				 * ����ֻ����getObject�����ս����,
				 * ���յ�����Ҫʹ��ResultSetǿת�ſ���.*/
				rs=(ResultSet)sqlHelper.getCs().getObject(4);
				//ѭ��ȡ��
				while(rs.next()){
					System.out.println(rs.getString("deptno")+" "+rs.getString("dname"));
				}
				//ȡ���ܼ�¼��
				rowCount=sqlHelper.getCs().getInt(5);
				//ȡ����ҳ��
				pageCount=sqlHelper.getCs().getInt(6);
				System.out.println("����"+rowCount+"����¼!   "+"����"+pageCount+"ҳ!");
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
--������Ҫ�ı���
  v_sql varchar2(4000);
  v_sql_select varchar2(4000);
  v_start number;
  v_end number;
--ִ�д���
begin
--����v_start��v_end�Ƕ���
  v_start:=v_in_pagesize*(v_in_pagenow-1)+1;
  v_end:=v_in_pagesize*v_in_pagenow;
  v_sql:='select t2.* from (select t1.*,rownum rn from (select * from '||v_in_table||') t1 where rownum<='||v_end||') t2 where rn>='||v_start;
--���α꣬���α�ָ������
  open v_out_result for v_sql;
--��ѯ���ж�������¼
  v_sql_select:='select count(*) from '||v_in_table;
  execute immediate v_sql_select into v_out_rows;
--ͳ�ƶ���ҳ��¼
  if mod(v_out_rows,v_in_pagesize)=0 then
    v_out_pagecount:=v_out_rows/v_in_pagesize;
  else
		v_out_pagecount:=v_out_rows/v_in_pagesize+1;
  end if;
end;
		 */
}
