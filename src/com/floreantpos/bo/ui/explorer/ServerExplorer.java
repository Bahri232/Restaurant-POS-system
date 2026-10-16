package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.floreantpos.POSConstants;
import com.floreantpos.PosLog;
import com.floreantpos.bo.ui.BOMessageDialog;
import com.floreantpos.model.Server;
import com.floreantpos.model.dao.ServerDAO;
import com.floreantpos.swing.ListTableModel;
import com.floreantpos.swing.TransparentPanel;
import com.floreantpos.ui.PosTableRenderer;
import com.floreantpos.ui.dialog.BeanEditorDialog;
import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.forms.ServerForm;
import com.floreantpos.util.POSUtil;

public class ServerExplorer extends TransparentPanel {
	private JTable table;
	private ServerTableModel tableModel;

	public ServerExplorer() {
		List<Server> servers = ServerDAO.getInstance().findAll();

		tableModel = new ServerTableModel(servers);
		table = new JTable(tableModel);
		table.setDefaultRenderer(Object.class, new PosTableRenderer());

		// Status column custom renderer to highlight Active vs Inactive
		table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				if (!isSelected) {
					if ("Active".equals(value)) {
						c.setForeground(new Color(0, 130, 0));
					} else {
						c.setForeground(Color.RED);
					}
				}
				return c;
			}
		});

		setLayout(new BorderLayout(5, 5));
		add(new JScrollPane(table), BorderLayout.CENTER);

		JButton addButton = new JButton(POSConstants.ADD);
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					ServerForm editor = new ServerForm();
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					Server server = editor.getBean();
					tableModel.addItem(server);
				} catch (Exception x) {
					PosLog.error(getClass(), x);
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		JButton editButton = new JButton(POSConstants.EDIT);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int index = table.getSelectedRow();
					if (index < 0)
						return;

					Server server = (Server) tableModel.getRowData(index);
					ServerForm editor = new ServerForm(server);
					BeanEditorDialog dialog = new BeanEditorDialog(POSUtil.getBackOfficeWindow(), editor);
					dialog.open();
					if (dialog.isCanceled())
						return;

					tableModel.updateItem(index);
				} catch (Exception x) {
					PosLog.error(getClass(), x);
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		JButton deleteButton = new JButton(POSConstants.DELETE);
		deleteButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int index = table.getSelectedRow();
				if (index < 0)
					return;

				Server server = (Server) tableModel.getRowData(index);
				if (server == null)
					return;

				try {
					// Requirement: Do NOT allow permanent deletion of servers who have past orders,
					// so historical sales reports remain intact. Setting a server to "Inactive"
					// should hide them from the cashier's dropdown while keeping their name on past receipts.
					if (ServerDAO.getInstance().hasOrders(server)) {
						int choice = JOptionPane.showOptionDialog(
								POSUtil.getBackOfficeWindow(),
								"Server '" + server.getDisplayName() + "' has past orders in the system.\n\n"
										+ "To protect historical sales reports and receipts, servers with order history cannot be permanently deleted.\n\n"
										+ "Would you like to set their status to Inactive instead?\n(Inactive servers will no longer appear on the cashier's selection screen).",
								"Cannot Delete Server With Past Orders",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.WARNING_MESSAGE,
								null,
								new Object[] { "Set to Inactive", "Cancel" },
								"Set to Inactive");

						if (choice == 0) {
							server.setActive(false);
							ServerDAO.getInstance().saveOrUpdate(server);
							tableModel.updateItem(index);
							POSMessageDialog.showMessage(POSUtil.getBackOfficeWindow(),
									"Server '" + server.getDisplayName() + "' is now Inactive.");
						}
						return;
					}

					if (ConfirmDeleteDialog.showMessage(POSUtil.getBackOfficeWindow(), POSConstants.CONFIRM_DELETE,
							POSConstants.DELETE) == ConfirmDeleteDialog.YES) {
						ServerDAO.getInstance().delete(server);
						tableModel.deleteItem(index);
					}
				} catch (Exception x) {
					PosLog.error(getClass(), x);
					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
				}
			}
		});

		TransparentPanel panel = new TransparentPanel();
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		add(panel, BorderLayout.SOUTH);
	}

	class ServerTableModel extends ListTableModel {
		ServerTableModel(List<Server> list) {
			super(new String[] { "ID", "First Name", "Last Name", "Nickname / Tag", "Phone Number", "Status" }, list);
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Server server = (Server) rows.get(rowIndex);
			switch (columnIndex) {
				case 0:
					return server.getId() != null ? String.valueOf(server.getId()) : "";
				case 1:
					return server.getFirstName() != null ? server.getFirstName() : "";
				case 2:
					return server.getLastName() != null ? server.getLastName() : "";
				case 3:
					return server.getNickname() != null ? server.getNickname() : "";
				case 4:
					return server.getPhone() != null ? server.getPhone() : "";
				case 5:
					return server.isActive() ? "Active" : "Inactive";
			}
			return null;
		}
	}
}
