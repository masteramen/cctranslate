package cc.translate;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class CustomeFontReading4 {
    public static void main(String[] args) {
        
        Font[] fonts = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
                .getAllFonts();
        for (Font font : fonts) {
            System.out.println(font.getFontName());
        }
        System.out.println("字体数量:" + fonts.length);
    }
}