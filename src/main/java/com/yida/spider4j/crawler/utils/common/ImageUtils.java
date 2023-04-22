package com.yida.spider4j.crawler.utils.common;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.yida.spider4j.crawler.utils.io.FileUtils;

/**
 * @ClassName: ImageUtils
 * @Description: 图片处理工具类
 * @author Lanxiaowei
 * @date 2013-3-22 下午09:19:52
 */
public class ImageUtils {

	private static final int THRESHOLD = 50;

	/**
	 * @Title: pressImage
	 * @Description: 添加图片水印
	 * @param @param watermark
	 * @param @param targetImg
	 * @param @param x
	 * @param @param y
	 * @return void
	 * @throws
	 */
	public static void pressImage(String watermark, String targetImg, int x, int y) {
		FileOutputStream fos = null;
		try {
			File file = new File(targetImg);
			Image src = ImageIO.read(file);
			int wideth = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(wideth, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.createGraphics();
			g.drawImage(src, 0, 0, wideth, height, null);
			File fileImg = new File(watermark);
			Image srcImg = ImageIO.read(fileImg);
			int watermarkWideth = srcImg.getWidth(null);
			int watermarkHeight = srcImg.getHeight(null);
			g.drawImage(srcImg, wideth - watermarkWideth - x, height - watermarkHeight - y, watermarkWideth, watermarkHeight, null);
			g.dispose();
			fos = new FileOutputStream(targetImg);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			encoder.encode(image);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: pressImage
	 * @Description: 添加图片水印
	 * @param @param pressimg 水印图片
	 * @param @param resourceImage 原图片
	 * @param @param targetImage 添加水印后目标图片
	 * @param @param location 1、左上角，2、右上角，3、左下角，4、右下角，5、正中间
	 * @param @param alpha 透明度
	 * @return void
	 * @throws
	 */
	public static void pressImage(String pressimg, String resourceImage, String targetImage, int location, float alpha) {
		FileOutputStream fos = null;
		try {
			File img = new File(resourceImage);
			Image src = ImageIO.read(img);
			if (null == src) {
				return;
			}
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src, 0, 0, width, height, null);
			Image src_biao = ImageIO.read(new File(pressimg));
			int width_biao = src_biao.getWidth(null);
			int height_biao = src_biao.getHeight(null);
			// 如果水印图片高或者宽大于目标图片时,使其水印宽或高等于目标图片的宽高，并且等比例缩放
			int new_width_biao = width_biao;
			int new_height_biao = height_biao;
			if (width_biao > width) {
				new_width_biao = width;
				new_height_biao = (int) ((double) new_width_biao / width_biao * height);
			}
			if (new_height_biao > height) {
				new_height_biao = height;
				new_width_biao = (int) ((double) new_height_biao / height_biao * new_width_biao);
			}
			// 根据位置参数确定坐标位置
			int x = 0;
			int y = 0;
			int rx = GerneralUtils.generateRandomNumber(THRESHOLD, 1);
			int ry = GerneralUtils.generateRandomNumber(THRESHOLD, 1);
			switch (location) {
			case 1:
				x += rx;
				y += ry;
				break;
			case 2:
				x = width - new_width_biao;
				x -= rx;
				y += ry;
				break;
			case 3:
				x += rx;
				y = height - new_height_biao;
				y -= ry;
				break;
			case 4:
				x = width - new_width_biao;
				y = height - new_height_biao;
				x -= rx;
				y -= ry;
				break;
			case 5:
				x = (width - new_width_biao) / 2;
				y = (height - new_height_biao) / 2;
				break;
			default:
				x += rx;
				y += ry;
				break;
			}
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			g.drawImage(src_biao, x, y, new_width_biao, new_height_biao, null);
			g.dispose();
			if (null == targetImage || targetImage.equals("")) {
				fos = new FileOutputStream(resourceImage);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
				encoder.encode(image);
			} else {
				fos = new FileOutputStream(targetImage);
				JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
				encoder.encode(image);
			}
		} catch (Exception e) {
			return;
		} finally {
			try {
				if (null != fos) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: calSize
	 * @Description: 计算缩放尺寸
	 * @param @param width 原图宽度
	 * @param @param height 原图高度
	 * @param @param destWidth 目标宽度
	 * @param @param destHeight 目标高度
	 * @param @return
	 * @return int[]
	 * @throws
	 */
	public static int[] calSize(int width, int height, int destWidth, int destHeight) {
		if (width < destWidth && height < destHeight) {
			return new int[] { width, height };
		}
		int dw = 0;
		int dh = 0;
		float ratio = width * 1.0f / height;
		float datio = destWidth * 1.0f / destHeight;
		if (ratio < datio) {
			dh = destHeight;
			dw = (int) Math.round(ratio * dh * 1.0);
		} else if (ratio > datio) {
			dw = destWidth;
			dh = (int) Math.round((height * 1.0f / width) * dw * 1.0);
		} else {
			dw = destWidth;
			dh = destHeight;
		}
		return new int[] { dw, dh };
	}

	/**
	 * @Title: zoom
	 * @Description: 图片等比例缩放
	 * @param @param resourceImage
	 * @param @param targetImage
	 * @param @param destWidth
	 * @param @param destHeight
	 * @return void
	 * @throws
	 */
	public static void zoom(String resourceImage, String targetImage, int destWidth, int destHeight) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetImage);
			File img = new File(resourceImage);
			Image src = ImageIO.read(img);
			if (null == src) {
				return;
			}
			int width = src.getWidth(null);
			int height = src.getHeight(null);
			int[] result = calSize(width, height, destWidth, destHeight);
			int scalW = result[0];
			int scalH = result[1];
			BufferedImage image = new BufferedImage(scalW, scalH, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(src.getScaledInstance(scalW, scalH, Image.SCALE_SMOOTH), 0, 0, null);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
			encoder.encode(image);
			g.dispose();
		} catch (IIOException ex) {
			return;
		} catch (IOException e) {
			return;
		} finally {
			try {
				if (null != fos) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: writeImageToFile
	 * @Description: 图片写入磁盘文件
	 * @param @param imgFile
	 * @param @param bi
	 * @return void
	 * @throws
	 */
	public static void writeImageToFile(String imgFile, String stuffix, BufferedImage bi) {
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(stuffix);
		ImageWriter writer = (ImageWriter) writers.next();
		// 设置输出源
		File f = new File(imgFile);
		ImageOutputStream ios = null;
		try {
			ios = ImageIO.createImageOutputStream(f);
			writer.setOutput(ios);
			writer.write(bi);
		} catch (Exception e) {
			return;
		} finally {
			try {
				if (null != ios) {
					ios.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将一个像素点转为白色或者黑色。
	 * 
	 * @param pixel
	 * @return 转换后的像素点（黑/白）
	 */
	public static int pixelConvert(int pixel) {
		int result = 0;
		// 获取R/G/B
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = (pixel) & 0xff;
		// 默认黑色
		result = 0xff000000;
		int tmp = r * r + g * g + b * b;
		if (tmp > 3 * 128 * 128) {
			// 白色
			result += 0x00ffffff;
		}
		return result;
	}

	/**
	 * 获取本地磁盘图片文件并读入缓冲区
	 * 
	 * @param path
	 * @return
	 */
	public static BufferedImage getImage(String path) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e) {
			return null;
		}
		return image;
	}

	/**
	 * @Title: bufferImage2File
	 * @Description: BufferedImage写入文件
	 * @param @param image
	 * @param @param formateName
	 * @param @param filePath
	 * @param @return
	 * @return File
	 * @throws
	 */
	public static File bufferImage2File(BufferedImage image, String formateName, File file) {
		try {
			ImageIO.write(image, formateName, file);
		} catch (IOException e) {
			return null;
		}
		return file;
	}

	/**
	 * 将一张图片按照规则切分为N张
	 * 
	 * @param image
	 * @return
	 */
	public static BufferedImage[] cutCodes(BufferedImage image, int codeLength) {
		BufferedImage checkCode[] = new BufferedImage[codeLength];
		int height = image.getHeight();
		int width = image.getWidth();
		checkCode[0] = image.getSubimage(0 * (width / checkCode.length), 0, width / checkCode.length, height);
		checkCode[1] = image.getSubimage(1 * (width / checkCode.length) + 1, 0, width / checkCode.length - 1, height);
		checkCode[2] = image.getSubimage(2 * (width / checkCode.length), 0, width / checkCode.length - 3, height);
		checkCode[3] = image.getSubimage(3 * (width / checkCode.length) - 2, 0, width / checkCode.length, height);
		return checkCode;
	}

	/**
	 * 图片黑白化
	 * 
	 * @param image
	 * @return
	 */
	public static void blackAndWhiteFilter(BufferedImage image) {
		if (image == null) {
			return;
		}
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				image.setRGB(j, i, pixelConvert(image.getRGB(j, i)));
			}
		}
	}

	/**
	 * @Title: isolatePixelFilter
	 * @Description: 将孤立的像素点过滤为白色
	 * @param @param image
	 * @return void
	 * @throws
	 */
	public static void isolatePixelFilter(BufferedImage image) {
		if (image == null) {
			return;
		}
		for (int i = 0; i < image.getHeight(); i++) {
			for (int j = 0; j < image.getWidth(); j++) {
				if (i > 0 && j > 0 && i < (image.getHeight() - 1) && j < (image.getWidth() - 1)) {
					if (image.getRGB(j, i) == 0xff000000) {
						if (image.getRGB(j - 1, i) == 0xffffffff && image.getRGB(j - 1, i - 1) == 0xffffffff && image.getRGB(j, i - 1) == 0xffffffff && image.getRGB(j + 1, i) == 0xffffffff
								&& image.getRGB(j + 1, i + 1) == 0xffffffff && image.getRGB(j, i + 1) == 0xffffffff) {
							image.setRGB(j, i, 0xffffffff);
						}
					}
				}
			}
		}
	}

	/**
	 * @Title: toGray
	 * @Description: 彩色图片灰度化
	 * @param @param srcImage
	 * @param @param hints
	 * @param @return
	 * @return BufferedImage
	 * @throws
	 */
	public static BufferedImage toGray(BufferedImage srcImage, RenderingHints hints) {
		BufferedImage dstImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		if (hints == null) {
			Graphics2D g2 = dstImage.createGraphics();
			hints = g2.getRenderingHints();
			g2.dispose();
			g2 = null;
		}
		ColorSpace grayCS = ColorSpace.getInstance(ColorSpace.CS_GRAY);
		ColorConvertOp colorConvertOp = new ColorConvertOp(grayCS, hints);
		colorConvertOp.filter(srcImage, dstImage);
		return dstImage;
	}

	/**
	 * @Title: grayImage
	 * @Description: 图片灰度化
	 * @param @param bi
	 * @param @throws IOException
	 * @return void
	 * @throws
	 */
	public static void grayImage(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = bi.getRGB(x, y);
				Color color = new Color(rgb);
				int gray = (int) (0.3 * color.getRed() + 0.59 * color.getGreen() + 0.11 * color.getBlue());
				Color newColor = new Color(gray, gray, gray);
				bi.setRGB(x, y, newColor.getRGB());
			}
		}
	}

	/**
	 * @Title: reverseGray
	 * @Description: 灰度反转
	 * @param @param bi
	 * @return void
	 * @throws
	 */
	public static void reverseGray(BufferedImage bi) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = bi.getRGB(x, y);
				Color color = new Color(rgb);
				Color newColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
				bi.setRGB(x, y, newColor.getRGB());
			}
		}
	}

	/**
	 * @Title: grayImage
	 * @Description: 图片二值化
	 * @param @param bi
	 * @param @throws IOException
	 * @return void
	 * @throws
	 */
	public static void binaryImage(BufferedImage bi, int average) {
		int width = bi.getWidth();
		int height = bi.getHeight();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = bi.getRGB(x, y);
				Color color = new Color(rgb);
				int value = 255 - color.getBlue();
				if (value > average) {
					Color newColor = new Color(0, 0, 0);
					bi.setRGB(x, y, newColor.getRGB());
				} else {
					Color newColor = new Color(255, 255, 255);
					bi.setRGB(x, y, newColor.getRGB());
				}
			}
		}
	}

	/**
	 * @Title: loadImageFromRemote
	 * @Description: 加载远程图片
	 * @param @param url
	 * @param @return
	 * @return BufferedImage
	 * @throws
	 */
	public static BufferedImage loadImageFromRemote(String url) {
		byte[] buffer = null;
		BufferedImage image = null;
		try {
			buffer = FileUtils.getBinaryDataFromURL(url);
			if (null == buffer || buffer.length == 0) {
				return null;
			}
			image = ImageIO.read(new ByteArrayInputStream(buffer));
		} catch (IOException e1) {
			return null;
		}
		return image;
	}
	
	/**
	 * BufferedImage写入到JPG图片文件
	 * @param out    输出流
	 * @param image
	 */
	public static void bufferImage2File(OutputStream out,BufferedImage image) {
		try {
			ImageIO.write(image, "jpg", out);
		} catch (IOException e) {
			throw new RuntimeException("write image occur a exception.");
		}
	}
}
