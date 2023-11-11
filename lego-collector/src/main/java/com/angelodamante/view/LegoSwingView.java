package com.angelodamante.view;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;

import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JButton;

public class LegoSwingView extends JFrame implements LegoView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtProductCode;
	private JList listLegos;
	private DefaultListModel<LegoEntity> listLegosModel;
	private JList listKits;
	private DefaultListModel<KitEntity> listKitsModel;
	private JLabel lblName;
	private JTextField txtName;
	private JButton btnAddKit;
	
	private LegoController legoController;

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
		gbl_contentPane.rowWeights = new double[] { 0, 0, 0, 0, 1.0, 0, 0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblProductCode = new JLabel("production code");
		GridBagConstraints gbc_lblProductCode = new GridBagConstraints();
		gbc_lblProductCode.anchor = GridBagConstraints.EAST;
		gbc_lblProductCode.insets = new Insets(0, 0, 5, 5);
		gbc_lblProductCode.gridx = 0;
		gbc_lblProductCode.gridy = 0;
		lblProductCode.setName("lblProductCode");
		contentPane.add(lblProductCode, gbc_lblProductCode);

		txtProductCode = new JTextField();
		GridBagConstraints gbc_txtProductCode = new GridBagConstraints();
		gbc_txtProductCode.insets = new Insets(0, 0, 5, 0);
		gbc_txtProductCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProductCode.gridx = 1;
		gbc_txtProductCode.gridy = 0;
		contentPane.add(txtProductCode, gbc_txtProductCode);
		txtProductCode.setName("txtProductCode");
		txtProductCode.setColumns(10);
		KeyAdapter keyAdapterAddBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddKit.setEnabled(!txtProductCode.getText().trim().isEmpty() && !txtName.getText().trim().isEmpty());
			}
		};
		txtProductCode.addKeyListener(keyAdapterAddBtnEnabler);

		listLegosModel = new DefaultListModel<>();

		lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 1;
		lblName.setName("lblName");
		contentPane.add(lblName, gbc_lblName);
		
		txtName = new JTextField();
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 0);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 1;
		contentPane.add(txtName, gbc_txtName);
		txtName.setName("txtName");
		txtName.setColumns(10);
		txtName.addKeyListener(keyAdapterAddBtnEnabler);
		
		btnAddKit = new JButton("Add Kit");
		btnAddKit.setEnabled(false);
		GridBagConstraints gbc_btnAddKit = new GridBagConstraints();
		gbc_btnAddKit.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddKit.gridx = 1;
		gbc_btnAddKit.gridy = 2;
		btnAddKit.setName("btnAddKit");
		contentPane.add(btnAddKit, gbc_btnAddKit);
		btnAddKit.addActionListener(e -> btnAddKitPressed());

		listLegos = new JList<>(listLegosModel);
		GridBagConstraints gbc_listLegos = new GridBagConstraints();
		gbc_listLegos.insets = new Insets(0, 0, 5, 0);
		gbc_listLegos.fill = GridBagConstraints.BOTH;
		gbc_listLegos.gridx = 1;
		gbc_listLegos.gridy = 4;
		listLegos.setName("listLegos");
		listLegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(listLegos, gbc_listLegos);

		listKitsModel = new DefaultListModel<>();
		listKits = new JList<>(listKitsModel);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 1;
		gbc_list.gridy = 7;
		listKits.setName("listKits");
		listKits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		contentPane.add(listKits, gbc_list);

	}

	private void btnAddKitPressed() {
		legoController.addKit(txtProductCode.getText(), txtName.getText());
	}

	@Override
	public void showAllLegos(List<LegoEntity> legos) {
		legos.stream().forEach(listLegosModel::addElement);
	}

	@Override
	public void showAllKits(List<KitEntity> kits) {
		kits.stream().forEach(listKitsModel::addElement);
	}

	public void setController(LegoController legoController) {
		this.legoController = legoController;
	}

	@Override
	public void onAddedKit(KitEntity kitEntity) {
		listKitsModel.addElement(kitEntity);
	}

}
