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

import java.awt.CardLayout;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.application.Action;
import org.nfunk.jep.*;

/**
 * Interfaz de usuario creada a través de la ventana principal de la aplicación
 * que permite definir la dinámica de los compartimentos de la población mediante
 * un asistente.
 * @author Víctor E. Tamames Gómez
 */
public class DinamicaPoblacion extends javax.swing.JDialog {

    /**
     * Indica el paso del asistente donde estamos actualmente.
     */
    private int pantallaActual = 1;

    /**
     * Lista de divisiones introducidas que se muestra en el paso 3 para escoger
     * cuál mostrar en las filas de las tablas del paso 4.
     */
    private DefaultListModel listaFilasPaso3 = new DefaultListModel();

    /**
     * Lista de divisiones introducidas que se muestra en el paso 3 para escoger
     * cuál mostrar en las columnas de las tablas del paso 4.
     */
    private DefaultListModel listaColumnasPaso3 = new DefaultListModel();

    /**
     * Indica la tabla del paso 4 que estamos mostrando.
     */
    private int tablaPaso4Actual = 0;

    /**
     * Indica el número total de tablas que hay en el paso 4.
     */
    private int maxIndexTablasPaso4 = 1;

    /**
     * Conjunto de modelos de tabla para la tabla del paso 4 que representan la
     * combinación de categorías para formar compartimentos.
     */
    private DelphSimTableModel[] tablasPaso4;

    /**
     * Conjunto de etiquetas del paso 4 para identificar cada una de las tablas.
     */
    private String[] tituloTablasPaso4;

    /**
     * Conjunto de tooltips del paso 4 para los botones que permiten cambiar de
     * una tabla a otra. Son descripciones cortas de la tabla anterior/siguiente
     * en lenguaje HTML.
     */
    private String[] tooltipsTablasPaso4;
    
    /**
     * La epidemia cuya población vamos a modelar.
     */
    private Epidemia epidemia;
    
    /**
     * Crea una nueva ventana de DinamicaPoblacion.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia con la que vamos a tratar.
     */
    public DinamicaPoblacion(java.awt.Frame parent, boolean modal, Epidemia epi) {
        super(parent, modal);
        initComponents();
        this.epidemia = epi;
        
        // Algunos detalles sobre los componentes iniciales
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaPoblacion.class);
        this.backButton.setEnabled(false);
        this.paso2Table.getColumnModel().getColumn(0).setPreferredWidth(100);
        this.paso2Table.getColumnModel().getColumn(1).setPreferredWidth(this.paso2Table.getWidth() - 100);
        // Si sólo tenemos una división, el comportamiento del asistente es notablemente distinto
        if (this.epidemia.getPoblacion().getDivisiones().length == 1) {
            // Mostrar la segunda "card", título y botón acordes a que es un sólo paso
            this.pantallaActual++;
            ((CardLayout) (this.mainPanel.getLayout())).show(this.mainPanel, "2"); // NOI18N
            this.titulo2Label.setText(resourceMap.getString("titulo2Label.textAlternativo")); // NOI18N
            this.exp2TextPane.setText(resourceMap.getString("exp2TextPane.textAlternativo")); // NOI18N
            this.nextButton.setText(resourceMap.getString("botonSiguiente.textAlternativo")); // NOI18N
            this.divPrincipalComboBox.addItem(this.epidemia.getPoblacion().getDivisiones()[0].getNombre());
            // Tabla del paso 2
            ((DefaultTableModel)this.paso2Table.getModel()).setRowCount(this.epidemia.getCompartimentos().length);
            for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                this.paso2Table.setValueAt(this.epidemia.getCompartimento(i).getNombre(), i, 0);
                this.paso2Table.setValueAt(this.epidemia.getCompartimento(i).getDefinicionContinua(), i, 1);
            }
        } else {
            // ComboBox del paso 1 y listas del paso 3
            for (int i = 0; i < this.epidemia.getPoblacion().getDivisiones().length; i++) {
                this.divPrincipalComboBox.addItem(this.epidemia.getPoblacion().getDivisiones()[i].getNombre());
                this.listaFilasPaso3.addElement(this.epidemia.getPoblacion().getDivisiones()[i].getNombre());
                this.listaColumnasPaso3.addElement(this.epidemia.getPoblacion().getDivisiones()[i].getNombre());
            }
            this.divFilasList.setSelectedIndex(0);
            this.divColumList.setSelectedIndex(1);
        }
        
        this.paso2Table.getTableHeader().setReorderingAllowed(false);
        this.compartimentosTable.getTableHeader().setReorderingAllowed(false);
        
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

        modoButtonGroup = new javax.swing.ButtonGroup();
        mainPanel = new javax.swing.JPanel();
        escogerModoPanel = new javax.swing.JPanel();
        titulo1Label = new javax.swing.JLabel();
        titulo1Separator = new javax.swing.JSeparator();
        escogerModoLabel = new javax.swing.JLabel();
        modo1RadioButton = new javax.swing.JRadioButton();
        modo1expTextPane = new javax.swing.JTextPane();
        divPrincipalLabel = new javax.swing.JLabel();
        divPrincipalComboBox = new javax.swing.JComboBox();
        modo2RadioButton = new javax.swing.JRadioButton();
        modo2expTextPane2 = new javax.swing.JTextPane();
        divPrincipalPanel = new javax.swing.JPanel();
        titulo2Label = new javax.swing.JLabel();
        titulo2Separator = new javax.swing.JSeparator();
        exp2TextPane = new javax.swing.JTextPane();
        paso2TableSP = new javax.swing.JScrollPane();
        paso2Table = new javax.swing.JTable();
        fijarDivsPanel = new javax.swing.JPanel();
        titulo3Label = new javax.swing.JLabel();
        titulo3Separator = new javax.swing.JSeparator();
        exp3TextPane = new javax.swing.JTextPane();
        divFilasLabel = new javax.swing.JLabel();
        divColumLabel = new javax.swing.JLabel();
        divFilasScrollPane = new javax.swing.JScrollPane();
        divFilasList = new javax.swing.JList();
        divColumScrollPane = new javax.swing.JScrollPane();
        divColumList = new javax.swing.JList();
        compIndividualPanel = new javax.swing.JPanel();
        titulo4Label = new javax.swing.JLabel();
        titulo4Separator = new javax.swing.JSeparator();
        exp4TextPane = new javax.swing.JTextPane();
        compartimentosLabel = new javax.swing.JLabel();
        compartimentosScrollPane = new javax.swing.JScrollPane();
        compartimentosTable = new javax.swing.JTable();
        derechaButton = new javax.swing.JButton();
        izquierdaButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        buttonSeparator = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaPoblacion.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.CardLayout());

        escogerModoPanel.setName("escogerModoPanel"); // NOI18N

        titulo1Label.setFont(resourceMap.getFont("titulo1Label.font")); // NOI18N
        titulo1Label.setText(resourceMap.getString("titulo1Label.text")); // NOI18N
        titulo1Label.setName("titulo1Label"); // NOI18N

        titulo1Separator.setName("titulo1Separator"); // NOI18N

        escogerModoLabel.setFont(resourceMap.getFont("escogerModoLabel.font")); // NOI18N
        escogerModoLabel.setText(resourceMap.getString("escogerModoLabel.text")); // NOI18N
        escogerModoLabel.setName("escogerModoLabel"); // NOI18N

        modoButtonGroup.add(modo1RadioButton);
        modo1RadioButton.setSelected(true);
        modo1RadioButton.setText(resourceMap.getString("modo1RadioButton.text")); // NOI18N
        modo1RadioButton.setName("modo1RadioButton"); // NOI18N

        modo1expTextPane.setBackground(this.getBackground());
        modo1expTextPane.setBorder(null);
        modo1expTextPane.setEditable(false);
        modo1expTextPane.setText(resourceMap.getString("modo1expTextPane.text")); // NOI18N
        modo1expTextPane.setName("modo1expTextPane"); // NOI18N

        divPrincipalLabel.setText(resourceMap.getString("divPrincipalLabel.text")); // NOI18N
        divPrincipalLabel.setName("divPrincipalLabel"); // NOI18N

        divPrincipalComboBox.setBackground(resourceMap.getColor("divPrincipalComboBox.background")); // NOI18N
        divPrincipalComboBox.setFont(resourceMap.getFont("divPrincipalComboBox.font")); // NOI18N
        divPrincipalComboBox.setName("divPrincipalComboBox"); // NOI18N

        modoButtonGroup.add(modo2RadioButton);
        modo2RadioButton.setText(resourceMap.getString("modo2RadioButton.text")); // NOI18N
        modo2RadioButton.setName("modo2RadioButton"); // NOI18N

        modo2expTextPane2.setBackground(this.getBackground());
        modo2expTextPane2.setBorder(null);
        modo2expTextPane2.setEditable(false);
        modo2expTextPane2.setText(resourceMap.getString("modo2expTextPane2.text")); // NOI18N
        modo2expTextPane2.setName("modo2expTextPane2"); // NOI18N

        javax.swing.GroupLayout escogerModoPanelLayout = new javax.swing.GroupLayout(escogerModoPanel);
        escogerModoPanel.setLayout(escogerModoPanelLayout);
        escogerModoPanelLayout.setHorizontalGroup(
            escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(escogerModoPanelLayout.createSequentialGroup()
                .addGroup(escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(escogerModoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(titulo1Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                            .addComponent(titulo1Label)
                            .addComponent(escogerModoLabel)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, escogerModoPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(modo1RadioButton)
                            .addGroup(escogerModoPanelLayout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addGroup(escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(escogerModoPanelLayout.createSequentialGroup()
                                        .addComponent(divPrincipalLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(divPrincipalComboBox, 0, 357, Short.MAX_VALUE))
                                    .addComponent(modo1expTextPane, 0, 0, Short.MAX_VALUE)))
                            .addComponent(modo2RadioButton)))
                    .addGroup(escogerModoPanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(modo2expTextPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)))
                .addContainerGap())
        );
        escogerModoPanelLayout.setVerticalGroup(
            escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(escogerModoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo1Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo1Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(escogerModoLabel)
                .addGap(18, 18, 18)
                .addComponent(modo1RadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modo1expTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(escogerModoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(divPrincipalLabel)
                    .addComponent(divPrincipalComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(modo2RadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(modo2expTextPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        mainPanel.add(escogerModoPanel, "1");

        divPrincipalPanel.setName("divPrincipalPanel"); // NOI18N

        titulo2Label.setFont(resourceMap.getFont("titulo2Label.font")); // NOI18N
        titulo2Label.setText(resourceMap.getString("titulo2Label.text")); // NOI18N
        titulo2Label.setName("titulo2Label"); // NOI18N

        titulo2Separator.setName("titulo2Separator"); // NOI18N

        exp2TextPane.setBackground(this.getBackground());
        exp2TextPane.setBorder(null);
        exp2TextPane.setEditable(false);
        exp2TextPane.setText(resourceMap.getString("exp2TextPane.text")); // NOI18N
        exp2TextPane.setName("exp2TextPane"); // NOI18N

        paso2TableSP.setName("paso2TableSP"); // NOI18N

        paso2Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null}
            },
            new String [] {
                "Categoría", "Definición"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paso2Table.setName("paso2Table"); // NOI18N
        paso2TableSP.setViewportView(paso2Table);

        javax.swing.GroupLayout divPrincipalPanelLayout = new javax.swing.GroupLayout(divPrincipalPanel);
        divPrincipalPanel.setLayout(divPrincipalPanelLayout);
        divPrincipalPanelLayout.setHorizontalGroup(
            divPrincipalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, divPrincipalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(divPrincipalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(paso2TableSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addComponent(titulo2Separator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addComponent(titulo2Label, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(exp2TextPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE))
                .addContainerGap())
        );
        divPrincipalPanelLayout.setVerticalGroup(
            divPrincipalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(divPrincipalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo2Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(exp2TextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(paso2TableSP, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(divPrincipalPanel, "2");

        fijarDivsPanel.setName("fijarDivsPanel"); // NOI18N

        titulo3Label.setFont(resourceMap.getFont("titulo3Label.font")); // NOI18N
        titulo3Label.setText(resourceMap.getString("titulo3Label.text")); // NOI18N
        titulo3Label.setName("titulo3Label"); // NOI18N

        titulo3Separator.setName("titulo3Separator"); // NOI18N

        exp3TextPane.setBackground(this.getBackground());
        exp3TextPane.setBorder(null);
        exp3TextPane.setEditable(false);
        exp3TextPane.setText(resourceMap.getString("exp3TextPane.text")); // NOI18N
        exp3TextPane.setName("exp3TextPane"); // NOI18N

        divFilasLabel.setText(resourceMap.getString("divFilasLabel.text")); // NOI18N
        divFilasLabel.setName("divFilasLabel"); // NOI18N

        divColumLabel.setText(resourceMap.getString("divColumLabel.text")); // NOI18N
        divColumLabel.setName("divColumLabel"); // NOI18N

        divFilasScrollPane.setName("divFilasScrollPane"); // NOI18N

        divFilasList.setModel(this.listaFilasPaso3);
        divFilasList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        divFilasList.setName("divFilasList"); // NOI18N
        divFilasScrollPane.setViewportView(divFilasList);

        divColumScrollPane.setName("divColumScrollPane"); // NOI18N

        divColumList.setModel(this.listaColumnasPaso3);
        divColumList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        divColumList.setName("divColumList"); // NOI18N
        divColumScrollPane.setViewportView(divColumList);

        javax.swing.GroupLayout fijarDivsPanelLayout = new javax.swing.GroupLayout(fijarDivsPanel);
        fijarDivsPanel.setLayout(fijarDivsPanelLayout);
        fijarDivsPanelLayout.setHorizontalGroup(
            fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fijarDivsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titulo3Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
                    .addComponent(titulo3Label)
                    .addGroup(fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(exp3TextPane, javax.swing.GroupLayout.Alignment.LEADING, 0, 0, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, fijarDivsPanelLayout.createSequentialGroup()
                            .addGroup(fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(divFilasLabel)
                                .addComponent(divFilasScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(fijarDivsPanelLayout.createSequentialGroup()
                                    .addComponent(divColumLabel)
                                    .addGap(59, 59, 59))
                                .addComponent(divColumScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );

        fijarDivsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {divColumScrollPane, divFilasScrollPane});

        fijarDivsPanelLayout.setVerticalGroup(
            fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fijarDivsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo3Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo3Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exp3TextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(fijarDivsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(fijarDivsPanelLayout.createSequentialGroup()
                        .addComponent(divFilasLabel)
                        .addGap(9, 9, 9)
                        .addComponent(divFilasScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(fijarDivsPanelLayout.createSequentialGroup()
                        .addComponent(divColumLabel)
                        .addGap(9, 9, 9)
                        .addComponent(divColumScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        fijarDivsPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {divColumScrollPane, divFilasScrollPane});

        mainPanel.add(fijarDivsPanel, "3");

        compIndividualPanel.setName("compIndividualPanel"); // NOI18N

        titulo4Label.setFont(resourceMap.getFont("titulo4Label.font")); // NOI18N
        titulo4Label.setText(resourceMap.getString("titulo4Label.text")); // NOI18N
        titulo4Label.setName("titulo4Label"); // NOI18N

        titulo4Separator.setName("titulo4Separator"); // NOI18N

        exp4TextPane.setBackground(this.getBackground());
        exp4TextPane.setBorder(null);
        exp4TextPane.setEditable(false);
        exp4TextPane.setText(resourceMap.getString("exp4TextPane.text")); // NOI18N
        exp4TextPane.setName("exp4TextPane"); // NOI18N

        compartimentosLabel.setFont(resourceMap.getFont("compartimentosLabel.font")); // NOI18N
        compartimentosLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        compartimentosLabel.setText(resourceMap.getString("compartimentosLabel.text")); // NOI18N
        compartimentosLabel.setName("compartimentosLabel"); // NOI18N

        compartimentosScrollPane.setName("compartimentosScrollPane"); // NOI18N

        compartimentosTable.setName("compartimentosTable"); // NOI18N
        compartimentosTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                compartimentosTableMouseClicked(evt);
            }
        });
        compartimentosScrollPane.setViewportView(compartimentosTable);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(DinamicaPoblacion.class, this);
        derechaButton.setAction(actionMap.get("tablaSiguientePaso4")); // NOI18N
        derechaButton.setToolTipText(resourceMap.getString("derechaButton.toolTipText")); // NOI18N
        derechaButton.setName("derechaButton"); // NOI18N

        izquierdaButton.setAction(actionMap.get("tablaAnteriorPaso4")); // NOI18N
        izquierdaButton.setToolTipText(resourceMap.getString("izquierdaButton.toolTipText")); // NOI18N
        izquierdaButton.setName("izquierdaButton"); // NOI18N

        javax.swing.GroupLayout compIndividualPanelLayout = new javax.swing.GroupLayout(compIndividualPanel);
        compIndividualPanel.setLayout(compIndividualPanelLayout);
        compIndividualPanelLayout.setHorizontalGroup(
            compIndividualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compIndividualPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(compIndividualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titulo4Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addComponent(titulo4Label)
                    .addComponent(exp4TextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, compIndividualPanelLayout.createSequentialGroup()
                        .addComponent(izquierdaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(compIndividualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(compartimentosScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                            .addComponent(compartimentosLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(derechaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        compIndividualPanelLayout.setVerticalGroup(
            compIndividualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compIndividualPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo4Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo4Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exp4TextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(compartimentosLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(compIndividualPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(derechaButton)
                    .addComponent(izquierdaButton)
                    .addComponent(compartimentosScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE))
                .addContainerGap())
        );

        mainPanel.add(compIndividualPanel, "4");

        cancelButton.setAction(actionMap.get("botonCancelar")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        nextButton.setAction(actionMap.get("botonSiguiente")); // NOI18N
        nextButton.setName("nextButton"); // NOI18N

        backButton.setAction(actionMap.get("botonAnterior")); // NOI18N
        backButton.setName("backButton"); // NOI18N

        buttonSeparator.setName("buttonSeparator"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(277, Short.MAX_VALUE)
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addContainerGap())
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(buttonSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {backButton, cancelButton, nextButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(nextButton)
                    .addComponent(backButton))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {backButton, cancelButton, nextButton});

        pack();
    }// </editor-fold>//GEN-END:initComponents

/**
 * Método que se realiza cuando el usuario pulsa sobre una celda del 3er paso,
 * modo 2, para abrir una ventana de modificación de la definición del
 * compartimento asociado a esa celda.
 * @param evt El evento generado.
 */
private void compartimentosTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_compartimentosTableMouseClicked
    // Al presionar sobre un compartimento
    // Si se trata de la primera columna, que serán categorías, no hacer nada
    if (this.compartimentosTable.getSelectedColumn() == 0) {
        return;
    }
    // Construir el nombre del compartimento seleccionado en base a la tabla en la que estemos, fila y columna
    String nombreCompartimento = ""; // NOI18N
    if (this.epidemia.getPoblacion().getDivisiones().length == 2) {
        // CASO 2: dos divisiones (sigue siendo una sola tabla)
        if (this.divFilasList.getSelectedIndex() == 0) {
            nombreCompartimento = (String) this.tablasPaso4[0].getValueAt(this.compartimentosTable.getSelectedRow(), 0) + "_" + this.tablasPaso4[0].getColumnName(this.compartimentosTable.getSelectedColumn()); // NOI18N
        } else {
            nombreCompartimento += this.tablasPaso4[0].getColumnName(this.compartimentosTable.getSelectedColumn()) + "_" + (String) this.tablasPaso4[0].getValueAt(this.compartimentosTable.getSelectedRow(), 0); // NOI18N
        }
    } else {
        // CASO 3: más de 2 divisiones (más de 1 tabla)
        String[] tituloSplit = this.tituloTablasPaso4[this.tablaPaso4Actual].split(" - "); // NOI18N
        nombreCompartimento = ""; // NOI18N
        int m = 0;
        for (int i = 0; i < this.epidemia.getPoblacion().getDivisiones().length; i++) {
            // Si la división que toca poner es la usada por filas,
            // poner el valor de la misma fila [j], columna 0
            if (this.divFilasList.getSelectedIndex() == i) {
                nombreCompartimento += (String) this.tablasPaso4[this.tablaPaso4Actual].getValueAt(this.compartimentosTable.getSelectedRow(), 0) + "_"; // NOI18N
            // Si la división que toca poner es la usada por columnas,
            // poner el nombre de la cabecera de la columna
            } else if (this.divColumList.getSelectedIndex() == i) {
                nombreCompartimento += this.tablasPaso4[this.tablaPaso4Actual].getColumnName(this.compartimentosTable.getSelectedColumn()) + "_"; // NOI18N
            // En otro caso, poner el trozo del título por el que nos llegamos
            } else {
                nombreCompartimento += tituloSplit[m] + "_"; // NOI18N
                m++;
            }
        }
        nombreCompartimento = nombreCompartimento.substring(0, (nombreCompartimento.length()-1));
        
    }
    // Abrir una nueva ventana para definir la dinámica de este compartimento independientemente
    javax.swing.JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
    delphsim.DinamicaCompartimento dc = new delphsim.DinamicaCompartimento(mainFrame, true, this.epidemia, nombreCompartimento);
    this.modificado = this.modificado || dc.modificado;
}//GEN-LAST:event_compartimentosTableMouseClicked

    /**
     * Acción a realizar cuando se pulsa, en cualquier paso, el botón "Cancelar".
     */
    @Action
    public void botonCancelar() {
        // Cerrar la ventana
        dispose();
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Siguiente" en
     * cualquier paso del asistente. Su comportamiento depende del paso donde
     * nos encontremos. Se encarga de la comprobación de campos correctos,
     * mostrar los pasos correspondientes actualizando sus componentes
     * dinámicamente e ir guardando la información introducida en una epidemia
     * temporal.
     */
    @Action
    public void botonSiguiente() {
        // Determinar el siguiente paso a mostrar
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaPoblacion.class);
        java.util.regex.Pattern patronFunciones = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoFunciones")); // NOI18N
        java.util.regex.Pattern patronElementos = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoElementos")); // NOI18N
        java.util.regex.Matcher comprobador;
        CardLayout cl = (CardLayout) (this.mainPanel.getLayout());
        String card = String.valueOf(this.pantallaActual + 1);
        if (this.pantallaActual == 1) {
            // COMPROBAR PASO 1, PREPARAR PASO 2 y 3
            // Tabla del paso 2
            Division div = this.epidemia.getPoblacion().getDivisiones()[this.divPrincipalComboBox.getSelectedIndex()];
            ((DefaultTableModel)this.paso2Table.getModel()).setRowCount(div.getCategorias().length);
            for (int i = 0; i < div.getCategorias().length; i++) {
                this.paso2Table.setValueAt(div.getCategorias()[i].getNombre(), i, 0);
                this.paso2Table.setValueAt(null, i, 1);
            }
            this.paso2Table.clearSelection();
            if (this.modo1RadioButton.isSelected()) {
                card = String.valueOf(this.pantallaActual + 1);
                // Cambiar el texto del botón "Siguiente" por "Terminar"
                this.nextButton.setText(resourceMap.getString("botonSiguiente.textAlternativo")); // NOI18N
            } else if (this.modo2RadioButton.isSelected()) {
                card = String.valueOf(this.pantallaActual + 2);
                this.pantallaActual++;
            }
            // Activar el botón "Anterior"
            this.backButton.setEnabled(true);            
        } else if (this.pantallaActual == 2) {
            // COMPROBAR PASO 2, AQUÍ TERMINA EL MODO1
            String comprobacion = this.comprobarPaso2();
            // Si se ha encontrado todo vacío, cerrar la ventana y terminar
            if (comprobacion.equals("VACIO")) { // NOI18N
                Object[] options = {resourceMap.getString("comprobacion.warn.todoVacio.op1"), // NOI18N
                                    resourceMap.getString("comprobacion.warn.todoVacio.op2"), // NOI18N
                                    resourceMap.getString("comprobacion.warn.todoVacio.op3")}; // NOI18N
                int n = JOptionPane.showOptionDialog(this.mainPanel,
                        resourceMap.getString("comprobacion.warn.todoVacio.msg"), // NOI18N
                        resourceMap.getString("comprobacion.warn.todoVacio.title"), // NOI18N
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.WARNING_MESSAGE,
                        null,
                        options,
                        options[2]);
                switch (n) {
                    // Si escoge eliminar la definición de los compartimentos
                    case 0:
                        // Eliminamos la definición de todos los compartimentos
                        for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                            this.epidemia.getCompartimento(i).setDefinicionContinua(null);
                        }
                        // Borramos vínculos de los elementos de los que dependían
                        for (Parametro par : this.epidemia.getParametros()) {
                            par.setCompartimentosVinculados(new String[0]);
                        }
                        for (Proceso proc : this.epidemia.getProcesos()) {
                            proc.setCompartimentosVinculados(new String[0]);
                        }
                        // Y cerramos la ventana e indicamos modificaciones
                        this.modificado = true;
                        dispose();
                        break;
                    // Si escoge no modificar nada
                    case 1:
                        // Cerramos la ventana
                        dispose();
                        break;
                    // Si escoge volver atrás (o cierra el popup) no hacer nada
                    case 2:
                    default:
                        break;
                }
                return;
            }
            // Si ha habido errores, mostrar y no seguir
            if (!comprobacion.isEmpty()) {
                JOptionPane.showMessageDialog(this.mainPanel, comprobacion, resourceMap.getString("comprobacion.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                return;
            }
            if (this.epidemia.getPoblacion().getDivisiones().length == 1) {
                // Si sólo tenemos una división, en este punto sólo tenemos que actualizar datos para cada compartimento
                for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                    // Guardamos la nueva definición
                    this.epidemia.getCompartimento(i).setDefinicionContinua(this.paso2Table.getValueAt(i, 1).toString());
                    // Eliminamos todos los posibles vínculos anteriores de otros elementos con éste
                    for (int j = 0; j < this.epidemia.getParametros().length; j++) {
                        this.epidemia.getParametro(j).eliminarCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                    }
                    for (int j = 0; j < this.epidemia.getProcesos().length; j++) {
                        this.epidemia.getProceso(j).eliminarProcesoVinculado(this.epidemia.getCompartimento(i).getNombre());
                    }
                    for (int j = 0; j < this.epidemia.getCompartimentos().length; j++) {
                        this.epidemia.getCompartimento(j).eliminarProcesoVinculado(this.epidemia.getCompartimento(i).getNombre());
                    }
                    // Eliminamos el uso de funciones de la cadena de la ecuación
                    // para no confundirlas con el nombre de otros elementos
                    String ecuacion = this.paso2Table.getValueAt(i, 1).toString();
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
                            this.epidemia.getParametro(comprobador.group()).anadirCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                        } else if (clase == Proceso.class) {
                            this.epidemia.getProceso(comprobador.group()).anadirCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                        } else if (clase == Compartimento.class) {
                            this.epidemia.getCompartimento(comprobador.group()).anadirCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                        } else {
                            System.err.println("Error: producido en DinamicaPoblacion::botonSiguiente(), paso 2, el patrón ha encontrado un elemento que no está definido, debería haberse detectado en comprobarPaso2()"); // NOI18N
                        }
                    }
                }
            } else {
                // En caso contrario, tendremos que transformar las ecuaciones para cada compartimento
                Division divPrinc = this.epidemia.getPoblacion().getDivisiones()[this.divPrincipalComboBox.getSelectedIndex()];
                Vector categoriasDP = new Vector();
                for (int i = 0; i < divPrinc.getCategorias().length; i++) {
                    categoriasDP.add(divPrinc.getCategorias()[i].getNombre());
                }
                // Para cada compartimento
                for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                    // Buscar en su nombre qué categoría de la división principal le corresponde
                    String[] nombreCachos = this.epidemia.getCompartimento(i).getNombre().split("_"); // NOI18N
                    String cachoInicio = ""; // NOI18N
                    String cachoFinal = ""; // NOI18N
                    String categoria = ""; // NOI18N
                    for (int j = 0; j < nombreCachos.length; j++) {
                        if (categoriasDP.contains(nombreCachos[j])) {
                            categoria = nombreCachos[j];
                            for (int k = 0; k < j; k++) {
                                cachoInicio += nombreCachos[k] + "_"; // NOI18N
                            }
                            for (int k = j+1; k < nombreCachos.length; k++) {
                                cachoFinal += "_" + nombreCachos[k]; // NOI18N
                            }
                            break;
                        }
                    }
                    String definicion = this.paso2Table.getValueAt(categoriasDP.indexOf(categoria), 1).toString();
                    // Reformar su definición buscando categorías de la división principal y sustituyéndolas por los compartimentos que correspondan
                    // En general, si su nombre fuera "sus_jov_hom" y la categoría fuera "adu" se sustituiría por "sus_adu_hom"
                    for (Object o : categoriasDP) {
                        java.util.regex.Pattern patron;
                        // Primero buscamos por si estuviera al principio
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombrePrincipio", o.toString())); // NOI18N
                        comprobador = patron.matcher(definicion);
                        if (comprobador.find()) {
                            definicion = cachoInicio + o.toString() + cachoFinal + definicion.substring(comprobador.end()-1);
                        }
                        // Segundo buscamos por si estuviera al final
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreFinal", o.toString())); // NOI18N
                        comprobador = patron.matcher(definicion);
                        if (comprobador.find()) {
                            definicion = definicion.substring(0, comprobador.start()+1) + cachoInicio + o.toString() + cachoFinal;
                        }
                        // Tercero buscamos por si fuera propiamente un nombre de una categoría
                        if (definicion.equals(o.toString())) {
                            definicion = cachoInicio + o.toString() + cachoFinal;
                        }
                        // Por último buscamos por si hubiera por el medio
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreMedio", o.toString())); // NOI18N
                        comprobador = patron.matcher(definicion);
                        while (comprobador.find()) {
                            definicion = definicion.substring(0, comprobador.start()+1) + cachoInicio + o.toString() + cachoFinal + definicion.substring(comprobador.end()-1);
                            comprobador = patron.matcher(definicion);
                        }
                    }
                    // Guardamos la nueva definición
                    this.epidemia.getCompartimento(i).setDefinicionContinua(definicion);
                    // Eliminamos todos los posibles vínculos anteriores de otros elementos con éste
                    for (int j = 0; j < this.epidemia.getParametros().length; j++) {
                        this.epidemia.getParametro(j).eliminarCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                    }
                    for (int j = 0; j < this.epidemia.getProcesos().length; j++) {
                        this.epidemia.getProceso(j).eliminarCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                    }
                    for (int j = 0; j < this.epidemia.getCompartimentos().length; j++) {
                        this.epidemia.getCompartimento(j).eliminarCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                    }
                    // Eliminamos el uso de funciones de la cadena de la ecuación
                    // para no confundirlas con el nombre de otros elementos
                    comprobador = patronFunciones.matcher(definicion);
                    while (comprobador.find()) {
                        String principio = definicion.substring(0, comprobador.start());
                        definicion = principio + definicion.substring(comprobador.end());
                        comprobador = patronFunciones.matcher(definicion);
                    }
                    // Buscamos referencias a otros elementos usados y añadimos los vínculos
                    comprobador = patronElementos.matcher(definicion);
                    while (comprobador.find()) {
                        Class clase = this.epidemia.estaDefinido(comprobador.group());
                        if (clase == Parametro.class) {
                            this.epidemia.getParametro(comprobador.group()).anadirCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                        } else if (clase == Proceso.class) {
                            this.epidemia.getProceso(comprobador.group()).anadirCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                        } else if (clase == Compartimento.class) {
                            this.epidemia.getCompartimento(comprobador.group()).anadirCompartimentoVinculado(this.epidemia.getCompartimento(i).getNombre());
                        } else {
                            System.err.println("ERROR");
                        }
                    }
                }
            }
            // Al terminar, cerrar la ventana
            this.modificado = true;
            dispose();
        } else if (this.pantallaActual == 3) {
            // COMPROBAR PASO 3, PREPARAR PASO 4
            // Primero comprobamos que los campos sean correctos
            int porFilas = this.divFilasList.getSelectedIndex();
            int porColumnas = this.divColumList.getSelectedIndex();
            if (porFilas == porColumnas) {
                JOptionPane.showMessageDialog(this.mainPanel, resourceMap.getString("comprobacion.error.paso3"), resourceMap.getString("comprobacion.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                return;
            }

            // Se calculan el nº de tablas que tendremos
            String[] nombreCompartimentos = this.epidemia.combinarCategorias(null);
            Division divFilas = this.epidemia.getPoblacion().getDivisiones()[porFilas];
            Division divColumnas = this.epidemia.getPoblacion().getDivisiones()[porColumnas];
            int numTablas = nombreCompartimentos.length/(divFilas.getCategorias().length*divColumnas.getCategorias().length);
            this.maxIndexTablasPaso4 = numTablas - 1;
            // Se pone el título de encima, el estado y tooltip de los botones
            this.izquierdaButton.setEnabled(false);
            this.izquierdaButton.setToolTipText(resourceMap.getString("izquierdaButton.toolTipText")); // NOI18N
            if (numTablas == 1) {
                this.compartimentosLabel.setText(""); // NOI18N
                this.derechaButton.setEnabled(false);
                this.derechaButton.setToolTipText(resourceMap.getString("derechaButton.toolTipText")); // NOI18N
            } else {
                // Se generan los nombres de todas las etiquetas y de los tooltips
                int numDivisiones = this.epidemia.getPoblacion().getDivisiones().length;
                String[][] divisiones = new String[numDivisiones-2][];
                int j = 0;
                for (int i = 0; i < numDivisiones; i++) {
                    if (i != porFilas && i != porColumnas) {
                        divisiones[j] = new String[this.epidemia.getPoblacion().getDivisiones()[i].getCategorias().length];
                        for (int k = 0; k < divisiones[j].length; k++) {
                            divisiones[j][k] = this.epidemia.getPoblacion().getDivisiones()[i].getCategorias()[k].getNombre();
                        }
                        j++;
                    }
                }
                this.tituloTablasPaso4 = new String[numTablas];
                this.tooltipsTablasPaso4 = new String[numTablas];
                // Combinar mediante una serie de bucles pa morirse
                int repetirNVeces = numTablas;
                int repetirMVeces = 1;
                for (int i = 0; i < divisiones.length; i++) {
                    repetirNVeces = repetirNVeces / divisiones[i].length;
                    for (j = 0; j < divisiones[i].length; j++) {
                        for (int l = 0; l < repetirMVeces; l++) {
                            for (int k = 0; k < repetirNVeces; k++) {
                                if (this.tituloTablasPaso4[l * (numTablas / repetirMVeces) + repetirNVeces * j + k] == null) {
                                    this.tituloTablasPaso4[l * (numTablas / repetirMVeces) + repetirNVeces * j + k] = divisiones[i][j];
                                    this.tooltipsTablasPaso4[l * (numTablas / repetirMVeces) + repetirNVeces * j + k] = "<html>" + divisiones[i][j]; // NOI18N
                                } else {
                                    this.tituloTablasPaso4[l * (numTablas / repetirMVeces) + repetirNVeces * j + k] += " - " + divisiones[i][j];
                                    this.tooltipsTablasPaso4[l * (numTablas / repetirMVeces) + repetirNVeces * j + k] += ",<br/>" + divisiones[i][j]; // NOI18N
                                }
                            }
                        }
                    }
                    repetirMVeces = repetirMVeces * divisiones[i].length;
                }
                for (int i = 0; i < numTablas; i++) {
                    this.tooltipsTablasPaso4[i] += "</html>"; // NOI18N
                }
                this.compartimentosLabel.setText(this.tituloTablasPaso4[0]);
                this.derechaButton.setEnabled(true);
                this.derechaButton.setToolTipText(this.tooltipsTablasPaso4[1]);
            }
            // Se crean todas las tablas
            this.tablasPaso4 = new DelphSimTableModel[numTablas];
            Object[][] nombresFilas = new Object[divFilas.getCategorias().length][1];
            for (int i = 0; i < divFilas.getCategorias().length; i++) {
                nombresFilas[i][0] = divFilas.getCategorias()[i].getNombre();
            }
            String[] nombresColumnas = new String[divColumnas.getCategorias().length+1];
            nombresColumnas[0] = ""; // NOI18N
            for (int i = 0; i < divColumnas.getCategorias().length; i++) {
                nombresColumnas[i+1] = divColumnas.getCategorias()[i].getNombre();
            }
            // Y se establecen las celdas no editables y el valor inicial de cada una
            for (int i = 0; i < numTablas; i++) {
                this.tablasPaso4[i] = new DelphSimTableModel(nombresFilas, nombresColumnas);
                this.tablasPaso4[i].refreshCanEdit(false);
                for (int j = 0; j < this.tablasPaso4[i].getRowCount(); j++) {
                    for (int k = 1; k < this.tablasPaso4[i].getColumnCount(); k++) {
                        this.tablasPaso4[i].setValueAt("...", j, k); // NOI18N
                    }
                }
            }
            // Se pone como modelo el de la primera tabla, selección de una sola celda, centrado al medio
            this.compartimentosTable.setModel(this.tablasPaso4[0]);
            this.compartimentosTable.setColumnSelectionAllowed(true);
            ((javax.swing.table.DefaultTableCellRenderer)this.compartimentosTable.getCellRenderer(0, 0)).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            
            // Estas 4 líneas son para poner en gris la primera columna.
            // Cuando se cambia entre distintas tablas se revierte el efecto.
            // Por ello se quita - TODO: estudiar más a fondo.
            //javax.swing.table.DefaultTableCellRenderer dtcr = new javax.swing.table.DefaultTableCellRenderer();
            //dtcr.setBackground(new java.awt.Color(244, 244, 244));
            //dtcr.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            //this.compartimentosTable.getColumnModel().getColumn(0).setCellRenderer(dtcr);
            
            // Cambiar el texto del botón "Siguiente" por "Terminar"
            this.nextButton.setText(resourceMap.getString("botonSiguiente.textAlternativo")); // NOI18N
        } else if (this.pantallaActual == 4) {
            // COMPROBAR PASO 4, GUARDAR Y SALIR
            // Como todo va mediante los otros botones, aquí no hay que comprobar ni guardar nada
            // Cerrar la ventana
            dispose();
        }
        // Mostrar el paso correspondiente
        cl.show(this.mainPanel, card);
        // Actualizar la variable del paso donde estamos
        this.pantallaActual++;
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Anterior" en
     * cualquier paso del asistente. Su comportamiento depende del paso donde
     * nos encontremos. Se encarga de mostrar el paso correspondiente.
     */
    @Action
    public void botonAnterior() {
        // Determinar el anterior paso a mostrar
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaPoblacion.class);
        CardLayout cl = (CardLayout) (this.mainPanel.getLayout());
        String card = String.valueOf(this.pantallaActual - 1);
        if (this.pantallaActual == 4) {
            // Poner a cero la variable que indica la tabla a mostrar en paso 4
            this.tablaPaso4Actual = 0;
            // Cambiar el texto del botón "Terminar" por "Siguiente"
            this.nextButton.setText(resourceMap.getString("botonSiguiente.Action.text")); // NOI18N
        } else if (this.pantallaActual == 3) {
            if (this.modo1RadioButton.isSelected()) {
                card = String.valueOf(this.pantallaActual - 1);
            } else if (this.modo2RadioButton.isSelected()) {
                card = String.valueOf(this.pantallaActual - 2);
                this.pantallaActual--;
                // Desactivar el botón "Anterior"
                this.backButton.setEnabled(false);
            }
        } else if (this.pantallaActual == 2) {
            // Cambiar el texto del botón "Terminar" por "Siguiente"
            this.nextButton.setText(resourceMap.getString("botonSiguiente.Action.text")); // NOI18N
            // Desactivar el botón "Anterior"
            this.backButton.setEnabled(false);
        } else if (this.pantallaActual == 1) {
            // Ya estamos en el paso 1, no hacer nada
        }
        // Mostrar el paso correspondiente
        cl.show(this.mainPanel, card);
        // Actualizar la variable del paso donde estamos (a no ser que sea el paso 1)
        this.pantallaActual = this.pantallaActual > 1 ? (--this.pantallaActual) : 1;
    }

    /**
     * Comprueba que los datos introducidos en el segundo paso sean correctos.
     * @return  Una cadena de texto con los errores de la comprobación.
     *          Vacía si no ha habido errores.
     */
    private String comprobarPaso2() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaPoblacion.class);
        String errores = ""; // NOI18N
        
        // Lo primero que miramos es si lo ha dejado todo en blanco, en cuyo caso será correcto y no se hará nada
        int numFilasVacias = 0;
        for (int i = 0; i < this.paso2Table.getRowCount(); i++) {
            if (this.paso2Table.getValueAt(i, 1) == null || this.paso2Table.getValueAt(i, 1).toString().isEmpty()) {
                numFilasVacias++;
            }
        }
        if (numFilasVacias == this.paso2Table.getRowCount()) {
            return "VACIO"; // NOI18N
        }
        
        // Preparamos un JEP añadiendo los elementos que se van a poder usar
        // NOTA: NO se podrán usar los atajos
        JEP jep = Epidemia.CrearDelphSimJEP();
        // Se podrán usar todos los parámetros
        for (int i = 0; i < this.epidemia.getParametros().length; i++) {
            jep.addVariable(this.epidemia.getParametro(i).getNombre(), 1);
        }
        // Se podrán usar todos los procesos
        for (int i = 0; i < this.epidemia.getProcesos().length; i++) {
            jep.addVariable(this.epidemia.getProceso(i).getNombre(), 1);
        }
        // Se podrán usar todos los compartimentos generados
        for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
            jep.addVariable(this.epidemia.getCompartimento(i).getNombre(), 1);
        }
        // Se podrán usar las categorías de la división escogida como principal
        for (int i = 0; i < this.epidemia.getPoblacion().getDivisiones()[this.divPrincipalComboBox.getSelectedIndex()].getCategorias().length; i++) {
            jep.addVariable(this.epidemia.getPoblacion().getDivisiones()[this.divPrincipalComboBox.getSelectedIndex()].getCategorias()[i].getNombre(), 1);
        }
        
        // Comprobar todas las filas de la tabla
        for (int i = 0; i < this.paso2Table.getRowCount(); i++) {
            try {
                // Comprobar la definición de la fila i
                // Si tienen puntos y coma -> inválida
                if (this.paso2Table.getValueAt(i, 1).toString().contains(";")) {
                    errores += resourceMap.getString("comprobacion.error.paso2.definicionInvalida", i+1); // NOI18N
                } else {
                    Node funcion = jep.parseExpression(this.paso2Table.getValueAt(i, 1).toString());
                    try {
                        jep.evaluate(funcion);
                    } catch (ParseException ex) {
                        System.err.println(ex.getMessage());
                        errores += resourceMap.getString("comprobacion.error.paso2.definicionInvalida", i+1); // NOI18N
                    }
                }
            } catch (java.lang.NullPointerException ex) {
                System.err.println("NullPointerException" + ex.getMessage());
                errores += resourceMap.getString("comprobacion.error.paso2.definicionVacia", i+1); // NOI18N
            }
        }
        
        return errores;
    }

    /**
     * Acción que se realiza en el paso 4 del asistente en el caso de que haya
     * varias tablas que mostrar y el usuario presione el botón para moverse
     * a la tabla anterior. Actualiza el modelo de la tabla, la etiqueta que
     * hay encima de ésta y los tooltips y estado de los botones.
     */
    @Action
    public void tablaAnteriorPaso4() {
        this.tablaPaso4Actual--;
        this.compartimentosTable.setModel(this.tablasPaso4[this.tablaPaso4Actual]);
        this.compartimentosLabel.setText(this.tituloTablasPaso4[this.tablaPaso4Actual]);
        if (this.tablaPaso4Actual == 0) {
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
            this.izquierdaButton.setEnabled(false);
            this.izquierdaButton.setToolTipText(resourceMap.getString("izquierdaButton.toolTipText")); // NOI18N
        } else {
            this.izquierdaButton.setToolTipText(this.tooltipsTablasPaso4[this.tablaPaso4Actual - 1]);
        }
        this.derechaButton.setEnabled(true);
        this.derechaButton.setToolTipText(this.tooltipsTablasPaso4[this.tablaPaso4Actual + 1]);
    }

    /**
     * Acción que se realiza en el paso 4 del asistente en el caso de que haya
     * varias tablas que mostrar y el usuario presione el botón para moverse
     * a la tabla siguiente. Actualiza el modelo de la tabla, la etiqueta que
     * hay encima de ésta y los tooltips y estado de los botones.
     */
    @Action
    public void tablaSiguientePaso4() {
        this.tablaPaso4Actual++;
        this.compartimentosTable.setModel(this.tablasPaso4[this.tablaPaso4Actual]);
        this.compartimentosLabel.setText(this.tituloTablasPaso4[this.tablaPaso4Actual]);
        if (this.tablaPaso4Actual == this.maxIndexTablasPaso4) {
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
            this.derechaButton.setEnabled(false);
            this.derechaButton.setToolTipText(resourceMap.getString("derechaButton.toolTipText")); // NOI18N
        } else {
            this.derechaButton.setToolTipText(this.tooltipsTablasPaso4[this.tablaPaso4Actual + 1]);
        }
        this.izquierdaButton.setEnabled(true);
        this.izquierdaButton.setToolTipText(this.tooltipsTablasPaso4[this.tablaPaso4Actual - 1]);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JSeparator buttonSeparator;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel compIndividualPanel;
    private javax.swing.JLabel compartimentosLabel;
    private javax.swing.JScrollPane compartimentosScrollPane;
    private javax.swing.JTable compartimentosTable;
    private javax.swing.JButton derechaButton;
    private javax.swing.JLabel divColumLabel;
    private javax.swing.JList divColumList;
    private javax.swing.JScrollPane divColumScrollPane;
    private javax.swing.JLabel divFilasLabel;
    private javax.swing.JList divFilasList;
    private javax.swing.JScrollPane divFilasScrollPane;
    private javax.swing.JComboBox divPrincipalComboBox;
    private javax.swing.JLabel divPrincipalLabel;
    private javax.swing.JPanel divPrincipalPanel;
    private javax.swing.JLabel escogerModoLabel;
    private javax.swing.JPanel escogerModoPanel;
    private javax.swing.JTextPane exp2TextPane;
    private javax.swing.JTextPane exp3TextPane;
    private javax.swing.JTextPane exp4TextPane;
    private javax.swing.JPanel fijarDivsPanel;
    private javax.swing.JButton izquierdaButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JRadioButton modo1RadioButton;
    private javax.swing.JTextPane modo1expTextPane;
    private javax.swing.JRadioButton modo2RadioButton;
    private javax.swing.JTextPane modo2expTextPane2;
    private javax.swing.ButtonGroup modoButtonGroup;
    private javax.swing.JButton nextButton;
    private javax.swing.JTable paso2Table;
    private javax.swing.JScrollPane paso2TableSP;
    private javax.swing.JLabel titulo1Label;
    private javax.swing.JSeparator titulo1Separator;
    private javax.swing.JLabel titulo2Label;
    private javax.swing.JSeparator titulo2Separator;
    private javax.swing.JLabel titulo3Label;
    private javax.swing.JSeparator titulo3Separator;
    private javax.swing.JLabel titulo4Label;
    private javax.swing.JSeparator titulo4Separator;
    // End of variables declaration//GEN-END:variables
    protected boolean modificado = false;
}
