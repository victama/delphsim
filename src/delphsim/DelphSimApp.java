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

import java.awt.Component;
import java.awt.Image;
import java.io.File;
import java.util.EventObject;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * Clase que implementa un elemento a la escucha de eventos de salida de la
 * aplicación, pide confirmación y si el usuario confirma, sale.
 * @author Víctor E. Tamames Gómez
 */
class ConfirmExit implements Application.ExitListener {
    
    /**
     * Método que pide confirmación al usuario antes de salir de la aplicación.
     * @param e El evento generado.
     * @return Si se puede salir o no (elección del usuario).
     */
    public boolean canExit(EventObject e) {
        // Si se ha modificado el modelo, pedir confirmación de salida
        if (DelphSimApp.getApplication().getInterfaz().getArchivoModificado()) {
            org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DelphSimView.class);
            Object source = (e != null) ? e.getSource() : null;
            Component owner = (source instanceof Component) ? (Component)source : null;
            String[] opciones = {"Sí", "No"};
            int option = JOptionPane.showOptionDialog(owner, resourceMap.getString("ExitListener.msg"), resourceMap.getString("ExitListener.title"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, opciones, opciones[1]); // NOI18N
            return option == JOptionPane.YES_OPTION;
        } else {
            return true;
        }
    }
    
    /**
     * Método a realizar cuando se va a salir.
     * @param e El evento generado.
     */
    public void willExit(EventObject e) {} 
}

/**
 * La clase principal de la aplicación.
 * @author Víctor E. Tamames Gómez
 */
public class DelphSimApp extends SingleFrameApplication {
    
    /**
     * Puerto para que las aplicaciones estén a la escucha y sólo se permita
     * una instancia del programa.
     */
    private static final int PORT = 9667; 
    
    /**
     * Socket a mantener para que el resto de instancias no se ejecuten.
     */
    private static java.net.ServerSocket ss;
    
    /**
     * La ventana principal de la aplicación.
     */
    private DelphSimView interfaz;
    
    /**
     * Al ejecutar la aplicación, crea y muestra la ventana principal.
     */
    @Override
    protected void startup() {
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(delphsim.DelphSimApp.class).getContext().getResourceMap(DelphSimView.class);
        
        // Sólo permitir una instancia (mediante sockets)
        try {
            // Se intenta bloquear el socket por defecto
            ss = new java.net.ServerSocket(PORT);
        } catch (java.net.BindException be) {
            // Si no se puede, ya hay otra instancia, mensaje y cerrar
            JOptionPane.showMessageDialog(null, resourceMap.getString("YaEjecutandose.msg"), resourceMap.getString("YaEjecutandose.title"), JOptionPane.INFORMATION_MESSAGE); // NOI18N
            System.exit(1);
        } catch (java.io.IOException ioe) {
            // Si ocurre otro problema, se vuelca el error y se cierra
            ioe.printStackTrace();
            System.exit(1);
        }
        
        // Crear y mostrar la vista general
        this.interfaz = new DelphSimView(this);
        show(this.interfaz);
        // Establecer icono, tamaño, posición y el listener para confirmar salida
        Image img = new ImageIcon(DelphSimApp.class.getResource(resourceMap.getString("Application.icon"))).getImage(); // NOI18N
        DelphSimApp.getApplication().getMainFrame().setIconImage(img);
        DelphSimApp.getApplication().getMainFrame().setSize(resourceMap.getInteger("Application.initialSize.width"), resourceMap.getInteger("Application.initialSize.height")); // NOI18N
        DelphSimApp.getApplication().getMainFrame().setLocationRelativeTo(null);
        DelphSimApp.getApplication().getMainFrame().setVisible(true);
        DelphSimApp.getApplication().addExitListener(new ConfirmExit());
        // Eliminar archivos temporales si es que queda alguno
        File[] fs = new File(new File(System.getProperty("java.class.path")).getParent() +  // NOI18N
                File.separator + "temp" + File.separator).listFiles(); // NOI18N
        if (fs != null) {
            for (int i = 0; i < fs.length; i++) {
                fs[i].delete();
            }
        }
        // Comprobar si hay una copia de respaldo
        this.interfaz.comprobarCopiaRespaldo();
    }
    
    /**
     * Este método sirve para inicializar la ventana especificada inyectando
     * los recursos. Las ventanas mostradas en esta aplicación vienen
     * completamente inicializadas por el constructor del GUI, por lo que no
     * es necesaria esta configuración adicional.
     * @param root La ventana a inicializar.
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }
    
    /**
     * Un método estático muy útil para obtener la instancia de la aplicación.
     * @return  La instancia de DelphSimApp.
     */
    public static DelphSimApp getApplication() {
        return Application.getInstance(DelphSimApp.class);
    }
    
    /**
     * Devuelve la interfaz de esta aplicación.
     * @return El objeto con la ventana principal.
     */
    public DelphSimView getInterfaz() {
        return this.interfaz;
    }

    /**
     * El método principal que lanza la aplicación.
     * @param args Argumentos introducidos por línea de comandos.
     */
    public static void main(String[] args) {
        launch(DelphSimApp.class, args);
    }
}
