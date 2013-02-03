/** 
 * Copyright 2008 Víctor Enrique Tamames,
 * Universidad de Valladolid, España.
 * 
 * This file is part of DelphSim.
 *
 * DelphSim is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or any later version.
 *
 * DelphSim is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * DelphSim. If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * DelphSim (Delphos Simulator), simulador de epidemias desarrollado como
 * Proyecto Fin de Carrera de Ingeniería Informática para la Escuela Técnica
 * Superior de Ingeniería Informática de la Universidad de Valladolid.
 */
package delphsim.util;

import java.util.Vector;
import javax.swing.table.*;

/**
 * Esta clase extiende el modelo por defecto de una <CODE>JTable</CODE> para
 * que se ajuste a las necesidades de las tabla que aparecen en DelphSim. En
 * concreto, sirve para crear tablas en las que se pueda especificar si una
 * celda es editable o no independientemente de las demás.
 * @author Víctor E. Tamames Gómez
 */
public class DelphSimTableModel extends DefaultTableModel {
    
    /**
     * Matriz que recoge si las celdas de esta tabla son editables o no.
     */
    private boolean[][] canEdit;
    
    /**
     * Constructor de la clase, crea una tabla vacía.
     */
    public DelphSimTableModel() {
        this(0, 0);
    }
    
    /**
     * Constructor de la clase con el número inicial de filas y columnas.
     * @param rowCount Número inicial de filas.
     * @param columnCount Número inicial de columnas.
     */
    public DelphSimTableModel(int rowCount, int columnCount) {
        this(newVector(columnCount), rowCount); 
    }
    
    /**
     * Constructor de la clase con los nombres de las columnas y el número
     * inicial de filas.
     * @param columnNames Nombres de las columnas.
     * @param rowCount Número inicial de filas.
     */
    public DelphSimTableModel(Vector columnNames, int rowCount) {
        setDataVector(newVector(rowCount), columnNames);
    }
    
    /**
     * Constructor de la clase con nombres para las columnas y datos con los que
     * rellenar la tabla.
     * @param data Los datos que compondrán la tabla.
     * @param columnNames Los nombres de las columnas.
     */
    public DelphSimTableModel(Object[][] data, Object[] columnNames) {
        setDataVector(data, columnNames);
    }
    
    /**
     * Método estático para crear un vector con un tamaño determinado.
     * @param size Tamaño del nuevo vector.
     * @return El vector creado.
     */
    private static Vector newVector(int size) { 
	Vector v = new Vector(size); 
	v.setSize(size); 
	return v; 
    }
    
    /**
     * Método para indicar rápidamente si todas las celdas de esta tabla son
     * editables o no.
     * @param editable Si son editables o no.
     */
    public void refreshCanEdit (boolean editable) {
        this.canEdit = new boolean[this.getRowCount()][this.getColumnCount()];
        for (int i = 0; i < this.getRowCount(); i++) {
            for (int j = 0; j < this.getColumnCount(); j++) {
                this.canEdit[i][j] = editable;
            }
        }
    }
    
    /**
     * Método para indicar si la celda definida por los parámetros es editable
     * o no.
     * @param rowNum La fila donde se encuentra la celda.
     * @param columnNum La columna donde se encuentra la celda.
     * @param editable Si es editable o no.
     */
    public void setCellEditable (int rowNum, int columnNum, boolean editable) {
        this.canEdit[rowNum][columnNum] = editable;
    }
    
    /**
     * Método que permite averiguar si una celda de este modelo es editable o no.
     * @param rowIndex La fila donde se encuentra la celda.
     * @param columnIndex La columna donde se encuentra la celda.
     * @return Si es editable o no.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[rowIndex][columnIndex];
    }
}
