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

import org.jdesktop.application.Action;
import org.nfunk.jep.*;

/**
 * Interfaz de usuario para añadir o modificar un parámetro seleccionado con
 * la interfaz proporcionada por GestionarVariables.java
 * @author Víctor E. Tamames Gómez
 */
public class GestionarParametro extends javax.swing.JDialog {

    /**
     * Flag que toma el valor verdadero si estamos modificando un parámetro,
     * falso en caso contrario.
     */
    private boolean modificando = false;
    
    /**
     * Referencia a la epidemia a la cual pertenecen los parámetros.
     */
    private Epidemia epidemia;
    
    /**
     * Índice del parámetro con el que tenemos que tratar.
     */
    private int numParametro;

    /**
     * Crea una nueva ventana de esta clase.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia con la que vamos a tratar.
     * @param indice El índice del parámetro con el que vamos a tratar.
     */
    public GestionarParametro(java.awt.Frame parent, boolean modal, Epidemia epi, int indice) {
        super(parent, modal);
        initComponents();
        // Poner tamaño máximo 64 caracteres al jTextField del nombre
        this.nombreTextField.setDocument(new JTextFieldLimit(64));
        // Y máximo 500 a la descripción
        this.descripcionTextField.setDocument(new JTextFieldLimit(500));
        
        this.epidemia = epi;

        // Si se ha pasado un parámetro, hay que modificarlo
        if (indice != -1) {
            this.numParametro = indice;
            cargarParametro();
            this.modificando = true;
        } else {
            this.numParametro = this.epidemia.getParametros().length;
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
        tituloLabel = new javax.swing.JLabel();
        tituloSeparator = new javax.swing.JSeparator();
        definicionTabbedPane = new javax.swing.JTabbedPane();
        generalPanel = new javax.swing.JPanel();
        aclaracionTextPane = new javax.swing.JTextPane();
        nombreLabel = new javax.swing.JLabel();
        nombreTextField = new javax.swing.JTextField();
        descripcionLabel = new javax.swing.JLabel();
        descripcionTextField = new javax.swing.JTextField();
        continuaPanel = new javax.swing.JPanel();
        defContinuaLabel = new javax.swing.JLabel();
        defContinuaTextField = new javax.swing.JTextField();
        aclaracionDefTextPane = new javax.swing.JTextPane();
        botonesSeparator = new javax.swing.JSeparator();
        cancelarButton = new javax.swing.JButton();
        aceptarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarParametro.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        mainPanel.setName("mainPanel"); // NOI18N

        tituloLabel.setFont(resourceMap.getFont("tituloLabel.font")); // NOI18N
        tituloLabel.setText(resourceMap.getString("tituloLabel.text")); // NOI18N
        tituloLabel.setName("tituloLabel"); // NOI18N

        tituloSeparator.setName("tituloSeparator"); // NOI18N

        definicionTabbedPane.setName("definicionTabbedPane"); // NOI18N

        generalPanel.setMinimumSize(new java.awt.Dimension(407, 157));
        generalPanel.setName("generalPanel"); // NOI18N
        generalPanel.setPreferredSize(new java.awt.Dimension(407, 157));

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
                    .addComponent(descripcionLabel)
                    .addComponent(nombreTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(descripcionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(aclaracionTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(nombreLabel))
                .addContainerGap())
        );
        generalPanelLayout.setVerticalGroup(
            generalPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aclaracionTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descripcionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(descripcionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        definicionTabbedPane.addTab(resourceMap.getString("generalPanel.TabConstraints.tabTitle"), generalPanel); // NOI18N

        continuaPanel.setMinimumSize(new java.awt.Dimension(407, 157));
        continuaPanel.setName("continuaPanel"); // NOI18N
        continuaPanel.setPreferredSize(new java.awt.Dimension(407, 157));

        defContinuaLabel.setText(resourceMap.getString("defContinuaLabel.text")); // NOI18N
        defContinuaLabel.setName("defContinuaLabel"); // NOI18N

        defContinuaTextField.setToolTipText(null);
        defContinuaTextField.setName("defContinuaTextField"); // NOI18N

        aclaracionDefTextPane.setBackground(this.getBackground());
        aclaracionDefTextPane.setBorder(null);
        aclaracionDefTextPane.setEditable(false);
        aclaracionDefTextPane.setText(resourceMap.getString("aclaracionDefTextPane.text")); // NOI18N
        aclaracionDefTextPane.setName("aclaracionDefTextPane"); // NOI18N

        javax.swing.GroupLayout continuaPanelLayout = new javax.swing.GroupLayout(continuaPanel);
        continuaPanel.setLayout(continuaPanelLayout);
        continuaPanelLayout.setHorizontalGroup(
            continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, continuaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(defContinuaTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(aclaracionDefTextPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                    .addComponent(defContinuaLabel, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        continuaPanelLayout.setVerticalGroup(
            continuaPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(continuaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(aclaracionDefTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(defContinuaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(defContinuaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        definicionTabbedPane.addTab(resourceMap.getString("continuaPanel.TabConstraints.tabTitle"), continuaPanel); // NOI18N

        botonesSeparator.setName("botonesSeparator"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(GestionarParametro.class, this);
        cancelarButton.setAction(actionMap.get("cancelar")); // NOI18N
        cancelarButton.setName("cancelarButton"); // NOI18N

        aceptarButton.setAction(actionMap.get("aceptar")); // NOI18N
        aceptarButton.setName("aceptarButton"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(definicionTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tituloSeparator, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                            .addComponent(tituloLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE))
                        .addGap(15, 15, 15))
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 256, Short.MAX_VALUE)
                        .addComponent(aceptarButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelarButton)
                        .addContainerGap())
                    .addGroup(mainPanelLayout.createSequentialGroup()
                        .addComponent(botonesSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                        .addContainerGap())))
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {aceptarButton, cancelarButton});

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tituloLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tituloSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(definicionTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(botonesSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelarButton)
                    .addComponent(aceptarButton))
                .addContainerGap())
        );

        mainPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {aceptarButton, cancelarButton});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Cancelar". Cierra la
     * ventana sin hacer cambio alguno sobre la epidemia.
     */
    @Action
    public void cancelar() {
        // Cerrar la ventana
        dispose();
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Aceptar". Comprueba
     * si los datos introducidos son válidos y crea/modifica un parámetro con
     * ellos en caso afirmativo, cerrando después la ventana. En caso negativo,
     * muestra el mensaje de error correspondiente.
     */
    @Action
    public void aceptar() {
        // Creamos variables necesarias, comprobamos errores
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarParametro.class);
        java.util.regex.Pattern patronFunciones = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoFunciones")); // NOI18N
        java.util.regex.Pattern patronElementos = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoElementos")); // NOI18N
        java.util.regex.Matcher comprobador;
        String errores = comprobar();
        errores += comprobarDefContinua();
        // Si hay errores, se muestra mensaje de error; si no, se actúa en función de si estamos modificando o no
        if (!errores.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this.mainPanel, errores, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
            return;
        } else {
            if (this.modificando) {
                // Creamos el parámetro con la información básica, guardando su nombre anterior
                Parametro modificandoParametro = this.epidemia.getParametro(this.numParametro);
                String nombreAnterior = modificandoParametro.getNombre();
                modificandoParametro.setNombre(this.nombreTextField.getText());
                modificandoParametro.setDescripcion(this.descripcionTextField.getText());
                modificandoParametro.setDefinicionContinua(this.defContinuaTextField.getText());
                // Eliminamos el uso de funciones de la cadena de la ecuación
                // para no confundirlas con el nombre de otros parámetros
                String ecuacion = this.defContinuaTextField.getText();
                comprobador = patronFunciones.matcher(ecuacion);
                while (comprobador.find()) {
                    String principio = ecuacion.substring(0, comprobador.start());
                    ecuacion = principio + ecuacion.substring(comprobador.end());
                    comprobador = patronFunciones.matcher(ecuacion);
                }
                // Eliminamos todos los posibles vínculos anteriores de otros parámetros con éste
                for (int i = 0; i < this.numParametro; i++) {
                    this.epidemia.getParametro(i).eliminarParametroVinculado(nombreAnterior);
                }
                // Buscamos referencias a otros parámetros usados y añadimos los vínculos
                comprobador = patronElementos.matcher(ecuacion);
                while (comprobador.find()) {
                    this.epidemia.getParametro(comprobador.group()).anadirParametroVinculado(this.nombreTextField.getText());
                }
                // Si hemos cambiado el nombre, habrá que revisar todos los elementos que dependen de él
                // y actualizar sus ecuaciones reemplazando su nombre viejo por el nuevo.
                // Nota: replaceAll no serviría (ej: my -> mu .. my1 -> mu1), y eso no es así.
                if (!nombreAnterior.equals(this.nombreTextField.getText())) {
                    java.util.regex.Pattern patron;
                    String eNueva;
                    // Para cada parámetro vinculado
                    for (int i = 0; i < modificandoParametro.getParametrosVinculados().length; i++) {
                        Parametro parVinc = this.epidemia.getParametro(modificandoParametro.getParametrosVinculados()[i]);
                        eNueva = parVinc.getDefinicionContinua();
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
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreParametro")); // NOI18N
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
                        parVinc.setDefinicionContinua(eNueva);
                    }
                    // Para cada tramo de cada proceso vinculado
                    for (int i = 0; i < modificandoParametro.getProcesosVinculados().length; i++) {
                        Proceso procVinc = this.epidemia.getProceso(modificandoParametro.getProcesosVinculados()[i]);
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
                            patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreParametro")); // NOI18N
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
                    for (int i = 0; i < modificandoParametro.getCompartimentosVinculados().length; i++) {
                        Compartimento compVinc = this.epidemia.getCompartimento(modificandoParametro.getCompartimentosVinculados()[i]);
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
                        patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreParametro")); // NOI18N
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
                // Creamos el parámetro con la información básica
                Parametro nuevoParametro = new Parametro();
                nuevoParametro.setNombre(this.nombreTextField.getText());
                nuevoParametro.setDescripcion(this.descripcionTextField.getText());
                nuevoParametro.setDefinicionContinua(this.defContinuaTextField.getText());
                // Eliminamos el uso de funciones de la cadena de la ecuación
                // para no confundirlas con el nombre de otros parámetros
                String ecuacion = this.defContinuaTextField.getText();
                comprobador = patronFunciones.matcher(ecuacion);
                while (comprobador.find()) {
                    String principio = ecuacion.substring(0, comprobador.start());
                    ecuacion = principio + ecuacion.substring(comprobador.end());
                    comprobador = patronFunciones.matcher(ecuacion);
                }
                // Buscamos referencias a otros parámetros usados y añadimos los vínculos
                comprobador = patronElementos.matcher(ecuacion);
                while (comprobador.find()) {
                    this.epidemia.getParametro(comprobador.group()).anadirParametroVinculado(this.nombreTextField.getText());
                }
                // Añadimos el parámetro a la epidemia y su nombre como reservado
                this.epidemia.anadirParametro(nuevoParametro);
                this.epidemia.getPalabrasReservadas().add(this.nombreTextField.getText());
            }
            // Por último, cerramos la ventana e indicamos modificaciones
            this.modificado = true;
            dispose();
        }
    }
    
    /**
     * Comprueba los campos básicos del parámetro que se está
     * añadiendo/modificando (nombre y descripción).
     * @return Una cadena de caracteres con los errores detectados. Vacía si no
     *         ha habido.
     */
    public String comprobar() {
        // Variables que vamos a usar
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarParametro.class);
        java.util.regex.Pattern patron = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.nombreParametro")); // NOI18N
        java.util.regex.Matcher comprobador;
        String errores = ""; // NOI18N
        
        // Comprobar que el nombre es válido
        comprobador = patron.matcher(this.nombreTextField.getText());
        if (!comprobador.find()) {
            errores += resourceMap.getString("comprobacion.error.nombreInvalido"); // NOI18N
        } else {
            // Comprobar que el nombre no esté siendo usado
            // Tanto si no estamos modificando como si, aún modificando, el nombre original difiere del final
            if (!(this.modificando && this.nombreTextField.getText().equals(this.epidemia.getParametro(this.numParametro).getNombre()))) {
                // Si el nombre está siendo usado (mirar tanto en palabras reservadas como en atajos)
                if (this.epidemia.getPalabrasReservadas().contains(this.nombreTextField.getText()) || this.epidemia.getPalabrasAtajos().contains(this.nombreTextField.getText())) {
                    errores += resourceMap.getString("comprobacion.error.nombreUsado"); // NOI18N
                }
            }
        }
        return errores;
    }
    
    /**
     * Comprueba los campos referidos al valor para simulaciones contínuas del
     * parámetro que se está añadiendo/modificando.
     * @return Una cadena de caracteres con los errores detectados. Vacía si no
     *         ha habido.
     */
    public String comprobarDefContinua() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarParametro.class);
        String errores = ""; // NOI18N
        
        // Puesto que el campo se puede dejar en blanco, sólo se comprueba si se ha escrito algo
        if (!this.defContinuaTextField.getText().isEmpty()) {
            // Si la cadena contiene algún punto y coma -> inválida
            if (this.defContinuaTextField.getText().contains(";")) {
                errores += resourceMap.getString("comprobacion.error.defContinuaInvalida"); // NOI18N
                return errores;
            }
            // Un nuevo analizador sintáctico
            JEP jep = Epidemia.CrearDelphSimJEP();
            // Añadimos todos los parámetros que pueden usarse en la definición de éste
            for (int i = 0; i < this.numParametro; i++) {
                jep.addVariable(this.epidemia.getParametro(i).getNombre(), 1);
            }
            // Analizar la función introducida
            Node funcion = jep.parseExpression(this.defContinuaTextField.getText());
            try {
                jep.evaluate(funcion);
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
                errores += resourceMap.getString("comprobacion.error.defContinuaInvalida"); // NOI18N
            }
        }
        
        return errores;
    }

    /**
     * Método que se encarga de rellenar los campos con los datos del parámetro
     * que se vaya a modificar.
     */
    private void cargarParametro() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarParametro.class);
        this.setTitle(resourceMap.getString("Form.title.modificar", this.epidemia.getParametro(this.numParametro).getNombre())); // NOI18N
        this.tituloLabel.setText(resourceMap.getString("tituloLabel.text.modificar", this.epidemia.getParametro(this.numParametro).getNombre())); // NOI18N
        this.nombreTextField.setText(this.epidemia.getParametro(this.numParametro).getNombre());
        this.descripcionTextField.setText(this.epidemia.getParametro(this.numParametro).getDescripcion());
        this.defContinuaTextField.setText(this.epidemia.getParametro(this.numParametro).getDefinicionContinua());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarButton;
    private javax.swing.JTextPane aclaracionDefTextPane;
    private javax.swing.JTextPane aclaracionTextPane;
    private javax.swing.JSeparator botonesSeparator;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JPanel continuaPanel;
    private javax.swing.JLabel defContinuaLabel;
    private javax.swing.JTextField defContinuaTextField;
    private javax.swing.JTabbedPane definicionTabbedPane;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JTextField descripcionTextField;
    private javax.swing.JPanel generalPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nombreLabel;
    private javax.swing.JTextField nombreTextField;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JSeparator tituloSeparator;
    // End of variables declaration//GEN-END:variables
    protected boolean modificado = false;
}
