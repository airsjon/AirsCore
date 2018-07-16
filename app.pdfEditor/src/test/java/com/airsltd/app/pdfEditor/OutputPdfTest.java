package com.airsltd.app.pdfEditor;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Rectangle;

public class OutputPdfTest {

	private OutputPdf f_outputPdf;

	@Before
	public void setUp() throws Exception {
		File l_resourcesDirectory = new File("src/test/resources");
		InputPdf l_inputPdf = new InputPdf(l_resourcesDirectory.getAbsolutePath()+"/test.pdf");
		f_outputPdf = new OutputPdf(l_inputPdf, l_resourcesDirectory.getAbsolutePath()+"/outfile.pdf");
		Scanner l_scannerByComma = 
				new Scanner("393,250,30,Player Name,Times-Roman,CENTER,417,190,16,R,Times-Roman,LEFT,457,113,14,10th of October in the year 2016,Times-Roman,LEFT");
		l_scannerByComma.useDelimiter(",");
		f_outputPdf.addMarkup(l_scannerByComma);
		f_outputPdf.addMarkup(l_scannerByComma);
		f_outputPdf.addMarkup(l_scannerByComma);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAddMarkup() {
		// given
		// when
		// then
		assertEquals(3, f_outputPdf.getMarkups().size());
		assertEquals("Player Name", f_outputPdf.getMarkups().get(0).getText());
		assertEquals("R", f_outputPdf.getMarkups().get(1).getText());
		assertEquals("10th of October in the year 2016", f_outputPdf.getMarkups().get(2).getText());
	}

	@Test
	public void testWritePdf() throws MarkupInvalidPDF {
		// given
		f_outputPdf.getTemplate().loadTemplate();
		// when
		f_outputPdf.writePdf();
		// then
	}

	@Test
	public void testGetTemplate() throws MarkupInvalidPDF {
		// given
		// when
		f_outputPdf.getTemplate().loadTemplate();
		// then
		assertEquals(new Rectangle(0, 0, 792, 612), f_outputPdf.getTemplate().getSize());
	}

	@Test
	public void testGetOutputFile() {
		// given
		File l_resourcesDirectory = new File("src/test/resources");
		String l_outfile = l_resourcesDirectory.getAbsolutePath()+"/outfile.pdf";
		// when
		// then
		assertEquals(l_outfile, f_outputPdf.getOutputFile());
	}

}
