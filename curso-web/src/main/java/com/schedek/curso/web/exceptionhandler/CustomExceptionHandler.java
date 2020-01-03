package com.schedek.curso.web.exceptionhandler;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.FacesException;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import org.primefaces.application.exceptionhandler.ExceptionInfo;
import org.primefaces.application.exceptionhandler.PrimeExceptionHandler;

public class CustomExceptionHandler extends PrimeExceptionHandler {

	private static final Logger LOG = Logger.getLogger(CustomExceptionHandler.class.getName());

	public CustomExceptionHandler(ExceptionHandler wrapped) {
		super(wrapped);
	}

	@Override
	public void handle() throws FacesException {
		FacesContext context = FacesContext.getCurrentInstance();

		if (context == null || context.getResponseComplete()) {
			return;
		}

		Iterable<ExceptionQueuedEvent> exceptionQueuedEvents = getUnhandledExceptionQueuedEvents();
		if (exceptionQueuedEvents != null && exceptionQueuedEvents.iterator() != null) {
			Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

			if (unhandledExceptionQueuedEvents.hasNext()) {
				try {
					Throwable throwable = unhandledExceptionQueuedEvents.next().getContext().getException();

					unhandledExceptionQueuedEvents.remove();

					Throwable rootCause = getRootCause(throwable);
					ExceptionInfo info = createExceptionInfo(throwable);

					if (isLogException(context, rootCause)) {
						logException(rootCause);
					}

					if (context.getPartialViewContext().isAjaxRequest()) {
						handleAjaxException(context, rootCause, info);
					} else {
						handleRedirect(context, rootCause, info, false);
					}
				} catch (Exception ex) {
					LOG.log(Level.SEVERE, "Could not handle exception!", ex);
				}
			}

			while (unhandledExceptionQueuedEvents.hasNext()) {
				unhandledExceptionQueuedEvents.next();
				unhandledExceptionQueuedEvents.remove();
			}
		}
	}

	@Override
	protected boolean isLogException(FacesContext context, Throwable rootCause) {
		if (rootCause != null && rootCause instanceof ViewExpiredException) {
			return false;
		}

		return true;
	}

}
