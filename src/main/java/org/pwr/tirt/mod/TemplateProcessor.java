package org.pwr.tirt.mod;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.lowagie.text.DocumentException;

@Component
public class TemplateProcessor {

	@Autowired
	TemplateEngine templateEngine;

	public String generateHtmlString(String templatePath, Object object)
			throws IOException, DocumentException {
		Context ctx = new Context();

		ctx.setVariable("Subjects", object);
		return templateEngine.process(templatePath, ctx);
	}
        
}
