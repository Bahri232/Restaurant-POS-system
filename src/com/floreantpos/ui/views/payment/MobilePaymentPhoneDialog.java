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
package com.floreantpos.ui.views.payment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.model.PaymentType;
import com.floreantpos.swing.PosButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.POSTheme;
import com.floreantpos.ui.dialog.POSDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.util.CurrencyUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.POSUtil;
import com.formdev.flatlaf.FlatClientProperties;

public class MobilePaymentPhoneDialog extends POSDialog implements ActionListener {

	private PaymentType paymentType;
	private double amount;
	private String phoneNumber;

	private JTextField tfPhone;
	private PosButton btnConfirm;
	private PosButton btnCancel;
	private PosButton btnClear;
	private PosButton btnBackspace;

	public MobilePaymentPhoneDialog(Window owner, PaymentType paymentType, double amount) {
		super(owner != null ? owner : POSUtil.getFocusedWindow(), paymentType.getDisplayString() + " Payment", true);
		this.paymentType = paymentType;
		this.amount = amount;

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		createUI();
	}

	private void createUI() {
		setLayout(new BorderLayout(0, 0));
		getContentPane().setBackground(POSTheme.CANVAS_BG);

		boolean isOrange = (paymentType == PaymentType.ORANGE_MONEY);
		Color brandColor = isOrange ? POSTheme.ORANGE_MONEY : POSTheme.AFRIMONEY_RED;
		String brandTitle = isOrange ? "Orange Money Payment" : "Afrimoney Payment";
		String brandSub = isOrange ? "Customer Orange Money Checkout" : "Customer Afrimoney Checkout";

		// 1. Clean Elevated Header with restrained brand accent stripe
		JPanel headerPanel = new JPanel(new BorderLayout(12, 0));
		headerPanel.setBackground(POSTheme.CARD_BG);
		headerPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, POSTheme.BORDER_SUBTLE),
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 4, 0, 0, brandColor),
						BorderFactory.createEmptyBorder(14, 14, 14, 18))));

		JPanel titleStack = new JPanel(new BorderLayout(0, 2));
		titleStack.setOpaque(false);
		JLabel lblTitle = new JLabel(brandTitle);
		lblTitle.setFont(POSTheme.fontBold(16));
		lblTitle.setForeground(POSTheme.TEXT_PRIMARY);
		JLabel lblSubtitle = new JLabel(brandSub);
		lblSubtitle.setFont(POSTheme.fontPlain(12));
		lblSubtitle.setForeground(POSTheme.TEXT_MUTED);
		titleStack.add(lblTitle, BorderLayout.NORTH);
		titleStack.add(lblSubtitle, BorderLayout.SOUTH);
		headerPanel.add(titleStack, BorderLayout.WEST);

		JPanel amountStack = new JPanel(new BorderLayout(0, 2));
		amountStack.setOpaque(false);
		JLabel lblAmountHint = new JLabel("Amount Due", SwingConstants.RIGHT);
		lblAmountHint.setFont(POSTheme.fontPlain(11));
		lblAmountHint.setForeground(POSTheme.TEXT_MUTED);
		JLabel lblAmount = new JLabel(CurrencyUtil.getCurrencySymbol() + " " + NumberUtil.formatNumber(amount), SwingConstants.RIGHT);
		lblAmount.setFont(POSTheme.fontBold(18));
		lblAmount.setForeground(brandColor);
		amountStack.add(lblAmountHint, BorderLayout.NORTH);
		amountStack.add(lblAmount, BorderLayout.SOUTH);
		headerPanel.add(amountStack, BorderLayout.EAST);

		add(headerPanel, BorderLayout.NORTH);

		// 2. Center Content Panel (Input + Touch Keypad)
		JPanel centerPanel = new JPanel(new MigLayout("fillx, insets 16", "[grow]", "[]8[]12[]10[]"));
		centerPanel.setBackground(POSTheme.CANVAS_BG);

		JLabel lblPrompt = new JLabel("Customer Phone Number");
		lblPrompt.setFont(POSTheme.fontBold(13));
		lblPrompt.setForeground(POSTheme.TEXT_MUTED);
		centerPanel.add(lblPrompt, "wrap");

		tfPhone = new JTextField();
		POSTheme.styleTextField(tfPhone, 22, true);
		tfPhone.setFont(POSTheme.monoBold(22));
		tfPhone.setHorizontalAlignment(JTextField.CENTER);
		tfPhone.setPreferredSize(new Dimension(0, 50));
		tfPhone.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter customer phone number");

		tfPhone.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					doConfirm();
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					doCancel();
				}
			}
		});
		centerPanel.add(tfPhone, "growx, wrap");

		// Touchscreen Number Keypad (1-9, +, 0, Backspace)
		JPanel keypadPanel = new JPanel(new GridLayout(4, 3, 8, 8));
		keypadPanel.setOpaque(false);

		String[] keys = {
			"1", "2", "3",
			"4", "5", "6",
			"7", "8", "9",
			"+", "0", "\u232B" // ⌫ backspace
		};

		for (String key : keys) {
			PosButton btnKey = new PosButton(key);
			POSTheme.styleKeypadButton(btnKey);
			btnKey.setFont(POSTheme.fontBold(19));
			btnKey.setPreferredSize(new Dimension(80, 48));
			btnKey.setActionCommand(key);
			btnKey.addActionListener(this);
			keypadPanel.add(btnKey);
		}
		centerPanel.add(keypadPanel, "growx, wrap");

		// Quick Clear Button
		btnClear = new PosButton("CLEAR NUMBER");
		POSTheme.styleNeutralButton(btnClear);
		btnClear.setFont(POSTheme.fontBold(12));
		btnClear.setPreferredSize(new Dimension(0, 38));
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tfPhone.setText("");
				tfPhone.requestFocus();
			}
		});
		centerPanel.add(btnClear, "growx, wrap");

		add(centerPanel, BorderLayout.CENTER);

		// 3. Bottom Action Buttons (Confirm & Cancel)
		JPanel footerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		footerPanel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, POSTheme.BORDER_SUBTLE),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));
		footerPanel.setBackground(POSTheme.CARD_BG);

		btnConfirm = new PosButton("CONFIRM PAYMENT");
		POSTheme.styleCashButton(btnConfirm);
		btnConfirm.setFont(POSTheme.fontBold(14));
		btnConfirm.setPreferredSize(new Dimension(0, 48));
		btnConfirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doConfirm();
			}
		});
		footerPanel.add(btnConfirm);

		btnCancel = new PosButton("CANCEL");
		POSTheme.styleNeutralButton(btnCancel);
		btnCancel.setFont(POSTheme.fontBold(14));
		btnCancel.setPreferredSize(new Dimension(0, 48));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		footerPanel.add(btnCancel);

		add(footerPanel, BorderLayout.SOUTH);

		pack();
		setSize(PosUIManager.getSize(460, 540));
		setLocationRelativeTo(getOwner());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if ("\u232B".equals(cmd)) { // Backspace
			String current = tfPhone.getText();
			if (current != null && current.length() > 0) {
				tfPhone.setText(current.substring(0, current.length() - 1));
			}
		}
		else {
			tfPhone.setText(tfPhone.getText() + cmd);
		}
		tfPhone.requestFocus();
	}

	private void doConfirm() {
		String phone = tfPhone.getText().trim();
		if (phone.isEmpty()) {
			POSMessageDialog.showError(this, "Please enter customer's payment phone number.");
			tfPhone.requestFocus();
			return;
		}

		String digits = phone.replaceAll("[^0-9]", "");
		if (digits.length() < 6) {
			POSMessageDialog.showError(this, "Please enter a valid phone number (at least 6 digits).");
			tfPhone.requestFocus();
			return;
		}

		this.phoneNumber = phone;
		setCanceled(false);
		dispose();
	}

	private void doCancel() {
		setCanceled(true);
		dispose();
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	@Override
	public void open() {
		setCanceled(true);
		if (tfPhone != null) {
			tfPhone.requestFocus();
		}
		super.open();
	}
}
