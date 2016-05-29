package com.evseev.imageProcessing;

import com.evseev.imageProcessing.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImageComparator {
    private int[][] firstImageArray;
    private int[][] secondImageArray;
    private int height;
    private int width;
    private List<ArrayList<Point>> areas;
    private BufferedImage resultImage;

    public ImageComparator(BufferedImage firstImage, BufferedImage secondImage) {
        this.firstImageArray = convertImageToPixelsArray(firstImage);
        this.secondImageArray = convertImageToPixelsArray(secondImage);
        height = firstImageArray.length;
        width = firstImageArray[0].length;
        if (height != secondImageArray.length || width != secondImageArray[0].length)
            throw new RuntimeException("Error. Images have different sizes.");
        resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public void compare() throws IOException {
        findAreas();
        markAreas();
        writeImage();
    }

    private int[][] convertImageToPixelsArray(BufferedImage bufferedImage) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int[][] pixelsArray = new int[height][width];
        for (int row = 0; row < height; row++) {
            bufferedImage.getRGB(0, row, width, 1, pixelsArray[row], 0, width);
        }
        return pixelsArray;
    }

    private void findAreas() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                resultImage.setRGB(j, i, secondImageArray[i][j]);
                double firstImgPntPercentage = Math.abs((double) firstImageArray[i][j] / Color.BLACK.getRGB() * 100);
                double secondImgPntPercentage = Math.abs((double) secondImageArray[i][j] / Color.BLACK.getRGB() * 100);
                if (Math.abs(firstImgPntPercentage - secondImgPntPercentage) > Constants.DIFFERENCE_IN_PERCENTS) {
                    points.add(new Point(j, i));
                }
            }
        }
        for (Point point : points) {
            if (areas == null) {
                areas = new ArrayList<ArrayList<Point>>();
                ArrayList<Point> firstArea = new ArrayList<>();
                firstArea.add(points.get(0));
                areas.add(firstArea);
            } else {
                if (!attachToArea(point)) {
                    ArrayList<Point> newArea = new ArrayList<>();
                    newArea.add(point);
                    areas.add(newArea);
                }
            }
        }
    }

    private boolean attachToArea(Point point) {
        for (int i = 0; i < areas.size(); i++) {
            for (int j = 0; j < areas.get(i).size(); j++) {
                int x = (int) areas.get(i).get(j).getX();
                int y = (int) areas.get(i).get(j).getY();
                if ((point.getX() < x + Constants.GAP) && (point.getX() > x - Constants.GAP)
                        && (point.getY() > y - Constants.GAP) && (point.getY() < y + Constants.GAP) ) {
                    areas.get(i).add(point);
                    return true;
                }
            }
        }
        return false;
    }

    private void markAreas() {
        for (int i = 0; i < areas.size(); i++) {
            int left = Integer.MAX_VALUE, right = 0, top = Integer.MAX_VALUE, bottom = 0;
            for (int j = 0; j < areas.get(i).size(); j++) {
                Point point = areas.get(i).get(j);
                if (point.getX() < left) left = (int) point.getX();
                if (point.getX() > right) right = (int) point.getX();
                if (point.getY() > bottom) bottom = (int) point.getY();
                if (point.getY() < top) top = (int) point.getY();
            }
            Graphics2D g2d = resultImage.createGraphics();
            g2d.setColor(Color.RED);
            g2d.drawRect(left, top, right - left, bottom - top);
            g2d.dispose();
        }
    }

    private void writeImage() throws IOException {
        File previousResult = new File(Constants.RESOURCE_PATH + Constants.RESULT_IMAGE);
        if (previousResult.exists())
            previousResult.delete();
        ImageIO.write(resultImage, "jpg", new File(Constants.RESOURCE_PATH + Constants.RESULT_IMAGE));
    }
}
