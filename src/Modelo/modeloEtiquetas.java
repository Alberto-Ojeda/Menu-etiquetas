/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import javax.swing.JTable;

import java.io.File;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author alberto
 * En esta clase se define los elementos que contendra la tabla que son el 
 * nombre del archivo junto con la dirección de este 
 */
public class modeloEtiquetas {
    int indicecolumna=0;
    public void importarT(File archivo, JTable table){
        
//se anexa la respuesta junto con el tamaño de la tabla.                
        String respuesta="No se pudo realizar la importación.";
        DefaultTableModel modeloT = new DefaultTableModel();
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        modeloT.addColumn("ID");
        modeloT.addColumn("Nombre del archivo");
        modeloT.addColumn("Ruta");
        for (int i = 0; i < modeloT.getColumnCount(); i++) {
            
        }
        
        
//        modeloT.addRow(rowData);
//       listaColumna[indiceColumna]= (int)Math.round(celda.getNumericCellValue());              
}
        public void addCheckBox (File archivo, JTable table) {
      
        table.setValueAt(archivo.getName(), 0, 0);
            table.setValueAt(archivo.getName(), 0, 1);
            table.setValueAt(archivo.getAbsolutePath(), 0, 2);
            }

        
    }

