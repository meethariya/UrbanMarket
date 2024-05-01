/**
 * 28-Apr-2024
 * meeth
 */
package com.urbanmarket.emailservice.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.urbanmarket.emailservice.dto.ResponseAddressDto;
import com.urbanmarket.emailservice.dto.ResponseCustomerDto;
import com.urbanmarket.emailservice.dto.ResponseOrderDto;
import com.urbanmarket.emailservice.dto.ResponseProductDto;
import com.urbanmarket.emailservice.dto.ResponseTransactionDto;
import com.urbanmarket.emailservice.openfeign.OrderClient;
import com.urbanmarket.emailservice.openfeign.PaymentClient;
import com.urbanmarket.emailservice.openfeign.ProductClient;
import com.urbanmarket.emailservice.openfeign.UserClient;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service layer for email
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

	private final MessageSource source;

	private final JavaMailSender mailSender;

	private final OrderClient orderClient;

	private final UserClient userClient;

	private final ProductClient productClient;

	private final PaymentClient paymentClient;

	private Random random = new Random();

	@Value("${server.port}")
	String port;

	private static String projectName = "project.name";
	private static String projectEmail = "project.email";

	/**
	 * Fetch data from order service, user service, payment service and product
	 * service. Create pdf of order as receipt, attach it in mail. Send the mail to
	 * customer.
	 * 
	 * @param orderId UUID
	 * @throws MailException
	 * @throws MessagingException
	 * @throws IOException
	 */
	public void sendOrderReceipt(UUID orderId) throws MailException, MessagingException, IOException {
		log.info("EMAILSERVICE: Send order receipt @PORT: " + port);
		// order service
		ResponseOrderDto order = orderClient.getOrdersById(orderId);
		// customer service
		ResponseCustomerDto customer = userClient.getCustomerById(order.getCustomerId());
		// transaction service
		// TODO: uncomment once payment service is completed
		// ResponseTransactionDto transaction =
		// paymentClient.getTransactionById(order.getTransactionId());
		ResponseTransactionDto transaction = ResponseTransactionDto.builder()
				.id(UUID.fromString("ea9286f1-49f3-4d11-813c-d894da124fa0")).status("success").timestamp(new Date())
				.amount(5000D).build();
		// product service
		List<ResponseProductDto> products = productClient
				.getMultipleProductsById(order.getItems().keySet().toArray(new String[0]));

		// if receipt is generated successfully, send mail
		if (orderReceiptGenerator(order, customer, transaction, products)) {
			mailSender.send(prepareOrderRecieptMail(order, customer));
			// deleting receipt to save storage
			Files.delete(Paths.get(orderId + ".pdf"));
		}
	}

	/**
	 * Send email having OTP to verify user. Also return same OTP to frontend for
	 * verification.
	 * 
	 * @param email user email
	 * @return OTP
	 * @throws MessagingException
	 * @throws MailException
	 */
	public String verifyEmail(String email, String name) throws MailException, MessagingException {
		String otp = otpGenerator();
		mailSender.send(prepareVerifyUserMail(email, name, otp));
		return otp;
	}

	/**
	 * Send a welcome email
	 * 
	 * @param id user id
	 * @throws MessagingException
	 * @throws MailException
	 */
	public void welcome(Long id) throws MailException, MessagingException {
		ResponseCustomerDto customer = userClient.getCustomerById(id);
		mailSender.send(prepareWelcomeMail(customer));
	}

	/**
	 * Prepare email to welcome user.
	 * 
	 * @param customer responseCustomerDto
	 * @return mail
	 * @throws MessagingException
	 */
	private MimeMessage prepareWelcomeMail(ResponseCustomerDto customer) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(new InternetAddress(customer.getEmail()));
		String htmlBody = """
				<html>
				<head>
				    <meta charset="UTF-8">
				    <title>Welcome to&nbsp;""" + propertyValue(projectName) + """
				    !</title>
				</head>
				<body>
				<h2>""" + (customer.getGender() == 'M' ? "Mr. " : "Ms. ")
				+ WordUtils.capitalizeFully(customer.getName()) + """
						,</h2>
						<p>Welcome to&nbsp;""" + propertyValue(projectName)
				+ """
						! We are thrilled to have you as a part of our community.</p>
						   <p>Your registration is now complete, and you are all set to explore everything our platform has to offer. Whether you're here to shop, connect with others, or access our services, we're here to make your experience enjoyable and rewarding.</p>
						   <p>As a valued member of&nbsp;"""
				+ propertyValue(projectName)
				+ """
						, you can look forward to:</p>
						<ul>
						    <li>Exclusive offers and promotions</li>
						    <li>Exciting new product launches</li>
						    <li>Personalized recommendations tailored just for you</li>
						    <li>A supportive community of like-minded individuals</li>
						</ul>
						<p>We're committed to providing you with the best possible experience, and we're always here to assist you with any questions or concerns you may have. Feel free to reach out to our friendly support team at&nbsp;"""
				+ propertyValue(projectEmail) + """
						   .</p>
						<p>Thank you for choosing&nbsp;""" + propertyValue(projectName) + """
						. We look forward to serving you, and we're here to assist you every step of the way.</p>

						<p>Best regards,<br>""" + propertyValue(projectName) + """
						</p>
						</body>
						</html>
						""";
		helper.setText(htmlBody, true);
		helper.setSubject("Welcome to " + propertyValue(projectName) + "!");
		return message;
	}

	/**
	 * Prepare email to verify user email-id using OTP
	 * 
	 * @param email user email
	 * @param name  user name
	 * @param otp   6digit
	 * @return mail
	 * @throws MessagingException
	 */
	private MimeMessage prepareVerifyUserMail(String email, String name, String otp) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(new InternetAddress(email));
		String htmlBody = """
				<html>
				<head>
				    <meta charset="UTF-8">
				    <title>Email Verification for Your Registration</title>
				</head>
				<body>
				<h2>Dear&nbsp;""" + WordUtils.capitalizeFully(name) + """
				,</h2>
				<p>Thank you for registering with&nbsp;""" + propertyValue(projectName)
				+ """
						. We are excited to welcome you to our platform!</p>
						<p>To ensure the security of your account and to complete the registration process, we need to verify your email address. This step is essential to protect your account from unauthorized access and to provide you with a seamless and secure experience.</p>
						<p>Please find below the One-Time Password (OTP) that you will need to enter on our website to verify your email address:</p>
						<p style="font-weight: bold;">OTP: """
				+ otp
				+ """
						</p>
						<p>Please enter this OTP on the registration page within 5 minutes to complete the verification process. If you did not initiate this registration or if you have any concerns, please contact our support team immediately at&nbsp;"""
				+ propertyValue(projectEmail) + """
						.</p>

						<p>Thank you for choosing&nbsp;""" + propertyValue(projectName) + """
						. We look forward to serving you, and we're here to assist you every step of the way.</p>

						<p>Best regards,<br>""" + propertyValue(projectName) + """
						</p>
						</body>
						</html>
						""";
		helper.setText(htmlBody, true);
		helper.setSubject("Email Verification for Your Registration");
		return message;
	}

	/**
	 * Generates 6 digit random OTP using {@link random}.
	 * 
	 * @return String of 6 digit OTP
	 */
	private String otpGenerator() {
		StringBuilder otp = new StringBuilder();
		this.random.ints(0, 10).limit(6).forEach(otp::append);
		return otp.toString();
	}

	/**
	 * Generates Order Receipt PDF for the given order using {@link PdfWriter}.
	 * Returns true if everything works well, any error goes to false.
	 * 
	 * @param order
	 * @param customer
	 * @param transaction
	 * @param allProducts
	 * @return completion status
	 * @throws IOException
	 */
	private boolean orderReceiptGenerator(ResponseOrderDto order, ResponseCustomerDto customer,
			ResponseTransactionDto transaction, List<ResponseProductDto> allProducts) throws IOException {
		String destination = order.getId() + ".pdf";

		// creating pdf components
		Document document = new Document();

		// document meta data
		document.addAuthor(propertyValue(projectName));
		document.addCreationDate();
		document.addCreator(propertyValue(projectName));
		document.addTitle("Order Reciept");

		// Fonts
		Font f0 = new Font(FontFamily.TIMES_ROMAN, 22, Font.BOLD, BaseColor.BLACK);
		Font f1 = new Font(FontFamily.TIMES_ROMAN, 18, Font.BOLD, BaseColor.BLACK);
		Font f2 = new Font(FontFamily.TIMES_ROMAN, 12, Font.NORMAL, BaseColor.BLACK);

		// writing to pdf
		try {
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(new File(destination)));
			document.open();
			
			// Get the PdfContentByte object to draw on the document
            PdfContentByte cb = writer.getDirectContentUnder();

            // Set the background color
            cb.setColorFill(new BaseColor(245, 245, 245)); // Use the color you need
            cb.rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight());
            cb.fill();
            
			// Company name
			document.add(createParagraph(propertyValue(projectName) + "\n", Element.ALIGN_CENTER, f0));
			ResponseAddressDto address = customer.getAddress().get(order.getAddressType());
			// Customer's address
			String addressLine2 = address.getAddressLine2() != null ? address.getAddressLine2() + ",\n" : "";
			String add = WordUtils.capitalizeFully(customer.getName()) + "\n" + address.getHouseNo() + ",\n"
					+ address.getAddressLine1() + ",\n" + addressLine2 + WordUtils.capitalizeFully(address.getCity())
					+ ", " + WordUtils.capitalizeFully(address.getState()) + "-" + address.getPincode() + "\n\n";
			document.add(createParagraph(add, Element.ALIGN_LEFT, f2));

			// Order Details
			document.add(createParagraph("Order Details\n", Element.ALIGN_CENTER, f1));

			// Order no & date
			document.add(createParagraph("\nId: " + order.getId() + "\nDate: " + order.getPlacedOn().toString() + "\n\n",
					Element.ALIGN_LEFT, f2));
			// Table
			document.add(createOrderReceiptTable(order, allProducts));

			// Transaction details
			document.add(createParagraph("\nTransaction details\n", Element.ALIGN_CENTER, f1));
			// Transaction id
			document.add(createParagraph(
					"\nId: " + transaction.getId() + "\nDate: " + transaction.getTimestamp().toString() + "\nAmount: "
							+ transaction.getAmount() + " Rs" + "\nStatus: " + transaction.getStatus() + "\n\n",
					Element.ALIGN_LEFT, f2));
			// Thanks Note
			document.add(createParagraph("Thank you for choosing " + propertyValue(projectName)
					+ " , If you have any queries please reach out to us at " + propertyValue(projectEmail) + "\nTeam "
					+ propertyValue(projectName), Element.ALIGN_LEFT, f2));
			// Company Logo
			String path = propertyValue("project.logo");
			Image image = Image.getInstance(path);
			image.scaleAbsoluteHeight(150);
			image.scaleAbsoluteWidth(150);
			image.setAbsolutePosition(document.getPageSize().getWidth() - image.getScaledWidth(), document.getPageSize().getHeight() - image.getScaledHeight());
			document.add(image);
			document.close();

		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Create new cell using text and font.
	 * 
	 * @param text cell content
	 * @param f    Font
	 * @return Cell
	 */
	private PdfPCell createCell(String text, Font f) {
		return new PdfPCell(createParagraph(text, Element.ALIGN_CENTER, f));
	}

	/**
	 * Creates Order table for it's receipt
	 * 
	 * @param order reciept creation
	 * @return table
	 * @throws DocumentException
	 */
	private PdfPTable createOrderReceiptTable(ResponseOrderDto order, List<ResponseProductDto> allProducts)
			throws DocumentException {
		// Table
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		// Define column widths
		table.setWidths(new float[] { 1f, 3f, 3f, 1.5f, 1.5f, 1.5f });
		// Fonts
		Font f3 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);

		Font f2 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

		// heading
		// sr no
		table.addCell(createCell("Sr No.", f3));
		// Product Name
		table.addCell(createCell("Product Name", f3));
		// Product Brand
		table.addCell(createCell("Product Brand", f3));
		// Price
		table.addCell(createCell("Price\n(in Rs)", f3));
		// Quantity
		table.addCell(createCell("Quantity", f3));
		// Total
		table.addCell(createCell("Total\n(in Rs)", f3));

		int srNo = 1;
		for (ResponseProductDto i : allProducts) {
			// sr no
			table.addCell(createCell(String.valueOf(srNo++), f2));
			// product name
			table.addCell(createCell(i.getName(), f2));
			// product brand
			table.addCell(createCell(i.getBrand(), f2));
			// product price
			table.addCell(createCell(String.valueOf(i.getPrice()), f2));
			// Quantity
			int quantity = order.getItems().get(i.getId());
			table.addCell(createCell(String.valueOf(quantity), f2));
			// Total
			table.addCell(createCell(String.valueOf(quantity * i.getPrice()), f2));
		}

		return table;
	}

	/**
	 * Creates new Paragraph for given text.
	 * 
	 * @param text      content of paragraph
	 * @param alignment paragraph alignment
	 * @param f         font
	 * @return
	 */
	private Paragraph createParagraph(String text, int alignment, Font f) {
		Paragraph temp = new Paragraph(text, f);
		temp.setAlignment(alignment);
		return temp;
	}

	/**
	 * Prepares mail with subject, body, recipient address and receipt attachment.
	 * 
	 * @param order
	 * @return Mail with attachment
	 * @throws MessagingException
	 */
	private MimeMessage prepareOrderRecieptMail(ResponseOrderDto order, ResponseCustomerDto customer)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(new InternetAddress(customer.getEmail()));
		String htmlBody = """
				<html>
				<head>
				    <meta charset="UTF-8">
				    <title>Order Confirmation</title>
				</head>
				<body>
				    <h1>Thank you for choosing&nbsp;""" + propertyValue(projectName) + """
				!</h1>
				<p>Dear&nbsp;""" + WordUtils.capitalizeFully(customer.getName())
				+ """
						,</p>
						<p>We are thrilled to have you as our valued customer.</p>
						<p>Your recent order has been successfully processed, and we're excited to let you know that your quality products are on their way to you. Your order receipt has been attached to this mail</p>
						<p>We're committed to providing you with the best shopping experience possible, and we hope you enjoy your selections. If you have any questions or need assistance, please don't hesitate to contact our friendly customer support team at &nbsp;"""
				+ propertyValue(projectEmail) + """
						</p>
						<p>Once again, thank you for shopping with&nbsp;""" + propertyValue(projectName) + """
						. We appreciate your support and look forward to serving you again soon!</p>
						<p>Warm regards,<br>
						""" + propertyValue(projectName) + """
						&nbsp; Team</p>
						</body>
						</html>
						""";
		helper.setText(htmlBody, true);
		FileSystemResource file = new FileSystemResource(new File(order.getId() + ".pdf"));
		helper.addAttachment("reciept.pdf", file);
		helper.setSubject("Order Reciept");
		return message;
	}

	/**
	 * @param key property key
	 * @return value of given property key
	 */
	private String propertyValue(String key) {
		return source.getMessage(key, null, Locale.ENGLISH);
	}
}
