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

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.application.Action;

/**
 * Interfaz de usuario creada a través de la ventana principal de la aplicación
 * que ofrece las opciones de añadir, modificar o eliminar parámetros o
 * procesos.
 * @author Víctor E. Tamames Gómez
 */
public class GestionarVariables extends javax.swing.JDialog {

    /**
     * La epidemia cuyos parámetros y procesos estamos actualizando.
     */
    private Epidemia epidemia;

    /**
     * Crea una nueva ventana de GestionarVariables.
     * @param parent El componente que crea la instancia de esta clase.
     * @param modal Flag que indica si el resto de componentes son accesibles o no.
     * @param epi La epidemia con la que vamos a tratar.
     */
    public GestionarVariables(java.awt.Frame parent, boolean modal, Epidemia epi) {
        super(parent, modal);
        initComponents();
        this.epidemia = epi;

        // Construir las listas iniciales de parámetros y procesos
        this.modificarParButton.setEnabled(false);
        this.eliminarParButton.setEnabled(false);
        this.subirParButton.setEnabled(false);
        this.bajarParButton.setEnabled(false);
        this.modificarProcButton.setEnabled(false);
        this.eliminarProcButton.setEnabled(false);
        this.subirProcButton.setEnabled(false);
        this.bajarProcButton.setEnabled(false);
        DefaultListModel listaParametros = this.epidemia.construirListaParametros();
        DefaultListModel listaProcesos = this.epidemia.construirListaProcesos();
        this.parList.setModel(listaParametros);
        this.procList.setModel(listaProcesos);

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

        mainTabbedPane = new javax.swing.JTabbedPane();
        parametrosPanel = new javax.swing.JPanel();
        parTextPane = new javax.swing.JTextPane();
        parScrollPane = new javax.swing.JScrollPane();
        parList = new javax.swing.JList();
        anadirParButton = new javax.swing.JButton();
        modificarParButton = new javax.swing.JButton();
        eliminarParButton = new javax.swing.JButton();
        subirParButton = new javax.swing.JButton();
        bajarParButton = new javax.swing.JButton();
        procesosPanel = new javax.swing.JPanel();
        procTextPane = new javax.swing.JTextPane();
        procScrollPane = new javax.swing.JScrollPane();
        procList = new javax.swing.JList();
        anadirProcButton = new javax.swing.JButton();
        modificarProcButton = new javax.swing.JButton();
        eliminarProcButton = new javax.swing.JButton();
        subirProcButton = new javax.swing.JButton();
        bajarProcButton = new javax.swing.JButton();
        cerrarButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N

        mainTabbedPane.setName("mainTabbedPane"); // NOI18N

        parametrosPanel.setName("parametrosPanel"); // NOI18N

        parTextPane.setBackground(this.getBackground());
        parTextPane.setBorder(null);
        parTextPane.setEditable(false);
        parTextPane.setText(resourceMap.getString("parTextPane.text")); // NOI18N
        parTextPane.setName("parTextPane"); // NOI18N

        parScrollPane.setName("parScrollPane"); // NOI18N

        parList.setFont(resourceMap.getFont("parList.font")); // NOI18N
        parList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        parList.setName("parList"); // NOI18N
        parList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                parListValueChanged(evt);
            }
        });
        parScrollPane.setViewportView(parList);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(GestionarVariables.class, this);
        anadirParButton.setAction(actionMap.get("anadirParametro")); // NOI18N
        anadirParButton.setName("anadirParButton"); // NOI18N

        modificarParButton.setAction(actionMap.get("modificarParametro")); // NOI18N
        modificarParButton.setName("modificarParButton"); // NOI18N

        eliminarParButton.setAction(actionMap.get("eliminarParametro")); // NOI18N
        eliminarParButton.setName("eliminarParButton"); // NOI18N

        subirParButton.setAction(actionMap.get("subirParametro")); // NOI18N
        subirParButton.setName("subirParButton"); // NOI18N

        bajarParButton.setAction(actionMap.get("bajarParametro")); // NOI18N
        bajarParButton.setName("bajarParButton"); // NOI18N

        javax.swing.GroupLayout parametrosPanelLayout = new javax.swing.GroupLayout(parametrosPanel);
        parametrosPanel.setLayout(parametrosPanelLayout);
        parametrosPanelLayout.setHorizontalGroup(
            parametrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parametrosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(parametrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(parTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addGroup(parametrosPanelLayout.createSequentialGroup()
                        .addComponent(parScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(parametrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(anadirParButton)
                            .addComponent(modificarParButton)
                            .addComponent(eliminarParButton)
                            .addComponent(subirParButton)
                            .addComponent(bajarParButton))))
                .addContainerGap())
        );

        parametrosPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {anadirParButton, bajarParButton, eliminarParButton, modificarParButton, subirParButton});

        parametrosPanelLayout.setVerticalGroup(
            parametrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parametrosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(parTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(parametrosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(parametrosPanelLayout.createSequentialGroup()
                        .addComponent(anadirParButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarParButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarParButton)
                        .addGap(51, 51, 51)
                        .addComponent(subirParButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bajarParButton))
                    .addComponent(parScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
                .addContainerGap())
        );

        parametrosPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {anadirParButton, bajarParButton, eliminarParButton, modificarParButton, subirParButton});

        mainTabbedPane.addTab(resourceMap.getString("parametrosPanel.TabConstraints.tabTitle"), parametrosPanel); // NOI18N

        procesosPanel.setName("procesosPanel"); // NOI18N

        procTextPane.setBackground(this.getBackground());
        procTextPane.setBorder(null);
        procTextPane.setEditable(false);
        procTextPane.setText(resourceMap.getString("procTextPane.text")); // NOI18N
        procTextPane.setName("procTextPane"); // NOI18N

        procScrollPane.setName("procScrollPane"); // NOI18N

        procList.setFont(resourceMap.getFont("procList.font")); // NOI18N
        procList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        procList.setName("procList"); // NOI18N
        procList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                procListValueChanged(evt);
            }
        });
        procScrollPane.setViewportView(procList);

        anadirProcButton.setAction(actionMap.get("anadirProceso")); // NOI18N
        anadirProcButton.setName("anadirProcButton"); // NOI18N

        modificarProcButton.setAction(actionMap.get("modificarProceso")); // NOI18N
        modificarProcButton.setName("modificarProcButton"); // NOI18N

        eliminarProcButton.setAction(actionMap.get("eliminarProceso")); // NOI18N
        eliminarProcButton.setName("eliminarProcButton"); // NOI18N

        subirProcButton.setAction(actionMap.get("subirProceso")); // NOI18N
        subirProcButton.setName("subirProcButton"); // NOI18N

        bajarProcButton.setAction(actionMap.get("bajarProceso")); // NOI18N
        bajarProcButton.setName("bajarProcButton"); // NOI18N

        javax.swing.GroupLayout procesosPanelLayout = new javax.swing.GroupLayout(procesosPanel);
        procesosPanel.setLayout(procesosPanelLayout);
        procesosPanelLayout.setHorizontalGroup(
            procesosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(procesosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(procesosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(procTextPane, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, procesosPanelLayout.createSequentialGroup()
                        .addComponent(procScrollPane)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(procesosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(anadirProcButton)
                            .addComponent(modificarProcButton)
                            .addComponent(eliminarProcButton)
                            .addComponent(subirProcButton)
                            .addComponent(bajarProcButton))))
                .addContainerGap())
        );

        procesosPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {anadirProcButton, bajarProcButton, eliminarProcButton, modificarProcButton, subirProcButton});

        procesosPanelLayout.setVerticalGroup(
            procesosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(procesosPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(procTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(procesosPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(procesosPanelLayout.createSequentialGroup()
                        .addComponent(anadirProcButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(modificarProcButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(eliminarProcButton)
                        .addGap(52, 52, 52)
                        .addComponent(subirProcButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bajarProcButton))
                    .addComponent(procScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
                .addContainerGap())
        );

        procesosPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {anadirProcButton, bajarProcButton, eliminarProcButton, modificarProcButton, subirProcButton});

        mainTabbedPane.addTab(resourceMap.getString("procesosPanel.TabConstraints.tabTitle"), procesosPanel); // NOI18N

        cerrarButton.setAction(actionMap.get("cerrar")); // NOI18N
        cerrarButton.setToolTipText(null);
        cerrarButton.setName("cerrarButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(250, Short.MAX_VALUE)
                .addComponent(cerrarButton)
                .addContainerGap())
            .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cerrarButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

/**
 * Evento que se acciona cuando se cambia la selección de la lista de parámetros.
 * @param evt El evento generado.
 */
private void parListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_parListValueChanged
    // Si hay alguno seleccionado, se activan los botones Modificar y Eliminar
    if (this.parList.getSelectedIndex() == -1) {
        this.modificarParButton.setEnabled(false);
        this.eliminarParButton.setEnabled(false);
        this.subirParButton.setEnabled(false);
        this.bajarParButton.setEnabled(false);
    } else {
        this.modificarParButton.setEnabled(true);
        this.eliminarParButton.setEnabled(true);
        // Si se selecciona el primero, no se puede subir
        this.subirParButton.setEnabled(this.parList.getSelectedIndex() != 0);
        // Si se selecciona el último, no se puede bajar
        this.bajarParButton.setEnabled(this.parList.getSelectedIndex() != this.parList.getModel().getSize()-1);
    }
}//GEN-LAST:event_parListValueChanged

/**
 * Evento que se acciona cuando se cambia la selección de la lista de procesos.
 * @param evt El evento generado.
 */
private void procListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_procListValueChanged
    // Si hay alguno seleccionado, se activan los botones Modificar y Eliminar
    if (this.procList.getSelectedIndex() == -1) {
        this.modificarProcButton.setEnabled(false);
        this.eliminarProcButton.setEnabled(false);
        this.subirProcButton.setEnabled(false);
        this.bajarProcButton.setEnabled(false);
    } else {
        this.modificarProcButton.setEnabled(true);
        this.eliminarProcButton.setEnabled(true);
        // Si se selecciona el primero, no se puede subir
        this.subirProcButton.setEnabled(this.procList.getSelectedIndex() != 0);
        // Si se selecciona el último, no se puede bajar
        this.bajarProcButton.setEnabled(this.procList.getSelectedIndex() != this.procList.getModel().getSize()-1);
    }
}//GEN-LAST:event_procListValueChanged

    /**
     * Acción a realizar cuando el usuario pulsa "Cerrar ventana". Todos los
     * posibles cambios se gestionan con otros métodos, por lo que éste sólo
     * se encarga de cerrar la ventana.
     */
    @Action
    public void cerrar() {
        // Cerrar la ventana
        dispose();
    }

    /**
     * Acción a realizar cuando se presiona "Añadir" en la pestaña de parámetros
     * creando una nueva ventana GestionarParametro en blanco.
     */
    @Action
    public void anadirParametro() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        delphsim.GestionarParametro gp = new delphsim.GestionarParametro(mainFrame, true, this.epidemia, -1);
        this.parList.setModel(this.epidemia.construirListaParametros());
        this.modificado = this.modificado || gp.modificado;
    }

    /**
     * Acción a realizar cuando se presiona "Modificar" en la pestaña de
     * parámetros creando una nueva ventana GestionarParametro con los datos
     * actuales introducidos.
     */
    @Action
    public void modificarParametro() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        delphsim.GestionarParametro gp = new delphsim.GestionarParametro(mainFrame, true, this.epidemia, this.parList.getSelectedIndex());
        this.parList.setModel(this.epidemia.construirListaParametros());
        this.modificado = this.modificado || gp.modificado;
    }

    /**
     * Acción a realizar cuando se presiona "Eliminar" en la pestaña de
     * parámetros. Se comprueba que se pueda eliminar, en caso afirmativo se
     * le pide a la Epidemia que lo elimine, y se actualiza la vista.
     */
    @Action
    public void eliminarParametro() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        // Comprobar que no esté siendo usado
        String[] parVinc = this.epidemia.getParametro(this.parList.getSelectedIndex()).getParametrosVinculados();
        String[] procVinc = this.epidemia.getParametro(this.parList.getSelectedIndex()).getProcesosVinculados();
        String[] compVinc = this.epidemia.getParametro(this.parList.getSelectedIndex()).getCompartimentosVinculados();
        // Si hay algún elemento en alguna de las 3 listas, hay elementos que dependen de él,
        // construimos y mostramos un mensaje de error con los elementos dependientes
        if (parVinc.length + procVinc.length + compVinc.length > 0) {
            Object[] msgError = new Object[2];
            msgError[0] = resourceMap.getString("comprobacion.eliminarParametro.cabecera", this.parList.getSelectedValue().toString()); // NOI18N
            String listaRelaciones = ""; // NOI18N
            if (parVinc.length > 0) {
                listaRelaciones += resourceMap.getString("comprobacion.eliminarParametro.parsVinc"); // NOI18N
                for (String par : parVinc) {
                    listaRelaciones += String.format("\t%s\n",par); // NOI18N
                }
            }
            if (procVinc.length > 0) {
                listaRelaciones += resourceMap.getString("comprobacion.eliminarParametro.procVinc"); // NOI18N
                for (String proc : procVinc) {
                    listaRelaciones += String.format("\t%s\n",proc); // NOI18N
                }
            }
            if (compVinc.length > 0) {
                listaRelaciones += resourceMap.getString("comprobacion.eliminarParametro.compVinc"); // NOI18N
                for (String comp : compVinc) {
                    listaRelaciones += String.format("\t%s\n",comp); // NOI18N
                }
            }
            javax.swing.JTextArea texto = new javax.swing.JTextArea();
            javax.swing.JScrollPane scrolling = new javax.swing.JScrollPane();
            scrolling.setViewportView(texto);
            texto.setText(listaRelaciones);
            texto.setColumns(48);
            texto.setRows(5);
            texto.setEditable(false);
            texto.setBackground(this.getBackground());
            texto.setCaretPosition(0);
            msgError[1] = scrolling;
            JOptionPane.showMessageDialog(mainTabbedPane, msgError, resourceMap.getString("comprobacion.eliminarParametro.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
        } else if (JOptionPane.showConfirmDialog(mainTabbedPane, resourceMap.getString("comprobacion.eliminarParametro.confirmacion.msg"), resourceMap.getString("comprobacion.eliminarParametro.confirmacion.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) { // NOI18N
            // Si se puede borrar y el usuario lo confirma, borramos y actualizamos la vista
            this.epidemia.eliminarParametro(this.parList.getSelectedIndex());
            this.parList.setModel(this.epidemia.construirListaParametros());
            this.modificado = true;
        }
    }

    /**
     * Acción a realizar cuando se presiona "Subir" en la pestaña de parámetros.
     * Se comprueba que el parámetro seleccionado no dependa del de arriba y, en
     * caso afirmativo, se mueve una posición hacia arriba y se actualiza la vista.
     */
    @Action
    public void subirParametro() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        int indiceSeleccionado = this.parList.getSelectedIndex();
        String nombreParSelec = this.epidemia.getParametro(indiceSeleccionado).getNombre();
        for (String parVinc : this.epidemia.getParametro(indiceSeleccionado-1).getParametrosVinculados()) {
            if (parVinc.equals(nombreParSelec)) {
                // Si encontramos una vinculación con el parámetro de arriba, mostramos mensaje de error y no lo movemos
                String nombreParAnterior = this.epidemia.getParametro(indiceSeleccionado-1).getNombre();
                JOptionPane.showMessageDialog(mainTabbedPane, resourceMap.getString("comprobacion.subirParametro.msg", nombreParAnterior), resourceMap.getString("comprobacion.subirParametro.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
                return;
            }
        }
        // Si no hemos encontrado vinculaciones al parámetro de arriba, lo movemos y actualizamos la lista
        this.epidemia.moverParametro(indiceSeleccionado, indiceSeleccionado-1);
        this.parList.setModel(this.epidemia.construirListaParametros());
        this.parList.setSelectedIndex(indiceSeleccionado-1);
        this.modificado = true;
    }

    /**
     * Acción a realizar cuando se presiona "Bajar" en la pestaña de parámetros.
     * Se comprueba que el parámetro seleccionado no dependa del de abajo y, en
     * caso afirmativo, se mueve una posición hacia abajo y se actualiza la vista.
     */
    @Action
    public void bajarParametro() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        int indiceSeleccionado = this.parList.getSelectedIndex();
        String nombreParSiguiente = this.epidemia.getParametro(indiceSeleccionado+1).getNombre();
        for (String parVinc : this.epidemia.getParametro(indiceSeleccionado).getParametrosVinculados()) {
            if (parVinc.equals(nombreParSiguiente)) {
                // Si encontramos una vinculación con el parámetro de abajo, mostramos mensaje de error y no lo movemos
                JOptionPane.showMessageDialog(mainTabbedPane, resourceMap.getString("comprobacion.bajarParametro.msg", parVinc), resourceMap.getString("comprobacion.bajarParametro.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
                return;
            }
        }
        // Si no hemos encontrado vinculaciones al parámetro de abajo, lo movemos y actualizamos la lista
        this.epidemia.moverParametro(indiceSeleccionado, indiceSeleccionado+1);
        this.parList.setModel(this.epidemia.construirListaParametros());
        this.parList.setSelectedIndex(indiceSeleccionado+1);
        this.modificado = true;
    }

    /**
     * Acción a realizar cuando se presiona "Añadir" en la pestaña de procesos
     * creando una nueva ventana GestionarProceso en blanco.
     */
    @Action
    public void anadirProceso() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        delphsim.GestionarProceso gp = new delphsim.GestionarProceso(mainFrame, true, this.epidemia, -1);
        this.procList.setModel(this.epidemia.construirListaProcesos());
        this.modificado = this.modificado || gp.modificado;
    }

    /**
     * Acción a realizar cuando se presiona "Añadir" en la pestaña de procesos
     * creando una nueva ventana GestionarProceso con los datos actuales
     * introducidos.
     */
    @Action
    public void modificarProceso() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        delphsim.GestionarProceso gp = new delphsim.GestionarProceso(mainFrame, true, this.epidemia, this.procList.getSelectedIndex());
        this.procList.setModel(this.epidemia.construirListaProcesos());
        this.modificado = this.modificado || gp.modificado;
    }

    /**
     * Acción a realizar cuando se presiona "Eliminar" en la pestaña de
     * procesos. Se comprueba que se pueda eliminar, en caso afirmativo se
     * le pide a la Epidemia que lo elimine, y se actualiza la vista.
     */
    @Action
    public void eliminarProceso() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        // Comprobar que no esté siendo usado
        String[] procVinc = this.epidemia.getProceso(this.procList.getSelectedIndex()).getProcesosVinculados();
        String[] compVinc = this.epidemia.getProceso(this.procList.getSelectedIndex()).getCompartimentosVinculados();
        // Si hay algún elemento en alguna de las 3 listas, hay elementos que dependen de él,
        // construimos y mostramos un mensaje de error con los elementos dependientes
        if (procVinc.length + compVinc.length > 0) {
            Object[] msgError = new Object[2];
            msgError[0] = resourceMap.getString("comprobacion.eliminarProceso.cabecera", this.procList.getSelectedValue().toString()); // NOI18N
            String listaRelaciones = ""; // NOI18N
            if (procVinc.length > 0) {
                listaRelaciones += resourceMap.getString("comprobacion.eliminarProceso.procVinc"); // NOI18N
                for (String proc : procVinc) {
                    listaRelaciones += String.format("\t%s\n",proc); // NOI18N
                }
            }
            if (compVinc.length > 0) {
                listaRelaciones += resourceMap.getString("comprobacion.eliminarProceso.compVinc"); // NOI18N
                for (String comp : compVinc) {
                    listaRelaciones += String.format("\t%s\n",comp); // NOI18N
                }
            }
            javax.swing.JTextArea texto = new javax.swing.JTextArea();
            javax.swing.JScrollPane scrolling = new javax.swing.JScrollPane();
            scrolling.setViewportView(texto);
            texto.setText(listaRelaciones);
            texto.setColumns(48);
            texto.setRows(5);
            texto.setEditable(false);
            texto.setBackground(this.getBackground());
            texto.setCaretPosition(0);
            msgError[1] = scrolling;
            JOptionPane.showMessageDialog(mainTabbedPane, msgError, resourceMap.getString("comprobacion.eliminarProceso.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
        } else if (JOptionPane.showConfirmDialog(mainTabbedPane, resourceMap.getString("comprobacion.eliminarProceso.confirmacion.msg"), resourceMap.getString("comprobacion.eliminarProceso.confirmacion.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) { // NOI18N
            // Si se puede borrar y el usuario lo confirma, borramos y actualizamos la vista
            this.epidemia.eliminarProceso(this.procList.getSelectedIndex());
            this.procList.setModel(this.epidemia.construirListaProcesos());
            this.modificado = true;
        }
    }

    /**
     * Acción a realizar cuando se presiona "Subir" en la pestaña de procesos.
     * Se comprueba que el proceso seleccionado no dependa del de arriba y, en
     * caso afirmativo, se mueve una posición hacia arriba y se actualiza la vista.
     */
    @Action
    public void subirProceso() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        int indiceSeleccionado = this.procList.getSelectedIndex();
        String nombreProcSelec = this.epidemia.getProceso(indiceSeleccionado).getNombre();
        for (String procVinc : this.epidemia.getProceso(indiceSeleccionado-1).getProcesosVinculados()) {
            if (procVinc.equals(nombreProcSelec)) {
                // Si encontramos una vinculación con el proceso de arriba, mostramos mensaje de error y no lo movemos
                String nombreProcAnterior = this.epidemia.getProceso(indiceSeleccionado-1).getNombre();
                JOptionPane.showMessageDialog(mainTabbedPane, resourceMap.getString("comprobacion.subirProceso.msg", nombreProcAnterior), resourceMap.getString("comprobacion.subirProceso.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
                return;
            }
        }
        // Si no hemos encontrado vinculaciones al proceso de arriba, lo movemos y actualizamos la lista
        this.epidemia.moverProceso(indiceSeleccionado, indiceSeleccionado-1);
        this.procList.setModel(this.epidemia.construirListaProcesos());
        this.procList.setSelectedIndex(indiceSeleccionado-1);
        this.modificado = true;
    }

    /**
     * Acción a realizar cuando se presiona "Bajar" en la pestaña de procesos.
     * Se comprueba que el proceso seleccionado no dependa del de abajo y, en
     * caso afirmativo, se mueve una posición hacia abajo y se actualiza la vista.
     */
    @Action
    public void bajarProceso() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(GestionarVariables.class);
        int indiceSeleccionado = this.procList.getSelectedIndex();
        String nombreProcSiguiente = this.epidemia.getProceso(indiceSeleccionado+1).getNombre();
        for (String procVinc : this.epidemia.getProceso(indiceSeleccionado).getProcesosVinculados()) {
            if (procVinc.equals(nombreProcSiguiente)) {
                // Si encontramos una vinculación con el proceso de abajo, mostramos mensaje de error y no lo movemos
                JOptionPane.showMessageDialog(mainTabbedPane, resourceMap.getString("comprobacion.bajarProceso.msg", procVinc), resourceMap.getString("comprobacion.bajarProceso.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
                return;
            }
        }
        // Si no hemos encontrado vinculaciones al proceso de abajo, lo movemos y actualizamos la lista
        this.epidemia.moverProceso(indiceSeleccionado, indiceSeleccionado+1);
        this.procList.setModel(this.epidemia.construirListaProcesos());
        this.procList.setSelectedIndex(indiceSeleccionado+1);
        this.modificado = true;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anadirParButton;
    private javax.swing.JButton anadirProcButton;
    private javax.swing.JButton bajarParButton;
    private javax.swing.JButton bajarProcButton;
    private javax.swing.JButton cerrarButton;
    private javax.swing.JButton eliminarParButton;
    private javax.swing.JButton eliminarProcButton;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JButton modificarParButton;
    private javax.swing.JButton modificarProcButton;
    private javax.swing.JList parList;
    private javax.swing.JScrollPane parScrollPane;
    private javax.swing.JTextPane parTextPane;
    private javax.swing.JPanel parametrosPanel;
    private javax.swing.JList procList;
    private javax.swing.JScrollPane procScrollPane;
    private javax.swing.JTextPane procTextPane;
    private javax.swing.JPanel procesosPanel;
    private javax.swing.JButton subirParButton;
    private javax.swing.JButton subirProcButton;
    // End of variables declaration//GEN-END:variables
    protected boolean modificado = false;
}
