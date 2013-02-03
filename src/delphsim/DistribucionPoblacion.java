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

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.application.Action;

/**
 * Esta clase implementa la Vista-Controlador de las ventanas de "Nuevo Modelo"
 * y "Modificar Modelo", dependiendo de cómo se use su constructor.
 * @author Víctor E. Tamames Gómez
 */
public class DistribucionPoblacion extends javax.swing.JDialog {

    /**
     * La epidemia que vamos a generar a partir de los datos que introduzca el
     * usuario. Cumple que, si no es null, es una epidemia consistente (tiene
     * definidos todos los campos obligatorios y con formato correcto).
     */
    private Epidemia epidemia;

    /**
     * La epidemia con la que va a trabajar esta clase, la cual irá actualizando
     * según el usuario introduzca nuevos datos. No tiene por qué ser
     * consistente, solamente al final del último paso.
     */
    private Epidemia epidemiaTemporal = new Epidemia();

    /**
     * Flag de control para diferenciar entre crear una nueva epidemia y
     * modificar la actual.
     */
    private boolean modificando = false;

    /**
     * Flag que nos indica si tenemos que cargar en pantalla compartimentos ya
     * existentes o crearlos nuevos. Se crearán nuevos en los siguientes casos:
     * si se está creando una epidemia nueva o si se está modificando una
     * existente pero se han modificado las categorías (de forma que los
     * compartimentos generados son distintos de los originales).
     */
    private boolean nuevosCompartimentos = true;
    
    /**
     * Vector que contendrá los procesos vinculados con los compartimentos, en
     * el caso de que estemos modificando la Epidemia, para así poder vaciar sus
     * definiciones si hubiera que rehacer los compartimentos y evitar
     * inconsistencias en el modelo.
     */
    private Vector procesosVinculados = new Vector();

    /**
     * Indica el paso del asistente donde estamos actualmente.
     */
    private int pantallaActual = 1;

    /**
     * Indica la tabla del paso 4 que estamos mostrando.
     */
    private int tablaPaso4Actual = 0;

    /**
     * Indica el número total de tablas que hay en el paso 4.
     */
    private int maxIndexTablasPaso4 = 1;

    /**
     * El modelo del desplegable del paso 1 del asistente.
     */
    private DefaultComboBoxModel modeloDesplegablePaso1 = new DefaultComboBoxModel(new String[] { "Segundos", "Minutos", "Horas", "Días", "Semanas", "Meses", "Años" }); // NOI18N

    /**
     * Modelo de la tabla que se mostrará en el paso 1 del asistente. Extiende
     * algunos métodos de DefaultTableModel para poder personalizarla.
     */
    private DefaultTableModel modeloTablaPaso1 = new DefaultTableModel(
            new Object[][]{{new Integer(1), null, null}},
            new String[]{"Nº", "Nombre", "Nº categorías"}) // NOI18N
    {
        Class[] types = new Class[]{
            java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class
        };
        boolean[] canEdit = new boolean[]{
            false, true, true
        };
        @Override
        public Class getColumnClass(int columnIndex) {
            return types[columnIndex];
        }
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return canEdit[columnIndex];
        }
    };

    /**
     * Modelo de la tabla que se mostrará en el paso 2 del asistente. Extiende
     * algunos métodos de DefaultTableModel para poder personalizarla.
     */
    private DelphSimTableModel modeloTablaPaso2;

    /**
     * Lista de divisiones introducidas que se muestra en el paso 3 para escoger
     * cuál mostrar en las filas de las tablas del paso 4.
     */
    private DefaultListModel listaFilasPaso3;

    /**
     * Lista de divisiones introducidas que se muestra en el paso 3 para escoger
     * cuál mostrar en las columnas de las tablas del paso 4.
     */
    private DefaultListModel listaColumnasPaso3;

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
     * Crea un nuevo asistente de DistribucionPoblacion.
     * @param parent El frame que contiene el objeto.
     * @param modal Indica si se puede acceder al resto de frames o no.
     * @param modificarEpidemia La epidemia a modificar, <CODE>null</CODE> si
     *                          estuviéramos creando una.
     */
    public DistribucionPoblacion(java.awt.Frame parent, boolean modal, Epidemia modificarEpidemia) {
        super(parent, modal);

        // Iniciar componentes
        this.modeloTablaPaso2 = new DelphSimTableModel(
            new Object[][]{{"Categoría 1"},{"Categoría 2"}}, // NOI18N
            new String[]{"Categorías \\ Divisiones"}); // NOI18N
        this.listaFilasPaso3 = new DefaultListModel();
        this.listaColumnasPaso3 = new DefaultListModel();
        initComponents();
        this.backButton.setEnabled(false);
        this.divisionesTable.getTableHeader().setReorderingAllowed(false);
        this.categoriasTable.getTableHeader().setReorderingAllowed(false);
        this.compartimentosTable.getTableHeader().setReorderingAllowed(false);

        // Poner tamaño máximo 64 caracteres al jTextField del nombre de la población
        nombrePobTextField.setDocument(new JTextFieldLimit(64));
        // Y a los campos de la columna "Nombre de la división" de la tabla de divisiones
        javax.swing.JTextField columnaNombres = new javax.swing.JTextField();
        columnaNombres.setDocument(new JTextFieldLimit(64));
        TableColumnModel divisionesTableColumnModel = divisionesTable.getColumnModel();
        divisionesTableColumnModel.getColumn(1).setCellEditor(new DefaultCellEditor(columnaNombres));

        // Personalizar la tabla de divisiones con algún detalle más
        divisionesTableColumnModel.getColumn(0).setMinWidth(27);
        divisionesTableColumnModel.getColumn(0).setMaxWidth(27);
        divisionesTableColumnModel.getColumn(0).setResizable(false);
        divisionesTableColumnModel.getColumn(2).setResizable(false);
        divisionesTableColumnModel.getColumn(2).setMinWidth(100);
        divisionesTableColumnModel.getColumn(2).setMaxWidth(100);
        
        // Índice seleccionado por defecto para la unidad de tiempo = Días (3)
        this.unidadTiempoComboBox.setSelectedIndex(3);

        // Si vamos a modificar una epidemia existente, ésa será la temporal
        if (modificarEpidemia != null) {
            cargarEpidemia(modificarEpidemia);
            this.nuevosCompartimentos = false;
            this.modificando = true;
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

        mainPanel = new javax.swing.JPanel();
        generalPanel = new javax.swing.JPanel();
        titulo1Label = new javax.swing.JLabel();
        titulo1Separator = new javax.swing.JSeparator();
        nombrePobLabel = new javax.swing.JLabel();
        nombrePobTextField = new javax.swing.JTextField();
        numeroHabLabel = new javax.swing.JLabel();
        numeroHabSpinner = new javax.swing.JSpinner();
        unidadTiempoLabel = new javax.swing.JLabel();
        unidadTiempoComboBox = new javax.swing.JComboBox();
        numeroDivLabel = new javax.swing.JLabel();
        numeroDivSpinner = new javax.swing.JSpinner();
        divisionesScrollPane = new javax.swing.JScrollPane();
        divisionesTable = new javax.swing.JTable();
        categoriasPanel = new javax.swing.JPanel();
        titulo2Label = new javax.swing.JLabel();
        titulo2Separator = new javax.swing.JSeparator();
        categoriasTextPane = new javax.swing.JTextPane();
        categoriasScrollPane = new javax.swing.JScrollPane();
        categoriasTable = new javax.swing.JTable();
        descripcionTextPane = new javax.swing.JTextPane();
        descripcionButton = new javax.swing.JButton();
        fijarDivisionesPanel = new javax.swing.JPanel();
        titulo3Label = new javax.swing.JLabel();
        titulo3Separator = new javax.swing.JSeparator();
        aclaracionTextPane = new javax.swing.JTextPane();
        divFilasLabel = new javax.swing.JLabel();
        divFilasScrollPane = new javax.swing.JScrollPane();
        divFilasList = new javax.swing.JList();
        divColumLabel = new javax.swing.JLabel();
        divColumScrollPane = new javax.swing.JScrollPane();
        divColumList = new javax.swing.JList();
        compartimentosPanel = new javax.swing.JPanel();
        titulo4Label = new javax.swing.JLabel();
        titulo4Separator = new javax.swing.JSeparator();
        numHabCompLabel = new javax.swing.JLabel();
        numHabCompNotaLabel = new javax.swing.JLabel();
        compartimentosLabel = new javax.swing.JLabel();
        compartimentosScrollPane = new javax.swing.JScrollPane();
        compartimentosTable = new javax.swing.JTable();
        derechaButton = new javax.swing.JButton();
        izquierdaButton = new javax.swing.JButton();
        Separator = new javax.swing.JSeparator();
        cancelButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.CardLayout());

        generalPanel.setName("generalPanel"); // NOI18N

        titulo1Label.setFont(resourceMap.getFont("titulo1Label.font")); // NOI18N
        titulo1Label.setText(resourceMap.getString("titulo1Label.text")); // NOI18N
        titulo1Label.setName("titulo1Label"); // NOI18N

        titulo1Separator.setName("titulo1Separator"); // NOI18N

        nombrePobLabel.setText(resourceMap.getString("nombrePobLabel.text")); // NOI18N
        nombrePobLabel.setName("nombrePobLabel"); // NOI18N

        nombrePobTextField.setToolTipText(resourceMap.getString("nombrePobTextField.toolTipText")); // NOI18N
        nombrePobTextField.setName("nombrePobTextField"); // NOI18N

        numeroHabLabel.setText(resourceMap.getString("numeroHabLabel.text")); // NOI18N
        numeroHabLabel.setName("numeroHabLabel"); // NOI18N

        numeroHabSpinner.setFont(resourceMap.getFont("numeroHabSpinner.font")); // NOI18N
        numeroHabSpinner.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(0L), Long.valueOf(0L), null, Long.valueOf(1L)));
        numeroHabSpinner.setToolTipText(resourceMap.getString("numeroHabSpinner.toolTipText")); // NOI18N
        numeroHabSpinner.setName("numeroHabSpinner"); // NOI18N

        unidadTiempoLabel.setText(resourceMap.getString("unidadTiempoLabel.text")); // NOI18N
        unidadTiempoLabel.setName("unidadTiempoLabel"); // NOI18N

        unidadTiempoComboBox.setBackground(resourceMap.getColor("unidadTiempoComboBox.background")); // NOI18N
        unidadTiempoComboBox.setFont(resourceMap.getFont("unidadTiempoComboBox.font")); // NOI18N
        unidadTiempoComboBox.setModel(this.modeloDesplegablePaso1);
        unidadTiempoComboBox.setName("unidadTiempoComboBox"); // NOI18N

        numeroDivLabel.setText(resourceMap.getString("numeroDivLabel.text")); // NOI18N
        numeroDivLabel.setName("numeroDivLabel"); // NOI18N

        numeroDivSpinner.setFont(resourceMap.getFont("numeroDivSpinner.font")); // NOI18N
        numeroDivSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        numeroDivSpinner.setName("numeroDivSpinner"); // NOI18N
        numeroDivSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                numeroDivSpinnerStateChanged(evt);
            }
        });

        divisionesScrollPane.setName("divisionesScrollPane"); // NOI18N

        divisionesTable.setModel(this.modeloTablaPaso1);
        divisionesTable.setName("divisionesTable"); // NOI18N
        divisionesScrollPane.setViewportView(divisionesTable);

        javax.swing.GroupLayout generalPanelLayout = new javax.swing.GroupLayout(generalPanel);
        generalPanel.setLayout(generalPanelLayout);
        generalPanelLayout.setHorizontalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(divisionesScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo1Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo1Label, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(unidadTiempoLabel)
                        .addGap(18, 18, 18)
                        .addComponent(unidadTiempoComboBox, 0, 108, Short.MAX_VALUE))
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(numeroHabLabel)
                        .addGap(18, 18, 18)
                        .addComponent(numeroHabSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
                    .addComponent(nombrePobTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(nombrePobLabel)
                    .addGroup(generalPanelLayout.createSequentialGroup()
                        .addComponent(numeroDivLabel)
                        .addGap(18, 18, 18)
                        .addComponent(numeroDivSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo1Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo1Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(nombrePobLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombrePobTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(13, 13, 13)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numeroHabLabel)
                    .addComponent(numeroHabSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unidadTiempoLabel)
                    .addComponent(unidadTiempoComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numeroDivLabel)
                    .addComponent(numeroDivSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(divisionesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
        );

        mainPanel.add(generalPanel, "1");

        categoriasPanel.setName("categoriasPanel"); // NOI18N

        titulo2Label.setFont(resourceMap.getFont("titulo2Label.font")); // NOI18N
        titulo2Label.setText(resourceMap.getString("titulo2Label.text")); // NOI18N
        titulo2Label.setName("titulo2Label"); // NOI18N

        titulo2Separator.setName("titulo2Separator"); // NOI18N

        categoriasTextPane.setBackground(this.getBackground());
        categoriasTextPane.setEditable(false);
        categoriasTextPane.setText(resourceMap.getString("categoriasTextPane.text")); // NOI18N
        categoriasTextPane.setName("categoriasTextPane"); // NOI18N

        categoriasScrollPane.setName("categoriasScrollPane"); // NOI18N

        categoriasTable.setModel(this.modeloTablaPaso2);
        categoriasTable.setName("categoriasTable"); // NOI18N
        categoriasScrollPane.setViewportView(categoriasTable);

        descripcionTextPane.setBackground(this.getBackground());
        descripcionTextPane.setEditable(false);
        descripcionTextPane.setText(resourceMap.getString("descripcionTextPane.text")); // NOI18N
        descripcionTextPane.setName("descripcionTextPane"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(DistribucionPoblacion.class, this);
        descripcionButton.setAction(actionMap.get("anadirDescripcionCat")); // NOI18N
        descripcionButton.setName("descripcionButton"); // NOI18N

        javax.swing.GroupLayout categoriasPanelLayout = new javax.swing.GroupLayout(categoriasPanel);
        categoriasPanel.setLayout(categoriasPanelLayout);
        categoriasPanelLayout.setHorizontalGroup(
            categoriasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, categoriasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(categoriasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(categoriasScrollPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo2Separator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo2Label, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, categoriasPanelLayout.createSequentialGroup()
                        .addComponent(descripcionTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(descripcionButton))
                    .addComponent(categoriasTextPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE))
                .addContainerGap())
        );
        categoriasPanelLayout.setVerticalGroup(
            categoriasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoriasPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo2Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo2Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoriasTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(categoriasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(categoriasPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(descripcionTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descripcionButton))
                .addContainerGap())
        );

        mainPanel.add(categoriasPanel, "2");

        fijarDivisionesPanel.setName("fijarDivisionesPanel"); // NOI18N

        titulo3Label.setFont(resourceMap.getFont("titulo3Label.font")); // NOI18N
        titulo3Label.setText(resourceMap.getString("titulo3Label.text")); // NOI18N
        titulo3Label.setName("titulo3Label"); // NOI18N

        titulo3Separator.setName("titulo3Separator"); // NOI18N

        aclaracionTextPane.setBackground(this.getBackground());
        aclaracionTextPane.setBorder(null);
        aclaracionTextPane.setEditable(false);
        aclaracionTextPane.setText(resourceMap.getString("aclaracionTextPane.text")); // NOI18N
        aclaracionTextPane.setName("aclaracionTextPane"); // NOI18N

        divFilasLabel.setText(resourceMap.getString("divFilasLabel.text")); // NOI18N
        divFilasLabel.setName("divFilasLabel"); // NOI18N

        divFilasScrollPane.setName("divFilasScrollPane"); // NOI18N

        divFilasList.setModel(this.listaFilasPaso3);
        divFilasList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        divFilasList.setName("divFilasList"); // NOI18N
        divFilasScrollPane.setViewportView(divFilasList);

        divColumLabel.setText(resourceMap.getString("divColumLabel.text")); // NOI18N
        divColumLabel.setName("divColumLabel"); // NOI18N

        divColumScrollPane.setName("divColumScrollPane"); // NOI18N

        divColumList.setModel(this.listaColumnasPaso3);
        divColumList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        divColumList.setName("divColumList"); // NOI18N
        divColumScrollPane.setViewportView(divColumList);

        javax.swing.GroupLayout fijarDivisionesPanelLayout = new javax.swing.GroupLayout(fijarDivisionesPanel);
        fijarDivisionesPanel.setLayout(fijarDivisionesPanelLayout);
        fijarDivisionesPanelLayout.setHorizontalGroup(
            fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fijarDivisionesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aclaracionTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo3Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo3Label, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fijarDivisionesPanelLayout.createSequentialGroup()
                        .addGroup(fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(divFilasLabel)
                            .addComponent(divFilasScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(divColumLabel)
                            .addComponent(divColumScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        fijarDivisionesPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {divColumScrollPane, divFilasScrollPane});

        fijarDivisionesPanelLayout.setVerticalGroup(
            fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fijarDivisionesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo3Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo3Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(aclaracionTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(divFilasLabel)
                    .addComponent(divColumLabel))
                .addGap(9, 9, 9)
                .addGroup(fijarDivisionesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(divColumScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                    .addComponent(divFilasScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
                .addContainerGap())
        );

        mainPanel.add(fijarDivisionesPanel, "3");

        compartimentosPanel.setName("compartimentosPanel"); // NOI18N

        titulo4Label.setFont(resourceMap.getFont("titulo4Label.font")); // NOI18N
        titulo4Label.setText(resourceMap.getString("titulo4Label.text")); // NOI18N
        titulo4Label.setName("titulo4Label"); // NOI18N

        titulo4Separator.setName("titulo4Separator"); // NOI18N

        numHabCompLabel.setFont(resourceMap.getFont("numHabCompLabel.font")); // NOI18N
        numHabCompLabel.setText(resourceMap.getString("numHabCompLabel.text")); // NOI18N
        numHabCompLabel.setName("numHabCompLabel"); // NOI18N

        numHabCompNotaLabel.setFont(resourceMap.getFont("numHabCompNotaLabel.font")); // NOI18N
        numHabCompNotaLabel.setText(resourceMap.getString("numHabCompNotaLabel.text")); // NOI18N
        numHabCompNotaLabel.setName("numHabCompNotaLabel"); // NOI18N

        compartimentosLabel.setFont(resourceMap.getFont("compartimentosLabel.font")); // NOI18N
        compartimentosLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        compartimentosLabel.setText(resourceMap.getString("compartimentosLabel.text")); // NOI18N
        compartimentosLabel.setName("compartimentosLabel"); // NOI18N

        compartimentosScrollPane.setName("compartimentosScrollPane"); // NOI18N

        compartimentosTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        compartimentosTable.setName("compartimentosTable"); // NOI18N
        compartimentosScrollPane.setViewportView(compartimentosTable);

        derechaButton.setAction(actionMap.get("tablaSiguientePaso4")); // NOI18N
        derechaButton.setToolTipText(resourceMap.getString("derechaButton.toolTipText")); // NOI18N
        derechaButton.setName("derechaButton"); // NOI18N

        izquierdaButton.setAction(actionMap.get("tablaAnteriorPaso4")); // NOI18N
        izquierdaButton.setToolTipText(resourceMap.getString("izquierdaButton.toolTipText")); // NOI18N
        izquierdaButton.setName("izquierdaButton"); // NOI18N

        javax.swing.GroupLayout compartimentosPanelLayout = new javax.swing.GroupLayout(compartimentosPanel);
        compartimentosPanel.setLayout(compartimentosPanelLayout);
        compartimentosPanelLayout.setHorizontalGroup(
            compartimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compartimentosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(compartimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(titulo4Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(titulo4Label, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                    .addComponent(numHabCompLabel)
                    .addComponent(numHabCompNotaLabel)
                    .addGroup(compartimentosPanelLayout.createSequentialGroup()
                        .addComponent(izquierdaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(compartimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(compartimentosScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                            .addComponent(compartimentosLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(derechaButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        compartimentosPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {derechaButton, izquierdaButton});

        compartimentosPanelLayout.setVerticalGroup(
            compartimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(compartimentosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(titulo4Label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(titulo4Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(numHabCompLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numHabCompNotaLabel)
                .addGap(18, 18, 18)
                .addComponent(compartimentosLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(compartimentosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(derechaButton)
                    .addComponent(izquierdaButton)
                    .addComponent(compartimentosScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                .addContainerGap())
        );

        compartimentosPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {derechaButton, izquierdaButton});

        mainPanel.add(compartimentosPanel, "4");

        Separator.setName("Separator"); // NOI18N

        cancelButton.setAction(actionMap.get("botonCancelar")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        nextButton.setAction(actionMap.get("botonSiguiente")); // NOI18N
        nextButton.setName("nextButton"); // NOI18N

        backButton.setAction(actionMap.get("botonAnterior")); // NOI18N
        backButton.setName("backButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(301, Short.MAX_VALUE)
                .addComponent(backButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextButton)
                .addGap(18, 18, 18)
                .addComponent(cancelButton)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Separator, javax.swing.GroupLayout.DEFAULT_SIZE, 574, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {backButton, cancelButton, nextButton});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Separator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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
 * Método que se realiza cuando el número marcado en el <i>spinner</i> del primer
 * paso del asistente cambia, actualizando el número de filas de la tabla inferior.
 * @param evt El evento generado.
 */
private void numeroDivSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_numeroDivSpinnerStateChanged
    // Cuando se cambia el número de divisiones en el spinner,
    // debe aparecer el mismo número de filas en la tabla
    int valor = (Integer) numeroDivSpinner.getValue();
    DefaultTableModel divisionesTablaModelo = (DefaultTableModel) divisionesTable.getModel();
    int numInicialRows = divisionesTablaModelo.getRowCount();
    divisionesTablaModelo.setRowCount(valor);
    for (int i = numInicialRows; i < valor; i++) {
        divisionesTablaModelo.setValueAt(i + 1, i, 0);
    }
}//GEN-LAST:event_numeroDivSpinnerStateChanged

    /**
     * Acción a realizar cuando se pulsa, en cualquier paso, el botón "Cancelar".
     */
    @Action
    public void botonCancelar() {
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
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        CardLayout cl = (CardLayout) (this.mainPanel.getLayout());
        String card = String.valueOf(this.pantallaActual + 1);
        if (this.pantallaActual == 1) {
            // MOSTRAR SEGUNDO PASO
            // Comprobar que todos los campos se hayan rellenado
            String comprobacion = comprobarPaso1();
            if (!comprobacion.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this.mainPanel, comprobacion, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
                return;
            }

            // Preparar el modelo de la tabla del segundo paso y generar la epidemia
            // Guardamos la unidad de tiempo como atributo de la epidemia
            this.epidemiaTemporal.setUnidadTiempo((String) this.unidadTiempoComboBox.getSelectedItem());
            // Vaciamos la tabla del paso 2 de toda información
            this.modeloTablaPaso2.setColumnCount(1);
            this.modeloTablaPaso2.setRowCount(0);
            // Comenzamos a trabajar con una población nueva, ponemos su nombre y habitantes
            Poblacion pobPaso1 = new Poblacion();
            pobPaso1.setNombre(this.nombrePobTextField.getText());
            pobPaso1.setHabitantes((Long) this.numeroHabSpinner.getValue());
            String nombreDiv;
            int numCateg;
            int numMaxCateg = 2;
            int numDivisiones = this.modeloTablaPaso1.getRowCount();
            // Si estamos modificando y ha cambiado el nº de divisiones, hay que hacer compartimentos nuevos
            if (!this.nuevosCompartimentos && this.epidemiaTemporal.getPoblacion().getDivisiones().length != numDivisiones) {
                this.nuevosCompartimentos = true;
            }
            // Y tantas divisiones como filas tenga la tabla del paso 1
            Division[] divsPaso1 = new Division[numDivisiones];
            pobPaso1.setDivisiones(divsPaso1);
            for (int i = 0; i < numDivisiones; i++) {
                // Para cada división ponemos su nombre y nº de categorías
                nombreDiv = (String) this.modeloTablaPaso1.getValueAt(i, 1);
                numCateg = (Integer) this.modeloTablaPaso1.getValueAt(i, 2);
                // Si estamos modificando y ha cambiado el nº de categorías, hay que hacer compartimentos nuevos
                if (!this.nuevosCompartimentos && this.epidemiaTemporal.getPoblacion().getDivisiones()[i].getCategorias().length != numCateg) {
                    this.nuevosCompartimentos = true;
                }
                divsPaso1[i] = new Division();
                divsPaso1[i].setNombre(nombreDiv);
                divsPaso1[i].setCategorias(new Categoria[numCateg]);
                for (int j = 0; j < numCateg; j++) {
                    divsPaso1[i].getCategorias()[j] = new Categoria();
                }
                // Añadimos una nueva columna a la tabla del paso 2 con dicho nombre
                this.modeloTablaPaso2.addColumn(nombreDiv);
                // Actualizamos el máximo de categorías
                if (numCateg > numMaxCateg) {
                    numMaxCateg = numCateg;
                }
            }
            // El máximo de categorías es el nº de líneas de la tabla del paso 2
            this.modeloTablaPaso2.setRowCount(numMaxCateg);
            this.modeloTablaPaso2.refreshCanEdit(false);
            // Marcamos como editables las que correspondan
            for (int i = 0; i < numDivisiones; i++) {
                numCateg = (Integer) this.modeloTablaPaso1.getValueAt(i, 2);
                for (int j = 0; j < numCateg; j++) {
                    this.modeloTablaPaso2.setCellEditable(j, i+1, true);
                }
            }
            this.categoriasTable.setColumnSelectionAllowed(true);
            // Primera columna: nombres, tamaño, centrado
            for (int i = 0; i < numMaxCateg; i++) {
                this.modeloTablaPaso2.setValueAt("Categoría "+(i+1), i, 0); // NOI18N
            }
            ((javax.swing.table.DefaultTableCellRenderer) categoriasTable.getCellRenderer(0, 0)).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            this.categoriasTable.getColumnModel().getColumn(0).setMinWidth(150);
            this.categoriasTable.getColumnModel().getColumn(0).setMaxWidth(150);
            this.categoriasTable.getColumnModel().getColumn(0).setResizable(false);
            // Limitamos las celdas a 64 caracteres
            javax.swing.JTextField[] formatoCeldas = new javax.swing.JTextField[numDivisiones];
            TableColumnModel categoriasTableColumnModel = this.categoriasTable.getColumnModel();
            for (int i = 0; i < numDivisiones; i++) {
                formatoCeldas[i] = new javax.swing.JTextField();
                formatoCeldas[i].setDocument(new JTextFieldLimit(64));
                categoriasTableColumnModel.getColumn(i+1).setCellEditor(new DefaultCellEditor(formatoCeldas[i]));
            }

            // Si se metieron datos de categorías en el paso 2, lo comprobamos y lo copiamos
            Poblacion pobPaso2 = this.epidemiaTemporal.getPoblacion();
            if (pobPaso2 != null) {
                if (pobPaso2.getDivisiones().length < pobPaso1.getDivisiones().length) {
                    for (int i = 0; i < pobPaso2.getDivisiones().length; i++) {
                        if (pobPaso2.getDivisiones()[i].getCategorias().length < pobPaso1.getDivisiones()[i].getCategorias().length) {
                            for (int j = 0; j < pobPaso2.getDivisiones()[i].getCategorias().length; j++) {
                                pobPaso1.getDivisiones()[i].getCategorias()[j] = pobPaso2.getDivisiones()[i].getCategorias()[j];
                                this.categoriasTable.setValueAt(pobPaso2.getDivisiones()[i].getCategorias()[j].getNombre(), j, i+1);
                            }
                        } else {
                            for (int j = 0; j < pobPaso1.getDivisiones()[i].getCategorias().length; j++) {
                                pobPaso1.getDivisiones()[i].getCategorias()[j] = pobPaso2.getDivisiones()[i].getCategorias()[j];
                                this.categoriasTable.setValueAt(pobPaso2.getDivisiones()[i].getCategorias()[j].getNombre(), j, i+1);
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < pobPaso1.getDivisiones().length; i++) {
                        if (pobPaso2.getDivisiones()[i].getCategorias().length < pobPaso1.getDivisiones()[i].getCategorias().length) {
                            for (int j = 0; j < pobPaso2.getDivisiones()[i].getCategorias().length; j++) {
                                pobPaso1.getDivisiones()[i].getCategorias()[j] = pobPaso2.getDivisiones()[i].getCategorias()[j];
                                this.categoriasTable.setValueAt(pobPaso2.getDivisiones()[i].getCategorias()[j].getNombre(), j, i+1);
                            }
                        } else {
                            for (int j = 0; j < pobPaso1.getDivisiones()[i].getCategorias().length; j++) {
                                pobPaso1.getDivisiones()[i].getCategorias()[j] = pobPaso2.getDivisiones()[i].getCategorias()[j];
                                this.categoriasTable.setValueAt(pobPaso2.getDivisiones()[i].getCategorias()[j].getNombre(), j, i+1);
                            }
                        }
                    }
                }
            }
            // Sustituimos la población de la epidemia temporal por los nuevos valores
            this.epidemiaTemporal.setPoblacion(pobPaso1);

            // Activar el botón "Anterior"
            this.backButton.setEnabled(true);
        } else if (this.pantallaActual == 2) {
            // MOSTRAR TERCER PASO
            // Primero comprobamos que los campos sean correctos
            String comprobacion = comprobarPaso2();
            if (!comprobacion.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this.mainPanel, comprobacion, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
                return;
            }

            // Guardamos los nombres de las categorías en la epidemia temporal
            Poblacion pobPaso2 = this.epidemiaTemporal.getPoblacion();
            int numDivs = pobPaso2.getDivisiones().length;
            int numCateg;
            String nombreCateg;
            Division divActual;
            for (int i = 0; i < numDivs; i++) {
                divActual = pobPaso2.getDivisiones()[i];
                numCateg = divActual.getCategorias().length;
                for (int j = 0; j < numCateg; j++) {
                    nombreCateg = (String) this.modeloTablaPaso2.getValueAt(j, i+1);
                    // Si estamos modificando y ha cambiado el nombre de alguna categoría, hay que hacer nuevos compartimentos
                    if (!this.nuevosCompartimentos && !divActual.getCategorias()[j].getNombre().equals(nombreCateg)) {
                        this.nuevosCompartimentos = true;
                    }
                    divActual.getCategorias()[j].setNombre(nombreCateg);
                }
            }

            // Preparamos las listas del paso 3 en caso de que haya más de una división
            Division[] divs = this.epidemiaTemporal.getPoblacion().getDivisiones();
            if (divs.length > 1) {
                this.listaFilasPaso3.removeAllElements();
                this.listaColumnasPaso3.removeAllElements();
                for (Division div : divs) {
                    this.listaFilasPaso3.addElement(div.getNombre());
                    this.listaColumnasPaso3.addElement(div.getNombre());
                }
                this.divFilasList.setSelectedIndex(0);
                this.divColumList.setSelectedIndex(1);
            } else {
                // En caso contrario, tenemos que preparar aquí la única tabla del paso 4
                this.tablasPaso4 = new DelphSimTableModel[1];
                this.tablasPaso4[0] = new DelphSimTableModel();
                int numCategPaso4 = divs[0].getCategorias().length;
                Object[] unicaFila = new Object[numCategPaso4];
                for (int i = 0; i < numCategPaso4; i++) {
                    this.tablasPaso4[0].addColumn(divs[0].getCategorias()[i].getNombre());
                    if (this.nuevosCompartimentos) {
                        unicaFila[i] = 0;
                    } else {
                        unicaFila[i] = this.epidemiaTemporal.getCompartimentos()[i].getHabitantes();
                    }
                }
                this.tablasPaso4[0].addRow(unicaFila);
                this.tablasPaso4[0].refreshCanEdit(true);
                this.compartimentosTable.setModel(tablasPaso4[0]);
                this.compartimentosTable.setColumnSelectionAllowed(true);
                ((javax.swing.table.DefaultTableCellRenderer) compartimentosTable.getCellRenderer(0, 0)).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                this.compartimentosLabel.setText(""); // NOI18N
                this.izquierdaButton.setEnabled(false);
                this.izquierdaButton.setToolTipText(resourceMap.getString("izquierdaButton.toolTipText")); // NOI18N
                this.derechaButton.setEnabled(false);
                this.derechaButton.setToolTipText(resourceMap.getString("derechaButton.toolTipText")); // NOI18N

                // Creamos los compartimentos (sólo si hace falta)
                if (this.nuevosCompartimentos) {
                    String[] nombreCompartimentos = this.epidemiaTemporal.combinarCategorias(null);
                    Compartimento[] compartimentos = new Compartimento[nombreCompartimentos.length];
                    for (int i = 0; i < nombreCompartimentos.length; i++) {
                        compartimentos[i] = new Compartimento();
                        compartimentos[i].setNombre(nombreCompartimentos[i]);
                    }
                    this.epidemiaTemporal.setCompartimentos(compartimentos);
                }

                // Y saltamos el paso 3
                this.pantallaActual++;
                card = String.valueOf(this.pantallaActual + 1);
                this.nextButton.setText(resourceMap.getString("botonSiguiente.text2")); // NOI18N
            }
        } else if (this.pantallaActual == 3) {
            // MOSTRAR CUARTO PASO
            // Primero comprobamos que los campos sean correctos
            int porFilas = this.divFilasList.getSelectedIndex();
            int porColumnas = this.divColumList.getSelectedIndex();
            if (porFilas == porColumnas) {
                javax.swing.JOptionPane.showMessageDialog(this.mainPanel, resourceMap.getString("comprobacion.error.paso3"), resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
                return;
            }

            // Ahora se crean todos los compartimentos
            String[] nombreCompartimentos = this.epidemiaTemporal.combinarCategorias(null);
            if (this.nuevosCompartimentos) {
                Compartimento[] compartimentos = new Compartimento[nombreCompartimentos.length];
                for (int i = 0; i < nombreCompartimentos.length; i++) {
                    compartimentos[i] = new Compartimento();
                    compartimentos[i].setNombre(nombreCompartimentos[i]);
                }
                this.epidemiaTemporal.setCompartimentos(compartimentos);
            }
            // Se calculan el nº de tablas que tendremos
            Division divFilas = this.epidemiaTemporal.getPoblacion().getDivisiones()[porFilas];
            Division divColumnas = this.epidemiaTemporal.getPoblacion().getDivisiones()[porColumnas];
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
                int numDivisiones = this.epidemiaTemporal.getPoblacion().getDivisiones().length;
                String[][] divisiones = new String[numDivisiones-2][];
                int j = 0;
                for (int i = 0; i < numDivisiones; i++) {
                    if (i != porFilas && i != porColumnas) {
                        divisiones[j] = new String[this.epidemiaTemporal.getPoblacion().getDivisiones()[i].getCategorias().length];
                        for (int k = 0; k < divisiones[j].length; k++) {
                            divisiones[j][k] = this.epidemiaTemporal.getPoblacion().getDivisiones()[i].getCategorias()[k].getNombre();
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
                this.tablasPaso4[i].refreshCanEdit(true);
                for (int j = 0; j < this.tablasPaso4[i].getRowCount(); j++) {
                    this.tablasPaso4[i].setCellEditable(j, 0, false);
                }
                for (int j = 0; j < this.tablasPaso4[i].getRowCount(); j++) {
                    for (int k = 1; k < this.tablasPaso4[i].getColumnCount(); k++) {
                        // Valor inicial cero si son nuevos compartimentos, el correspondiente si hemos cargado los existentes
                        if (this.nuevosCompartimentos) {
                            this.tablasPaso4[i].setValueAt(0, j, k);
                        } else {
                            // Buscamos el compartimento que corresponde y ponemos su valor en la celda
                            int numDivs = (Integer) this.numeroDivSpinner.getValue();
                            String nombreCompartimento = ""; // NOI18N
                            if (numDivs == 2) {
                                // Construimos la etiqueta correspondiente a ese compartimento
                                if (porFilas == 0) {
                                    nombreCompartimento = (String) this.tablasPaso4[0].getValueAt(j, 0) + "_" + this.tablasPaso4[0].getColumnName(k); // NOI18N
                                } else {
                                    nombreCompartimento = this.tablasPaso4[0].getColumnName(k) + "_" + (String) this.tablasPaso4[0].getValueAt(j, 0); // NOI18N
                                }
                                // Buscamos el compartimento con esa etiqueta y el valor de la celda [j,k] será su número de habitantes
                                for (int l = 0; l < nombreCompartimentos.length; l++) {
                                    if (nombreCompartimento.equals(nombreCompartimentos[l])) {
                                        this.tablasPaso4[i].setValueAt(this.epidemiaTemporal.getCompartimentos()[l].getHabitantes(), j, k);
                                        break;
                                    }
                                }
                            } else {
                                // Construimos la etiqueta correspondiente a ese compartimento
                                String[] tituloSplit = this.tituloTablasPaso4[i].split(" - "); // NOI18N
                                int m = 0;
                                for (int l = 0; l < numDivs; l++) {
                                    // Si la división que toca poner es la usada por filas,
                                    // poner el valor de la misma fila [j], columna 0
                                    if (porFilas == l) {
                                        nombreCompartimento += (String) this.tablasPaso4[i].getValueAt(j, 0) + "_"; // NOI18N
                                    // Si la división que toca poner es la usada por columnas,
                                    // poner el nombre de la cabecera de la columna
                                    } else if (porColumnas == l) {
                                        nombreCompartimento += this.tablasPaso4[i].getColumnName(k) + "_"; // NOI18N
                                    // En otro caso, poner el trozo del título por el que nos llegamos
                                    } else {
                                        nombreCompartimento += tituloSplit[m] + "_"; // NOI18N
                                        m++;
                                    }
                                }
                                nombreCompartimento = nombreCompartimento.substring(0, (nombreCompartimento.length()-1));
                                // Buscamos el compartimento con esa etiqueta
                                // Y el valor de la celda [j,k+1] de la tabla [i] es su número de habitantes
                                for (int l = 0; l < nombreCompartimentos.length; l++) {
                                    if (nombreCompartimento.equals(nombreCompartimentos[l])) {
                                        this.tablasPaso4[i].setValueAt(this.epidemiaTemporal.getCompartimentos()[l].getHabitantes(), j, k);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // Se pone como modelo el de la primera tabla, selección de una sola celda, centrado al medio
            this.compartimentosTable.setModel(this.tablasPaso4[0]);
            this.compartimentosTable.setColumnSelectionAllowed(true);
            ((javax.swing.table.DefaultTableCellRenderer) compartimentosTable.getCellRenderer(0, 0)).setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

            // Cambiar el texto del botón "Siguiente" por "Terminar"
            this.nextButton.setText(resourceMap.getString("botonSiguiente.text2")); // NOI18N
        } else if (this.pantallaActual == 4) {
            // TERMINAR, GUARDANDO LA EPIDEMIA
            // Primero comprobamos que los campos sean correctos
            String comprobacion = comprobarPaso4();
            if (!comprobacion.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this.mainPanel, comprobacion, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }
            /**
             * Si estamos modificando una epidemia y hemos tenido que crear
             * compartimentos nuevos, avisamos al usuario de que se perderá la
             * información de las ecuaciones de los compartimentos anteriores,
             * y que se borrará la definición de los procesos que dependieran de
             * ellos, y pedimos confirmación para continuar.
             */
            if (this.modificando && this.nuevosCompartimentos) {
                int respuesta = javax.swing.JOptionPane.showConfirmDialog(this.mainPanel, resourceMap.getString("nuevosCompartimentos.confirmacion.msg"), resourceMap.getString("nuevosCompartimentos.confirmacion.title"), javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE); // NOI18N
                if (respuesta != javax.swing.JOptionPane.YES_OPTION) {
                    // Si dice que no o cancela, no hacemos nada
                    return;
                } else {
                    // Eliminar la definición de los procesos que dependieran
                    // de los compartimentos viejos
                    for (int i = 0; i < this.procesosVinculados.size(); i++) {
                        Proceso p = this.epidemiaTemporal.getProceso(this.procesosVinculados.get(i).toString());
                        p.setTramosContinua(new TramoContinua[0]);
                    }
                    // Y las dependencias de parámetros y procesos con los compartimentos
                    for (Parametro par : this.epidemiaTemporal.getParametros()) {
                        par.setCompartimentosVinculados(new String[0]);
                    }
                    for (Proceso proc : this.epidemiaTemporal.getProcesos()) {
                        proc.setCompartimentosVinculados(new String[0]);
                    }
                }
            }
            // Luego colocamos cada número de personas en su compartimento correspondiente
            String[] nombresGenerados = this.epidemiaTemporal.combinarCategorias(null);
            int numeroCompartimentos = nombresGenerados.length;
            int numeroDivs = (Integer) this.numeroDivSpinner.getValue();
            if (numeroDivs == 1) {
                // CASO 1: una sola division
                for (int i = 0; i < numeroCompartimentos; i++) {
                    this.epidemiaTemporal.getCompartimentos()[i].setHabitantes(Long.valueOf(this.tablasPaso4[0].getValueAt(0, i).toString()));
                }
            } else if (numeroDivs == 2) {
                // CASO 2: dos divisiones (sigue siendo una sola tabla)
                String nombreCompartimento = ""; // NOI18N
                int divEnFilas = this.divFilasList.getSelectedIndex();
                int categDivFilas = (Integer) this.modeloTablaPaso1.getValueAt(divEnFilas, 2);
                int divEnColumnas = this.divColumList.getSelectedIndex();
                int categDivColumnas = (Integer) this.modeloTablaPaso1.getValueAt(divEnColumnas, 2);
                // Iterando sobre cada fila
                for (int j = 0; j < categDivFilas; j++) {
                    // Y sobre cada columna ([j,k+1] indica la celda actual)
                    for (int k = 0; k < categDivColumnas; k++) {
                        // Construimos la etiqueta correspondiente a ese compartimento
                        nombreCompartimento = ""; // NOI18N
                        if (divEnFilas == 0) {
                            nombreCompartimento = (String) this.tablasPaso4[0].getValueAt(j, 0) + "_" + this.tablasPaso4[0].getColumnName(k+1); // NOI18N
                        } else {
                            nombreCompartimento += this.tablasPaso4[0].getColumnName(k+1) + "_" + (String) this.tablasPaso4[0].getValueAt(j, 0); // NOI18N
                        }
                        // Buscamos el compartimento con esa etiqueta
                        // Y el valor de la celda [j,k+1] es su número de habitantes
                        for (int l = 0; l < numeroCompartimentos; l++) {
                            if (nombreCompartimento.equals(nombresGenerados[l])) {
                                this.epidemiaTemporal.getCompartimentos()[l].setHabitantes(Long.valueOf(this.tablasPaso4[0].getValueAt(j, k+1).toString()));
                                break;
                            }
                        }
                    }
                }
            } else {
                // CASO 3: más de 2 divisiones (más de 1 tabla)
                String[] tituloSplit;
                String nombreCompartimento = ""; // NOI18N
                int divEnFilas = this.divFilasList.getSelectedIndex();
                int categDivFilas = (Integer) this.modeloTablaPaso1.getValueAt(divEnFilas, 2);
                int divEnColumnas = this.divColumList.getSelectedIndex();
                int categDivColumnas = (Integer) this.modeloTablaPaso1.getValueAt(divEnColumnas, 2);
                int numeroTablas = this.tablasPaso4.length;
                int m = 0;
                // Para cada tabla
                for (int i = 0; i < numeroTablas; i++) {
                    // Iterando sobre cada fila
                    for (int j = 0; j < categDivFilas; j++) {
                        // Y sobre cada columna
                        // (es decir, [i] indica la tabla y [j,k+1] indica la celda)
                        for (int k = 0; k < categDivColumnas; k++) {
                            // Construimos la etiqueta correspondiente a ese compartimento
                            tituloSplit = this.tituloTablasPaso4[i].split(" - "); // NOI18N
                            nombreCompartimento = ""; // NOI18N
                            m = 0;
                            for (int l = 0; l < numeroDivs; l++) {
                                // Si la división que toca poner es la usada por filas,
                                // poner el valor de la misma fila [j], columna 0
                                if (divEnFilas == l) {
                                    nombreCompartimento += (String) this.tablasPaso4[i].getValueAt(j, 0) + "_"; // NOI18N
                                // Si la división que toca poner es la usada por columnas,
                                // poner el nombre de la cabecera de la columna
                                } else if (divEnColumnas == l) {
                                    nombreCompartimento += this.tablasPaso4[i].getColumnName(k+1) + "_"; // NOI18N
                                // En otro caso, poner el trozo del título por el que nos llegamos
                                } else {
                                    nombreCompartimento += tituloSplit[m] + "_"; // NOI18N
                                    m++;
                                }
                            }
                            nombreCompartimento = nombreCompartimento.substring(0, (nombreCompartimento.length()-1));
                            // Buscamos el compartimento con esa etiqueta
                            // Y el valor de la celda [j,k+1] de la tabla [i] es su número de habitantes
                            for (int l = 0; l < numeroCompartimentos; l++) {
                                if (nombreCompartimento.equals(nombresGenerados[l])) {
                                    this.epidemiaTemporal.getCompartimentos()[l].setHabitantes(Long.valueOf(this.tablasPaso4[i].getValueAt(j, k+1).toString()));
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Generamos los atajos que correspondan
            this.epidemiaTemporal.generarAtajos();
            
            // Añadimos los nombres de los compartimentos a las palabras reservadas
            for (int i = 0; i < this.epidemiaTemporal.getCompartimentos().length; i++) {
                this.epidemiaTemporal.getPalabrasReservadas().add(this.epidemiaTemporal.getCompartimento(i).getNombre());
            }
            
            // Establecemos la epidemia temporal como epidemia final y cerramos la ventana
            this.epidemia = this.epidemiaTemporal;
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
     * nos encontremos. Se encarga, principalmente, de mostrar el paso
     * correspondiente y guardar parte de la información para que esté
     * disponible cuando el usuario vuelva a avanzar de paso.
     */
    @Action
    public void botonAnterior() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        // Determinar el anterior paso a mostrar
        CardLayout cl = (CardLayout) (this.mainPanel.getLayout());
        String card = String.valueOf(this.pantallaActual - 1);
        if (this.pantallaActual == 4) {
            // Poner a cero la variable que indica la tabla a mostrar en paso 4
            this.tablaPaso4Actual = 0;
            // Si sólo hay una división, MOSTRAR PASO 2, else, PASO 3
            if (this.epidemiaTemporal.getPoblacion().getDivisiones().length == 1) {
                this.pantallaActual--;
                card = String.valueOf(this.pantallaActual - 1);
            }
            // No se guarda la información de los compartimentos porque sería poco eficiente y
            // si el usuario cambiase alguna división o categoría habría que borrarlo igualmente
            // Cambiar el texto del botón "Terminar" por "Siguiente"
            this.nextButton.setText(resourceMap.getString("botonSiguiente.text1")); // NOI18N
        } else if (this.pantallaActual == 3) {
            // MOSTRAR SEGUNDO PASO (no hay que hacer nada)
        } else if (this.pantallaActual == 2) {
            // MOSTRAR PRIMER PASO
            // No hace falta comprobar los campos porque ya se comprobarán del paso 2 al 3
            // Guardamos los nombres de las categorías en la epidemia temporal
            Poblacion pobPaso2 = this.epidemiaTemporal.getPoblacion();
            int numDivs = pobPaso2.getDivisiones().length;
            int numCateg;
            String nombreCateg;
            Division divActual;
            for (int i = 0; i < numDivs; i++) {
                divActual = pobPaso2.getDivisiones()[i];
                numCateg = divActual.getCategorias().length;
                for (int j = 0; j < numCateg; j++) {
                    nombreCateg = (String) this.modeloTablaPaso2.getValueAt(j, i+1);
                    // Si estamos modificando y ha cambiado el nombre de alguna categoría, hay que hacer nuevos compartimentos
                    if (!this.nuevosCompartimentos && !divActual.getCategorias()[j].getNombre().equals(nombreCateg)) {
                        this.nuevosCompartimentos = true;
                    }
                    divActual.getCategorias()[j].setNombre(nombreCateg);
                }
            }
            // Desactivar el botón "Anterior"
            backButton.setEnabled(false);
        } else if (this.pantallaActual == 1) {
            // Ya estamos en el primer paso, no hacer nada
        }
        // Mostrar el paso correspondiente
        cl.show(this.mainPanel, card);
        // Actualizar la variable del paso donde estamos (a no ser que sea el paso 1)
        this.pantallaActual = this.pantallaActual > 1 ? (--this.pantallaActual) : 1;
    }

    /**
     * Acción que se realiza en el paso 2 del asistente cuando el usuario pulsa
     * el botón de "Añadir descripción" a una categoría. Si la celda corresponde
     * realmente a una categoría, se abre un cuadro de diálogo con un campo de
     * texto para introducir la descripción.
     */
    @Action
    public void anadirDescripcionCat() {
        // En principio todos los caracteres son admitidos
        // Primero comprobamos que la celda seleccionada corresponda a una categoría
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        int columna = this.categoriasTable.getSelectedColumn();
        int fila = this.categoriasTable.getSelectedRow();
        String descripcion;
        if (columna < 0 || fila < 0) {
            return;
        }
        if (this.modeloTablaPaso2.isCellEditable(fila, columna)) {
            // Cargamos la descripción ya introducida anteriormente si la hubiera y mostramos mensaje de input
            String descInicial = this.epidemiaTemporal.getPoblacion().getDivisiones()[columna-1].getCategorias()[fila].getDescripcion();
            if (descInicial != null) {
                descripcion = javax.swing.JOptionPane.showInputDialog(resourceMap.getString("anadirDescripcion.msg"), descInicial); // NOI18N
            } else {
                descripcion = javax.swing.JOptionPane.showInputDialog(resourceMap.getString("anadirDescripcion.msg")); // NOI18N
            }
            // Si se ha introducido algo, truncamos en caso de que sea mayor de 500 caracteres y lo guardamos
            if (descripcion != null) {
                if (descripcion.length() > 500) {
                    descripcion = descripcion.substring(0, 500);
                }
                this.epidemiaTemporal.getPoblacion().getDivisiones()[columna-1].getCategorias()[fila].setDescripcion(descripcion);
            }
        }
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

    /**
     * Comprueba que los datos introducidos en el primer paso sean correctos.
     * @return  Una cadena de texto con los errores de la comprobación.
     *          Vacía si no ha habido errores.
     */
    private String comprobarPaso1() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        String errores = ""; // NOI18N
        String nombreDiv;
        String otroNombreDiv;
        int numeroCat;
        int numeroDivisiones = divisionesTable.getRowCount();
        // En principio cualquier tipo de caracter es válido
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreDivision")); // NOI18N
        java.util.regex.Matcher comprobador;

        // El campo nombre debe tener algo escrito. En principio se admiten todos los caracteres
        nombrePobTextField.setText(nombrePobTextField.getText().trim());
        if (nombrePobTextField.getText().isEmpty()) {
            errores += resourceMap.getString("comprobacion.error.paso1.nombrePoblacion"); // NOI18N
        }

        // Para cada línea (cada division)
        for (int i = 0; i < numeroDivisiones; i++) {
            // Comprobar el nombre
            if (divisionesTable.getValueAt(i, 1) == null) {
                errores += resourceMap.getString("comprobacion.error.paso1.nombreDivision.vacio", (i+1)); // NOI18N
            } else {
                nombreDiv = ((String) divisionesTable.getValueAt(i, 1)).trim();
                divisionesTable.setValueAt(nombreDiv, i, 1);
                if (nombreDiv.isEmpty()) {
                    errores += resourceMap.getString("comprobacion.error.paso1.nombreDivision.vacio", (i+1)); // NOI18N
                } else {
                    // Comprobar el nombre de las divisiones con el patrón cargado
                    comprobador = patron.matcher(nombreDiv);
                    if (!comprobador.find()) {
                        errores += resourceMap.getString("comprobacion.error.paso1.nombreDivision.invalido", (i+1)); // NOI18N
                    }
                    // Y también que dos divisiones no se llamen igual
                    for (int j = i+1; j < numeroDivisiones; j++) {
                        if (divisionesTable.getValueAt(j, 1) != null) {
                            otroNombreDiv = (String) divisionesTable.getValueAt(j, 1);
                            if (nombreDiv.equals(otroNombreDiv)) {
                                errores += resourceMap.getString("comprobacion.error.paso1.nombreDivision.repetido", (i+1), (j+1)); // NOI18N
                            }
                        }
                    }
                }
            }
            // Y el número de categorías
            if (divisionesTable.getValueAt(i, 2) == null) {
                errores += resourceMap.getString("comprobacion.error.paso1.numeroCategorias.vacio", (i+1)); // NOI18N
            } else {
                numeroCat = (Integer) divisionesTable.getValueAt(i, 2);
                if (numeroCat < 2) {
                    errores += resourceMap.getString("comprobacion.error.paso1.numeroCategorias.invalido", (i+1)); // NOI18N
                }
            }
        }
        return errores;
    }

    /**
     * Comprueba que los datos introducidos en el segundo paso sean correctos.
     * @return  Una cadena de texto con los errores de la comprobación.
     *          Vacía si no ha habido errores.
     */
    private String comprobarPaso2() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        String errores = ""; // NOI18N
        // Primer caracter letra (a-zA-Z) y el resto alfanumérico (a-zA-Z0-9)
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreCategoria")); // NOI18N
        java.util.regex.Matcher comprobador;
        String nombreCateg;
        java.util.Vector todasCateg = new java.util.Vector();
        int numFilas = this.modeloTablaPaso2.getRowCount();
        int numColumnas = this.modeloTablaPaso2.getColumnCount();

        for (int i = 0; i < numFilas; i++) {
            for (int j = 0; j < numColumnas; j++) {
                // Sólo hay que comprobar las celdas de las categorías, es decir, las editables
                if (this.modeloTablaPaso2.isCellEditable(i, j)) {
                    if (this.modeloTablaPaso2.getValueAt(i, j) == null) {
                        // El nombre de cada categoría es obligatorio
                        errores += resourceMap.getString("comprobacion.error.paso2.nombreCategoria.invalido", (i+1), this.modeloTablaPaso2.getColumnName(j)); // NOI18N
                    } else {
                        // Comprobar el nombre de las divisiones con el patrón cargado
                        nombreCateg = this.modeloTablaPaso2.getValueAt(i, j).toString();
                        comprobador = patron.matcher(nombreCateg);
                        if (!comprobador.find()) {
                            errores += resourceMap.getString("comprobacion.error.paso2.nombreCategoria.invalido", (i+1), this.modeloTablaPaso2.getColumnName(j)); // NOI18N
                        } else {
                            // Que esa categoría no se haya definido ya
                            if (todasCateg.contains(nombreCateg)) {
                                errores += resourceMap.getString("comprobacion.error.paso2.nombreCategoria.repetido", (i+1), this.modeloTablaPaso2.getColumnName(j)); // NOI18N
                            }
                            todasCateg.add(nombreCateg);
                            // Y además que no esté siendo usado por un parámetro o un proceso
                            Class clase = this.epidemiaTemporal.estaDefinido(nombreCateg);
                            if (clase == Parametro.class) {
                                errores += resourceMap.getString("comprobacion.error.paso2.nombreCategoria.usado", (i+1), this.modeloTablaPaso2.getColumnName(j), "un parámetro"); // NOI18N
                            } else if (clase == Proceso.class) {
                                errores += resourceMap.getString("comprobacion.error.paso2.nombreCategoria.usado", (i+1), this.modeloTablaPaso2.getColumnName(j), "un proceso"); // NOI18N
                            } else if (clase == org.nfunk.jep.function.PostfixMathCommand.class) {
                                errores += resourceMap.getString("comprobacion.error.paso2.nombreCategoria.usado", (i+1), this.modeloTablaPaso2.getColumnName(j), "una función"); // NOI18N
                            }
                        }
                        
                    }
                }
            }
        }

        return errores;
    }

    /**
     * Comprueba que los datos introducidos en el tercer paso sean correctos.
     * @return  Una cadena de texto con los errores de la comprobación.
     *          Vacía si no ha habido errores.
     */
    private String comprobarPaso4() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        String errores = ""; // NOI18N
        // Este patrón significa que sólo se aceptan números enteros mayores de cero
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.numeroPersonas")); // NOI18N
        java.util.regex.Matcher comprobador;
        long sumaPersonas = 0;
        long numPersonas = (Long) this.numeroHabSpinner.getValue();
        int numDivisiones = divisionesTable.getRowCount();
        int numTablas = this.tablasPaso4.length;
        int numFilas = this.tablasPaso4[0].getRowCount();

        if (numDivisiones == 1) {
            // Si sólo tenemos una división, hay una única tabla con una fila
            int numColumnas = this.tablasPaso4[0].getColumnCount();
            for (int k = 0; k < numColumnas; k++) {
                // Comprobar el nombre de las divisiones con el patrón cargado
                comprobador = patron.matcher(this.tablasPaso4[0].getValueAt(0, k).toString());
                if (!comprobador.find()) {
                    errores += resourceMap.getString("comprobacion.error.paso4.conUnaDiv", (k+1)); // NOI18N
                } else {
                    sumaPersonas += Long.valueOf(this.tablasPaso4[0].getValueAt(0, k).toString());
                }
            }
        } else if (numDivisiones == 2) {
            // Si tenemos dos divisiones, una sola tabla con un número indeterminado de filas
            int numColumnas = this.tablasPaso4[0].getColumnCount() - 1;
            for (int j = 0; j < numFilas; j++) {
                for (int k = 0; k < numColumnas; k++) {
                    // Comprobar el nombre de las divisiones con el patrón cargado
                    comprobador = patron.matcher(this.tablasPaso4[0].getValueAt(j, k+1).toString());
                    if (!comprobador.find()) {
                        errores += resourceMap.getString("comprobacion.error.paso4.conDosDivs", (j+1), (k+1)); // NOI18N
                    } else {
                        sumaPersonas += Long.valueOf(this.tablasPaso4[0].getValueAt(j, k+1).toString());
                    }
                }
            }
        } else {
            // Si tenemos más de dos divisiones, tendremos varias tablas iguales
            int numColumnas = this.tablasPaso4[0].getColumnCount() - 1;
            for (int i = 0; i < numTablas; i++) {
                for (int j = 0; j < numFilas; j++) {
                    for (int k = 0; k < numColumnas; k++) {
                        // Comprobar el nombre de las divisiones con el patrón cargado
                        comprobador = patron.matcher(this.tablasPaso4[i].getValueAt(j, k+1).toString());
                        if (!comprobador.find()) {
                            errores += resourceMap.getString("comprobacion.error.paso4.conMasDivs", (j+1), (k+1), this.tituloTablasPaso4[i]); // NOI18N
                        } else {
                            sumaPersonas += Long.valueOf(this.tablasPaso4[i].getValueAt(j, k+1).toString());
                        }
                    }
                }
            }
        }
        if (errores.equals("") && sumaPersonas != numPersonas) { // NOI18N
            errores += resourceMap.getString("comprobacion.error.paso4.sumaTotal", numPersonas, sumaPersonas); // NOI18N
        }

        return errores;
    }

    /**
     * Método para obtener la epidemia creada por este objeto.
     * @return La epidemia creada.
     */
    public Epidemia getEpidemia() {
        return this.epidemia;
    }

    /**
     * Método para rellenar los campos de la interfaz una vez creados con los
     * datos de la epidemia a modificar.
     * @param epi La epidemia cuyos datos vamos a cargar en la interfaz, además
     *            clonarla para poder modificarla.
     */
    private void cargarEpidemia(Epidemia epi) {
        // Nos quedamos con los procesos vinculados de los compartimentos viejos
        // por si los sustituyéramos, vaciar la definición de dichos procesos
        for (int i = 0; i < epi.getCompartimentos().length; i++) {
            String[] pvinc = epi.getCompartimento(i).getProcesosVinculados();
            for (int j = 0; j < pvinc.length; j++) {
                if (!this.procesosVinculados.contains(pvinc[j])) {
                    this.procesosVinculados.add(pvinc[j]);
                }
            }
        }
        // Rellenar los campos del paso 1
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DistribucionPoblacion.class);
        this.setTitle(resourceMap.getString("Form.title.modificar")); // NOI18N
        this.nombrePobTextField.setText(epi.getPoblacion().getNombre());
        this.numeroHabSpinner.setValue(epi.getPoblacion().getHabitantes());
        this.unidadTiempoComboBox.setSelectedItem(epi.getUnidadTiempo());
        Division[] divs = epi.getPoblacion().getDivisiones();
        this.numeroDivSpinner.setValue(divs.length);
        for (int i = 0; i < divs.length; i++) {
            this.modeloTablaPaso1.setValueAt(divs[i].getNombre(), i, 1);
            this.modeloTablaPaso1.setValueAt(divs[i].getCategorias().length, i, 2);
        }
        // Y clonar la epidemia pasada como parámetro a la propia de esta clase
        this.epidemiaTemporal = epi.clone();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSeparator Separator;
    private javax.swing.JTextPane aclaracionTextPane;
    private javax.swing.JButton backButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel categoriasPanel;
    private javax.swing.JScrollPane categoriasScrollPane;
    private javax.swing.JTable categoriasTable;
    private javax.swing.JTextPane categoriasTextPane;
    private javax.swing.JLabel compartimentosLabel;
    private javax.swing.JPanel compartimentosPanel;
    private javax.swing.JScrollPane compartimentosScrollPane;
    private javax.swing.JTable compartimentosTable;
    private javax.swing.JButton derechaButton;
    private javax.swing.JButton descripcionButton;
    private javax.swing.JTextPane descripcionTextPane;
    private javax.swing.JLabel divColumLabel;
    private javax.swing.JList divColumList;
    private javax.swing.JScrollPane divColumScrollPane;
    private javax.swing.JLabel divFilasLabel;
    private javax.swing.JList divFilasList;
    private javax.swing.JScrollPane divFilasScrollPane;
    private javax.swing.JScrollPane divisionesScrollPane;
    private javax.swing.JTable divisionesTable;
    private javax.swing.JPanel fijarDivisionesPanel;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JButton izquierdaButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton nextButton;
    private javax.swing.JLabel nombrePobLabel;
    private javax.swing.JTextField nombrePobTextField;
    private javax.swing.JLabel numHabCompLabel;
    private javax.swing.JLabel numHabCompNotaLabel;
    private javax.swing.JLabel numeroDivLabel;
    private javax.swing.JSpinner numeroDivSpinner;
    private javax.swing.JLabel numeroHabLabel;
    private javax.swing.JSpinner numeroHabSpinner;
    private javax.swing.JLabel titulo1Label;
    private javax.swing.JSeparator titulo1Separator;
    private javax.swing.JLabel titulo2Label;
    private javax.swing.JSeparator titulo2Separator;
    private javax.swing.JLabel titulo3Label;
    private javax.swing.JSeparator titulo3Separator;
    private javax.swing.JLabel titulo4Label;
    private javax.swing.JSeparator titulo4Separator;
    private javax.swing.JComboBox unidadTiempoComboBox;
    private javax.swing.JLabel unidadTiempoLabel;
    // End of variables declaration//GEN-END:variables

}
