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
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.application.Action;

/**
 * Ventana con información sobre el sistema, en concreto: información sobre
 * DelphSim, licencia de DelphSim, información sobre las librerías utilizadas y
 * variables de entorno del sistema.
 * @author Víctor E. Tamames Gómez
 */
public class DelphSimAboutBox extends javax.swing.JDialog {
    
    /**
     * Constructor de la clase. Inicia los componentes de la interfaz y
     * lo muestra.
     * @param parent Elemento de la interfaz que será el ancestro de esta clase.
     */
    public DelphSimAboutBox(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DelphSimAboutBox.class);

        // Cargar el texto de licencia en la segunda pestaña
        try {
            String rutaLicencia = new File(System.getProperty("java.class.path")).getParent() + resourceMap.getString("license.path"); // NOI18N
            URL u = new URL("file", "localhost", rutaLicencia); // NOI18N
            this.licenseTextPane.setPage(u);
        } catch (IOException ex) {
            Logger.getLogger(DelphSimAboutBox.class.getName()).log(Level.SEVERE, null, ex);
            this.licenseTextPane.setText(resourceMap.getString("license.error")); // NOI18N
        }
        
        // Redimensionar columnas de la tercera pestaña (librerías)
        this.librariesTable.getColumnModel().getColumn(0).setMinWidth(150);
        this.librariesTable.getColumnModel().getColumn(0).setMaxWidth(150);
        this.librariesTable.getColumnModel().getColumn(1).setMinWidth(50);
        this.librariesTable.getColumnModel().getColumn(1).setMaxWidth(50);
        this.librariesTable.getColumnModel().getColumn(3).setMinWidth(200);
        this.librariesTable.getColumnModel().getColumn(3).setMaxWidth(200);
        
        // Cargar las variables de entorno en la tabla de la cuarta pestaña
        Set propset = System.getProperties().stringPropertyNames();
        for (Iterator iter = propset.iterator(); iter.hasNext();) {
            String[] fila = new String[2];
            fila[0] = (String) iter.next();
            fila[1] = System.getProperty(fila[0]);
            ((DefaultTableModel) systemTable.getModel()).addRow(fila);
        }
        
        this.librariesTable.getTableHeader().setReorderingAllowed(false);
        this.systemTable.getTableHeader().setReorderingAllowed(false);
        
        // Centrar y mostrar
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Cierra la ventana.
     */
    @Action public void closeAboutBox() {
        dispose();
    }
    
    /** 
     * Este método se llama desde el constructor para iniciar los componentes.
     * ADVERTENCIA: NO modificar este código. El contenido de este método es
     * generado siempre por el Editor de Formularios de NetBeans 6.1.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        aboutTabbedPane = new javax.swing.JTabbedPane();
        applicationPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel orgLabel = new javax.swing.JLabel();
        javax.swing.JLabel appOrgLabel = new javax.swing.JLabel();
        javax.swing.JLabel clientLabel = new javax.swing.JLabel();
        javax.swing.JLabel appClientLabel = new javax.swing.JLabel();
        javax.swing.JLabel homepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel appHomepageLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLogoLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageUVaLabel = new javax.swing.JLabel();
        licensePanel = new javax.swing.JPanel();
        licenseScrollPane = new javax.swing.JScrollPane();
        licenseTextPane = new javax.swing.JTextPane();
        librariesPanel = new javax.swing.JPanel();
        librariesScrollPane = new javax.swing.JScrollPane();
        librariesTable = new javax.swing.JTable();
        systemPanel = new javax.swing.JPanel();
        systemScrollPane = new javax.swing.JScrollPane();
        systemTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DelphSimAboutBox.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setModal(true);
        setName("aboutBox"); // NOI18N

        aboutTabbedPane.setName("aboutTabbedPane"); // NOI18N

        applicationPanel.setName("applicationPanel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(DelphSimAboutBox.class, this);
        closeButton.setAction(actionMap.get("closeAboutBox")); // NOI18N

        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+4));
        appTitleLabel.setText(resourceMap.getString("appNameLabel.title")); // NOI18N

        appDescLabel.setText(resourceMap.getString("appDescLabel.text")); // NOI18N

        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel.setText(resourceMap.getString("versionLabel.text")); // NOI18N

        appVersionLabel.setText(resourceMap.getString("Application.version")); // NOI18N

        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text")); // NOI18N

        appVendorLabel.setText(resourceMap.getString("Application.vendor")); // NOI18N

        orgLabel.setFont(orgLabel.getFont().deriveFont(orgLabel.getFont().getStyle() | java.awt.Font.BOLD));
        orgLabel.setText(resourceMap.getString("orgLabel.text")); // NOI18N
        orgLabel.setName("orgLabel"); // NOI18N

        appOrgLabel.setText(resourceMap.getString("appOrgLabel.text")); // NOI18N
        appOrgLabel.setName("appOrgLabel"); // NOI18N

        clientLabel.setFont(clientLabel.getFont().deriveFont(clientLabel.getFont().getStyle() | java.awt.Font.BOLD));
        clientLabel.setText(resourceMap.getString("clientLabel.text")); // NOI18N
        clientLabel.setName("clientLabel"); // NOI18N

        appClientLabel.setText(resourceMap.getString("appClientLabel.text")); // NOI18N
        appClientLabel.setName("appClientLabel"); // NOI18N

        homepageLabel.setFont(homepageLabel.getFont().deriveFont(homepageLabel.getFont().getStyle() | java.awt.Font.BOLD));
        homepageLabel.setText(resourceMap.getString("homepageLabel.text")); // NOI18N

        appHomepageLabel.setText(resourceMap.getString("Application.homepage")); // NOI18N

        imageLogoLabel.setIcon(resourceMap.getIcon("imageLogoLabel.icon")); // NOI18N
        imageLogoLabel.setName("imageLogoLabel"); // NOI18N

        imageUVaLabel.setIcon(resourceMap.getIcon("imageUVaLabel.icon")); // NOI18N
        imageUVaLabel.setName("imageUVaLabel"); // NOI18N

        javax.swing.GroupLayout applicationPanelLayout = new javax.swing.GroupLayout(applicationPanel);
        applicationPanel.setLayout(applicationPanelLayout);
        applicationPanelLayout.setHorizontalGroup(
            applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 573, Short.MAX_VALUE)
            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(applicationPanelLayout.createSequentialGroup()
                    .addGap(7, 7, 7)
                    .addComponent(imageUVaLabel)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(appTitleLabel)
                        .addGroup(applicationPanelLayout.createSequentialGroup()
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(versionLabel)
                                .addComponent(vendorLabel)
                                .addComponent(orgLabel)
                                .addComponent(clientLabel)
                                .addComponent(homepageLabel))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(appClientLabel)
                                .addComponent(appHomepageLabel)
                                .addComponent(appOrgLabel)
                                .addComponent(appVersionLabel)
                                .addComponent(appVendorLabel)))
                        .addComponent(appDescLabel))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(imageLogoLabel)
                        .addComponent(closeButton))
                    .addGap(8, 8, 8)))
        );
        applicationPanelLayout.setVerticalGroup(
            applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 184, Short.MAX_VALUE)
            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(applicationPanelLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(applicationPanelLayout.createSequentialGroup()
                            .addComponent(appTitleLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(appDescLabel)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(versionLabel)
                                .addComponent(appVersionLabel))
                            .addGap(6, 6, 6)
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(vendorLabel)
                                .addComponent(appVendorLabel))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(orgLabel)
                                .addComponent(appOrgLabel))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(clientLabel)
                                .addComponent(appClientLabel))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(homepageLabel)
                                .addComponent(appHomepageLabel)))
                        .addGroup(applicationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, applicationPanelLayout.createSequentialGroup()
                                .addComponent(imageLogoLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(closeButton))
                            .addComponent(imageUVaLabel, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        aboutTabbedPane.addTab(resourceMap.getString("applicationPanel.TabConstraints.tabTitle"), applicationPanel); // NOI18N

        licensePanel.setName("licensePanel"); // NOI18N

        licenseScrollPane.setName("licenseScrollPane"); // NOI18N

        licenseTextPane.setEditable(false);
        licenseTextPane.setName("licenseTextPane"); // NOI18N
        licenseScrollPane.setViewportView(licenseTextPane);

        javax.swing.GroupLayout licensePanelLayout = new javax.swing.GroupLayout(licensePanel);
        licensePanel.setLayout(licensePanelLayout);
        licensePanelLayout.setHorizontalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(licenseScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        licensePanelLayout.setVerticalGroup(
            licensePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(licenseScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(resourceMap.getString("licensePanel.TabConstraints.tabTitle"), licensePanel); // NOI18N

        librariesPanel.setName("librariesPanel"); // NOI18N

        librariesScrollPane.setName("librariesScrollPane"); // NOI18N

        librariesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"Absolute Layout", "", "AbsoluteLayout.jar;", "CDDL & GNU General Public License 2.0"},
                {"Batik", "1.7", "batik-awt-util.jar; batik-dom.jar; batik-ext.jar; batik-svggen.jar; batik-util.jar; batik-xml.jar;", "Apache License 2.0"},
                {"Beans Binding", "1.2.1", "beansbinding-1.2.1.jar;", "CDDL & GNU General Public License 2.0"},
                {"COLT", "1.2.0", "colt.jar", "Open Source"},
                {"DOM4J", "1.6.1", "dom4j-1.6.1.jar;", "BSD Style License"},
                {"Java Expression Parser", "2.4.1", "jep-2.4.1.jar;", "GNU General Public License 2.0"},
                {"JFreeChart", "1.0.10", "jfreechart-1.0.10.jar; jcommon-1.0.13.jar;", "GNU Lesser General Public License 3.0"},
                {"Swing Application Framework", "1.0.3", "appframework-1.0.3.jar; swing-worker-1.1.jar;", "CDDL & GNU General Public License 2.0"},
                {"Swing Layout Extensions", "1.0.3", "swing-layout-1.0.3.jar;", "CDDL & GNU General Public License 2.0"}
            },
            new String [] {
                "Librería", "Versión", "Archivos JAR", "Licencia"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        librariesTable.setName("librariesTable"); // NOI18N
        librariesScrollPane.setViewportView(librariesTable);

        javax.swing.GroupLayout librariesPanelLayout = new javax.swing.GroupLayout(librariesPanel);
        librariesPanel.setLayout(librariesPanelLayout);
        librariesPanelLayout.setHorizontalGroup(
            librariesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(librariesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        librariesPanelLayout.setVerticalGroup(
            librariesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(librariesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(resourceMap.getString("librariesPanel.TabConstraints.tabTitle"), librariesPanel); // NOI18N

        systemPanel.setName("systemPanel"); // NOI18N

        systemScrollPane.setName("systemScrollPane"); // NOI18N

        systemTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Variable", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        systemTable.setName("systemTable"); // NOI18N
        systemScrollPane.setViewportView(systemTable);

        javax.swing.GroupLayout systemPanelLayout = new javax.swing.GroupLayout(systemPanel);
        systemPanel.setLayout(systemPanelLayout);
        systemPanelLayout.setHorizontalGroup(
            systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(systemScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
        );
        systemPanelLayout.setVerticalGroup(
            systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(systemScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
        );

        aboutTabbedPane.addTab(resourceMap.getString("systemPanel.TabConstraints.tabTitle"), systemPanel); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aboutTabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(aboutTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane aboutTabbedPane;
    private javax.swing.JPanel applicationPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel librariesPanel;
    private javax.swing.JScrollPane librariesScrollPane;
    private javax.swing.JTable librariesTable;
    private javax.swing.JPanel licensePanel;
    private javax.swing.JScrollPane licenseScrollPane;
    private javax.swing.JTextPane licenseTextPane;
    private javax.swing.JPanel systemPanel;
    private javax.swing.JScrollPane systemScrollPane;
    private javax.swing.JTable systemTable;
    // End of variables declaration//GEN-END:variables
    
}
