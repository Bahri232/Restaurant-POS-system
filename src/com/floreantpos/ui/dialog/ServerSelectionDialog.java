package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.IconFactory;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.Server;
import com.floreantpos.model.dao.ServerDAO;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.TitlePanel;
import com.floreantpos.util.POSUtil;

public class ServerSelectionDialog extends POSDialog {
	private JComboBox<Server> cbServers;
	private DefaultComboBoxModel<Server> serverComboBoxModel;
	private PosButton btnAddServer;
	private PosButton btnOk;
	private PosButton btnCancel;
	private Server selectedServer;

	public ServerSelectionDialog() {
		this(POSUtil.getFocusedWindow());
	}

	public ServerSelectionDialog(java.awt.Window parent) {
		super(parent, "Select Server", true);
		initComponents();
		loadActiveServers();
	}

	private void initComponents() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("SELECT SERVER FOR TABLE");
		add(titlePanel, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel(new MigLayout("insets 25 30 20 30, fillx", "[grow][]", "[]15[]20[]"));

		JLabel lblPrompt = new JLabel("Choose the server handling this order:");
		lblPrompt.setFont(lblPrompt.getFont().deriveFont(Font.BOLD, 14f));
		contentPanel.add(lblPrompt, "span 2, wrap");

		serverComboBoxModel = new DefaultComboBoxModel<>();
		cbServers = new JComboBox<>(serverComboBoxModel);
		cbServers.setPreferredSize(new Dimension(320, 48));
		cbServers.setFont(cbServers.getFont().deriveFont(14f));
		cbServers.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Server) {
					Server s = (Server) value;
					setText(s.getDisplayName());
				} else if (value == null) {
					setText("-- Select Server --");
					setForeground(Color.GRAY);
				}
				return this;
			}
		});
		contentPanel.add(cbServers, "growx, height 48!");

		btnAddServer = new PosButton("+ Add New Server");
		btnAddServer.setPreferredSize(new Dimension(160, 48));
		btnAddServer.setBackground(new Color(41, 128, 185));
		btnAddServer.setForeground(Color.WHITE);
		btnAddServer.setFont(btnAddServer.getFont().deriveFont(Font.BOLD, 13f));
		btnAddServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doQuickAddServer();
			}
		});
		contentPanel.add(btnAddServer, "height 48!, wrap");

		JLabel lblNote = new JLabel("<html><small style='color:gray;'>Only Active servers appear here. If a waiter/waitress is not listed, click <b>+ Add New Server</b> above.</small></html>");
		contentPanel.add(lblNote, "span 2, wrap");

		add(contentPanel, BorderLayout.CENTER);

		// Button Bar
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(new JSeparator(), BorderLayout.NORTH);

		JPanel actionButtonPanel = new JPanel(new MigLayout("insets 10 20 15 20, align center", "[130px]15[130px]", "[50px]"));

		btnOk = new PosButton(POSConstants.OK);
		btnOk.setIcon(IconFactory.getIcon("/ui_icons/", "finish.png"));
		btnOk.setPreferredSize(new Dimension(130, 50));
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});
		actionButtonPanel.add(btnOk, "grow");

		btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setIcon(IconFactory.getIcon("/ui_icons/", "cancel.png"));
		btnCancel.setPreferredSize(new Dimension(130, 50));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		actionButtonPanel.add(btnCancel, "grow");

		bottomPanel.add(actionButtonPanel, BorderLayout.CENTER);
		add(bottomPanel, BorderLayout.SOUTH);

		setResizable(false);
		pack();
		setLocationRelativeTo(getOwner());
	}

	private void loadActiveServers() {
		serverComboBoxModel.removeAllElements();
		List<Server> activeServers = ServerDAO.getInstance().findAllActive();
		for (Server server : activeServers) {
			serverComboBoxModel.addElement(server);
		}
		if (serverComboBoxModel.getSize() > 0) {
			cbServers.setSelectedIndex(0);
		}
	}

	private void doQuickAddServer() {
		QuickAddServerDialog quickAddDialog = new QuickAddServerDialog(this);
		quickAddDialog.open();
		if (!quickAddDialog.isCanceled()) {
			Server newServer = quickAddDialog.getCreatedServer();
			if (newServer != null) {
				loadActiveServers();
				cbServers.setSelectedItem(newServer);
				// Automatically select and proceed with newly created server
				doOk();
			}
		}
	}

	private void doOk() {
		selectedServer = (Server) cbServers.getSelectedItem();
		if (selectedServer == null) {
			POSMessageDialog.showError(this, "Please select an active server or click '+ Add New Server'.");
			return;
		}
		setCanceled(false);
		dispose();
	}

	public Server getSelectedServer() {
		return selectedServer;
	}

	public void setSelectedServer(Server server) {
		this.selectedServer = server;
		if (server != null && cbServers != null) {
			for (int i = 0; i < cbServers.getItemCount(); i++) {
				Server item = cbServers.getItemAt(i);
				if (item != null && item.getId() != null && item.getId().equals(server.getId())) {
					cbServers.setSelectedIndex(i);
					break;
				}
			}
		}
	}
}
