package ESSSImage;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        File file;
        //String path = Thread.currentThread().getContextClassLoader().getResource("resources/sample1.txt").getPath();

        if (validations(args)) {
            file = new File(args[1]);

            if(args[0].equals("-split")){
                System.out.println("Split image");
                split(file);
            }
            else if(args[0].equals("-blur")){
                int radious = (args.length > 2 && args[2] != null)? Integer.parseInt(args[2]) : 1;
                int central_weight = (args.length > 2 && args[3] != null)? Integer.parseInt(args[3]) : 1;
                System.out.println("Blur image with radius="+radious+" and central_weight="+central_weight);

                blur(file, radious, central_weight);
            }
            else{
                System.out.println("Command not found");
            }
        }
    }

    public static void split(File file){
        File out;
        BufferedImage image, imageR, imageG, imageB;
        try{
            image = ImageIO.read(file);
            Color colorR, colorG, colorB;
            int width = image.getWidth();
            int height = image.getHeight();
            int pixel;

            imageR= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageG = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageB = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    pixel = image.getRGB(row, col);

                    colorR = new Color((pixel & 0x00ff0000) >> 16, 0, 0);
                    colorG = new Color(0, (pixel & 0x0000ff00) >> 8, 0);
                    colorB = new Color(0, 0, (pixel & 0x000000ff));

                    imageR.setRGB(row, col, colorR.getRGB());//
                    imageG.setRGB(row, col, colorG.getRGB());//
                    imageB.setRGB(row, col, colorB.getRGB());//
                }
            }

            out = new File("imageR.jpg");  //output file path
            ImageIO.write(imageR, "jpg", out);
            out = new File("imageG.jpg");  //output file path
            ImageIO.write(imageG, "jpg", out);
            out = new File("imageB.jpg");  //output file path
            ImageIO.write(imageB, "jpg", out);
            System.out.println("Writing complete.");

        }catch(IOException e){
            System.out.println("Error: "+e);
        }
    }

    public static void blur(File file, int radius, int central_weight){
        File out;
        BufferedImage image, imageBlur;
        try{
            image = ImageIO.read(file);
            Color color;
            int width = image.getWidth();
            int height = image.getHeight();
            int pixel, avgR, avgG, avgB, weight, count;

            imageBlur= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int row = 0; row < width; row++) {
                for (int col = 0; col < height; col++) {
                    avgR = avgG = avgB =0;
                    count = central_weight-1;

                    for(int i=(-1*radius); i<=(radius); i++){
                        for(int j=(-1*radius); j<=(radius); j++){
                            if(row+i >= 0 && row+i < width && col+j >= 0 && col+j < height){
                                pixel = image.getRGB(row+i, col+j);
                                weight = (i==0 && j==0) ? central_weight : 1;

                                avgR += weight*((pixel & 0x00ff0000) >> 16);
                                avgG += weight*((pixel & 0x0000ff00) >> 8);
                                avgB += weight*((pixel & 0x000000ff));
                                count++;
                            }
                        }
                    }

                    color = new Color(avgR/count, avgG/count, avgB/count);
                    imageBlur.setRGB(row, col, color.getRGB());
                }
            }

            out = new File("imageBlur.jpg");  //output file path
            ImageIO.write(imageBlur, "jpg", out);
            System.out.println("Writing complete.");

        }catch(IOException e){
            System.out.println("Error: "+e);
        }
    }

    public static boolean validations(String[] args){
        if(args.length == 2 || args.length == 4){
                return true;
        }
        else{
            System.out.println("An error has occurred");
            System.out.println("Please enter a correct format of arguments");
        }
        System.exit(1);
        return false;
    }
}
