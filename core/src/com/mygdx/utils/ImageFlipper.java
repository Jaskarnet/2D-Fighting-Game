package com.mygdx.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageFlipper {

    public static void flipImagesHorizontally(String srcFolder, String destFolder) {
        File srcDir = new File(srcFolder).getAbsoluteFile();
        File destDir = new File(destFolder).getAbsoluteFile();

        if (!srcDir.exists() || !srcDir.isDirectory()) {
            System.out.println("Ścieżka źródłowa musi być poprawnym folderem.");
            return;
        }

        if (!destDir.exists()) {
            destDir.mkdirs();
        }

        // Ustal ścieżki do narzędzi ImageMagick
        String magickConvertPath = "magick";
        String magickConvertCommand = "convert";

        File[] files = srcDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
                    try {
                        File destFile = new File(destDir, file.getName());

                        // Komenda do odwrócenia obrazu za pomocą ImageMagick
                        String[] command = {
                                magickConvertPath,
                                magickConvertCommand,
                                file.getAbsolutePath(),
                                "-flop",
                                destFile.getAbsolutePath()
                        };

                        Process process = Runtime.getRuntime().exec(command);
                        int exitCode = process.waitFor();

                        if (exitCode == 0) {
                            System.out.println("Obraz " + file.getName() + " został odwrócony pomyślnie.");
                        } else {
                            System.out.println("Wystąpił błąd podczas odwracania obrazu " + file.getName());
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
