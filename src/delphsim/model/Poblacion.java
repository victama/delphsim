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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * Esta clase representa la configuración de la población que va a sufrir la
 * epidemia.
 * @author Víctor E. Tamames Gómez
 */
public class Poblacion implements Cloneable {

    /**
     * El nombre de la población.
     */
    private String nombre;
    
    /**
     * El número de habitantes total de la población.
     */
    private long habitantes;
    
    /**
     * Las divisiones de esta población.
     */
    private Division[] divisiones = new Division[0];

    /**
     * Constructor de la clase.
     */
    public Poblacion() {
    }

    /**
     * Método para cambiar el nombre de la población.
     * @param nombrePob El nuevo nombre.
     */
    public void setNombre(String nombrePob) {
        this.nombre = nombrePob;
    }

    /**
     * Método para obtener el nombre de la población.
     * @return El nombre de la población.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método para cambiar el número total de habitantes.
     * @param numHabitantes El nuevo número total.
     */
    public void setHabitantes(long numHabitantes) {
        this.habitantes = numHabitantes;
    }

    /**
     * Método para obtener el número total de habitantes.
     * @return Número total de habitantes.
     */
    public long getHabitantes() {
        return this.habitantes;
    }

    /**
     * Método para cambiar las divisiones de esta población.
     * @param divisionesPob Las nuevas divisiones.
     */
    public void setDivisiones(Division[] divisionesPob) {
        this.divisiones = divisionesPob;
    }

    /**
     * Método para obtener las divisiones de esta población.
     * @return Las divisiones.
     */
    public Division[] getDivisiones() {
        return this.divisiones;
    }

    /**
     * Método que transforma la información contenida por este objeto en un
     * modelo de árbol para ser mostrada por pantalla.
     * @return Un modelo de árbol para un <CODE>JTree</CODE> con la información
     * de esta población.
     */
    public DefaultTreeModel construirArbolDistribucion() {
        DefaultMutableTreeNode raiz = new DefaultMutableTreeNode("raiz");
        DefaultTreeModel distribucion = new DefaultTreeModel(raiz, false);
        for (Division division : this.getDivisiones()) {
            DefaultMutableTreeNode otraDivision = new DefaultMutableTreeNode(division.getNombre());
            raiz.add(otraDivision);
            for (Categoria categoria : division.getCategorias()) {
                DefaultMutableTreeNode otraCategoria = new DefaultMutableTreeNode(categoria.getNombre());
                otraDivision.add(otraCategoria);
            }
        }
        return distribucion;
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoPoblacion El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoPoblacion) {
        this.setNombre(elementoPoblacion.attributeValue("nombre"));
        this.setHabitantes(Long.valueOf(elementoPoblacion.attributeValue("habitantes")));
        this.divisiones = new Division[elementoPoblacion.elements("division").size()];
        int indice = 0;
        for (Iterator i = elementoPoblacion.elementIterator("division"); i.hasNext();) {
            Element elementoDivision = (Element) i.next();
            this.divisiones[indice] = new Division();
            this.divisiones[indice++].cargarDesdeXML(elementoDivision);
        }
    }

    /**
     * Método para volcar los datos de este objeto en uno de tipo
     * <CODE>org.dom4j.Element</CODE>.
     * @return El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public Element volcarAXML() {
        Element elementoPoblacion = new DefaultElement("poblacion");

        elementoPoblacion.addAttribute("nombre", this.nombre);
        elementoPoblacion.addAttribute("habitantes", String.valueOf(this.habitantes));
        for (Division div : this.divisiones) {
            elementoPoblacion.add(div.volcarAXML());
        }

        return elementoPoblacion;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Poblacion clone() {
        int numDivs = this.getDivisiones().length;
        Poblacion clon = new Poblacion();
        clon.setNombre(this.getNombre());
        clon.setHabitantes(this.getHabitantes());
        clon.setDivisiones(new Division[numDivs]);
        for (int i = 0; i < numDivs; i++) {
            clon.getDivisiones()[i] = this.getDivisiones()[i].clone();
        }
        return clon;
    }
}
