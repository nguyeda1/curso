package com.schedek.curso.web.servlets;

import com.schedek.curso.ejb.util.IconUtil;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.model.StreamedContent;
import util.IconResolver;

/**
 *
 * @author Root
 */
@WebServlet(name = "IconServlet", urlPatterns = {"/servlet/icon"})
public class IconServlet extends HttpServlet {

		@Inject
	IconResolver ir;
		@EJB IconUtil iu;

	
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletOutputStream sos = response.getOutputStream();


        try {
            String filename = request.getParameter("filename");
            File img = new File(filename);
            if (filename == null || !img.exists()) {
                response.sendError(404);
                return;
            }

			if(!iu.checkFilename(img))throw new ServletException("File not found.");
            StreamedContent sc = ir.thumb(img);
            response.setContentType(sc.getContentType());

            DataInputStream is = new DataInputStream(sc.getStream());
            byte[] buf = new byte[1024];
            while(is.available()>0){
                is.read(buf);
                sos.write(buf);
            }
            is.close();

        } finally {
            sos.flush();
            sos.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
