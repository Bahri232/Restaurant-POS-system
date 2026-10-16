package com.floreantpos.ui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.Server;
import com.floreantpos.model.dao.ServerDAO;
import com.floreantpos.model.util.IllegalModelStateException;
import com.floreantpos.swing.FixedLengthTextField;
import com.floreantpos.ui.BeanEditor;

public class ServerForm extends BeanEditor<Server> {
	private FixedLengthTextField tfFirstName;
	private FixedLengthTextField tfLastName;
	private FixedLengthTextField tfNickname;
	private FixedLengthTextField tfPhone;
	private JCheckBox chkActive;

	public ServerForm() {
		this(new Server());
	}

	public ServerForm(Server server) {
		initComponents();
		setBean(server);
	}

	private void initComponents() {
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new MigLayout("insets 20, fillx", "[140px][grow,fill]", "[]12[]12[]12[]12[]"));

		JLabel lblInstruction = new JLabel("Enter restaurant server details:");
		lblInstruction.setFont(lblInstruction.getFont().deriveFont(Font.BOLD, 13f));
		mainPanel.add(lblInstruction, "span 2, wrap 15");

		tfFirstName = new FixedLengthTextField(60);
		tfLastName = new FixedLengthTextField(60);
		tfNickname = new FixedLengthTextField(60);
		tfPhone = new FixedLengthTextField(30);
		chkActive = new JCheckBox("Active (Server is currently working and appears on cashier order screen)");
		chkActive.setSelected(true);

		mainPanel.add(new JLabel("First Name *:"));
		mainPanel.add(tfFirstName, "wrap");

		mainPanel.add(new JLabel("Last Name *:"));
		mainPanel.add(tfLastName, "wrap");

		mainPanel.add(new JLabel("Nickname / Tag:"));
		mainPanel.add(tfNickname, "wrap");
		JLabel lblNickHint = new JLabel("<html><small style='color:gray;'>Optional - e.g., 'Mohamed K.' to tell apart servers with the same name</small></html>");
		mainPanel.add(new JLabel(""));
		mainPanel.add(lblNickHint, "wrap");

		mainPanel.add(new JLabel("Phone Number:"));
		mainPanel.add(tfPhone, "wrap");
		JLabel lblPhoneHint = new JLabel("<html><small style='color:gray;'>Optional - for shift contact or payment discrepancy follow-up</small></html>");
		mainPanel.add(new JLabel(""));
		mainPanel.add(lblPhoneHint, "wrap");

		mainPanel.add(new JLabel("Status:"));
		mainPanel.add(chkActive, "wrap");

		add(mainPanel, BorderLayout.CENTER);
	}

	@Override
	public boolean save() {
		try {
			if (!updateModel())
				return false;

			Server server = getBean();
			ServerDAO.getInstance().saveOrUpdate(server);
			return true;
		} catch (IllegalModelStateException e) {
			com.floreantpos.ui.dialog.POSMessageDialog.showError(this, e.getMessage());
			return false;
		} catch (Exception e) {
			com.floreantpos.ui.dialog.POSMessageDialog.showError(this, e.getMessage());
			return false;
		}
	}

	@Override
	protected boolean updateModel() throws IllegalModelStateException {
		String firstName = tfFirstName.getText();
		if (firstName == null || firstName.trim().isEmpty()) {
			tfFirstName.requestFocus();
			throw new IllegalModelStateException("First Name is required.");
		}

		String lastName = tfLastName.getText();
		if (lastName == null || lastName.trim().isEmpty()) {
			tfLastName.requestFocus();
			throw new IllegalModelStateException("Last Name is required.");
		}

		Server server = getBean();
		if (server == null) {
			server = new Server();
		}

		server.setFirstName(firstName.trim());
		server.setLastName(lastName.trim());

		String nickname = tfNickname.getText();
		server.setNickname(nickname != null && !nickname.trim().isEmpty() ? nickname.trim() : null);

		String phone = tfPhone.getText();
		server.setPhone(phone != null && !phone.trim().isEmpty() ? phone.trim() : null);

		server.setActive(chkActive.isSelected());

		setBean(server);
		return true;
	}

	@Override
	protected void updateView() {
		Server server = getBean();
		if (server == null) {
			tfFirstName.setText("");
			tfLastName.setText("");
			tfNickname.setText("");
			tfPhone.setText("");
			chkActive.setSelected(true);
			return;
		}

		tfFirstName.setText(server.getFirstName() != null ? server.getFirstName() : "");
		tfLastName.setText(server.getLastName() != null ? server.getLastName() : "");
		tfNickname.setText(server.getNickname() != null ? server.getNickname() : "");
		tfPhone.setText(server.getPhone() != null ? server.getPhone() : "");
		chkActive.setSelected(server.isActive());
	}

	@Override
	public String getDisplayText() {
		Server server = getBean();
		if (server != null && server.getId() != null) {
			return "Edit Server (" + server.getDisplayName() + ")";
		}
		return "Add New Server";
	}
}
