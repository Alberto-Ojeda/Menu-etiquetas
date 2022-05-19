package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import Vista.Vista;
import Modelo.ModeloExcel;
import Modelo.ModeloLiverpoolExcel;
import com.itextpdf.text.Document;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;
import Modelo.generacionPdf;
import com.itextpdf.text.DocumentException;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.generacionPdf_1;
import Vista.VistaLiverpool;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.bouncycastle.util.io.BufferingOutputStream;

public class ControladorLiverpoolExcel implements ActionListener {

    ModeloLiverpoolExcel modeloL = new ModeloLiverpoolExcel();
    char[] descripcion2;
    VistaLiverpool vistaL = new VistaLiverpool();
    generacionPdf generar = new generacionPdf();
    generacionPdf_1 generar_1 = new generacionPdf_1();
    JFileChooser selecArchivo = new JFileChooser();
    File archivo;
    int contAccion = 0;

    public ControladorLiverpoolExcel(VistaLiverpool vistaL, ModeloLiverpoolExcel modeloL) {
        this.vistaL = vistaL;
        this.modeloL = modeloL;
        this.vistaL.cargarL.addActionListener(this);
        this.vistaL.exportar.addActionListener(this);
        this.vistaL.exportar1.addActionListener(this);
    }

    public void AgregarFiltro() {
        selecArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xls)", "xls"));
        selecArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
    }

    public void addCheckBox(int column, JTable table) {
        TableColumn tc = table.getColumnModel().getColumn(column);
        tc.setCellEditor(table.getDefaultEditor(Boolean.class));
        tc.setCellRenderer(table.getDefaultRenderer(Boolean.class));
        for (int i = 0; i < vistaL.jTable1.getRowCount(); i++) {
            this.vistaL.jTable1.setValueAt(true, i, 10);
        }
    }

    public void accederDatosFilacopia5x5() throws DocumentException, FileNotFoundException, IOException, PrinterException {
        //se establecen los tamaños para la etiqueta producto la cual tendra un tamaño aprox de 10x10cm
        this.vistaL = vistaL;
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

//una vez estableciendo el tamaño se definiran nuestras variables que serviran para escribir en la etiqueta
        String datProvedor, numProvedor, sku, descripcion, modelo, color, bulto, pesoEmpaque, ups, desa, cantidadBultos, fechaE;
        columna = (boolean) this.vistaL.jTable1.getValueAt(0, 10);
        Date fecha = this.vistaL.jDateChooser1.getDate();
        if (fecha==null) {
                    JOptionPane.showMessageDialog(null, "Inserta fecha" );
        }else{
        SimpleDateFormat formato = new SimpleDateFormat("d/MM/YYYY");
        JOptionPane.showMessageDialog(null, "la fecha es" + formato.format(fecha));

        fechaE = String.valueOf(formato.format(fecha));
        datProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 0));
        numProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 1));
        sku = String.valueOf(this.vistaL.jTable1.getValueAt(i, 2));
        descripcion = String.valueOf(this.vistaL.jTable1.getValueAt(i, 3));
        modelo = String.valueOf(this.vistaL.jTable1.getValueAt(i, 4));
        color = String.valueOf(this.vistaL.jTable1.getValueAt(i, 5));
        bulto = String.valueOf(this.vistaL.jTable1.getValueAt(i, 7));
        pesoEmpaque = String.valueOf(this.vistaL.jTable1.getValueAt(i, 8));
        cantidadBultos = String.valueOf(this.vistaL.jTable1.getValueAt(i, 9));

        //  if(selecArchivo.showDialog(null, "Crear")==JFileChooser.APPROVE_OPTION){
        //    archivo=selecArchivo.getSelectedFile();
        // }

        /*A continuación se realiza la iniciación del documento que se estara construyendo 
        junto con la definición de los parrafos*/
        Document doc = new Document(PageSize.LEGAL_LANDSCAPE);
        ByteArrayOutputStream archivotemp = new ByteArrayOutputStream();
        PdfWriter pdf = PdfWriter.getInstance(doc, archivotemp);

        doc.setPageSize(rec);
        doc.setMargins(4, 4, 12, 4);
        doc.setMarginMirroring(columna);
        doc.open();
        Barcode128 code = new Barcode128();
        //Creamos el codigo de barras con el SKU
        code.setCode(sku);
        Image img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
        img.scaleToFit(50, 30);

        //Definimos los parrafos
        //----------------------------------------------------------
        Paragraph datProvedorP = new Paragraph();
        Paragraph numProvedorP = new Paragraph();
        Paragraph skuP = new Paragraph();
        Paragraph parrafo = new Paragraph();
        Paragraph modeloP = new Paragraph();
        Paragraph colorP = new Paragraph();
        Paragraph bultoP = new Paragraph();
        Paragraph pesoEmpaqueP = new Paragraph();
        Paragraph cantidadBultosP = new Paragraph();
        //-----------------------------------------------------------
        Paragraph header = new Paragraph();
        Paragraph line = new Paragraph();
        Paragraph salto = new Paragraph(6);

        // se define la alineación junto con el tamaño que tendran las letras
        //-------------------------------------------------------------------
        datProvedorP.setAlignment(Paragraph.ALIGN_LEFT);
        numProvedorP.setAlignment(Paragraph.ALIGN_LEFT);
        skuP.setAlignment(Paragraph.ALIGN_LEFT);
        parrafo.setAlignment(Paragraph.ALIGN_LEFT);
        modeloP.setAlignment(Paragraph.ALIGN_LEFT);
        colorP.setAlignment(Paragraph.ALIGN_LEFT);
        bultoP.setAlignment(Paragraph.ALIGN_LEFT);
        pesoEmpaqueP.setAlignment(Paragraph.ALIGN_LEFT);
        cantidadBultosP.setAlignment(Paragraph.ALIGN_LEFT);
        //-------------------------------------------------------------------        

        header.setAlignment(Paragraph.ALIGN_RIGHT);
        line.setAlignment(Paragraph.ALIGN_CENTER);
        //------------------------------------------------------------------
        //Definición del tamaño de letra
        datProvedorP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));

        numProvedorP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));

        skuP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        /*        skuP.setSpacingBefore(5);
        skuP.setSpacingAfter(5);
        skuP.setIndentationLeft(10);
        skuP.setIndentationRight(10);*/

        parrafo.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        /*        parrafo.setIndentationLeft(8);
        parrafo.setIndentationRight(8);*/

        modeloP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        colorP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        bultoP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        pesoEmpaqueP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        cantidadBultosP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));

        header.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        header.setIndentationLeft(10);
        header.setIndentationRight(10);
        //-------------------------------------------------------------------------------

        doc.open();
        //      header.add("Claroshop");
        //      header.add("\n" + "__________");
        salto.add("\n");
        //comienza el while para dar inicio a los ciclos y poder obtener los datos de la tabla
        while (i < this.vistaL.jTable1.getRowCount()) {
            for (int h = 0; h < Integer.parseInt(cantidadBultos); h++) {
                columna = (boolean) this.vistaL.jTable1.getValueAt(i, 10);
                if (columna == true) {

                    datProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 0));
                    numProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 1));
                    sku = String.valueOf(this.vistaL.jTable1.getValueAt(i, 2));
                    descripcion = String.valueOf(this.vistaL.jTable1.getValueAt(i, 3));
                    modelo = String.valueOf(this.vistaL.jTable1.getValueAt(i, 4));
                    color = String.valueOf(this.vistaL.jTable1.getValueAt(i, 5));
                    bulto = String.valueOf(this.vistaL.jTable1.getValueAt(i, 7));
                    pesoEmpaque = String.valueOf(this.vistaL.jTable1.getValueAt(i, 8));
                    cantidadBultos = String.valueOf(this.vistaL.jTable1.getValueAt(i, 9));

                    //Validación de los sku de cada uno 
                    if (sku.length() > 13) {
                        sku = sku.substring(0, 12);
                    }

                    //           ups = String.valueOf(this.vistaL.jTable1.getValueAt(i, 2));
                    code.setCode(sku);
                    img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
                    img.scaleToFit(100, 70);
                    img.setBorderColorBottom(BaseColor.WHITE);
                    img.setBorderWidthBottom(8);
                    img.setAlignment(img.ALIGN_RIGHT);
                    descripcion = String.valueOf(this.vistaL.jTable1.getValueAt(i, 3)).toUpperCase();
                    if (descripcion.length() > 38) {

                        descripcion = descripcion.substring(0, 38);

                    }
                    descripcion2 = descripcion.toCharArray();
                    if (h + 1 == Integer.parseInt(cantidadBultos)) {
                        i = i + 1;
                    }

                    header.add(fechaE);
                    doc.add(header);
                    datProvedorP.add("Proveedor:" + " " + datProvedor);
                    doc.add(datProvedorP);
                    numProvedorP.add("No. De Provedor:" + " " + numProvedor);
                    doc.add(numProvedorP);
                    skuP.add("SKU:" + " " + sku);
//                skuP.add("SKU:" + " " + sku + "\n" + "___________");
                    doc.add(skuP);
                    doc.add(img);
                    if (descripcion.length() < 20) {
                        doc.add(salto);
                    }
                    
                    parrafo.add("Descripción:" + " " + descripcion);
                    doc.add(parrafo);
                    doc.add(salto);

                    modeloP.add("Modelo:" + " " + modelo);
                    doc.add(modeloP);
                    doc.add(salto);

                    colorP.add("Color:" + " " + color);
                    doc.add(colorP);
                    doc.add(salto);

                    int cuentaBulto;
                    cuentaBulto = Integer.parseInt(bulto) + h;
                    bulto = String.valueOf(cuentaBulto);
                    bultoP.add("Bulto:" + " " + bulto + " " + "de" + " " + cantidadBultos);
                    doc.add(bultoP);
                    doc.add(salto);

                    pesoEmpaqueP.add("Peso con empaque:" + " " + pesoEmpaque);
                    doc.add(pesoEmpaqueP);

                    for (int j = 0; j < descripcion.length(); j++) {
                        desa = String.valueOf(descripcion2[j]);
                        parrafo.add(desa);
                    }
                    //  parrafo.add("\n" + "___________");

                    if (descripcion.length() < 34) {
                        doc.add(salto);
                    }
                    //     doc.add(parrafo);

                    line.setSpacingBefore((float) -2);
                    pdf.getPageSize();
                    header.removeAll(header);
                    datProvedorP.removeAll(datProvedorP);
                    numProvedorP.removeAll(numProvedorP);
                    modeloP.removeAll(modeloP);
                    colorP.removeAll(colorP);
                    bultoP.removeAll(bultoP);
                    pesoEmpaqueP.removeAll(pesoEmpaqueP);
                    parrafo.removeAll(parrafo);
                    skuP.removeAll(skuP);

                    if (i + 1 > this.vistaL.jTable1.getRowCount()) {
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
                       if (h + 1 == Integer.parseInt(cantidadBultos)) {
                        i = i + 1;
                    }
                    if (i + 1 > this.vistaL.jTable1.getRowCount()) {
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
    }
    }
    public void imprimirPdfMayor() throws DocumentException, FileNotFoundException, IOException, PrinterException {
        //se establecen los tamaños para la etiqueta producto la cual tendra un tamaño aprox de 10x10cm
        this.vistaL = vistaL;
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

//una vez estableciendo el tamaño se definiran nuestras variables que serviran para escribir en la etiqueta
        String datProvedor, numProvedor, sku, descripcion, modelo, color, bulto, pesoEmpaque, ups, desa, cantidadBultos, fechaE, contenido;
        columna = (boolean) this.vistaL.jTable1.getValueAt(0, 10);
        Date fecha = this.vistaL.jDateChooser1.getDate();
          if (fecha==null) {
                    JOptionPane.showMessageDialog(null, "Inserta fecha" );
        }else{
        SimpleDateFormat formato = new SimpleDateFormat("d/MM/YYYY");
        JOptionPane.showMessageDialog(null, "la fecha es" + formato.format(fecha));

        fechaE = String.valueOf(formato.format(fecha));
        datProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 0));
        numProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 1));
        sku = String.valueOf(this.vistaL.jTable1.getValueAt(i, 2));
        descripcion = String.valueOf(this.vistaL.jTable1.getValueAt(i, 3));
        modelo = String.valueOf(this.vistaL.jTable1.getValueAt(i, 4));
        color = String.valueOf(this.vistaL.jTable1.getValueAt(i, 5));
        contenido = String.valueOf(this.vistaL.jTable1.getValueAt(i, 6));
        bulto = String.valueOf(this.vistaL.jTable1.getValueAt(i, 7));
        pesoEmpaque = String.valueOf(this.vistaL.jTable1.getValueAt(i, 8));
        cantidadBultos = String.valueOf(this.vistaL.jTable1.getValueAt(i, 9));

        //  if(selecArchivo.showDialog(null, "Crear")==JFileChooser.APPROVE_OPTION){
        //    archivo=selecArchivo.getSelectedFile();
        // }

        /*A continuación se realiza la iniciación del documento que se estara construyendo 
        junto con la definición de los parrafos*/
        Document doc = new Document(PageSize.LEGAL_LANDSCAPE);
        ByteArrayOutputStream archivotemp = new ByteArrayOutputStream();
        PdfWriter pdf = PdfWriter.getInstance(doc, archivotemp);

        doc.setPageSize(rec);
        doc.setMargins(4, 4, 12, 4);
        doc.setMarginMirroring(columna);
        doc.open();
        Barcode128 code = new Barcode128();
        //Creamos el codigo de barras con el SKU
        code.setCode(sku);
        Image img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
        img.scaleToFit(50, 30);

        //Definimos los parrafos
        //----------------------------------------------------------
        Paragraph datProvedorP = new Paragraph();
        Paragraph numProvedorP = new Paragraph();
        Paragraph skuP = new Paragraph();
        Paragraph parrafo = new Paragraph();
        Paragraph modeloP = new Paragraph();
        Paragraph colorP = new Paragraph();
        Paragraph contenidoP = new Paragraph();
        Paragraph bultoP = new Paragraph();
        Paragraph pesoEmpaqueP = new Paragraph();
        Paragraph cantidadBultosP = new Paragraph();
        //-----------------------------------------------------------
        Paragraph header = new Paragraph();
        Paragraph line = new Paragraph();
        Paragraph salto = new Paragraph(6);

        // se define la alineación junto con el tamaño que tendran las letras
        //-------------------------------------------------------------------
        datProvedorP.setAlignment(Paragraph.ALIGN_LEFT);
        numProvedorP.setAlignment(Paragraph.ALIGN_LEFT);
        skuP.setAlignment(Paragraph.ALIGN_LEFT);
        parrafo.setAlignment(Paragraph.ALIGN_LEFT);
        modeloP.setAlignment(Paragraph.ALIGN_LEFT);
        colorP.setAlignment(Paragraph.ALIGN_LEFT);
        contenidoP.setAlignment(Paragraph.ALIGN_LEFT);
        bultoP.setAlignment(Paragraph.ALIGN_LEFT);
        pesoEmpaqueP.setAlignment(Paragraph.ALIGN_LEFT);
        cantidadBultosP.setAlignment(Paragraph.ALIGN_LEFT);
        //-------------------------------------------------------------------        

        header.setAlignment(Paragraph.ALIGN_RIGHT);
        line.setAlignment(Paragraph.ALIGN_CENTER);
        //------------------------------------------------------------------
        //Definición del tamaño de letra
        datProvedorP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));

        numProvedorP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));

        skuP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        /*        skuP.setSpacingBefore(5);
        skuP.setSpacingAfter(5);
        skuP.setIndentationLeft(10);
        skuP.setIndentationRight(10);*/

        parrafo.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        /*        parrafo.setIndentationLeft(8);
        parrafo.setIndentationRight(8);*/

        modeloP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        colorP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        contenidoP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        bultoP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        pesoEmpaqueP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        cantidadBultosP.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));

        header.setFont(FontFactory.getFont("Arial", 12, Font.BOLD, BaseColor.DARK_GRAY));
        header.setIndentationLeft(10);
        header.setIndentationRight(10);
        //-------------------------------------------------------------------------------
       
        doc.open();
        //      header.add("Claroshop");
        //      header.add("\n" + "__________");
        salto.add("\n");
        //comienza el while para dar inicio a los ciclos y poder obtener los datos de la tabla
        while (i < this.vistaL.jTable1.getRowCount()) {
            for (int h = 0; h < Integer.parseInt(cantidadBultos); h++) {
                columna = (boolean) this.vistaL.jTable1.getValueAt(i, 10);
                if (columna == true) {

                    datProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 0));
                    numProvedor = String.valueOf(this.vistaL.jTable1.getValueAt(i, 1));
                    sku = String.valueOf(this.vistaL.jTable1.getValueAt(i, 2));
                    descripcion = String.valueOf(this.vistaL.jTable1.getValueAt(i, 3));
                    modelo = String.valueOf(this.vistaL.jTable1.getValueAt(i, 4));
                    color = String.valueOf(this.vistaL.jTable1.getValueAt(i, 5));
                    contenido = String.valueOf(this.vistaL.jTable1.getValueAt(i, 6));
                    bulto = String.valueOf(this.vistaL.jTable1.getValueAt(i, 7));
                    pesoEmpaque = String.valueOf(this.vistaL.jTable1.getValueAt(i, 8));
                    cantidadBultos = String.valueOf(this.vistaL.jTable1.getValueAt(i, 9));

                    //Validación de los sku de cada uno 
                    if (sku.length() > 13) {
                        sku = sku.substring(0, 12);
                    }

                    //           ups = String.valueOf(this.vistaL.jTable1.getValueAt(i, 2));
                    code.setCode(sku);
                    img = code.createImageWithBarcode(pdf.getDirectContent(), BaseColor.BLACK, BaseColor.BLACK);
                    img.scaleToFit(100, 70);
                    img.setBorderColorBottom(BaseColor.WHITE);
                    img.setBorderWidthBottom(8);
                    img.setAlignment(img.ALIGN_RIGHT);
                    descripcion = String.valueOf(this.vistaL.jTable1.getValueAt(i, 3)).toUpperCase();
                    if (descripcion.length() > 38) {

                        descripcion = descripcion.substring(0, 38);

                    }
                    descripcion2 = descripcion.toCharArray();
                    if (h + 1 == Integer.parseInt(cantidadBultos)) {
                        i = i + 1;
                    }

                    header.add(fechaE);
                    doc.add(header);
                    datProvedorP.add("Proveedor:" + " " + datProvedor);
                    doc.add(datProvedorP);
                    numProvedorP.add("No. De Provedor:" + " " + numProvedor);
                    doc.add(numProvedorP);
                    skuP.add("SKU:" + " " + sku);
//                skuP.add("SKU:" + " " + sku + "\n" + "___________");
                    doc.add(skuP);
                    doc.add(img);
                    if (descripcion.length() < 20) {
                        doc.add(salto);
                    }
                    
                    parrafo.add("Descripción:" + " " + descripcion);
                    doc.add(parrafo);
                    doc.add(salto);

                    modeloP.add("Modelo:" + " " + modelo);
                    doc.add(modeloP);

                    colorP.add("Color:" + " " + color);
                    doc.add(colorP);
                    contenidoP.add("contenido:"+" "+contenido +" " +"piezas");
                    doc.add(contenidoP);

                    int cuentaBulto;
                    cuentaBulto = Integer.parseInt(bulto) + h;
                    bulto = String.valueOf(cuentaBulto);
                    bultoP.add("Bulto:" + " " + bulto + " " + "de" + " " + cantidadBultos);
                    doc.add(bultoP);
                    doc.add(salto);

                    pesoEmpaqueP.add("Peso con empaque:" + " " + pesoEmpaque);
                    doc.add(pesoEmpaqueP);

                    for (int j = 0; j < descripcion.length(); j++) {
                        desa = String.valueOf(descripcion2[j]);
                        parrafo.add(desa);
                    }
                    //  parrafo.add("\n" + "___________");

                    if (descripcion.length() < 34) {
                        doc.add(salto);
                    }
                    //     doc.add(parrafo);

                    line.setSpacingBefore((float) -2);
                    pdf.getPageSize();
                    header.removeAll(header);
                    contenidoP.removeAll(contenidoP);
                    datProvedorP.removeAll(datProvedorP);
                    numProvedorP.removeAll(numProvedorP);
                    modeloP.removeAll(modeloP);
                    colorP.removeAll(colorP);
                    bultoP.removeAll(bultoP);
                    pesoEmpaqueP.removeAll(pesoEmpaqueP);
                    parrafo.removeAll(parrafo);
                    skuP.removeAll(skuP);

                    if (i + 1 > this.vistaL.jTable1.getRowCount()) {
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
                    if (h + 1 == Integer.parseInt(cantidadBultos)) {
                        i = i + 1;
                    }


                    if (i + 1 > this.vistaL.jTable1.getRowCount()) {
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
        if (e.getSource() == vistaL.cargarL) {
            if (selecArchivo.showDialog(null, "Seleccionar archivo") == JFileChooser.APPROVE_OPTION) {
                archivo = selecArchivo.getSelectedFile();
                if (archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")) {
                    JOptionPane.showMessageDialog(null,
                            modeloL.Importar(archivo, vistaL.jTable1) + "\n Formato ." + archivo.getName().substring(archivo.getName().lastIndexOf(".") + 1),
                            "IMPORTAR EXCEL", JOptionPane.INFORMATION_MESSAGE);
                    addCheckBox(10, this.vistaL.jTable1);
                } else {
                    JOptionPane.showMessageDialog(null, "Elija un formato valido.");
                }
            }
        }
        if (e.getSource() == vistaL.exportar1) {

            try {
                imprimirPdfMayor();
            } catch (DocumentException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrinterException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (e.getSource() == vistaL.exportar) {
            try {
                accederDatosFilacopia5x5();
            } catch (DocumentException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (PrinterException ex) {
                Logger.getLogger(ControladorLiverpoolExcel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
