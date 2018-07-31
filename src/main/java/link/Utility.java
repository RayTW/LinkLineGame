package link;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import link.util.ImageCache;
import link.util.ImageTool;

public final class Utility {
	private static Utility sInstance = new Utility();

	/** 載圖、裁圖、縮放圖工具 */
	private ImageTool mImgTool = new ImageTool();

	/** 暫存圖片 */
	private ImageCache mImgCache = new ImageCache();

	private Utility() {
	}

	public static Utility get() {
		return sInstance;
	}

	public void loadImages() {
		mImgCache.put("restart.png", mImgTool.loadImg("Img/restart.png", this).getImage());
		mImgCache.put("background.jpg", mImgTool.loadImg("Img/background.jpg", this).getImage());

		AtomicInteger counter = new AtomicInteger();

		cut("Img/poker.png", 148, 215, (image) -> {
			String imageName = "poker_" + counter.get() + ".png";
			mImgCache.put(imageName, image);

			counter.incrementAndGet();
		});
	}

	public ImageCache getImageCache() {
		return mImgCache;
	}

	public ImageTool getImageTool() {
		return mImgTool;
	}

	/**
	 * @param srcImageFile
	 * @param descImageFile
	 * @param destWidth
	 * @param destHeight
	 */
	public void cut(String srcImageFile, int destWidth, int destHeight, Consumer<Image> listener) {
		try {
			BufferedImage bi = imageToBufferedImage(mImgTool.loadImg(srcImageFile, this).getImage());
			int srcWidth = bi.getWidth();
			int srcHeight = bi.getHeight();

			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int cols = 0;
				int rows = 0;
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				Image img;
				ImageFilter cropFilter;

				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, (j + 1) * destWidth,
								(i + 1) * destHeight);
						img = Toolkit.getDefaultToolkit()
								.createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_ARGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null);
						g.dispose();
						if (listener != null) {
							listener.accept(bufferedImagetoImage(tag));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage imageToBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		return bimage;
	}

	public Image bufferedImagetoImage(BufferedImage bi) {
		return Toolkit.getDefaultToolkit().createImage(bi.getSource());
	}

	public BufferedImage reduce(final BufferedImage original, double scale) {
		final int w = new Double(original.getWidth() * scale).intValue();
		final int h = new Double(original.getHeight() * scale).intValue();
		final Image rescaled = original.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
		final BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g = result.createGraphics();
		g.drawImage(rescaled, 0, 0, null);
		g.dispose();
		return result;
	}
}
