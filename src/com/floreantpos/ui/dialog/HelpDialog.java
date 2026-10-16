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
import com.floreantpos.main.Application;
import com.floreantpos.util.POSUtil;
import com.orocube.common.util.TerminalUtil;

public class HelpDialog extends POSDialog {

	public HelpDialog() {
		super(POSUtil.getFocusedWindow(), "Help & Technical Support");
		if (Application.getApplicationIcon() != null) {
			setIconImage(Application.getApplicationIcon().getImage());
		}
	}

	@Override
	protected void initUI() {
		JPanel contentPanel = new JPanel(new MigLayout("fillx, insets 15", "[grow]", "[]10[]10[]10[]10[]"));
		contentPanel.setBackground(Color.WHITE);

		// Top Header
		JPanel headerPanel = new JPanel(new MigLayout("insets 5, fillx", "[][grow]", "[]"));
		headerPanel.setOpaque(false);
		JLabel logoLabel = new JLabel(IconFactory.getIcon("/icons/", "fp_logo128x128.png"));
		headerPanel.add(logoLabel, "cell 0 0, aligny center");

		JLabel titleLabel = new JLabel("<html><h1 style='margin:0; color:#1e3a8a;'>Help &amp; Technical Support</h1>"
				+ "<p style='margin:2px 0; color:#475569; font-size:13px;'>Reness POS &bull; Version " + Application.VERSION + "</p>"
				+ "<b style='color:#059669; font-size:12px;'>&#9679; 24/7 Technical Support &amp; System Maintenance Available</b></html>");
		headerPanel.add(titleLabel, "cell 1 0, gapleft 15, aligny center");
		contentPanel.add(headerPanel, "wrap, growx");

		contentPanel.add(new JSeparator(), "wrap, growx, gaptop 5, gapbottom 5");

		// Developer & Support Contact Card
		JPanel devCard = new JPanel(new MigLayout("insets 12, fillx", "[][grow]", "[]"));
		devCard.setBackground(new Color(248, 250, 252));
		devCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		// Developer Photo
		JLabel photoLabel = new JLabel();
		ImageIcon devIcon = IconFactory.getIcon("/icons/", "developer.jpg");
		if (devIcon != null && devIcon.getImage() != null) {
			Image scaled = devIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
			photoLabel.setIcon(new ImageIcon(scaled));
		}
		photoLabel.setBorder(BorderFactory.createLineBorder(new Color(148, 163, 184), 1));
		devCard.add(photoLabel, "cell 0 0, aligny top");

		// Developer Contact Details
		String contactHtml = "<html>"
				+ "<div style='font-family:sans-serif;'>"
				+ "<b style='font-size:14px; color:#0f172a;'>Software Developer:</b> <span style='font-size:14px; color:#1e40af; font-weight:bold;'>Abdul Malik Barrie (BAHRI)</span><br/>"
				+ "<table style='margin-top:8px; font-size:13px; color:#334155;' cellpadding='2'>"
				+ "<tr><td><b>Direct Line:</b></td><td><b style='color:#1d4ed8; font-size:14px;'>+232 80 020097</b> / <b style='color:#1d4ed8;'>+232 76 159189</b></td></tr>"
				+ "<tr><td><b>WhatsApp:</b></td><td><b style='color:#15803d; font-size:14px;'>+232 80 020097</b></td></tr>"
				+ "<tr><td><b>Email:</b></td><td><a href='mailto:abdulmalikbarrie@gmail.com' style='color:#2563eb;'>abdulmalikbarrie@gmail.com</a></td></tr>"
				+ "</table>"
				+ "<p style='margin-top:8px; margin-bottom:0px; color:#047857; font-weight:bold; font-size:12px;'>&#10004; 24/7 Technical Support &amp; System Maintenance Available</p>"
				+ "</div></html>";
		JLabel contactLabel = new JLabel(contactHtml);
		devCard.add(contactLabel, "cell 1 0, gapleft 15, growx");
		contentPanel.add(devCard, "wrap, growx");

		// Remote Support Box (Terminal ID)
		JPanel remotePanel = new JPanel(new MigLayout("insets 10, fillx", "[][grow][]", "[]"));
		remotePanel.setBackground(new Color(241, 245, 249));
		remotePanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));

		remotePanel.add(new JLabel("<html><b>Remote Assistance Terminal ID:</b><br/><small style='color:#64748b;'>Provide this key to the developer when requesting remote help</small></html>"), "cell 0 0");

		final JTextField tfTerminalKey = new JTextField(TerminalUtil.getSystemUID());
		tfTerminalKey.setEditable(false);
		tfTerminalKey.setHorizontalAlignment(JTextField.CENTER);
		tfTerminalKey.setFont(new Font("Monospaced", Font.BOLD, 15));
		tfTerminalKey.setBackground(Color.WHITE);
		remotePanel.add(tfTerminalKey, "cell 1 0, growx");

		final JButton btnCopy = new JButton("Copy Key");
		btnCopy.setPreferredSize(new Dimension(90, 32));
		btnCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(tfTerminalKey.getText());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				btnCopy.setText("Copied!");
			}
		});
		remotePanel.add(btnCopy, "cell 2 0");
		contentPanel.add(remotePanel, "wrap, growx");

		// Quick Operational Help for Cashier
		JPanel tipsPanel = new JPanel(new MigLayout("insets 10, fillx", "[grow]", "[]"));
		tipsPanel.setBackground(new Color(254, 252, 232));
		tipsPanel.setBorder(BorderFactory.createLineBorder(new Color(254, 240, 138), 1));
		String tipsHtml = "<html>"
				+ "<b style='color:#854d0e; font-size:12px;'>Quick Operating Tips for Cashiers:</b>"
				+ "<ul style='margin-top:4px; margin-bottom:0px; color:#713f12; font-size:11px;'>"
				+ "<li><b>Server Table Assignment:</b> The system prompts you to select a server when opening a table, or tap the <b>SERVER</b> button on the order screen.</li>"
				+ "<li><b>Guest Settlement:</b> Always collect cash against the printed pre-bill/guest check, then click <b>Settle</b>.</li>"
				+ "<li><b>End of Shift Balancing:</b> Go to <b>Other Functions &gt; Drawer Pull</b> to balance physical drawer cash with the system total.</li>"
				+ "</ul></html>";
		tipsPanel.add(new JLabel(tipsHtml), "growx");
		contentPanel.add(tipsPanel, "wrap, growx");

		contentPanel.add(new JSeparator(), "wrap, growx");

		// Footer Close Button
		JPanel footerPanel = new JPanel(new MigLayout("fillx, insets 0", "[grow]", "[]"));
		footerPanel.setOpaque(false);
		JButton btnClose = new JButton("Close");
		btnClose.setPreferredSize(new Dimension(110, 35));
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		footerPanel.add(btnClose, "center");
		contentPanel.add(footerPanel, "wrap, growx");

		getContentPane().add(contentPanel);
	}
}
