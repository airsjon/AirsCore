/**
 * 
 */
package com.airsltd.app.pdfEditor;

import com.itextpdf.text.pdf.PdfContentByte;

/**
 * @author Jon Boley
 *
 */
public enum MarkupAlignment {

	LEFT, CENTER, RIGHT;

	public int toPdfAlignment() {
		int l_retVal = 0;
		switch (this) {
		case LEFT:
			l_retVal = PdfContentByte.ALIGN_LEFT;
			break;
		case CENTER:
			l_retVal = PdfContentByte.ALIGN_CENTER;
			break;
		case RIGHT:
			l_retVal = PdfContentByte.ALIGN_RIGHT;
			break;
		}
		return l_retVal;
	}
	
}
