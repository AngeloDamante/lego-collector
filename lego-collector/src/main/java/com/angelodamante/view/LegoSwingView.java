package com.angelodamante.view;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private JList<LegoEntity> listLegos;
	private DefaultListModel<LegoEntity> listLegosModel;
	private JList<KitEntity> listKits;
	private DefaultListModel<KitEntity> listKitsModel;
	private JLabel lblName;
	private JTextField txtName;
	private JButton btnAddKit;
	private JScrollPane scrollPaneLego;
	private JScrollPane scrollPaneKit;

	private LegoController legoController;
	private JButton btnDeleteKit;

	/**
	 * Create the frame.
	 */
	public LegoSwingView() {
		setTitle("Lego Collector");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 910, 618);
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

		scrollPaneLego = new JScrollPane();
		GridBagConstraints gbc_scrollPaneLego = new GridBagConstraints();
		gbc_scrollPaneLego.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneLego.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneLego.gridx = 1;
		gbc_scrollPaneLego.gridy = 4;
		contentPane.add(scrollPaneLego, gbc_scrollPaneLego);

		listLegosModel = new DefaultListModel<>();
		listLegos = new JList<>(listLegosModel);
		listLegos.setName("listLegos");
		listLegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneLego.setViewportView(listLegos);

		scrollPaneKit = new JScrollPane();
		GridBagConstraints gbc_scrollPaneKit = new GridBagConstraints();
		gbc_scrollPaneKit.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneKit.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneKit.gridx = 1;
		gbc_scrollPaneKit.gridy = 6;
		contentPane.add(scrollPaneKit, gbc_scrollPaneKit);

		listKitsModel = new DefaultListModel<>();
		listKits = new JList<>(listKitsModel);
		listKits.addListSelectionListener(listSelectionEvent -> {
			btnDeleteKit.setEnabled(listKits.getSelectedIndex() != -1);
		});
		listKits.setName("listKits");
		listKits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneKit.setViewportView(listKits);
		
		btnDeleteKit = new JButton("Delete Kit");
		btnDeleteKit.setEnabled(false);
		GridBagConstraints gbc_btnDeleteKit = new GridBagConstraints();
		gbc_btnDeleteKit.gridx = 1;
		gbc_btnDeleteKit.gridy = 7;
		btnDeleteKit.setName("btnDeleteKit");
		contentPane.add(btnDeleteKit, gbc_btnDeleteKit);
		btnDeleteKit.addActionListener(e -> legoController.removeKit(listKits.getSelectedValue()));

		

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

	@Override
	public void onDeletedKit(KitEntity kit) {
		listKitsModel.removeElement(kit);
	}

}
