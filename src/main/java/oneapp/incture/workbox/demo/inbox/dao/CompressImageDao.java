package oneapp.incture.workbox.demo.inbox.dao;

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
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import oneapp.incture.workbox.demo.inbox.util.FieldConstants;

@Repository
public class CompressImageDao {

	@Autowired
	private SessionFactory sessionFactory;

	public Session getSession() {
		try {
			return sessionFactory.getCurrentSession();
		} catch (Exception e) {
			return sessionFactory.openSession();
		}
	}

	String imageFormat = "";

	public String compressImage(String img, String fileType) {
		String comppressedScaled = "";

		double percent = 0.2;

		if (FieldConstants.FILE_TYPE_PNG.equals(fileType))
			imageFormat = "PNG";
		else if (FieldConstants.FILE_TYPE_JPG.equals(fileType))
			imageFormat = "JPG";
		else if (FieldConstants.FILE_TYPE_JPEG.equals(fileType))
			imageFormat = "JPEG";
		else if (FieldConstants.FILE_TYPE_PDF.equals(fileType))
			imageFormat = "PDF";
		try {
			if (FieldConstants.FILE_TYPE_PDF.equals(imageFormat)) {
				return img;
			}
			File input = convertBase64ToFile(img);
			BufferedImage image = ImageIO.read(input);

			if (!FieldConstants.FILE_TYPE_PNG.equals(imageFormat))
				compressImage(image, input);
			scaleImage(input, percent, image);

			comppressedScaled = convertFileToBase64(input);

		} catch (IOException e) {

			e.printStackTrace();
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

	public void compressImage(BufferedImage image, File input) {

		try {
			OutputStream os = new FileOutputStream(input);

			Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(imageFormat);
			ImageWriter writer = (ImageWriter) writers.next();

			ImageOutputStream ios = ImageIO.createImageOutputStream(os);
			writer.setOutput(ios);

			ImageWriteParam param = writer.getDefaultWriteParam();

			param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			param.setCompressionQuality(0.10f);

			writer.write(null, new IIOImage(image, null, null), param);

			os.close();
			ios.close();
			writer.dispose();
		} catch (Exception e) {
			System.err.println(e);
		}

	}

	public File convertBase64ToFile(String img) {

		try {

			File file = File.createTempFile("NEW", "." + imageFormat);
			byte[] data = Base64.getDecoder().decode(img.getBytes(StandardCharsets.UTF_8));
			try (OutputStream stream = new FileOutputStream(file)) {
				stream.write(data);
			}
			return file;

		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public void scaleImage(File inputImagePath, double percent, BufferedImage inputImage) throws IOException {
		try {

			int scaledWidth = 350;// (int) (inputImage.getWidth() * percent);
			int scaledHeight = 350;// (int) (inputImage.getHeight() * percent);

			BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());
			Graphics2D g2d = outputImage.createGraphics();
			g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
			g2d.dispose();
			ImageIO.write(outputImage, imageFormat, inputImagePath);

		} catch (Exception e) {

		}
	}

}
