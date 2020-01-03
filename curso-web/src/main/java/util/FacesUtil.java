/*
 * (c) Pavel Šedek 2009, all rights reserved
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJBException;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.view.Location;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableStyleInfo;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STTotalsRowFunction;

/**
 *
 * @author Root
 */
public class FacesUtil {

	private static FacesUtil inst = null;

	private FacesUtil() {
	}

	public static FacesUtil getInstance() {
		if (inst == null) {
			inst = new FacesUtil();
		}
		return inst;
	}

	public static void processDbException(String title, Exception e) {
		String msg = "Unknown error occured during database operation";
		if (e instanceof EJBException && e.getCause() != null && e.getCause() instanceof RollbackException
				&& e.getCause().getCause() != null && e.getCause().getCause() instanceof PersistenceException
				&& e.getCause().getCause().getCause() != null && e.getCause().getCause().getCause().getClass().getSimpleName().equals("DatabaseException")
				&& e.getCause().getCause().getCause().getCause() != null && e.getCause().getCause().getCause().getCause().getClass().getSimpleName().equals("PSQLException")) {
			msg = e.getCause().getCause().getCause().getCause().getMessage();
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, title, msg));
	}

	/**
	 * Instantly invokes a method using expression language.
	 *
	 * @param ctx
	 * @param expression
	 * @param expectedReturnType
	 * @param expectedParamTypes
	 * @param params
	 * @return
	 */
	public static <T> T invokeMethod(FacesContext ctx, String expression, Class<?> expectedReturnType,
			Class[] expectedParamTypes, Object[] params) {
		Application app = ctx.getApplication();
		ExpressionFactory ef = app.getExpressionFactory();
		ELContext elctx = ctx.getELContext();
		MethodExpression me = ef.createMethodExpression(elctx, expression, expectedReturnType, expectedParamTypes);
		return (T) me.invoke(elctx, params);
	}

	public static <T> T invokeMethod(FacesContext ctx, String expression, Class<?> expectedReturnType) {
		return invokeMethod(ctx, expression, expectedReturnType, new Class[]{}, new Object[]{});
	}

	public void invokeMethod(FacesContext ctx, String expression) {
		invokeMethod(ctx, expression, Void.class);
	}

	/**
	 * Gets value using expression language
	 *
	 * @param ctx
	 * @param expression
	 * @param expectedType
	 * @return
	 */
	public static <T> T getValue(FacesContext ctx, String expression, Class<T> expectedType) {
		ExpressionFactory exFactory = ctx.getApplication().getExpressionFactory();
		ValueExpression vex = exFactory.createValueExpression(ctx.getELContext(), expression, expectedType);
		return (T) vex.getValue(ctx.getELContext());
	}

	public static String getContextParam(FacesContext ctx, String name) {
		return ctx.getExternalContext().getInitParameter("com.schedek.simpleweb." + name);
	}

	public static String getLink(FacesContext ctx) {
//        HashMap evalmap = (HashMap) ctx.getAttributes().get("com.sun.faces.el.CompositeComponentAttributesELResolver_EVAL_MAP");
//        Map next = (Map) evalmap.values().iterator().next();
//        Location loc = (Location) next.get("javax.faces.component.VIEW_LOCATION_KEY");
//        return loc.getPath();
		return ctx.getExternalContext().getRequestServletPath();

	}

	public static String pfCalendarDate(Date d) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(d);
	}

	public static void addMessage(String m) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(m));
	}

	public static Object getEJB(Class c) {
		try {
			return new InitialContext().lookup(c.getSimpleName() + "/remote");
		} catch (NamingException ex) {
			return null;
		}
	}

	public static void sendRedirect(String target) {
		try {

			//FacesContext.getCurrentInstance().getApplication().getNavigationHandler().handleNavigation(FacesContext.getCurrentInstance(), null, target);
			String cp = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
			if (target.startsWith("/")) {
				target = cp + target;
			}
	//		target = target.replace(".xhtml", ".jsf");
			FacesContext.getCurrentInstance().getExternalContext().redirect(target);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static void throwError(FacesMessage.Severity s, String message) {
		/*
		 String label="";
		 ResourceBundle b = LanguageBean.getBundleMain();
		 if(FacesMessage.SEVERITY_FATAL.equals(s)){
		 label=b.getString("error.fatal");
		 }
		 if(FacesMessage.SEVERITY_ERROR.equals(s)){
		 label=b.getString("error.error");
		 }
		 if(FacesMessage.SEVERITY_WARN.equals(s)){
		 label=b.getString("error.warning");
		 }
		 if(FacesMessage.SEVERITY_INFO.equals(s)){
		 label=b.getString("error.notice");
		 }
		 FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(s, label, message));
		 */
	}

	public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext == null) {

			FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
			LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
			Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

			facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response, lifecycle);

			// Set using our inner class
			InnerFacesContext.setFacesContextAsCurrentInstance(facesContext);

			// set a new viewRoot, otherwise context.getViewRoot returns null
			UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
			facesContext.setViewRoot(view);
		}
		return facesContext;
	}

	private abstract static class InnerFacesContext extends FacesContext {

		protected static void setFacesContextAsCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}
	}

	public static void postProcessXLS(Object document, int cols, Integer[] colnum) {
		XSSFWorkbook wb = (XSSFWorkbook) document;
		DataFormat format = wb.createDataFormat();
		XSSFSheet sheet = wb.getSheetAt(0);
		XSSFTable my_table = sheet.createTable();
		List<Integer> al = Arrays.asList(colnum);

		for (Row myrow : sheet) {
			if (myrow.getRowNum() == 0) {
				continue;
			}
			for (Cell cell : myrow) {
				if (al.contains(cell.getColumnIndex())) {
					java.text.NumberFormat numf = java.text.NumberFormat.getInstance(FacesContext.getCurrentInstance().getViewRoot().getLocale());
					String v = cell.getStringCellValue();
					try {
						Number number = numf.parse(v);
						cell.setCellValue(number.doubleValue());
						CellStyle style = cell.getCellStyle();
						style.setDataFormat(format.getFormat("0.0"));
						cell.setCellStyle(style);
					} catch (ParseException ex) {
						Logger.getLogger(FacesUtil.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
		}

		CTTable cttable = my_table.getCTTable();
		CTTableStyleInfo table_style = cttable.addNewTableStyleInfo();
		table_style.setName("TableStyleMedium9");
		table_style.setShowColumnStripes(false); //showColumnStripes=0
		table_style.setShowRowStripes(true); //showRowStripes=1  

		AreaReference my_data_range = new AreaReference(new CellReference(0, 0), new CellReference(sheet.getLastRowNum() + 1, cols - 1));
		/* Set Range to the Table */
		cttable.setRef(my_data_range.formatAsString());
		cttable.setDisplayName("MYTABLE");      /* this is the display name of the table */

		cttable.setTotalsRowShown(true);
		cttable.setTotalsRowCount(1);

		// Set AutoFilter
		CTAutoFilter fltr = CTAutoFilter.Factory.newInstance();
		fltr.setRef(my_data_range.formatAsString());
		cttable.setAutoFilter(fltr);

		cttable.setName("Test");    /* This maps to "displayName" attribute in &lt;table&gt;, OOXML */

		cttable.setId(1L);
		CTTableColumns columns = cttable.addNewTableColumns();
		columns.setCount(cols);
		for (int i = 0; i < cols; i++) {
			CTTableColumn column = columns.addNewTableColumn();
			column.setName("Column" + i);
			column.setId(i + 1);
			if (i > 1) {
				column.setTotalsRowFunction(STTotalsRowFunction.Enum.forInt(2));
			}
		}

//		cttable.setTotalsRowShown(true);
//		cttable.setTotalsRowCount(1);
	}

}
