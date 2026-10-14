/**
 * ************************************************************************
 * * The contents of this file are subject to the MRPL 1.2
 * * (the  "License"),  being   the  Mozilla   Public  License
 * * Version 1.1  with a permitted attribution clause; you may not  use this
 * * file except in compliance with the License. You  may  obtain  a copy of
 * * the License at http://www.floreantpos.org/license.html
 * * Software distributed under the License  is  distributed  on  an "AS IS"
 * * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * * License for the specific  language  governing  rights  and  limitations
 * * under the License.
 * * The Original Code is FLOREANT POS.
 * * The Initial Developer of the Original Code is OROCUBE LLC
 * * All portions are Copyright (C) 2015 OROCUBE LLC
 * * All Rights Reserved.
 * ************************************************************************
 */
package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.config.TerminalConfig;
import com.floreantpos.demo.KitchenDisplayView;
import com.floreantpos.main.Application;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.User;
import com.floreantpos.model.UserPermission;
import com.floreantpos.model.dao.UserDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.views.LoginView;
import com.floreantpos.ui.views.SwitchboardOtherFunctionsView;
import com.floreantpos.ui.views.TableMapView;
import com.floreantpos.ui.views.order.OrderView;
import com.floreantpos.ui.views.order.RootView;

public class PasswordEntryDialog extends OkCancelOptionDialog implements ActionListener {
	private JPasswordField tfPassword;
	private JLabel statusLabel;

	private PosButton btnClear;
	private PosButton btnEnter;
	private boolean autoLogOffMode;
	private User user;
	private User selectedUser;
	private javax.swing.JButton selectedUserBtn;

	public PasswordEntryDialog() {
		super(Application.getPosWindow(), true);
		init();
	}

	public PasswordEntryDialog(Frame parent) {
		super(parent, true);
		init();
	}

	private void init() {
		//setResizable(false);
		btnClear = new PosButton();
		btnClear.setText(Messages.getString("PasswordEntryDialog.11"));

		btnEnter = new PosButton();
		btnEnter.setText("ENTER");

		JPanel contentPane = new JPanel(new BorderLayout(10, 10));
		getContentPanel().add(contentPane);

		JPanel usersPanel = createUsersPanel();
		contentPane.add(usersPanel, BorderLayout.NORTH);

		JPanel inputPanel = createInputPanel();
		contentPane.add(inputPanel, BorderLayout.CENTER);

		JPanel keyboardPanel = createKeyboardPanel();
		contentPane.add(keyboardPanel, BorderLayout.SOUTH);
	}
	
	private JPanel createUsersPanel() {
		JPanel usersPanel = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 10));
		usersPanel.setOpaque(false);
		
		final javax.swing.border.Border defaultBorder = new javax.swing.border.LineBorder(new java.awt.Color(200, 200, 200), 2, true);
		final javax.swing.border.Border selectedBorder = new javax.swing.border.LineBorder(new java.awt.Color(30, 144, 255), 3, true);

		try {
			java.util.List<User> users = UserDAO.getInstance().findAll();
			for (final User u : users) {
				if (!u.isActive()) continue;
				
				final javax.swing.JButton btn = new javax.swing.JButton();
				btn.setFocusable(false);
				btn.setContentAreaFilled(true);
				btn.setOpaque(true);
				btn.setBackground(java.awt.Color.WHITE);
				btn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
				btn.setToolTipText(u.getFirstName() + " (" + (u.getType() != null ? u.getType().getName() : "") + ")");
				btn.setBorder(defaultBorder);
				btn.setPreferredSize(new java.awt.Dimension(100, 100));

				if (u.getAvatarPath() != null && !u.getAvatarPath().isEmpty()) {
					javax.swing.ImageIcon avatarIcon = com.floreantpos.ui.HeaderPanel.getCircularAvatar(u.getAvatarPath(), 90);
					if (avatarIcon != null) {
						btn.setIcon(avatarIcon);
					} else {
						btn.setText(u.getFirstName());
						btn.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 15));
						btn.setBackground(new java.awt.Color(230, 230, 230));
					}
				} else {
					btn.setText(u.getFirstName());
					btn.setFont(new java.awt.Font("Dialog", java.awt.Font.BOLD, 15));
					btn.setBackground(new java.awt.Color(230, 230, 230));
				}
				
				btn.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						if (selectedUser != null && selectedUser.getUserId().equals(u.getUserId())) {
							selectedUser = null;
							btn.setBorder(defaultBorder);
							selectedUserBtn = null;
							tfPassword.setText("");
							statusLabel.setForeground(java.awt.Color.DARK_GRAY);
							statusLabel.setText("");
						} else {
							if (selectedUserBtn != null) {
								selectedUserBtn.setBorder(defaultBorder);
							}
							selectedUser = u;
							selectedUserBtn = btn;
							btn.setBorder(selectedBorder);
							tfPassword.setText("");
							statusLabel.setForeground(new java.awt.Color(0, 102, 204));
							statusLabel.setText("Enter PIN for " + u.getFirstName());
						}
						tfPassword.requestFocus();
					}
				});
				usersPanel.add(btn);
			}
		} catch (Exception ex) {
			org.apache.commons.logging.LogFactory.getLog(PasswordEntryDialog.class).error("Error loading users for login", ex);
		}
		
		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(usersPanel);
		scrollPane.setBorder(null);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setPreferredSize(new java.awt.Dimension(600, 130));
		scrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		JPanel wrapper = new JPanel(new BorderLayout());
		wrapper.setOpaque(false);
		wrapper.add(scrollPane, BorderLayout.CENTER);
		return wrapper;
	}

	private JPanel createInputPanel() {
		JPanel inputPanel = new JPanel(new BorderLayout(5, 5));

		tfPassword = new JPasswordField();
		tfPassword.setFont(tfPassword.getFont().deriveFont(Font.BOLD, PosUIManager.getNumberFieldFontSize()));
		tfPassword.setFocusable(true);
		tfPassword.requestFocus();
		tfPassword.setBackground(Color.WHITE);
		tfPassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});
		tfPassword.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					return;
				}
				String secretKey = getPasswordAsString();
				if (secretKey != null && secretKey.length() == TerminalConfig.getDefaultPassLen()) {
					if (checkLogin(secretKey)) {
						setCanceled(false);
						dispose();
					}
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doOk();
				}
			}
		});
		inputPanel.add(tfPassword, BorderLayout.NORTH);

		statusLabel = new JLabel(" ");
		statusLabel.setFont(statusLabel.getFont().deriveFont(Font.BOLD, 14f));
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		inputPanel.add(statusLabel);

		return inputPanel;
	}

	private JPanel createKeyboardPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 5, 5));

		String[][] numbers = { { "7", "8", "9" }, { "4", "5", "6" }, { "1", "2", "3" }, { "0" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$
		String[][] iconNames = new String[][] { { "7.png", "8.png", "9.png" }, { "4.png", "5.png", "6.png" }, { "1.png", "2.png", "3.png" }, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
				{ "0.png" } }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		Dimension size = PosUIManager.getSize_w120_h70();

		for (int i = 0; i < numbers.length; i++) {
			for (int j = 0; j < numbers[i].length; j++) {
				String buttonText = String.valueOf(numbers[i][j]);

				PosButton posButton = new PosButton();
				posButton.setAction(loginAction);
				posButton.setFocusable(false);
				ImageIcon icon = com.floreantpos.IconFactory.getIcon("/ui_icons/", iconNames[i][j]); //$NON-NLS-1$
				if (icon != null) {
					posButton.setIcon(icon);
				}
				else {
					posButton.setText(buttonText);
				}

				posButton.setPreferredSize(size);
				posButton.setIconTextGap(0);
				posButton.setActionCommand(buttonText);
				buttonPanel.add(posButton);
			}
		}
		ImageIcon clearIcon = com.floreantpos.IconFactory.getIcon("/ui_icons/", "clear.png"); //$NON-NLS-1$
		if (clearIcon != null) {
			btnClear.setIcon(clearIcon);
		}
		btnClear.setText("CLEAR");
		btnClear.setIconTextGap(0);
		btnClear.setPreferredSize(size);
		btnClear.setFocusable(false);

		ImageIcon enterIcon = com.floreantpos.IconFactory.getIcon("/ui_icons/", "log_in.png"); //$NON-NLS-1$
		if (enterIcon != null) {
			btnEnter.setIcon(enterIcon);
		}
		btnEnter.setText("ENTER");
		btnEnter.setIconTextGap(0);
		btnEnter.setPreferredSize(size);
		btnEnter.setFocusable(false);

		buttonPanel.add(btnClear);
		buttonPanel.add(btnEnter);

		btnClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doClear();
			}
		});

		btnEnter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});

		return buttonPanel;
	}

	@Override
	public void doOk() {
		if (!isVisible()) return;
		String secretKey = getPasswordAsString();
		if (StringUtils.isEmpty(secretKey)) {
			statusLabel.setForeground(Color.RED);
			statusLabel.setText(Messages.getString("PasswordEntryDialog.30"));
			return;
		}
		if (checkLogin(secretKey)) {
			setCanceled(false);
			dispose();
		}
	}

	@Override
	public void doCancel() {
		user = null;
		selectedUser = null;
		selectedUserBtn = null;
		setCanceled(true);
		dispose();
	}

	private void doClearAll() {
		statusLabel.setText(""); //$NON-NLS-1$
		tfPassword.setText(""); //$NON-NLS-1$
	}

	private void doClear() {
		statusLabel.setText(""); //$NON-NLS-1$
		String passwordAsString = getPasswordAsString();
		if (StringUtils.isNotEmpty(passwordAsString)) {
			passwordAsString = passwordAsString.substring(0, passwordAsString.length() - 1);
		}
		tfPassword.setText(passwordAsString);
	}

	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		if (POSConstants.CANCEL.equalsIgnoreCase(actionCommand)) {
			doCancel();
		}
		else if (POSConstants.OK.equalsIgnoreCase(actionCommand)) {
			doOk();
		}
		else {
			if (StringUtils.isNotEmpty(actionCommand)) {
				tfPassword.setText(getPasswordAsString() + actionCommand);
			}
		}
	}

	public void setTitle(String title) {
		super.setTitlePaneText(title);

		super.setTitle(title);
	}

	public void setDialogTitle(String title) {
		super.setTitle(title);
	}

	private String getPasswordAsString() {
		return new String(tfPassword.getPassword());
	}

	public static void main(String[] args) {
		PasswordEntryDialog dialog2 = new PasswordEntryDialog();
		dialog2.pack();
		dialog2.setVisible(true);
	}

	public static String show(Component parent, String title) {
		PasswordEntryDialog dialog2 = new PasswordEntryDialog();
		dialog2.setTitle(title);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setVisible(true);

		if (dialog2.isCanceled()) {
			return null;
		}

		return dialog2.getPasswordAsString();
	}

	public static User getUser(Component parent, String title) {
		return getUser(parent, title, title);
	}

	public static User getUser(Component parent, String windowTitle, String title) {
		PasswordEntryDialog dialog2 = new PasswordEntryDialog();
		dialog2.setTitle(title);
		dialog2.setDialogTitle(windowTitle);
		dialog2.pack();
		dialog2.setLocationRelativeTo(parent);
		dialog2.setVisible(true);
		if (dialog2.isCanceled()) {
			return null;
		}

		return dialog2.getUser();
	}

	private synchronized boolean checkLogin(String secretKey) {
		if (StringUtils.isEmpty(secretKey)) {
			statusLabel.setForeground(Color.RED);
			statusLabel.setText(Messages.getString("PasswordEntryDialog.30"));
			tfPassword.setText("");
			return false;
		}

		if (selectedUser != null) {
			String userPass = selectedUser.getPassword();
			String userIdStr = String.valueOf(selectedUser.getUserId());
			if (secretKey.equals(userPass) || secretKey.equals(userIdStr)) {
				user = selectedUser;
			} else {
				statusLabel.setForeground(Color.RED);
				statusLabel.setText("Incorrect PIN for " + selectedUser.getFirstName());
				tfPassword.setText("");
				return false;
			}
		} else {
			user = UserDAO.getInstance().findUserBySecretKey(secretKey);
		}

		if (user == null) {
			statusLabel.setForeground(Color.RED);
			statusLabel.setText(Messages.getString("PasswordEntryDialog.30")); //$NON-NLS-1$
			tfPassword.setText("");
			return false;
		}

		if (!user.isActive()) {
			statusLabel.setForeground(Color.RED);
			statusLabel.setText("User account is inactive");
			tfPassword.setText("");
			return false;
		}

		if (LoginView.getInstance().isBackOfficeLogin()) {
			if (!user.hasPermission(UserPermission.VIEW_BACK_OFFICE)) {
				statusLabel.setForeground(Color.RED);
				statusLabel.setText("user has no permission to access this view");
				tfPassword.setText("");
				return false;
			}
			return true;
		}

		if (isAutoLogOffMode()) {

			String viewName = RootView.getInstance().getCurrentViewName();

			if (viewName.equals(TableMapView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.CREATE_TICKET)) {
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("user has no permission to access this view");
					tfPassword.setText("");
					return false;
				}
			}
			else if (viewName.equals(SwitchboardOtherFunctionsView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.ALL_FUNCTIONS)) {
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("user has no permission to access this view");
					tfPassword.setText("");
					return false;
				}
			}
			else if (viewName.equals(OrderView.VIEW_NAME)) {
				if (!OrderView.getInstance().getCurrentTicket().getOwner().getUserId().equals(user.getUserId())) {
					if (!user.hasPermission(UserPermission.CREATE_TICKET)
							|| (!user.hasPermission(UserPermission.PERFORM_ADMINISTRATIVE_TASK) && !user.hasPermission(UserPermission.PERFORM_MANAGER_TASK))) {
						statusLabel.setForeground(Color.RED);
						statusLabel.setText("user has no permission to access this view");
						tfPassword.setText("");
						return false;
					}
				}
			}
			else if (viewName.equals(KitchenDisplayView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.KITCHEN_DISPLAY)) {
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("user has no permission to access this view");
					tfPassword.setText("");
					return false;
				}
			}
		}
		else {// to check login view access 
			List<OrderType> orderTypes = Application.getInstance().getOrderTypes();
			if (orderTypes != null) {
				for (OrderType orderType : orderTypes) {
					if (TerminalConfig.getDefaultView().equals(orderType.getName())) {
						if (!user.hasPermission(UserPermission.CREATE_TICKET)) {
							statusLabel.setForeground(Color.RED);
							statusLabel.setText("user has no permission to access this view");
							tfPassword.setText("");
							return false;
						}
					}
				}
			}
			if (TerminalConfig.getDefaultView().equals(KitchenDisplayView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.KITCHEN_DISPLAY)) {
					statusLabel.setForeground(Color.RED);
					statusLabel.setText("user has no permission to access this view");
					tfPassword.setText("");
					return false;
				}
			}
		}

		setAutoLogOffMode(false);
		return true;
	}

	Action loginAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			tfPassword.setText(getPasswordAsString() + e.getActionCommand());

			String secretKey = getPasswordAsString();
			if (secretKey != null && secretKey.length() == TerminalConfig.getDefaultPassLen()) {
				if (checkLogin(secretKey)) {
					setCanceled(false);
					dispose();
				}
			}
		}
	};

	public User getUser() {
		return user;
	}

	/**
	 * @return the autoLogOffMode
	 */
	public boolean isAutoLogOffMode() {
		return autoLogOffMode;
	}

	/**
	 * @param autoLogOffMode the autoLogOffMode to set
	 */
	public void setAutoLogOffMode(boolean autoLogOffMode) {
		this.autoLogOffMode = autoLogOffMode;
	}
}