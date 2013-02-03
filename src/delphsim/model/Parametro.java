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
 * Los objetos de esta clase representan los parámetros que se pueden definir
 * en la aplicación.
 * @author Víctor E. Tamames Gómez
 */
public class Parametro implements Cloneable {

    /**
     * El nombre del parámetro (obligatorio). Debe ser único en el conjunto
     * compuesto por los nombres de los parámetros, procesos y categorías.
     */
    private String nombre;
    
    /**
     * Una descripción del parámetro (opcional).
     */
    private String descripcion;
    
    /**
     * La definición del parámetro para simulaciones continuas.
     */
    private String definicionContinua;
    
    /**
     * Otros parámetros que dependen de éste. 
     */
    private Vector parametrosVinculados = new Vector();
    
    /**
     * Procesos que dependen de este parámetro.
     */
    private Vector procesosVinculados = new Vector();
    
    /**
     * Compartimentos que dependen de este parámetro.
     */
    private Vector compartimentosVinculados = new Vector();

    /**
     * Constructor de la clase.
     */
    public Parametro() {
    }

    /**
     * Método para cambiar el nombre del parámetro.
     * @param nombrePar El nuevo nombre.
     */
    public void setNombre(String nombrePar) {
        this.nombre = nombrePar;
    }

    /**
     * Método para obtener el nombre del parámetro.
     * @return El nombre del parámetro.
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Método para cambiar la descripción del parámetro.
     * @param descripcionPar La nueva descripción.
     */
    public void setDescripcion(String descripcionPar) {
        this.descripcion = descripcionPar;
    }

    /**
     * Método para obtener la descripción del parámetro.
     * @return La descripción del parámetro.
     */
    public String getDescripcion() {
        return this.descripcion;
    }

    /**
     * Método para cambiar la definición continua del parámetro.
     * @param valorPar La nueva definición continua.
     */
    public void setDefinicionContinua(String valorPar) {
        this.definicionContinua = valorPar;
    }

    /**
     * Método para obtener la definición continua del parámetro.
     * @return La definición continua del parámetro.
     */
    public String getDefinicionContinua() {
        return this.definicionContinua;
    }

    /**
     * Método para cambiar el conjunto de parámetros que dependen de éste.
     * @param parametrosVinculadosPar Los nombres del nuevo conjunto de
     *                                parámetros vinculados.
     */
    public void setParametrosVinculados(String[] parametrosVinculadosPar) {
        this.parametrosVinculados = new Vector();
        for (String parVinc : parametrosVinculadosPar) {
            if (!this.parametrosVinculados.contains(parVinc)) {
                this.parametrosVinculados.add(parVinc);
            }
        }
    }

    /**
     * Método para obtener los parámetros que dependen de éste.
     * @return Los nombres del conjunto de parámetros vinculados.
     */
    public String[] getParametrosVinculados() {
        String[] parVinculados = new String[this.parametrosVinculados.size()];
        for (int i = 0; i < this.parametrosVinculados.size(); i++) {
            parVinculados[i] = (String) this.parametrosVinculados.get(i);
        }
        return parVinculados;
    }
    
    /**
     * Método para añadir un nuevo parámetro a la lista de parámetros vinculados.
     * Si ya estuviera contenido en ella, no se hace nada.
     * @param parVincNuevo El nombre del parámetro a añadir.
     */
    public void anadirParametroVinculado(String parVincNuevo) {
        if (!this.parametrosVinculados.contains(parVincNuevo)) {
            this.parametrosVinculados.add(parVincNuevo);
        }
    }

    /**
     * Método para eliminar un parámetro de la lista de parámetros vinculados.
     * Si no estuviera contenido en ella, no se hace nada.
     * @param parVincEliminado El nombre del parámetro a eliminar.
     */
    public void eliminarParametroVinculado(String parVincEliminado) {
        this.parametrosVinculados.remove(parVincEliminado);
    }

    /**
     * Método para cambiar el conjunto de procesos que dependen de este
     * parámetro.
     * @param procesosVinculadosPar Los nombres del nuevo conjunto de procesos
     *                              vinculados.
     */
    public void setProcesosVinculados(String[] procesosVinculadosPar) {
        this.procesosVinculados = new Vector();
        for (String procVinc : procesosVinculadosPar) {
            if (!this.procesosVinculados.contains(procVinc)) {
                this.procesosVinculados.add(procVinc);
            }
        }
    }

    /**
     * Método para obtener los procesos que dependen de este parámetro.
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
     * Método para cambiar el conjunto de compartimentos que dependen de este
     * parámetro.
     * @param compartimentosVinculadosPar Los nombres del nuevo conjunto de
     *                                    compartimentos.
     */
    public void setCompartimentosVinculados(String[] compartimentosVinculadosPar) {
        this.compartimentosVinculados = new Vector();
        for (String compVinc : compartimentosVinculadosPar) {
            if (!this.compartimentosVinculados.contains(compVinc)) {
                this.compartimentosVinculados.add(compVinc);
            }
        }
    }

    /**
     * Método para obtener el conjunto de compartimentos que dependen de este
     * parámetro.
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
     * dependen de este parámetro. Si ya estuviera contenido en ella, no se hace
     * nada.
     * @param compVincNuevo El nombre del compartimento a añadir.
     */
    public void anadirCompartimentoVinculado(String compVincNuevo) {
        if (!this.compartimentosVinculados.contains(compVincNuevo)) {
            this.compartimentosVinculados.add(compVincNuevo);
        }
    }
    
    /**
     * Método para eliminar un compartimento de la lista de compartimentos que
     * dependen de este parámetro. Si no estuviera contenido en ella, no se hace
     * nada.
     * @param compVincEliminado El nombre del compartimento a eliminar.
     */
    public void eliminarCompartimentoVinculado(String compVincEliminado) {
        this.compartimentosVinculados.remove(compVincEliminado);
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoParametro El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoParametro) {
        this.setNombre(elementoParametro.attributeValue("nombre"));
        this.setDescripcion(elementoParametro.elementText("descripcion"));

        String[] parVinculados = new String[elementoParametro.elements("parametroVinculado").size()];
        int indice = 0;
        for (Iterator i = elementoParametro.elementIterator("parametroVinculado"); i.hasNext();) {
            Element elementoParametroVinculado = (Element) i.next();
            parVinculados[indice++] = elementoParametroVinculado.attributeValue("nombre");
        }
        this.setParametrosVinculados(parVinculados);

        String[] procVinculados = new String[elementoParametro.elements("procesoVinculado").size()];
        indice = 0;
        for (Iterator i = elementoParametro.elementIterator("procesoVinculado"); i.hasNext();) {
            Element elementoProcesoVinculado = (Element) i.next();
            procVinculados[indice++] = elementoProcesoVinculado.attributeValue("nombre");
        }
        this.setProcesosVinculados(procVinculados);

        String[] compVinculados = new String[elementoParametro.elements("compartimentoVinculado").size()];
        indice = 0;
        for (Iterator i = elementoParametro.elementIterator("compartimentoVinculado"); i.hasNext();) {
            Element elementoCompartimentoVinculado = (Element) i.next();
            compVinculados[indice++] = elementoCompartimentoVinculado.attributeValue("nombre");
        }
        this.setCompartimentosVinculados(compVinculados);

        Element elementoDefinicionContinua = elementoParametro.element("definicionContinua");
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
        Element elementoParametro = new DefaultElement("parametro");

        elementoParametro.addAttribute("nombre", this.nombre);
        if (this.descripcion != null) {
            if (!this.descripcion.equals("")) {
                Element elementoDescripcion = elementoParametro.addElement("descripcion");
                elementoDescripcion.setText(this.descripcion);
            }
        }
        for (int i = 0; i < this.parametrosVinculados.size(); i++) {
            Element elementoParametroVinculado = elementoParametro.addElement("parametroVinculado");
            elementoParametroVinculado.addAttribute("nombre",(String) this.parametrosVinculados.get(i));
        }
        for (int i = 0; i < this.procesosVinculados.size(); i++) {
            Element elementoProcesoVinculado = elementoParametro.addElement("procesoVinculado");
            elementoProcesoVinculado.addAttribute("nombre",(String) this.procesosVinculados.get(i));
        }
        for (int i = 0; i < this.compartimentosVinculados.size(); i++) {
            Element elementoCompartimentoVinculado = elementoParametro.addElement("compartimentoVinculado");
            elementoCompartimentoVinculado.addAttribute("nombre",(String) this.compartimentosVinculados.get(i));
        }
        if (this.definicionContinua != null && !this.definicionContinua.equals("")) {
            Element elementoDefinicionContinua = elementoParametro.addElement("definicionContinua");
            elementoDefinicionContinua.addAttribute("definicion", this.definicionContinua);
        }

        return elementoParametro;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Parametro clone() {
        Parametro clon = new Parametro();
        clon.setNombre(this.getNombre());
        clon.setDescripcion(this.getDescripcion());
        clon.setParametrosVinculados(this.getParametrosVinculados());
        clon.setProcesosVinculados(this.getProcesosVinculados());
        clon.setCompartimentosVinculados(this.getCompartimentosVinculados());
        clon.setDefinicionContinua(this.getDefinicionContinua());
        return clon;
    }
}
