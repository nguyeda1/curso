package util;

import com.schedek.curso.ejb.AppConfig;
import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.files.FileUtil;
import com.schedek.curso.ejb.util.IconUtil;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.imageio.ImageIO;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Root
 */
@Named(value = "iconResolver")
@ApplicationScoped
public class IconResolver implements java.io.Serializable {

    @EJB
    AppEJB app;
    @EJB
    IconUtil iu;

    public StreamedContent thumb(File file) {
        return thumb(file, false);
    }

    public StreamedContent thumb(File file, boolean square) {
        String contentType;

        if (file == null) {
            return null;
        }

        try {
            contentType = iu.mime.getContentType(new File(file.getPath()));
            String fnl = file.getName().toLowerCase();
            if (contentType.startsWith("image/") || fnl.endsWith(".png")
                    || fnl.endsWith(".gif") || fnl.endsWith(".jpg") || fnl.endsWith(".jpeg")) {
                String thumbPath = iu.thumbPath(file.getPath());
                File thumb = new File(thumbPath);
                if ((!thumb.exists() || file.lastModified() > thumb.lastModified() || thumb.length() == 0) && !square) {
                    iu.createThumbnail(file.getPath(), new FileOutputStream(thumbPath), 200, 200);
                }
                if (square) {
                    iu.createSquareThumbnail(file.getPath(), new FileOutputStream(thumbPath));
                }
                FileInputStream fis = new FileInputStream(thumb);
                DefaultStreamedContent dsc = new DefaultStreamedContent(fis, "image/png");
                return dsc;
            }
        } catch (Exception ex) {
            Logger.getLogger(IconResolver.class.getName()).log(Level.INFO, "thumb generation error", ex);
        }

        return null;
    }
}
