/**
 * 
 */
package com.airsltd.app.pdfEditor;

import java.io.IOException;
import java.util.Scanner;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;

/**
 * Initial implementation focuses on text mark up.
 * 
 * Text will include x,y,font-size,text{,font}
 * 
 * @author Jon Boley
 *
 */
public class Markup {

	private float f_x;
	private float f_y;
	private int f_size;
	private String f_text;
	private String f_fontName;
	private	MarkupAlignment f_alignment;
	
	/**
	 * Load a Markup from the text in p_lineScanner.
	 * 
	 * @param p_lineScanner  the Scanner providing the data to be loaded.
	 */
	public Markup(Scanner p_lineScanner) {
		f_x = p_lineScanner.nextFloat();
		f_y = p_lineScanner.nextFloat();
		f_size = p_lineScanner.nextInt();
		f_text = p_lineScanner.next();
		f_fontName = p_lineScanner.next();
		f_alignment = MarkupAlignment.valueOf(p_lineScanner.next());
	}
	
	/**
	 * Write text onto a pdf page.
	 * 
	 * @param p_cb  the page to be written onto.
	 * @throws MarkupInvalidFont 
	 */
	public void write(PdfContentByte p_cb) throws MarkupInvalidFont {
		p_cb.beginText();
		try {
			BaseFont bf = BaseFont.createFont(f_fontName, "UTF-8", false);
			p_cb.setFontAndSize(bf, f_size);
		} catch (DocumentException | IOException e) {
			throw new MarkupInvalidFont(e);
		}
		p_cb.showTextAligned(f_alignment.toPdfAlignment(),f_text,f_x, f_y, 0);
		p_cb.endText();
	}

	public float getX() {
		return f_x;
	}

	public float getY() {
		return f_y;
	}

	public int getSize() {
		return f_size;
	}

	public String getText() {
		return f_text;
	}

	public String getFontName() {
		return f_fontName;
	}

	public MarkupAlignment getAlignment() {
		return f_alignment;
	}
	
}
