package com.stuM.view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.stuM.control.StudentService;
import com.stuM.domain.Student;

public class StuUpdata extends JDialog implements ActionListener{

	/**
	 * 修改界面(增加/更新)
	 */
	private static final long serialVersionUID = 1L;
	//定义需要的控件
	JLabel jl1,jl2,jl3,jl4,jl5,jl6;
	JButton jb1,jb2;
	JTextField jtf1,jtf2,jtf3,jtf4,jtf5,jtf6;
	JPanel jp1,jp2,jp3;
	StudentService studentService =null;
	String view;
	int rowNum;
	
	private void initWin() {
		jl1=new JLabel("学号");
		jl2=new JLabel("名字");
		jl3=new JLabel("性别");
		jl4=new JLabel("年龄");
		jl5=new JLabel("学院");
		jl6=new JLabel("班级");
		
		jtf1=new JTextField();
		jtf2=new JTextField();
		jtf3=new JTextField();
		jtf4=new JTextField();
		jtf5=new JTextField();
		jtf6=new JTextField();
		
		if(view.equals("modify")){
			//初始化数据
			Student student=studentService.getStudent(rowNum);
			jtf1.setText(student.getStuNum());
			jtf2.setText(student.getStuName());
			jtf3.setText(student.getStuSex());
			jtf4.setText(String.valueOf(student.getStuAge()));
			jtf5.setText(student.getStuDept());
			jtf6.setText(student.getStuClass());
			//让jtf1不能被修改
			jtf1.setEditable(false);
		}
		if(view.equals("modify")){
			jb1=new JButton("修改");
		}else if(view.equals("add")){
			jb1=new JButton("添加");
		}
		jb2=new JButton("取消");
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		
		//设置布局
		jp1.setLayout(new GridLayout(6,1));
		jp2.setLayout(new GridLayout(6,1));
		
		//添加组件
		jp1.add(jl1);
		jp1.add(jl2);
		jp1.add(jl3);
		jp1.add(jl4);
		jp1.add(jl5);
		jp1.add(jl6);

		jp2.add(jtf1);
		jp2.add(jtf2);
		jp2.add(jtf3);
		jp2.add(jtf4);
		jp2.add(jtf5);
		jp2.add(jtf6);
		
		jp3.add(jb1);
		jp3.add(jb2);
		
		this.add(jp1,BorderLayout.WEST);
		this.add(jp2,BorderLayout.CENTER);
		this.add(jp3,BorderLayout.SOUTH);
		
		//发现如果监听两次jb1则点一下相当于点两下
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		
		//展现
		this.setSize(300,200);
		//这句话不能添加在子窗口
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	//owner他的父窗口
	//title窗口名
	//modal指定是模式窗口(true)还是非模式窗口(false)
	public StuUpdata(Frame owner,String title,boolean modal,StudentService studentService,int rowNum) {
		super(owner,title,modal);//调用父类构造方法
		//修改数据
		this.view = "modify";
		this.studentService = studentService;
		this.rowNum = rowNum;
		initWin();
	}
	public StuUpdata(Frame owner,String title,boolean modal,StudentService studentService) {
		super(owner,title,modal);//调用父类构造方法
		//添加数据
		this.view = "add";
		this.studentService = studentService;
		initWin();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == jb1){
			Student student=new Student(jtf1.getText(),
					jtf2.getText(), jtf3.getText(), 
					Integer.parseInt(jtf4.getText()),
					jtf5.getText(), jtf6.getText());
			if(view.equals("modify")){
				studentService.modify(student);
			}else if(view.equals("add")){
				studentService.add(student);
			}
			//关闭对话框
			this.dispose();		
		}else if(e.getSource() == jb2){
			//关闭对话框
			this.dispose();
		}	
	}

}
