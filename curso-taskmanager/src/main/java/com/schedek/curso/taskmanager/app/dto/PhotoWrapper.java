/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto;

import com.schedek.curso.taskmanager.app.controllers.CaseController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class PhotoWrapper {

    private String stream;
    private String path;
    private String name;

    public PhotoWrapper() {
    }

    public PhotoWrapper(File f) {
        this.stream = encodeFileToBase64Binary(f);
        this.path = f.getPath();
        this.name = f.getName();
    }

    @XmlElement
    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    @XmlElement
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
            fileInputStreamReader.close();
        } catch (IOException ex) {
            Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encodedfile;
    }

}
