package edu.bbte.beavolunteerbackend.service;

import java.sql.Blob;
import java.sql.SQLException;

public class ImgService {

    public byte[] getImg(Blob img) throws SQLException {
        if (img == null) {
            return new byte[0];
        }
        int blobLength = (int) img.length();
        return img.getBytes(1, blobLength);
    }
}
