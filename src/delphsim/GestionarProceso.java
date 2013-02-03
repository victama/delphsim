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

import delphsim.model.*;
import delphsim.util.*;

import javax.swing.table.*;

import org.jdesktop.application.Action;
import org.nfunk.jep.*;

/**
 * Interfaz de usuario para añadir o modificar un proceso seleccionado con la
 * interfaz proporcionada por GestionarVariables.java
 * @author Víctor E. Tamames Gómez
 */
public class GestionarProceso extends javax.swing.JDialog {

    /**
     * Flag que toma el valor verdadero si estamos modificando un proceso,
     * falso en caso contrario.
     */
    private boolean modificando = false;
    
    /**
     * Referencia a la epidemia a la cual pertenecen los procesos.
     */
    private Epidemia epidemia;
    
    /**
     * Índice del proceso con el que tenemos que tratar.
     */
    private int numProceso;

    /**
     * Modelo de la tabla de tramos en la definición continua.
     */
    DefaultTableModel modeloTramosContinua = new DefaultTableModel(
        new Object [][] {{new Integer(0), null}},
        new String [] {"Tiempo de inicio", "Definición"}) { // NOI18N
            Class[] types = new Class [] {java.lang.Integer.class, java.lang.String.class};
            @Override
            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                if (rowIndex == 0 && columnIndex == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        };

    /**
     * Crea una nueva ventana de esta clase.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia con la que vamos a tratar.
     * @param indice El índice del proceso con el que vamos a tratar.
     */
    public GestionarProceso(java.awt.Frame parent, boolean modal, Epidemia epi, int indice) {
        super(parent, modal);
        initComponents();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarProceso.class);
        // Poner tamaño máximo 64 caracteres al jTextField del nombre
        this.nombreTextField.setDocument(new JTextFieldLimit(64));
        // Y máximo 500 a la descripción
        this.descripcionTextField.setDocument(new JTextFieldLimit(500));

        // Personalizar la tabla de divisiones con algún detalle más
        this.tramosContinuaTable.getColumnModel().getColumn(0).setMinWidth(110);
        this.tramosContinuaTable.getColumnModel().getColumn(0).setMaxWidth(110);
        this.tramosContinuaTable.getColumnModel().getColumn(0).setResizable(false);
        this.tramosContinuaTable.getTableHeader().setReorderingAllowed(false);
        this.unidadTiempo.setText(resourceMap.getString("unidadTiempo.text", epi.getUnidadTiempo()));
        
        this.epidemia = epi;

        // Si se ha pasado un parámetro, hay que modificarlo
        if (indice != -1) {
            this.numProceso = indice;
            cargarProceso();
            this.modificando = true;
        } else {
            this.numProceso = this.epidemia.getProcesos().length;
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
        mainTabbedPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        aclaracionTextPane = new javax.swing.JTextPane();
        nombreLabel = new javax.swing.JLabel();
        nombreTextField = new javax.swing.JTextField();
        descripcionLabel = new javax.swing.JLabel();
        descripcionTextField = new javax.swing.JTextField();
        continuaPanel = new javax.swing.JPanel();
        continuaTextPane1 = new javax.swing.JTextPane();
        continuaTextPane2 = new javax.swing.JTextPane();
        unidadTiempo = new javax.swing.JLabel();
        numTramosContinuaLabel = new javax.swing.JLabel();
        numTramosContinuaSpinner = new javax.swing.JSpinner();
        tramosContinuaScrollPane = new javax.swing.JScrollPane();
        tramosContinuaTable = new javax.swing.JTable();
        cancelarButton = new javax.swing.JButton();
        aceptarButton = new javax.swing.JButton();
        botonesSeparator = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarProceso.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        tituloLabel.setFont(resourceMap.getFont("tituloLabel.font")); // NOI18N
        tituloLabel.setText(resourceMap.getString("tituloLabel.text")); // NOI18N
        tituloLabel.setName("tituloLabel"); // NOI18N

        tituloSeparator.setName("tituloSeparator"); // NOI18N

        mainTabbedPane.setName("mainTabbedPane"); // NOI18N

        generalPanel.setName("generalPanel"); // NOI18N

        aclaracionTextPane.setBackground(this.getBackground());
        aclaracionTextPane.setBorder(null);
        aclaracionTextPane.setEditable(false);
        aclaracionTextPane.setText(resourceMap.getString("aclaracionTextPane.text")); // NOI18N
        aclaracionTextPane.setName("aclaracionTextPane"); // NOI18N

        nombreLabel.setText(resourceMap.getString("nombreLabel.text")); // NOI18N
        nombreLabel.setName("nombreLabel"); // NOI18N

        nombreTextField.setToolTipText(resourceMap.getString("nombreTextField.toolTipText")); // NOI18N
        nombreTextField.setName("nombreTextField"); // NOI18N

        descripcionLabel.setText(resourceMap.getString("descripcionLabel.text")); // NOI18N
        descripcionLabel.setName("descripcionLabel"); // NOI18N

        descripcionTextField.setName("descripcionTextField"); // NOI18N

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nombreLabel)
                    .addComponent(descripcionLabel)
                    .addComponent(descripcionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addComponent(nombreTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addComponent(aclaracionTextPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aclaracionTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nombreLabel)
                .addGap(7, 7, 7)
                .addComponent(nombreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(descripcionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(descripcionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab(resourceMap.getString("generalPanel.TabConstraints.tabTitle"), generalPanel); // NOI18N

        continuaPanel.setName("continuaPanel"); // NOI18N

        continuaTextPane1.setBackground(this.getBackground());
        continuaTextPane1.setBorder(null);
        continuaTextPane1.setEditable(false);
        continuaTextPane1.setText(resourceMap.getString("continuaTextPane1.text")); // NOI18N
        continuaTextPane1.setName("continuaTextPane1"); // NOI18N

        continuaTextPane2.setBackground(this.getBackground());
        continuaTextPane2.setBorder(null);
        continuaTextPane2.setEditable(false);
        continuaTextPane2.setText(resourceMap.getString("continuaTextPane2.text")); // NOI18N
        continuaTextPane2.setName("continuaTextPane2"); // NOI18N

        unidadTiempo.setText(resourceMap.getString("unidadTiempo.text")); // NOI18N
        unidadTiempo.setName("unidadTiempo"); // NOI18N

        numTramosContinuaLabel.setText(resourceMap.getString("numTramosContinuaLabel.text")); // NOI18N
        numTramosContinuaLabel.setName("numTramosContinuaLabel"); // NOI18N

        numTramosContinuaSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        numTramosContinuaSpinner.setName("numTramosContinuaSpinner"); // NOI18N
        numTramosContinuaSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numTramosContinuaSpinnerStateChanged(evt);
            }
        });

        tramosContinuaScrollPane.setName("tramosContinuaScrollPane"); // NOI18N

        tramosContinuaTable.setModel(this.modeloTramosContinua);
        tramosContinuaTable.setName("tramosContinuaTable"); // NOI18N
        tramosContinuaScrollPane.setViewportView(tramosContinuaTable);

        javax.swing.GroupLayout continuaPanelLayout = new javax.swing.GroupLayout(continuaPanel);
        continuaPanel.setLayout(continuaPanelLayout);
        continuaPanelLayout.setHorizontalGroup(
            continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, continuaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tramosContinuaScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addComponent(continuaTextPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addComponent(continuaTextPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, continuaPanelLayout.createSequentialGroup()
                        .addComponent(numTramosContinuaLabel)
                        .addGap(18, 18, 18)
                        .addComponent(numTramosContinuaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 160, Short.MAX_VALUE)
                        .addComponent(unidadTiempo)))
                .addContainerGap())
        );
        continuaPanelLayout.setVerticalGroup(
            continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(continuaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(continuaTextPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(continuaTextPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numTramosContinuaLabel)
                    .addComponent(numTramosContinuaSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(unidadTiempo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tramosContinuaScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainTabbedPane.addTab(resourceMap.getString("continuaPanel.TabConstraints.tabTitle"), continuaPanel); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(GestionarProceso.class, this);
        cancelarButton.setAction(actionMap.get("cancelar")); // NOI18N
        cancelarButton.setToolTipText(null);
        cancelarButton.setName("cancelarButton"); // NOI18N

        aceptarButton.setAction(actionMap.get("aceptar")); // NOI18N
        aceptarButton.setToolTipText(null);
        aceptarButton.setName("aceptarButton"); // NOI18N

        botonesSeparator.setName("botonesSeparator"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mainTabbedPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addComponent(tituloSeparator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addComponent(tituloLabel, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(aceptarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelarButton))
                    .addComponent(botonesSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tituloSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelarButton)
                    .addComponent(aceptarButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

/**
 * Método a realizar cuando se cambia el valor del <i>spinner</i> con el número
 * de tramos, actualizando el número de filas de la tabla inferior.
 * @param evt El evento generado.
 */
private void numTramosContinuaSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numTramosContinuaSpinnerStateChanged
    // Cuando se cambia el número de tramos en el spinner, se pone el mismo número de filas en la tabla
    this.modeloTramosContinua.setRowCount((Integer)this.numTramosContinuaSpinner.getValue());
}//GEN-LAST:event_numTramosContinuaSpinnerStateChanged

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Cancelar" de la
     * interfaz. Cierra la ventana sin efectuar ningún cambio sobre la epidemia.
     */
    @Action
    public void cancelar() {
        // Cerrar la ventana
        dispose();
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Aceptar". Comprueba
     * si los datos introducidos son válidos y crea/modifica un proceso con
     * ellos en caso afirmativo, cerrando después la ventana. En caso negativo,
     * muestra el mensaje de error correspondiente.
     */
    @Action
    public void aceptar() {
        // Creamos variables necesarias, comprobamos errores
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarProceso.class);
        java.util.regex.Pattern patronFunciones = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoFunciones")); // NOI18N
        java.util.regex.Pattern patronElementos = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoElementos")); // NOI18N
        java.util.regex.Matcher comprobador;
        String errores = comprobar();
        errores += comprobarDefContinua();
        // Si hay errores, se muestra mensaje de error; si no, se actúa en función de si estamos modificando o no
        if (!errores.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.mainTabbedPane, errores, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
            return;
        } else {
            if (this.modificando) {
                // Creamos el proceso con la información básica, guardando su nombre anterior
                Proceso modificandoProceso = this.epidemia.getProceso(this.numProceso);
                String nombreAnterior = modificandoProceso.getNombre();
                modificandoProceso.setNombre(this.nombreTextField.getText());
                modificandoProceso.setDescripcion(this.descripcionTextField.getText());
                // Eliminamos todos los posibles vínculos anteriores de otros elementos con éste
                for (int i = 0; i < this.epidemia.getParametros().length; i++) {
                    this.epidemia.getParametro(i).eliminarProcesoVinculado(nombreAnterior);
                }
                for (int i = 0; i < this.numProceso; i++) {
                    this.epidemia.getProceso(i).eliminarProcesoVinculado(nombreAnterior);
                }
                for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                    this.epidemia.getCompartimento(i).eliminarProcesoVinculado(nombreAnterior);
                }
                // Continua: si no se ha dejado en blanco, para cada fila de la tabla
                if (this.tramosContinuaTable.getValueAt(0, 1) != null && !this.tramosContinuaTable.getValueAt(0, 1).toString().trim().isEmpty()) {
                    TramoContinua[] tramos = new TramoContinua[this.tramosContinuaTable.getRowCount()];
                    for (int i = 0; i < this.tramosContinuaTable.getRowCount(); i++) {
                        // Un tramo nuevo con sus datos
                        tramos[i] = new TramoContinua();
                        tramos[i].setTiempoInicio((Integer)this.tramosContinuaTable.getValueAt(i, 0));
                        tramos[i].setDefinicionContinua(this.tramosContinuaTable.getValueAt(i, 1).toString());
                        // Eliminamos el uso de funciones de la cadena de la ecuación
                        // para no confundirlas con el nombre de otros elementos
                        String ecuacion = this.tramosContinuaTable.getValueAt(i, 1).toString();
                        comprobador = patronFunciones.matcher(ecuacion);
                        while (comprobador.find()) {
                            String principio = ecuacion.substring(0, comprobador.start());
                            ecuacion = principio + ecuacion.substring(comprobador.end());
                            comprobador = patronFunciones.matcher(ecuacion);
                        }
                        // Buscamos referencias a otros elementos usados y añadimos los vínculos
                        comprobador = patronElementos.matcher(ecuacion);
                        while (comprobador.find()) {
                            Class clase = this.epidemia.estaDefinido(comprobador.group());
                            if (clase == Parametro.class) {
                                this.epidemia.getParametro(comprobador.group()).anadirProcesoVinculado(this.nombreTextField.getText());
                            } else if (clase == Proceso.class) {
                                this.epidemia.getProceso(comprobador.group()).anadirProcesoVinculado(this.nombreTextField.getText());
                            } else if (clase == Compartimento.class) {
                                this.epidemia.getCompartimento(comprobador.group()).anadirProcesoVinculado(this.nombreTextField.getText());
                            } else if (clase == Atajo.class) {
                                String[] trozos = this.epidemia.getAtajo(comprobador.group()).getDefinicionContinua().split(" \\+ "); // NOI18N
                                for (int j = 0; j < trozos.length; j++) {
                                    this.epidemia.getCompartimento(trozos[j]).anadirProcesoVinculado(this.nombreTextField.getText());
                                }
                            } else {
                                System.err.println("Error: producido en GestionarProceso::aceptar(), modificando, el patrón ha encontrado un elemento que no está definido, debería haberse detectado en comprobarDefContinua()"); // NOI18N
                            }
                        }
                    }
                    modificandoProceso.setTramosContinua(tramos);
                } else {
                    modificandoProceso.setTramosContinua(null);
                }
                // Si hemos cambiado el nombre, habrá que revisar todos los elementos que dependen de él
                // y actualizar sus ecuaciones reemplazando su nombre viejo por el nuevo.
                // Nota: replaceAll no serviría (ej: my -> mu .. my1 -> mu1), y eso no es así.
                if (!nombreAnterior.equals(this.nombreTextField.getText())) {
                    java.util.regex.Pattern patron;
                    String eNueva;
                    // Para cada tramo de cada proceso vinculado
                    for (int i = 0; i < modificandoProceso.getProcesosVinculados().length; i++) {
                        Proceso procVinc = this.epidemia.getProceso(modificandoProceso.getProcesosVinculados()[i]);
                        for (int j = 0; j < procVinc.getTramosContinua().length; j++) {
                            TramoContinua tramo = procVinc.getTramosContinua()[j];
                            eNueva = tramo.getDefinicionContinua();
                            // Primero buscamos por si estuviera al principio
                            patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombrePrincipio", nombreAnterior)); // NOI18N
                            comprobador = patron.matcher(eNueva);
                            if (comprobador.find()) {
                                eNueva = this.nombreTextField.getText() + eNueva.substring(comprobador.end()-1);
                            }
                            // Segundo buscamos por si estuviera al final
                            patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreFinal", nombreAnterior)); // NOI18N
                            comprobador = patron.matcher(eNueva);
                            if (comprobador.find()) {
                                eNueva = eNueva.substring(0, comprobador.start()+1) + this.nombreTextField.getText();
                            }
                            // Tercero buscamos por si fuera propiamente un nombre de un elemento
                            patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreProceso")); // NOI18N
                            comprobador = patron.matcher(eNueva);
                            if (comprobador.find()) {
                                eNueva = this.nombreTextField.getText();
                            }
                            // Por último buscamos por si hubiera por el medio
                            patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreMedio", nombreAnterior)); // NOI18N
                            comprobador = patron.matcher(eNueva);
                            while (comprobador.find()) {
                                eNueva = eNueva.substring(0, comprobador.start()+1) + this.nombreTextField.getText() + eNueva.substring(comprobador.end()-1);
                                comprobador = patron.matcher(eNueva);
                            }
                            // Actualizamos la definición
                            tramo.setDefinicionContinua(eNueva);
                        }
                    }
                    // Y para cada compartimento vinculado
                    for (int i = 0; i < modificandoProceso.getCompartimentosVinculados().length; i++) {
                        Compartimento compVinc = this.epidemia.getCompartimento(modificandoProceso.getCompartimentosVinculados()[i]);
                        eNueva = compVinc.getDefinicionContinua();
                        // Primero buscamos por si estuviera al principio
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombrePrincipio", nombreAnterior)); // NOI18N
                        comprobador = patron.matcher(eNueva);
                        if (comprobador.find()) {
                            eNueva = this.nombreTextField.getText() + eNueva.substring(comprobador.end()-1);
                        }
                        // Segundo buscamos por si estuviera al final
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreFinal", nombreAnterior)); // NOI18N
                        comprobador = patron.matcher(eNueva);
                        if (comprobador.find()) {
                            eNueva = eNueva.substring(0, comprobador.start()+1) + this.nombreTextField.getText();
                        }
                        // Tercero buscamos por si fuera propiamente un nombre de un elemento
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreProceso")); // NOI18N
                        comprobador = patron.matcher(eNueva);
                        if (comprobador.find()) {
                            eNueva = this.nombreTextField.getText();
                        }
                        // Por último buscamos por si hubiera por el medio
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreMedio", nombreAnterior)); // NOI18N
                        comprobador = patron.matcher(eNueva);
                        while (comprobador.find()) {
                            eNueva = eNueva.substring(0, comprobador.start()+1) + this.nombreTextField.getText() + eNueva.substring(comprobador.end()-1);
                            comprobador = patron.matcher(eNueva);
                        }
                        // Actualizamos la definición
                        compVinc.setDefinicionContinua(eNueva);
                    }
                }
                // Borramos del conjunto de palabras reservadas su nombre anterior y añadimos el nuevo
                this.epidemia.getPalabrasReservadas().remove(nombreAnterior);
                this.epidemia.getPalabrasReservadas().add(this.nombreTextField.getText());
            } else {
                // Creamos el proceso con la información básica
                Proceso nuevoProceso = new Proceso();
                nuevoProceso.setNombre(this.nombreTextField.getText());
                nuevoProceso.setDescripcion(this.descripcionTextField.getText());
                // Continua: si no se ha dejado en blanco, para cada fila de la tabla
                if (this.tramosContinuaTable.getValueAt(0, 1) != null && !this.tramosContinuaTable.getValueAt(0, 1).toString().trim().isEmpty()) {
                    TramoContinua[] tramos = new TramoContinua[this.tramosContinuaTable.getRowCount()];
                    for (int i = 0; i < this.tramosContinuaTable.getRowCount(); i++) {
                        // Un tramo nuevo con sus datos
                        tramos[i] = new TramoContinua();
                        tramos[i].setTiempoInicio((Integer)this.tramosContinuaTable.getValueAt(i, 0));
                        tramos[i].setDefinicionContinua(this.tramosContinuaTable.getValueAt(i, 1).toString());
                        // Eliminamos el uso de funciones de la cadena de la ecuación
                        // para no confundirlas con el nombre de otros elementos
                        String ecuacion = this.tramosContinuaTable.getValueAt(i, 1).toString();
                        comprobador = patronFunciones.matcher(ecuacion);
                        while (comprobador.find()) {
                            String principio = ecuacion.substring(0, comprobador.start());
                            ecuacion = principio + ecuacion.substring(comprobador.end());
                            comprobador = patronFunciones.matcher(ecuacion);
                        }
                        // Buscamos referencias a otros elementos usados y añadimos los vínculos
                        comprobador = patronElementos.matcher(ecuacion);
                        while (comprobador.find()) {
                            Class clase = this.epidemia.estaDefinido(comprobador.group());
                            if (clase == Parametro.class) {
                                this.epidemia.getParametro(comprobador.group()).anadirProcesoVinculado(this.nombreTextField.getText());
                            } else if (clase == Proceso.class) {
                                this.epidemia.getProceso(comprobador.group()).anadirProcesoVinculado(this.nombreTextField.getText());
                            } else if (clase == Compartimento.class) {
                                this.epidemia.getCompartimento(comprobador.group()).anadirProcesoVinculado(this.nombreTextField.getText());
                            } else if (clase == Atajo.class) {
                                String[] trozos = this.epidemia.getAtajo(comprobador.group()).getDefinicionContinua().split(" \\+ "); // NOI18N
                                for (int j = 0; j < trozos.length; j++) {
                                    this.epidemia.getCompartimento(trozos[j]).anadirProcesoVinculado(this.nombreTextField.getText());
                                }
                            } else {
                                System.err.println("Error: producido en GestionarProceso::aceptar(), añadiendo, el patrón ha encontrado un elemento que no está definido, debería haberse detectado en comprobarDefContinua()"); // NOI18N
                            }
                        }
                    }
                    nuevoProceso.setTramosContinua(tramos);
                }
                // Añadimos el proceso a la epidemia y su nombre como reservado
                this.epidemia.anadirProceso(nuevoProceso);
                this.epidemia.getPalabrasReservadas().add(this.nombreTextField.getText());
            }
            // Por último, cerramos la ventana e indicamos modificaciones
            this.modificado = true;
            dispose();
        }
    }
    
    /**
     * Comprueba los campos generales del proceso que se está
     * añadiendo/modificando (nombre y descripción).
     * @return Una cadena de caracteres con los errores detectados. Vacía si no
     *         ha habido.
     */
    public String comprobar() {
        // Variables que vamos a usar
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarProceso.class);
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreProceso")); // NOI18N
        java.util.regex.Matcher comprobador;
        String errores = ""; // NOI18N
        
        // Comprobar que el nombre es válido
        comprobador = patron.matcher(this.nombreTextField.getText());
        if (!comprobador.find()) {
            errores += resourceMap.getString("comprobacion.error.nombreInvalido"); // NOI18N
        } else {
            // Comprobar que el nombre no esté siendo usado
            // Tanto si no estamos modificando como si, aún modificando, el nombre original difiere del final
            if (!(this.modificando && this.nombreTextField.getText().equals(this.epidemia.getProceso(this.numProceso).getNombre()))) {
                // Si el nombre está siendo usado (mirar tanto en palabras reservadas como en atajos)
                if (this.epidemia.getPalabrasReservadas().contains(this.nombreTextField.getText()) || this.epidemia.getPalabrasAtajos().contains(this.nombreTextField.getText())) {
                    errores += resourceMap.getString("comprobacion.error.nombreUsado"); // NOI18N
                }
            }
        }
        return errores;
    }
    
    /**
     * Comprueba los campos referidos a la definición para simulaciones
     * contínuas del proceso que se está añadiendo/modificando.
     * @return Una cadena de caracteres con los errores detectados. Vacía si no
     *         ha habido.
     */
    public String comprobarDefContinua() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarProceso.class);
        String errores = ""; // NOI18N
        
        // Preparamos un JEP añadiendo los elementos que se van a poder usar
        JEP jep = Epidemia.CrearDelphSimJEP();
        // Se podrán usar todos los parámetros
        for (int i = 0; i < this.epidemia.getParametros().length; i++) {
            jep.addVariable(this.epidemia.getParametro(i).getNombre(), 1);
        }
        // Se podrán usar todos los compartimentos generados
        for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
            jep.addVariable(this.epidemia.getCompartimento(i).getNombre(), 1);
        }
        // Se podrán usar los "atajos"
        for (int i = 0; i < this.epidemia.getPalabrasAtajos().size(); i++) {
            jep.addVariable(this.epidemia.getPalabrasAtajos().get(i).toString(), 1);
        }
        // Se podrán usar los procesos definidos previos a éste
        for (int i = 0; i < this.numProceso; i++) {
            jep.addVariable(this.epidemia.getProceso(i).getNombre(), 1);
        }
        
        // Si sólo hay un tramo
        if (this.tramosContinuaTable.getRowCount() == 1) {
            // Y no se ha introducido definición para él -> no se quiere definir -> no hay errores
            if (this.tramosContinuaTable.getValueAt(0, 1) == null || this.tramosContinuaTable.getValueAt(0, 1).toString().trim().isEmpty()) {
                return errores;
            } else {
                // Si no, comprobar la definición de ese único tramo
                // Si la cadena contiene algún punto y coma -> inválida
                if (this.tramosContinuaTable.getValueAt(0, 1).toString().contains(";")) {
                    errores += resourceMap.getString("comprobacion.error.defContinuaInvalida"); // NOI18N
                    return errores;
                }
                Node funcion = jep.parseExpression(this.tramosContinuaTable.getValueAt(0, 1).toString());
                try {
                    jep.evaluate(funcion);
                } catch (ParseException ex) {
                    System.err.println(ex.getMessage());
                    errores += resourceMap.getString("comprobacion.error.defContinuaInvalida"); // NOI18N
                }
                // Y terminar
                return errores;
            }
        }
        
        // Si hay varios tramos, los tiempos de inicio deben ser secuenciales: si i < j, t(i) < t(j)
        for (int i = 1; i < this.tramosContinuaTable.getRowCount(); i++) {
            try {
                if ((Integer)this.tramosContinuaTable.getValueAt(i-1, 0) >= (Integer)this.tramosContinuaTable.getValueAt(i, 0)) {
                    errores += resourceMap.getString("comprobacion.error.defContinuaDesordenado", i, i+1); // NOI18N
                }
            } catch (java.lang.NullPointerException ex) {
                errores += resourceMap.getString("comprobacion.error.defContinuaSinTiempoInicio", i+1); // NOI18N
            }
        }
        
        // Si hay varios tramos, todas las definiciones deben ser correctas
        for (int i = 0; i < this.tramosContinuaTable.getRowCount(); i++) {
            try {
                // Comprobar la definición del tramo (i+1)
                // Si tienen puntos y coma -> inválida
                if (this.tramosContinuaTable.getValueAt(i, 1).toString().contains(";")) {
                    errores += resourceMap.getString("comprobacion.error.defContinuaInvalidaTramo", i+1); // NOI18N
                } else {
                    Node funcion = jep.parseExpression(this.tramosContinuaTable.getValueAt(i, 1).toString());
                    try {
                        jep.evaluate(funcion);
                    } catch (ParseException ex) {
                        System.err.println(ex.getMessage());
                        errores += resourceMap.getString("comprobacion.error.defContinuaInvalidaTramo", i+1); // NOI18N
                    }
                }
            } catch (java.lang.NullPointerException ex) {
                errores += resourceMap.getString("comprobacion.error.defContinuaVaciaTramo", i+1); // NOI18N
            }
        }
        
        // Devolvemos el mensaje de error
        return errores;
    }
    
    /**
     * Método que se encarga de rellenar los campos con los datos del proceso
     * que se vaya a modificar.
     */
    public void cargarProceso() {
        // Actualizar título de la ventana y del jLabel
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarProceso.class);
        this.setTitle(resourceMap.getString("Form.title.modificar", this.epidemia.getProceso(this.numProceso).getNombre())); // NOI18N
        this.tituloLabel.setText(resourceMap.getString("tituloLabel.text.modificar", this.epidemia.getProceso(this.numProceso).getNombre())); // NOI18N
        // Cargar la información general del proceso
        this.nombreTextField.setText(this.epidemia.getProceso(this.numProceso).getNombre());
        this.descripcionTextField.setText(this.epidemia.getProceso(this.numProceso).getDescripcion());
        // Cargar la parte continua
        if (this.epidemia.getProceso(this.numProceso).getTramosContinua() != null && this.epidemia.getProceso(this.numProceso).getTramosContinua().length > 0) {
            int numTramos = this.epidemia.getProceso(this.numProceso).getTramosContinua().length;
            this.numTramosContinuaSpinner.setValue(numTramos);
            this.modeloTramosContinua.setRowCount(numTramos);
            for (int i = 0; i < numTramos; i++) {
                this.modeloTramosContinua.setValueAt(this.epidemia.getProceso(this.numProceso).getTramoContinua(i).getTiempoInicio(), i, 0);
                this.modeloTramosContinua.setValueAt(this.epidemia.getProceso(this.numProceso).getTramoContinua(i).getDefinicionContinua(), i, 1);
            }
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarButton;
    private javax.swing.JTextPane aclaracionTextPane;
    private javax.swing.JSeparator botonesSeparator;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JPanel continuaPanel;
    private javax.swing.JTextPane continuaTextPane1;
    private javax.swing.JTextPane continuaTextPane2;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JTextField descripcionTextField;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JTextField nombreTextField;
    private javax.swing.JLabel numTramosContinuaLabel;
    private javax.swing.JSpinner numTramosContinuaSpinner;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JSeparator tituloSeparator;
    private javax.swing.JScrollPane tramosContinuaScrollPane;
    private javax.swing.JTable tramosContinuaTable;
    private javax.swing.JLabel unidadTiempo;
    // End of variables declaration//GEN-END:variables
    protected boolean modificado = false;
}
