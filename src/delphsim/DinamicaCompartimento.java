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

import org.jdesktop.application.Action;
import org.nfunk.jep.*;

/**
 * Interfaz de usuario creada a través del asistente DinamicaPoblacion.java
 * que permite definir la dinámica de un compartimento.
 * @author Víctor E. Tamames Gómez
 */
public class DinamicaCompartimento extends javax.swing.JDialog {

    /**
     * El compartimento con el que vamos a trabajar
     */
    private Compartimento compartimento;
    
    /**
     * La epidemia cuya población vamos a modelar.
     */
    private Epidemia epidemia;
    
    /**
     * Crea una nueva ventana de DinamicaPoblacion.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia con la que vamos a tratar.
     * @param nombreCompartimento El nombre del compartimento con el que vamos a tratar.
     */
    public DinamicaCompartimento(java.awt.Frame parent, boolean modal, Epidemia epi, String nombreCompartimento) {
        super(parent, modal);
        initComponents();
        
        // Iniciar títulos y parámetros de creación
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaCompartimento.class);
        this.setTitle(resourceMap.getString("Form.titleConNombre", nombreCompartimento));
        this.tituloLabel.setText(resourceMap.getString("tituloConNombre.text", nombreCompartimento));
        this.epidemia = epi;
        this.compartimento = this.epidemia.getCompartimento(nombreCompartimento);
        
        // Si hay algo ya definido para este compartimento, mostrarlo
        cargarCompartimento();

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
        exp1TextPane = new javax.swing.JTextPane();
        definicionLabel = new javax.swing.JLabel();
        definicionTextField = new javax.swing.JTextField();
        buttonSeparator = new javax.swing.JSeparator();
        cancelarButton = new javax.swing.JButton();
        aceptarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaCompartimento.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setMinimumSize(new java.awt.Dimension(480, 280));
        setName("Form"); // NOI18N

        tituloLabel.setFont(resourceMap.getFont("tituloLabel.font")); // NOI18N
        tituloLabel.setText(resourceMap.getString("tituloLabel.text")); // NOI18N
        tituloLabel.setName("tituloLabel"); // NOI18N

        tituloSeparator.setName("tituloSeparator"); // NOI18N

        expTextPane.setBackground(this.getBackground());
        expTextPane.setBorder(null);
        expTextPane.setEditable(false);
        expTextPane.setFont(resourceMap.getFont("expTextPane.font")); // NOI18N
        expTextPane.setText(resourceMap.getString("expTextPane.text")); // NOI18N
        expTextPane.setName("expTextPane"); // NOI18N

        exp1TextPane.setBackground(this.getBackground());
        exp1TextPane.setBorder(null);
        exp1TextPane.setEditable(false);
        exp1TextPane.setFont(resourceMap.getFont("exp1TextPane.font")); // NOI18N
        exp1TextPane.setText(resourceMap.getString("exp1TextPane.text")); // NOI18N
        exp1TextPane.setName("exp1TextPane"); // NOI18N

        definicionLabel.setText(resourceMap.getString("definicionLabel.text")); // NOI18N
        definicionLabel.setName("definicionLabel"); // NOI18N

        definicionTextField.setName("definicionTextField"); // NOI18N

        buttonSeparator.setName("buttonSeparator"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(DinamicaCompartimento.class, this);
        cancelarButton.setAction(actionMap.get("cancelar")); // NOI18N
        cancelarButton.setName("cancelarButton"); // NOI18N

        aceptarButton.setAction(actionMap.get("aceptar")); // NOI18N
        aceptarButton.setName("aceptarButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(tituloLabel)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(aceptarButton)
                        .addGap(18, 18, 18)
                        .addComponent(cancelarButton))
                    .addComponent(buttonSeparator, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(definicionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(definicionLabel)
                    .addComponent(exp1TextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
                    .addComponent(expTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(expTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exp1TextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(definicionLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(definicionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(buttonSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
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
     * si los datos introducidos son válidos y actualiza la definición del
     * compartimento en caso afirmativo, cerrando después la ventana. En caso
     * negativo, muestra el mensaje de error correspondiente.
     */
    @Action
    public void aceptar() {
        // Creamos variables necesarias, comprobamos errores
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaCompartimento.class);
        java.util.regex.Pattern patronFunciones = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoFunciones")); // NOI18N
        java.util.regex.Pattern patronElementos = java.util.regex.Pattern.compile(resourceMap.getString("comprobacion.patron.usoElementos")); // NOI18N
        java.util.regex.Matcher comprobador;
        String errores = comprobarDefContinua();
        // Si hay errores, se muestra mensaje de error; si no, se actúa en función de si estamos modificando o no
        if (!errores.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(this, errores, resourceMap.getString("comprobacion.error.title"), javax.swing.JOptionPane.ERROR_MESSAGE); // NOI18N
            return;
        } else {
            // Actualizamos la definición del compartimento
            this.compartimento.setDefinicionContinua(this.definicionTextField.getText());
            // Eliminamos todos los posibles vínculos anteriores de otros elementos con éste
            for (int i = 0; i < this.epidemia.getParametros().length; i++) {
                this.epidemia.getParametro(i).eliminarCompartimentoVinculado(this.compartimento.getNombre());
            }
            for (int i = 0; i < this.epidemia.getProcesos().length; i++) {
                this.epidemia.getProceso(i).eliminarCompartimentoVinculado(this.compartimento.getNombre());
            }
            for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                this.epidemia.getCompartimento(i).eliminarCompartimentoVinculado(this.compartimento.getNombre());
            }
            // Eliminamos el uso de funciones de la cadena de la ecuación
            // para no confundirlas con el nombre de otros elementos
            String ecuacion = this.definicionTextField.getText();
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
                    this.epidemia.getParametro(comprobador.group()).anadirCompartimentoVinculado(this.compartimento.getNombre());
                } else if (clase == Proceso.class) {
                    this.epidemia.getProceso(comprobador.group()).anadirCompartimentoVinculado(this.compartimento.getNombre());
                } else if (clase == Compartimento.class) {
                    this.epidemia.getCompartimento(comprobador.group()).anadirCompartimentoVinculado(this.compartimento.getNombre());
                } else if (clase == Atajo.class) {
                    String[] trozos = this.epidemia.getAtajo(comprobador.group()).getDefinicionContinua().split(" \\+ "); // NOI18N
                    for (String s : trozos) {
                        this.epidemia.getCompartimento(s).anadirCompartimentoVinculado(this.compartimento.getNombre());
                    }
                } else {
                    System.err.println("Error: producido en DinamicaCompartimento::aceptar(), el patrón ha encontrado un elemento que no está definido, debería haberse detectado en comprobarDefContinua()"); // NOI18N
                }
            }
            // Cerrar la ventana indicando que ha habido modificaciones
            this.modificado = true;
            dispose();
        }
    }
    
    /**
     * Comprueba los campos referidos a la definición para simulaciones
     * contínuas del compartimento que se está modificando.
     * @return Una cadena de caracteres con los errores detectados. Vacía si no
     *         ha habido.
     */
    private String comprobarDefContinua() {
        String errores = ""; // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DinamicaCompartimento.class);
        
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
        // Se podrán usar todos los procesos
        for (int i = 0; i < this.epidemia.getProcesos().length; i++) {
            jep.addVariable(this.epidemia.getProceso(i).getNombre(), 1);
        }
        
        // Sólo comprobamos si se ha escrito algo en el campo
        if (!this.definicionTextField.getText().isEmpty()) {
            // Si la cadena contiene algún punto y coma -> inválida
            if (this.definicionTextField.getText().contains(";")) {
                errores += resourceMap.getString("comprobacion.error.defContinuaInvalida"); // NOI18N
                return errores;
            }
            // En caso contrario, comprobamos que lo introducido es una ecuación aceptable
            Node funcion = jep.parseExpression(this.definicionTextField.getText());
            try {
                jep.evaluate(funcion);
            } catch (ParseException ex) {
                System.err.println(ex.getMessage());
                errores += resourceMap.getString("comprobacion.error.defContinuaInvalida"); // NOI18N
            }
        } 
        
        // Devolvemos los errores
        return errores;
    }

    /**
     * Método que se encarga de rellenar los campos con los datos del
     * compartimento que se vaya a definir.
     */
    private void cargarCompartimento() {
        if (this.compartimento.getDefinicionContinua() != null && !this.compartimento.getDefinicionContinua().isEmpty()) {
            this.definicionTextField.setText(this.compartimento.getDefinicionContinua());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton aceptarButton;
    private javax.swing.JSeparator buttonSeparator;
    private javax.swing.JButton cancelarButton;
    private javax.swing.JLabel definicionLabel;
    private javax.swing.JTextField definicionTextField;
    private javax.swing.JTextPane exp1TextPane;
    private javax.swing.JTextPane expTextPane;
    private javax.swing.JLabel tituloLabel;
    private javax.swing.JSeparator tituloSeparator;
    // End of variables declaration//GEN-END:variables
    protected boolean modificado = false;
}
