package ua.ryabuhin_valentine;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class GistImageUtil {

	private BufferedImage userImage;
	private BufferedImage templateOutImage;

	private final String wayInImage;
	private final String wayOutImage;

	int brightPixel[] = new int[256];

	/* Variables linked to the static template(template.jpg) */
	private final int TEMPLATE_COLUMN_H = 680;
	private final int TEMPLATE_COLUMN_W = 2;
	private final int TEMPLATE_OFFSET_L = 66;
	private final int TEMPLATE_OFFSET_D = 700;
	private final int TEMPLATE_INTERVAL_H = 10;
	private final int TEMPLATE_INTERVAL_COLUMN = 2;

	private final int BLACK_PIXEL = (0xFF << 24);

	public GistImageUtil(String wayIn, String wayOut) {
		this.wayInImage = wayIn;
		this.wayOutImage = wayOut;
	}

	/**
	 * Starting processing lifecycle
	 */
	public void run() {

		initImages();
		scalingImageToTemplate();
		countingPixelBright();
		drawGist();
		writeImage();

	}

	/**
	 * Function to initialize the path of the incoming and outgoing image
	 */
	private void initImages() {
		try {
			templateOutImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("template.jpg"));
			userImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream(wayInImage));
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception with generating File into BufferedImage stream");
		}
	}

	/**
	 * Scaling images up to 1024x768. <br>
	 * P.S: The proportion still remains the same, so is convenient for
	 * installation interval value on the vertical axis
	 */
	private void scalingImageToTemplate() {
		Image imageScaled = userImage.getScaledInstance(1024, 768, Image.SCALE_AREA_AVERAGING);
		BufferedImage changedUserImage = new BufferedImage(1024, 768, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = changedUserImage.createGraphics();
		g2d.drawImage(imageScaled, 0, 0, null);
		g2d.dispose();
		userImage = new BufferedImage(1024, 768, userImage.getType());
		userImage.setData(changedUserImage.getRaster());
	}

	/**
	 * 
	 * @param pixel
	 *            - Pixel brightness value which will be calculated
	 * @return brightness value in <b>integer</b>
	 */
	private int computeBrightness(int pixel) {
		int brightness = 0;
		int red = (pixel >> 16) & 0xFF;
		int green = (pixel >> 8) & 0xFF;
		int blue = pixel & 0xFF;
		brightness = (int) Math.round(0.2126 * red + 0.7152 * green + 0.0722 * blue);
		return brightness;
	}

	/**
	 * Collect statistics pixel-brightness
	 */
	public void countingPixelBright() {
		/* Counting brightness of all pixels users image */
		for (int i = 0; i < userImage.getHeight(); i++) {
			for (int j = 0; j < userImage.getWidth(); j++) {
				brightPixel[computeBrightness(userImage.getRGB(j, i))]++;
			}
		}
	}

	/**
	 * Draw the histogram of the image to the template.jpg
	 */
	private void drawGist() {
		for (int i = 0; i < brightPixel.length; i++) {
			/* Calculate column height */
			int column_height = 0;
			for (int z = 0; z < TEMPLATE_COLUMN_H; z++) {
				if (z * TEMPLATE_INTERVAL_H < brightPixel[i]) {
					if (z * TEMPLATE_INTERVAL_H / brightPixel[i] < 0.5) {
						column_height = z;
					} else {
						column_height = ++z;
					}
				}
			}
			int brInterval = (i * 2) + TEMPLATE_OFFSET_L + (TEMPLATE_INTERVAL_COLUMN * i);
			for (int y = 0; y < column_height; y++) {
				for (int x = 0; x < TEMPLATE_COLUMN_W; x++) {
					templateOutImage.setRGB((x + brInterval), TEMPLATE_OFFSET_D - y, BLACK_PIXEL);
				}
			}
		}
	}

	/**
	 * Write and save histogram in the right resolution and name
	 */
	private void writeImage() {
		try {
			ImageIO.write(templateOutImage, wayOutImage.substring(wayOutImage.indexOf('.') + 1, wayOutImage.length()),
					new File(wayOutImage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
