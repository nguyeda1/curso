package com.schedek.curso.web.beans.cc;

import com.schedek.curso.ejb.files.Filestore;
import com.schedek.curso.ejb.files.ManagedDir;
import com.schedek.curso.ejb.files.UploadType;
import com.schedek.curso.web.beans.sess.SessionBean;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import util.IconResolver;

@Named(value = "viewFiles")
@ViewScoped
public class ViewFiles implements Serializable {
    @Inject
    SessionBean sb;
    @EJB
    Filestore filestore;
    @Inject
    IconResolver ir;
    private Map<ComponentAttributes, ManagedDir> cwd = new HashMap<>();
    private Map<ComponentAttributes, List<File>> files = new HashMap<>();
    private Map<ComponentAttributes, List<ManagedDir>> dirs = new HashMap<>();
    private File fileRename;
    private String fileRenameNewName;
    private String fileRenameExtension;
    private String newDirName;

    public ViewFiles() {
    }

    public List<File> getFiles() {
        ComponentAttributes ca = ComponentAttributes.getInstance();
        if (!files.containsKey(ca)) {
            List<File> found = filestore.listFiles(ca.t, ca.o, cwd.get(ca));
            List<File> ret = new ArrayList<>(found);
            files.put(ca, ret);
        }
        return files.get(ca);
    }

    public List<ManagedDir> getDirs() {
        ComponentAttributes ca = ComponentAttributes.getInstance();
        if (!dirs.containsKey(ca)) {
            dirs.put(ca, filestore.listDirs(ca.t, ca.o, cwd.get(ca)));
        }
        return dirs.get(ca);
    }

    public File getZip(Long id) {
        List<File> files = getFiles();
        String filename = ComponentAttributes.getInstance().t.name() + " " + id + ".zip";
        File zipfile = new File(filename);
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try {
            // create the ZIP file
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipfile));
            // compress the files
            for (File file : files) {
                FileInputStream in = new FileInputStream(file);
                // add ZIP entry to output stream
                out.putNextEntry(new ZipEntry(file.getName()));
                // transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                // complete the entry
                out.closeEntry();
                in.close();
            }
            // complete the ZIP file
            out.close();
            return zipfile;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }


    public void handleFileUpload(FileUploadEvent event) {
        event.getComponent().setTransient(false);
        ComponentAttributes ca = ComponentAttributes.getInstance();
        UploadedFile file = event.getFile();
        try {
            filestore.uploadFile(ca.t, ca.o, cwd.get(ca), file.getFileName(), file.getInputstream());
            files.remove(ca);
        } catch (IOException ex) {
            Logger.getLogger(ViewFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected static class ComponentAttributes {

        Object o;
        UploadType t;

        private ComponentAttributes() {
        }

        static ComponentAttributes getInstance() {
            FacesContext ctx = FacesContext.getCurrentInstance();
            UIComponent comp = UIComponent.getCurrentComponent(ctx), c = comp;
            do {
                c = c.getParent();
                if (c == null) {
                    throw new RuntimeException("FileUpload not called within the component");
                }
            } while (!"javax.faces.Composite".equals(c.getRendererType()));
            Map<String, Object> attrs = c.getAttributes();
            ComponentAttributes r = new ComponentAttributes();
            r.o = attrs.get("refobj");
            r.t = (UploadType) attrs.get("type");
            return r;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + Objects.hashCode(this.o);
            hash = 71 * hash + Objects.hashCode(this.t);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ComponentAttributes other = (ComponentAttributes) obj;
            if (!Objects.equals(this.o, other.o)) {
                return false;
            }
            return this.t == other.t;
        }
    }

    public File getFileRename() {
        return fileRename;
    }

    public void setFileRename(File fileRename) {
        this.fileRename = fileRename;
        if (fileRename != null) {
            fileRenameNewName = this.fileRename.getName();
            fileRenameExtension = null;
            if (fileRenameNewName.contains(".")) {
                int p = fileRenameNewName.lastIndexOf(".");
                fileRenameExtension = fileRenameNewName.substring(p);
                fileRenameNewName = fileRenameNewName.substring(0, p);
            }
        }
    }

    public File getFileDelete() {
        return null;
    }

    public void setFileDelete(File fileDelete) {
        filestore.delete(fileDelete);
        ComponentAttributes ca = ComponentAttributes.getInstance();
        files.remove(ca);
    }

    public String renameFile() {
        String nn = fileRenameNewName;
        if (fileRenameExtension != null) {
            nn += fileRenameExtension;
        }
        filestore.rename(fileRename, nn);
        fileRename = null;
        fileRenameNewName = null;
        fileRenameExtension = null;
        ComponentAttributes ca = ComponentAttributes.getInstance();
        files.remove(ca);
        return null;
    }

    public String getNewDirName() {
        return newDirName;
    }

    public void setNewDirName(String newDirName) {
        this.newDirName = newDirName;
    }

    public String createDir() {
        ComponentAttributes ca = ComponentAttributes.getInstance();
        ManagedDir md = filestore.mkdir(ca.t, ca.o, cwd.get(ca), newDirName);
        newDirName = "";
        dirs.remove(ca);
        return null;
    }

    public ManagedDir getCwd() {
        ComponentAttributes ca = ComponentAttributes.getInstance();
        return cwd.get(ca);
    }

    public void setCwd(ManagedDir c) {
        ComponentAttributes ca = ComponentAttributes.getInstance();
        if (c == null) {
            cwd.remove(ca);
        } else {
            cwd.put(ca, c);
        }
        files.remove(ca);
        dirs.remove(ca);
    }

    public ManagedDir getDirRemove() {
        return null;
    }

    public void setDirRemove(ManagedDir dirRemove) {
        ComponentAttributes ca = ComponentAttributes.getInstance();
        filestore.delete(ca.t, ca.o, dirRemove);
        dirs.remove(ca);
    }

    public StreamedContent getFileStream(File f) throws FileNotFoundException {
        return new DefaultStreamedContent(new FileInputStream(f), "application/octet-stream", f.getName());
    }

    public StreamedContent getFileIcon(File f) {
        return ir.thumb(f);
    }

    public String getFileRenameNewName() {
        return fileRenameNewName;
    }

    public void setFileRenameNewName(String fileRenameNewName) {
        this.fileRenameNewName = fileRenameNewName;
    }

    public String getFileRenameExtension() {
        return fileRenameExtension;
    }

    public void setFileRenameExtension(String fileRenameExtension) {
        this.fileRenameExtension = fileRenameExtension;
    }

}
