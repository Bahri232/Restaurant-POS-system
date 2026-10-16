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
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JSeparator;
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

		JPanel mainPanel = new JPanel(new MigLayout("fillx, insets 14 18 10 18", "[grow, fill]", "[]8[]8[]8[]"));
		mainPanel.setBackground(Color.WHITE);

		// Header: Logo & Reness POS title
		JPanel headerPanel = new JPanel(new MigLayout("insets 0, fillx", "[][grow]", "[]"));
		headerPanel.setOpaque(false);

		JLabel logoLabel = new JLabel();
		ImageIcon rawLogo = IconFactory.getIcon("/icons/", "fp_logo128x128.png");
		if (rawLogo != null && rawLogo.getImage() != null) {
			logoLabel.setIcon(new ImageIcon(rawLogo.getImage().getScaledInstance(54, 54, Image.SCALE_SMOOTH)));
		}
		headerPanel.add(logoLabel, "cell 0 0, aligny center");

		JLabel titleLabel = new JLabel("<html><div style='font-family:sans-serif;'>"
				+ "<div style='font-size:18px; font-weight:bold; color:#1a365d;'>Reness POS</div>"
				+ "<div style='font-size:11px; color:#4a5568; margin-top:2px;'>Professional Restaurant &amp; Retail POS &bull; <b style='color:#2b6cb0;'>Version " + Application.VERSION + "</b></div>"
				+ "</div></html>");
		headerPanel.add(titleLabel, "cell 1 0, gapleft 12, aligny center");
		mainPanel.add(headerPanel, "wrap, growx");

		mainPanel.add(new JSeparator(), "wrap, growx, gaptop 2, gapbottom 2");

		// Developer Card Panel
		JPanel devCard = new JPanel(new MigLayout("insets 10, fillx", "[][grow]", "[]"));
		devCard.setBackground(new Color(248, 249, 250));
		devCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
				BorderFactory.createEmptyBorder(6, 8, 6, 8)));

		// Developer Photo (Crisp 75x75)
		JLabel photoLabel = new JLabel();
		ImageIcon devIcon = IconFactory.getIcon("/icons/", "developer.jpg");
		if (devIcon != null && devIcon.getImage() != null) {
			Image scaled = devIcon.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH);
			photoLabel.setIcon(new ImageIcon(scaled));
		}
		photoLabel.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
		devCard.add(photoLabel, "cell 0 0, aligny top");

		// Developer Details
		String devHtml = "<html><div style='font-family:sans-serif; font-size:11px; color:#334155;'>"
				+ "<div style='font-size:13px; margin-bottom:3px;'><b style='color:#0f172a;'>Software Developer:</b> <span style='color:#1d4ed8; font-weight:bold;'>Abdul Malik Barrie (BAHRI)</span></div>"
				+ "<div style='margin-bottom:2px;'><b>Direct Line:</b> <b style='color:#2563eb;'>+232 80 020097</b> / <b style='color:#2563eb;'>+232 76 159189</b></div>"
				+ "<div style='margin-bottom:3px;'><b>WhatsApp:</b> <b style='color:#16a34a;'>+232 80 020097</b> &nbsp;&bull;&nbsp; <b>Email:</b> <span style='color:#2563eb;'>abdulmalikbarrie@gmail.com</span></div>"
				+ "<div style='color:#059669; font-weight:bold;'>&#10004; 24/7 Technical Support &amp; System Maintenance Available</div>"
				+ "</div></html>";
		JLabel devDetails = new JLabel(devHtml);
		devCard.add(devDetails, "cell 1 0, gapleft 12, growx");
		mainPanel.add(devCard, "wrap, growx");

		// System Terminal UID Section
		JPanel terminalPanel = new JPanel(new MigLayout("insets 6 10 6 10, fillx", "[][grow][]", "[]"));
		terminalPanel.setBackground(new Color(241, 245, 249));
		terminalPanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
		terminalPanel.add(new JLabel("<html><b style='font-size:11px;'>Terminal Key (System UID):</b></html>"), "cell 0 0");

		final JTextField tfTerminalKey = new JTextField(TerminalUtil.getSystemUID());
		tfTerminalKey.setEditable(false);
		tfTerminalKey.setHorizontalAlignment(JTextField.CENTER);
		tfTerminalKey.setFont(new Font("Monospaced", Font.BOLD, 12));
		tfTerminalKey.setBackground(Color.WHITE);
		tfTerminalKey.setPreferredSize(new Dimension(220, 26));
		terminalPanel.add(tfTerminalKey, "cell 1 0, growx, gapleft 8, gapright 8");

		final JButton btnCopy = new JButton("Copy");
		btnCopy.setPreferredSize(new Dimension(75, 26));
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
		JPanel footerPanel = new JPanel(new MigLayout("fillx, insets 8 15 8 15", "[grow]", "[]"));
		footerPanel.setBackground(new Color(248, 250, 252));
		footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
		JButton btnOk = new JButton(Messages.getString("AboutDialog.5"));
		btnOk.setFont(btnOk.getFont().deriveFont(Font.BOLD, 12f));
		btnOk.setPreferredSize(PosUIManager.getSize(110, 32));
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
