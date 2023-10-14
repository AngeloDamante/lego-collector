package com.angelodamante.app.view;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.angelodamante.app.model.LegoEntity;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;

public class LegoSwingView extends JFrame implements LegoView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtName;
	private JList listLegos;
	private DefaultListModel<LegoEntity> listLegosModel;

	/**
	 * Create the frame.
	 */
	public LegoSwingView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0, 0, 0, 0, 1.0, 0, 0, 0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 0;
		contentPane.add(txtName, gbc_txtName);
		txtName.setName("txtName");
		txtName.setColumns(10);

		listLegosModel = new DefaultListModel<>();
		listLegos = new JList<>(listLegosModel);
		GridBagConstraints gbc_listLegos = new GridBagConstraints();
		gbc_listLegos.insets = new Insets(0, 0, 5, 0);
		gbc_listLegos.fill = GridBagConstraints.BOTH;
		gbc_listLegos.gridx = 1;
		gbc_listLegos.gridy = 4;
		listLegos.setName("listLegos");
		listLegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(listLegos, gbc_listLegos);

	}

	@Override
	public void showAllLegos(List<LegoEntity> legos) {
		legos.stream().forEach(listLegosModel::addElement);
	}

}
