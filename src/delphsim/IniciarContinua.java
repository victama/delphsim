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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

/**
 * Clase que representa la interfaz a través de la cual el usuario puede añadir,
 * modificar y eliminar resultados que desea obtener para la nueva simulación.
 * @author Víctor E. Tamames Gómez
 */
public class IniciarContinua extends javax.swing.JDialog {
    
    /**
     * Flag que indica si, cuando se cierre esta ventana, la interfaz principal
     * debe iniciar la simulación o no.
     */
    private boolean continuar = false;
    
    /**
     * La epidemia que se va a simular, cuyos resultados debe definir el usuario.
     */
    private Epidemia epidemia;
    
    /**
     * Crea y muestra una nueva instancia de IniciarContinua.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia con la que vamos a tratar.
     */
    public IniciarContinua(java.awt.Frame parent, boolean modal, Epidemia epi) {
        // Iniciar componentes
        super(parent, modal);
        initComponents();
        this.epidemia = epi;
        this.modificarButton.setEnabled(false);
        this.eliminarButton.setEnabled(false);
        
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
        exp1TextPane = new javax.swing.JTextPane();
        exp2TextPane = new javax.swing.JTextPane();
        buttonSeparator = new javax.swing.JSeparator();
        cancelarButton = new javax.swing.JButton();
        aceptarButton = new javax.swing.JButton();
        resultadosScrollPane = new javax.swing.JScrollPane();
        resultadosList = new javax.swing.JList();
        anadirButton = new javax.swing.JButton();
        modificarButton = new javax.swing.JButton();
        eliminarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(IniciarContinua.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        tituloLabel.setFont(resourceMap.getFont("tituloLabel.font")); // NOI18N
        tituloLabel.setText(resourceMap.getString("tituloLabel.text")); // NOI18N
        tituloLabel.setName("tituloLabel"); // NOI18N

        tituloSeparator.setName("tituloSeparator"); // NOI18N

        exp1TextPane.setBackground(this.getBackground());
        exp1TextPane.setBorder(null);
        exp1TextPane.setEditable(false);
        exp1TextPane.setText(resourceMap.getString("exp1TextPane.text")); // NOI18N
        exp1TextPane.setName("exp1TextPane"); // NOI18N

        exp2TextPane.setBackground(this.getBackground());
        exp2TextPane.setBorder(null);
        exp2TextPane.setEditable(false);
        exp2TextPane.setText(resourceMap.getString("exp2TextPane.text")); // NOI18N
        exp2TextPane.setName("exp2TextPane"); // NOI18N

        buttonSeparator.setName("buttonSeparator"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(IniciarContinua.class, this);
        cancelarButton.setAction(actionMap.get("cancelar")); // NOI18N
        cancelarButton.setName("cancelarButton"); // NOI18N

        aceptarButton.setAction(actionMap.get("aceptar")); // NOI18N
        aceptarButton.setName("aceptarButton"); // NOI18N

        resultadosScrollPane.setName("resultadosScrollPane"); // NOI18N

        resultadosList.setFont(resourceMap.getFont("resultadosList.font")); // NOI18N
        resultadosList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        resultadosList.setName("resultadosList"); // NOI18N
        resultadosList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                resultadosListValueChanged(evt);
            }
        });
        resultadosScrollPane.setViewportView(resultadosList);

        anadirButton.setAction(actionMap.get("anadirResultado")); // NOI18N
        anadirButton.setName("anadirButton"); // NOI18N

        modificarButton.setAction(actionMap.get("modificarResultado")); // NOI18N
        modificarButton.setName("modificarButton"); // NOI18N

        eliminarButton.setAction(actionMap.get("eliminarResultado")); // NOI18N
        eliminarButton.setName("eliminarButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(resultadosScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(anadirButton)
                            .addComponent(modificarButton)
                            .addComponent(eliminarButton)))
                    .addComponent(exp1TextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                    .addComponent(tituloSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                    .addComponent(tituloLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(aceptarButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelarButton))
                    .addComponent(exp2TextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
                    .addComponent(buttonSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {anadirButton, eliminarButton, modificarButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tituloSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exp1TextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exp2TextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(anadirButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarButton))
                    .addComponent(resultadosScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(buttonSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelarButton)
                    .addComponent(aceptarButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {anadirButton, eliminarButton, modificarButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

/**
 * Evento que se acciona cuando se cambia la selección de la lista de resultados.
 * @param evt El evento generado.
 */
private void resultadosListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_resultadosListValueChanged
    // Si hay alguno seleccionado, se activan los botones Modificar y Eliminar
    if (this.resultadosList.getSelectedIndex() == -1) {
        this.modificarButton.setEnabled(false);
        this.eliminarButton.setEnabled(false);
    } else {
        this.modificarButton.setEnabled(true);
        this.eliminarButton.setEnabled(true);
    }
}//GEN-LAST:event_resultadosListValueChanged
    
    /**
     * Acción a realizar cuando se presiona "Cancelar". Se limita a cerrar la
     * ventana. La simulación no comenzará porque no se ha modificado el flag
     * 'continuar'.
     */
    @Action
    public void cancelar() {
        dispose();
    }
    
    /**
     * Acción a realizar cuando se presiona "Aceptar". Comprueba que se haya
     * definido al menos un resultado (no tiene sentido simular sin resultados
     * a mostrar). Si así fuera, cambiaría el flag 'continuar' a <i>true</i>
     * para que se sepa que el usuario desea comenzar la simulación. En caso
     * contrario se mostraría mensaje de error y se retornaría.
     */
    @Action
    public void aceptar() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(IniciarContinua.class);
        if (this.resultadosList.getModel().getSize() < 1) {
            JOptionPane.showMessageDialog(this, resourceMap.getString("comprobaciones.noResultados.msg"), resourceMap.getString("comprobaciones.noResultados.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
            return;
        }
        // Dejar constancia de que se tiene que iniciar la simulación
        this.continuar = true;
        dispose();
    }

    /**
     * Acción a realizar cuando se presiona "Añadir", creando una nueva ventana
     * GestionarResultado en blanco.
     */
    @Action
    public void anadirResultado() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        new delphsim.GestionarResultado(mainFrame, true, this.epidemia, -1);
        this.resultadosList.setModel(this.epidemia.construirListaResultados());
    }

    /**
     * Acción a realizar cuando se presiona "Modificar", creando una nueva
     * ventana GestionarResultado con los datos que se hubieran introducido
     * para el resultado seleccionado en ese momento.
     */
    @Action
    public void modificarResultado() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        new delphsim.GestionarResultado(mainFrame, true, this.epidemia, this.resultadosList.getSelectedIndex());
        this.resultadosList.setModel(this.epidemia.construirListaResultados());
    }

    /**
     * Acción a realizar cuando se presiona "Eliminar". Se le pide a la Epidemia
     * que lo elimine, y se actualiza la vista.
     */
    @Action
    public void eliminarResultado() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(IniciarContinua.class);
        if (JOptionPane.showConfirmDialog(this, resourceMap.getString("comprobaciones.confirmarEliminarResultado.msg"), resourceMap.getString("comprobaciones.confirmarEliminarResultado.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) { // NOI18N
            // Si el usuario lo confirma, borramos y actualizamos la vista
            this.epidemia.eliminarResultado(this.resultadosList.getSelectedIndex());
            this.resultadosList.setModel(this.epidemia.construirListaResultados());
        }
    }
    
    /**
     * Método para que la ventana principal sepa si debe iniciar la simulación
     * o, por el contrario, el usuario lo canceló.
     */
    public boolean continuar() {
        return this.continuar;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarButton;
    private javax.swing.JButton anadirButton;
    private javax.swing.JSeparator buttonSeparator;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JButton eliminarButton;
    private javax.swing.JTextPane exp1TextPane;
    private javax.swing.JTextPane exp2TextPane;
    private javax.swing.JButton modificarButton;
    private javax.swing.JList resultadosList;
    private javax.swing.JScrollPane resultadosScrollPane;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JSeparator tituloSeparator;
    // End of variables declaration//GEN-END:variables

}
