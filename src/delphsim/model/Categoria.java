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
 * Los objetos de esta clase representan las distintas categorías en que se
 * puede descomponer una división de la población.
 * @author Víctor E. Tamames Gómez
 */
public class Categoria implements Cloneable {

    /**
     * El nombre de la categoría (obligatorio). Debe ser único en el conjunto
     * compuesto por los nombres de los parámetros, procesos y categorías.
     */
    private String nombre;
    
    /**
     * Una descripción de la categoría (opcional).
     */
    private String descripcion;

    /**
     * Constructor de la clase.
     */
    public Categoria() {
    }

    /**
     * Método para cambiar el nombre a este categoría.
     * @param nombreCat El nuevo nombre para la categoría.
     */
    public void setNombre(String nombreCat) {
        this.nombre = nombreCat;
    }

    /**
     * Método para obtener el nombre de esta categoría.
     * @return El nombre de esta categoría.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método para cambiar la descripción de esta categoría.
     * @param descripcionCat La nueva descripción para esta categoría.
     */
    public void setDescripcion(String descripcionCat) {
        this.descripcion = descripcionCat;
    }

    /**
     * Método para obtener la descripción de esta categoría.
     * @return La descripción de esta categoría.
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoCategoria El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoCategoria) {
        this.setNombre(elementoCategoria.attributeValue("nombre"));
        this.setDescripcion(elementoCategoria.elementText("descripcion"));
    }

    /**
     * Método para volcar los datos de este objeto en uno de tipo
     * <CODE>org.dom4j.Element</CODE>.
     * @return El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public Element volcarAXML() {
        Element elementoCategoria = new DefaultElement("categoria");

        elementoCategoria.addAttribute("nombre", this.nombre);
        if (this.descripcion != null) {
            if (!this.descripcion.equals("")) {
                Element elementoDescripcion = elementoCategoria.addElement("descripcion");
                elementoDescripcion.setText(this.descripcion);
            }
        }

        return elementoCategoria;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Categoria clone() {
        Categoria clon = new Categoria();
        clon.setNombre(this.getNombre());
        clon.setDescripcion(this.getDescripcion());
        return clon;
    }
}
