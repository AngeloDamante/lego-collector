package com.angelodamante.view;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.angelodamante.controller.LegoController;
import com.angelodamante.model.entities.KitEntity;
import com.angelodamante.model.entities.LegoEntity;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.Color;
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
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class LegoSwingView extends JFrame implements LegoView {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtProductCode;
	private JList<LegoEntity> listLegos;
	private DefaultListModel<LegoEntity> listLegosModel;
	private JList<KitEntity> listKits;
	private DefaultListModel<KitEntity> listKitsModel;
	private JList<LegoEntity> listSearchedLegos;
	private DefaultListModel<LegoEntity> listSearchedLegosModel;
	private JLabel lblName;
	private JTextField txtName;
	private JButton btnAddKit;
	private JScrollPane scrollPaneLego;
	private JScrollPane scrollPaneKit;
	private JScrollPane scrollPaneSearch;

	private LegoController legoController;
	private JButton btnDeleteKit;
	private JButton btnAddLego;
	private JLabel lblProductCodeLego;
	private JLabel lblBudsLego;
	private JLabel lblQuantityLego;
	private JTextField txtProductCodeLego;
	private JTextField txtBudsLego;
	private JTextField txtQuantityLego;
	private JLabel lblErrorLog;
	private JButton btnDeleteLego;
	private JSeparator separatorKits;
	private JTextField txtSearchBuds;
	private JButton btnSearchLegos;
	private JSeparator separator;
	private JTextField txtNewKitProductCode;
	private JTextField txtNewKitName;
	private JButton btnUpdateKit;

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
		gbl_contentPane.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 23, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0, 1.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0, 0, 0, 0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0, 0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		lblErrorLog = new JLabel(" ");
		GridBagConstraints gbc_lblErrorLog = new GridBagConstraints();
		gbc_lblErrorLog.insets = new Insets(0, 0, 5, 5);
		gbc_lblErrorLog.gridx = 1;
		gbc_lblErrorLog.gridy = 0;
		lblErrorLog.setName("lblErrorLog");
		contentPane.add(lblErrorLog, gbc_lblErrorLog);

		JLabel lblProductCode = new JLabel("production code");
		GridBagConstraints gbc_lblProductCode = new GridBagConstraints();
		gbc_lblProductCode.anchor = GridBagConstraints.EAST;
		gbc_lblProductCode.insets = new Insets(0, 0, 5, 5);
		gbc_lblProductCode.gridx = 0;
		gbc_lblProductCode.gridy = 1;
		lblProductCode.setName("lblProductCode");
		contentPane.add(lblProductCode, gbc_lblProductCode);
		KeyAdapter keyAdapterAddBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnAddKit.setEnabled(!txtProductCode.getText().trim().isEmpty() && !txtName.getText().trim().isEmpty());
			}
		};

		txtProductCode = new JTextField();
		GridBagConstraints gbc_txtProductCode = new GridBagConstraints();
		gbc_txtProductCode.insets = new Insets(0, 0, 5, 5);
		gbc_txtProductCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProductCode.gridx = 1;
		gbc_txtProductCode.gridy = 1;
		contentPane.add(txtProductCode, gbc_txtProductCode);
		txtProductCode.setName("txtProductCode");
		txtProductCode.setColumns(10);
		txtProductCode.addKeyListener(keyAdapterAddBtnEnabler);

		KeyAdapter keyAdapterUpdateKitBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateBtnUpdateKitEnabled();
			}
		};

		txtNewKitProductCode = new JTextField();
		GridBagConstraints gbc_txtNewKitProductCode = new GridBagConstraints();
		gbc_txtNewKitProductCode.insets = new Insets(0, 0, 5, 0);
		gbc_txtNewKitProductCode.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNewKitProductCode.gridx = 3;
		gbc_txtNewKitProductCode.gridy = 1;
		txtNewKitProductCode.setName("txtNewKitProductCode");
		contentPane.add(txtNewKitProductCode, gbc_txtNewKitProductCode);
		txtNewKitProductCode.setColumns(10);
		txtNewKitProductCode.addKeyListener(keyAdapterUpdateKitBtnEnabler);

		txtNewKitName = new JTextField();
		GridBagConstraints gbc_txtNewKitName = new GridBagConstraints();
		gbc_txtNewKitName.insets = new Insets(0, 0, 5, 0);
		gbc_txtNewKitName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtNewKitName.gridx = 3;
		gbc_txtNewKitName.gridy = 2;
		txtNewKitName.setName("txtNewKitName");
		contentPane.add(txtNewKitName, gbc_txtNewKitName);
		txtNewKitName.setColumns(10);
		txtNewKitName.addKeyListener(keyAdapterUpdateKitBtnEnabler);

		btnUpdateKit = new JButton("Update Kit");
		btnUpdateKit.setEnabled(false);
		GridBagConstraints gbc_btnUpdateKit = new GridBagConstraints();
		gbc_btnUpdateKit.insets = new Insets(0, 0, 5, 0);
		gbc_btnUpdateKit.gridx = 3;
		gbc_btnUpdateKit.gridy = 3;
		btnUpdateKit.setName("btnUpdateKit");
		contentPane.add(btnUpdateKit, gbc_btnUpdateKit);
		btnUpdateKit.addActionListener(e -> legoController.updateKit(txtNewKitProductCode.getText(),
				txtNewKitName.getText(), listKits.getSelectedValue()));

		txtSearchBuds = new JTextField();
		GridBagConstraints gbc_txtSearchBuds = new GridBagConstraints();
		gbc_txtSearchBuds.insets = new Insets(0, 0, 5, 0);
		gbc_txtSearchBuds.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSearchBuds.gridx = 3;
		gbc_txtSearchBuds.gridy = 10;
		txtSearchBuds.setName("txtSearchBuds");
		contentPane.add(txtSearchBuds, gbc_txtSearchBuds);
		txtSearchBuds.setColumns(10);
		KeyAdapter keyAdapterSearchLegosBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				btnSearchLegos.setEnabled(!txtSearchBuds.getText().trim().isEmpty());
			}
		};
		txtSearchBuds.addKeyListener(keyAdapterSearchLegosBtnEnabler);

		lblName = new JLabel("name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 2;
		lblName.setName("lblName");
		contentPane.add(lblName, gbc_lblName);

		txtName = new JTextField();
		GridBagConstraints gbc_txtName = new GridBagConstraints();
		gbc_txtName.insets = new Insets(0, 0, 5, 5);
		gbc_txtName.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtName.gridx = 1;
		gbc_txtName.gridy = 2;
		contentPane.add(txtName, gbc_txtName);
		txtName.setName("txtName");
		txtName.setColumns(10);
		txtName.addKeyListener(keyAdapterAddBtnEnabler);

		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 2;
		gbc_separator.gridy = 2;
		contentPane.add(separator, gbc_separator);

		btnSearchLegos = new JButton("Search Legos with buds");
		btnSearchLegos.setEnabled(false);
		GridBagConstraints gbc_btnSearchLegos = new GridBagConstraints();
		gbc_btnSearchLegos.insets = new Insets(0, 0, 5, 0);
		gbc_btnSearchLegos.gridx = 3;
		gbc_btnSearchLegos.gridy = 11;
		btnSearchLegos.setName("btnSearchLegos");
		contentPane.add(btnSearchLegos, gbc_btnSearchLegos);
		btnSearchLegos.addActionListener(e -> legoController.legosByBuds(txtSearchBuds.getText()));

		btnAddKit = new JButton("Add Kit");
		btnAddKit.setEnabled(false);
		GridBagConstraints gbc_btnAddKit = new GridBagConstraints();
		gbc_btnAddKit.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddKit.gridx = 1;
		gbc_btnAddKit.gridy = 3;
		btnAddKit.setName("btnAddKit");
		contentPane.add(btnAddKit, gbc_btnAddKit);
		btnAddKit.addActionListener(e -> btnAddKitPressed());

		listKitsModel = new DefaultListModel<>();

		scrollPaneKit = new JScrollPane();
		GridBagConstraints gbc_scrollPaneKit = new GridBagConstraints();
		gbc_scrollPaneKit.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneKit.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneKit.gridx = 1;
		gbc_scrollPaneKit.gridy = 4;
		contentPane.add(scrollPaneKit, gbc_scrollPaneKit);
		listKits = new JList<>(listKitsModel);
		listKits.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!listKits.getValueIsAdjusting()) {
					return;
				}
				btnDeleteKit.setEnabled(listKits.getSelectedIndex() != -1);
				updateAddLegoButtonEnable();
				KitEntity kit = listKits.getSelectedValue();
				if (kit != null) {
					legoController.legosOfKitId(kit.getId());
					txtNewKitProductCode.setText(kit.getProductCode().toString());
					txtNewKitName.setText(kit.getName().toString());
				}
				updateBtnUpdateKitEnabled();
			}
		});
		listKits.setName("listKits");
		listKits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneKit.setViewportView(listKits);

		btnDeleteKit = new JButton("Delete Kit");
		btnDeleteKit.setEnabled(false);
		GridBagConstraints gbc_btnDeleteKit = new GridBagConstraints();
		gbc_btnDeleteKit.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteKit.gridx = 1;
		gbc_btnDeleteKit.gridy = 5;
		btnDeleteKit.setName("btnDeleteKit");
		contentPane.add(btnDeleteKit, gbc_btnDeleteKit);
		btnDeleteKit.addActionListener(e -> legoController.removeKit(listKits.getSelectedValue()));

		listLegosModel = new DefaultListModel<>();

		separatorKits = new JSeparator();
		GridBagConstraints gbc_separatorKits = new GridBagConstraints();
		gbc_separatorKits.insets = new Insets(0, 0, 5, 5);
		gbc_separatorKits.gridx = 1;
		gbc_separatorKits.gridy = 6;
		contentPane.add(separatorKits, gbc_separatorKits);

		lblProductCodeLego = new JLabel("product code");
		GridBagConstraints gbc_lblProductCodeLego = new GridBagConstraints();
		gbc_lblProductCodeLego.anchor = GridBagConstraints.EAST;
		gbc_lblProductCodeLego.insets = new Insets(0, 0, 5, 5);
		gbc_lblProductCodeLego.gridx = 0;
		gbc_lblProductCodeLego.gridy = 7;
		lblProductCodeLego.setName("lblProductCodeLego");
		contentPane.add(lblProductCodeLego, gbc_lblProductCodeLego);
		KeyAdapter keyAdapterAddLegoBtnEnabler = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				updateAddLegoButtonEnable();
			}
		};

		txtProductCodeLego = new JTextField();
		GridBagConstraints gbc_txtProductCodeLego = new GridBagConstraints();
		gbc_txtProductCodeLego.insets = new Insets(0, 0, 5, 5);
		gbc_txtProductCodeLego.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtProductCodeLego.gridx = 1;
		gbc_txtProductCodeLego.gridy = 7;
		contentPane.add(txtProductCodeLego, gbc_txtProductCodeLego);
		txtProductCodeLego.setName("txtProductCodeLego");
		txtProductCodeLego.setColumns(10);
		txtProductCodeLego.addKeyListener(keyAdapterAddLegoBtnEnabler);

		lblBudsLego = new JLabel("buds");
		GridBagConstraints gbc_lblBudsLego = new GridBagConstraints();
		gbc_lblBudsLego.anchor = GridBagConstraints.EAST;
		gbc_lblBudsLego.insets = new Insets(0, 0, 5, 5);
		gbc_lblBudsLego.gridx = 0;
		gbc_lblBudsLego.gridy = 8;
		lblBudsLego.setName("lblBudsLego");
		contentPane.add(lblBudsLego, gbc_lblBudsLego);

		txtBudsLego = new JTextField();
		GridBagConstraints gbc_txtBudsLego = new GridBagConstraints();
		gbc_txtBudsLego.insets = new Insets(0, 0, 5, 5);
		gbc_txtBudsLego.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtBudsLego.gridx = 1;
		gbc_txtBudsLego.gridy = 8;
		contentPane.add(txtBudsLego, gbc_txtBudsLego);
		txtBudsLego.setName("txtBudsLego");
		txtBudsLego.setColumns(10);
		txtBudsLego.addKeyListener(keyAdapterAddLegoBtnEnabler);

		lblQuantityLego = new JLabel("quantity");
		GridBagConstraints gbc_lblQuantityLego = new GridBagConstraints();
		gbc_lblQuantityLego.anchor = GridBagConstraints.EAST;
		gbc_lblQuantityLego.insets = new Insets(0, 0, 5, 5);
		gbc_lblQuantityLego.gridx = 0;
		gbc_lblQuantityLego.gridy = 9;
		lblQuantityLego.setName("lblQuantityLego");
		contentPane.add(lblQuantityLego, gbc_lblQuantityLego);

		txtQuantityLego = new JTextField();
		GridBagConstraints gbc_txtQuantityLego = new GridBagConstraints();
		gbc_txtQuantityLego.insets = new Insets(0, 0, 5, 5);
		gbc_txtQuantityLego.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtQuantityLego.gridx = 1;
		gbc_txtQuantityLego.gridy = 9;
		contentPane.add(txtQuantityLego, gbc_txtQuantityLego);
		txtQuantityLego.setName("txtQuantityLego");
		txtQuantityLego.setColumns(10);
		txtQuantityLego.addKeyListener(keyAdapterAddLegoBtnEnabler);

		btnAddLego = new JButton("Add lego");
		GridBagConstraints gbc_btnAddLego = new GridBagConstraints();
		gbc_btnAddLego.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddLego.gridx = 1;
		gbc_btnAddLego.gridy = 11;
		btnAddLego.setName("btnAddLego");
		contentPane.add(btnAddLego, gbc_btnAddLego);
		btnAddLego.addActionListener(e -> legoController.addLego(txtProductCodeLego.getText(), txtBudsLego.getText(),
				txtQuantityLego.getText(), listKits.getSelectedValue()));

		scrollPaneLego = new JScrollPane();
		GridBagConstraints gbc_scrollPaneLego = new GridBagConstraints();
		gbc_scrollPaneLego.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneLego.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneLego.gridx = 1;
		gbc_scrollPaneLego.gridy = 12;
		contentPane.add(scrollPaneLego, gbc_scrollPaneLego);
		listLegos = new JList<>(listLegosModel);
		listLegos.setName("listLegos");
		listLegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneLego.setViewportView(listLegos);
		listLegos.addListSelectionListener(listSelectionEvent -> {
			if (listLegos.getValueIsAdjusting()) {
				return;
			}
			btnDeleteLego.setEnabled(listLegos.getSelectedIndex() != -1);
		});

		btnDeleteLego = new JButton("Delete Lego");
		btnDeleteLego.setEnabled(false);
		GridBagConstraints gbc_btnDeleteLego = new GridBagConstraints();
		gbc_btnDeleteLego.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteLego.gridx = 1;
		gbc_btnDeleteLego.gridy = 13;
		btnDeleteLego.setName("btnDeleteLego");
		contentPane.add(btnDeleteLego, gbc_btnDeleteLego);
		btnDeleteLego.addActionListener(e -> legoController.removeLego(listLegos.getSelectedValue()));

		listSearchedLegosModel = new DefaultListModel<>();
		scrollPaneSearch = new JScrollPane();
		GridBagConstraints gbc_scrollPaneSearch = new GridBagConstraints();
		gbc_scrollPaneSearch.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPaneSearch.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneSearch.gridx = 3;
		gbc_scrollPaneSearch.gridy = 12;
		contentPane.add(scrollPaneSearch, gbc_scrollPaneSearch);
		listSearchedLegos = new JList<>(listSearchedLegosModel);
		listSearchedLegos.setName("listSearchedKits");
		listSearchedLegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPaneSearch.setViewportView(listSearchedLegos);

	}

	private void updateBtnUpdateKitEnabled() {
		btnUpdateKit.setEnabled(!txtNewKitProductCode.getText().trim().isEmpty()
				&& !txtNewKitName.getText().trim().isEmpty() && listKits.getSelectedIndex() != -1);

	}

	private void updateAddLegoButtonEnable() {
		btnAddLego.setEnabled(
				!txtProductCodeLego.getText().trim().isEmpty() && !txtQuantityLego.getText().trim().isEmpty()
						&& !txtBudsLego.getText().trim().isEmpty() && listKits.getSelectedIndex() != -1);
	}

	private void btnAddKitPressed() {
		legoController.addKit(txtProductCode.getText(), txtName.getText());
	}

	@Override
	public void showAllLegos(List<LegoEntity> legos) {
		listLegosModel.clear();
		legos.stream().forEach(listLegosModel::addElement);
	}

	@Override
	public void showAllKits(List<KitEntity> kits) {
		listKitsModel.clear();
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

	@Override
	public void onAddedLego(LegoEntity legoEntity) {
		listLegosModel.addElement(legoEntity);
	}

	@Override
	public void showError(String message) {
		lblErrorLog.setText(message);
	}

	@Override
	public void onDeletedLego(LegoEntity lego) {
		listLegosModel.removeElement(lego);
	}

	@Override
	public void showAllSearchedLegos(List<LegoEntity> legos) {
		listSearchedLegosModel.clear();
		legos.stream().forEach(listSearchedLegosModel::addElement);
	}
}
