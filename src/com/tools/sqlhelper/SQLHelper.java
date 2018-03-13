package com.tools.sqlhelper;

import java.io.*;
import java.sql.*;
import java.util.*;

/**
 * SQL���ݿ⹤����SQLHelper
 * @author zpxuz
 * ˵��:
 * 1.�������ļ�dbinfo.properties�еõ�username/password/driver/url
 *   Ĭ�������ļ�dbinfo.properties���ڹ��̸�Ŀ¼,�����޸ĳ������ı�Ŀ¼
 * 2.ÿ����1��SQLHelper���������1��Connection,����ͨ��getCt()��ȡ��.
 *   ��SQLhelper������ʹ��ʱҪ����close()�ر���Դ.
 * 3.����˵��:
 * 		executeUpdate---֧��1��dml����
 * 		executeUpdate2---֧�ֶ���dml������Ϊ1������������
 * 		executeQuery---֧��1��selec��ѯ����
 * 		executeProcedure---֧�ֵ���1���洢���̻��� (��������)
 *   ��������ĸ�������������,����ͨ��getCt/getPs/getRs/getCs��ȡ
 *   Connection/PreparedStatement/ResultSet/CallableStatement��
 *   �Զ��庯���������.
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
			//ÿ����һ�����󶼻� �õ��µ����� ct
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
		
		//��SQLhelper������ʹ��ʱҪ����close()�ر���Դ.
		public void close(){
			closePS();
			closeCS();
			closeRS();
			closeCT();
		}
		
		//֧��1��dml����
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
		//֧�ֶ���dml������Ϊ1������������
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
		//֧��selec��ѯ����
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
		//֧�ֵ���1���洢���̻���   
		//ֻ��������in��out���˳��.�о�����.������������о���!
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
