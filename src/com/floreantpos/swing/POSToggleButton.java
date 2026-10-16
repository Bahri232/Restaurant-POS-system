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
package com.floreantpos.swing;

import java.awt.Dimension;

import javax.swing.JToggleButton;
import javax.swing.UIManager;

import com.floreantpos.POSConstants;


public class POSToggleButton extends JToggleButton {
	//public static Border border = new LineBorder(Color.BLACK, 1);
	private static java.awt.Insets margin = new java.awt.Insets(4, 6, 4, 6);

	static {
		UIManager.put("POSToggleButtonUI", "com.floreantpos.swing.POSToggleButtonUI"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public POSToggleButton() {
		this(null);
	}

	public POSToggleButton(String text) {
		super(text);

//		setFont(UIConfig.getButtonFont());
		setFocusPainted(false);
		setFocusable(false);
		setMargin(margin);
	}

	@Override
	public String getUIClassID() {
		return "POSToggleButtonUI"; //$NON-NLS-1$
	}

	@Override
	public Dimension getPreferredSize() {
		if (isPreferredSizeSet()) {
			return super.getPreferredSize();
		}

		Dimension size = super.getPreferredSize();
		int minWidth = PosUIManager.getSize(POSConstants.BUTTON_DEFAULT_WIDTH);
		int minHeight = PosUIManager.getSize(POSConstants.BUTTON_DEFAULT_HEIGHT);

		if (size == null) {
			return new Dimension(minWidth, minHeight);
		}

		int width = Math.max(size.width, minWidth);
		int height = Math.max(size.height, minHeight);

		return new Dimension(width, height);
	}

	@Override
	public Dimension getMinimumSize() {
		if (isMinimumSizeSet()) {
			return super.getMinimumSize();
		}
		return getPreferredSize();
	}
}
