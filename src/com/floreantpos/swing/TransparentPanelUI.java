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

import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

/**
 * Clean flat panel UI delegate without legacy diagonal gradient fills.
 */
public class TransparentPanelUI extends BasicPanelUI {

	static TransparentPanelUI ui = new TransparentPanelUI();

	public static ComponentUI createUI(JComponent c) {
		return ui;
	}

	@Override
	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			g.setColor(c.getBackground());
			g.fillRect(0, 0, c.getWidth(), c.getHeight());
		}
		paint(g, c);
	}
}
