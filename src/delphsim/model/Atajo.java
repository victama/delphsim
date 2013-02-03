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
package delphsim.model;

/**
 * Cada uno de estos elementos representa una palabra clave, generada por
 * combinación de las categorías de la población, de manera que el usuario
 * puede usarla para referirse de una manera más sencilla a varios
 * compartimentos al mismo tiempo, los cuales comparten una o varias categorías.
 * @author Víctor E. Tamames Gómez
 */
public class Atajo implements Cloneable {
        
    /**
     * El identificador a través del cual el usuario hace referencia al atajo.
     */
    private String nombre;
    
    /**
     * Definición del atajo para simulaciones continuas. Es una cadena de texto
     * con el sumatorio de los compartimentos a los que hace referencia este
     * atajo, es decir, una ecuación.
     */
    private String definicionContinua;
    
    /**
     * Constructor vacío de la clase.
     */
    public Atajo() {
    }
    
    /**
     * Constructor de la clase con el nombre del atajo y los compartimentos para
     * construir la definición a partir de ellos.
     * @param nomb Nombre del nuevo atajo.
     * @param comps Compartimentos de la población.
     */
    public Atajo(String nomb, Compartimento[] comps) {
        this.setNombre(nomb);
        this.generarDefinicionContinua(comps);
    }
    
    /**
     * Método para cambiar el nombre al atajo.
     * @param nombreAtajo Nuevo nombre para el atajo.
     */
    public void setNombre(String nombreAtajo) {
        this.nombre = nombreAtajo;
    }
    
    /**
     * Método para obtener el nombre del atajo.
     * @return Cadena de texto con el nombre del atajo.
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Método para cambiar la definición continua del atajo.
     * @param definicionAtajo La nueva definición del atajo.
     */
    public void setDefinicionContinua(String definicionAtajo) {
        this.definicionContinua = definicionAtajo;
    }
    
    /**
     * Método para obtener la definición continua del atajo.
     * @return Cadena de texto con la definición continua.
     */
    public String getDefinicionContinua() {
        return this.definicionContinua;
    }
    
    /**
     * Método que recoge los nombres de los compartimentos que deben estar en
     * su definición para poder formarla.
     * @param compartimentosASumar Los compartimentos que pueden estar en su
     *                             definición.
     */
    private void generarDefinicionContinua(Compartimento[] compartimentosASumar) {
        String definicion = "";
        // Dividimos el nombre del atajo, obteniendo un array con sus categorías
        String[] catAtajo = this.getNombre().split("_");
        // Recorremos todos los compartimentos
        for (int i = 0; i < compartimentosASumar.length; i++) {
            int total = 0;
            String[] catComp = compartimentosASumar[i].getNombre().split("_");
            java.util.Vector v = new java.util.Vector();
            for (int j = 0; j < catComp.length; j++) {
                v.add(catComp[j]);
            }
            // Para cada categoría que aparezca en el atajo
            for (int j = 0; j < catAtajo.length; j++) {
                // Comprobamos si aparece también en las categorías del compartimento
                if (v.contains(catAtajo[j])) {
                    // En tal caso, aumentamos el total de categorías que coinciden
                    total++;
                }
            }
            if (total == catAtajo.length) {
                // Si el total es el número de categorías del atajo, es decir, el compartimento tenía TODAS
                definicion += compartimentosASumar[i].getNombre() + " + ";
            }
        }
        // Al terminar el bucle, nos sobrarán los 3 últimos caracteres -> " + "
        this.setDefinicionContinua(definicion.substring(0, definicion.length()-3));
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Atajo clone() {
        Atajo clon = new Atajo();
        clon.setNombre(this.getNombre());
        clon.setDefinicionContinua(this.getDefinicionContinua());
        return clon;
    }
}
