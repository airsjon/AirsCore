/**
 * 
 */
package com.airsltd.app.pdfEditor;

import java.io.IOException;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;

/**
 * @author Jon Boley
 *
 */
public class InputPdf implements AutoCloseable {

	/*
	 * Name of the file that is being used as a template
	 */
	private String	f_inputPDF;
	/*
	 * PdfReader generated when the file is opened
	 */
	private PdfReader f_reader;
	/*
	 * Rectangle specifying the size of the PDF file's page
	 */
	private Rectangle f_size;
	
	public InputPdf(String p_inputPDF) {
		f_inputPDF = p_inputPDF;
	}
	
	public void loadTemplate() throws MarkupInvalidPDF {
		try {
			f_reader = new PdfReader(f_inputPDF);
			f_size = f_reader.getPageSize(1);
		} catch (IOException e) {
			throw new MarkupInvalidPDF(e);
		}
	}

	@Override
	public void close() throws Exception {
		if (f_reader!=null) {
			f_reader.close();
			f_reader = null;
		}
	}

	/**
	 * @return the size
	 */
	public Rectangle getSize() {
		return f_size;
	}

	/**
	 * @param p_size the size to set
	 */
	public void setSize(Rectangle p_size) {
		f_size = p_size;
	}

	public PdfReader getReader() {
		return f_reader;
	}

}
