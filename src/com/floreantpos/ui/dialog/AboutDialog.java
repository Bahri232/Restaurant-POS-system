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
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.POSUtil;
import com.orocube.common.util.TerminalUtil;

public class AboutDialog extends POSDialog {

	public AboutDialog() {
		super(POSUtil.getFocusedWindow(), Messages.getString("AboutDialog.0")); //$NON-NLS-1$
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (Application.getApplicationIcon() != null) {
			setIconImage(Application.getApplicationIcon().getImage());
		}
	}

	@Override
	protected void initUI() {
		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 16 18 14 18", "[grow, fill]", "[]10[]10[]"));
		mainPanel.setBackground(com.floreantpos.ui.POSTheme.CANVAS_BG);

		// Header: Logo & Reness POS title
		JPanel headerPanel = new JPanel(new MigLayout("insets 0, fillx", "[][grow]", "[]"));
		headerPanel.setOpaque(false);

		JLabel logoLabel = new JLabel();
		ImageIcon rawLogo = IconFactory.getIcon("/icons/", "fp_logo128x128.png");
		if (rawLogo != null && rawLogo.getImage() != null) {
			logoLabel.setIcon(new ImageIcon(rawLogo.getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH)));
		}
		headerPanel.add(logoLabel, "cell 0 0, aligny center");

		String ff = com.floreantpos.ui.POSTheme.getFontFamily();
		JLabel titleLabel = new JLabel("<html><div style='font-family:" + ff + ";'>"
				+ "<div style='font-size:18px; font-weight:bold; color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_PRIMARY + ";'>Reness POS</div>"
				+ "<div style='font-size:11px; color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_MUTED + "; margin-top:2px;'>Professional Restaurant &amp; Retail POS &bull; <b style='color:" + com.floreantpos.ui.POSTheme.HEX_ACCENT_PRESSED + ";'>Version " + Application.VERSION + "</b></div>"
				+ "</div></html>");
		headerPanel.add(titleLabel, "cell 1 0, gapleft 12, aligny center");
		mainPanel.add(headerPanel, "wrap, growx");

		// Developer Card Panel
		JPanel devCard = new JPanel(new MigLayout("insets 12 14 12 14, fillx", "[][grow]", "[]"));
		devCard.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		devCard.setBorder(com.floreantpos.ui.POSTheme.createHairlineBorder());

		// Developer Photo (Crisp 75x75)
		JLabel photoLabel = new JLabel();
		ImageIcon devIcon = IconFactory.getIcon("/icons/", "developer.jpg");
		if (devIcon != null && devIcon.getImage() != null) {
			Image scaled = devIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
			photoLabel.setIcon(new ImageIcon(scaled));
		}
		photoLabel.setBorder(com.floreantpos.ui.POSTheme.createHairlineBorder());
		devCard.add(photoLabel, "cell 0 0, aligny top");

		// Developer Details
		String devHtml = "<html><div style='font-family:" + ff + "; font-size:11px; color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_SECONDARY + ";'>"
				+ "<div style='font-size:13px; margin-bottom:4px;'><b style='color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_PRIMARY + ";'>Software Developer:</b> <span style='color:" + com.floreantpos.ui.POSTheme.HEX_ACCENT_PRESSED + "; font-weight:bold;'>Abdul Malik Barrie (BAHRI)</span></div>"
				+ "<div style='margin-bottom:2px;'><b>Direct Line:</b> <b style='color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_PRIMARY + ";'>+232 80 020097</b> / <b style='color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_PRIMARY + ";'>+232 76 159189</b></div>"
				+ "<div style='margin-bottom:4px;'><b>WhatsApp:</b> <b style='color:" + com.floreantpos.ui.POSTheme.HEX_CASH_GREEN + ";'>+232 80 020097</b> &nbsp;&bull;&nbsp; <b>Email:</b> <span style='color:" + com.floreantpos.ui.POSTheme.HEX_TEXT_PRIMARY + ";'>abdulmalikbarrie@gmail.com</span></div>"
				+ "<div style='color:" + com.floreantpos.ui.POSTheme.HEX_CASH_GREEN + "; font-weight:bold;'>24/7 Technical Support &amp; System Maintenance Available</div>"
				+ "</div></html>";
		JLabel devDetails = new JLabel(devHtml);
		devCard.add(devDetails, "cell 1 0, gapleft 14, growx");
		mainPanel.add(devCard, "wrap, growx");

		// System Terminal UID Section
		JPanel terminalPanel = new JPanel(new MigLayout("insets 10 14 10 14, fillx", "[][grow][]", "[]"));
		terminalPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		terminalPanel.setBorder(com.floreantpos.ui.POSTheme.createHairlineBorder());
		JLabel lblUid = new JLabel("Terminal Key (System UID):");
		lblUid.setFont(com.floreantpos.ui.POSTheme.fontBold(12));
		lblUid.setForeground(com.floreantpos.ui.POSTheme.TEXT_SECONDARY);
		terminalPanel.add(lblUid, "cell 0 0");

		final JTextField tfTerminalKey = new JTextField(TerminalUtil.getSystemUID());
		tfTerminalKey.setEditable(false);
		tfTerminalKey.setHorizontalAlignment(JTextField.CENTER);
		tfTerminalKey.setFont(com.floreantpos.ui.POSTheme.monoBold(12));
		tfTerminalKey.setBackground(com.floreantpos.ui.POSTheme.SUBTLE_SURFACE);
		tfTerminalKey.setForeground(com.floreantpos.ui.POSTheme.TEXT_PRIMARY);
		tfTerminalKey.setPreferredSize(new Dimension(220, 32));
		terminalPanel.add(tfTerminalKey, "cell 1 0, growx, gapleft 8, gapright 8");

		final JButton btnCopy = new JButton("Copy");
		btnCopy.setFont(com.floreantpos.ui.POSTheme.fontBold(12));
		btnCopy.setPreferredSize(new Dimension(80, 32));
		com.floreantpos.ui.POSTheme.styleSecondaryButton(btnCopy);
		btnCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(tfTerminalKey.getText());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				btnCopy.setText("Copied!");
			}
		});
		terminalPanel.add(btnCopy, "cell 2 0");
		mainPanel.add(terminalPanel, "wrap, growx");

		add(mainPanel, BorderLayout.CENTER);

		// Fixed Footer Panel with OK/Close Button
		JPanel footerPanel = new JPanel(new MigLayout("fillx, insets 10 16 12 16", "[grow]", "[]"));
		footerPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, com.floreantpos.ui.POSTheme.BORDER_SUBTLE));
		JButton btnOk = new JButton(Messages.getString("AboutDialog.5"));
		btnOk.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		btnOk.setPreferredSize(PosUIManager.getSize(120, 38));
		com.floreantpos.ui.POSTheme.stylePrimaryButton(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		footerPanel.add(btnOk, "center");
		add(footerPanel, BorderLayout.SOUTH);
	}

	@Override
	public void open() {
		pack();
		setLocationRelativeTo(getOwner());
		super.open();
	}
}
