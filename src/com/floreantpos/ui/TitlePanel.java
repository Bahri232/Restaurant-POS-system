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
package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JSeparator;

import com.floreantpos.swing.TransparentPanel;

/**
 * Clean elevated card header banner with a subtle bottom hairline border.
 */
public class TitlePanel extends TransparentPanel {
	protected JSeparator jSeparator1;
	protected JLabel lblTitle;

	public TitlePanel() {
		initComponents();
	}

	private void initComponents() {
		lblTitle = new JLabel();
		jSeparator1 = new JSeparator();

		setOpaque(true);
		setBackground(POSTheme.CARD_BG);
		setLayout(new BorderLayout());

		lblTitle.setFont(getTitleFont());
		lblTitle.setForeground(getTitleColor());
		lblTitle.setText(com.floreantpos.POSConstants.TITLE);
		lblTitle.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));

		jSeparator1.setForeground(POSTheme.BORDER_SUBTLE);
		jSeparator1.setBackground(POSTheme.BORDER_SUBTLE);

		add(lblTitle, BorderLayout.CENTER);
		add(jSeparator1, BorderLayout.SOUTH);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (isOpaque()) {
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
	}

	private Font getTitleFont() {
		return POSTheme.fontBold(16);
	}

	private Color getTitleColor() {
		return POSTheme.TEXT_PRIMARY;
	}

	public String getTitle() {
		return lblTitle.getText();
	}

	public void setTitle(String title) {
		lblTitle.setText(title);
	}

	public void setTextAlignment(int alignment) {
		lblTitle.setHorizontalAlignment(alignment);
	}

	public void setSeparatorVisible(boolean visible) {
		jSeparator1.setVisible(visible);
	}
}
