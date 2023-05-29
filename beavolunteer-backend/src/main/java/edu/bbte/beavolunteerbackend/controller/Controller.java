package edu.bbte.beavolunteerbackend.controller;

import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

public class Controller {

    public Blob prepareImage(MultipartFile file) throws SQLException, IOException {
        if (file.getOriginalFilename() == null) {
            return null;
        }
        byte[] bytes = file.getBytes();
        return new SerialBlob(bytes);
    }

}
