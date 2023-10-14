package com.angelodamante.app.launcher;

import java.awt.EventQueue;

import com.angelodamante.app.view.LegoSwingView;

public class LegoApp {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LegoSwingView frame = new LegoSwingView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
