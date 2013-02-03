/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package delphsim.util;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Implementa un editor de celdas de una tabla capaz de abrir un cuadro de
 * diálogo de selección de color y mostrar en la celda donde se use el color
 * escogido.
 * @author Víctor E. Tamames Gómez
 * @see delphsim.util.ColorRenderer ColorRenderer
 */
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    
    /**
     * El color que tiene la celda en principio.
     */
    Color currentColor;
    
    /**
     * El botón que abre el cuadro de selección de color.
     */
    JButton button;
    
    /**
     * El cuadro de selección de color.
     */
    JColorChooser colorChooser;
    
    /**
     * El cuadro de diálogo que contiene al seleccionador de color.
     */
    JDialog dialog;
    
    /**
     * El comando de la acción relacionado con la opción "editar" (<CODE>edit</CODE>).
     */
    protected static final String EDIT = "edit";
    
    /**
     * Constructor de la clase.
     */
    public ColorEditor() {
        //Set up the editor (from the table's point of view),
        //which is a button.
        //This button brings up the color chooser dialog,
        //which is the editor from the user's point of view.
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        //Set up the dialog that the button brings up.
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(delphsim.DelphSimApp.getApplication().getMainFrame(),
                                        "Escoja un color",
                                        true,  //modal
                                        colorChooser,
                                        this,  //OK button handler
                                        null); //no CANCEL button handler
    }
    
    /**
     * Maneja eventos del botón del editor y del botón "Aceptar" del cuadro
     * de diálogo.
     * @param e El evento disparado.
     */
    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            //The user has clicked the cell, so
            //bring up the dialog.
            button.setBackground(currentColor);
            colorChooser.setColor(currentColor);
            dialog.setVisible(true);

            //Make the renderer reappear.
            fireEditingStopped();

        } else { //User pressed dialog's "OK" button.
            currentColor = colorChooser.getColor();
        }
    }
    
    /**
     * Implementa el único método de CellEditor que no existe en
     * AbstractCellEditor.
     * @return El color actual.
     */
    public Object getCellEditorValue() {
        return currentColor;
    }
    
    /**
     * Implementa el método definido en TableCellEditor.
     * @param table La tabla donde se encuentra el componente.
     * @param value El valor de la celda.
     * @param isSelected Si está seleccionada o no.
     * @param row La fila en que se encuentra la celda.
     * @param column La columna en que se encuentra la celda.
     * @return El botón que abre el cuadro de diálogo.
     */
    public Component getTableCellEditorComponent(JTable table,
                                                 Object value,
                                                 boolean isSelected,
                                                 int row,
                                                 int column) {
        currentColor = (Color)value;
        return button;
    }
}
