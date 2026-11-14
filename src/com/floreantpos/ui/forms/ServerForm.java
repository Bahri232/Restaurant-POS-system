package com.floreantpos.ui.forms;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		setBackground(com.floreantpos.ui.POSTheme.CANVAS_BG);

		JPanel mainPanel = new JPanel(new MigLayout("insets 18 20 18 20, fillx", "[120px][grow,fill]", "[]12[]12[]12[]12[]"));
		mainPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		mainPanel.setBorder(com.floreantpos.ui.POSTheme.createHairlineBorder());

		JLabel lblInstruction = com.floreantpos.ui.POSTheme.createSectionHeader("Server Details");
		mainPanel.add(lblInstruction, "span 2, wrap 12");

		tfFirstName = new FixedLengthTextField(60);
		com.floreantpos.ui.POSTheme.styleTextField(tfFirstName, 13, false);
		tfLastName = new FixedLengthTextField(60);
		com.floreantpos.ui.POSTheme.styleTextField(tfLastName, 13, false);
		tfNickname = new FixedLengthTextField(60);
		com.floreantpos.ui.POSTheme.styleTextField(tfNickname, 13, false);
		tfNickname.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Optional (e.g. Mohamed K.)"); //$NON-NLS-1$
		tfPhone = new FixedLengthTextField(30);
		com.floreantpos.ui.POSTheme.styleTextField(tfPhone, 13, false);
		tfPhone.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Optional"); //$NON-NLS-1$
		chkActive = new JCheckBox("Active (Appears on cashier server list)");
		chkActive.setOpaque(false);
		chkActive.setFont(com.floreantpos.ui.POSTheme.fontPlain(13));
		chkActive.setForeground(com.floreantpos.ui.POSTheme.TEXT_PRIMARY);
		chkActive.setSelected(true);

		mainPanel.add(new JLabel("First Name *"));
		mainPanel.add(tfFirstName, "height 36!, wrap");

		mainPanel.add(new JLabel("Last Name *"));
		mainPanel.add(tfLastName, "height 36!, wrap");

		mainPanel.add(new JLabel("Nickname"));
		mainPanel.add(tfNickname, "height 36!, wrap");

		mainPanel.add(new JLabel("Phone"));
		mainPanel.add(tfPhone, "height 36!, wrap");

		mainPanel.add(new JLabel("Status"));
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
