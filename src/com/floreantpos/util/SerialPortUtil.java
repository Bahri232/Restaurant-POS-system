package com.floreantpos.util;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

import org.apache.commons.lang.StringUtils;

import com.floreantpos.PosLog;
import com.floreantpos.config.TerminalConfig;

public class SerialPortUtil {
	public static String readWeight(String comPort) throws SerialPortException {
		if (StringUtils.isBlank(comPort) || "NONE".equalsIgnoreCase(comPort.trim())) { //$NON-NLS-1$
			throw new SerialPortException(comPort, "readWeight", SerialPortException.TYPE_PORT_NOT_FOUND); //$NON-NLS-1$
		}

		// jSSC-2.8 native openPort() can crash Windows/Java 17 JVMs with EXCEPTION_ACCESS_VIOLATION
		// when no hardware scale is configured or enabled.
		if (!TerminalConfig.isActiveScaleDisplay() && !Boolean.getBoolean("floreantpos.enable.jssc")) { //$NON-NLS-1$
			throw new SerialPortException(comPort.trim(), "readWeight", "Scale display is disabled in peripheral settings."); //$NON-NLS-1$ //$NON-NLS-2$
		}

		final SerialPort serialPort = new SerialPort(comPort.trim());
		try {
			serialPort.openPort();//Open serial port
			serialPort.setParams(SerialPort.BAUDRATE_9600, SerialPort.DATABITS_7, SerialPort.STOPBITS_2, SerialPort.PARITY_EVEN);
			serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT | SerialPort.FLOWCONTROL_XONXOFF_IN
					| SerialPort.FLOWCONTROL_XONXOFF_OUT);

			final StringBuilder messageBuilder = new StringBuilder();

			serialPort.addEventListener(new SerialPortEventListener() {
				@Override
				public void serialEvent(SerialPortEvent event) {
					try {
						if (event.isRXCHAR() && event.getEventValue() > 0) {
							byte buffer[] = serialPort.readBytes();
							for (byte b : buffer) {
								if ((b == '\r' || b == '\n') && messageBuilder.length() > 0) {
									synchronized (messageBuilder) {
										messageBuilder.notify();
									}
									break;
								}
								else {
									messageBuilder.append((char) b);
								}
							}
						}
					} catch (Exception e) {
						PosLog.error(getClass(), e);
					}
				}
			});
			byte[] data = new byte[] { 0x57, 0x0D, 0 };
			serialPort.writeBytes(data);

			synchronized (messageBuilder) {
				try {
					messageBuilder.wait(2000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					return messageBuilder.toString();
				}
			}

			return messageBuilder.toString();
		} finally {
			try {
				if (serialPort.isOpened()) {
					serialPort.closePort();
				}
			} catch (Exception e) {
				PosLog.error(SerialPortUtil.class, e);
			}
		}
	}
}
