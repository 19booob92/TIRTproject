package org.pwr.tirt.mod;

import java.io.IOException;

import org.pwr.tirt.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.sun.xml.messaging.saaj.util.ByteOutputStream;

@Component
public class PdfGenerator {

	@Autowired
	ScheduleRepository scheduleRepo;

	public byte[] createPdf(String htmlContent) throws IOException,
			DocumentException {

		byte[] pdfAsBytes;
		try (ByteOutputStream os = new ByteOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();

			renderer.setDocumentFromString(htmlContent);
			renderer.layout();
			renderer.createPDF(os);
			pdfAsBytes = os.getBytes();
		}

		return pdfAsBytes;
	}

}
