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

import delphsim.model.Epidemia;
import delphsim.model.Resultado;
import delphsim.simulation.*;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.dom4j.DocumentException;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;

/**
 * Esta clase representa la ventana principal de la aplicación.
 */
public class DelphSimView extends FrameView {
    
    /**
     * El modelo de la epidemia que se está modificando.
     */
    private Epidemia epidemia;
    
    /**
     * El archivo asociado con el modelo que se está modificando en
     * la aplicación.
     */
    private File archivoActual = null;
    
    /**
     * Flag que indica si el modelo se ha modificado desde la última vez que
     * se guardó.
     */
    private boolean archivoModificado = false;
    
    /**
     * La tarea de simulación que se está ejecutando.
     */
    private Task tareaActual;
    
    /**
     * Reloj que marca cuánto tiempo permanece un mensaje en la barra de estado.
     */
    private final Timer messageTimer;
    
    /**
     * Reloj que marca la velocidad a la que se mueven los iconos de "tarea
     * ejecutándose" en la barra de estado.
     */
    private final Timer busyIconTimer;
    
    /**
     * Icono que indica que no se está ejecutando ninguna tarea.
     */
    private final Icon idleIcon;
    
    /**
     * Iconos que se usan cuando se está ejecutando una tarea.
     */
    private final Icon[] busyIcons = new Icon[15];
    
    /**
     *¨Índice en el que comienzan a alternarse los iconos de ejecución de tarea.
     */
    private int busyIconIndex = 0;
    
    private javax.swing.DefaultComboBoxModel unidadesTiempo;

    /**
     * Constructor de la clase.
     * @param app La clase principal de la aplicación.
     */
    public DelphSimView(SingleFrameApplication app) {
        super(app);
        
        // ComboBox con las unidades de tiempo
        ResourceMap resourceMap = getResourceMap();
        this.unidadesTiempo = new javax.swing.DefaultComboBoxModel(new String[] { 
            resourceMap.getString("TimeUnit.seconds"),  // NOI18N
            resourceMap.getString("TimeUnit.minutes"),  // NOI18N
            resourceMap.getString("TimeUnit.hours"),  // NOI18N
            resourceMap.getString("TimeUnit.days"),  // NOI18N
            resourceMap.getString("TimeUnit.weeks"),  // NOI18N
            resourceMap.getString("TimeUnit.months"),  // NOI18N
            resourceMap.getString("TimeUnit.years") }); // NOI18N
        
        initComponents();
        PanelPestanas.setVisible(false);
        
        // Estado inicial de los botones, activados o no
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        actionMap.get("guardarModelo").setEnabled(false); // NOI18N
        actionMap.get("guardarModeloComo").setEnabled(false); // NOI18N
        actionMap.get("distribucionPoblacion").setEnabled(false); // NOI18N
        actionMap.get("dinamicaPoblacion").setEnabled(false); // NOI18N
        actionMap.get("gestionarVariables").setEnabled(false); // NOI18N
        actionMap.get("iniciarContinua").setEnabled(false); // NOI18N
        actionMap.get("detenerSimulacion").setEnabled(false); // NOI18N
        actionMap.get("exportarGrafica").setEnabled(false); // NOI18N
        actionMap.get("exportarInforme").setEnabled(false); // NOI18N
        actionMap.get("mostrarModelo").setEnabled(false); // NOI18N
        this.windowButtonGroup.clearSelection();
        this.resultsWindowsMenu.setEnabled(false);
        this.jButtonCompContinua.setEnabled(false);
        this.jButtonParContinua.setEnabled(false);
        this.jButtonProcContinua.setEnabled(false);
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout"); // NOI18N

        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText(""); // NOI18N
                
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate"); // NOI18N

        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]"); // NOI18N

        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon"); // NOI18N

        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) { // NOI18N
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) { // NOI18N
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) { // NOI18N
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text); // NOI18N
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) { // NOI18N
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }
    
    /**
     * Método para que la aplicación compruebe si existe o no una copia de
     * respaldo de una ejecución anterior una vez que la interfaz esté creada.
     */
    public void comprobarCopiaRespaldo() {
        ResourceMap resourceMap = getResourceMap();
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        
        // Por último, si detectamos que hay un backup.temp, presuponemos que ha terminado
        // mal una ejecución anterior y preguntamos al usuario si desea cargar el backup
        File backup = new File(new File(System.getProperty("java.class.path")).getParent() + resourceMap.getString("autoguardado.path")); // NOI18N
        if (backup.exists()) {
            int cargar = JOptionPane.showConfirmDialog(this.mainPanel, resourceMap.getString("autoguardado.hayBackup.msg"), resourceMap.getString("autoguardado.hayBackup.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE); // NOI18N
            if (cargar == JOptionPane.YES_OPTION) {
                try {
                    this.epidemia = new Epidemia();
                    String errores = this.epidemia.abrirXML(resourceMap.getString("Application.XMLSchemaPath"), backup); // NOI18N
                    if (errores.equals("")) { // NOI18N
                        actualizarVistaModelo();
                        getFrame().setTitle(resourceMap.getString("Application.title") + " - " + backup.getName()); // NOI18N
                        // Actualizamos los botones que tienen que estar activados/desactivados
                        actionMap.get("guardarModelo").setEnabled(true); // NOI18N
                        actionMap.get("guardarModeloComo").setEnabled(true); // NOI18N
                        actionMap.get("distribucionPoblacion").setEnabled(true); // NOI18N
                        actionMap.get("dinamicaPoblacion").setEnabled(true); // NOI18N
                        actionMap.get("gestionarVariables").setEnabled(true); // NOI18N
                        actionMap.get("iniciarContinua").setEnabled(true); // NOI18N
                        actionMap.get("mostrarModelo").setEnabled(true); // NOI18N
                        this.timeComboBox.setEnabled(true);
                        this.timeSpinner.setEnabled(true);
                        this.modelMenuItem.setSelected(true);
                        this.archivoModificado = true;
                        if (!backup.delete()) {
                            System.err.println("Error: no se ha podido borrar la copia de seguridad"); // NOI18N
                        }
                    } else {
                        this.epidemia = null;
                        int borrar = JOptionPane.showConfirmDialog(this.mainPanel, resourceMap.getString("autoguardado.errorCargar.msg"), resourceMap.getString("autoguardado.errorCargar.title"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE); // NOI18N
                        if (borrar == JOptionPane.YES_OPTION) {
                            if (!backup.delete()) {
                                JOptionPane.showMessageDialog(this.mainPanel, resourceMap.getString("autoguardado.errorBorrar.msg"), resourceMap.getString("autoguardado.errorBorrar.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
                                System.err.println("Error: no se ha podido borrar la copia de seguridad"); // NOI18N
                            }
                        }
                    }
                } catch (Exception ex) {
                    this.epidemia = null;
                    System.err.println("Error: no se ha podido cargar la copia de seguridad. Mensaje: " + ex.getMessage()); // NOI18N
                    int borrar = JOptionPane.showConfirmDialog(this.mainPanel, resourceMap.getString("autoguardado.errorCargar.msg"), resourceMap.getString("autoguardado.errorCargar.title"), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE); // NOI18N
                    if (borrar == JOptionPane.YES_OPTION) {
                        if (!backup.delete()) {
                            JOptionPane.showMessageDialog(this.mainPanel, resourceMap.getString("autoguardado.errorBorrar.msg"), resourceMap.getString("autoguardado.errorBorrar.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
                            System.err.println("Error: no se ha podido borrar la copia de seguridad"); // NOI18N
                        }
                    }
                    // Error detallado mostrado por consola
                    // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                if (!backup.delete()) {
                    System.err.println("Error: no se ha podido borrar la copia de seguridad"); // NOI18N
                }
            }
        }
    }
    
    /**
     * Devuelve si el archivo ha sido modificado desde la última vez que se
     * guardó o no. De esta manera la aplicación puede preguntar al usuario si
     * realmente desea salir de la aplicación sin guardar.
     * @return Si el modelo ha sido modificado o no.
     */
    public boolean getArchivoModificado() {
        return this.archivoModificado;
    }
    
    /**
     * Acción a realizar cuando se escoge la opción "Nuevo" del menú "Modelo" o
     * de la barra de herramientas. Crea y muestra la primera ventana del
     * proceso de definición de un modelo nuevo. Si se estuviera modificando un
     * modelo que aún no se ha guardado, deberá ofrecer la posibilidad de
     * guardarlo antes de borrar/sobreescribir ningún campo.
     */
    @Action
    public void nuevoModelo() {
        ResourceMap resourceMap = getResourceMap();
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);

        // Si tenemos un archivo modificado y no guardado, preguntamos si quiere guardar
        if (this.archivoModificado) {
            int opcion = JOptionPane.showConfirmDialog(PestanaModelo, resourceMap.getString("NuevoModelo.confirm.msg"), resourceMap.getString("NuevoModelo.confirm.title"), JOptionPane.INFORMATION_MESSAGE);  // NOI18N
            switch (opcion) {
                // Si desea guardar, se ejecuta guardarModelo o guardarModeloComo
                case JOptionPane.YES_OPTION:
                    this.guardarModelo();
                    break;
                // Si no, continuamos fuera del switch
                case JOptionPane.NO_OPTION:
                    break;
                // Si cancela o cierra el cuadro de dialogo, no se hace absolutamente nada
                case JOptionPane.CANCEL_OPTION:
                default:
                    return;
            }
        }
        
        // Avisar de que se van a cerrar los resultados y se perderán
        if (this.epidemia != null) {
            if (this.epidemia.getResultados() != null && this.epidemia.getResultados().length > 0) {
                if (JOptionPane.showConfirmDialog(mainPanel, this.getResourceMap().getString("ResultadosSePerderan.msg2"), this.getResourceMap().getString("ResultadosSePerderan.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            // Cerrar/eliminar pestañas de resultados y mostrar la del modelo
            this.mostrarModelo();
            this.epidemia.eliminarResultados();
            while (this.PanelPestanas.getTabCount() > 1) {
                this.PanelPestanas.remove(1);
            }
            this.resultsWindowsMenu.removeAll();
            this.resultsWindowsMenu.setEnabled(false);
        }        
        
        // Si hemos llegado hasta aquí, comenzamos la secuencia de crear un modelo nuevo desde el paso 1
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        DistribucionPoblacion nuevoModelo = new DistribucionPoblacion(mainFrame, true, null);
        if (nuevoModelo.getEpidemia() != null) {
            // Sustituir la nueva por la actual y actualizar vista
            this.epidemia = nuevoModelo.getEpidemia();
            this.archivoActual = null;
            this.actualizarVistaModelo();
            getFrame().setTitle(resourceMap.getString("Application.title") + " - " + resourceMap.getString("nuevoArchivo.name")); // NOI18N
            // Actualizamos los botones que tienen que estar activados/desactivados
            actionMap.get("guardarModelo").setEnabled(true); // NOI18N
            actionMap.get("guardarModeloComo").setEnabled(true); // NOI18N
            actionMap.get("distribucionPoblacion").setEnabled(true); // NOI18N
            actionMap.get("dinamicaPoblacion").setEnabled(true); // NOI18N
            actionMap.get("gestionarVariables").setEnabled(true); // NOI18N
            actionMap.get("iniciarContinua").setEnabled(true); // NOI18N
            actionMap.get("mostrarModelo").setEnabled(true); // NOI18N
            this.timeComboBox.setEnabled(true);
            this.timeSpinner.setEnabled(true);
            this.modelMenuItem.setSelected(true);
            this.archivoModificado = true;
        }
    }

    /**
     * Acción a realizar cuando se escoge la opción "Abrir" del menú "Modelo" o
     * de la barra de herramientas. Crea y muestra un cuadro de diálogo para
     * buscar el archivo que contiene el modelo que se quiere cargar.
     */
    @Action
    public void abrirModelo() {
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        ResourceMap resourceMap = getResourceMap();
        
        // Si tenemos un archivo modificado y no guardado, preguntamos si quiere guardar
        if (this.archivoModificado) {
            int opcion = JOptionPane.showConfirmDialog(PestanaModelo, resourceMap.getString("NuevoModelo.confirm.msg"), resourceMap.getString("NuevoModelo.confirm.title"), JOptionPane.INFORMATION_MESSAGE);  // NOI18N
            switch (opcion) {
                // Si desea guardar, se ejecuta guardarModelo o guardarModeloComo
                case JOptionPane.YES_OPTION:
                    this.guardarModelo();
                    break;
                // Si no, continuamos fuera del switch
                case JOptionPane.NO_OPTION:
                    break;
                // Si cancela o cierra el cuadro de dialogo, no se hace absolutamente nada
                case JOptionPane.CANCEL_OPTION:
                default:
                    return;
            }
        }
        
        // Avisar de que se van a cerrar los resultados y se perderán
        if (this.epidemia != null) {
            if (this.epidemia.getResultados() != null && this.epidemia.getResultados().length > 0) {
                if (JOptionPane.showConfirmDialog(mainPanel, this.getResourceMap().getString("ResultadosSePerderan.msg3"), this.getResourceMap().getString("ResultadosSePerderan.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            // Cerrar/eliminar pestañas de resultados y mostrar la del modelo
            this.mostrarModelo();
            this.epidemia.eliminarResultados();
            while (this.PanelPestanas.getTabCount() > 1) {
                this.PanelPestanas.remove(1);
            }
            this.resultsWindowsMenu.removeAll();
            this.resultsWindowsMenu.setEnabled(false);
        }
        
        // Variables locales que vamos a necesitar
        Epidemia epidemiaAnterior = null;
        File archivoAnterior = null;
        JFileChooser dialogoAbrir = new JFileChooser(this.archivoActual);

        // Cadenas de texto del cuadro de diálogo y filtros de archivos ofertados
        dialogoAbrir.setDialogTitle(resourceMap.getString("JCAbrirArchivo.title")); // NOI18N
        FileFilter filtroXML = new FileNameExtensionFilter(resourceMap.getString("JCArchivo.filter1desc"), "xml"); // NOI18N
        dialogoAbrir.addChoosableFileFilter(filtroXML);
        FileFilter filtroPorDefecto = new FileNameExtensionFilter(resourceMap.getString("JCArchivo.filter2desc"), "dsx"); // NOI18N
        dialogoAbrir.addChoosableFileFilter(filtroPorDefecto);
        dialogoAbrir.setFileFilter(filtroPorDefecto);

        // Mostramos el cuadro de diálogo y, si el usuario pulsa "Aceptar"
        int opcion = dialogoAbrir.showOpenDialog(getFrame());
        if (opcion == JFileChooser.APPROVE_OPTION) {
            // Intentamos abrir el archivo y cargarlo. Actualizamos la vista
            try {
                epidemiaAnterior = this.epidemia;
                archivoAnterior = this.archivoActual;
                this.archivoActual = dialogoAbrir.getSelectedFile();
                this.epidemia = new Epidemia();
                String errores = this.epidemia.abrirXML(resourceMap.getString("Application.XMLSchemaPath"), this.archivoActual); // NOI18N
                if (errores.equals("")) { // NOI18N
                    actualizarVistaModelo();
                    getFrame().setTitle(resourceMap.getString("Application.title") + " - " + archivoActual.getName()); // NOI18N
                    // Actualizamos los botones que tienen que estar activados/desactivados
                    actionMap.get("guardarModelo").setEnabled(false); // NOI18N
                    actionMap.get("guardarModeloComo").setEnabled(true); // NOI18N
                    actionMap.get("distribucionPoblacion").setEnabled(true); // NOI18N
                    actionMap.get("dinamicaPoblacion").setEnabled(true); // NOI18N
                    actionMap.get("gestionarVariables").setEnabled(true); // NOI18N
                    actionMap.get("iniciarContinua").setEnabled(true); // NOI18N
                    actionMap.get("mostrarModelo").setEnabled(true); // NOI18N
                    this.timeComboBox.setEnabled(true);
                    this.timeSpinner.setEnabled(true);
                    this.modelMenuItem.setSelected(true);
                    this.archivoModificado = false;
                } else {
                    this.epidemia = epidemiaAnterior;
                    this.archivoActual = archivoAnterior;
                    Object[] conErrores = new Object[3];
                    conErrores[0] = resourceMap.getString("JCAbrirArchivo.error.msg", dialogoAbrir.getSelectedFile().getPath());
                    conErrores[1] = resourceMap.getString("JCAbrirArchivo.error.msgErrores");
                    javax.swing.JTextArea texto = new javax.swing.JTextArea();
                    javax.swing.JScrollPane scrolling = new javax.swing.JScrollPane();
                    scrolling.setViewportView(texto);
                    texto.setText(errores);
                    texto.setColumns(50);
                    texto.setRows(5);
                    texto.setEditable(false);
                    texto.setBackground(this.getFrame().getBackground());
                    texto.setCaretPosition(0);
                    conErrores[2] = scrolling;
                    // Mensaje de error algo más detallado. Contiene qué elementos del archivo XML no concuerdan con el XML Schema
                    JOptionPane.showMessageDialog(mainPanel, conErrores, resourceMap.getString("JCAbrirArchivo.error.title"), JOptionPane.WARNING_MESSAGE); // NOI18N
                }
            // De producirse algún error, recuperamos la epidemia anterior e informamos del error
            } catch (Exception ex) {
                this.epidemia = epidemiaAnterior;
                this.archivoActual = archivoAnterior;
                JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCAbrirArchivo.error.msg", dialogoAbrir.getSelectedFile().getPath()), resourceMap.getString("JCAbrirArchivo.error.title"), JOptionPane.WARNING_MESSAGE); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.WARNING, null, ex);
            }
        }
    }

    /**
     * Acción a realizar cuando se escoge la opción "Guardar" del menú "Modelo"
     * o de la barra de herramientas. Actualiza el archivo vinculado al modelo
     * que se está modificando en el momento en que se invoca la acción.
     */
    @Action
    public void guardarModelo() {
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        ResourceMap resourceMap = getResourceMap();
        // Si no existía el archivo previamente, es lo mismo que hacer un Guardar Como
        if (this.archivoActual == null || !this.archivoActual.exists()) {
            guardarModeloComo();
        // Si existe pero no se puede escribir, hay que mostrar mensaje de error e invocar un Guardar Como para que elija otra ubicación
        } else if (!this.archivoActual.canWrite()) {
            JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCGuardarArchivo.soloLectura.msg"), resourceMap.getString("JCGuardarArchivo.soloLectura.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
            guardarModeloComo();
        // En caso contrario, sólo tenemos que reescribir el archivo actual
        } else {
            try {
                epidemia.guardarXML(this.archivoActual);
                // Activar/desactivar acordemente los botones
                actionMap.get("guardarModelo").setEnabled(false); // NOI18N
                this.archivoModificado = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCGuardarArchivo.error.msg"), resourceMap.getString("JCGuardarArchivo.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Acción a realizar cuando se escoge la opción "Guardar como" del menú
     * "Modelo" o de la barra de herramientas. Crea y muestra un cuadro de
     * diálogo para escoger dónde guardar el modelo que se está modificando en
     * el momento en que se invoca la acción.
     */
    @Action
    public void guardarModeloComo() {
        JFileChooser dialogoGuardar = new JFileChooser(this.archivoActual);
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        ResourceMap resourceMap = getResourceMap();

        // Cadenas de texto del cuadro de diálogo y filtros de archivos ofertados
        dialogoGuardar.setDialogTitle(resourceMap.getString("JCGuardarArchivoComo.title")); // NOI18N
        FileFilter filtroXML = new FileNameExtensionFilter(resourceMap.getString("JCArchivo.filter1desc"), "xml"); // NOI18N
        dialogoGuardar.addChoosableFileFilter(filtroXML);
        FileFilter filtroPorDefecto = new FileNameExtensionFilter(resourceMap.getString("JCArchivo.filter2desc"), "dsx"); // NOI18N
        dialogoGuardar.addChoosableFileFilter(filtroPorDefecto);
        dialogoGuardar.setFileFilter(filtroPorDefecto);

        // Mostramos el cuadro de diálogo y, si el usuario pulsa "Aceptar"
        int option = dialogoGuardar.showSaveDialog(getFrame());
        if (option == JFileChooser.APPROVE_OPTION) {
            // Primero averiguamos el nombre que debería tener
            String ruta = dialogoGuardar.getSelectedFile().getPath();
            if (!dialogoGuardar.getAcceptAllFileFilter().equals(dialogoGuardar.getFileFilter())) {
                String extension = ((FileNameExtensionFilter) dialogoGuardar.getFileFilter()).getExtensions()[0];
                if (!ruta.endsWith("." + extension)) { // NOI18N
                    ruta += "." + extension; // NOI18N
                }
            }
            // Si el archivo existe y el usuario no quiere reemplazarlo, volvemos
            File archivoDestino = new File(ruta);
            if (archivoDestino.exists()) {
                if (JOptionPane.showConfirmDialog(mainPanel, resourceMap.getString("JCGuardarArchivoComo.confirmacion.msg", archivoDestino.getPath()), resourceMap.getString("JCGuardarArchivoComo.confirmacion.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) { // NOI18N
                    return;
                }
                // Y si quiere reemplazarlo pero es de sólo lectura, mostramos error y volvemos
                if (!archivoDestino.canWrite()) {
                    JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCGuardarArchivoComo.soloLectura.msg"), resourceMap.getString("JCGuardarArchivoComo.soloLectura.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                    return;
                }
            }
            // En caso contrario, hay que escribir el archivo y guardarlo
            try {
                epidemia.guardarXML(archivoDestino);
                // Activar/desactivar acordemente los botones
                actionMap.get("guardarModelo").setEnabled(false); // NOI18N
                this.archivoActual = archivoDestino;
                getFrame().setTitle(resourceMap.getString("Application.title") + " - " + this.archivoActual.getName()); // NOI18N
                this.archivoModificado = false;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCGuardarArchivoComo.error.msg"), resourceMap.getString("JCGuardarArchivoComo.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
                // Si no se ha podido guardar, pero se ha creado, se borra
                if (archivoDestino.exists()) {
                    archivoDestino.delete();
                }
            }
        }
    }

    /**
     * Acción a realizar cuando el usuario selecciona la opción "Distribución
     * de la población" del menú "Modelo" o presiona el icono correspondiente
     * de la barra de herramientas. Muestra la misma ventana que crear un modelo
     * nuevo, pero con los datos de la epidemia con la que se está trabajando
     * cargados para que puedan modificarse. Si se completa correctamente el
     * proceso, se actualiza el modelo y la vista.
     */
    @Action
    public void distribucionPoblacion() {
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        DistribucionPoblacion modificarModelo = new DistribucionPoblacion(mainFrame, true, this.epidemia);
        Epidemia modificada = modificarModelo.getEpidemia();
        if (modificada != null) {
            this.epidemia = modificada;
            actualizarVistaModelo();
            actionMap.get("guardarModelo").setEnabled(true); // NOI18N
            this.archivoModificado = true;
        }
    }

    /**
     * Acción a realizar cuando el usuario selecciona la opción "Comportamiento
     * de la población" del menú "Modelo" o presiona el icono correspondiente de
     * la barra de herramientas. Muestra un asistente que le permitirá definir
     * cómo las personas de la población pasan de un compartimento a otro a lo
     * largo del tiempo en una simulación.
     */
    @Action
    public void dinamicaPoblacion() {
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        delphsim.DinamicaPoblacion dp = new delphsim.DinamicaPoblacion(mainFrame, true, this.epidemia);
        if (dp.modificado) {
            actionMap.get("guardarModelo").setEnabled(true); // NOI18N
            this.archivoModificado = true;
        }
    }

    /**
     * Acción a realizar cuando el usuario selecciona la opción "Parámetros y
     * procesos" del menú "Modelo" o presiona el icono correspondiente de la
     * barra de herramientas. Muestra una ventana con dos listas en las cuales
     * se pueden agregar, modificar o eliminar parámetros y procesos. El modelo
     * se actualizará con cada cambio que se haga individualmente, mientras que
     * la vista se actualizará cuando se hayan terminado de hacer todos los
     * cambios y se haya cerrado dicha ventana.
     */
    @Action
    public void gestionarVariables() {
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        delphsim.GestionarVariables gv = new delphsim.GestionarVariables(mainFrame, true, this.epidemia);
        actualizarVistaModelo();
        if (gv.modificado) {
            actionMap.get("guardarModelo").setEnabled(true); // NOI18N
            this.archivoModificado = true;
        }
    }

    /**
     * Acción que abre una ventana para que el usuario introduzca los resultados
     * que desea obtener, hace una copia de seguridad del modelo si así estuviera
     * indicado en las preferencias, e invoca la tarea de simulación del paquete
     * <CODE>delphsim.simulation</CODE> que corresponda con el método seleccionado
     * por el usuario en ellas.
     */
    @Action
    public Task iniciarContinua() {
        // Forzamos a tomar el valor del tiempo de inicio
        try {
            this.timeSpinner.commitEdit();
        } catch (ParseException ex) {
            //Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
        }
        ResourceMap resourceMap = getResourceMap();
        // Avisar de que se van a cerrar los resultados y se perderán
        if (this.epidemia.getResultados() != null && this.epidemia.getResultados().length > 0) {
            if (JOptionPane.showConfirmDialog(mainPanel, this.getResourceMap().getString("ResultadosSePerderan.msg1"), this.getResourceMap().getString("ResultadosSePerderan.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return null;
            }
        }
        // Cerrar/eliminar pestañas de resultados y mostrar la del modelo
        this.mostrarModelo();
        this.epidemia.eliminarResultados();
        while (this.PanelPestanas.getTabCount() > 1) {
            this.PanelPestanas.remove(1);
        }
        this.resultsWindowsMenu.removeAll();
        this.resultsWindowsMenu.setEnabled(false);
        // Mostrar ventana de especificación de resultados
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        IniciarContinua ic = new delphsim.IniciarContinua(mainFrame, true, this.epidemia);
        // Si el usuario ha cerrado la ventana o cancelado
        if (!ic.continuar()) {
            // Eliminar los resultados que se hayan podido crear y retornar
            this.epidemia.eliminarResultados();
            return null;
        }
        // Guardamos el tiempo de simulación pasándole a la epidemia el valor y
        // las unidades desde las cuales tiene que convertirlo
        this.epidemia.setTiempoSimulacion(
                ((Integer) this.timeSpinner.getValue()).floatValue(), 
                this.timeComboBox.getSelectedIndex());
        // Hacemos una copia de seguridad del modelo si así lo dicen las preferencias
        if (PreferenciasSimulacion.preferencias.get("autosave", PreferenciasSimulacion.autosavePorDefecto).equals("si")) { // NOI18N
            try {
                File backup = new File(new File(System.getProperty("java.class.path")).getParent() + resourceMap.getString("autoguardado.path")); // NOI18N
                this.epidemia.guardarXML(backup);
            } catch (IOException ex) {
                System.err.println("Advertencia: no se ha podido guardar automáticamente el modelo antes de la simulación. Mensaje: " + ex.getMessage()); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.WARNING, null, ex);
            } catch (DocumentException ex) {
                System.err.println("Advertencia: no se ha podido guardar automáticamente el modelo antes de la simulación. Mensaje: " + ex.getMessage()); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.WARNING, null, ex);
            } catch (Exception ex) {
                System.err.println("Advertencia: no se ha podido guardar automáticamente el modelo antes de la simulación. Mensaje: " + ex.getMessage()); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.WARNING, null, ex);
            }
        }
        // Por último, miramos qué método hay seleccionado en las preferencias y lo invocamos
        javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
        int metodo = Integer.valueOf(PreferenciasSimulacion.preferencias.get("metodo", PreferenciasSimulacion.metodoPorDefecto)); // NOI18N
        switch (metodo) {
            case 0:
                this.tareaActual = new EulerExplicito(getApplication(), this.epidemia, actionMap, this.PanelPestanas, this.resultsWindowsMenu, this.windowButtonGroup);
                break;
            case 1:
                this.tareaActual = new RK2Heun(getApplication(), this.epidemia, actionMap, this.PanelPestanas, this.resultsWindowsMenu, this.windowButtonGroup);
                break;
            case 2:
                this.tareaActual = new RungeKutta4(getApplication(), this.epidemia, actionMap, this.PanelPestanas, this.resultsWindowsMenu, this.windowButtonGroup);
                break;
            case 3:
                this.tareaActual = new EulerPredictorCorrector(getApplication(), this.epidemia, actionMap, this.PanelPestanas, this.resultsWindowsMenu, this.windowButtonGroup);
                break;
            case 4:
                this.tareaActual = new RungeKuttaFehlberg(getApplication(), this.epidemia, actionMap, this.PanelPestanas, this.resultsWindowsMenu, this.windowButtonGroup);
                break;
            default:
                this.tareaActual = null;
                break;
        }
        return this.tareaActual;
    }

    /**
     * Acción que avisa a la tarea en curso de que el usuario la ha cancelado.
     */
    @Action
    public void detenerSimulacion() {
        // Pedimos confirmación al usuario
        if (JOptionPane.showConfirmDialog(mainPanel, getResourceMap().getString("CancelarSimulacion.msg"), getResourceMap().getString("CancelarSimulacion.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) { // NOI18N
            return;
        }
        // Y cancelamos la tarea (si existe, aunque así debería ser)
        if (this.tareaActual != null) {
            this.tareaActual.cancel(true);
        }
    }

    /**
     * Acción que abre una ventana donde el usuario puede establecer algunos
     * parámetros de la simulación, entre ellos el método a emplear.
     */
    @Action
    public void preferenciasSimulacion() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        new PreferenciasSimulacion(mainFrame, true);
    }

    /**
     * Acción que abre un cuadro de diálogo en el que el usuario puede escoger
     * una ruta, nombre, formato y tamaño para guardar la gráfica que se está
     * mostrando en ese momento en un archivo.
     */
    @Action
    public void exportarGrafica() {
        ResourceMap resourceMap = this.getResourceMap();
        JFileChooser dialogoGuardar = new JFileChooser(this.archivoActual);
        
        JOptionPane pane = new JOptionPane(resourceMap.getString("JCExportarGrafica.resolucion.msg"), JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, null, null);
        pane.setWantsInput(true);
        pane.setSelectionValues(Resultado.RESOLUCIONES_DISPONIBLES);
        pane.setInitialSelectionValue(Resultado.RESOLUCIONES_DISPONIBLES[Resultado.RESOLUCION_POR_DEFECTO]);
        JDialog dialog = pane.createDialog(mainPanel, resourceMap.getString("JCExportarGrafica.resolucion.title"));
        pane.selectInitialValue();
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        dialog.dispose();
        Object res = pane.getInputValue();
        
        // Cadenas de texto del cuadro de diálogo y filtros de archivos ofertados
        dialogoGuardar.setDialogTitle(resourceMap.getString("JCExportarGrafica.title")); // NOI18N
        dialogoGuardar.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filtroSVG = new FileNameExtensionFilter(resourceMap.getString("JCExportarGrafica.filter1desc"), "svg"); // NOI18N
        dialogoGuardar.addChoosableFileFilter(filtroSVG);
        FileNameExtensionFilter filtroPNG = new FileNameExtensionFilter(resourceMap.getString("JCExportarGrafica.filter2desc"), "png"); // NOI18N
        dialogoGuardar.addChoosableFileFilter(filtroPNG);
        dialogoGuardar.setFileFilter(filtroPNG);
        
        // Mostramos el cuadro de diálogo y, si el usuario pulsa "Aceptar"
        int opcion = dialogoGuardar.showSaveDialog(getFrame());
        if (opcion == JFileChooser.APPROVE_OPTION) {
            // Primero averiguamos el nombre que debería tener
            String ruta = dialogoGuardar.getSelectedFile().getPath();
            String extension = ((FileNameExtensionFilter) dialogoGuardar.getFileFilter()).getExtensions()[0];
            if (!ruta.endsWith("." + extension)) { // NOI18N
                ruta += "." + extension; // NOI18N
            }
            // Si el archivo existe y el usuario no quiere reemplazarlo, volvemos
            File destino = new File(ruta);
            if (destino.exists()) {
                if (JOptionPane.showConfirmDialog(mainPanel, resourceMap.getString("JCExportarGrafica.confirmacion.msg", destino.getPath()), resourceMap.getString("JCExportarGrafica.confirmacion.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) { // NOI18N
                    return;
                }
                // Y si quiere reemplazarlo pero es de sólo lectura, mostramos error y volvemos
                if (!destino.canWrite()) {
                    JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCExportarGrafica.soloLectura.msg"), resourceMap.getString("JCExportarGrafica.soloLectura.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                    return;
                }
            }
            try {
                this.epidemia.getResultado(this.PanelPestanas.getSelectedIndex() - 1).exportarImagen(destino, res, extension);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCExportarGrafica.error.msg"), resourceMap.getString("JCExportarGrafica.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
                // Si no se ha podido guardar, pero se ha creado, se borra
                if (destino.exists()) {
                    destino.delete();
                }
            }
        }
    }

    /**
     * Acción que abre un cuadro de diálogo en el que el usuario puede escoger
     * una ruta, nombre, formato y número de puntos que se guardarán de la
     * gráfica que se está mostrando en formato textual.
     */
    @Action
    public void exportarInforme() {
        ResourceMap resourceMap = this.getResourceMap();
        JFileChooser dialogoGuardar = new JFileChooser(this.archivoActual);
        
        // Preguntamos al usuario cuántos puntos quiere guardar
        String numPuntosMensaje1 = resourceMap.getString("JCExportarInforme.numPuntos.msg1");
        String numPuntosMensaje2 = resourceMap.getString("JCExportarInforme.numPuntos.msg2");
        SpinnerNumberModel numPuntosSpinnerModel = new SpinnerNumberModel(0, 0, null, 1);
        JSpinner numPuntosSpinner = new JSpinner(numPuntosSpinnerModel);
        Object[] message = new Object[3];
        message[0] = numPuntosMensaje1;
        message[1] = numPuntosMensaje2;
        message[2] = numPuntosSpinner;
        JOptionPane pane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION);
        JDialog dialog = pane.createDialog(mainPanel, resourceMap.getString("JCExportarInforme.numPuntos.title"));
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        
        // Cadenas de texto del cuadro de diálogo y filtros de archivos ofertados
        dialogoGuardar.setDialogTitle(resourceMap.getString("JCExportarInforme.title")); // NOI18N
        dialogoGuardar.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filtroXML = new FileNameExtensionFilter(resourceMap.getString("JCExportarInforme.filter1desc"), "xml"); // NOI18N
        dialogoGuardar.addChoosableFileFilter(filtroXML);
        FileNameExtensionFilter filtroCSV = new FileNameExtensionFilter(resourceMap.getString("JCExportarInforme.filter2desc"), "csv"); // NOI18N
        dialogoGuardar.addChoosableFileFilter(filtroCSV);
        dialogoGuardar.setFileFilter(filtroCSV);
        
        // Mostramos el cuadro de diálogo y, si el usuario pulsa "Aceptar"
        int opcion = dialogoGuardar.showSaveDialog(getFrame());
        if (opcion == JFileChooser.APPROVE_OPTION) {
            // Primero averiguamos el nombre que debería tener
            String ruta = dialogoGuardar.getSelectedFile().getPath();
            String extension = ((FileNameExtensionFilter) dialogoGuardar.getFileFilter()).getExtensions()[0];
            if (!ruta.endsWith("." + extension)) { // NOI18N
                ruta += "." + extension; // NOI18N
            }
            // Si el archivo existe y el usuario no quiere reemplazarlo, volvemos
            File destino = new File(ruta);
            if (destino.exists()) {
                if (JOptionPane.showConfirmDialog(mainPanel, resourceMap.getString("JCExportarInforme.confirmacion.msg", destino.getPath()), resourceMap.getString("JCExportarInforme.confirmacion.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) { // NOI18N
                    return;
                }
                // Y si quiere reemplazarlo pero es de sólo lectura, mostramos error y volvemos
                if (!destino.canWrite()) {
                    JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCExportarInforme.soloLectura.msg"), resourceMap.getString("JCExportarInforme.soloLectura.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                    return;
                }
            }
            try {
                this.epidemia.getResultado(this.PanelPestanas.getSelectedIndex() - 1).exportarDatos(destino, (Integer)numPuntosSpinnerModel.getValue(), extension);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("JCExportarInforme.error.msg"), resourceMap.getString("JCExportarInforme.error.title"), JOptionPane.ERROR_MESSAGE); // NOI18N
                // Error detallado mostrado por consola
                // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
                // Si no se ha podido guardar, pero se ha creado, se borra
                if (destino.exists()) {
                    destino.delete();
                }
            }
        }
    }

    /**
     * Acción a realizar cuando se pulsa "Mostrar Modelo" del menú "Ventana".
     * Selecciona y muestra automáticamente la pestaña del modelo de la epidemia
     * del TabbedPane de la ventana principal.
     */
    @Action
    public void mostrarModelo() {
        this.PanelPestanas.setSelectedComponent(this.PestanaModelo);
    }

    /**
     * Acción a realizar cuando se pulsa un elemento del submenú "Resultados"
     * del menú "Ventana". Selecciona y muestra automáticamente la pestaña del
     * resultado correspondiente del TabbedPane de la ventana principal.
     */
    @Action
    public void mostrarResultado() {
        Enumeration<AbstractButton> elements = this.windowButtonGroup.getElements();
        int i = 0;
        while (elements.hasMoreElements()) {
            AbstractButton nextElement = elements.nextElement();
            if (nextElement.isSelected()) {
                this.PanelPestanas.setSelectedIndex(i);
                break;
            }
            i++;
        }
    }

    /**
     * Acción a realizar cuando el usuario escoja la opción "Manual de usuario"
     * del menú "Ayuda". Abre el manual de usuario en formato PDF con la
     * aplicación por defecto.
     */
    @Action
    public void manualUsuario() {
        ResourceMap resourceMap = getResourceMap();
        try {
            String archivo = new File(System.getProperty("java.class.path")).getParent() + this.getResourceMap().getString("Application.ManualPath"); // NOI18N
            java.awt.Desktop.getDesktop().open(new File(archivo));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("abrirManual.error.msg"), resourceMap.getString("abrirManual.error.title"), JOptionPane.WARNING_MESSAGE); // NOI18N
            // Error detallado mostrado por consola
            // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Acción a realizar cuando el usuario escoja la opción "Contenidos de la
     * Ayuda" del menú "Ayuda".
     */
    @Action
    public void mostrarAyuda() {
        ResourceMap resourceMap = getResourceMap();
        try {
            String archivo = new File(System.getProperty("java.class.path")).getParent() + this.getResourceMap().getString("Application.HelpIndexPath"); // NOI18N
            java.awt.Desktop.getDesktop().open(new File(archivo));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("abrirAyuda.error.msg"), resourceMap.getString("abrirAyuda.error.title"), JOptionPane.WARNING_MESSAGE); // NOI18N
            // Error detallado mostrado por consola
            // Logger.getLogger(DelphSimView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Acción a realizar cuando se escoge la opción "Acerca de..." del menú
     * "Ayuda". Crea y muestra una instancia de DelphSimAboutBox.
     */
    @Action
    public void mostrarAboutBox() {
        JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
        new DelphSimAboutBox(mainFrame);
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Mostrar Definición"
     * de la lista de compartimentos.
     */
    @Action
    public void mostrarDinamicaContinuaCompartimento() {
        ResourceMap resourceMap = getResourceMap();
        delphsim.model.Compartimento compSeleccionado = epidemia.getCompartimento(jListCompartimentos.getSelectedIndex());
        if (compSeleccionado.getDefinicionContinua() != null && !compSeleccionado.getDefinicionContinua().isEmpty()) {
            JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
            new delphsim.MostrarDinamicaContinuaCompartimento(mainFrame, true, compSeleccionado);
        } else {
            JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("NoDefinido.msg"), resourceMap.getString("NoDefinido.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
        }
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Mostrar Definición"
     * de la lista de parámetros.
     */
    @Action
    public void mostrarValorContinuaParametro() {
        ResourceMap resourceMap = getResourceMap();
        delphsim.model.Parametro parSeleccionado = epidemia.getParametros()[jListParametros.getSelectedIndex()];
        if (parSeleccionado.getDefinicionContinua() != null && !parSeleccionado.getDefinicionContinua().isEmpty()) {
            JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
            new delphsim.MostrarValorContinuaParametro(mainFrame, true, parSeleccionado);
        } else {
            JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("NoDefinido.msg"), resourceMap.getString("NoDefinido.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
        }
    }

    /**
     * Acción a realizar cuando el usuario pulsa el botón "Mostrar Definición"
     * de la lista de procesos.
     */
    @Action
    public void mostrarDinamicaContinuaProceso() {
        ResourceMap resourceMap = getResourceMap();
        delphsim.model.Proceso procSeleccionado = epidemia.getProceso(jListProcesos.getSelectedIndex());
        if (procSeleccionado.getTramosContinua() != null && procSeleccionado.getTramosContinua().length > 0) {
            JFrame mainFrame = DelphSimApp.getApplication().getMainFrame();
            new delphsim.MostrarDinamicaContinuaProceso(mainFrame, true, procSeleccionado);
        } else {
            JOptionPane.showMessageDialog(mainPanel, resourceMap.getString("NoDefinido.msg"), resourceMap.getString("NoDefinido.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
        }
    }

    /**
     * Método invocado cuando se hayan producido cambios en la epidemia y haya
     * que actualizar la pestaña del modelo con esos cambios.
     */
    private void actualizarVistaModelo() {
        jLabelNombre.setText(epidemia.getPoblacion().getNombre());
        jLabelHabitantes.setText(String.valueOf(epidemia.getPoblacion().getHabitantes()));
        jLabelUnidad.setText(epidemia.getUnidadTiempo());

        jTreeDistribucion.setRootVisible(false);
        jTreeDistribucion.getSelectionModel().setSelectionMode(javax.swing.tree.DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
        jTreeDistribucion.setModel(epidemia.getPoblacion().construirArbolDistribucion());

        try {
            javax.swing.ImageIcon divOpenIcon = getResourceMap().getImageIcon("jTreeDistribucion.divisionOpen.icon"); // NOI18N
            javax.swing.ImageIcon divCloseIcon = getResourceMap().getImageIcon("jTreeDistribucion.divisionClose.icon"); // NOI18N
            javax.swing.ImageIcon catIcon = getResourceMap().getImageIcon("jTreeDistribucion.categoria.icon"); // NOI18N
            if ((divOpenIcon != null) && (divCloseIcon != null) && (catIcon != null)) {
                javax.swing.tree.DefaultTreeCellRenderer renderer = new javax.swing.tree.DefaultTreeCellRenderer();
                renderer.setOpenIcon(divOpenIcon);
                renderer.setClosedIcon(divCloseIcon);
                renderer.setLeafIcon(catIcon);
                jTreeDistribucion.setCellRenderer(renderer);
            }
        } catch (Exception ex) {
            System.err.println("Error: se ha producido un fallo en el método DelphSimView::actualizarVistaModelo()"); // NOI18N
        }

        jListCompartimentos.setModel(epidemia.construirListaCompartimentos());
        jListParametros.setModel(epidemia.construirListaParametros());
        jListProcesos.setModel(epidemia.construirListaProcesos());

        DivisionHorizontal.setDividerLocation(this.getFrame().getWidth()/3);
        SubdivisionHorizontal.setDividerLocation(this.getFrame().getWidth()/3);
        PanelPestanas.setVisible(true);
    }

    /**
     * Este método se llama desde el constructor para iniciar los componentes.
     * ADVERTENCIA: NO modificar este código. El contenido de este método es
     * generado siempre por el Editor de Formularios de NetBeans 6.1.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        ToolBar = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        saveasButton = new javax.swing.JButton();
        toolbarSeparator1 = new javax.swing.JToolBar.Separator();
        populationButton = new javax.swing.JButton();
        behaviourButton = new javax.swing.JButton();
        variablesButton = new javax.swing.JButton();
        toolbarSeparator2 = new javax.swing.JToolBar.Separator();
        startSimButton = new javax.swing.JButton();
        stopSimButton = new javax.swing.JButton();
        optionsSimButton = new javax.swing.JButton();
        toolbarSeparator3 = new javax.swing.JToolBar.Separator();
        exportGraphicButton = new javax.swing.JButton();
        exportReportButton = new javax.swing.JButton();
        toolbarSeparator4 = new javax.swing.JToolBar.Separator();
        timeLabel = new javax.swing.JLabel();
        toolbarSeparator5 = new javax.swing.JToolBar.Separator();
        timeSpinner = new javax.swing.JSpinner();
        toolbarSeparator6 = new javax.swing.JToolBar.Separator();
        timeComboBox = new javax.swing.JComboBox();
        PanelPestanas = new javax.swing.JTabbedPane();
        PestanaModelo = new javax.swing.JPanel();
        DivisionHorizontal = new javax.swing.JSplitPane();
        jScrollPoblacion = new javax.swing.JScrollPane();
        jPanelPoblacion = new javax.swing.JPanel();
        jLabelTituloNombre = new javax.swing.JLabel();
        jLabelNombre = new javax.swing.JLabel();
        jLabelTituloHabitantes = new javax.swing.JLabel();
        jLabelHabitantes = new javax.swing.JLabel();
        jLabelTituloUnidad = new javax.swing.JLabel();
        jLabelUnidad = new javax.swing.JLabel();
        jLabelTituloDistribucion = new javax.swing.JLabel();
        jScrollDistribucion = new javax.swing.JScrollPane();
        jTreeDistribucion = new javax.swing.JTree();
        jLabelTituloDescCateg = new javax.swing.JLabel();
        jTextPaneDescCateg = new javax.swing.JTextPane();
        SubdivisionHorizontal = new javax.swing.JSplitPane();
        jScrollDinamica = new javax.swing.JScrollPane();
        jPanelDinamica = new javax.swing.JPanel();
        jLabelTituloCompartimentos = new javax.swing.JLabel();
        jScrollCompartimentos = new javax.swing.JScrollPane();
        jListCompartimentos = new javax.swing.JList();
        jLabelTituloPersonas = new javax.swing.JLabel();
        jLabelPersonas = new javax.swing.JLabel();
        jTextPaneTituloDinamicaComp = new javax.swing.JTextPane();
        jButtonCompContinua = new javax.swing.JButton();
        jScrollVariables = new javax.swing.JScrollPane();
        jTabbedVariables = new javax.swing.JTabbedPane();
        jPanelParametros = new javax.swing.JPanel();
        jLabelTituloParametros = new javax.swing.JLabel();
        jScrollParametros = new javax.swing.JScrollPane();
        jListParametros = new javax.swing.JList();
        jLabelTituloDescPar = new javax.swing.JLabel();
        jTextPaneDescPar = new javax.swing.JTextPane();
        jTextPaneTituloDinamicaPar = new javax.swing.JTextPane();
        jButtonParContinua = new javax.swing.JButton();
        jPanelProcesos = new javax.swing.JPanel();
        jLabelTituloProcesos = new javax.swing.JLabel();
        jScrollProcesos = new javax.swing.JScrollPane();
        jListProcesos = new javax.swing.JList();
        jLabelTituloDescProc = new javax.swing.JLabel();
        jTextPaneDescProc = new javax.swing.JTextPane();
        jTextPaneTituloDinamicaProc = new javax.swing.JTextPane();
        jButtonProcContinua = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu modelMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        modelSeparator1 = new javax.swing.JSeparator();
        populationMenuItem = new javax.swing.JMenuItem();
        behaviourMenuItem = new javax.swing.JMenuItem();
        variablesMenuItem = new javax.swing.JMenuItem();
        modelSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        simulationMenu = new javax.swing.JMenu();
        startSimMenuItem = new javax.swing.JMenuItem();
        stopSimMenuItem = new javax.swing.JMenuItem();
        simulationSeparator = new javax.swing.JSeparator();
        optionsSimMenuItem = new javax.swing.JMenuItem();
        resultsMenu = new javax.swing.JMenu();
        exportGraphicMenuItem = new javax.swing.JMenuItem();
        exportReportMenuItem = new javax.swing.JMenuItem();
        windowMenu = new javax.swing.JMenu();
        modelMenuItem = new javax.swing.JRadioButtonMenuItem();
        resultsWindowsMenu = new javax.swing.JMenu();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        userManualMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        windowButtonGroup = new javax.swing.ButtonGroup();

        mainPanel.setName("mainPanel"); // NOI18N

        ToolBar.setBorder(null);
        ToolBar.setFloatable(false);
        ToolBar.setRollover(true);
        ToolBar.setName("ToolBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getActionMap(DelphSimView.class, this);
        newButton.setAction(actionMap.get("nuevoModelo")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DelphSimView.class);
        newButton.setIcon(resourceMap.getIcon("newButton.icon")); // NOI18N
        newButton.setText(resourceMap.getString("newButton.text")); // NOI18N
        newButton.setToolTipText(resourceMap.getString("newButton.toolTipText")); // NOI18N
        newButton.setFocusable(false);
        newButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newButton.setName("newButton"); // NOI18N
        newButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(newButton);

        openButton.setAction(actionMap.get("abrirModelo")); // NOI18N
        openButton.setIcon(resourceMap.getIcon("openButton.icon")); // NOI18N
        openButton.setText(resourceMap.getString("openButton.text")); // NOI18N
        openButton.setToolTipText(resourceMap.getString("openButton.toolTipText")); // NOI18N
        openButton.setFocusable(false);
        openButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        openButton.setName("openButton"); // NOI18N
        openButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(openButton);

        saveButton.setAction(actionMap.get("guardarModelo")); // NOI18N
        saveButton.setIcon(resourceMap.getIcon("saveButton.icon")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setToolTipText(resourceMap.getString("saveButton.toolTipText")); // NOI18N
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setName("saveButton"); // NOI18N
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(saveButton);

        saveasButton.setAction(actionMap.get("guardarModeloComo")); // NOI18N
        saveasButton.setIcon(resourceMap.getIcon("saveasButton.icon")); // NOI18N
        saveasButton.setText(resourceMap.getString("saveasButton.text")); // NOI18N
        saveasButton.setToolTipText(resourceMap.getString("saveasButton.toolTipText")); // NOI18N
        saveasButton.setFocusable(false);
        saveasButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveasButton.setName("saveasButton"); // NOI18N
        saveasButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(saveasButton);

        toolbarSeparator1.setMaximumSize(new java.awt.Dimension(24, 32767));
        toolbarSeparator1.setMinimumSize(new java.awt.Dimension(24, 0));
        toolbarSeparator1.setName("toolbarSeparator1"); // NOI18N
        toolbarSeparator1.setPreferredSize(new java.awt.Dimension(24, 0));
        ToolBar.add(toolbarSeparator1);

        populationButton.setAction(actionMap.get("distribucionPoblacion")); // NOI18N
        populationButton.setIcon(resourceMap.getIcon("populationButton.icon")); // NOI18N
        populationButton.setText(resourceMap.getString("populationButton.text")); // NOI18N
        populationButton.setToolTipText(resourceMap.getString("populationButton.toolTipText")); // NOI18N
        populationButton.setFocusable(false);
        populationButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        populationButton.setName("populationButton"); // NOI18N
        populationButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(populationButton);

        behaviourButton.setAction(actionMap.get("dinamicaPoblacion")); // NOI18N
        behaviourButton.setIcon(resourceMap.getIcon("behaviourButton.icon")); // NOI18N
        behaviourButton.setText(resourceMap.getString("behaviourButton.text")); // NOI18N
        behaviourButton.setToolTipText(resourceMap.getString("behaviourButton.toolTipText")); // NOI18N
        behaviourButton.setFocusable(false);
        behaviourButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        behaviourButton.setName("behaviourButton"); // NOI18N
        behaviourButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(behaviourButton);

        variablesButton.setAction(actionMap.get("gestionarVariables")); // NOI18N
        variablesButton.setIcon(resourceMap.getIcon("variablesButton.icon")); // NOI18N
        variablesButton.setText(resourceMap.getString("variablesButton.text")); // NOI18N
        variablesButton.setToolTipText(resourceMap.getString("variablesButton.toolTipText")); // NOI18N
        variablesButton.setFocusable(false);
        variablesButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        variablesButton.setName("variablesButton"); // NOI18N
        variablesButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(variablesButton);

        toolbarSeparator2.setMaximumSize(new java.awt.Dimension(24, 32767));
        toolbarSeparator2.setMinimumSize(new java.awt.Dimension(24, 0));
        toolbarSeparator2.setName("toolbarSeparator2"); // NOI18N
        toolbarSeparator2.setPreferredSize(new java.awt.Dimension(24, 0));
        ToolBar.add(toolbarSeparator2);

        startSimButton.setAction(actionMap.get("iniciarContinua")); // NOI18N
        startSimButton.setIcon(resourceMap.getIcon("startSimButton.icon")); // NOI18N
        startSimButton.setText(resourceMap.getString("startSimButton.text")); // NOI18N
        startSimButton.setToolTipText(resourceMap.getString("startSimButton.toolTipText")); // NOI18N
        startSimButton.setFocusable(false);
        startSimButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startSimButton.setName("startSimButton"); // NOI18N
        startSimButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(startSimButton);

        stopSimButton.setAction(actionMap.get("detenerSimulacion")); // NOI18N
        stopSimButton.setIcon(resourceMap.getIcon("stopSimButton.icon")); // NOI18N
        stopSimButton.setText(resourceMap.getString("stopSimButton.text")); // NOI18N
        stopSimButton.setToolTipText(resourceMap.getString("stopSimButton.toolTipText")); // NOI18N
        stopSimButton.setFocusable(false);
        stopSimButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        stopSimButton.setName("stopSimButton"); // NOI18N
        stopSimButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(stopSimButton);

        optionsSimButton.setAction(actionMap.get("preferenciasSimulacion")); // NOI18N
        optionsSimButton.setIcon(resourceMap.getIcon("optionsSimButton.icon")); // NOI18N
        optionsSimButton.setText(resourceMap.getString("optionsSimButton.text")); // NOI18N
        optionsSimButton.setToolTipText(resourceMap.getString("optionsSimButton.toolTipText")); // NOI18N
        optionsSimButton.setFocusable(false);
        optionsSimButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        optionsSimButton.setName("optionsSimButton"); // NOI18N
        optionsSimButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(optionsSimButton);

        toolbarSeparator3.setMaximumSize(new java.awt.Dimension(24, 32767));
        toolbarSeparator3.setMinimumSize(new java.awt.Dimension(24, 0));
        toolbarSeparator3.setName("toolbarSeparator3"); // NOI18N
        toolbarSeparator3.setPreferredSize(new java.awt.Dimension(24, 0));
        ToolBar.add(toolbarSeparator3);

        exportGraphicButton.setAction(actionMap.get("exportarGrafica")); // NOI18N
        exportGraphicButton.setIcon(resourceMap.getIcon("exportGraphicButton.icon")); // NOI18N
        exportGraphicButton.setText(resourceMap.getString("exportGraphicButton.text")); // NOI18N
        exportGraphicButton.setToolTipText(resourceMap.getString("exportGraphicButton.toolTipText")); // NOI18N
        exportGraphicButton.setFocusable(false);
        exportGraphicButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exportGraphicButton.setName("exportGraphicButton"); // NOI18N
        exportGraphicButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(exportGraphicButton);

        exportReportButton.setAction(actionMap.get("exportarInforme")); // NOI18N
        exportReportButton.setIcon(resourceMap.getIcon("exportReportButton.icon")); // NOI18N
        exportReportButton.setText(resourceMap.getString("exportReportButton.text")); // NOI18N
        exportReportButton.setToolTipText(resourceMap.getString("exportReportButton.toolTipText")); // NOI18N
        exportReportButton.setFocusable(false);
        exportReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exportReportButton.setName("exportReportButton"); // NOI18N
        exportReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        ToolBar.add(exportReportButton);

        toolbarSeparator4.setMaximumSize(new java.awt.Dimension(48, 32767));
        toolbarSeparator4.setMinimumSize(new java.awt.Dimension(48, 0));
        toolbarSeparator4.setName("toolbarSeparator4"); // NOI18N
        toolbarSeparator4.setPreferredSize(new java.awt.Dimension(48, 0));
        ToolBar.add(toolbarSeparator4);

        timeLabel.setText(resourceMap.getString("timeLabel.text")); // NOI18N
        timeLabel.setName("timeLabel"); // NOI18N
        ToolBar.add(timeLabel);

        toolbarSeparator5.setMaximumSize(new java.awt.Dimension(20, 32767));
        toolbarSeparator5.setMinimumSize(new java.awt.Dimension(20, 0));
        toolbarSeparator5.setName("toolbarSeparator5"); // NOI18N
        toolbarSeparator5.setPreferredSize(new java.awt.Dimension(20, 0));
        ToolBar.add(toolbarSeparator5);

        timeSpinner.setFont(resourceMap.getFont("timeSpinner.font")); // NOI18N
        timeSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        timeSpinner.setEnabled(false);
        timeSpinner.setMaximumSize(new java.awt.Dimension(100, 20));
        timeSpinner.setMinimumSize(new java.awt.Dimension(100, 20));
        timeSpinner.setName("timeSpinner"); // NOI18N
        timeSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        ToolBar.add(timeSpinner);

        toolbarSeparator6.setMaximumSize(new java.awt.Dimension(10, 32767));
        toolbarSeparator6.setMinimumSize(new java.awt.Dimension(10, 0));
        toolbarSeparator6.setName("toolbarSeparator6"); // NOI18N
        toolbarSeparator6.setPreferredSize(new java.awt.Dimension(10, 0));
        ToolBar.add(toolbarSeparator6);

        timeComboBox.setBackground(resourceMap.getColor("timeComboBox.background")); // NOI18N
        timeComboBox.setFont(resourceMap.getFont("timeComboBox.font")); // NOI18N
        timeComboBox.setModel(this.unidadesTiempo);
        timeComboBox.setEnabled(false);
        timeComboBox.setMaximumSize(new java.awt.Dimension(100, 20));
        timeComboBox.setMinimumSize(new java.awt.Dimension(100, 20));
        timeComboBox.setName("timeComboBox"); // NOI18N
        timeComboBox.setPreferredSize(new java.awt.Dimension(100, 20));
        ToolBar.add(timeComboBox);

        PanelPestanas.setName("PanelPestanas"); // NOI18N
        PanelPestanas.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PanelPestanasStateChanged(evt);
            }
        });

        PestanaModelo.setName("PestanaModelo"); // NOI18N

        DivisionHorizontal.setName("DivisionHorizontal"); // NOI18N
        DivisionHorizontal.setOneTouchExpandable(true);

        jScrollPoblacion.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jScrollPoblacion.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jScrollPoblacion.border.titleFont"), resourceMap.getColor("jScrollPoblacion.border.titleColor"))); // NOI18N
        jScrollPoblacion.setName("jScrollPoblacion"); // NOI18N

        jPanelPoblacion.setMinimumSize(new java.awt.Dimension(320, 400));
        jPanelPoblacion.setName("jPanelPoblacion"); // NOI18N
        jPanelPoblacion.setPreferredSize(new java.awt.Dimension(320, 400));

        jLabelTituloNombre.setText(resourceMap.getString("jLabelTituloNombre.text")); // NOI18N
        jLabelTituloNombre.setName("jLabelTituloNombre"); // NOI18N

        jLabelNombre.setFont(resourceMap.getFont("jLabelNombre.font")); // NOI18N
        jLabelNombre.setText(resourceMap.getString("jLabelNombre.text")); // NOI18N
        jLabelNombre.setName("jLabelNombre"); // NOI18N

        jLabelTituloHabitantes.setText(resourceMap.getString("jLabelTituloHabitantes.text")); // NOI18N
        jLabelTituloHabitantes.setName("jLabelTituloHabitantes"); // NOI18N

        jLabelHabitantes.setFont(resourceMap.getFont("jLabelHabitantes.font")); // NOI18N
        jLabelHabitantes.setText(resourceMap.getString("jLabelHabitantes.text")); // NOI18N
        jLabelHabitantes.setName("jLabelHabitantes"); // NOI18N

        jLabelTituloUnidad.setText(resourceMap.getString("jLabelTituloUnidad.text")); // NOI18N
        jLabelTituloUnidad.setName("jLabelTituloUnidad"); // NOI18N

        jLabelUnidad.setFont(resourceMap.getFont("jLabelUnidad.font")); // NOI18N
        jLabelUnidad.setText(resourceMap.getString("jLabelUnidad.text")); // NOI18N
        jLabelUnidad.setName("jLabelUnidad"); // NOI18N

        jLabelTituloDistribucion.setText(resourceMap.getString("jLabelTituloDistribucion.text")); // NOI18N
        jLabelTituloDistribucion.setName("jLabelTituloDistribucion"); // NOI18N

        jScrollDistribucion.setName("jScrollDistribucion"); // NOI18N

        jTreeDistribucion.setModel(null);
        jTreeDistribucion.setName("jTreeDistribucion"); // NOI18N
        jTreeDistribucion.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTreeDistribucionValueChanged(evt);
            }
        });
        jScrollDistribucion.setViewportView(jTreeDistribucion);

        jLabelTituloDescCateg.setText(resourceMap.getString("jLabelTituloDescCateg.text")); // NOI18N
        jLabelTituloDescCateg.setName("jLabelTituloDescCateg"); // NOI18N

        jTextPaneDescCateg.setBackground(getFrame().getBackground());
        jTextPaneDescCateg.setBorder(null);
        jTextPaneDescCateg.setEditable(false);
        jTextPaneDescCateg.setText(resourceMap.getString("jTextPaneDescCateg.text")); // NOI18N
        jTextPaneDescCateg.setName("jTextPaneDescCateg"); // NOI18N

        javax.swing.GroupLayout jPanelPoblacionLayout = new javax.swing.GroupLayout(jPanelPoblacion);
        jPanelPoblacion.setLayout(jPanelPoblacionLayout);
        jPanelPoblacionLayout.setHorizontalGroup(
            jPanelPoblacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPoblacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPoblacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTituloDescCateg)
                    .addGroup(jPanelPoblacionLayout.createSequentialGroup()
                        .addComponent(jLabelTituloNombre)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelNombre))
                    .addGroup(jPanelPoblacionLayout.createSequentialGroup()
                        .addComponent(jLabelTituloHabitantes)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHabitantes))
                    .addGroup(jPanelPoblacionLayout.createSequentialGroup()
                        .addComponent(jLabelTituloUnidad)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelUnidad))
                    .addComponent(jTextPaneDescCateg, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(jScrollDistribucion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
                    .addComponent(jLabelTituloDistribucion))
                .addContainerGap())
        );
        jPanelPoblacionLayout.setVerticalGroup(
            jPanelPoblacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelPoblacionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelPoblacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloNombre)
                    .addComponent(jLabelNombre))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPoblacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloHabitantes)
                    .addComponent(jLabelHabitantes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelPoblacionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTituloUnidad)
                    .addComponent(jLabelUnidad))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelTituloDistribucion)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollDistribucion, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelTituloDescCateg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextPaneDescCateg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jScrollPoblacion.setViewportView(jPanelPoblacion);

        DivisionHorizontal.setLeftComponent(jScrollPoblacion);

        SubdivisionHorizontal.setDividerLocation(360);
        SubdivisionHorizontal.setName("SubdivisionHorizontal"); // NOI18N
        SubdivisionHorizontal.setOneTouchExpandable(true);

        jScrollDinamica.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jScrollDinamica.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jScrollDinamica.border.titleFont"), resourceMap.getColor("jScrollDinamica.border.titleColor"))); // NOI18N
        jScrollDinamica.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollDinamica.setName("jScrollDinamica"); // NOI18N
        jScrollDinamica.setPreferredSize(new java.awt.Dimension(0, 0));

        jPanelDinamica.setMinimumSize(new java.awt.Dimension(300, 400));
        jPanelDinamica.setName("jPanelDinamica"); // NOI18N
        jPanelDinamica.setPreferredSize(new java.awt.Dimension(300, 400));

        jLabelTituloCompartimentos.setText(resourceMap.getString("jLabelTituloCompartimentos.text")); // NOI18N
        jLabelTituloCompartimentos.setName("jLabelTituloCompartimentos"); // NOI18N

        jScrollCompartimentos.setName("jScrollCompartimentos"); // NOI18N

        jListCompartimentos.setFont(resourceMap.getFont("jListCompartimentos.font")); // NOI18N
        jListCompartimentos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListCompartimentos.setName("jListCompartimentos"); // NOI18N
        jListCompartimentos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListCompartimentosValueChanged(evt);
            }
        });
        jScrollCompartimentos.setViewportView(jListCompartimentos);

        jLabelTituloPersonas.setText(resourceMap.getString("jLabelTituloPersonas.text")); // NOI18N
        jLabelTituloPersonas.setName("jLabelTituloPersonas"); // NOI18N

        jLabelPersonas.setFont(resourceMap.getFont("jLabelPersonas.font")); // NOI18N
        jLabelPersonas.setText(resourceMap.getString("jLabelPersonas.text")); // NOI18N
        jLabelPersonas.setToolTipText(resourceMap.getString("jLabelPersonas.toolTipText")); // NOI18N
        jLabelPersonas.setName("jLabelPersonas"); // NOI18N

        jTextPaneTituloDinamicaComp.setBackground(getFrame().getBackground());
        jTextPaneTituloDinamicaComp.setBorder(null);
        jTextPaneTituloDinamicaComp.setEditable(false);
        jTextPaneTituloDinamicaComp.setFont(resourceMap.getFont("jTextPaneTituloDinamicaComp.font")); // NOI18N
        jTextPaneTituloDinamicaComp.setText(resourceMap.getString("jTextPaneTituloDinamicaComp.text")); // NOI18N
        jTextPaneTituloDinamicaComp.setName("jTextPaneTituloDinamicaComp"); // NOI18N

        jButtonCompContinua.setAction(actionMap.get("mostrarDinamicaContinuaCompartimento")); // NOI18N
        jButtonCompContinua.setName("jButtonCompContinua"); // NOI18N

        javax.swing.GroupLayout jPanelDinamicaLayout = new javax.swing.GroupLayout(jPanelDinamica);
        jPanelDinamica.setLayout(jPanelDinamicaLayout);
        jPanelDinamicaLayout.setHorizontalGroup(
            jPanelDinamicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelDinamicaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelDinamicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollCompartimentos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jLabelTituloCompartimentos, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextPaneTituloDinamicaComp, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                    .addComponent(jLabelPersonas, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTituloPersonas, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCompContinua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelDinamicaLayout.setVerticalGroup(
            jPanelDinamicaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelDinamicaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTituloCompartimentos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollCompartimentos, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTituloPersonas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelPersonas)
                .addGap(18, 18, 18)
                .addComponent(jTextPaneTituloDinamicaComp, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonCompContinua)
                .addContainerGap())
        );

        jScrollDinamica.setViewportView(jPanelDinamica);

        SubdivisionHorizontal.setLeftComponent(jScrollDinamica);

        jScrollVariables.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jScrollVariables.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jScrollVariables.border.titleFont"), resourceMap.getColor("jScrollVariables.border.titleColor"))); // NOI18N
        jScrollVariables.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollVariables.setName("jScrollVariables"); // NOI18N
        jScrollVariables.setPreferredSize(new java.awt.Dimension(0, 0));

        jTabbedVariables.setMinimumSize(new java.awt.Dimension(280, 400));
        jTabbedVariables.setName("jTabbedVariables"); // NOI18N
        jTabbedVariables.setPreferredSize(new java.awt.Dimension(280, 400));

        jPanelParametros.setMinimumSize(new java.awt.Dimension(0, 0));
        jPanelParametros.setName("jPanelParametros"); // NOI18N
        jPanelParametros.setPreferredSize(new java.awt.Dimension(0, 0));

        jLabelTituloParametros.setText(resourceMap.getString("jLabelTituloParametros.text")); // NOI18N
        jLabelTituloParametros.setName("jLabelTituloParametros"); // NOI18N

        jScrollParametros.setName("jScrollParametros"); // NOI18N

        jListParametros.setFont(resourceMap.getFont("jListParametros.font")); // NOI18N
        jListParametros.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListParametros.setName("jListParametros"); // NOI18N
        jListParametros.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListParametrosValueChanged(evt);
            }
        });
        jScrollParametros.setViewportView(jListParametros);

        jLabelTituloDescPar.setText(resourceMap.getString("jLabelTituloDescPar.text")); // NOI18N
        jLabelTituloDescPar.setName("jLabelTituloDescPar"); // NOI18N

        jTextPaneDescPar.setBackground(getFrame().getBackground());
        jTextPaneDescPar.setBorder(null);
        jTextPaneDescPar.setEditable(false);
        jTextPaneDescPar.setText(resourceMap.getString("jTextPaneDescPar.text")); // NOI18N
        jTextPaneDescPar.setName("jTextPaneDescPar"); // NOI18N

        jTextPaneTituloDinamicaPar.setBackground(getFrame().getBackground());
        jTextPaneTituloDinamicaPar.setBorder(null);
        jTextPaneTituloDinamicaPar.setEditable(false);
        jTextPaneTituloDinamicaPar.setFont(resourceMap.getFont("jTextPaneTituloDinamicaPar.font")); // NOI18N
        jTextPaneTituloDinamicaPar.setText(resourceMap.getString("jTextPaneTituloDinamicaPar.text")); // NOI18N
        jTextPaneTituloDinamicaPar.setName("jTextPaneTituloDinamicaPar"); // NOI18N

        jButtonParContinua.setAction(actionMap.get("mostrarValorContinuaParametro")); // NOI18N
        jButtonParContinua.setName("jButtonParContinua"); // NOI18N

        javax.swing.GroupLayout jPanelParametrosLayout = new javax.swing.GroupLayout(jPanelParametros);
        jPanelParametros.setLayout(jPanelParametrosLayout);
        jPanelParametrosLayout.setHorizontalGroup(
            jPanelParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelParametrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollParametros, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(jLabelTituloParametros, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextPaneTituloDinamicaPar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(jTextPaneDescPar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(jLabelTituloDescPar, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonParContinua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelParametrosLayout.setVerticalGroup(
            jPanelParametrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelParametrosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTituloParametros)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollParametros, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTituloDescPar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextPaneDescPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextPaneTituloDinamicaPar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonParContinua)
                .addContainerGap())
        );

        jTabbedVariables.addTab(resourceMap.getString("jPanelParametros.TabConstraints.tabTitle"), jPanelParametros); // NOI18N

        jPanelProcesos.setName("jPanelProcesos"); // NOI18N

        jLabelTituloProcesos.setText(resourceMap.getString("jLabelTituloProcesos.text")); // NOI18N
        jLabelTituloProcesos.setName("jLabelTituloProcesos"); // NOI18N

        jScrollProcesos.setName("jScrollProcesos"); // NOI18N

        jListProcesos.setFont(resourceMap.getFont("jListProcesos.font")); // NOI18N
        jListProcesos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListProcesos.setName("jListProcesos"); // NOI18N
        jListProcesos.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListProcesosValueChanged(evt);
            }
        });
        jScrollProcesos.setViewportView(jListProcesos);

        jLabelTituloDescProc.setText(resourceMap.getString("jLabelTituloDescProc.text")); // NOI18N
        jLabelTituloDescProc.setName("jLabelTituloDescProc"); // NOI18N

        jTextPaneDescProc.setBackground(getFrame().getBackground());
        jTextPaneDescProc.setBorder(null);
        jTextPaneDescProc.setEditable(false);
        jTextPaneDescProc.setText(resourceMap.getString("jTextPaneDescProc.text")); // NOI18N
        jTextPaneDescProc.setName("jTextPaneDescProc"); // NOI18N

        jTextPaneTituloDinamicaProc.setBackground(getFrame().getBackground());
        jTextPaneTituloDinamicaProc.setBorder(null);
        jTextPaneTituloDinamicaProc.setEditable(false);
        jTextPaneTituloDinamicaProc.setFont(resourceMap.getFont("jTextPaneTituloDinamicaProc.font")); // NOI18N
        jTextPaneTituloDinamicaProc.setText(resourceMap.getString("jTextPaneTituloDinamicaProc.text")); // NOI18N
        jTextPaneTituloDinamicaProc.setName("jTextPaneTituloDinamicaProc"); // NOI18N

        jButtonProcContinua.setAction(actionMap.get("mostrarDinamicaContinuaProceso")); // NOI18N
        jButtonProcContinua.setName("jButtonProcContinua"); // NOI18N

        javax.swing.GroupLayout jPanelProcesosLayout = new javax.swing.GroupLayout(jPanelProcesos);
        jPanelProcesos.setLayout(jPanelProcesosLayout);
        jPanelProcesosLayout.setHorizontalGroup(
            jPanelProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelProcesosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollProcesos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(jLabelTituloProcesos, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextPaneTituloDinamicaProc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(jTextPaneDescProc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
                    .addComponent(jLabelTituloDescProc, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonProcContinua, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelProcesosLayout.setVerticalGroup(
            jPanelProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelProcesosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelTituloProcesos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollProcesos, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jLabelTituloDescProc)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextPaneDescProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextPaneTituloDinamicaProc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButtonProcContinua)
                .addContainerGap())
        );

        jTabbedVariables.addTab(resourceMap.getString("jPanelProcesos.TabConstraints.tabTitle"), jPanelProcesos); // NOI18N

        jScrollVariables.setViewportView(jTabbedVariables);

        SubdivisionHorizontal.setRightComponent(jScrollVariables);

        DivisionHorizontal.setRightComponent(SubdivisionHorizontal);

        javax.swing.GroupLayout PestanaModeloLayout = new javax.swing.GroupLayout(PestanaModelo);
        PestanaModelo.setLayout(PestanaModeloLayout);
        PestanaModeloLayout.setHorizontalGroup(
            PestanaModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DivisionHorizontal, javax.swing.GroupLayout.DEFAULT_SIZE, 1093, Short.MAX_VALUE)
        );
        PestanaModeloLayout.setVerticalGroup(
            PestanaModeloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(DivisionHorizontal, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)
        );

        PanelPestanas.addTab(resourceMap.getString("PestanaModelo.TabConstraints.tabTitle"), resourceMap.getIcon("PestanaModelo.TabConstraints.tabIcon"), PestanaModelo, resourceMap.getString("PestanaModelo.TabConstraints.tabToolTip")); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ToolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
            .addComponent(PanelPestanas, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(ToolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelPestanas, javax.swing.GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        modelMenu.setText(resourceMap.getString("modelMenu.text")); // NOI18N
        modelMenu.setMinimumSize(new java.awt.Dimension(53, 21));
        modelMenu.setName("modelMenu"); // NOI18N

        newMenuItem.setAction(actionMap.get("nuevoModelo")); // NOI18N
        newMenuItem.setName("newMenuItem"); // NOI18N
        modelMenu.add(newMenuItem);

        openMenuItem.setAction(actionMap.get("abrirModelo")); // NOI18N
        openMenuItem.setName("openMenuItem"); // NOI18N
        modelMenu.add(openMenuItem);

        saveMenuItem.setAction(actionMap.get("guardarModelo")); // NOI18N
        saveMenuItem.setName("saveMenuItem"); // NOI18N
        modelMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("guardarModeloComo")); // NOI18N
        saveAsMenuItem.setName("saveAsMenuItem"); // NOI18N
        modelMenu.add(saveAsMenuItem);

        modelSeparator1.setName("modelSeparator1"); // NOI18N
        modelMenu.add(modelSeparator1);

        populationMenuItem.setAction(actionMap.get("distribucionPoblacion")); // NOI18N
        populationMenuItem.setName("populationMenuItem"); // NOI18N
        modelMenu.add(populationMenuItem);

        behaviourMenuItem.setAction(actionMap.get("dinamicaPoblacion")); // NOI18N
        behaviourMenuItem.setName("behaviourMenuItem"); // NOI18N
        modelMenu.add(behaviourMenuItem);

        variablesMenuItem.setAction(actionMap.get("gestionarVariables")); // NOI18N
        variablesMenuItem.setName("variablesMenuItem"); // NOI18N
        modelMenu.add(variablesMenuItem);

        modelSeparator2.setName("modelSeparator2"); // NOI18N
        modelMenu.add(modelSeparator2);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        modelMenu.add(exitMenuItem);

        menuBar.add(modelMenu);

        simulationMenu.setText(resourceMap.getString("simulationMenu.text")); // NOI18N
        simulationMenu.setMinimumSize(new java.awt.Dimension(71, 21));
        simulationMenu.setName("simulationMenu"); // NOI18N

        startSimMenuItem.setAction(actionMap.get("iniciarContinua")); // NOI18N
        startSimMenuItem.setName("startSimMenuItem"); // NOI18N
        simulationMenu.add(startSimMenuItem);

        stopSimMenuItem.setAction(actionMap.get("detenerSimulacion")); // NOI18N
        stopSimMenuItem.setName("stopSimMenuItem"); // NOI18N
        simulationMenu.add(stopSimMenuItem);

        simulationSeparator.setName("simulationSeparator"); // NOI18N
        simulationMenu.add(simulationSeparator);

        optionsSimMenuItem.setAction(actionMap.get("preferenciasSimulacion")); // NOI18N
        optionsSimMenuItem.setName("optionsSimMenuItem"); // NOI18N
        simulationMenu.add(optionsSimMenuItem);

        menuBar.add(simulationMenu);

        resultsMenu.setText(resourceMap.getString("resultsMenu.text")); // NOI18N
        resultsMenu.setMinimumSize(new java.awt.Dimension(69, 21));
        resultsMenu.setName("resultsMenu"); // NOI18N

        exportGraphicMenuItem.setAction(actionMap.get("exportarGrafica")); // NOI18N
        exportGraphicMenuItem.setName("exportGraphicMenuItem"); // NOI18N
        resultsMenu.add(exportGraphicMenuItem);

        exportReportMenuItem.setAction(actionMap.get("exportarInforme")); // NOI18N
        exportReportMenuItem.setName("exportReportMenuItem"); // NOI18N
        resultsMenu.add(exportReportMenuItem);

        menuBar.add(resultsMenu);

        windowMenu.setText(resourceMap.getString("windowMenu.text")); // NOI18N
        windowMenu.setMinimumSize(new java.awt.Dimension(55, 21));
        windowMenu.setName("windowMenu"); // NOI18N

        modelMenuItem.setAction(actionMap.get("mostrarModelo")); // NOI18N
        windowButtonGroup.add(modelMenuItem);
        modelMenuItem.setName("modelMenuItem"); // NOI18N
        windowMenu.add(modelMenuItem);

        resultsWindowsMenu.setIcon(resourceMap.getIcon("resultsWindowsMenu.icon")); // NOI18N
        resultsWindowsMenu.setText(resourceMap.getString("resultsWindowsMenu.text")); // NOI18N
        resultsWindowsMenu.setName("resultsWindowsMenu"); // NOI18N
        windowMenu.add(resultsWindowsMenu);

        menuBar.add(windowMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setMinimumSize(new java.awt.Dimension(47, 21));
        helpMenu.setName("helpMenu"); // NOI18N

        contentsMenuItem.setAction(actionMap.get("mostrarAyuda")); // NOI18N
        contentsMenuItem.setName("contentsMenuItem"); // NOI18N
        helpMenu.add(contentsMenuItem);

        userManualMenuItem.setAction(actionMap.get("manualUsuario")); // NOI18N
        userManualMenuItem.setName("userManualMenuItem"); // NOI18N
        helpMenu.add(userManualMenuItem);

        aboutMenuItem.setAction(actionMap.get("mostrarAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1098, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 914, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(ToolBar);
    }// </editor-fold>//GEN-END:initComponents

/**
 * Método que, a partir del índice seleccionado, recoge el número de
 * personas del compartimento y lo pone en el texto de <CODE>jLabelPersonas</CODE>.
 * @param evt El evento generado.
 */
private void jListCompartimentosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListCompartimentosValueChanged
    try {
        ResourceMap resourceMap = getResourceMap();
        if (jListCompartimentos.getSelectedIndex() != -1) {
            jLabelPersonas.setText(String.valueOf(epidemia.getCompartimentos()[jListCompartimentos.getSelectedIndex()].getHabitantes()));
            jButtonCompContinua.setEnabled(true);
        } else {
            jLabelPersonas.setText(resourceMap.getString("jLabelPersonas.text")); // NOI18N
            jButtonCompContinua.setEnabled(false);
        }
    } catch (Exception ex) {
        System.err.println("Error: se ha producido un fallo en el método DelphSimView::jListCompartimentosValueChanged, evento disparado al seleccionar un elemento de la lista de compartimentos"); // NOI18N
    }
}//GEN-LAST:event_jListCompartimentosValueChanged

/**
 * Método que, a partir del índice seleccionado, recoge la descripción del
 * parámetro seleccionado y lo pone en el texto de <CODE>jTextPaneDescPar</CODE>.
 * @param evt El evento generado.
 */
private void jListParametrosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListParametrosValueChanged
    try {
        ResourceMap resourceMap = getResourceMap();
        if (jListParametros.getSelectedIndex() != -1) {
            String texto = epidemia.getParametros()[jListParametros.getSelectedIndex()].getDescripcion();
            if ((texto == null) || (texto.equals(""))) { // NOI18N
                jTextPaneDescPar.setText(resourceMap.getString("jTextPaneDescPar.undefined")); // NOI18N
            } else {
                jTextPaneDescPar.setText(texto);
            }
            jButtonParContinua.setEnabled(true);
        } else {
            jTextPaneDescPar.setText(resourceMap.getString("jTextPaneDescPar.text")); // NOI18N
            jButtonParContinua.setEnabled(false);
        }
    } catch (Exception ex) {
        System.err.println("Error: se ha producido un fallo en el método DelphSimView::jListParametrosValueChanged, evento disparado al seleccionar un elemento de la lista de parámetros"); // NOI18N
    }
}//GEN-LAST:event_jListParametrosValueChanged

/**
 * Método que, a partir del índice seleccionado, recoge la descripción del
 * proceso seleccionado y lo pone en el texto de <CODE>jTextPaneDescProc</CODE>.
 * @param evt El evento generado.
 */
private void jListProcesosValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListProcesosValueChanged
    try {
        ResourceMap resourceMap = getResourceMap();
        if (jListProcesos.getSelectedIndex() != -1) {
            String texto = epidemia.getProcesos()[jListProcesos.getSelectedIndex()].getDescripcion();
            if ((texto == null) || (texto.equals(""))) { // NOI18N
                jTextPaneDescProc.setText(resourceMap.getString("jTextPaneDescProc.undefined")); // NOI18N
            } else {
                jTextPaneDescProc.setText(texto);
            }
            jButtonProcContinua.setEnabled(true);
        } else {
            jTextPaneDescProc.setText(resourceMap.getString("jTextPaneDescProc.text")); // NOI18N
            jButtonProcContinua.setEnabled(false);
        }
    } catch (Exception ex) {
        System.err.println("Error: se ha producido un fallo en el método DelphSimView::jListProcesosValueChanged, evento disparado al seleccionar un elemento de la lista de procesos"); // NOI18N
    }
}//GEN-LAST:event_jListProcesosValueChanged

/**
 * Método que, a partir del elemento hoja seleccionado, obtiene la
 * descripción de la categoría y la muestra en el texto de <CODE>jTextPaneDescCateg</CODE>.
 * @param evt El evento generado.
 */
private void jTreeDistribucionValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTreeDistribucionValueChanged
    try {
        ResourceMap resourceMap = getResourceMap();
        if (jTreeDistribucion.getSelectionPath() != null) {
            javax.swing.tree.DefaultMutableTreeNode nodoSeleccionado = (javax.swing.tree.DefaultMutableTreeNode) jTreeDistribucion.getSelectionPath().getLastPathComponent();
            if (nodoSeleccionado.isLeaf()) {
                javax.swing.tree.DefaultMutableTreeNode raiz = (javax.swing.tree.DefaultMutableTreeNode) jTreeDistribucion.getSelectionPath().getPathComponent(0);
                javax.swing.tree.DefaultMutableTreeNode div = (javax.swing.tree.DefaultMutableTreeNode) jTreeDistribucion.getSelectionPath().getPathComponent(1);
                javax.swing.tree.DefaultMutableTreeNode cat = (javax.swing.tree.DefaultMutableTreeNode) jTreeDistribucion.getSelectionPath().getPathComponent(2);
                String texto = epidemia.getPoblacion().getDivisiones()[raiz.getIndex(div)].getCategorias()[div.getIndex(cat)].getDescripcion();
                if ((texto == null) || (texto.equals(""))) { // NOI18N
                    jTextPaneDescCateg.setText(resourceMap.getString("jTextPaneDescCateg.undefined")); // NOI18N
                } else {
                    jTextPaneDescCateg.setText(texto);
                }
            } else {
                jTextPaneDescCateg.setText(resourceMap.getString("jTextPaneDescCateg.text")); // NOI18N
            }
        } else {
            jTextPaneDescCateg.setText(resourceMap.getString("jTextPaneDescCateg.text")); // NOI18N
        }
    } catch (Exception ex) {
        System.err.println("Error: se ha producido un fallo en el método DelphSimView::jTreeDistribucionValueChanged, evento disparado al seleccionar un elemento del árbol de divisiones/categorías"); // NOI18N
    }
}//GEN-LAST:event_jTreeDistribucionValueChanged

/**
 * Método para detectar un cambio de pestaña y, si es la del modelo, no dejar
 * exportar; si fuera de algún resultado, sí permitirlo.
 * @param evt El evento generado.
 */
private void PanelPestanasStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PanelPestanasStateChanged
    // Recuperamos el mapa de acciones que vamos a necesitar
    javax.swing.ActionMap actionMap = getContext().getActionMap(DelphSimView.class, this);
    // Si se tiene seleccionada la pestaña del modelo (la número 0)
    int seleccion = this.PanelPestanas.getSelectedIndex();
    switch (seleccion) {
        case -1:
            break;
        case 0:
            actionMap.get("exportarGrafica").setEnabled(false); // NOI18N
            actionMap.get("exportarInforme").setEnabled(false); // NOI18N
            this.modelMenuItem.setSelected(true);
            break;
        default:
            actionMap.get("exportarGrafica").setEnabled(true); // NOI18N
            actionMap.get("exportarInforme").setEnabled(true); // NOI18N
            ((javax.swing.JRadioButtonMenuItem)this.resultsWindowsMenu.getMenuComponent(seleccion-1)).setSelected(true);
            break;
    }
}//GEN-LAST:event_PanelPestanasStateChanged
    // No borrar esta línea para que funcione bien shift+alt+f en NetBeans
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSplitPane DivisionHorizontal;
    private javax.swing.JTabbedPane PanelPestanas;
    private javax.swing.JPanel PestanaModelo;
    private javax.swing.JSplitPane SubdivisionHorizontal;
    private javax.swing.JToolBar ToolBar;
    private javax.swing.JButton behaviourButton;
    private javax.swing.JMenuItem behaviourMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JButton exportGraphicButton;
    private javax.swing.JMenuItem exportGraphicMenuItem;
    private javax.swing.JButton exportReportButton;
    private javax.swing.JMenuItem exportReportMenuItem;
    private javax.swing.JButton jButtonCompContinua;
    private javax.swing.JButton jButtonParContinua;
    private javax.swing.JButton jButtonProcContinua;
    private javax.swing.JLabel jLabelHabitantes;
    private javax.swing.JLabel jLabelNombre;
    private javax.swing.JLabel jLabelPersonas;
    private javax.swing.JLabel jLabelTituloCompartimentos;
    private javax.swing.JLabel jLabelTituloDescCateg;
    private javax.swing.JLabel jLabelTituloDescPar;
    private javax.swing.JLabel jLabelTituloDescProc;
    private javax.swing.JLabel jLabelTituloDistribucion;
    private javax.swing.JLabel jLabelTituloHabitantes;
    private javax.swing.JLabel jLabelTituloNombre;
    private javax.swing.JLabel jLabelTituloParametros;
    private javax.swing.JLabel jLabelTituloPersonas;
    private javax.swing.JLabel jLabelTituloProcesos;
    private javax.swing.JLabel jLabelTituloUnidad;
    private javax.swing.JLabel jLabelUnidad;
    private javax.swing.JList jListCompartimentos;
    private javax.swing.JList jListParametros;
    private javax.swing.JList jListProcesos;
    private javax.swing.JPanel jPanelDinamica;
    private javax.swing.JPanel jPanelParametros;
    private javax.swing.JPanel jPanelPoblacion;
    private javax.swing.JPanel jPanelProcesos;
    private javax.swing.JScrollPane jScrollCompartimentos;
    private javax.swing.JScrollPane jScrollDinamica;
    private javax.swing.JScrollPane jScrollDistribucion;
    private javax.swing.JScrollPane jScrollParametros;
    private javax.swing.JScrollPane jScrollPoblacion;
    private javax.swing.JScrollPane jScrollProcesos;
    private javax.swing.JScrollPane jScrollVariables;
    private javax.swing.JTabbedPane jTabbedVariables;
    private javax.swing.JTextPane jTextPaneDescCateg;
    private javax.swing.JTextPane jTextPaneDescPar;
    private javax.swing.JTextPane jTextPaneDescProc;
    private javax.swing.JTextPane jTextPaneTituloDinamicaComp;
    private javax.swing.JTextPane jTextPaneTituloDinamicaPar;
    private javax.swing.JTextPane jTextPaneTituloDinamicaProc;
    private javax.swing.JTree jTreeDistribucion;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JRadioButtonMenuItem modelMenuItem;
    private javax.swing.JSeparator modelSeparator1;
    private javax.swing.JSeparator modelSeparator2;
    private javax.swing.JButton newButton;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JButton openButton;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JButton optionsSimButton;
    private javax.swing.JMenuItem optionsSimMenuItem;
    private javax.swing.JButton populationButton;
    private javax.swing.JMenuItem populationMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenu resultsMenu;
    private javax.swing.JMenu resultsWindowsMenu;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JButton saveasButton;
    private javax.swing.JMenu simulationMenu;
    private javax.swing.JSeparator simulationSeparator;
    private javax.swing.JButton startSimButton;
    private javax.swing.JMenuItem startSimMenuItem;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JButton stopSimButton;
    private javax.swing.JMenuItem stopSimMenuItem;
    private javax.swing.JComboBox timeComboBox;
    private javax.swing.JLabel timeLabel;
    private javax.swing.JSpinner timeSpinner;
    private javax.swing.JToolBar.Separator toolbarSeparator1;
    private javax.swing.JToolBar.Separator toolbarSeparator2;
    private javax.swing.JToolBar.Separator toolbarSeparator3;
    private javax.swing.JToolBar.Separator toolbarSeparator4;
    private javax.swing.JToolBar.Separator toolbarSeparator5;
    private javax.swing.JToolBar.Separator toolbarSeparator6;
    private javax.swing.JMenuItem userManualMenuItem;
    private javax.swing.JButton variablesButton;
    private javax.swing.JMenuItem variablesMenuItem;
    private javax.swing.ButtonGroup windowButtonGroup;
    private javax.swing.JMenu windowMenu;
    // End of variables declaration//GEN-END:variables
}
