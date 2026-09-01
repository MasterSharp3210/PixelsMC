import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class Main {

    private static class BlockData {
        final BufferedImage texture;
        final int avgR, avgG, avgB;

        BlockData(BufferedImage texture, Color averageColor) {
            this.texture = texture;
            this.avgR = averageColor.getRed();
            this.avgG = averageColor.getGreen();
            this.avgB = averageColor.getBlue();
        }
    }

    public static void main(String[] args) {
        String inputPath;
        System.out.print("Insert the input path: ");
        try (Scanner scanner = new Scanner(System.in)) {
            inputPath = scanner.nextLine().trim().replaceAll("^\"|\"$", "");
        }

        try {
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                System.err.println("Error: File does not exist at path: " + inputFile.getAbsolutePath());
                return;
            }

            BufferedImage inputImage = ImageIO.read(inputFile);
            if (inputImage == null) {
                System.err.println("Error: Could not load input image. Unsupported format.");
                return;
            }

            List<BlockData> blocks = loadBlockTextures("assets");
            if (blocks.isEmpty()) {
                System.out.println("No textures found in assets directory...");
                return;
            }

            int inputWidth = inputImage.getWidth();
            int inputHeight = inputImage.getHeight();
            int tileSize = 16;
            int processedPixels = 0;
            int totalPixels = inputWidth * inputHeight;

            BufferedImage outputImage = new BufferedImage(
                    inputWidth * tileSize,
                    inputHeight * tileSize,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g2d = outputImage.createGraphics();

            for (int x = 0; x < inputWidth; x++) {
                for (int y = 0; y < inputHeight; y++) {
                    processedPixels++;

                    if (processedPixels % 100 == 0 || processedPixels == totalPixels) {
                        System.out.print("\r" + processedPixels + "/" + totalPixels);
                    }

                    int argb = inputImage.getRGB(x, y);
                    int alpha = (argb >> 24) & 0xFF;

                    if (alpha == 0) continue;

                    int r = (argb >> 16) & 0xFF;
                    int g = (argb >> 8) & 0xFF;
                    int b = argb & 0xFF;

                    BlockData bestBlock = findClosestBlock(r, g, b, blocks);
                    g2d.drawImage(bestBlock.texture, x * tileSize, y * tileSize, tileSize, tileSize, null);
                }
            }

            g2d.dispose();
            ImageIO.write(outputImage, "png", new File("output.png"));
            System.out.println("\nDone.");

        } catch (IOException e) {
            System.err.println("An error occurred while processing the image (Stack Trace):");
            e.printStackTrace();
        }
    }

    private static List<BlockData> loadBlockTextures(String dirPath) throws IOException {
        List<BlockData> blocks = new ArrayList<>();
        File dir = new File(dirPath);
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".png"));

        if (files != null) {
            for (File file : files) {
                BufferedImage img = ImageIO.read(file);
                if (img != null) {
                    Color avgColor = calculateAverageColor(img);
                    blocks.add(new BlockData(img, avgColor));
                }
            }
        }
        return blocks;
    }

    private static Color calculateAverageColor(BufferedImage img) {
        long sumR = 0, sumG = 0, sumB = 0;
        int count = 0;
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int argb = img.getRGB(x, y);
                int alpha = (argb >> 24) & 0xFF;
                if (alpha > 128) {
                    sumR += (argb >> 16) & 0xFF;
                    sumG += (argb >> 8) & 0xFF;
                    sumB += argb & 0xFF;
                    count++;
                }
            }
        }

        if (count == 0) return Color.BLACK;
        return new Color((int) (sumR / count), (int) (sumG / count), (int) (sumB / count));
    }

    private static BlockData findClosestBlock(int r, int g, int b, List<BlockData> blocks) {
        BlockData bestMatch = null;
        int minDistance = Integer.MAX_VALUE;

        for (BlockData block : blocks) {
            int dr = r - block.avgR;
            int dg = g - block.avgG;
            int db = b - block.avgB;
            int distance = dr * dr + dg * dg + db * db;

            if (distance < minDistance) {
                minDistance = distance;
                bestMatch = block;
            }
        }
        return bestMatch;
    }
}
