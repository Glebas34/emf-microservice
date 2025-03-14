package com.emf_microservice.emf_microservice.Controllers;

import java.io.ByteArrayOutputStream;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.apache.poi.hemf.usermodel.HemfPicture;
import org.apache.poi.util.Units;
import java.awt.image.BufferedImage;
import java.awt.geom.Dimension2D;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import javax.imageio.ImageIO;

@RestController
@RequestMapping("/emf")
public class EmfController {

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convert(@RequestParam("file") MultipartFile file) {
        try {  
            HemfPicture emf = new HemfPicture(file.getInputStream());              
            Dimension2D dim = emf.getSize();            
            int width = Units.pointsToPixel(dim.getWidth());           
            // keep aspect ratio for height        
            int height = Units.pointsToPixel(dim.getHeight());             
            double max = Math.max(width, height);          
            if (max > 1500) {
            width *= 1500/max;
            height *= 1500/max;
            }
                
            BufferedImage bufImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = bufImg.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        
            emf.draw(g, new Rectangle2D.Double(0,0,width,height));
            
            g.dispose();
            
            ByteArrayOutputStream image = new ByteArrayOutputStream();
            ImageIO.write(bufImg, "PNG", image);
            image.flush();
            byte[] imageBytes = image.toByteArray();
            image.close();

            return ResponseEntity.ok().body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}