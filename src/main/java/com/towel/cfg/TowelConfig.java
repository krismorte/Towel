package com.towel.cfg;

import java.util.Locale;

public class TowelConfig {
	private Locale locale;

	public static TowelConfig instance;

	public static TowelConfig getInstance() {
		if (instance == null) {
			instance = new TowelConfig();
		}
		return instance;
	}

	private TowelConfig() {
		locale = new Locale("pt", "BR");
	}

	public Locale getDefaultLocale() {
		return locale;
	}

	public void setLocale(final Locale locale) {
		this.locale = locale;
	}
}
