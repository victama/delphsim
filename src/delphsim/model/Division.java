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

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * Los objetos de esta clase representan cada una de las divisiones que pueden
 * hacerse sobre una población.
 * @author Víctor E. Tamames Gómez
 */
public class Division implements Cloneable {

    /**
     * El nombre de la división.
     */
    private String nombre;
    
    /**
     * Las categorías que existen dentro de esta división.
     */
    private Categoria[] categorias = new Categoria[0];

    /**
     * Constructor de la clase.
     */
    public Division() {
    }

    /**
     * Método para cambiar el nombre de la división.
     * @param nombreDiv El nuevo nombre de la división.
     */
    public void setNombre(String nombreDiv) {
        this.nombre = nombreDiv;
    }

    /**
     * Método para obtener el nombre de la división.
     * @return El nombre de la división.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método para cambiar las categorías que componen esta división.
     * @param categoriasDiv Las nuevas categorías.
     */
    public void setCategorias(Categoria[] categoriasDiv) {
        this.categorias = categoriasDiv;
    }

    /**
     * Método para obtener las categorías que componen esta división.
     * @return Las categorías de esta división.
     */
    public Categoria[] getCategorias() {
        return this.categorias;
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoDivision El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoDivision) {
        this.setNombre(elementoDivision.attributeValue("nombre"));
        this.categorias = new Categoria[elementoDivision.elements("categoria").size()];
        int indice = 0;
        for (Iterator i = elementoDivision.elementIterator("categoria"); i.hasNext();) {
            Element elementoCategoria = (Element) i.next();
            this.categorias[indice] = new Categoria();
            this.categorias[indice++].cargarDesdeXML(elementoCategoria);
        }
    }

    /**
     * Método para volcar los datos de este objeto en uno de tipo
     * <CODE>org.dom4j.Element</CODE>.
     * @return El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public Element volcarAXML() {
        Element elementoDivision = new DefaultElement("division");

        elementoDivision.addAttribute("nombre", this.nombre);
        for (Categoria categ : this.categorias) {
            elementoDivision.add(categ.volcarAXML());
        }

        return elementoDivision;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Division clone() {
        int numCateg = this.getCategorias().length;
        Division clon = new Division();
        clon.setNombre(this.getNombre());
        clon.setCategorias(new Categoria[numCateg]);
        for (int i = 0; i < numCateg; i++) {
            clon.getCategorias()[i] = this.getCategorias()[i].clone();
        }
        return clon;
    }
}
