package com.airsltd.app.pdfEditor;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PdfEditorAppTest {

	private PdfEditorApp f_test;
	
	@Before
	public void setUp() throws Exception {
		f_test = new PdfEditorApp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMain() throws Exception {
		// given
		File l_resourcesDirectory = new File("src/test/resources");
		// when
		PdfEditorApp.main(new String[] { "dump.txt", l_resourcesDirectory.getAbsolutePath()+"/"});
		// then
	}

	@Test
	public void testGeneratePDFS() {
		fail("Not yet implemented");
	}

	@Test
	public void testLoadTemplates() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFile() throws FileNotFoundException {
		// given
		File l_resourcesDirectory = new File("src/test/resources");
		f_test.setWorkDir(l_resourcesDirectory.getAbsolutePath()+"/");
		// when
		f_test.readFile("dump.out");
	}

	@Test
	public void testReadLine() {
		// given
		
		// when
		f_test.readLine("in.pdf,out.pdf,3,4,5,something else,Times-Roman,LEFT,5,6,7,another thing,Arial,RIGHT");
		// then
		List<OutputPdf> l_output= f_test.getOutput();
		assertEquals("src/main/resources/in.pdf", f_test.getTemplates().keySet().toArray()[0]);
		assertEquals(1, l_output.size());
		assertEquals(2, l_output.get(0).getMarkups().size());
		assertEquals(f_test.getInputPdf("src/main/resources/in.pdf"), l_output.get(0).getTemplate());
		assertEquals("something else", l_output.get(0).getMarkups().get(0).getText());
		assertEquals("another thing", l_output.get(0).getMarkups().get(1).getText());

	}

	@Test
	public void testGetOutputPdf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetInputPdf() {
		fail("Not yet implemented");
	}

	@Test
	public void testClose() {
		fail("Not yet implemented");
	}

}
