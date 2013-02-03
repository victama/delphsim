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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.DefaultComboBoxModel;

import org.jdesktop.application.Action;

/**
 * Ventana de preferencias de la simulación, donde se puede escoger el método
 * de simulación entre otras cosas.
 * @author Víctor E. Tamames Gómez
 */
public class PreferenciasSimulacion extends javax.swing.JDialog {
    
    /**
     * Las preferencias para el nodo identificado por el paquete de esta clase,
     * <CODE>delphsim</CODE>.
     */
    public static Preferences preferencias = Preferences.userNodeForPackage(PreferenciasSimulacion.class);
    
    /**
     * Método que se va a utilizar por defecto.
     */
    public static String metodoPorDefecto = "1"; // NOI18N
    
    /**
     * Intervalo de comunicación que se va a utilizar por defecto.
     */
    public static String hPorDefecto = "0.1"; // NOI18N
    
    /**
     * Opción de autoguardado por defecto.
     */
    public static String autosavePorDefecto = "si"; // NOI18N
    
    /**
     * Crea una nueva ventana de preferencias de la simulación, iniciando los
     * componentes y cargando las preferencias actuales (si las hubiera, los
     * valores por defecto en caso contrario).
     * @param parent El componente que lo crea.
     * @param modal Si se pueden usar el resto de ventanas o no.
     */
    public PreferenciasSimulacion(java.awt.Frame parent, boolean modal) {
        // Iniciamos los componentes
        super(parent, modal);
        initComponents();
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(PreferenciasSimulacion.class);
        String[] metodos = {resourceMap.getString("metodos.nombre1"), // NOI18N
                            resourceMap.getString("metodos.nombre2"), // NOI18N
                            resourceMap.getString("metodos.nombre3"), // NOI18N
                            resourceMap.getString("metodos.nombre4"), // NOI18N
                            resourceMap.getString("metodos.nombre5")}; // NOI18N
        this.metodoComboBox.setModel(new DefaultComboBoxModel(metodos));
        this.hSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.01d, 10.0d, 0.1d));

        // Cargamos o creamos el archivo de preferencias
        try {
            String rutaArchivo = new File(System.getProperty("java.class.path")).getParent() + resourceMap.getString("archivoPreferencias.path"); // NOI18N
            File f = new File(rutaArchivo);
            if (f.exists()) {
                FileInputStream is = new FileInputStream(f);
                Preferences.importPreferences(is);
            } else {
                PreferenciasSimulacion.preferencias.put("metodo", PreferenciasSimulacion.metodoPorDefecto); // NOI18N
                PreferenciasSimulacion.preferencias.put("h", PreferenciasSimulacion.hPorDefecto); // NOI18N
                PreferenciasSimulacion.preferencias.put("autosave", PreferenciasSimulacion.autosavePorDefecto); // NOI18N
            }
            FileOutputStream os = new FileOutputStream(f);
            PreferenciasSimulacion.preferencias.exportSubtree(os);
        } catch (Exception e) {
            PreferenciasSimulacion.preferencias.put("metodo", PreferenciasSimulacion.metodoPorDefecto); // NOI18N
            PreferenciasSimulacion.preferencias.put("h", PreferenciasSimulacion.hPorDefecto); // NOI18N
            PreferenciasSimulacion.preferencias.put("autosave", PreferenciasSimulacion.autosavePorDefecto); // NOI18N
            System.err.println("Error: producido en PreferenciasSimulacion, en la creación, al intentar cargar/crear las preferencias. Mensaje: " + e.getMessage()); // NOI18N
        }
        
        // Iniciamos los campos con las preferencias actuales
        this.metodoComboBox.setSelectedIndex(
                Integer.valueOf(PreferenciasSimulacion.preferencias
                .get("metodo", PreferenciasSimulacion.metodoPorDefecto))); // NOI18N
        this.hSpinner.setValue(
                Double.valueOf(PreferenciasSimulacion.preferencias
                .get("h", PreferenciasSimulacion.hPorDefecto))); // NOI18N
        if (PreferenciasSimulacion.preferencias.get("autosave",  // NOI18N
                PreferenciasSimulacion.autosavePorDefecto).equals("si")) { // NOI18N
            this.autosaveCheckBox.setSelected(true);
        } else {
            this.autosaveCheckBox.setSelected(false);
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
        metodoLabel = new javax.swing.JLabel();
        metodoComboBox = new javax.swing.JComboBox();
        hLabel = new javax.swing.JLabel();
        hSpinner = new javax.swing.JSpinner();
        autosaveCheckBox = new javax.swing.JCheckBox();
        botonesSeparator = new javax.swing.JSeparator();
        aceptarButton = new javax.swing.JButton();
        cancelarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(PreferenciasSimulacion.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(489, 319));
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

        metodoLabel.setText(resourceMap.getString("metodoLabel.text")); // NOI18N
        metodoLabel.setName("metodoLabel"); // NOI18N

        metodoComboBox.setBackground(resourceMap.getColor("metodoComboBox.background")); // NOI18N
        metodoComboBox.setFont(resourceMap.getFont("metodoComboBox.font")); // NOI18N
        metodoComboBox.setName("metodoComboBox"); // NOI18N

        hLabel.setText(resourceMap.getString("hLabel.text")); // NOI18N
        hLabel.setName("hLabel"); // NOI18N

        hSpinner.setBackground(resourceMap.getColor("hSpinner.background")); // NOI18N
        hSpinner.setFont(resourceMap.getFont("hSpinner.font")); // NOI18N
        hSpinner.setForeground(resourceMap.getColor("hSpinner.foreground")); // NOI18N
        hSpinner.setName("hSpinner"); // NOI18N

        autosaveCheckBox.setSelected(true);
        autosaveCheckBox.setText(resourceMap.getString("autosaveCheckBox.text")); // NOI18N
        autosaveCheckBox.setName("autosaveCheckBox"); // NOI18N

        botonesSeparator.setName("botonesSeparator"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(PreferenciasSimulacion.class, this);
        aceptarButton.setAction(actionMap.get("aceptar")); // NOI18N
        aceptarButton.setName("aceptarButton"); // NOI18N

        cancelarButton.setAction(actionMap.get("cancelar")); // NOI18N
        cancelarButton.setName("cancelarButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(autosaveCheckBox)
                    .addComponent(tituloSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addComponent(tituloLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(aceptarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelarButton))
                    .addComponent(botonesSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addComponent(expTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 568, Short.MAX_VALUE)
                    .addComponent(metodoLabel)
                    .addComponent(metodoComboBox, 0, 568, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(hSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addComponent(metodoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(metodoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hLabel)
                    .addComponent(hSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(autosaveCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(botonesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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
     * Acción a realizar cuando el usuario pulsa el botón "Cancelar". Cierra
     * la ventana sin aplicar ningún cambio.
     */
    @Action
    public void cancelar() {
        dispose();
    }
    
    /**
     * Acción a realizar cuando el usuario pulsa el botón "Aceptar". Cierra
     * la ventana tras aplicar los cambios realizados.
     */
    @Action
    public void aceptar() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(PreferenciasSimulacion.class);
        try {
            // Actualizamos las preferencias
            PreferenciasSimulacion.preferencias.put("metodo", Integer.toString(this.metodoComboBox.getSelectedIndex())); // NOI18N
            PreferenciasSimulacion.preferencias.put("h", Double.toString((Double) this.hSpinner.getValue())); // NOI18N
            if (this.autosaveCheckBox.isSelected()) {
                PreferenciasSimulacion.preferencias.put("autosave", "si"); // NOI18N
            } else {
                PreferenciasSimulacion.preferencias.put("autosave", "no"); // NOI18N
            }
            // Las exportamos al fichero
            String rutaArchivo = new File(System.getProperty("java.class.path")).getParent() + resourceMap.getString("archivoPreferencias.path"); // NOI18N
            File f = new File(rutaArchivo);
            FileOutputStream os = new FileOutputStream(f);
            PreferenciasSimulacion.preferencias.exportSubtree(os);
            // Y cerramos la ventana
            dispose();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (BackingStoreException bse) {
            System.err.println(bse.getMessage());
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarButton;
    private javax.swing.JCheckBox autosaveCheckBox;
    private javax.swing.JSeparator botonesSeparator;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JTextPane expTextPane;
    private javax.swing.JLabel hLabel;
    private javax.swing.JSpinner hSpinner;
    private javax.swing.JComboBox metodoComboBox;
    private javax.swing.JLabel metodoLabel;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JSeparator tituloSeparator;
    // End of variables declaration//GEN-END:variables
}
