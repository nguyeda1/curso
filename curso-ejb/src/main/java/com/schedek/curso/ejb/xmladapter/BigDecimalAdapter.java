package com.schedek.curso.ejb.xmladapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalAdapter extends XmlAdapter<String, BigDecimal> {

	@Override
	public String marshal(BigDecimal v) throws Exception {
		DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(' ');
		dfs.setDecimalSeparator(',');
		DecimalFormat ni = new DecimalFormat("#,##0.00",dfs);
		ni.setMinimumFractionDigits(2);
		ni.setMaximumFractionDigits(2);
		return ni.format(v);
	}

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
		return new BigDecimal(v);
	}

}
