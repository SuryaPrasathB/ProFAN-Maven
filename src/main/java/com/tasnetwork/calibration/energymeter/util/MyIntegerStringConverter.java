package com.tasnetwork.calibration.energymeter.util;

import javafx.util.converter.IntegerStringConverter;

public class MyIntegerStringConverter extends IntegerStringConverter {
	@Override
	public Integer fromString(final String value) {
		return value.isEmpty() || !isNumber(value) ? null
				: super.fromString(value);
	}

	public boolean isNumber(String value) {
		int size = value.length();
		for (int i = 0; i < size; i++) {
			if (!Character.isDigit(value.charAt(i))) {
				if(!(value.charAt(i) == '.'))
				return false;
			}
		}
		return size > 0;
	}
}
