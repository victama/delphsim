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
package delphsim.simulation;

import delphsim.DelphSimApp;
import delphsim.DelphSimView;
import delphsim.PreferenciasSimulacion;
import delphsim.model.Epidemia;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


/**
 * Esta clase abstracta contiene la estructura de un método de simulación
 * continua genérico, a falta de implementar en <CODE>doInBackground()</CODE>
 * el método numérico que se desee. Puesto que extiende las propiedades de una
 * tarea (<CODE>Task</CODE>), está pensada para trabajar en paralelo con otras
 * tareas siguiendo los mismos estados que hay definidos para éstas (ver el
 * diagrama de máquinas de estados correspondiente en los documentos de diseño).
 * @author Víctor E. Tamames Gómez
 */
public abstract class SimulationTask extends org.jdesktop.application.Task<Object, Void> {

    /**
     * La epidemia que se va a simular.
     */
    protected Epidemia epidemia;
    
    /**
     * El paso de integración a usar por el método. Para los métodos de paso
     * variable, éste es el paso de integración inicial.
     */
    protected double h_;
    
    /**
     * El mapa de acciones de la interfaz general, para reactivar las opciones
     * desactivadas, prohibidas durante la ejecución de la tarea, una vez que
     * ésta termine.
     */
    protected javax.swing.ActionMap actionMap;
    
    /**
     * Panel de pestañas de la interfaz general donde añadir los resultados
     * obtenidos al término de la simulación.
     */
    protected javax.swing.JTabbedPane pestanas;
    
    /**
     * El submenú del menú "Ventana" de la interfaz general, donde añadir más
     * elementos de menú, uno por cada resultado.
     */
    protected javax.swing.JMenu menuResultados;
    
    /**
     * El grupo de botones al que pertenecen los elementos del menú mencionados
     * antes.
     */
    protected javax.swing.ButtonGroup grupoVentana;
    
    /**
     * Si el archivo estaba modificado o no, para la reactivación de las
     * acciones desactivadas.
     */
    protected boolean archivoModificado;
    
    /**
     * El tiempo en el que se inició la simulación. Necesario para calcular el
     * tiempo transcurrido y el tiempo estimado restante.
     */
    protected long tiempoComienzo;
    
    /**
     * Constructor de la tarea que toma referencia de los parámetros en sus
     * propios atributos para poder trabajar con ellos. El primer parámetro es
     * necesario para tener referencia a la aplicación que ha invocado la tarea,
     * el segundo contiene la epidemia que tenemos que simular, y los cuatro
     * últimos son elementos del GUI que tenemos que actualizar cuando la tarea
     * termine.
     * @param app   La aplicación que invoca esta tarea.
     * @param epi   La epidemia a simular.
     * @param aMap  El mapa de acciones para poder desactivar las prohibidas.
     * @param jtabbed   Panel de pestañas donde añadir los resultados.
     * @param jmenu  Submenú donde añadir los elementos de menú de resultados.
     * @param bgroup   Grupo de botones para añadirle los elementos de menú.
     */
    public SimulationTask(org.jdesktop.application.Application app,
                               Epidemia epi, javax.swing.ActionMap aMap,
                               javax.swing.JTabbedPane jtabbed,
                               javax.swing.JMenu jmenu,
                               javax.swing.ButtonGroup bgroup) {
        // Guardamos los parámetros como atributos de la clase
        super(app);
        this.epidemia = epi;
        this.actionMap = aMap;
        this.pestanas = jtabbed;
        this.menuResultados = jmenu;
        this.grupoVentana = bgroup;
        this.h_ = Double.valueOf(PreferenciasSimulacion.preferencias
                .get("h", PreferenciasSimulacion.hPorDefecto));
        // Y desactivamos todas las opciones excepto "Detener"
        this.actionMap.get("nuevoModelo").setEnabled(false); // NOI18N
        this.actionMap.get("abrirModelo").setEnabled(false); // NOI18N
        this.actionMap.get("guardarModeloComo").setEnabled(false); // NOI18N
        this.actionMap.get("distribucionPoblacion").setEnabled(false); // NOI18N
        this.actionMap.get("dinamicaPoblacion").setEnabled(false); // NOI18N
        this.actionMap.get("gestionarVariables").setEnabled(false); // NOI18N
        this.actionMap.get("iniciarContinua").setEnabled(false); // NOI18N
        this.actionMap.get("preferenciasSimulacion").setEnabled(false); // NOI18N
        this.actionMap.get("exportarGrafica").setEnabled(false); // NOI18N
        this.actionMap.get("exportarInforme").setEnabled(false); // NOI18N
        this.actionMap.get("mostrarModelo").setEnabled(false); // NOI18N
        this.actionMap.get("mostrarResultado").setEnabled(false); // NOI18N
        // La posibilidad de cancelar la simulación
        this.actionMap.get("detenerSimulacion").setEnabled(true); // NOI18N
        // Nos quedamos con si el archivo estaba modificado o no
        this.archivoModificado = actionMap.get("guardarModelo").isEnabled(); // NOI18N
        this.actionMap.get("guardarModelo").setEnabled(false); // NOI18N
    }
    
    /**
     * Este método debe sobreescribirlo la clase que hereda implementando su
     * método numérico para la simulación continua.
     * @return En realidad no devolverá nada, ya que los resultados se guardan
     *         en los objetos correspondientes del paquete <CODE>model</CODE>.
     * @throws java.io.IOException Si hay algún problema al escribir los valores
     *                             calculados en disco duro.
     */
    @Override
    protected abstract Object doInBackground() throws IOException;
    
    /**
     * Actualiza el GUI en base a los resultados obtenidos en doInBackground().
     * Obtiene los paneles con los resultados y los agrega como pestañas a la
     * interfaz general.
     * @param result Es un parámetro vacío, ya que los resultados se almacenan
     *               en el modelo de DelphSim.
     */
    @Override
    protected void succeeded(Object result) {
        // Si el resultado es null, realmente ha fallado, no hacer nada
        if (result == null) {
            return;
        }
        // Colocamos el mensaje de éxito
        this.setMessage("¡La simulación se ha completado con éxito! Duración: " + this.elapsedTime());
        // Para cada resultado de la epidemia creamos y añadimos una pestaña
        // y un elemento en el submenú "Resultados" del menú "Ventana"
        for (int i = 0; i < this.epidemia.getResultados().length; i++) {
            this.pestanas.addTab(this.epidemia.getResultado(i).getTitulo(), DelphSimApp.getApplication().getContext().getResourceMap().getIcon("PestanaGrafica.TabConstraints.tabIcon"), this.epidemia.getResultado(i).construirPanelResultado());
            javax.swing.JRadioButtonMenuItem item = new javax.swing.JRadioButtonMenuItem();
            item.setAction(this.actionMap.get("mostrarResultado"));
            item.setText(this.epidemia.getResultado(i).getTitulo());
            item.setIcon(DelphSimApp.getApplication().getContext().getResourceMap().getIcon("PestanaGrafica.TabConstraints.tabIcon"));
            item.setSelected(false);
            this.menuResultados.add(item);
            this.grupoVentana.add(item);
        }
        this.menuResultados.setEnabled(true);
        // Popup de éxito
        JOptionPane.showMessageDialog(DelphSimApp.getApplication().getMainFrame(), "¡La simulación se ha completado con éxito!\nPulse en la pestaña correspondiente para ver los resultados.", 
                "Información: simulación completada", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Método a lanzar si la simulación falla.
     * @param cause Causa del fallo.
     */
    @Override
    protected void failed(java.lang.Throwable cause) {
        // Borrar todos los archivos de la carpeta "temp"
        // Si no se pueden borrar ahora, cuando se cierre la JVM
        String rutaTemp = new File(System.getProperty("java.class.path")).getParent() + File.separator + "temp";
        File[] tempFiles = new File(rutaTemp).listFiles();
        for (int i = 0; i < tempFiles.length; i++) {
            if (!tempFiles[i].delete()) {
                tempFiles[i].deleteOnExit();
            }
        }
        // Borrar de la epidemia los objetos Resultado que pueda haber
        this.epidemia.eliminarResultados();
        // Mostrar mensaje de error
        Logger.getLogger(SimulationTask.class.getName()).log(Level.SEVERE, null, cause);
        JOptionPane.showMessageDialog(DelphSimApp.getApplication().getMainFrame(), cause.getMessage(), 
                "Error: no se pudo simular", JOptionPane.ERROR_MESSAGE);
        this.setMessage("La simulación ha fallado.");
    }
    
    /**
     * Método a lanzar si la simulación es cancelada por el usuario.
     */
    @Override
    protected void cancelled() {
        // Borrar todos los archivos de la carpeta "temp"
        // Si no se pueden borrar ahora, cuando se cierre la JVM
        String rutaTemp = new File(System.getProperty("java.class.path")).getParent() + File.separator + "temp";
        File[] tempFiles = new File(rutaTemp).listFiles();
        for (int i = 0; i < tempFiles.length; i++) {
            if (!tempFiles[i].delete()) {
                tempFiles[i].deleteOnExit();
            }
        }
        // Borrar de la epidemia los objetos Resultado que pueda haber
        this.epidemia.eliminarResultados();
        // Actualizar mensaje
        this.setMessage("La simulación ha sido cancelada antes de que se completara.");
    }
    
    /**
     * Método a lanzar si la simulación es interrumpida (cancelada por el
     * usuario una vez que ya ha comenzado a ejecutarse <CODE>doInBackground()</CODE>.
     * @param e La interrupción.
     */
    @Override
    protected void interrupted(java.lang.InterruptedException e) {
        // Borrar todos los archivos de la carpeta "temp"
        // Si no se pueden borrar ahora, cuando se cierre la JVM
        String rutaTemp = new File(System.getProperty("java.class.path")).getParent() + File.separator + "temp";
        File[] tempFiles = new File(rutaTemp).listFiles();
        for (int i = 0; i < tempFiles.length; i++) {
            if (!tempFiles[i].delete()) {
                tempFiles[i].deleteOnExit();
            }
        }
        // Borrar de la epidemia los objetos Resultado que pueda haber
        this.epidemia.eliminarResultados();
        // Mostrar mensaje de error
        Logger.getLogger(SimulationTask.class.getName()).log(Level.SEVERE, null, e);
        JOptionPane.showMessageDialog(DelphSimApp.getApplication().getMainFrame(), e.getMessage(), 
                "Error: no se pudo simular", JOptionPane.ERROR_MESSAGE);
        this.setMessage("La simulación ha sido interrumpida por el sistema.");
    }
    
    /**
     * Método a realizar siempre que se termine la tarea, bien sea por éxito,
     * fallo, cancelación o interrupción. Reactiva las acciones de la interfaz
     * general que habían sido deshabilitadas.
     */
    @Override
    protected void finished() {
        // Reactivamos todas las opciones desactivadas anteriormente, menos
        // las de exportar gráfica/informe.
        this.actionMap.get("nuevoModelo").setEnabled(true); // NOI18N
        this.actionMap.get("abrirModelo").setEnabled(true); // NOI18N
        this.actionMap.get("guardarModeloComo").setEnabled(true); // NOI18N
        this.actionMap.get("distribucionPoblacion").setEnabled(true); // NOI18N
        this.actionMap.get("dinamicaPoblacion").setEnabled(true); // NOI18N
        this.actionMap.get("gestionarVariables").setEnabled(true); // NOI18N
        this.actionMap.get("iniciarContinua").setEnabled(true); // NOI18N
        this.actionMap.get("preferenciasSimulacion").setEnabled(true); // NOI18N
        this.actionMap.get("mostrarModelo").setEnabled(true); // NOI18N
        this.actionMap.get("mostrarResultado").setEnabled(true); // NOI18N
        // Desactivamos la posibilidad de cancelar
        this.actionMap.get("detenerSimulacion").setEnabled(false); // NOI18N
        // Y sólo activamos la de guardar modelo si el archivo estaba modificado
        this.actionMap.get("guardarModelo").setEnabled(this.archivoModificado); // NOI18N
        // Por último, como todo ha terminado bien, borrar backup
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(DelphSimApp.class).getContext().getResourceMap(DelphSimView.class);
        File backup = new File(new File(System.getProperty("java.class.path")).getParent() + resourceMap.getString("autoguardado.path"));
        if (backup.exists()) {
            if (!backup.delete()) {
                System.err.println("Error: no se ha podido eliminar el backup después de una simulación exitosa");
            }
        }
    }
    
    /**
     * Método para calcular el tiempo transcurrido desde que se comenzó la
     * simulación.
     */
    protected String elapsedTime() {
        long tiempoTranscurrido = (System.currentTimeMillis() - this.tiempoComienzo) / 1000;
        String tiempo = String.format("%02d", tiempoTranscurrido % 60);
        tiempoTranscurrido = tiempoTranscurrido / 60;
        tiempo = String.format("%02d:", tiempoTranscurrido % 60) + tiempo;
        tiempoTranscurrido = tiempoTranscurrido / 60;
        tiempo = String.format("%02d:", tiempoTranscurrido) + tiempo;
        return tiempo;
    }
    
    /**
     * Método para estimar el tiempo restante para que termine la simulación.
     */
    protected String estimatedTime(long iteracionActual, long totalIteraciones, long paso) {
        long tiempoTranscurrido = (System.currentTimeMillis() - this.tiempoComienzo) / 1000;
        long tiempoRestante = (tiempoTranscurrido * (totalIteraciones - iteracionActual - paso)) 
                                / (iteracionActual + paso);
        String tiempo = String.format("%02d", tiempoRestante % 60);
        tiempoRestante = tiempoRestante / 60;
        tiempo = String.format("%02d:", tiempoRestante % 60) + tiempo;
        tiempoRestante = tiempoRestante / 60;
        tiempo = String.format("%02d:", tiempoRestante) + tiempo;
        return tiempo;
    }
}
