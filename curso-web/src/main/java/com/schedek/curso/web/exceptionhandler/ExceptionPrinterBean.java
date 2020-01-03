package com.schedek.curso.web.exceptionhandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.ApplicationScoped;

@Named(value = "exceptionPrinterBean")
@ApplicationScoped
public class ExceptionPrinterBean {

	public ExceptionPrinterBean() {
	}

	public List<Throwable> exCauses(Throwable t) {
		List<Throwable> ret = new ArrayList<Throwable>();
		do {
			ret.add(t);
			t = t.getCause();
		} while (t != null);
		return ret;

	}

	public String formatStackTrace(Throwable e) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		return sw.toString();
	}

}
