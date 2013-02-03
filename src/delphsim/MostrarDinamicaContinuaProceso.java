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

import delphsim.model.Proceso;

import org.jdesktop.application.Action;

/**
 * Esta ventana muestra la información sobre la dinámica para simulaciones
 * continuas de un proceso dado.
 * @author Víctor E. Tamames Gómez
 */
public class MostrarDinamicaContinuaProceso extends javax.swing.JDialog {

    /**
     * Crea una nueva ventana de MostrarDinamicaContinuaProceso.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param proc El proceso cuyo contenido tenemos que mostrar.
     */
    public MostrarDinamicaContinuaProceso(java.awt.Frame parent, boolean modal, Proceso proc) {
        super(parent, modal);
        initComponents();
        this.jTextPaneNombre.setText(proc.getNombre());
        for (int i = 0; i < proc.getTramosContinua().length; i++) {
            this.anadirPestanaTramoContinua(i, 
                    proc.getTramoContinua(i).getTiempoInicio(),
                    proc.getTramoContinua(i).getDefinicionContinua());
        }
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
        jLabelNombre = new javax.swing.JLabel();
        jButtonCerrar = new javax.swing.JButton();
        jScrollPaneNombre = new javax.swing.JScrollPane();
        jTextPaneNombre = new javax.swing.JTextPane();
        jLabelDefinicion = new javax.swing.JLabel();
        jTabbedPaneTramos = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(MostrarDinamicaContinuaProceso.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        jPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel.border.titleFont"), resourceMap.getColor("jPanel.border.titleColor"))); // NOI18N
        jPanel.setName("jPanel"); // NOI18N

        jLabelNombre.setText(resourceMap.getString("jLabelNombre.text")); // NOI18N
        jLabelNombre.setName("jLabelNombre"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(MostrarDinamicaContinuaProceso.class, this);
        jButtonCerrar.setAction(actionMap.get("cerrarVentana")); // NOI18N
        jButtonCerrar.setName("jButtonCerrar"); // NOI18N

        jScrollPaneNombre.setName("jScrollPaneNombre"); // NOI18N

        jTextPaneNombre.setEditable(false);
        jTextPaneNombre.setName("jTextPaneNombre"); // NOI18N
        jScrollPaneNombre.setViewportView(jTextPaneNombre);

        jLabelDefinicion.setText(resourceMap.getString("jLabelDefinicion.text")); // NOI18N
        jLabelDefinicion.setName("jLabelDefinicion"); // NOI18N

        jTabbedPaneTramos.setName("jTabbedPaneTramos"); // NOI18N

        javax.swing.GroupLayout jPanelLayout = new javax.swing.GroupLayout(jPanel);
        jPanel.setLayout(jPanelLayout);
        jPanelLayout.setHorizontalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPaneTramos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                    .addComponent(jLabelNombre, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 469, Short.MAX_VALUE)
                    .addComponent(jLabelDefinicion, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCerrar))
                .addContainerGap())
        );
        jPanelLayout.setVerticalGroup(
            jPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelNombre)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelDefinicion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPaneTramos, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jButtonCerrar)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
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
    
    /**
     * Método que añade una nueva pestaña al panel que contiene los tramos del
     * proceso con la información sobre dicho tramo.
     * @param pos La posición del nuevo tramo.
     * @param t El tiempo de inicio del tramo.
     * @param d La definición del tramo.
     */
    private void anadirPestanaTramoContinua(int pos, int t, String d) {
	javax.swing.JPanel jPanelTramo = new javax.swing.JPanel();
        javax.swing.JLabel jLabelTiempo = new javax.swing.JLabel();
        javax.swing.JTextField jTextFieldTiempo = new javax.swing.JTextField();
        javax.swing.JLabel jLabelDefinicionTramo = new javax.swing.JLabel();
        javax.swing.JScrollPane jScrollPaneDefinicion = new javax.swing.JScrollPane();
        javax.swing.JTextPane jTextPaneDefinicion = new javax.swing.JTextPane();

        jPanelTramo.setName("jPanelTramo"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(MostrarDinamicaContinuaProceso.class);
        jLabelTiempo.setText(resourceMap.getString("jLabelTiempo.text")); // NOI18N
        jLabelTiempo.setName("jLabelTiempo"); // NOI18N

        jTextFieldTiempo.setBackground(resourceMap.getColor("jTextFieldTiempo.background")); // NOI18N
        jTextFieldTiempo.setEditable(false);
        jTextFieldTiempo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldTiempo.setName("jTextFieldTiempo"); // NOI18N

        jLabelDefinicionTramo.setText(resourceMap.getString("jLabelDefinicionTramo.text")); // NOI18N
        jLabelDefinicionTramo.setName("jLabelDefinicionTramo"); // NOI18N

        jScrollPaneDefinicion.setName("jScrollPaneDefinicion"); // NOI18N

        jTextPaneDefinicion.setEditable(false);
        jTextPaneDefinicion.setName("jTextPaneDefinicion"); // NOI18N
        jScrollPaneDefinicion.setViewportView(jTextPaneDefinicion);

        javax.swing.GroupLayout jPanelTramoLayout = new javax.swing.GroupLayout(jPanelTramo);
        jPanelTramo.setLayout(jPanelTramoLayout);
        jPanelTramoLayout.setHorizontalGroup(
            jPanelTramoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTramoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTramoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPaneDefinicion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelTramoLayout.createSequentialGroup()
                        .addComponent(jLabelTiempo)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldTiempo, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE))
                    .addComponent(jLabelDefinicionTramo, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanelTramoLayout.setVerticalGroup(
            jPanelTramoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTramoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelTramoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTiempo)
                    .addComponent(jTextFieldTiempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabelDefinicionTramo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPaneDefinicion, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                .addContainerGap())
        );
        
        jTextFieldTiempo.setText(String.valueOf(t));
        jTextPaneDefinicion.setText(d);
        jTextPaneDefinicion.setCaretPosition(0);
        this.jTabbedPaneTramos.add(resourceMap.getString("jTabbed.tab.name", pos+1), jPanelTramo); // NOI18N
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCerrar;
    private javax.swing.JLabel jLabelDefinicion;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JPanel jPanel;
    private javax.swing.JScrollPane jScrollPaneNombre;
    private javax.swing.JTabbedPane jTabbedPaneTramos;
    private javax.swing.JTextPane jTextPaneNombre;
    // End of variables declaration//GEN-END:variables

}
