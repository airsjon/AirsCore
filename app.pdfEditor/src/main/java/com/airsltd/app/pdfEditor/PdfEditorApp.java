package com.airsltd.app.pdfEditor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * PDF Editor
 * 
 * Modify an existing PDF with text fields read from an input file.
 *
 */
public class PdfEditorApp implements AutoCloseable
{
	/**
	 * Run the application
	 * 
	 * pdfEditor accepts only one argument, the file describing edits
	 * 
	 * Format: 'input file', 'output file', edit descriptions ...<br>
	 * Version 1 only allows for text placement: x, y, font-size, font-name, text ...
	 * @param args
	 * @throws Exception 
	 */
    public static void main( String[] args ) throws Exception
    {
    	try (PdfEditorApp l_app = new PdfEditorApp()) {
	    	// verify arg
    		if (args.length==2) {
    			l_app.setWorkDir(args[1]);
    		}
	    	// read file
	    	l_app.readFile(args[0]);
	    	// cache PDF templates
	    	l_app.loadTemplates();
	    	// generate new files
	    	l_app.generatePDFS();
    	}
    }

	private Map<String,InputPdf> f_templates = new HashMap<String, InputPdf>();
	private List<OutputPdf> f_output = new ArrayList<OutputPdf>();
	private String f_workDir = "";
	private String f_certificateDir = "src/main/resources/";

	public void generatePDFS() {
		f_output.forEach(OutputPdf::writePdf);
	}

	public void loadTemplates() throws MarkupInvalidPDF {
		for (InputPdf l_inputPdf : f_templates.values()) {
			l_inputPdf.loadTemplate();
		}
	}

	/**
	 * Read data from file p_string.
	 * 
	 * Each line of the file represents a new pdf file.
	 * 
	 * @param p_string
	 * @throws FileNotFoundException 
	 */
	public void readFile(String p_string) throws FileNotFoundException {
		try (Scanner l_input = new Scanner(new File(f_workDir + p_string))) {
			l_input.useDelimiter(",");
			while (l_input.hasNextLine()) {
				readLine(l_input.nextLine());
			}
		}
	}

	/**
	 * Each line from the update file is converted into a {@link OutputPdf} and placed in f_output.
	 * 
	 * @param p_input  String representing the file to read, the file to create, and data to place
	 */
	public void readLine(String p_input) {
		try (Scanner l_lineScanner = new Scanner(p_input)) {
			l_lineScanner.useDelimiter(",");
			InputPdf l_template = getInputPdf(f_certificateDir+l_lineScanner.next());
			OutputPdf l_output = getOutputPdf(l_template, f_workDir+l_lineScanner.next());
			while (l_lineScanner.hasNext()) {
				l_output.addMarkup(l_lineScanner);
			}
			f_output.add(l_output);
		}
	}
			

	public OutputPdf getOutputPdf(InputPdf p_template, String p_outputFile) {
		return new OutputPdf(p_template, p_outputFile);
	}

	public InputPdf getInputPdf(String p_inputPDF) {
		InputPdf l_pdf = f_templates.get(p_inputPDF);
		if (l_pdf == null) {
			l_pdf = new InputPdf(p_inputPDF);
			f_templates.put(p_inputPDF, l_pdf);
		}
		return l_pdf;
	}

	@Override
	public void close() throws Exception {
		for (InputPdf l_inputPdf : f_templates.values()) {
			l_inputPdf.close();
		}
	}

	public Map<String, InputPdf> getTemplates() {
		return f_templates;
	}

	public List<OutputPdf> getOutput() {
		return f_output;
	}

	/**
	 * @return the workDir
	 */
	public String getWorkDir() {
		return f_workDir;
	}

	/**
	 * @param p_workDir the workDir to set
	 */
	public void setWorkDir(String p_workDir) {
		f_workDir = p_workDir;
	}

	/**
	 * @return the certificateDir
	 */
	public String getCertificateDir() {
		return f_certificateDir;
	}

	/**
	 * @param p_certificateDir the certificateDir to set
	 */
	public void setCertificateDir(String p_certificateDir) {
		f_certificateDir = p_certificateDir;
	}
	
	
}
