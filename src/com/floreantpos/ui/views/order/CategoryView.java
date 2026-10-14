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
/*
 * CategoryView.java
 *
 * Created on August 5, 2006, 2:21 AM
 */

package com.floreantpos.ui.views.order;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;

import org.apache.log4j.Logger;

import com.floreantpos.IconFactory;
import com.floreantpos.bo.ui.explorer.QuickMaintenanceExplorer;
import com.floreantpos.model.MenuCategory;
import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.MenuCategoryDAO;
import com.floreantpos.model.dao.MenuGroupDAO;
import com.floreantpos.swing.POSToggleButton;
import com.floreantpos.swing.PosUIManager;
import com.floreantpos.ui.views.order.actions.CategorySelectionListener;

/**
 *
 * @author  MShahriar
 */
public class CategoryView extends SelectionView implements ActionListener {
	private Vector<CategorySelectionListener> listenerList = new Vector<CategorySelectionListener>();
	//private CardLayout cardLayout = new CardLayout();

	private ButtonGroup categoryButtonGroup;
	private Map<String, CategoryButton> buttonMap = new HashMap<String, CategoryButton>();
	private MenuCategory selectedCategory;

	public static final String VIEW_NAME = "CATEGORY_VIEW"; //$NON-NLS-1$

	//private int panelCount = 0;

	/** Creates new form CategoryView */
	public CategoryView() {
		super(com.floreantpos.POSConstants.CATEGORIES, PosUIManager.getSize(100), PosUIManager.getSize(80));

		categoryButtonGroup = new ButtonGroup();
		setPreferredSize(new Dimension(PosUIManager.getSize(120, 100)));
	}

	public void initialize() {
		reset();

		MenuCategoryDAO categoryDAO = new MenuCategoryDAO();
		List<MenuCategory> categories = categoryDAO.findAllEnable();
		boolean maintenanceMode = RootView.getInstance().isMaintenanceMode();
		if (categories.size() == 0 && !maintenanceMode)
			return;

		Ticket currentTicket = OrderView.getInstance().getCurrentTicket();
		if (currentTicket != null) {
			OrderType orderType = currentTicket.getOrderType();
			MenuGroupDAO menuGroupDAO = MenuGroupDAO.getInstance();
			if (maintenanceMode) {
				categories.add(new MenuCategory(null, ""));
			}
			else {
				for (Iterator iterator = categories.iterator(); iterator.hasNext();) {
					MenuCategory menuCategory = (MenuCategory) iterator.next();
					if (menuCategory.getId() == null)
						continue;
					List<MenuGroup> menuGroups = menuCategory.getMenuGroups();

					for (Iterator iterator2 = menuGroups.iterator(); iterator2.hasNext();) {
						MenuGroup menuGroup = (MenuGroup) iterator2.next();
						if (!menuGroupDAO.hasChildren(null, menuGroup, orderType)) {
							iterator2.remove();
						}
					}

					if (menuGroups == null || menuGroups.size() == 0) {
						iterator.remove();
					}
				}
			}
			setItems(categories);

			CategoryButton categoryButton = null;
			if (maintenanceMode && (!categories.isEmpty() && selectedCategory != null)) {
				categoryButton = buttonMap.get(String.valueOf(selectedCategory.getId()));
			}
			else
				categoryButton = (CategoryButton) getFirstItemButton();

			if (categoryButton != null) {
				categoryButton.setSelected(true);
				fireCategorySelected(categoryButton.foodCategory);
			}
			else {
				fireCategorySelected(null);
			}

			if (!maintenanceMode && categories.size() <= 1) {
				setVisible(false);
			}
			else {
				setVisible(true);
			}
		}
	}

	@Override
	protected AbstractButton createItemButton(Object item) {
		MenuCategory menuCategory = (MenuCategory) item;
		//List<MenuGroup> menuGroups = menuCategory.getMenuGroups();
		/*if (menuGroups == null || menuGroups.size() == 0) {
			return null;
		}*/

		CategoryButton button = new CategoryButton(this, menuCategory);
		categoryButtonGroup.add(button);

		buttonMap.put(String.valueOf(menuCategory.getId()), button);

		return button;
	}

	public void updateView(MenuCategory menuCategory) {
		this.selectedCategory = menuCategory;
		initialize();
	}

	@Override
	protected LayoutManager createButtonPanelLayout() {
		return new GridLayout(0, 1, 2, 5);
	}

	public void addCategorySelectionListener(CategorySelectionListener listener) {
		listenerList.add(listener);
	}

	public void removeCategorySelectionListener(CategorySelectionListener listener) {
		listenerList.remove(listener);
	}

	private void fireCategorySelected(MenuCategory foodCategory) {
		selectedCategory = foodCategory;
		for (CategorySelectionListener listener : listenerList) {
			listener.categorySelected(foodCategory);
		}
	}

	public void setSelectedCategory(MenuCategory category) {
		CategoryButton button = buttonMap.get(String.valueOf(category.getId()));
		if (button != null) {
			button.setSelected(true);
		}
	}

	private static class CategoryButton extends POSToggleButton {
		MenuCategory foodCategory;
		private boolean isHovered = false;

		CategoryButton(CategoryView view, MenuCategory menuCategory) {
			updateView(menuCategory);
			addActionListener(view);
			
			setMargin(new java.awt.Insets(8, 8, 8, 8));
			setOpaque(false);
			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorderPainted(false);
			
			addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseEntered(java.awt.event.MouseEvent e) {
					isHovered = true;
					repaint();
				}
				public void mouseExited(java.awt.event.MouseEvent e) {
					isHovered = false;
					repaint();
				}
			});
		}
		
		@Override
		protected void paintComponent(java.awt.Graphics g) {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
			g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			
			// Shadow
			g2.setColor(new java.awt.Color(0, 0, 0, 35));
			g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, 10, 10);
			
			// Background
			if (isSelected() || isHovered) {
				g2.setColor(java.awt.Color.decode("#B8731A")); // Warm Amber
			} else {
				g2.setColor(getBackground() != null ? getBackground() : java.awt.Color.WHITE);
			}
			g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, 10, 10);
			
			super.paintComponent(g);
			g2.dispose();
		}
		
		@Override
		protected void paintBorder(java.awt.Graphics g) {
			if (isSelected() || isHovered) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
				g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(java.awt.Color.decode("#E5A01E")); // Gold border
				g2.setStroke(new java.awt.BasicStroke(1.5f));
				g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, 10, 10);
				g2.dispose();
			}
		}

		public void updateView(MenuCategory menuCategory) {
			this.foodCategory = menuCategory;

			if (foodCategory.getId() == null) {
				setIcon(IconFactory.getIcon("/ui_icons/", "add+user.png"));
			}
			else
				setText("<html><body><center>" + menuCategory.getDisplayName() + "</center></body></html>"); //$NON-NLS-1$ //$NON-NLS-2$

			if (menuCategory.getButtonColor() != null) {
				setBackground(menuCategory.getButtonColor());
			}
			if (menuCategory.getTextColor() != null) {
				setForeground(menuCategory.getTextColor());
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		CategoryButton button = (CategoryButton) e.getSource();
		if (button.isSelected()) {
			if (OrderView.getInstance().isVisible() && RootView.getInstance().isMaintenanceMode()) {
				QuickMaintenanceExplorer.quickMaintain(button.foodCategory);
				if (button.foodCategory.getId() == null)
					return;
			}
			fireCategorySelected(button.foodCategory);
		}
	}

	public void cleanup() {
		Collection<CategoryButton> buttons = buttonMap.values();

		for (CategoryButton button : buttons) {
			button.removeActionListener(this);
		}
		buttonMap.clear();
	}

	@Override
	public void componentResized(ComponentEvent e) {
		int totalItem = getFitableButtonCount();
		if (totalItem == buttonPanelContainer.getComponentCount()) {
			return;
		}
		renderItems();
		if (!isInitialized()) {
			CategoryButton categoryButton = (CategoryButton) getFirstItemButton();
			if (categoryButton != null) {
				categoryButton.setSelected(true);
				fireCategorySelected(categoryButton.foodCategory);
			}
		}
	}

	private static Logger logger = Logger.getLogger(MenuItemView.class);
}
