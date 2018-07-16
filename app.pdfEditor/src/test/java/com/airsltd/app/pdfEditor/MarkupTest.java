package com.airsltd.app.pdfEditor;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import java.io.IOException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;

public class MarkupTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMarkup() {
		// given
		Scanner l_scanner = new Scanner("3 4.2 5 test Times-Roman RIGHT");
		Scanner l_scannerByComma = new Scanner("4.7,4.5,5,test of much,Times Roman,LEFT");
		l_scannerByComma.useDelimiter(",");
		// when
		Markup l_one = new Markup(l_scanner);
		Markup l_two = new Markup(l_scannerByComma);
		// then
		assertEquals(3, l_one.getX(),0.001f);
		assertEquals(4.2, l_one.getY(),0.001f);
		assertEquals(5, l_one.getSize());
		assertEquals("test", l_one.getText());
		assertEquals("Times-Roman", l_one.getFontName());
		assertEquals(MarkupAlignment.RIGHT, l_one.getAlignment());
		assertEquals(4.7, l_two.getX(),0.001f);
		assertEquals(4.5, l_two.getY(),0.001f);
		assertEquals(5, l_two.getSize());
		assertEquals("test of much", l_two.getText());
		assertEquals("Times Roman", l_two.getFontName());
		assertEquals(MarkupAlignment.LEFT, l_two.getAlignment());
	}

	@Test
	public void testWrite() throws DocumentException, IOException, MarkupInvalidFont {
		// given
		PdfImportedPage m_page = mock(PdfImportedPage.class);
		Scanner l_scannerByComma = new Scanner("4.7,4.5,5,test of much,Times-Roman,LEFT");
		l_scannerByComma.useDelimiter(",");
		BaseFont l_font = BaseFont.createFont("Times-Roman", "UTF-8", true);
		// when
		Markup l_one = new Markup(l_scannerByComma);
		l_one.write(m_page);
		// then
		verify(m_page).beginText();
		verify(m_page).setFontAndSize(l_font, 5);
		verify(m_page).showTextAligned(PdfContentByte.ALIGN_LEFT,"test of much",4.7f,4.5f,0);
		verify(m_page).endText();
	}

	@Test(expected=MarkupInvalidFont.class)
	public void testWriteInvalidFont() throws MarkupInvalidFont {
		// given
		PdfImportedPage m_page = mock(PdfImportedPage.class);
		Scanner l_scannerByComma = new Scanner("4.7,4.5,5,test of much,Times Roman,LEFT");
		l_scannerByComma.useDelimiter(",");
		// when
		Markup l_one = new Markup(l_scannerByComma);
		l_one.write(m_page);
		// then
	}
}
