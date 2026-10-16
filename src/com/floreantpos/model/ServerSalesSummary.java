package com.floreantpos.model;

import java.io.Serializable;

public class ServerSalesSummary implements Serializable {
	private static final long serialVersionUID = 1L;

	private String serverName;
	private int tableCount;
	private int ticketCount;
	private double totalRevenue;

	public ServerSalesSummary() {
	}

	public ServerSalesSummary(String serverName, int tableCount, int ticketCount, double totalRevenue) {
		this.serverName = serverName;
		this.tableCount = tableCount;
		this.ticketCount = ticketCount;
		this.totalRevenue = totalRevenue;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getTableCount() {
		return tableCount;
	}

	public void setTableCount(int tableCount) {
		this.tableCount = tableCount;
	}

	public int getTicketCount() {
		return ticketCount;
	}

	public void setTicketCount(int ticketCount) {
		this.ticketCount = ticketCount;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setTotalRevenue(double totalRevenue) {
		this.totalRevenue = totalRevenue;
	}
}
