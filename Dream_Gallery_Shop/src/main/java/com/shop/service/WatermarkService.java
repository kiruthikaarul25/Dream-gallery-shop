package com.shop.service;

import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Service
public class WatermarkService {

    public byte[] addWatermark(String imagePath, String clientName) throws IOException {

        File file = new File(imagePath);
        BufferedImage original = ImageIO.read(file);
        if (original == null) throw new IOException("Cannot read image: " + imagePath);

        int width  = original.getWidth();
        int height = original.getHeight();

        BufferedImage watermarked = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = watermarked.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Draw original image
        g.drawImage(original, 0, 0, null);

        // 2. Diagonal tiled watermark — bigger font, higher opacity
        int fontSize = Math.max(28, width / 18);   // was width/30 → now width/18 (bigger)
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        String watermarkText = "© Dream Gallery  |  " + clientName;

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.38f)); // was 0.22 → now 0.38
        g.setColor(Color.WHITE);

        g.rotate(Math.toRadians(-35), (double) width / 2, (double) height / 2);

        int textWidth  = fm.stringWidth(watermarkText);
        int textHeight = fm.getHeight();

        int stepX = textWidth + 60;
        int stepY = textHeight + 40;

        int range = (int) Math.sqrt(width * width + height * height);

        for (int y = -range; y < range; y += stepY) {
            for (int x = -range; x < range; x += stepX) {
                // Shadow for readability
                g.setColor(new Color(0, 0, 0, 100));
                g.drawString(watermarkText, x + 2, y + 2);
                g.setColor(Color.WHITE);
                g.drawString(watermarkText, x, y);
            }
        }

        // 3. Reset rotation
        g.setTransform(new java.awt.geom.AffineTransform());

        // 4. Bottom-right corner credit — bigger, more visible
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f)); // was 0.55
        int smallSize = Math.max(16, width / 45); // was width/60
        g.setFont(new Font("Arial", Font.BOLD, smallSize));
        FontMetrics fm2 = g.getFontMetrics();
        String credit = "© Dream Gallery Photography — Personal Use Only";
        int cx = width  - fm2.stringWidth(credit) - 16;
        int cy = height - 14;

        // Shadow
        g.setColor(new Color(0, 0, 0, 160));
        g.drawString(credit, cx + 2, cy + 2);
        // Text
        g.setColor(new Color(255, 255, 255, 220));
        g.drawString(credit, cx, cy);

        g.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(watermarked, "jpg", baos);
        return baos.toByteArray();
    }
}