package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.POSConstants;
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
		getContentPane().setBackground(com.floreantpos.ui.POSTheme.CANVAS_BG);
		setLayout(new BorderLayout(0, 0));

		TitlePanel titlePanel = new TitlePanel();
		titlePanel.setTitle("Select Server");
		add(titlePanel, BorderLayout.NORTH);

		JPanel contentPanel = new JPanel(new MigLayout("insets 16 18 16 18, fillx", "[grow][]", "[]10[]"));
		contentPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		contentPanel.setBorder(com.floreantpos.ui.POSTheme.createHairlineBorder(4, 4, 4, 4));

		JLabel lblPrompt = new JLabel("Server assigned to this table");
		lblPrompt.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		lblPrompt.setForeground(com.floreantpos.ui.POSTheme.TEXT_MUTED);
		contentPanel.add(lblPrompt, "span 2, wrap");

		serverComboBoxModel = new DefaultComboBoxModel<>();
		cbServers = new JComboBox<>(serverComboBoxModel);
		cbServers.setPreferredSize(new Dimension(320, 44));
		cbServers.setFont(com.floreantpos.ui.POSTheme.fontPlain(14));
		cbServers.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (value instanceof Server) {
					Server s = (Server) value;
					setText(s.getDisplayName());
				} else if (value == null) {
					setText("-- Select Server --");
					setForeground(com.floreantpos.ui.POSTheme.TEXT_MUTED);
				}
				return this;
			}
		});
		contentPanel.add(cbServers, "growx, height 44!");

		btnAddServer = new PosButton("+ New Server");
		btnAddServer.setPreferredSize(new Dimension(145, 44));
		btnAddServer.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		com.floreantpos.ui.POSTheme.styleAccentOutlineButton(btnAddServer);
		btnAddServer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doQuickAddServer();
			}
		});
		contentPanel.add(btnAddServer, "height 44!, wrap");

		JPanel bodyWrapper = new JPanel(new BorderLayout());
		bodyWrapper.setOpaque(false);
		bodyWrapper.setBorder(javax.swing.BorderFactory.createEmptyBorder(14, 16, 14, 16));
		bodyWrapper.add(contentPanel, BorderLayout.CENTER);
		add(bodyWrapper, BorderLayout.CENTER);

		// Button Bar
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.setBackground(com.floreantpos.ui.POSTheme.CARD_BG);
		bottomPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 0, 0, com.floreantpos.ui.POSTheme.BORDER_SUBTLE));

		JPanel actionButtonPanel = new JPanel(new MigLayout("insets 12 16 12 16, align right", "[120px]12[140px]", "[46px]"));
		actionButtonPanel.setOpaque(false);

		btnCancel = new PosButton(POSConstants.CANCEL);
		btnCancel.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		btnCancel.setPreferredSize(new Dimension(120, 46));
		com.floreantpos.ui.POSTheme.styleNeutralButton(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCanceled(true);
				dispose();
			}
		});
		actionButtonPanel.add(btnCancel, "grow");

		btnOk = new PosButton(POSConstants.OK);
		btnOk.setFont(com.floreantpos.ui.POSTheme.fontBold(13));
		btnOk.setPreferredSize(new Dimension(140, 46));
		com.floreantpos.ui.POSTheme.stylePrimaryButton(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doOk();
			}
		});
		actionButtonPanel.add(btnOk, "grow");

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
