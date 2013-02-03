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

import delphsim.model.Parametro;

import org.jdesktop.application.Action;

/**
 * Esta ventana muestra la información sobre el valor para simulaciones
 * continuas de un parámetro dado.
 * @author Víctor E. Tamames Gómez
 */
public class MostrarValorContinuaParametro extends javax.swing.JDialog {

    /**
     * Crea una nueva ventana MostrarValorContinuaParametro.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param par El parámetro cuyo contenido debemos mostrar.
     */
    public MostrarValorContinuaParametro(java.awt.Frame parent, boolean modal, Parametro par) {
        super(parent, modal);
        initComponents();
        jTextPaneNombre.setText(par.getNombre());
        jTextPaneNombre.setCaretPosition(0);
        jTextPaneValor.setText(par.getDefinicionContinua());
        jTextPaneValor.setCaretPosition(0);
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
        jScrollPaneNombre = new javax.swing.JScrollPane();
        jTextPaneNombre = new javax.swing.JTextPane();
        jLabelTituloValor = new javax.swing.JLabel();
        jScrollPaneValor = new javax.swing.JScrollPane();
        jTextPaneValor = new javax.swing.JTextPane();
        jButtonCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(MostrarValorContinuaParametro.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel.border.titleFont"), resourceMap.getColor("jPanel.border.titleColor"))); // NOI18N
        jPanel.setName("jPanel"); // NOI18N

        jLabelTituloNombre.setText(resourceMap.getString("jLabelTituloNombre.text")); // NOI18N
        jLabelTituloNombre.setName("jLabelTituloNombre"); // NOI18N

        jScrollPaneNombre.setName("jScrollPaneNombre"); // NOI18N

        jTextPaneNombre.setEditable(false);
        jTextPaneNombre.setName("jTextPaneNombre"); // NOI18N
        jScrollPaneNombre.setViewportView(jTextPaneNombre);

        jLabelTituloValor.setText(resourceMap.getString("jLabelTituloValor.text")); // NOI18N
        jLabelTituloValor.setName("jLabelTituloValor"); // NOI18N

        jScrollPaneValor.setName("jScrollPaneValor"); // NOI18N

        jTextPaneValor.setEditable(false);
        jTextPaneValor.setName("jTextPaneValor"); // NOI18N
        jScrollPaneValor.setViewportView(jTextPaneValor);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(MostrarValorContinuaParametro.class, this);
        jButtonCerrar.setAction(actionMap.get("cerrarVentana")); // NOI18N
        jButtonCerrar.setName("jButtonCerrar"); // NOI18N

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTituloNombre)
                    .addComponent(jScrollPaneNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(jLabelTituloValor)
                    .addComponent(jScrollPaneValor, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                    .addComponent(jButtonCerrar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTituloNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTituloValor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneValor, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButtonCerrar)
                .addContainerGap())
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
                .addComponent(jPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
    private javax.swing.JLabel jLabelTituloNombre;
    private javax.swing.JLabel jLabelTituloValor;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPaneNombre;
    private javax.swing.JScrollPane jScrollPaneValor;
    private javax.swing.JTextPane jTextPaneNombre;
    private javax.swing.JTextPane jTextPaneValor;
    // End of variables declaration//GEN-END:variables
}
