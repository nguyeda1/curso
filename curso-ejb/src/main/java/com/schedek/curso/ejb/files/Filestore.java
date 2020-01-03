package com.schedek.curso.ejb.files;

import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.entities.Case;
import com.schedek.curso.ejb.entities.CaseActivity;
import com.schedek.curso.ejb.entities.Task;
import com.schedek.curso.ejb.entities.TaskActivity;
import com.schedek.curso.ejb.entities.User;
import com.schedek.curso.ejb.enums.ActivityType;
import com.schedek.curso.ejb.facade.ActivityFacade;
import com.schedek.curso.ejb.facade.UserFacade;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Lock;
import static javax.ejb.LockType.WRITE;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.BodyPartEntity;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class Filestore {

    @EJB
    AppEJB app;
    @Resource
    private SessionContext ctx;
    @EJB
    ActivityFacade af;
    @EJB
    UserFacade uf;

    @Lock(WRITE)
    public File uploadFile(UploadType t, Object entity, String filename, InputStream is) {
        return uploadFile(t, entity, null, filename, is, getUserFromContext());
    }

    @Lock(WRITE)
    public File uploadFile(UploadType t, Object entity, String filename, InputStream is, User u) {
        return uploadFile(t, entity, null, filename, is, u);
    }

    @Lock(WRITE)
    public File uploadFile(UploadType t, Object entity, ManagedDir parent, String filename, InputStream is) {
        return uploadFile(t, entity, null, filename, is, getUserFromContext());
    }

    @Lock(WRITE)
    public File uploadFile(UploadType t, Object entity, ManagedDir parent, String filename, InputStream is, User u) {
        File f = getCWD(t, entity, parent);
        if (!f.isDirectory() || !f.exists()) {
            f.mkdirs();
        }
        String rfn = FileUtil.assignFilename(f, filename);
        File dst = new File(f, rfn);
        if (!FileUtil.copyFile(is, dst) && dst.isFile()) {
            dst.delete();
            return null;
        }
        createLog(t, entity, 1, u);
        return dst;
    }

    @Lock(WRITE)
    public List<File> uploadFileList(UploadType t, Object entity, List<FormDataBodyPart> list, User u) {
        List<File> result = new ArrayList();

        for (FormDataBodyPart bodyPart : list) {
            BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPart.getEntity();
            String filename = bodyPart.getContentDisposition().getFileName();
            try (InputStream is = bodyPartEntity.getInputStream()) {
                File f = getCWD(t, entity, null);
                if (!f.isDirectory() || !f.exists()) {
                    f.mkdirs();
                }
                String rfn = FileUtil.assignFilename(f, filename);
                File dst = new File(f, rfn);
                if (!FileUtil.copyFile(is, dst) && dst.isFile()) {
                    dst.delete();
                    return null;
                }
                result.add(dst);
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(Filestore.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        createLog(t, entity, result.size(), u);
        return result;
    }

    public List<File> listFiles(UploadType t, Object entity) {
        return listFiles(t, entity, null);
    }

    public List<File> listFiles(UploadType t, Object entity, ManagedDir parent) {
        File d = getCWD(t, entity, parent);
        if (!d.isDirectory()) {
            return new ArrayList<>();
        }
        return Arrays.asList(d.listFiles(new DirFilter(false)));
    }

    public List<ManagedDir> listDirs(UploadType t, Object entity, ManagedDir parent) {
        File d = getCWD(t, entity, parent);
        if (!d.isDirectory()) {
            return new ArrayList<>();
        }
        List<ManagedDir> ret = new ArrayList<ManagedDir>();
        for (File f : d.listFiles(new DirFilter(true))) {
            ret.add(new ManagedDir(f, parent));
        }
        return ret;
    }

    public File getCWD(UploadType t, Object entity, ManagedDir parent) {
        if (parent != null) {
            return parent.getFile();
        }
        return new File(app.getConfig().getDirPrefix()
                + File.separator
                + t.dirName(entity)
                + File.separator);

    }

    public void delete(File fileDelete) {
        fileDelete.delete();
    }

    public void rename(File fileRename, String nn) {
        File n = new File(fileRename.getParentFile(), nn);
        if (!n.getParent().equals(fileRename.getParent())) {
            throw new UnsupportedOperationException("Move not supported yet.");
        }
        fileRename.renameTo(n);
    }

    public ManagedDir mkdir(UploadType t, Object o, ManagedDir get, String newDirName) {
        File f = new File(getCWD(t, o, get), newDirName);
        f.mkdirs();
        return new ManagedDir(f, get);
    }

    public void delete(UploadType t, Object o, ManagedDir dirRemove) {
        for (File listFile : listFiles(t, o, dirRemove)) {
            throw new UnsupportedOperationException("Recursive delete not supported yet.");
        }
        for (ManagedDir listDir : listDirs(t, o, dirRemove)) {
            throw new UnsupportedOperationException("Recursive delete not supported yet.");
        }
    }

    private static class DirFilter implements FileFilter {

        private boolean dir = false;

        public DirFilter() {
        }

        public DirFilter(boolean dir) {
            this.dir = dir;
        }

        @Override
        public boolean accept(File pathname) {
            return ((!dir) ^ pathname.isDirectory());
        }
    }

    private void createLog(UploadType t, Object entity, Integer size, User u) {
        if (t.equals(UploadType.CASE_IMAGE)) {
            Case c = (Case) entity;
            CaseActivity a = buildLog(c, u, size);
            a.setType(ActivityType.CASE_PHOTO);
            a.setCursoCase(c);
            af.createLog(a, u);
        } else if (t.equals(UploadType.TASK_IMAGE)) {
            Task c = (Task) entity;
            TaskActivity a = buildLog(c, u, size);
            a.setType(ActivityType.TASK_PHOTO);
            a.setTask(c);
            af.createLog(a, u);
        }
    }

    private CaseActivity buildLog(Case c, User u, Integer number) {
        if (c != null) {
            StringBuilder builder = new StringBuilder();
            CaseActivity a = new CaseActivity();
            a.setCursoCase(c);
            builder.append("<strong>")
                    .append(u.getFullname())
                    .append("</strong> uploaded ")
                    .append(number)
                    .append(" ")
                    .append(number > 1 ? "photos" : "photo")
                    .append(" on case <strong>")
                    .append(c.getLabel())
                    .append("</strong>")
                    .append("\n");
            a.setLog(builder.toString());
            return a;
        }
        return null;
    }

    private TaskActivity buildLog(Task c, User u, int number) {
        if (c != null) {
            StringBuilder builder = new StringBuilder();
            TaskActivity a = new TaskActivity();
            a.setTask(c);
            a.setType(ActivityType.CASE_PHOTO);
            builder.append("<strong>")
                    .append(u.getFullname())
                    .append("</strong> uploaded ")
                    .append(number)
                    .append(" ")
                    .append(number > 1 ? "photos" : "photo")
                    .append(" on task <strong>")
                    .append(c.getLabel())
                    .append("</strong>")
                    .append("\n");
            a.setLog(builder.toString());
            return a;
        }
        return null;
    }

    private User getUserFromContext() {
        User u = null;
        if (ctx != null && ctx.getCallerPrincipal() != null) {
            String p = ctx.getCallerPrincipal().getName();
            if (p != null) {
                u = uf.findOneByUsername(p);
            }
        }
        if (u == null) {
            throw new EJBException("Error getting User from context");
        }
        return u;
    }
}
