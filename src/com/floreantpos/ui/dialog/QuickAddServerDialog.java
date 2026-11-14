package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
import com.floreantpos.model.Server;
import com.floreantpos.model.dao.ServerDAO;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;

public class QuickAddServerDialog extends POSDialog {
	private FixedLengthTextField tfFirstName;
	private FixedLengthTextField tfLastName;
	private FixedLengthTextField tfNickname;
	private FixedLengthTextField tfPhone;
	private Server createdServer;

	public QuickAddServerDialog(Window parent) {
		super(parent, "Quick Add Server", true);
		initComponents();
	}

	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setBackground(com.floreantpos.ui.POSTheme.CANVAS_BG);
		setLayout(new BorderLayout(0, 0));

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Add New Server");
		add(titlePanel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new MigLayout("insets 16 20 16 20, fillx", "[120px][grow,fill]", "[]12[]12[]12[]"));
		formPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		formPanel.setBorder(com.floreantpos.ui.POSTheme.createHairlineBorder(4, 4, 4, 4));

		tfFirstName = new FixedLengthTextField(60);
		com.floreantpos.ui.POSTheme.styleTextField(tfFirstName, 14, false);
		tfLastName = new FixedLengthTextField(60);
		com.floreantpos.ui.POSTheme.styleTextField(tfLastName, 14, false);
		tfNickname = new FixedLengthTextField(60);
		com.floreantpos.ui.POSTheme.styleTextField(tfNickname, 14, false);
		tfNickname.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Optional (e.g. Mohamed K.)"); //$NON-NLS-1$
		tfPhone = new FixedLengthTextField(30);
		com.floreantpos.ui.POSTheme.styleTextField(tfPhone, 14, false);
		tfPhone.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Optional"); //$NON-NLS-1$

		JLabel lblFirst = new JLabel("First Name *");
		lblFirst.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		lblFirst.setForeground(com.floreantpos.ui.POSTheme.TEXT_SECONDARY);
		formPanel.add(lblFirst);
		formPanel.add(tfFirstName, "height 40!, wrap");

		JLabel lblLast = new JLabel("Last Name *");
		lblLast.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		lblLast.setForeground(com.floreantpos.ui.POSTheme.TEXT_SECONDARY);
		formPanel.add(lblLast);
		formPanel.add(tfLastName, "height 40!, wrap");

		JLabel lblNick = new JLabel("Nickname");
		lblNick.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		lblNick.setForeground(com.floreantpos.ui.POSTheme.TEXT_SECONDARY);
		formPanel.add(lblNick);
		formPanel.add(tfNickname, "height 40!, wrap");

		JLabel lblPhone = new JLabel("Phone");
		lblPhone.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		lblPhone.setForeground(com.floreantpos.ui.POSTheme.TEXT_SECONDARY);
		formPanel.add(lblPhone);
		formPanel.add(tfPhone, "height 40!, wrap");

		JPanel bodyWrapper = new JPanel(new BorderLayout());
		bodyWrapper.setOpaque(false);
		bodyWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16));
		bodyWrapper.add(formPanel, BorderLayout.CENTER);
		add(bodyWrapper, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		bottomPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, com.floreantpos.ui.POSTheme.BORDER_SUBTLE));

		JPanel actionButtonPanel = new JPanel(new MigLayout("insets 12 16 12 16, align right", "[120px]12[160px]", "[46px]"));
		actionButtonPanel.setOpaque(false);

		PosButton btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		btnCancel.setPreferredSize(new Dimension(120, 46));
		com.floreantpos.ui.POSTheme.styleNeutralButton(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		actionButtonPanel.add(btnCancel, "grow");

		PosButton btnSave = new PosButton("Save & Assign");
		btnSave.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		btnSave.setPreferredSize(new Dimension(160, 46));
		com.floreantpos.ui.POSTheme.styleCashButton(btnSave);
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		actionButtonPanel.add(btnSave, "grow");

		bottomPanel.add(actionButtonPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		setResizable(false);
		pack();
		setLocationRelativeTo(getOwner());
	}

	private void doSave() {
		String firstName = tfFirstName.getText();
		if (firstName == null || firstName.trim().isEmpty()) {
			tfFirstName.requestFocus();
			POSMessageDialog.showError(this, "First Name is required.");
			return;
		}

		String lastName = tfLastName.getText();
		if (lastName == null || lastName.trim().isEmpty()) {
			tfLastName.requestFocus();
			POSMessageDialog.showError(this, "Last Name is required.");
			return;
		}

		try {
			Server server = new Server();
			server.setFirstName(firstName.trim());
			server.setLastName(lastName.trim());

			String nickname = tfNickname.getText();
			server.setNickname(nickname != null && !nickname.trim().isEmpty() ? nickname.trim() : null);

			String phone = tfPhone.getText();
			server.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);

			// Automatically active
			server.setActive(true);

			ServerDAO.getInstance().saveOrUpdate(server);

			this.createdServer = server;
			setCanceled(false);
			dispose();
		} catch (Exception e) {
			POSMessageDialog.showError(this, "Failed to save server: " + e.getMessage());
		}
	}

	public Server getCreatedServer() {
		return createdServer;
	}
}
