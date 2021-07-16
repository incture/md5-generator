package oneapp.incture.workbox.demo.adhocTask.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

@Component
public class CompressImage {

	String ImageFormat = "";

	public String compress(String attachment, String fileType, Integer attachmentSize) {

		String comppressedScaled = "";

		double percent = 0.1;

		Integer attachmentSizeLimit = 102400;

		if (TaskCreationConstant.FILE_TYPE_PNG.equals(fileType))
			ImageFormat = "png";
		else if (TaskCreationConstant.FILE_TYPE_JPG.equals(fileType))
			ImageFormat = "jpg";
		else if (TaskCreationConstant.FILE_TYPE_JPEG.equals(fileType))
			ImageFormat = "jpeg";

		try {

			File input = convertBase64ToFile(attachment);
			BufferedImage image = ImageIO.read(input);

			if (!TaskCreationConstant.FILE_TYPE_PNG.equals(fileType)) {
				compressImage(image, input, attachmentSize);
			}
			// if(attachmentSizeLimit < attachmentSize)
			scaleImage(input, percent, image);

			comppressedScaled = convertFileToBase64(input);

		} catch (IOException e) {

			System.err.println("CompressImage.compress() error : "+e);
		}

		System.err.println(comppressedScaled);

		return comppressedScaled;
	}

	private String convertFileToBase64(File input) {
		try {
			byte[] encoded = Base64.getEncoder().encode(FileUtils.readFileToByteArray(input));
			return new String(encoded, StandardCharsets.US_ASCII);
		} catch (Exception e) {

		}
		return null;
	}

	private void scaleImage(File inputImagePath, double percent, BufferedImage inputImage) {
		try {

			int scaledWidth = (int) (inputImage.getWidth() * percent);
			int scaledHeight = (int) (inputImage.getHeight() * percent);

			BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
			Graphics2D g2d = outputImage.createGraphics();
			g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
			g2d.dispose();
			ImageIO.write(outputImage, ImageFormat, inputImagePath);

		} catch (Exception e) {

		}
	}

	private void compressImage(BufferedImage image, File input, Integer attachmentSize) {
		try {
			OutputStream os = new FileOutputStream(input);

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(ImageFormat);
			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			// if(attachmentSize > attachmentSizeLimit)
			param.setCompressionQuality(0.10f);
			// else
			// param.setCompressionQuality(0.10f);
			writer.write(null, new IIOImage(image, null, null), param);

			os.close();
			ios.close();
			writer.dispose();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	private File convertBase64ToFile(String attachment) {
		try {

			File file = File.createTempFile("NEW", "." + ImageFormat);
			byte[] data = Base64.getDecoder().decode(attachment.getBytes(StandardCharsets.UTF_8));
			try (OutputStream stream = new FileOutputStream(file)) {
				stream.write(data);
			}
			return file;

		} catch (IOException e) {

			System.err.println("CompressImage.convertBase64ToFile() error ; "+e);
		}
		return null;
	}

}
