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

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * Los elementos de esta clase representan los tramos de tiempo en los cuales
 * se puede dividir la definición de un proceso dado.
 * @author Víctor E. Tamames Gómez
 */
public class TramoContinua implements Cloneable {

    /**
     * El tiempo a partir del cual es válida la definición de este tramo.
     */
    private int tiempoInicio;
    
    /**
     * Definición continua de este tramo.
     */
    private String definicionContinua;

    /**
     * Constructor de la clase.
     */
    public TramoContinua() {
    }

    /**
     * Método para cambiar el tiempo de inicio de este tramo.
     * @param tiempoInicioTramo El nuevo tiempo de inicio.
     */
    public void setTiempoInicio(int tiempoInicioTramo) {
        this.tiempoInicio = tiempoInicioTramo;
    }

    /**
     * Método para obtener el tiempo de inicio de este tramo.
     * @return El tiempo de inicio de este tramo.
     */
    public int getTiempoInicio() {
        return this.tiempoInicio;
    }

    /**
     * Método para cambiar la definición continua de este tramo.
     * @param funcionTramo La nueva definición.
     */
    public void setDefinicionContinua(String funcionTramo) {
        this.definicionContinua = funcionTramo;
    }

    /**
     * Método para obtener la definición de este tramo.
     * @return La definición de este tramo.
     */
    public String getDefinicionContinua() {
        return this.definicionContinua;
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoTramo El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoTramo) {
        this.setTiempoInicio(Integer.parseInt(elementoTramo.attributeValue("tiempoInicio")));
        this.setDefinicionContinua(elementoTramo.attributeValue("definicion"));
    }

    /**
     * Método para volcar los datos de este objeto en uno de tipo
     * <CODE>org.dom4j.Element</CODE>.
     * @return El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public Element volcarAXML() {
        Element elementoTramo = new DefaultElement("tramo");

        elementoTramo.addAttribute("tiempoInicio", String.valueOf(this.tiempoInicio));
        elementoTramo.addAttribute("definicion", this.definicionContinua);

        return elementoTramo;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public TramoContinua clone() {
        TramoContinua clon = new TramoContinua();
        clon.setTiempoInicio(this.getTiempoInicio());
        clon.setDefinicionContinua(this.getDefinicionContinua());
        return clon;
    }
}
