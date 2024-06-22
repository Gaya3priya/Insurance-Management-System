package com.insurance.service;

import java.awt.Color;
import java.io.IOException;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.insurance.entities.Policy;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

public class PoliciesPDFExporter {
      private List<Policy> policyHoldersList;

	public PoliciesPDFExporter(List<Policy> policyHoldersList) {
		
		this.policyHoldersList = policyHoldersList;
	}
      
    private void writeTableHeader(PdfPTable table) {
    	
    	PdfPCell cell=new PdfPCell();
    	cell.setBackgroundColor(Color.BLUE);
    	cell.setPadding(5);
    	Font font=FontFactory.getFont(FontFactory.HELVETICA);
    	font.setColor(Color.WHITE);
    	cell.setPhrase(new Phrase("PolicyID",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("Name",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("DOC",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("Term",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("Premium",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("SumAssured",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("mode",font));
    	table.addCell(cell);
    	
    	cell.setPhrase(new Phrase("FUP",font));
    	table.addCell(cell);
    }
    
    private void writeTableData(PdfPTable table) {
    	for (Policy policy: policyHoldersList) {
    		table.addCell(String.valueOf(policy.getPolicyID()));
    		table.addCell(policy.getName());
    		table.addCell((policy.getDOC()).toString());
    		table.addCell(String.valueOf(policy.getTerm()));
    		table.addCell(String.valueOf(policy.getPremium()));
    		table.addCell(String.valueOf(policy.getSumAssured()));
    		table.addCell(policy.getMode());
    		table.addCell((policy.getFUP()).toString());
    	}
    }
    
    public void export(HttpServletResponse response) throws DocumentException, IOException {
    	Document document =new Document(PageSize.A4);
    	
    	PdfWriter.getInstance(document, response.getOutputStream());
    	document.open();
    	
    	Font font=FontFactory.getFont(FontFactory.HELVETICA_BOLD);
    	font.setColor(Color.BLUE);
    	font.setSize(18);
    	Paragraph title=new Paragraph("List of Policy Holders",font);
    	title.setAlignment(Paragraph.ALIGN_CENTER);
    	document.add(title);
    	
    	PdfPTable table=new PdfPTable(8);
    	table.setWidthPercentage(100);
    	table.setSpacingBefore(15);
    	
    	table.setWidths(new float[] {3.0f,3.9f,3.0f,2.0f,3.0f,3.5f,2.0f,3.0f});
    	
    	writeTableHeader(table);
    	writeTableData(table);
    	document.add(table);
    	document.close();
    }
}
