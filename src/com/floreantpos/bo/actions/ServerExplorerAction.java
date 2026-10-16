package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.bo.ui.explorer.ServerExplorer;
import com.floreantpos.util.POSUtil;

public class ServerExplorerAction extends AbstractAction {

	public ServerExplorerAction() {
		super("Servers");
	}

	public ServerExplorerAction(String name) {
		super(name);
	}

	public ServerExplorerAction(String name, Icon icon) {
		super(name, icon);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = POSUtil.getBackOfficeWindow();

		ServerExplorer explorer = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab("Servers");
		if (index == -1) {
			explorer = new ServerExplorer();
			tabbedPane.addTab("Servers", explorer);
		} else {
			explorer = (ServerExplorer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(explorer);
	}
}
