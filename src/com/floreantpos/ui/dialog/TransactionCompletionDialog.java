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
package com.floreantpos.ui.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.floreantpos.Messages;
import com.floreantpos.main.Application;
import com.floreantpos.model.PosTransaction;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.swing.PosButton;
import com.floreantpos.ui.POSTheme;
import com.floreantpos.util.NumberUtil;

public class TransactionCompletionDialog extends POSDialog {
	//private List<Ticket> tickets;
	private double tenderedAmount;
	private double totalAmount;
	private double paidAmount;
	private double dueAmount;
	private double gratuityAmount;
	private double changeAmount;
	private double feeAmount;

	private JLabel lblTenderedAmount;
	private JLabel lblTotalAmount;
	private JLabel lblPaidAmount;
	private JLabel lblDueAmount;
	private JLabel lblChangeDue;
	private JLabel lblGratuityAmount;
	private JLabel lblFeeAmount;

	private PosTransaction completedTransaction;
	private boolean isCard;

	public TransactionCompletionDialog(PosTransaction transaction) {
		this.completedTransaction = transaction;
		this.isCard = completedTransaction != null && completedTransaction.isCard();
		initializeComponents();
		//setResizable(false);
	}

	public TransactionCompletionDialog(List<PosTransaction> transaction) {
		initializeComponents();
		//setResizable(false);
	}

	private void initializeComponents() {
		setTitle(com.floreantpos.POSConstants.TRANSACTION_COMPLETED);
		getContentPane().setBackground(POSTheme.CANVAS_BG);

		setLayout(new MigLayout("fillx, align 50% 0%, ins 20", "[grow]", "[]14[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JPanel summaryCard = POSTheme.createCardPanel(18, 22, 18, 22);
		summaryCard.setLayout(new MigLayout("fillx, wrap 2, ins 0", "[grow]24[right]", "[]10[]10[]10[]10[]10[]14[]")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

		JLabel lblHeader = new JLabel(com.floreantpos.POSConstants.TRANSACTION_COMPLETED);
		lblHeader.setFont(POSTheme.fontBold(16));
		lblHeader.setForeground(POSTheme.TEXT_PRIMARY);
		lblHeader.setBorder(POSTheme.createSectionDividerBorder(0, 0, 10, 0));
		summaryCard.add(lblHeader, "span 2, growx"); //$NON-NLS-1$

		summaryCard.add(createRowTitleLabel(Messages.getString("TransactionCompletionDialog.3") + ":"), "growx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblTotalAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		summaryCard.add(lblTotalAmount, "growx"); //$NON-NLS-1$

		summaryCard.add(createRowTitleLabel(Messages.getString("TransactionCompletionDialog.8") + ":"), "growx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblTenderedAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		summaryCard.add(lblTenderedAmount, "growx"); //$NON-NLS-1$

		summaryCard.add(createRowTitleLabel(Messages.getString("TransactionCompletionDialog.14") + ":"), "growx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblPaidAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		summaryCard.add(lblPaidAmount, "growx"); //$NON-NLS-1$

		summaryCard.add(createRowTitleLabel(Messages.getString("TransactionCompletionDialog.19") + ":"), "growx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblDueAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		summaryCard.add(lblDueAmount, "growx"); //$NON-NLS-1$

		summaryCard.add(createRowTitleLabel(Messages.getString("TransactionCompletionDialog.25") + ":"), "growx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblGratuityAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		summaryCard.add(lblGratuityAmount, "growx"); //$NON-NLS-1$

		summaryCard.add(createRowTitleLabel(Messages.getString("TransactionCompletionDialog.42") + ":"), "growx"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		lblFeeAmount = createLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		summaryCard.add(lblFeeAmount, "growx"); //$NON-NLS-1$

		JPanel changeBanner = new JPanel(new BorderLayout(16, 0));
		changeBanner.setBackground(POSTheme.SUCCESS_SOFT_BG);
		changeBanner.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(POSTheme.SUCCESS_SOFT_BORDER, 1),
				BorderFactory.createEmptyBorder(12, 16, 12, 16)));
		JLabel lblChangeTitle = new JLabel(Messages.getString("TransactionCompletionDialog.31") + ":"); //$NON-NLS-1$ //$NON-NLS-2$
		lblChangeTitle.setFont(POSTheme.fontBold(16));
		lblChangeTitle.setForeground(POSTheme.CASH_GREEN);
		lblChangeDue = new JLabel("0.0", JLabel.RIGHT); //$NON-NLS-1$
		lblChangeDue.setFont(POSTheme.fontBold(22));
		lblChangeDue.setForeground(POSTheme.CASH_GREEN);
		changeBanner.add(lblChangeTitle, BorderLayout.WEST);
		changeBanner.add(lblChangeDue, BorderLayout.EAST);
		summaryCard.add(changeBanner, "span 2, growx"); //$NON-NLS-1$

		add(summaryCard, "growx, wrap"); //$NON-NLS-1$

		PosButton btnClose = new PosButton(Messages.getString("TransactionCompletionDialog.37")); //$NON-NLS-1$
		POSTheme.stylePrimaryButton(btnClose);
		btnClose.setFont(POSTheme.fontBold(14));
		btnClose.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}

		});

		PosButton btnPrintStoreCopy = new PosButton(Messages.getString("TransactionCompletionDialog.38")); //$NON-NLS-1$
		POSTheme.styleSecondaryButton(btnPrintStoreCopy);
		btnPrintStoreCopy.setFont(POSTheme.fontBold(14));
		btnPrintStoreCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (completedTransaction != null) {
						ReceiptPrintService.printTransaction(completedTransaction, true, false);
					}
				} catch (Throwable ee) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TransactionCompletionDialog.39"), ee); //$NON-NLS-1$
				}
				//dispose();
			}
		});

		PosButton btnPrintAllCopy = new PosButton(Messages.getString("TransactionCompletionDialog.40")); //$NON-NLS-1$
		POSTheme.styleSecondaryButton(btnPrintAllCopy);
		btnPrintAllCopy.setFont(POSTheme.fontBold(14));
		btnPrintAllCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (completedTransaction != null) {
						ReceiptPrintService.printTransaction(completedTransaction, true, true);
					}
				} catch (Throwable ee) {
					POSMessageDialog.showError(Application.getPosWindow(), Messages.getString("TransactionCompletionDialog.41"), ee); //$NON-NLS-1$
				}
				dispose();
			}
		});

		JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
		p.setOpaque(false);

		if (isCard) {
			p.add(btnPrintAllCopy);
			p.add(btnPrintStoreCopy);
			p.add(btnClose);
		}
		else {
			btnPrintStoreCopy.setText(Messages.getString("TransactionCompletionDialog.0")); //$NON-NLS-1$
			p.add(btnPrintStoreCopy);
			p.add(btnClose);
		}

		add(p, "growx"); //$NON-NLS-1$
	}

	private JLabel createRowTitleLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(POSTheme.fontPlain(15));
		label.setForeground(POSTheme.TEXT_MUTED);
		label.setHorizontalAlignment(JLabel.LEFT);
		return label;
	}

	protected JLabel createLabel(String text, int alignment) {
		JLabel label = new JLabel(text);
		label.setFont(POSTheme.fontBold(17));
		label.setForeground(POSTheme.TEXT_PRIMARY);
		label.setHorizontalAlignment(alignment);
		label.setText(text);
		return label;
	}

	public double getTenderedAmount() {
		return tenderedAmount;
	}

	public void setTenderedAmount(double amountTendered) {
		this.tenderedAmount = amountTendered;
	}

	public void updateView() {
		lblTotalAmount.setText(NumberUtil.formatNumber(totalAmount));
		lblTenderedAmount.setText(NumberUtil.formatNumber(tenderedAmount));
		lblPaidAmount.setText(NumberUtil.formatNumber(paidAmount));
		lblDueAmount.setText(NumberUtil.formatNumber(dueAmount));
		lblGratuityAmount.setText(NumberUtil.formatNumber(gratuityAmount));
		lblFeeAmount.setText(NumberUtil.formatNumber(feeAmount));
		lblChangeDue.setText(NumberUtil.formatNumber(changeAmount));
	}

	public double getFeeAmount() {
		return feeAmount;
	}
	public void setFeeAmount(double feeAmount) {
		this.feeAmount = feeAmount;
	}
	public double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getGratuityAmount() {
		return gratuityAmount;
	}

	public void setGratuityAmount(double gratuityAmount) {
		this.gratuityAmount = gratuityAmount;
	}

	public double getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(double changeAmount) {
		this.changeAmount = changeAmount;
	}

	public void setCompletedTransaction(PosTransaction completedTransaction) {
		this.completedTransaction = completedTransaction;
	}

	public boolean isCard() {
		return isCard;
	}

	public void setCard(boolean isCard) {
		this.isCard = isCard;
	}
}
