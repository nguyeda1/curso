/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.schedek.curso.taskmanager.app.dto;

import com.schedek.curso.taskmanager.app.controllers.CaseController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.crypto.OctetStreamData;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author Dan Nguyen
 */
@XmlRootElement
public class PhotoListWrapper {

    List<String> files = new ArrayList();

    public PhotoListWrapper(List<File> files) {
        files.forEach(file -> {
           this.files.add(encodeFileToBase64Binary(file));
        });

    }

    public PhotoListWrapper() {
    }

    @XmlElement
    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    private static String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (IOException ex) {
            Logger.getLogger(CaseController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encodedfile;
    }

}
