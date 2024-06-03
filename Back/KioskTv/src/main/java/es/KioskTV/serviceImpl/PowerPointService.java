package es.KioskTV.serviceImpl;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class PowerPointService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<String> convertPowerPointToImages(MultipartFile file) {
        List<String> imageUrl=new ArrayList<>();
        try {
            File tempFile = File.createTempFile("uploaded", ".pptx");
            file.transferTo(tempFile);
 
            InputStream inputStream = new FileInputStream(tempFile);
            XMLSlideShow ppt = new XMLSlideShow(inputStream);
            Dimension pgsize = ppt.getPageSize();
            int idx = 1;
 
            for (XSLFSlide slide : ppt.getSlides()) {
                BufferedImage img = new BufferedImage(pgsize.width, pgsize.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = img.createGraphics();
 
                // Clear the drawing area
                graphics.setPaint(Color.white);
                graphics.fill(new Rectangle(0, 0, pgsize.width, pgsize.height));
 
                // Render the slide
                slide.draw(graphics);
 
                // Save the output
                String fileName = "slide-"+file.getOriginalFilename()+ idx + ".png";
                Path outputPath = Paths.get(uploadDir, fileName);
                try (FileOutputStream out = new FileOutputStream(outputPath.toFile())) {
                    ImageIO.write(img, "png", out);
                }
                imageUrl.add(fileName);
                idx++;
            }
            ppt.close();
            return imageUrl;
        } catch (Exception e) {
            e.printStackTrace();
            return imageUrl;
        }
    }
}
