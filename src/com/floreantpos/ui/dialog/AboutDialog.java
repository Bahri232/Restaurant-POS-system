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
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
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
import javax.swing.Scrollable;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.util.POSUtil;
import com.orocube.common.util.TerminalUtil;

public class AboutDialog extends POSDialog {

	private static class ScrollableContentPanel extends JPanel implements Scrollable {
		public ScrollableContentPanel(LayoutManager layout) {
			super(layout);
		}

		@Override
		public Dimension getPreferredScrollableViewportSize() {
			return new Dimension(660, 480);
		}

		@Override
		public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
			return 25;
		}

		@Override
		public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
			return Math.max(visibleRect.height - 25, 25);
		}

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return true;
		}

		@Override
		public boolean getScrollableTracksViewportHeight() {
			return false;
		}
	}

	public AboutDialog() {
		super(POSUtil.getFocusedWindow(), Messages.getString("AboutDialog.0")); //$NON-NLS-1$
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		if (Application.getApplicationIcon() != null) {
			setIconImage(Application.getApplicationIcon().getImage());
		}
	}

	@Override
	protected void initUI() {
		setLayout(new BorderLayout());

		ScrollableContentPanel contentPanel = new ScrollableContentPanel(new MigLayout("fillx, insets 15", "[grow, fill]", "[]10[]10[]10[]10[]"));
		contentPanel.setBackground(Color.WHITE);

		// Header: Logo & Reness POS title
		JPanel headerPanel = new JPanel(new MigLayout("insets 5, fillx", "[][grow]", "[]"));
		headerPanel.setOpaque(false);
		JLabel logoLabel = new JLabel(IconFactory.getIcon("/icons/", "fp_logo128x128.png"));
		headerPanel.add(logoLabel, "cell 0 0, aligny center");

		JLabel titleLabel = new JLabel("<html><h1 style='margin:0; color:#1a365d;'>Reness POS</h1>"
				+ "<p style='margin:2px 0; color:#4a5568; font-size:13px;'>Professional Restaurant &amp; Retail POS</p>"
				+ "<b style='color:#2b6cb0; font-size:12px;'>Version " + Application.VERSION + "</b></html>");
		headerPanel.add(titleLabel, "cell 1 0, gapleft 15, aligny center");
		contentPanel.add(headerPanel, "wrap, growx");

		contentPanel.add(new JSeparator(), "wrap, growx, gaptop 5, gapbottom 5");

		// Developer Card Panel
		JPanel devCard = new JPanel(new MigLayout("insets 12, fillx", "[][grow]", "[]"));
		devCard.setBackground(new Color(248, 249, 250));
		devCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		// Developer Photo
		JLabel photoLabel = new JLabel();
		ImageIcon devIcon = IconFactory.getIcon("/icons/", "developer.jpg");
		if (devIcon != null && devIcon.getImage() != null) {
			Image scaled = devIcon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
			photoLabel.setIcon(new ImageIcon(scaled));
		}
		photoLabel.setBorder(BorderFactory.createLineBorder(new Color(203, 213, 225), 1));
		devCard.add(photoLabel, "cell 0 0, aligny top");

		// Developer Details
		String devHtml = "<html>"
				+ "<div style='font-family:sans-serif;'>"
				+ "<b style='font-size:14px; color:#1a202c;'>Software Developer:</b> <span style='font-size:14px; color:#1e40af; font-weight:bold;'>Abdul Malik Barrie (BAHRI)</span><br/>"
				+ "<table style='margin-top:6px; font-size:12px; color:#4a5568;' cellpadding='2'>"
				+ "<tr><td><b>Direct Line:</b></td><td><b style='color:#2563eb;'>+232 80 020097</b> / <b style='color:#2563eb;'>+232 76 159189</b></td></tr>"
				+ "<tr><td><b>WhatsApp:</b></td><td><b style='color:#16a34a;'>+232 80 020097</b></td></tr>"
				+ "<tr><td><b>Email:</b></td><td><a href='mailto:abdulmalikbarrie@gmail.com' style='color:#2563eb;'>abdulmalikbarrie@gmail.com</a></td></tr>"
				+ "</table>"
				+ "<p style='margin-top:8px; margin-bottom:0px; color:#059669; font-weight:bold; font-size:12px;'>&#10004; 24/7 Technical Support &amp; System Maintenance Available</p>"
				+ "</div></html>";
		JLabel devDetails = new JLabel(devHtml);
		devCard.add(devDetails, "cell 1 0, gapleft 15, growx");
		contentPanel.add(devCard, "wrap, growx");

		// System Terminal UID Section
		JPanel terminalPanel = new JPanel(new MigLayout("insets 8, fillx", "[][grow][]", "[]"));
		terminalPanel.setBackground(new Color(241, 245, 249));
		terminalPanel.setBorder(BorderFactory.createLineBorder(new Color(226, 232, 240), 1));
		terminalPanel.add(new JLabel("<html><b>Terminal Key (System UID):</b></html>"), "cell 0 0");

		final JTextField tfTerminalKey = new JTextField(TerminalUtil.getSystemUID());
		tfTerminalKey.setEditable(false);
		tfTerminalKey.setHorizontalAlignment(JTextField.CENTER);
		tfTerminalKey.setFont(new Font("Monospaced", Font.BOLD, 14));
		tfTerminalKey.setBackground(Color.WHITE);
		terminalPanel.add(tfTerminalKey, "cell 1 0, growx");

		final JButton btnCopy = new JButton("Copy");
		btnCopy.setPreferredSize(new Dimension(80, 30));
		btnCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringSelection stringSelection = new StringSelection(tfTerminalKey.getText());
				Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
				btnCopy.setText("Copied!");
			}
		});
		terminalPanel.add(btnCopy, "cell 2 0");
		contentPanel.add(terminalPanel, "wrap, growx");

		// Scroll pane wrapping content
		PosScrollPane scrollPane = new PosScrollPane(contentPanel, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBorder(null);
		scrollPane.setWheelScrollingEnabled(true);
		scrollPane.getVerticalScrollBar().setUnitIncrement(25);
		add(scrollPane, BorderLayout.CENTER);

		// Fixed Footer Panel with OK/Close Button
		JPanel footerPanel = new JPanel(new MigLayout("fillx, insets 10 15 10 15", "[grow]", "[]"));
		footerPanel.setBackground(new Color(248, 250, 252));
		footerPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(226, 232, 240)));
		JButton btnOk = new JButton(Messages.getString("AboutDialog.5"));
		btnOk.setFont(btnOk.getFont().deriveFont(Font.BOLD, 13f));
		btnOk.setPreferredSize(PosUIManager.getSize(120, 38));
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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
		int maxAvailableWidth = screenSize.width - (insets != null ? insets.left + insets.right : 0);
		int maxAvailableHeight = screenSize.height - (insets != null ? insets.top + insets.bottom : 0);

		int targetWidth = Math.min(680, (int) (maxAvailableWidth * 0.94));
		int targetHeight = Math.min(580, (int) (maxAvailableHeight * 0.88));

		setSize(targetWidth, targetHeight);
		super.open();
	}
}
