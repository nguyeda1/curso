package com.schedek.curso.ejb;

import com.schedek.curso.ejb.util.SQLScriptRunner;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class DataImport implements Serializable {

    @Resource
    private EJBContext context;

    @PersistenceContext(unitName = "curso-ejbPU")
    private EntityManager em;

    public void importViews() {
        runSQL("DeleteViewTables.sql");
        importSQL("Views.sql");
        importSQL("Views-accounting.sql");
        importSQL("Permissions.sql");
    }
    

    private void importSQL(String fn) {
        UserTransaction utx = context.getUserTransaction();
        try {
            utx.begin();
            SQLScriptRunner sr = new SQLScriptRunner(em.unwrap(Connection.class));
            DataImport.runScript(sr, fn);
        } catch (Exception ex) {
            Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
            try {
                utx.setRollbackOnly();
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex1) {
                Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        try {
            utx.commit();
        } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException ex) {
            Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doImport() {
        UserTransaction utx = context.getUserTransaction();
        try {
            utx.begin();
            int ax = 1, ay = 5;
            Logger.getLogger(AppEJB.class.getName()).log(Level.INFO, "=== Transaction started (" + (ax++) + "/" + ay + ") ", new Object[]{});
            Connection connection = em.unwrap(Connection.class);
            SQLScriptRunner sr = new SQLScriptRunner(connection);
            Logger.getLogger(AppEJB.class.getName()).log(Level.INFO, "=== Connection unwrapped, starting import  (" + (ax++) + "/" + ay + ") ", new Object[]{});
            runScript(sr, "InitialData.sql");
            Logger.getLogger(AppEJB.class.getName()).log(Level.INFO, "=== Import finished, commiting (" + (ax++) + "/" + ay + ") ", new Object[]{});
            utx.commit();
            Logger.getLogger(AppEJB.class.getName()).log(Level.INFO, "=== Transaction commited ===", new Object[]{});
        } catch (Exception ex) {
            Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
            try {
                utx.setRollbackOnly();
                utx.rollback();
            } catch (IllegalStateException | SecurityException | SystemException ex1) {
                Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    static void runScript(SQLScriptRunner sr, String filename) {
        InputStream is = DataImport.class.getResourceAsStream(filename);
        if (is == null) {
            Logger.getLogger(AppEJB.class.getName()).log(Level.WARNING, "No import script found.", new Object[]{});
            return;
        }
        try {
            sr.runScript(new InputStreamReader(is, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            is.close();
        } catch (Exception ex) {
            Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void runSQL(String filename) {
        InputStream is = DataImport.class.getResourceAsStream(filename);
        if (is == null) {
            Logger.getLogger(AppEJB.class.getName()).log(Level.WARNING, "No import script found.", new Object[]{});
            return;
        }
        Scanner s = new Scanner(is).useDelimiter(";");
        while(s.hasNext()){
			String sql=s.next();
            UserTransaction utx = context.getUserTransaction();
            try {

                utx.begin();
                SQLScriptRunner sr = new SQLScriptRunner(em.unwrap(Connection.class));
                sr.runScript(new StringReader(sql + ";"));
            } catch (Exception ex) {
                Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
                try {
                    utx.setRollbackOnly();
                    utx.rollback();
                } catch (IllegalStateException | SecurityException | SystemException ex1) {
                    Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            try {
                utx.commit();
            } catch (RollbackException | HeuristicMixedException | HeuristicRollbackException | SecurityException | IllegalStateException | SystemException ex) {
                Logger.getLogger(DataImport.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
