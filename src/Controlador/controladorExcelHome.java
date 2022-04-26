
package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import com.itextpdf.text.Document;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import Modelo.generacionPdf;
import com.itextpdf.text.DocumentException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.generacionPdf_1;
import Modelo.modeloExcelHome;
import Vista.vistaHome;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.Barcode39;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.bouncycastle.util.io.BufferingOutputStream;

public class controladorExcelHome implements ActionListener {

    modeloExcelHome modeloH = new modeloExcelHome();
    char[] descripcion2;
    vistaHome vistaH = new vistaHome();
    generacionPdf generar = new generacionPdf();
    generacionPdf_1 generar_1 = new generacionPdf_1();
    JFileChooser selecArchivo = new JFileChooser();
    File archivo;
    int contAccion = 0;

    public controladorExcelHome(vistaHome vistaH, modeloExcelHome modeloH) {
        this.vistaH = vistaH;
        this.modeloH = modeloH;
        this.vistaH.cargar.addActionListener(this);
        this.vistaH.exportar1.addActionListener(this);
    }

    public void AgregarFiltro() {
        selecArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xls)", "xls"));
        selecArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
    }

    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        for (int i = 0; i < vistaH.jTable1.getRowCount(); i++) {
            this.vistaH.jTable1.setValueAt(true, i, 3);
        }
    }
    public void addCheckPiezas(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(String.class));
        tc.setCellRenderer(table.getDefaultRenderer(String.class));
        for (int i = 0; i < vistaH.jTable1.getRowCount(); i++) {
            this.vistaH.jTable1.setValueAt(1, i, 4);
        }
    }

     public void imprimirPdfMayor10x10() throws DocumentException, FileNotFoundException, IOException, PrinterException {
       this.vistaH = vistaH;
        boolean columna;
        int i = 0;
        JFileChooser selecArchivo = new JFileChooser();
        File archivo = null;
        int width = 275;
        int height = 275;
        Rectangle rec = new Rectangle(width, height);
        Rectangle rec2 = new Rectangle(width, height);
        rec.setBorderColor(BaseColor.BLACK);
        rec.setBorderWidthBottom(2);
        rec.setBorderWidthLeft(3);
        rec.setBorderWidthRight(2);
        rec.setBorderWidthTop(4);

        String sku, descripcion, ups,desa, piezas1;

        columna = (boolean) this.vistaH.jTable1.getValueAt(0, 3);
        sku = String.valueOf(this.vistaH.jTable1.getValueAt(i, 0));
        ups = String.valueOf(this.vistaH.jTable1.getValueAt(i, 2));
        //  if(selecArchivo.showDialog(null, "Crear")==JFileChooser.APPROVE_OPTION){
        //    archivo=selecArchivo.getSelectedFile();
        // }
        Document doc = new Document(PageSize.LEGAL_LANDSCAPE);
        ByteArrayOutputStream archivotemp = new ByteArrayOutputStream();
        PdfWriter pdf = PdfWriter.getInstance(doc, archivotemp);

        doc.setPageSize(rec);
        doc.setMargins(4, 4, 12, 4);
        doc.setMarginMirroring(columna);
        doc.open();
         BarcodeEAN code = new BarcodeEAN();
        code.setCode(ups);
        code.setCodeType(Barcode.EAN13);
        Image img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
        img.scaleToFit(100, 70);
        Paragraph parrafo = new Paragraph();
        Paragraph sku_ = new Paragraph();
        Paragraph header = new Paragraph();
        Paragraph piezas = new Paragraph();        
        Paragraph line = new Paragraph();
        Paragraph salto = new Paragraph();
        parrafo.setAlignment(Paragraph.ALIGN_CENTER);
        header.setAlignment(Paragraph.ALIGN_CENTER);
        line.setAlignment(Paragraph.ALIGN_CENTER);
        piezas.setAlignment(Paragraph.ALIGN_RIGHT);
        line.setSpacingBefore((float) .2);
        line.setSpacingAfter((float) .2);
        sku_.setAlignment(Paragraph.ALIGN_CENTER);
        sku_.setSpacingAfter((float) 0.10);
        sku_.setSpacingBefore((float) 0.10);
        parrafo.setFont(FontFactory.getFont("Arial", 18, Font.BOLD, BaseColor.DARK_GRAY));
        parrafo.setIndentationLeft(8);
        parrafo.setIndentationRight(8);
       
        sku_.setFont(FontFactory.getFont("Arial", 16, Font.BOLD, BaseColor.DARK_GRAY));
        sku_.setSpacingBefore(5);
        sku_.setSpacingAfter(5);
        sku_.setIndentationLeft(10);
        sku_.setIndentationRight(10);
/*        
        header.setFont(FontFactory.getFont("Arial",14, Font.BOLD, BaseColor.DARK_GRAY));
        header.setIndentationLeft(10);
        header.setIndentationRight(10);*/
        
        piezas.setFont(FontFactory.getFont("Arial",14, Font.BOLD, BaseColor.DARK_GRAY));
        piezas.setIndentationLeft(10);
        piezas.setIndentationRight(10);
        
        doc.open();
        header.add("Claroshop");
        header.add("\n"+"__________");
        salto.add("\n");
 //       line.add("_____________");
        while (i < this.vistaH.jTable1.getRowCount()) {
            columna = (boolean) this.vistaH.jTable1.getValueAt(i, 3);
            if (columna == true) {
                sku = String.valueOf(this.vistaH.jTable1.getValueAt(i, 0));
                piezas1= String.valueOf(this.vistaH.jTable1.getValueAt(i, 4));
                if (sku.length()>9 ) {
                sku= sku.substring(0,9);
                
                }
                ups = String.valueOf(this.vistaH.jTable1.getValueAt(i, 2));
 
                code.setCode(ups);
                code.setCodeType(Barcode.EAN13);
                img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
                img.scaleToFit(150, 120);
                img.setAlignment(img.ALIGN_CENTER);
                descripcion = String.valueOf(this.vistaH.jTable1.getValueAt(i, 1));
                 if (descripcion.length()>38 ) {
                
                descripcion= descripcion.substring(0,38);

                }
                descripcion2=descripcion.toCharArray();                
                i = i + 1;


                sku_.add("SKU:" + " " + sku+"\n"+"____________________");
                piezas.add(piezas1+" "+"PZ"+"\n"+"Cantidad de producto");
                for (int j = 0; j < descripcion.length(); j++) {
                desa=String.valueOf(descripcion2[j]);
                parrafo.add(desa);                    
                }
                parrafo.add("\n"+"___________");
                 doc.add(salto);
                doc.add(parrafo);
                 if (descripcion.length()<23) {
                    doc.add(salto);
                }
                doc.add(sku_);
                doc.add(salto);
                doc.add(piezas);
                doc.add(salto);
                doc.add(img);

                line.setSpacingBefore((float) -2);
                pdf.getPageSize();
                parrafo.removeAll(parrafo);
                sku_.removeAll(sku_);
                piezas.removeAll(piezas);
                if (i + 1 > this.vistaH.jTable1.getRowCount()) {
                    doc.close();
                    ByteArrayInputStream input = new ByteArrayInputStream(archivotemp.toByteArray());
                    PDDocument documento12 = PDDocument.load(input);
                    PrinterJob job = PrinterJob.getPrinterJob();
                    if (job.printDialog() == true) {
                        job.setPageable(new PDFPageable(documento12));
                        job.print();
                    }
                }
            } else if (columna == false) {
                i = i + 1;

                if (i + 1 > this.vistaH.jTable1.getRowCount()) {
                    doc.close();
                    ByteArrayInputStream input = new ByteArrayInputStream(archivotemp.toByteArray());
                    PDDocument documento12 = PDDocument.load(input);
                    
                    PrinterJob job = PrinterJob.getPrinterJob();
                    if (job.printDialog() == true) {
                        job.setPageable(new PDFPageable(documento12));
                        job.print();
                    }
                }
            }
        }
    }
    
    
    public void imprimir() {
        Document documento = new Document();
        try {
        } catch (Exception e) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        contAccion++;
        if (contAccion == 1) {
            AgregarFiltro();
        }
        if (e.getSource() == vistaH.cargar) {
            if (selecArchivo.showDialog(null, "Seleccionar archivo") == JFileChooser.APPROVE_OPTION) {
                archivo = selecArchivo.getSelectedFile();
                if (archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")) {
                    JOptionPane.showMessageDialog(null,
                            modeloH.Importar(archivo, vistaH.jTable1) + "\n Formato ." + archivo.getName().substring(archivo.getName().lastIndexOf(".") + 1),
                            "IMPORTAR EXCEL", JOptionPane.INFORMATION_MESSAGE);
                    addCheckBox(3, this.vistaH.jTable1);
                    addCheckPiezas(4, this.vistaH.jTable1);
                } else {
                    JOptionPane.showMessageDialog(null, "Elija un formato valido.");
                }
            }
        }
        if (e.getSource() == vistaH.exportar1) {

            try {
                imprimirPdfMayor10x10();
            } catch (DocumentException ex) {
                Logger.getLogger(controladorExcelHome.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(controladorExcelHome.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(controladorExcelHome.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrinterException ex) {
                Logger.getLogger(controladorExcelHome.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    

    }
}
