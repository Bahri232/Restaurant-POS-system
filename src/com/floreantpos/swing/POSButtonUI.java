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
/**
 * 
 */
package com.floreantpos.swing;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.jgoodies.looks.plastic.PlasticButtonUI;

public class POSButtonUI extends PlasticButtonUI {
	private static final POSButtonUI INSTANCE = new POSButtonUI();

	public static ComponentUI createUI(JComponent b) {
		return INSTANCE;
	}

	public void update(Graphics g, JComponent c) {
		if (c.isOpaque()) {
			AbstractButton b = (AbstractButton) c;
			if (isToolBarButton(b)) {
				c.setOpaque(false);
			} else if (b.isContentAreaFilled()) {
				g.setColor(c.getBackground());
				g.fillRect(0, 0, c.getWidth(), c.getHeight());

				if (is3D(b)) {
					Color color1 = java.awt.Color.decode("#B8731A"); // Warm Amber
					Color color2 = java.awt.Color.decode("#E5A01E"); // Gold
					
					// Keep original background if explicitly colored (not a UIResource)
					if (c.getBackground() != null && !(c.getBackground() instanceof javax.swing.plaf.UIResource)) {
						color1 = c.getBackground();
						color2 = color1.brighter();
					} else {
						// Crisp high-contrast text for Amber gradient
						c.setForeground(java.awt.Color.decode("#3D1E06")); // Deep bronze
					}
					
					int x = 0;
					int y = 0;
					int width = c.getWidth();
					int height = c.getHeight();
					
					GradientPaint gp = new GradientPaint(x, y, color2, x, height, color1, true); // vertical gradient
					Graphics2D g2 = (Graphics2D) g;
					g2.setPaint(gp);
					g2.fillRect(x, y, width, height);
				}
			}
		}
		paint(g, c);
	}

}