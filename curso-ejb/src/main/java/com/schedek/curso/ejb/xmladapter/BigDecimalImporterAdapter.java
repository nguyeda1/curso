package com.schedek.curso.ejb.xmladapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BigDecimalImporterAdapter extends XmlAdapter<String, BigDecimal> {

	@Override
	public String marshal(BigDecimal v) throws Exception {

		return v.toString();
	}

	@Override
	public BigDecimal unmarshal(String v) throws Exception {
            v = v.trim();
            v = v.replace(" ", "");
            v = v.replace(",", ".");
            return new BigDecimal(v);
	}

}
