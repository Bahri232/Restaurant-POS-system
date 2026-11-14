package test.java.com.floreantpos;

import java.text.ParseException;
import java.util.Locale;

import jssc.SerialPortException;

import com.floreantpos.config.TerminalConfig;
import com.floreantpos.model.CashTransaction;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.TransactionType;
import com.floreantpos.util.DrawerUtil;
import com.floreantpos.util.NumberUtil;
import com.floreantpos.util.SerialPortUtil;

/**
 * Dependency-free headless smoke tests for critical cash settlement, number
 * parsing,
 * terminal configuration, and peripheral safety paths.
 */
public class PosSmokeTest {

	public static void main(String[] args) throws Exception {
		testNumberUtilParsing();
		testNumberUtilRoundingAndFormatting();
		testTerminalConfigDefaultsAndControlCodes();
		testDrawerAndScaleSafetyGuards();
		testCashTransactionDefaults();
		System.out.println("ALL POS SMOKE TESTS PASSED");
	}

	private static void testNumberUtilParsing() throws ParseException {
		assertEquals(0.0, NumberUtil.parse(null).doubleValue(), "parse(null)");
		assertEquals(0.0, NumberUtil.parse("").doubleValue(), "parse(empty)");
		assertEquals(0.0, NumberUtil.parse("   ").doubleValue(), "parse(blank)");
		assertEquals(0.0, NumberUtil.parse("Le").doubleValue(), "parse(Le only)");
		assertEquals(150.5, NumberUtil.parse("150.50").doubleValue(), "parse(150.50)");
		assertEquals(150.5, NumberUtil.parse("  Le 150.50  ").doubleValue(), "parse(Le 150.50)");
		assertEquals(200.0, NumberUtil.parse("Le200").doubleValue(), "parse(Le200)");
	}

	private static void testNumberUtilRoundingAndFormatting() {
		assertEquals(12.35, NumberUtil.roundToTwoDigit(12.345), "roundToTwoDigit half-up");
		assertEquals(0.0, NumberUtil.roundToTwoDigit(Double.NaN), "roundToTwoDigit NaN");
		assertEquals(0.0, NumberUtil.roundToTwoDigit(Double.POSITIVE_INFINITY), "roundToTwoDigit Infinity");

		assertEquals(1.235, NumberUtil.roundToThreeDigit(1.2345), "roundToThreeDigit half-up");
		assertEquals(0.0, NumberUtil.roundToThreeDigit(Double.NEGATIVE_INFINITY), "roundToThreeDigit -Infinity");

		assertEquals("0.00", NumberUtil.formatNumber((Double) null), "formatNumber(null)");
		assertEquals("0.00", NumberUtil.formatNumber(Double.NaN), "formatNumber(NaN)");
		assertEquals("0.000", NumberUtil.format3DigitNumber(Double.NaN), "format3DigitNumber(NaN)");
	}

	private static void testTerminalConfigDefaultsAndControlCodes() {
		assertTrue(!TerminalConfig.isCashDrawerKickEnabled(), "Cash drawer kick must default to false");
		char[] codes = TerminalConfig.getDrawerControlCodesArray();
		assertTrue(codes != null && codes.length >= 1, "Control codes array must not be empty");

		Locale defaultLocale = TerminalConfig.getDefaultLocale();
		assertTrue(defaultLocale == null || defaultLocale.getLanguage() != null, "Default locale lookup safe");
	}

	private static void testDrawerAndScaleSafetyGuards() {
		// Must safely no-op when disabled or port is invalid without calling native
		// jSSC DLLs
		DrawerUtil.kickDrawer();
		DrawerUtil.kickDrawer("", new char[] { 27, 112, 0, 25, 250 });
		DrawerUtil.kickDrawer("NONE", new char[] { 27, 112, 0, 25, 250 });

		boolean threwForBlankPort = false;
		try {
			SerialPortUtil.readWeight("");
		} catch (SerialPortException e) {
			threwForBlankPort = true;
		}
		assertTrue(threwForBlankPort, "SerialPortUtil.readWeight(\"\") must throw SerialPortException cleanly");

		boolean threwForDisabledScale = false;
		try {
			SerialPortUtil.readWeight("COM1");
		} catch (SerialPortException e) {
			threwForDisabledScale = true;
		}
		assertTrue(threwForDisabledScale,
				"SerialPortUtil.readWeight must reject native openPort when scale display is disabled");
	}

	private static void testCashTransactionDefaults() {
		CashTransaction tx = new CashTransaction();
		tx.setPaymentType(PaymentType.CASH.name());
		tx.setTransactionType(TransactionType.CREDIT.name());
		assertEquals("CASH", tx.getPaymentType(), "CashTransaction paymentType");
		assertEquals("CREDIT", tx.getTransactionType(), "CashTransaction transactionType");
	}

	private static void assertEquals(double expected, double actual, String label) {
		if (Math.abs(expected - actual) > 1e-6) {
			throw new AssertionError(label + ": expected=" + expected + " actual=" + actual);
		}
	}

	private static void assertEquals(String expected, String actual, String label) {
		if (expected == null ? actual != null : !expected.equals(actual)) {
			throw new AssertionError(label + ": expected='" + expected + "' actual='" + actual + "'");
		}
	}

	private static void assertTrue(boolean condition, String label) {
		if (!condition) {
			throw new AssertionError("Assertion failed: " + label);
		}
	}
}
