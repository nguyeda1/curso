package com.schedek.curso.ejb.util;

import com.schedek.curso.ejb.AppConfig;
import com.schedek.curso.ejb.AppEJB;
import com.schedek.curso.ejb.files.FileUtil;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;
import com.sun.imageio.plugins.jpeg.JPEGImageWriterSpi;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import javax.activation.MimetypesFileTypeMap;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.imageio.ImageIO;

/**
 *
 * @author Root
 */
@Stateless
public class IconUtil implements java.io.Serializable {

    @EJB
    AppEJB app;

    public static final MimetypesFileTypeMap mime = new MimetypesFileTypeMap();

    static {
        mime.addMimeTypes("image/jpeg jpg jpeg JPG JPEG");
        mime.addMimeTypes("image/png png PNG");
        mime.addMimeTypes("image/gif gif GIF");
    }

    public static class InvalidImageException extends Exception {
    }

    public String thumbPath(String path) {
        return thumbPath(path, "");
    }

    public String thumbPath(String path, String suffix) {
        AppConfig cfg = app.getConfig();
        if (path.startsWith(cfg.getDirPrefix())) {
            path = File.separator + path.substring(cfg.getDirPrefix().length());
        }
        String fn = cfg.getDirPrefix() + File.separator + "thumbs" + suffix + path;
        String tr = cfg.getThumbRoot();
        if (tr != null && !tr.isEmpty()) {
            fn = tr + File.separator + path;
        }
        if (fn.lastIndexOf(".") > fn.lastIndexOf(File.separator)) {
            fn = fn.substring(0, fn.lastIndexOf("."));
        }
        fn += ".png";
        File f = new File(fn.substring(0, fn.lastIndexOf(File.separator)));
        f.mkdirs();
        return fn;
    }

    public void createThumbnail(String imgFilePath, OutputStream o, int thumbWidth, int thumbHeight) throws Exception {

        Image image = Toolkit.getDefaultToolkit().getImage(imgFilePath);
        createThumbnail(image, o, thumbWidth, thumbHeight);
    }

    public void createSquareThumbnail(String imgFilePath, OutputStream o) throws Exception {

        Image image = Toolkit.getDefaultToolkit().getImage(imgFilePath);
        createSquareThumbnail(image, o);
    }

    public void createThumbnail(Image image, OutputStream o, int thumbWidth, int thumbHeight) throws Exception {
        if (image == null) {
            throw new InvalidImageException();
        }
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        mediaTracker.waitForID(0);
        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        double imageRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < imageRatio) {
            thumbHeight = (int) (thumbWidth / imageRatio);
        } else {
            thumbWidth = (int) (thumbHeight * imageRatio);
        }
        BufferedImage thumbImage = new BufferedImage(thumbWidth,
                thumbHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, thumbWidth, thumbHeight, null);
        graphics2D.dispose();

        BufferedOutputStream out = new BufferedOutputStream(o);

        //PNGImageWriter pw = new PNGImageWriter(new PNGImageWriterSpi());
        ImageIO.write(thumbImage, "jpeg", out);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//        int quality = 80;
//        param.setQuality((float) quality / 100.0f, true);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(thumbImage);

        out.close();
    }

    public void createSquareThumbnail(Image image, OutputStream o) throws Exception {
        if (image == null) {
            throw new InvalidImageException();
        }
        MediaTracker mediaTracker = new MediaTracker(new Container());
        mediaTracker.addImage(image, 0);
        mediaTracker.waitForID(0);

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        if (imageHeight > imageWidth) {
            //height = width
            imageHeight = imageWidth;
        }
        BufferedImage thumbImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, imageWidth, image.getHeight(null), null);
        graphics2D.dispose();

        BufferedOutputStream out = new BufferedOutputStream(o);

        //PNGImageWriter pw = new PNGImageWriter(new PNGImageWriterSpi());
        ImageIO.write(thumbImage, "jpeg", out);
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//        JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
//        int quality = 80;
//        param.setQuality((float) quality / 100.0f, true);
//        encoder.setJPEGEncodeParam(param);
//        encoder.encode(thumbImage);

        out.close();
    }

    public boolean checkFilename(File img) {
        return FileUtil.checkFilename(app.getConfig(), img);
    }
}
