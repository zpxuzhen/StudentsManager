package com.stuM.view;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

import com.stuM.control.StudentService;

public class StuManager  extends JFrame implements ActionListener{
	
	/**
	 * ������
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
		jb1=new JButton("��ѯ");
		jb1.addActionListener(this);
//		jb1.setActionCommand("select");
		jl1=new JLabel("����������");
		jp1.add(jl1);
		jp1.add(jtf);
		jp1.add(jb1);
		
		jp2=new JPanel();
		jb2=new JButton("���");
		jb2.addActionListener(this);
//		jb2.setActionCommand("insert");
		jb3=new JButton("�޸�");
		jb3.addActionListener(this);
//		jb3.setActionCommand("update");
		jb4=new JButton("ɾ��");
		jb4.addActionListener(this);
//		jb4.setActionCommand("delete");
		jp2.add(jb2);
		jp2.add(jb3);
		jp2.add(jb4);
		
		//�м�Ĳ���
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
		//�����жϷ�����ֻ������ͬһ���࣬�ܷ��ʵ�jb1/jb2/jb3/jb4������
		if(e.getSource() == jb1){
			studentService=new StudentService();//���´���һ������ģ��
			jt.setModel(studentService.queryAllByName(this.jtf.getText().trim()));
		}else if(e.getSource() == jb2){
			new StuUpdata(this, "���ѧ��", true,studentService);
			studentService=new StudentService();//���´���һ������ģ��
			jt.setModel(studentService.queryAll());
			
		}else if(e.getSource() == jb3){
			if (IsSelectedRow()) {
				new StuUpdata(this, "�޸�ѧ����Ϣ", true, studentService, this.rowNum);
				studentService=new StudentService();//���´���һ������ģ��
				jt.setModel(studentService.queryAll());
			}
		}else if(e.getSource() == jb4){
			if (IsSelectedRow()) {
				studentService.delete(this.rowNum);
				studentService=new StudentService();//���´���һ������ģ��
				jt.setModel(studentService.queryAll());
			}
		}
	}	
	//�Ƿ�ѡ����һ��
	private boolean IsSelectedRow(){
		int rowNum=this.jt.getSelectedRow();
		if(rowNum == -1){
			//��ʾ
			JOptionPane.showMessageDialog(this, "��ѡ��һ��");
			return false;
		}
		this.rowNum=rowNum;
		return true;
	}
}
