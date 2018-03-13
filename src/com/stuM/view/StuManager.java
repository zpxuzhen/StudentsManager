package com.stuM.view;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

import com.stuM.control.StudentService;

public class StuManager  extends JFrame implements ActionListener{
	
	/**
	 * 主界面
	 */
	private static final long serialVersionUID = 1L;
	JTable jt;
	JScrollPane jsp;
	JPanel jp1,jp2;
	JLabel jl1;
	JButton jb1,jb2,jb3,jb4;
	JTextField jtf;
	
	StudentService studentService = null;
	int rowNum;
	
	public StuManager() {
		
		jp1=new JPanel();
		jtf=new JTextField(10);
		jb1=new JButton("查询");
		jb1.addActionListener(this);
//		jb1.setActionCommand("select");
		jl1=new JLabel("请输入名字");
		jp1.add(jl1);
		jp1.add(jtf);
		jp1.add(jb1);
		
		jp2=new JPanel();
		jb2=new JButton("添加");
		jb2.addActionListener(this);
//		jb2.setActionCommand("insert");
		jb3=new JButton("修改");
		jb3.addActionListener(this);
//		jb3.setActionCommand("update");
		jb4=new JButton("删除");
		jb4.addActionListener(this);
//		jb4.setActionCommand("delete");
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		
		//中间的布局
		studentService=new StudentService();
		jt=new JTable(studentService.queryAll());
		jsp=new JScrollPane(jt);
		
		this.add(jp1,BorderLayout.NORTH);
		this.add(jsp,BorderLayout.CENTER);
		this.add(jp2,BorderLayout.SOUTH);
		
		this.setSize(500, 400);
		this.setLocation(300,150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	//		if(e.getActionCommand().equals("select")){
	//		
	//	}else if(e.getActionCommand().equals("insert")){
	//		
	//	}else if(e.getActionCommand().equals("update")){
	//		
	//	}else if(e.getActionCommand().equals("delete")){
	//		
	//	}
		//这种判断方法，只局限于同一个类，能访问到jb1/jb2/jb3/jb4才能用
		if(e.getSource() == jb1){
			studentService=new StudentService();//重新创建一个数据模型
			jt.setModel(studentService.queryAllByName(this.jtf.getText().trim()));
		}else if(e.getSource() == jb2){
			new StuUpdata(this, "添加学生", true,studentService);
			studentService=new StudentService();//重新创建一个数据模型
			jt.setModel(studentService.queryAll());
			
		}else if(e.getSource() == jb3){
			if (IsSelectedRow()) {
				new StuUpdata(this, "修改学生信息", true, studentService, this.rowNum);
				studentService=new StudentService();//重新创建一个数据模型
				jt.setModel(studentService.queryAll());
			}
		}else if(e.getSource() == jb4){
			if (IsSelectedRow()) {
				studentService.delete(this.rowNum);
				studentService=new StudentService();//重新创建一个数据模型
				jt.setModel(studentService.queryAll());
			}
		}
	}	
	//是否选择了一行
	private boolean IsSelectedRow(){
		int rowNum=this.jt.getSelectedRow();
		if(rowNum == -1){
			//提示
			JOptionPane.showMessageDialog(this, "请选择一行");
			return false;
		}
		this.rowNum=rowNum;
		return true;
	}
}
