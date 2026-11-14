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
import java.awt.Component;
import java.awt.Dimension;
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
		
		final javax.swing.border.Border defaultBorder = com.floreantpos.ui.POSTheme.createHairlineBorder(4, 4, 4, 4);
		final javax.swing.border.Border selectedBorder = com.floreantpos.ui.POSTheme.createSelectedCardBorder(4, 4, 4, 4);

		try {
			java.util.List<User> users = UserDAO.getInstance().findAll();
			for (final User u : users) {
				if (!u.isActive()) continue;
				
				final javax.swing.JButton btn = new javax.swing.JButton();
				btn.setFocusable(false);
				btn.setContentAreaFilled(true);
				btn.setOpaque(true);
				btn.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
				btn.setForeground(com.floreantpos.ui.POSTheme.TEXT_PRIMARY);
				btn.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
				btn.setToolTipText(u.getFirstName() + " (" + (u.getType() != null ? u.getType().getName() : "") + ")");
				btn.setBorder(defaultBorder);
				btn.putClientProperty(com.formdev.flatlaf.FlatClientProperties.STYLE, "arc: " + com.floreantpos.ui.POSTheme.CORNER_RADIUS); //$NON-NLS-1$
				btn.setPreferredSize(new java.awt.Dimension(100, 100));

				if (u.getAvatarPath() != null && !u.getAvatarPath().isEmpty()) {
					javax.swing.ImageIcon avatarIcon = com.floreantpos.ui.HeaderPanel.getCircularAvatar(u.getAvatarPath(), 84);
					if (avatarIcon != null) {
						btn.setIcon(avatarIcon);
					} else {
						btn.setText(u.getFirstName());
						btn.setFont(com.floreantpos.ui.POSTheme.fontBold(15));
					}
				} else {
					btn.setText(u.getFirstName());
					btn.setFont(com.floreantpos.ui.POSTheme.fontBold(15));
				}
				
				btn.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent e) {
						clearErrorFeedback();
						if (selectedUser != null && selectedUser.getUserId().equals(u.getUserId())) {
							selectedUser = null;
							btn.setBorder(defaultBorder);
							btn.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
							selectedUserBtn = null;
							tfPassword.setText("");
							statusLabel.setForeground(com.floreantpos.ui.POSTheme.TEXT_MUTED);
							statusLabel.setText(" ");
						} else {
							if (selectedUserBtn != null) {
								selectedUserBtn.setBorder(defaultBorder);
								selectedUserBtn.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
							}
							selectedUser = u;
							selectedUserBtn = btn;
							btn.setBorder(selectedBorder);
							btn.setBackground(com.floreantpos.ui.POSTheme.ACCENT_SOFT_BG);
							tfPassword.setText("");
							statusLabel.setForeground(com.floreantpos.ui.POSTheme.ACCENT_PRESSED);
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
		JPanel inputPanel = new JPanel(new BorderLayout(5, 6));
		inputPanel.setOpaque(false);

		tfPassword = new JPasswordField();
		tfPassword.setHorizontalAlignment(JPasswordField.CENTER);
		tfPassword.setPreferredSize(new java.awt.Dimension(0, PosUIManager.getSize(48)));
		com.floreantpos.ui.POSTheme.styleTextField(tfPassword, 24, true);
		tfPassword.putClientProperty(com.formdev.flatlaf.FlatClientProperties.PLACEHOLDER_TEXT, "Enter PIN"); //$NON-NLS-1$
		tfPassword.setFocusable(true);
		tfPassword.requestFocus();
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
				clearErrorFeedback();
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
		statusLabel.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		statusLabel.setForeground(com.floreantpos.ui.POSTheme.TEXT_MUTED);
		statusLabel.setHorizontalAlignment(JLabel.CENTER);
		inputPanel.add(statusLabel);

		return inputPanel;
	}

	private JPanel createKeyboardPanel() {
		JPanel buttonPanel = new JPanel(new GridLayout(0, 3, 6, 6));
		buttonPanel.setOpaque(false);

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
					com.floreantpos.ui.POSTheme.styleSecondaryButton(posButton);
				}
				else {
					posButton.setText(buttonText);
					com.floreantpos.ui.POSTheme.styleKeypadButton(posButton);
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
		btnClear.setIconTextGap(4);
		btnClear.setPreferredSize(size);
		btnClear.setFocusable(false);
		btnClear.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		com.floreantpos.ui.POSTheme.styleNeutralButton(btnClear);

		ImageIcon enterIcon = com.floreantpos.IconFactory.getIcon("/ui_icons/", "log_in.png"); //$NON-NLS-1$
		if (enterIcon != null) {
			btnEnter.setIcon(enterIcon);
		}
		btnEnter.setText("ENTER");
		btnEnter.setIconTextGap(4);
		btnEnter.setPreferredSize(size);
		btnEnter.setFocusable(false);
		btnEnter.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		com.floreantpos.ui.POSTheme.stylePrimaryButton(btnEnter);

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
			failLogin(Messages.getString("PasswordEntryDialog.30"));
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

	private void clearErrorFeedback() {
		tfPassword.putClientProperty(com.formdev.flatlaf.FlatClientProperties.OUTLINE, null);
		if (selectedUser != null) {
			statusLabel.setForeground(com.floreantpos.ui.POSTheme.ACCENT_PRESSED);
			statusLabel.setText("Enter PIN for " + selectedUser.getFirstName());
		}
		else {
			statusLabel.setForeground(com.floreantpos.ui.POSTheme.TEXT_MUTED);
			statusLabel.setText(" "); //$NON-NLS-1$
		}
	}

	private boolean failLogin(String message) {
		tfPassword.putClientProperty(com.formdev.flatlaf.FlatClientProperties.OUTLINE,
				com.formdev.flatlaf.FlatClientProperties.OUTLINE_ERROR);
		statusLabel.setForeground(com.floreantpos.ui.POSTheme.AFRIMONEY_RED);
		statusLabel.setText(message);
		tfPassword.setText(""); //$NON-NLS-1$
		return false;
	}

	private void doClearAll() {
		tfPassword.setText(""); //$NON-NLS-1$
		clearErrorFeedback();
	}

	private void doClear() {
		clearErrorFeedback();
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
			return failLogin(Messages.getString("PasswordEntryDialog.30"));
		}

		if (selectedUser != null) {
			String userPass = selectedUser.getPassword();
			String userIdStr = String.valueOf(selectedUser.getUserId());
			if (secretKey.equals(userPass) || secretKey.equals(userIdStr)) {
				user = selectedUser;
			} else {
				return failLogin("Incorrect PIN for " + selectedUser.getFirstName());
			}
		} else {
			user = UserDAO.getInstance().findUserBySecretKey(secretKey);
		}

		if (user == null) {
			return failLogin(Messages.getString("PasswordEntryDialog.30")); //$NON-NLS-1$
		}

		if (!user.isActive()) {
			return failLogin("User account is inactive");
		}

		if (LoginView.getInstance().isBackOfficeLogin()) {
			if (!user.hasPermission(UserPermission.VIEW_BACK_OFFICE)) {
				return failLogin("User has no permission to access this view");
			}
			return true;
		}

		if (isAutoLogOffMode()) {

			String viewName = RootView.getInstance().getCurrentViewName();

			if (viewName.equals(TableMapView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.CREATE_TICKET)) {
					return failLogin("User has no permission to access this view");
				}
			}
			else if (viewName.equals(SwitchboardOtherFunctionsView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.ALL_FUNCTIONS)) {
					return failLogin("User has no permission to access this view");
				}
			}
			else if (viewName.equals(OrderView.VIEW_NAME)) {
				if (!OrderView.getInstance().getCurrentTicket().getOwner().getUserId().equals(user.getUserId())) {
					if (!user.hasPermission(UserPermission.CREATE_TICKET)
							|| (!user.hasPermission(UserPermission.PERFORM_ADMINISTRATIVE_TASK) && !user.hasPermission(UserPermission.PERFORM_MANAGER_TASK))) {
						return failLogin("User has no permission to access this view");
					}
				}
			}
			else if (viewName.equals(KitchenDisplayView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.KITCHEN_DISPLAY)) {
					return failLogin("User has no permission to access this view");
				}
			}
		}
		else {// to check login view access 
			List<OrderType> orderTypes = Application.getInstance().getOrderTypes();
			if (orderTypes != null) {
				for (OrderType orderType : orderTypes) {
					if (TerminalConfig.getDefaultView().equals(orderType.getName())) {
						if (!user.hasPermission(UserPermission.CREATE_TICKET)) {
							return failLogin("User has no permission to access this view");
						}
					}
				}
			}
			if (TerminalConfig.getDefaultView().equals(KitchenDisplayView.VIEW_NAME)) {
				if (!user.hasPermission(UserPermission.KITCHEN_DISPLAY)) {
					return failLogin("User has no permission to access this view");
				}
			}
		}

		setAutoLogOffMode(false);
		return true;
	}

	Action loginAction = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			clearErrorFeedback();
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