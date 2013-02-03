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
package delphsim;

import delphsim.model.Epidemia;
import delphsim.model.Resultado;
import delphsim.util.*;

import java.awt.Color;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.application.Action;

import org.nfunk.jep.*;

/**
 * Interfaz para que el usuario pueda especificar un nuevo resultado para una
 * simulación.
 * @author Víctor E. Tamames Gómez
 */
public class GestionarResultado extends javax.swing.JDialog {

    /**
     * Flag que toma el valor verdadero si estamos modificando un resultado,
     * falso en caso contrario.
     */
    private boolean modificando = false;
    
    /**
     * Referencia a la epidemia a la cual pertenecen los resultados.
     */
    private Epidemia epidemia;
    
    /**
     * Índice del resultado con el que tenemos que tratar.
     */
    private int numResultado;
    
    /**
     * Crea una nueva ventana de GestionarResultado.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia de la que queremos obtener resultados.
     * @param indice El índice del resultado a modificar, <CODE>-1</CODE> si
     *               se trata de uno nuevo.
     */
    public GestionarResultado(java.awt.Frame parent, boolean modal, Epidemia epi, int indice) {
        // Operaciones iniciales
        super(parent, modal);
        initComponents();
        this.epidemia = epi;
        
        // Iniciar la tabla para que pueda mostrar colores
        this.definicionTable.setModel(new MyTableModel());
        this.definicionTable.setDefaultRenderer(Color.class,
                new ColorRenderer(true));
        this.definicionTable.setDefaultEditor(Color.class,
                new ColorEditor());
        this.definicionTable.getColumnModel().getColumn(0).setMinWidth(27);
        this.definicionTable.getColumnModel().getColumn(0).setMaxWidth(27);
        this.definicionTable.getColumnModel().getColumn(3).setMinWidth(80);
        this.definicionTable.getColumnModel().getColumn(3).setMaxWidth(80);
        this.definicionTable.getColumnModel().getColumn(4).setMinWidth(50);
        this.definicionTable.getColumnModel().getColumn(4).setMaxWidth(50);
        this.definicionTable.getTableHeader().setReorderingAllowed(false);

        // Si se ha pasado un resultado, hay que modificarlo
        if (indice != -1) {
            this.numResultado = indice;
            cargarResultado();
            this.modificando = true;
        } else {
            this.numResultado = this.epidemia.getResultados().length;
        }
        
        // Centrar y mostrar
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /** 
     * Este método se llama desde el constructor para iniciar los componentes.
     * ADVERTENCIA: NO modificar este código. El contenido de este método es
     * generado siempre por el Editor de Formularios de NetBeans 6.1.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tituloLabel = new javax.swing.JLabel();
        tituloSeparator = new javax.swing.JSeparator();
        expTextPane = new javax.swing.JTextPane();
        tituloResLabel = new javax.swing.JLabel();
        tituloResTextField = new javax.swing.JTextField();
        ejexLabel = new javax.swing.JLabel();
        ejexTextField = new javax.swing.JTextField();
        ejeyLabel = new javax.swing.JLabel();
        ejeyTextField = new javax.swing.JTextField();
        numFuncionesLabel = new javax.swing.JLabel();
        numFuncionesSpinner = new javax.swing.JSpinner();
        definicionScrollPane = new javax.swing.JScrollPane();
        definicionTable = new javax.swing.JTable();
        buttonSeparator = new javax.swing.JSeparator();
        cancelarButton = new javax.swing.JButton();
        aceptarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarResultado.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        tituloLabel.setFont(resourceMap.getFont("tituloLabel.font")); // NOI18N
        tituloLabel.setText(resourceMap.getString("tituloLabel.text")); // NOI18N
        tituloLabel.setName("tituloLabel"); // NOI18N

        tituloSeparator.setName("tituloSeparator"); // NOI18N

        expTextPane.setBackground(this.getBackground());
        expTextPane.setBorder(null);
        expTextPane.setEditable(false);
        expTextPane.setText(resourceMap.getString("expTextPane.text")); // NOI18N
        expTextPane.setName("expTextPane"); // NOI18N

        tituloResLabel.setText(resourceMap.getString("tituloResLabel.text")); // NOI18N
        tituloResLabel.setName("tituloResLabel"); // NOI18N

        tituloResTextField.setName("tituloResTextField"); // NOI18N

        ejexLabel.setText(resourceMap.getString("ejexLabel.text")); // NOI18N
        ejexLabel.setName("ejexLabel"); // NOI18N

        ejexTextField.setName("ejexTextField"); // NOI18N

        ejeyLabel.setText(resourceMap.getString("ejeyLabel.text")); // NOI18N
        ejeyLabel.setName("ejeyLabel"); // NOI18N

        ejeyTextField.setName("ejeyTextField"); // NOI18N

        numFuncionesLabel.setText(resourceMap.getString("numFuncionesLabel.text")); // NOI18N
        numFuncionesLabel.setName("numFuncionesLabel"); // NOI18N

        numFuncionesSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        numFuncionesSpinner.setName("numFuncionesSpinner"); // NOI18N
        numFuncionesSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numFuncionesSpinnerStateChanged(evt);
            }
        });

        definicionScrollPane.setName("definicionScrollPane"); // NOI18N

        definicionTable.setName("definicionTable"); // NOI18N
        definicionScrollPane.setViewportView(definicionTable);

        buttonSeparator.setName("buttonSeparator"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(GestionarResultado.class, this);
        cancelarButton.setAction(actionMap.get("cancelar")); // NOI18N
        cancelarButton.setName("cancelarButton"); // NOI18N

        aceptarButton.setAction(actionMap.get("aceptar")); // NOI18N
        aceptarButton.setName("aceptarButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addComponent(tituloLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(aceptarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelarButton))
                    .addComponent(buttonSeparator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addComponent(expTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tituloResLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tituloResTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ejeyLabel)
                            .addComponent(ejexLabel)
                            .addComponent(numFuncionesLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(numFuncionesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ejexTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
                            .addComponent(ejeyTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)))
                    .addComponent(definicionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {aceptarButton, cancelarButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tituloSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(expTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tituloResLabel)
                    .addComponent(tituloResTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ejexTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ejexLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ejeyTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ejeyLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numFuncionesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numFuncionesLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(definicionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelarButton)
                    .addComponent(aceptarButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {aceptarButton, cancelarButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

/**
 * Se encarga de actualizar el número de filas de la tabla de funciones en base
 * al valor que se haya puesto en el spinner.
 * @param evt El evento producido.
 */
private void numFuncionesSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numFuncionesSpinnerStateChanged
    // Actualizar el número de filas de la tabla en función del spinner
    ((MyTableModel)this.definicionTable.getModel()).setRowCount((Integer)this.numFuncionesSpinner.getValue());
}//GEN-LAST:event_numFuncionesSpinnerStateChanged
    
    /**
     * Acción a realizar cuando se presiona "Cancelar", cierra la ventana sin
     * haber aplicado ningún cambio.
     */
    @Action
    public void cancelar() {
        // Cerrar la ventana
        dispose();
    }
    
    /**
     * Acción a realizar cuando se presiona "Aceptar", comprueba que los campos
     * sean correctos y, si así fuera, crea un nuevo resultado y cierra la
     * ventana; en caso de error muestra el mensaje adecuado.
     */
    @Action
    public void aceptar() {
        // Comprobamos, y si hay algún error, mostramos el mensaje y volvemos
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarResultado.class);
        String errores = this.comprobar();
        if (!errores.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, errores, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
            return;
        }
        // Crear el resultado y añadirlo a la epidemia
        if (this.modificando) {
            Resultado res = this.epidemia.getResultado(this.numResultado);
            res.setTitulo(this.tituloResTextField.getText());
            res.setXLabel(this.ejexTextField.getText());
            res.setYLabel(this.ejeyTextField.getText());
            res.eliminarFunciones();
            for (int i = 0; i < this.definicionTable.getRowCount(); i++) {
                res.anadirFuncion(
                        this.definicionTable.getValueAt(i, 1).toString(),
                        this.definicionTable.getValueAt(i, 2).toString(), 
                        (Color)this.definicionTable.getValueAt(i, 3), 
                        (Integer)this.definicionTable.getValueAt(i, 4));
            }
        } else {
            Resultado res = new Resultado();
            res.setTitulo(this.tituloResTextField.getText());
            res.setXLabel(this.ejexTextField.getText());
            res.setYLabel(this.ejeyTextField.getText());
            for (int i = 0; i < this.definicionTable.getRowCount(); i++) {
                res.anadirFuncion(
                        this.definicionTable.getValueAt(i, 1).toString(),
                        this.definicionTable.getValueAt(i, 2).toString(), 
                        (Color)this.definicionTable.getValueAt(i, 3), 
                        (Integer)this.definicionTable.getValueAt(i, 4));
            }
            this.epidemia.anadirResultado(res);
        }
        // Cerrar la ventana
        dispose();
    }
    
    /**
     * Comprueba que los campos sean correctos.
     */
    public String comprobar() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarResultado.class);
        String errores = ""; // NOI18N
        
        // El título es obligatorio
        if (this.tituloResTextField.getText() == null || this.tituloResTextField.getText().isEmpty()) {
            errores += resourceMap.getString("comprobacion.error.tituloVacio"); // NOI18N
        }
        
        // Un nuevo analizador sintáctico
        JEP jep = Epidemia.CrearDelphSimJEP();
        // Añadimos todos los parámetros
        for (int i = 0; i < this.epidemia.getParametros().length; i++) {
            jep.addVariable(this.epidemia.getParametro(i).getNombre(), 1);
        }
        // Añadimos todos los procesos
        for (int i = 0; i < this.epidemia.getProcesos().length; i++) {
            jep.addVariable(this.epidemia.getProceso(i).getNombre(), 1);
        }
        // Añadimos todos los compartimentos
        for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
            jep.addVariable(this.epidemia.getCompartimento(i).getNombre(), 1);
        }
        // Añadimos todos los atajos
        for (int i = 0; i < this.epidemia.getAtajos().length; i++) {
            jep.addVariable(this.epidemia.getAtajo(i).getNombre(), 1);
        }
        
        // Comprobar que todos los campos de la tabla están bien definidos
        for (int i = 0; i < this.definicionTable.getRowCount(); i++) {
            // El nombre de cada función es obligatorio para la leyenda (valueAt i,1)
            if (this.definicionTable.getValueAt(i, 1) == null || this.definicionTable.getValueAt(i, 1).toString().isEmpty()) {
                errores += resourceMap.getString("comprobacion.error.nombreInvalido", i+1); // NOI18N
            }
            // Definición correcta, usando JEP (valueAt i,2)
            // Si tienen puntos y coma -> inválida
            if (this.definicionTable.getValueAt(i, 2).toString().contains(";")) {
                errores += resourceMap.getString("comprobacion.error.definicionInvalida", i+1); // NOI18N
            } else {
                try {
                    jep.evaluate(jep.parseExpression(this.definicionTable.getValueAt(i, 2).toString()));
                } catch (ParseException ex) {
                    System.err.println(ex.getMessage());
                    errores += resourceMap.getString("comprobacion.error.definicionInvalida", i+1); // NOI18N
                }
            }
            // El color siempre va a ser correcto, ni se mira (valueAt i,3)
            // El grosor debe ser un entero entre 1 y 10 (valueAt i,4)
            if ((Integer)this.definicionTable.getValueAt(i, 4) < 1 || (Integer)this.definicionTable.getValueAt(i, 4) > 10) {
                errores += resourceMap.getString("comprobacion.error.grosorInvalido", i+1); // NOI18N
            }
        }
        
        return errores;
    }
    
    /**
     * Si estuviéramos modificando un resultado, este método se encarga de
     * rellenar desde un principio los campos del formulario con la información
     * que se hubiera introducido anteriormente para este resultado.
     */
    private void cargarResultado() {
        this.tituloResTextField.setText(this.epidemia.getResultado(this.numResultado).getTitulo());
        this.ejexTextField.setText(this.epidemia.getResultado(this.numResultado).getXLabel());
        this.ejeyTextField.setText(this.epidemia.getResultado(this.numResultado).getYLabel());
        this.numFuncionesSpinner.setValue(this.epidemia.getResultado(this.numResultado).getNumFunciones());
        for (int i = 0; i < this.epidemia.getResultado(this.numResultado).getNumFunciones(); i++) {
            Object[] funcion = this.epidemia.getResultado(this.numResultado).getFuncion(i);
            for (int j = 0; j < funcion.length; j++) {
                this.definicionTable.setValueAt(funcion[j], i, j+1);
            }
        }
    }

    /**
     * Clase interna que extiende un modelo abstracto de tabla para adecuarla
     * a las necesidades de esta interfaz. En concreto, permite que una o más
     * de sus columnas sean de tipo "Color".
     * @see delphsim.util.ColorEditor ColorEditor
     * @see delphsim.util.ColorRenderer ColorRenderer
     */
    class MyTableModel extends AbstractTableModel {

        /**
         * Nombres por defecto de las columnas.
         */
        private String[] columnNames = {"Nº", // NOI18N
            "Nombre", // NOI18N
            "Definición", // NOI18N
            "Color", // NOI18N
            "Grosor" // NOI18N
        };
        
        /**
         * Contenido por defecto de la tabla.
         */
        private Object[][] data = {
            {new Integer(1), "", "", new Color(0, 0, 0), // NOI18N
                new Integer(1)
            }
        };

        /**
         * Devuelve el número de columnas de la tabla.
         * @return El número de columnas de la tabla.
         */
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
         * Devuelve el número de filas de la tabla.
         * @return El número de filas de la tabla.
         */
        public int getRowCount() {
            return data.length;
        }
        /**
         * Dado un índice, devuelve el nombre de la columna en dicha posición.
         * @param col Índice de la columna cuyo nombre se quiere obtener.
         * @return El nombre de la columna.
         */
        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        /**
         * Dada una celda, identificada por su fila y columna, devuelve su
         * contenido.
         * @param row Índice de la fila donde se encuentra la celda.
         * @param col Índice de la columna donde se encuentra la celda.
         * @return El contenido de la celda.
         */
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }

        /**
         * <CODE>JTable</CODE> usa este método para determinar el
         * <i>renderer</i>/editor por defecto para cada celda.
         * @param c El índice de la columna cuyo tipo se quiere conocer.
         * @return El tipo de la columna.
         */
        @Override
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        /**
         * Sirve para conocer si una celda dada es editable o no. En este caso
         * todas las celdas son editables excepto las de la primera columna.
         * @param row La fila de la celda.
         * @param col La columna de la celda.
         * @return Si es editable o no.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            //Note that the data/cell address is constant,
            //no matter where the cell appears onscreen.
            if (col < 1) {
                return false;
            } else {
                return true;
            }
        }

        /**
         * Método para establecer un valor en una celda.
         * @param value El contenido nuevo.
         * @param row La fila de la celda.
         * @param col La columna de la celda.
         */
        @Override
        public void setValueAt(Object value, int row, int col) {
            data[row][col] = value;
            fireTableCellUpdated(row, col);
        }
        
        /**
         * Método para establecer el número de filas de una celda. Si se
         * incrementa, se añaden filas vacías; si se disminuye, se borran las
         * últimas filas pero el resto no varían en contenido.
         * @param rowCount El número de filas que debe haber.
         */
        public void setRowCount(int rowCount) {
            if (this.getRowCount() == rowCount) {
                return;
            } else {
                Object[][] o = new Object[rowCount][this.getColumnCount()];
                if (this.getRowCount() > rowCount) {
                    for (int i = 0; i < rowCount; i++) {
                        o[i] = this.data[i];
                    }
                } else {
                    for (int i = 0; i < this.getRowCount(); i++) {
                        o[i] = this.data[i];
                    }
                    for (int i = this.getRowCount(); i < rowCount; i++) {
                        o[i][0] = new Integer(i+1);
                        o[i][1] = ""; // NOI18N
                        o[i][2] = ""; // NOI18N
                        o[i][3] = new Color(0, 0, 0);
                        o[i][4] = new Integer(1);
                    }
                }
                this.data = o;
                this.fireTableDataChanged();
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarButton;
    private javax.swing.JSeparator buttonSeparator;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JScrollPane definicionScrollPane;
    private javax.swing.JTable definicionTable;
    private javax.swing.JLabel ejexLabel;
    private javax.swing.JTextField ejexTextField;
    private javax.swing.JLabel ejeyLabel;
    private javax.swing.JTextField ejeyTextField;
    private javax.swing.JTextPane expTextPane;
    private javax.swing.JLabel numFuncionesLabel;
    private javax.swing.JSpinner numFuncionesSpinner;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JLabel tituloResLabel;
    private javax.swing.JTextField tituloResTextField;
    private javax.swing.JSeparator tituloSeparator;
    // End of variables declaration//GEN-END:variables

}
