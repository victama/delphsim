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

import delphsim.model.Compartimento;

import org.jdesktop.application.Action;

/**
 * Esta ventana muestra la información sobre la dinámica para simulaciones
 * continuas de un compartimento dado.
 * @author Víctor E. Tamames Gómez
 */
public class MostrarDinamicaContinuaCompartimento extends javax.swing.JDialog {

    /**
     * Crea una nueva ventana MostrarDinamicaContinuaCompartimento.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param comp El compartimento cuyo contenido tenemos que mostrar.
     */
    public MostrarDinamicaContinuaCompartimento(java.awt.Frame parent, boolean modal, Compartimento comp) {
        super(parent, modal);
        initComponents();
        jTextPaneNombre.setText(comp.getNombre());
        jTextPaneNombre.setCaretPosition(0);
        jTextFieldPersonas.setText(String.valueOf(comp.getHabitantes()));
        jTextPaneDefinicion.setText(comp.getDefinicionContinua());
        jTextPaneDefinicion.setCaretPosition(0);
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

        jPanel = new javax.swing.JPanel();
        jLabelTituloNombre = new javax.swing.JLabel();
        jLabelTituloPersonas = new javax.swing.JLabel();
        jLabelTituloDefinicion = new javax.swing.JLabel();
        jButtonCerrar = new javax.swing.JButton();
        jScrollPaneNombre = new javax.swing.JScrollPane();
        jTextPaneNombre = new javax.swing.JTextPane();
        jTextFieldPersonas = new javax.swing.JTextField();
        jScrollPaneDefinicion = new javax.swing.JScrollPane();
        jTextPaneDefinicion = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(MostrarDinamicaContinuaCompartimento.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel.border.titleFont"), resourceMap.getColor("jPanel.border.titleColor"))); // NOI18N
        jPanel.setName("jPanel"); // NOI18N

        jLabelTituloNombre.setText(resourceMap.getString("jLabelTituloNombre.text")); // NOI18N
        jLabelTituloNombre.setName("jLabelTituloNombre"); // NOI18N

        jLabelTituloPersonas.setText(resourceMap.getString("jLabelTituloPersonas.text")); // NOI18N
        jLabelTituloPersonas.setName("jLabelTituloPersonas"); // NOI18N

        jLabelTituloDefinicion.setText(resourceMap.getString("jLabelTituloDefinicion.text")); // NOI18N
        jLabelTituloDefinicion.setName("jLabelTituloDefinicion"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(MostrarDinamicaContinuaCompartimento.class, this);
        jButtonCerrar.setAction(actionMap.get("cerrarVentana")); // NOI18N
        jButtonCerrar.setText(resourceMap.getString("jButtonCerrar.text")); // NOI18N
        jButtonCerrar.setName("jButtonCerrar"); // NOI18N

        jScrollPaneNombre.setName("jScrollPaneNombre"); // NOI18N

        jTextPaneNombre.setEditable(false);
        jTextPaneNombre.setName("jTextPaneNombre"); // NOI18N
        jScrollPaneNombre.setViewportView(jTextPaneNombre);

        jTextFieldPersonas.setBackground(resourceMap.getColor("jTextFieldPersonas.background")); // NOI18N
        jTextFieldPersonas.setEditable(false);
        jTextFieldPersonas.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldPersonas.setName("jTextFieldPersonas"); // NOI18N

        jScrollPaneDefinicion.setName("jScrollPaneDefinicion"); // NOI18N

        jTextPaneDefinicion.setEditable(false);
        jTextPaneDefinicion.setName("jTextPaneDefinicion"); // NOI18N
        jScrollPaneDefinicion.setViewportView(jTextPaneDefinicion);

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanelLayout.createSequentialGroup()
                            .addComponent(jScrollPaneDefinicion, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(jPanelLayout.createSequentialGroup()
                            .addComponent(jScrollPaneNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
                            .addContainerGap())
                        .addComponent(jLabelTituloNombre)
                        .addGroup(jPanelLayout.createSequentialGroup()
                            .addComponent(jLabelTituloPersonas)
                            .addGap(18, 18, 18)
                            .addComponent(jTextFieldPersonas, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(jPanelLayout.createSequentialGroup()
                            .addComponent(jLabelTituloDefinicion)
                            .addContainerGap(427, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                        .addComponent(jButtonCerrar)
                        .addContainerGap())))
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTituloNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloPersonas)
                    .addComponent(jTextFieldPersonas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabelTituloDefinicion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneDefinicion, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonCerrar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Acción a realizar cuando el usuario presiona el botón "Cerrar ventana".
     */
    @Action
    public void cerrarVentana() {
        dispose();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JLabel jLabelTituloDefinicion;
    private javax.swing.JLabel jLabelTituloNombre;
    private javax.swing.JLabel jLabelTituloPersonas;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPaneDefinicion;
    private javax.swing.JScrollPane jScrollPaneNombre;
    private javax.swing.JTextField jTextFieldPersonas;
    private javax.swing.JTextPane jTextPaneDefinicion;
    private javax.swing.JTextPane jTextPaneNombre;
    // End of variables declaration//GEN-END:variables
}
