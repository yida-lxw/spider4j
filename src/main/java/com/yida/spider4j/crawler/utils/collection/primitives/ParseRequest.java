package com.yida.spider4j.crawler.utils.collection.primitives;

import com.yida.spider4j.crawler.utils.collection.anno.GwtCompatible;

/**
 * A string to be parsed as a number and the radix to interpret it in.
 */
@GwtCompatible
final class ParseRequest {
	final String rawValue;
	final int radix;

	private ParseRequest(String rawValue, int radix) {
		this.rawValue = rawValue;
		this.radix = radix;
	}

	static ParseRequest fromString(String stringValue) {
		if (stringValue.length() == 0) {
			throw new NumberFormatException("empty string");
		}

		// Handle radix specifier if present
		String rawValue;
		int radix;
		char firstChar = stringValue.charAt(0);
		if (stringValue.startsWith("0x") || stringValue.startsWith("0X")) {
			rawValue = stringValue.substring(2);
			radix = 16;
		} else if (firstChar == '#') {
			rawValue = stringValue.substring(1);
			radix = 16;
		} else if (firstChar == '0' && stringValue.length() > 1) {
			rawValue = stringValue.substring(1);
			radix = 8;
		} else {
			rawValue = stringValue;
			radix = 10;
		}

		return new ParseRequest(rawValue, radix);
	}
}
