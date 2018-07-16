package com.airsltd.app.pdfEditor;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.Rectangle;

public class InputPdfTest {

	private InputPdf f_inputPdf;

	@Before
	public void setUp() throws Exception {
		File l_resourcesDirectory = new File("src/test/resources");
		f_inputPdf = new InputPdf(l_resourcesDirectory.getAbsolutePath()+"/test.pdf");
	}

	@After
	public void tearDown() throws Exception {
		if (f_inputPdf!=null) {
			f_inputPdf.close();
		}
	}

	@Test
	public void testLoadTemplate() throws MarkupInvalidPDF {
		// given
		// when
		f_inputPdf.loadTemplate();
		// then
		assertEquals(new Rectangle(0, 0, 792, 612), f_inputPdf.getSize());
	}
	
	@Test(expected=MarkupInvalidPDF.class)
	public void testLoadTemplateThrow() throws Exception {
		// given
		try (InputPdf l_badInput = new InputPdf("badname.pdf")) {
			// when
			l_badInput.loadTemplate();
		}
		// then
	}

	@Test
	public void testSetSize() {
		// given
		Rectangle l_rectangle = new Rectangle(3, 5, 10, 20);
		// when
		f_inputPdf.setSize(l_rectangle);
		// then
		assertEquals(l_rectangle, f_inputPdf.getSize());
	}

	@Test
	public void testGetReader() {
		// given
		// when
		assertNull(f_inputPdf.getReader());
		// then
	}

}
