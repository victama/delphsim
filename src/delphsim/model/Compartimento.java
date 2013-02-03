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
import java.util.Vector;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

/**
 * Los objetos de esta clase representan cada uno de los compartimentos, fruto
 * de combinar las categorías entre sí, entre los que se distribuyen los
 * habitantes de la población.
 * @author Víctor E. Tamames Gómez
 */
public class Compartimento implements Cloneable {

    /**
     * El nombre del compartimento.
     */
    private String nombre;
    
    /**
     * El número de habitantes que hay en este compartimento inicialmente.
     */
    private long habitantes;
    
    /**
     * Procesos que dependen de este compartimento.
     */
    private Vector procesosVinculados = new Vector();
    
    /**
     * Otros compartimentos que dependen de éste.
     */
    private Vector compartimentosVinculados = new Vector();
    
    /**
     * Definición de este compartimento para simulaciones continuas.
     */
    private String definicionContinua;

    /**
     * Constructor de la clase.
     */
    public Compartimento() {
    }

    /**
     * Método para cambiar el nombre al compartimento.
     * @param nombreCat El nuevo nombre para el compartimento.
     */
    public void setNombre(String nombreCat) {
        this.nombre = nombreCat;
    }

    /**
     * Método para obtener el nombre del compartimento.
     * @return El nombre del compartimento.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método para cambiar el número de habitantes inicial del compartimento.
     * @param personasComp El nuevo número de habitantes.
     */
    public void setHabitantes(long personasComp) {
        this.habitantes = personasComp;
    }

    /**
     * Método para obtener el número inicial de habitantes en el compartimento.
     * @return El número de habitantes.
     */
    public long getHabitantes() {
        return this.habitantes;
    }

    /**
     * Método para cambiar el conjunto de procesos que dependen de este
     * compartimento.
     * @param procesosVinculadosComp Los nombres del nuevo conjunto de procesos
     *                               vinculados.
     */
    public void setProcesosVinculados(String[] procesosVinculadosComp) {
        this.procesosVinculados = new Vector();
        for (String procVinc : procesosVinculadosComp) {
            if (!this.procesosVinculados.contains(procVinc)) {
                this.procesosVinculados.add(procVinc);
            }
        }
    }

    /**
     * Método para obtener los procesos que dependen de este compartimento.
     * @return Los nombres del conjunto de procesos vinculados.
     */
    public String[] getProcesosVinculados() {
        String[] procVinculados = new String[this.procesosVinculados.size()];
        for (int i = 0; i < this.procesosVinculados.size(); i++) {
            procVinculados[i] = (String) this.procesosVinculados.get(i);
        }
        return procVinculados;
    }
    
    /**
     * Método para añadir un nuevo proceso a la lista de procesos vinculados.
     * Si ya estuviera contenido en ella, no se hace nada.
     * @param procVincNuevo El nombre del proceso a añadir.
     */
    public void anadirProcesoVinculado(String procVincNuevo) {
        if (!this.procesosVinculados.contains(procVincNuevo)) {
            this.procesosVinculados.add(procVincNuevo);
        }
    }

    /**
     * Método para eliminar un proceso de la lista de procesos vinculados. Si
     * no estuviera contenido en ella, no se hace nada.
     * @param procVincEliminado El nombre del proceso a eliminar.
     */
    public void eliminarProcesoVinculado(String procVincEliminado) {
        this.procesosVinculados.remove(procVincEliminado);
    }

    /**
     * Método para cambiar el conjunto de compartimentos que dependen de éste.
     * @param compartimentosVinculadosComp Los nombres del nuevo conjunto de
     *                                     compartimentos.
     */
    public void setCompartimentosVinculados(String[] compartimentosVinculadosComp) {
        this.compartimentosVinculados = new Vector();
        for (String compVinc : compartimentosVinculadosComp) {
            if (!this.compartimentosVinculados.contains(compVinc)) {
                this.compartimentosVinculados.add(compVinc);
            }
        }
    }

    /**
     * Método para obtener el conjunto de compartimentos que dependen de éste.
     * @return Los nombres de los compartimentos.
     */
    public String[] getCompartimentosVinculados() {
        String[] compVinculados = new String[this.compartimentosVinculados.size()];
        for (int i = 0; i < this.compartimentosVinculados.size(); i++) {
            compVinculados[i] = (String) this.compartimentosVinculados.get(i);
        }
        return compVinculados;
    }
    
    /**
     * Método para añadir un compartimento a la lista de compartimentos que
     * dependen de éste. Si ya estuviera contenido en ella, no se hace nada.
     * @param compVincNuevo El nombre del compartimento a añadir.
     */
    public void anadirCompartimentoVinculado(String compVincNuevo) {
        if (!this.compartimentosVinculados.contains(compVincNuevo)) {
            this.compartimentosVinculados.add(compVincNuevo);
        }
    }
    
    /**
     * Método para eliminar un compartimento de la lista de compartimentos que
     * dependen de éste. Si no estuviera contenido en ella, no se hace nada.
     * @param compVincEliminado El nombre del compartimento a eliminar.
     */
    public void eliminarCompartimentoVinculado(String compVincEliminado) {
        this.compartimentosVinculados.remove(compVincEliminado);
    }

    /**
     * Método para cambiar la definición continua de este compartimento.
     * @param dinamicaContinuaComp La nueva definición continua.
     */
    public void setDefinicionContinua(String dinamicaContinuaComp) {
        this.definicionContinua = dinamicaContinuaComp;
    }

    /**
     * Método para obtener la definición continua de este compartimento.
     * @return La definición continua.
     */
    public String getDefinicionContinua() {
        return this.definicionContinua;
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoCompartimento El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoCompartimento) {
        this.setNombre(elementoCompartimento.attributeValue("nombre"));
        this.setHabitantes(Long.valueOf(elementoCompartimento.attributeValue("habitantes")));

        String[] procVinculados = new String[elementoCompartimento.elements("procesoVinculado").size()];
        int indice = 0;
        for (Iterator i = elementoCompartimento.elementIterator("procesoVinculado"); i.hasNext();) {
            Element elementoProcesoVinculado = (Element) i.next();
            procVinculados[indice++] = elementoProcesoVinculado.attributeValue("nombre");
        }
        this.setProcesosVinculados(procVinculados);

        String[] compVinculados = new String[elementoCompartimento.elements("compartimentoVinculados").size()];
        indice = 0;
        for (Iterator i = elementoCompartimento.elementIterator("compartimentoVinculados"); i.hasNext();) {
            Element elementoCompartimentoVinculado = (Element) i.next();
            compVinculados[indice++] = elementoCompartimentoVinculado.attributeValue("nombre");
        }
        this.setCompartimentosVinculados(compVinculados);

        Element elementoDefinicionContinua = elementoCompartimento.element("definicionContinua");
        if (elementoDefinicionContinua != null) {
            this.setDefinicionContinua(elementoDefinicionContinua.attributeValue("definicion"));
        }
        
    }

    /**
     * Método para volcar los datos de este objeto en uno de tipo
     * <CODE>org.dom4j.Element</CODE>.
     * @return El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public Element volcarAXML() {
        Element elementoCompartimento = new DefaultElement("compartimento");

        elementoCompartimento.addAttribute("nombre", this.nombre);
        elementoCompartimento.addAttribute("habitantes", String.valueOf(this.habitantes));
        
        for (int i = 0; i < this.procesosVinculados.size(); i++) {
            Element elementoProcesoVinculado = elementoCompartimento.addElement("procesoVinculado");
            elementoProcesoVinculado.addAttribute("nombre",(String) this.procesosVinculados.get(i));
        }
        for (int i = 0; i < this.compartimentosVinculados.size(); i++) {
            Element elementoCompartimentoVinculado = elementoCompartimento.addElement("compartimentoVinculado");
            elementoCompartimentoVinculado.addAttribute("nombre",(String) this.compartimentosVinculados.get(i));
        }
        
        if (this.definicionContinua != null && !this.definicionContinua.equals("")) {
            Element elementoDefinicionContinua = elementoCompartimento.addElement("definicionContinua");
            elementoDefinicionContinua.addAttribute("definicion", this.definicionContinua);
        }

        return elementoCompartimento;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Compartimento clone() {
        Compartimento clon = new Compartimento();
        clon.setNombre(this.getNombre());
        clon.setHabitantes(this.getHabitantes());
        clon.setProcesosVinculados(this.getProcesosVinculados());
        clon.setCompartimentosVinculados(this.getCompartimentosVinculados());
        clon.setDefinicionContinua(this.getDefinicionContinua());
        return clon;
    }
}
