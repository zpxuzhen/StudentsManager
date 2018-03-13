package com.tools.sqlhelper;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * SQL数据库工具类SQLHelper
 * @author zpxuz
 * 说明:
 * 1.从配置文件dbinfo.properties中得到username/password/driver/url
 *   默认配置文件dbinfo.properties放在工程根目录,可以修改程序来改变目录
 * 2.每创建1个SQLHelper对象会申请1个Connection,可以通过getCt()获取到.
 *   当SQLhelper对象不再使用时要调用close()关闭资源.
 * 3.函数说明:
 * 		executeUpdate---支持1条dml操作
 * 		executeUpdate2---支持多条dml操作作为1个事务来处理
 * 		executeQuery---支持1条selec查询操作
 * 		executeProcedure---支持调用1个存储过程或函数 (并不完美)
 *   如果以上四个函数还不够用,可以通过getCt/getPs/getRs/getCs获取
 *   Connection/PreparedStatement/ResultSet/CallableStatement来
 *   自定义函数完成需求.
 */

public class SQLHelper {

		private  Connection ct=null;
		private  PreparedStatement ps=null;
		private  ResultSet rs=null;
		private  CallableStatement cs=null;
		
		private static String username;
		private static String password;
		private static String driver;
		private static String url;
		
		public Connection getCt() {
			return ct;
		}
		public PreparedStatement getPs() {
			return ps;
		}
		public ResultSet getRs() {
			return rs;
		}
		public CallableStatement getCs() {
			return cs;
		}
		
		private  void getConnection(){
			try {
				this.ct=DriverManager.getConnection(url,username,password);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public SQLHelper() {
			//每申请一个对象都会 得到新的连接 ct
			this.getConnection();
		}
		
		static{
			Properties pp=new Properties();
			FileInputStream fis=null;
			try {
				fis=new FileInputStream("dbinfo.properties");
				pp.load(fis);
				username=(String) pp.getProperty("username");
				password=(String) pp.getProperty("password");
				driver=(String) pp.getProperty("driver");
				url=(String) pp.getProperty("url");
				
				Class.forName(driver);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				try {
					if(fis!=null){
						fis.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				fis=null;
			}
		}
		
		private void closePS(){
			if(ps!=null){
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				ps=null;
			}
		}
		private void closeCS(){
			if(cs!=null){
				try {
					cs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				cs=null;
			}
		}
		private void closeRS(){
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				rs=null;
			}
		}
		private void closeCT(){
			if(ct!=null){
				try {
					ct.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				ct=null;
			}
		}
		
		//当SQLhelper对象不再使用时要调用close()关闭资源.
		public void close(){
			closePS();
			closeCS();
			closeRS();
			closeCT();
		}
		
		//支持1条dml操作
		public void executeUpdate(String sql,String[] parameters){
			try {
				this.ps=this.ct.prepareStatement(sql);
				if(parameters!=null){
					for(int i=0;i<parameters.length;i++){
						this.ps.setString(i+1, parameters[i]);
					}
				}
				this.ps.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}finally{
				closePS();
			}
		}
		//支持多条dml操作作为1个事务来处理
		public void executeUpdate2(String[] sql,String[][] parameters){
			try {
				this.ct.setAutoCommit(false); 
				if(parameters!=null){
					for(int i=0;i<sql.length;i++){
						this.ps=this.ct.prepareStatement(sql[i]);
							for(int j=0;j<parameters[i].length;j++){
								this.ps.setString(j+1, parameters[i][j]);
							}
							this.ps.executeUpdate();
					}
				}else{
					for(int i=0;i<sql.length;i++){
						this.ps=this.ct.prepareStatement(sql[i]);
						this.ps.executeUpdate();
					}
				}
				this.ct.commit();
			} catch (Exception e) {
				e.printStackTrace();
				try {
					this.ct.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				throw new RuntimeException(e.getMessage());
			}finally{
				closePS();
			}
		}
		//支持selec查询操作
		public ResultSet executeQuery(String sql,String[] parameters){
			try {
				this.ps=this.ct.prepareStatement(sql);
				if(parameters!=null){
					for(int i=0;i<parameters.length;i++){
						this.ps.setString(i+1, parameters[i]);
					}
				}
				this.rs=this.ps.executeQuery();
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
			return this.rs;
		}
		//支持调用1个存储过程或函数   
		//只适用于先in再out这个顺序.有局限性.具体问题具体研究吧!
		public void executeProcedure(String sql,Object [] parameters_in,int [] parameters_out){
			int index=0;
			try {
				this.ct.setAutoCommit(false); 
				this.cs=this.ct.prepareCall(sql);
				if(parameters_in!=null){
					for(int i=0;i<parameters_in.length;i++){
						index++;
						this.cs.setString(index, parameters_in[i].toString());
					}
				}
				if(parameters_out!=null){
					for(int i=0;i<parameters_out.length;i++){
						index++;
						this.cs.registerOutParameter(index, parameters_out[i]);
					}
				}
				this.cs.execute();
				this.ct.commit();
			} catch (Exception e) {
				try {
					this.ct.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
					throw new RuntimeException(e.getMessage());
				}
				throw new RuntimeException(e.getMessage());
			}
		}
}
