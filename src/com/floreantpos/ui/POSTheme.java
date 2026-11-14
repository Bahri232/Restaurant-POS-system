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
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import com.floreantpos.swing.PosUIManager;
import com.formdev.flatlaf.FlatClientProperties;

/**
 * Centralized visual design tokens, FlatLaf UIManager defaults, and reusable
 * component styling helpers for the POS UI.
 */
public final class POSTheme {

	public static final int CORNER_RADIUS = 10;

	// Surface & neutral palette
	public static final String HEX_CANVAS_BG = "#F4F5F7"; //$NON-NLS-1$
	public static final String HEX_CARD_BG = "#FFFFFF"; //$NON-NLS-1$
	public static final String HEX_SUBTLE_SURFACE = "#F8FAFC"; //$NON-NLS-1$
	public static final String HEX_SURFACE_HOVER = "#F1F5F9"; //$NON-NLS-1$
	public static final String HEX_SURFACE_PRESSED = "#E2E8F0"; //$NON-NLS-1$
	public static final String HEX_BORDER_SUBTLE = "#E2E8F0"; //$NON-NLS-1$
	public static final String HEX_BORDER_STRONG = "#CBD5E1"; //$NON-NLS-1$

	// Typography colors
	public static final String HEX_TEXT_PRIMARY = "#1A202C"; //$NON-NLS-1$
	public static final String HEX_TEXT_SECONDARY = "#334155"; //$NON-NLS-1$
	public static final String HEX_TEXT_MUTED = "#64748B"; //$NON-NLS-1$

	// Brand accent: Amber-Gold
	public static final String HEX_ACCENT_PRIMARY = "#C87D19"; //$NON-NLS-1$
	public static final String HEX_ACCENT_HOVER = "#B06A10"; //$NON-NLS-1$
	public static final String HEX_ACCENT_PRESSED = "#9A5A0C"; //$NON-NLS-1$
	public static final String HEX_ACCENT_SOFT_BG = "#FFFBEB"; //$NON-NLS-1$
	public static final String HEX_ACCENT_SOFT_HOVER = "#FEF3C7"; //$NON-NLS-1$
	public static final String HEX_ACCENT_SOFT_PRESSED = "#FDE68A"; //$NON-NLS-1$

	// Semantic payment & action accents
	public static final String HEX_CASH_GREEN = "#15803D"; //$NON-NLS-1$
	public static final String HEX_CASH_GREEN_HOVER = "#166534"; //$NON-NLS-1$
	public static final String HEX_CASH_GREEN_PRESSED = "#14532D"; //$NON-NLS-1$
	public static final String HEX_SUCCESS_SOFT_BG = "#ECFDF5"; //$NON-NLS-1$
	public static final String HEX_SUCCESS_SOFT_BORDER = "#A7F3D0"; //$NON-NLS-1$

	public static final String HEX_ORANGE_MONEY = "#EA580C"; //$NON-NLS-1$
	public static final String HEX_ORANGE_MONEY_HOVER = "#C2410C"; //$NON-NLS-1$
	public static final String HEX_ORANGE_MONEY_PRESSED = "#9A3412"; //$NON-NLS-1$

	public static final String HEX_AFRIMONEY_RED = "#DC2626"; //$NON-NLS-1$
	public static final String HEX_AFRIMONEY_RED_HOVER = "#B91C1C"; //$NON-NLS-1$
	public static final String HEX_AFRIMONEY_RED_PRESSED = "#991B1B"; //$NON-NLS-1$
	public static final String HEX_ERROR_SOFT_BG = "#FEF2F2"; //$NON-NLS-1$

	public static final String HEX_NEUTRAL_BTN_BG = "#E2E8F0"; //$NON-NLS-1$
	public static final String HEX_NEUTRAL_BTN_HOVER = "#CBD5E1"; //$NON-NLS-1$
	public static final String HEX_NEUTRAL_BTN_PRESSED = "#94A3B8"; //$NON-NLS-1$
	public static final String HEX_NEUTRAL_BTN_TEXT = "#334155"; //$NON-NLS-1$

	public static final Color CANVAS_BG = Color.decode(HEX_CANVAS_BG);
	public static final Color CARD_BG = Color.decode(HEX_CARD_BG);
	public static final Color SUBTLE_SURFACE = Color.decode(HEX_SUBTLE_SURFACE);
	public static final Color SURFACE_HOVER = Color.decode(HEX_SURFACE_HOVER);
	public static final Color SURFACE_PRESSED = Color.decode(HEX_SURFACE_PRESSED);
	public static final Color BORDER_SUBTLE = Color.decode(HEX_BORDER_SUBTLE);
	public static final Color BORDER_STRONG = Color.decode(HEX_BORDER_STRONG);

	public static final Color TEXT_PRIMARY = Color.decode(HEX_TEXT_PRIMARY);
	public static final Color TEXT_SECONDARY = Color.decode(HEX_TEXT_SECONDARY);
	public static final Color TEXT_MUTED = Color.decode(HEX_TEXT_MUTED);

	public static final Color ACCENT_PRIMARY = Color.decode(HEX_ACCENT_PRIMARY);
	public static final Color ACCENT_HOVER = Color.decode(HEX_ACCENT_HOVER);
	public static final Color ACCENT_PRESSED = Color.decode(HEX_ACCENT_PRESSED);
	public static final Color ACCENT_SOFT_BG = Color.decode(HEX_ACCENT_SOFT_BG);
	public static final Color ACCENT_SOFT_HOVER = Color.decode(HEX_ACCENT_SOFT_HOVER);
	public static final Color ACCENT_SOFT_PRESSED = Color.decode(HEX_ACCENT_SOFT_PRESSED);
	public static final Color ON_ACCENT = Color.WHITE;

	public static final Color CASH_GREEN = Color.decode(HEX_CASH_GREEN);
	public static final Color CASH_GREEN_HOVER = Color.decode(HEX_CASH_GREEN_HOVER);
	public static final Color CASH_GREEN_PRESSED = Color.decode(HEX_CASH_GREEN_PRESSED);
	public static final Color SUCCESS_SOFT_BG = Color.decode(HEX_SUCCESS_SOFT_BG);
	public static final Color SUCCESS_SOFT_BORDER = Color.decode(HEX_SUCCESS_SOFT_BORDER);

	public static final Color ORANGE_MONEY = Color.decode(HEX_ORANGE_MONEY);
	public static final Color ORANGE_MONEY_HOVER = Color.decode(HEX_ORANGE_MONEY_HOVER);
	public static final Color ORANGE_MONEY_PRESSED = Color.decode(HEX_ORANGE_MONEY_PRESSED);

	public static final Color AFRIMONEY_RED = Color.decode(HEX_AFRIMONEY_RED);
	public static final Color AFRIMONEY_RED_HOVER = Color.decode(HEX_AFRIMONEY_RED_HOVER);
	public static final Color AFRIMONEY_RED_PRESSED = Color.decode(HEX_AFRIMONEY_RED_PRESSED);
	public static final Color ERROR_SOFT_BG = Color.decode(HEX_ERROR_SOFT_BG);

	public static final Color NEUTRAL_BTN_BG = Color.decode(HEX_NEUTRAL_BTN_BG);
	public static final Color NEUTRAL_BTN_HOVER = Color.decode(HEX_NEUTRAL_BTN_HOVER);
	public static final Color NEUTRAL_BTN_PRESSED = Color.decode(HEX_NEUTRAL_BTN_PRESSED);
	public static final Color NEUTRAL_BTN_TEXT = Color.decode(HEX_NEUTRAL_BTN_TEXT);

	private static String resolvedFontFamily;

	private POSTheme() {
	}

	/**
	 * Resolves the unified UI font family once, preferring Segoe UI, then Inter,
	 * then Swing's Dialog fallback.
	 */
	public static synchronized String getFontFamily() {
		if (resolvedFontFamily != null) {
			return resolvedFontFamily;
		}
		try {
			String[] available = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			Set<String> familySet = new HashSet<String>(Arrays.asList(available));
			if (familySet.contains("Segoe UI")) { //$NON-NLS-1$
				resolvedFontFamily = "Segoe UI"; //$NON-NLS-1$
			}
			else if (familySet.contains("Inter")) { //$NON-NLS-1$
				resolvedFontFamily = "Inter"; //$NON-NLS-1$
			}
			else {
				resolvedFontFamily = Font.DIALOG;
			}
		} catch (Throwable t) {
			resolvedFontFamily = Font.DIALOG;
		}
		return resolvedFontFamily;
	}

	public static Font fontPlain(int ptSize) {
		return new Font(getFontFamily(), Font.PLAIN, PosUIManager.getFontSize(ptSize));
	}

	public static Font fontBold(int ptSize) {
		return new Font(getFontFamily(), Font.BOLD, PosUIManager.getFontSize(ptSize));
	}

	public static Font monoBold(int ptSize) {
		return new Font(Font.MONOSPACED, Font.BOLD, PosUIManager.getFontSize(ptSize));
	}

	/**
	 * Applies the restrained high-contrast POS color system and FlatLaf tactile
	 * states globally via UIManager.
	 */
	public static void applyFlatLafDefaults() {
		// Surfaces & background
		UIManager.put("Panel.background", CANVAS_BG); //$NON-NLS-1$
		UIManager.put("Dialog.background", CANVAS_BG); //$NON-NLS-1$
		UIManager.put("OptionPane.background", CANVAS_BG); //$NON-NLS-1$
		UIManager.put("Viewport.background", CANVAS_BG); //$NON-NLS-1$
		UIManager.put("ScrollPane.background", CANVAS_BG); //$NON-NLS-1$
		UIManager.put("control", CANVAS_BG); //$NON-NLS-1$

		// Foreground text
		UIManager.put("Label.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Label.disabledForeground", TEXT_MUTED); //$NON-NLS-1$

		// Tables & lists
		UIManager.put("Table.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("Table.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Table.gridColor", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("Table.selectionBackground", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Table.selectionForeground", ON_ACCENT); //$NON-NLS-1$
		UIManager.put("TableHeader.background", SUBTLE_SURFACE); //$NON-NLS-1$
		UIManager.put("TableHeader.foreground", TEXT_MUTED); //$NON-NLS-1$
		UIManager.put("TableHeader.separatorColor", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("TableHeader.bottomSeparatorColor", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("TableHeader.height", Integer.valueOf(34)); //$NON-NLS-1$

		UIManager.put("List.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("List.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("List.selectionBackground", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("List.selectionForeground", ON_ACCENT); //$NON-NLS-1$

		// Touch-friendly scrollbars
		UIManager.put("ScrollBar.width", Integer.valueOf(16)); //$NON-NLS-1$
		UIManager.put("ScrollBar.thumbArc", Integer.valueOf(999)); //$NON-NLS-1$
		UIManager.put("ScrollBar.thumbInsets", new Insets(2, 2, 2, 2)); //$NON-NLS-1$

		// Inputs & corners
		UIManager.put("Component.accentColor", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Component.focusColor", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Component.focusedBorderColor", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Component.borderColor", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("Component.focusWidth", Integer.valueOf(1)); //$NON-NLS-1$
		UIManager.put("Component.arc", Integer.valueOf(CORNER_RADIUS)); //$NON-NLS-1$
		UIManager.put("TextComponent.arc", Integer.valueOf(CORNER_RADIUS)); //$NON-NLS-1$

		UIManager.put("TextField.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("TextField.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("TextField.selectionBackground", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("TextField.selectionForeground", ON_ACCENT); //$NON-NLS-1$
		UIManager.put("PasswordField.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("PasswordField.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("ComboBox.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("ComboBox.foreground", TEXT_PRIMARY); //$NON-NLS-1$

		// Crisp restrained default buttons & toggles
		UIManager.put("Button.arc", Integer.valueOf(CORNER_RADIUS)); //$NON-NLS-1$
		UIManager.put("Button.borderWidth", Integer.valueOf(1)); //$NON-NLS-1$
		UIManager.put("Button.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("Button.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Button.hoverBackground", SURFACE_HOVER); //$NON-NLS-1$
		UIManager.put("Button.pressedBackground", SURFACE_PRESSED); //$NON-NLS-1$
		UIManager.put("Button.selectedBackground", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Button.selectedForeground", ON_ACCENT); //$NON-NLS-1$
		UIManager.put("Button.borderColor", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("Button.hoverBorderColor", BORDER_STRONG); //$NON-NLS-1$
		UIManager.put("Button.focusedBorderColor", ACCENT_PRIMARY); //$NON-NLS-1$

		UIManager.put("Button.default.background", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Button.default.foreground", ON_ACCENT); //$NON-NLS-1$
		UIManager.put("Button.default.hoverBackground", ACCENT_HOVER); //$NON-NLS-1$
		UIManager.put("Button.default.pressedBackground", ACCENT_PRESSED); //$NON-NLS-1$
		UIManager.put("Button.default.borderColor", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("Button.default.hoverBorderColor", ACCENT_HOVER); //$NON-NLS-1$
		UIManager.put("Button.default.focusedBorderColor", ACCENT_PRESSED); //$NON-NLS-1$

		UIManager.put("ToggleButton.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("ToggleButton.foreground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("ToggleButton.hoverBackground", SURFACE_HOVER); //$NON-NLS-1$
		UIManager.put("ToggleButton.pressedBackground", SURFACE_PRESSED); //$NON-NLS-1$
		UIManager.put("ToggleButton.selectedBackground", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("ToggleButton.selectedForeground", ON_ACCENT); //$NON-NLS-1$

		// Tabs & dividers
		UIManager.put("TabbedPane.background", CARD_BG); //$NON-NLS-1$
		UIManager.put("TabbedPane.foreground", TEXT_MUTED); //$NON-NLS-1$
		UIManager.put("TabbedPane.selectedForeground", TEXT_PRIMARY); //$NON-NLS-1$
		UIManager.put("TabbedPane.underlineColor", ACCENT_PRIMARY); //$NON-NLS-1$
		UIManager.put("TabbedPane.inactiveUnderlineColor", BORDER_STRONG); //$NON-NLS-1$
		UIManager.put("TabbedPane.hoverColor", SURFACE_HOVER); //$NON-NLS-1$
		UIManager.put("TabbedPane.contentAreaColor", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("TabbedPane.tabHeight", Integer.valueOf(36)); //$NON-NLS-1$

		UIManager.put("Separator.foreground", BORDER_SUBTLE); //$NON-NLS-1$
		UIManager.put("TitledBorder.titleColor", TEXT_MUTED); //$NON-NLS-1$
		UIManager.put("TitledBorder.border", BorderFactory.createLineBorder(BORDER_SUBTLE, 1)); //$NON-NLS-1$
	}

	/**
	 * Creates a crisp white elevated card panel with a 1px hairline border and internal padding.
	 */
	public static JPanel createCardPanel(int top, int left, int bottom, int right) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(CARD_BG);
		panel.setOpaque(true);
		panel.setBorder(createHairlineBorder(top, left, bottom, right));
		return panel;
	}

	public static Border createHairlineBorder() {
		return BorderFactory.createLineBorder(BORDER_SUBTLE, 1);
	}

	public static Border createHairlineBorder(int top, int left, int bottom, int right) {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(BORDER_SUBTLE, 1),
				BorderFactory.createEmptyBorder(top, left, bottom, right));
	}

	public static Border createSelectedCardBorder(int top, int left, int bottom, int right) {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(ACCENT_PRIMARY, 2),
				BorderFactory.createEmptyBorder(Math.max(0, top - 1), Math.max(0, left - 1),
						Math.max(0, bottom - 1), Math.max(0, right - 1)));
	}

	public static Border createSectionDividerBorder(int top, int left, int bottom, int right) {
		return BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_SUBTLE),
				BorderFactory.createEmptyBorder(top, left, bottom, right));
	}

	/**
	 * Creates a clean typography-driven section label instead of stacked TitledBorder boxes.
	 */
	public static JLabel createSectionHeader(String title) {
		JLabel label = new JLabel(title);
		label.setFont(fontBold(13));
		label.setForeground(TEXT_MUTED);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		return label;
	}

	/**
	 * Styles a text input field with white card surface, hairline border, and generous touch padding.
	 */
	public static void styleTextField(JTextField field, int fontSize, boolean bold) {
		if (field == null) {
			return;
		}
		field.setBackground(CARD_BG);
		field.setForeground(TEXT_PRIMARY);
		field.setFont(bold ? fontBold(fontSize) : fontPlain(fontSize));
		field.putClientProperty(FlatClientProperties.STYLE,
				"arc: " + CORNER_RADIUS + "; background: " + HEX_CARD_BG + "; foreground: " + HEX_TEXT_PRIMARY
						+ "; borderColor: " + HEX_BORDER_STRONG + "; focusedBorderColor: " + HEX_ACCENT_PRIMARY);
	}

	public static void styleReadOnlySummaryField(JTextField field, int fontSize, boolean emphasized) {
		if (field == null) {
			return;
		}
		field.setEditable(false);
		field.setBackground(CARD_BG);
		field.setForeground(emphasized ? TEXT_PRIMARY : TEXT_SECONDARY);
		field.setFont(emphasized ? fontBold(fontSize) : fontPlain(fontSize));
		field.putClientProperty(FlatClientProperties.STYLE,
				"arc: 8; background: " + HEX_SUBTLE_SURFACE + "; borderColor: " + HEX_BORDER_SUBTLE
						+ "; foreground: " + (emphasized ? HEX_TEXT_PRIMARY : HEX_TEXT_SECONDARY));
	}

	public static void stylePrimaryButton(AbstractButton button) {
		styleSolidButton(button, ACCENT_PRIMARY, ACCENT_HOVER, ACCENT_PRESSED, ON_ACCENT, ACCENT_PRIMARY);
	}

	public static void styleCashButton(AbstractButton button) {
		styleSolidButton(button, CASH_GREEN, CASH_GREEN_HOVER, CASH_GREEN_PRESSED, ON_ACCENT, CASH_GREEN);
	}

	public static void styleOrangeMoneyButton(AbstractButton button) {
		styleSolidButton(button, ORANGE_MONEY, ORANGE_MONEY_HOVER, ORANGE_MONEY_PRESSED, ON_ACCENT, ORANGE_MONEY);
	}

	public static void styleAfrimoneyButton(AbstractButton button) {
		styleSolidButton(button, AFRIMONEY_RED, AFRIMONEY_RED_HOVER, AFRIMONEY_RED_PRESSED, ON_ACCENT, AFRIMONEY_RED);
	}

	public static void styleNeutralButton(AbstractButton button) {
		styleSolidButton(button, NEUTRAL_BTN_BG, NEUTRAL_BTN_HOVER, NEUTRAL_BTN_PRESSED, NEUTRAL_BTN_TEXT,
				NEUTRAL_BTN_BG);
	}

	public static void styleSecondaryButton(AbstractButton button) {
		styleSolidButton(button, CARD_BG, SURFACE_HOVER, SURFACE_PRESSED, TEXT_PRIMARY, BORDER_STRONG);
	}

	public static void styleAccentOutlineButton(AbstractButton button) {
		styleSolidButton(button, ACCENT_SOFT_BG, ACCENT_SOFT_HOVER, ACCENT_SOFT_PRESSED, ACCENT_PRESSED,
				ACCENT_PRIMARY);
	}

	public static void styleKeypadButton(AbstractButton button) {
		styleSecondaryButton(button);
		if (button != null) {
			button.setFont(fontBold(22));
		}
	}

	/**
	 * Applies unified card tile styling to POS category/group/menu buttons while honoring
	 * explicit database-configured button/text colors when present.
	 */
	public static void styleTileButton(AbstractButton button, Color customBg, Color customFg) {
		if (button == null) {
			return;
		}
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		if (customBg != null) {
			Color hover = shiftColor(customBg, -0.08f);
			Color pressed = shiftColor(customBg, -0.16f);
			Color fg = customFg != null ? customFg : contrastForeground(customBg);
			button.setBackground(customBg);
			button.setForeground(fg);
			button.putClientProperty(FlatClientProperties.STYLE,
					"arc: " + CORNER_RADIUS + "; borderWidth: 1; background: " + toHex(customBg)
							+ "; foreground: " + toHex(fg)
							+ "; hoverBackground: " + toHex(hover)
							+ "; pressedBackground: " + toHex(pressed)
							+ "; selectedBackground: " + HEX_ACCENT_PRIMARY
							+ "; selectedForeground: #FFFFFF"
							+ "; borderColor: " + toHex(shiftColor(customBg, -0.15f))
							+ "; hoverBorderColor: " + HEX_ACCENT_PRIMARY);
		}
		else {
			Color fg = customFg != null ? customFg : TEXT_PRIMARY;
			button.setBackground(CARD_BG);
			button.setForeground(fg);
			button.putClientProperty(FlatClientProperties.STYLE,
					"arc: " + CORNER_RADIUS + "; borderWidth: 1; background: " + HEX_CARD_BG
							+ "; foreground: " + toHex(fg)
							+ "; hoverBackground: " + HEX_SURFACE_HOVER
							+ "; pressedBackground: " + HEX_SURFACE_PRESSED
							+ "; selectedBackground: " + HEX_ACCENT_PRIMARY
							+ "; selectedForeground: #FFFFFF"
							+ "; borderColor: " + HEX_BORDER_SUBTLE
							+ "; hoverBorderColor: " + HEX_BORDER_STRONG);
		}
	}

	public static void styleSolidButton(AbstractButton button, Color bg, Color hover, Color pressed, Color fg,
			Color borderColor) {
		if (button == null) {
			return;
		}
		button.setFocusPainted(false);
		button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		button.setBackground(bg);
		button.setForeground(fg);

		String bgHex = toHex(bg);
		String hoverHex = toHex(hover);
		String pressedHex = toHex(pressed);
		String fgHex = toHex(fg);
		String borderHex = toHex(borderColor != null ? borderColor : bg);

		button.putClientProperty(FlatClientProperties.STYLE,
				"arc: " + CORNER_RADIUS + "; borderWidth: 1; background: " + bgHex
						+ "; foreground: " + fgHex
						+ "; hoverBackground: " + hoverHex
						+ "; pressedBackground: " + pressedHex
						+ "; borderColor: " + borderHex
						+ "; hoverBorderColor: " + hoverHex
						+ "; focusedBorderColor: " + pressedHex);
	}

	public static String toHex(Color c) {
		if (c == null) {
			return "#FFFFFF"; //$NON-NLS-1$
		}
		return String.format("#%02X%02X%02X", Integer.valueOf(c.getRed()), Integer.valueOf(c.getGreen()), //$NON-NLS-1$
				Integer.valueOf(c.getBlue()));
	}

	public static Color contrastForeground(Color bg) {
		if (bg == null) {
			return TEXT_PRIMARY;
		}
		double luminance = (0.299 * bg.getRed() + 0.587 * bg.getGreen() + 0.114 * bg.getBlue()) / 255.0;
		return luminance < 0.55 ? Color.WHITE : TEXT_PRIMARY;
	}

	private static Color shiftColor(Color c, float amount) {
		int delta = Math.round(amount * 255f);
		int r = Math.max(0, Math.min(255, c.getRed() + delta));
		int g = Math.max(0, Math.min(255, c.getGreen() + delta));
		int b = Math.max(0, Math.min(255, c.getBlue() + delta));
		return new Color(r, g, b);
	}
}
