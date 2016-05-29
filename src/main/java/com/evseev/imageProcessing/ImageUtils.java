package com.evseev.imageProcessing;


import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {

    public static boolean validate(MultipartFile firstImage, MultipartFile secondImage) {
        if (firstImage.isEmpty() || secondImage.isEmpty())
            return false;
        if (!FilenameUtils.getExtension(firstImage.getOriginalFilename()).equalsIgnoreCase(Constants.JRG_EXTENSION) ||
                !FilenameUtils.getExtension(secondImage.getOriginalFilename()).equalsIgnoreCase(Constants.JRG_EXTENSION))
            return false;
        return true;
    }

    public static BufferedImage loadImage(MultipartFile loadedImage) throws IOException {
        BufferedImage imBuff = ImageIO.read(loadedImage.getInputStream());
        return imBuff;
    }
}
