/**
 * 
 */
package com.airsltd.app.pdfEditor;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Jon Boley
 *
 */
public class OutputPdf {

	private InputPdf f_template;
	private String f_outputFile;
	private List<Markup> f_markups = new ArrayList<Markup>();

	public OutputPdf(InputPdf p_template, String p_outputFile) {
		f_template = p_template;
		f_outputFile = p_outputFile;
	}

	public void addMarkup(Scanner p_lineScanner) {
		f_markups.add(new Markup(p_lineScanner));
	}
	
	/*
	 * Write the new PDF based on the original template and mark ups.
	 */
	public void writePdf() {
		Document l_document = new Document(f_template.getSize());
		PdfWriter l_writer;
		try {
			l_writer = PdfWriter.getInstance(l_document, new FileOutputStream(getOutputFile()));
			l_document.open();
			PdfContentByte l_cb = l_writer.getDirectContent();
			PdfImportedPage l_import = l_writer.getImportedPage(f_template.getReader(), 1);
			l_document.newPage();
			l_cb.addTemplate(l_import, 0, 0);
			for (Markup l_markup : f_markups) {
				l_markup.write(l_cb);
			}
			l_document.close();
		} catch (FileNotFoundException | DocumentException | MarkupInvalidFont e) {
			e.printStackTrace(System.err);
		}
	}

	public InputPdf getTemplate() {
		return f_template;
	}

	public String getOutputFile() {
		return f_outputFile;
	}

	public List<Markup> getMarkups() {
		return f_markups;
	}
	
	

}
