package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
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
		setLayout(new BorderLayout(10, 10));

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("+ QUICK ADD NEW SERVER");
		add(titlePanel, BorderLayout.NORTH);

		JPanel formPanel = new JPanel(new MigLayout("insets 20 30 15 30, fillx", "[120px][grow,fill]", "[]12[]12[]12[]12[]"));

		JLabel lblInstruction = new JLabel("<html><b>Quickly register a new server for immediate table assignment:</b></html>");
		formPanel.add(lblInstruction, "span 2, wrap 10");

		tfFirstName = new FixedLengthTextField(60);
		tfFirstName.setFont(tfFirstName.getFont().deriveFont(13f));
		tfLastName = new FixedLengthTextField(60);
		tfLastName.setFont(tfLastName.getFont().deriveFont(13f));
		tfNickname = new FixedLengthTextField(60);
		tfNickname.setFont(tfNickname.getFont().deriveFont(13f));
		tfPhone = new FixedLengthTextField(30);
		tfPhone.setFont(tfPhone.getFont().deriveFont(13f));

		formPanel.add(new JLabel("First Name *:"));
		formPanel.add(tfFirstName, "height 36!, wrap");

		formPanel.add(new JLabel("Last Name *:"));
		formPanel.add(tfLastName, "height 36!, wrap");

		formPanel.add(new JLabel("Nickname / Tag:"));
		formPanel.add(tfNickname, "height 36!, wrap");
		formPanel.add(new JLabel(""));
		formPanel.add(new JLabel("<html><small style='color:gray;'>Optional - e.g. 'Mohamed K.'</small></html>"), "wrap");

		formPanel.add(new JLabel("Phone Number:"));
		formPanel.add(tfPhone, "height 36!, wrap");

		add(formPanel, BorderLayout.CENTER);

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(new JSeparator(), BorderLayout.NORTH);

		JPanel actionButtonPanel = new JPanel(new MigLayout("insets 10 20 15 20, align center", "[160px]15[120px]", "[50px]"));

		PosButton btnSave = new PosButton("Save & Assign");
		btnSave.setIcon(IconFactory.getIcon("/ui_icons/", "finish.png"));
		btnSave.setBackground(new Color(39, 174, 96));
		btnSave.setForeground(Color.WHITE);
		btnSave.setFont(btnSave.getFont().deriveFont(Font.BOLD, 13f));
		btnSave.setPreferredSize(new Dimension(160, 50));
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doSave();
			}
		});
		actionButtonPanel.add(btnSave, "grow");

		PosButton btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setIcon(IconFactory.getIcon("/ui_icons/", "cancel.png"));
		btnCancel.setPreferredSize(new Dimension(120, 50));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		actionButtonPanel.add(btnCancel, "grow");

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
