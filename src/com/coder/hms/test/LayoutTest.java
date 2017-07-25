package com.coder.hms.test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.coder.hms.usrinterface.CustomAllRooms;

public class LayoutTest extends JFrame {

	/**
	 * 
	 */
	private CustomAllRooms allRooms;
	private static final long serialVersionUID = 1L;

	public LayoutTest() {
		
		allRooms = new CustomAllRooms(48);
		
		this.setTitle("Coder HMS [LayoutTest]");
		setPreferredSize(new Dimension(1000, 800));
		getContentPane().add(allRooms.getWindow(), BorderLayout.CENTER);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				LayoutTest layoutTest = new LayoutTest();
				layoutTest.setVisible(true);
			}
		});
	}
}
