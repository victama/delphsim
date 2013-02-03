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
 * Los objetos de esta clase representan los procesos que se pueden definir
 * en la aplicación.
 * @author Víctor E. Tamames Gómez
 */
public class Proceso implements Cloneable {

    /**
     * El nombre del proceso (obligatorio). Debe ser único en el conjunto
     * compuesto por los nombres de los parámetros, procesos y categorías.
     */
    private String nombre;
    
    /**
     * Descripción del proceso (opcional).
     */
    private String descripcion;
    
    /**
     * Otros procesos que dependen de éste.
     */
    private Vector procesosVinculados = new Vector();
    
    /**
     * Compartimentos que dependen de este proceso.
     */
    private Vector compartimentosVinculados = new Vector();
    
    /**
     * Tramos en los que se divide la definición continua de este proceso.
     */
    private TramoContinua[] tramosContinua = new TramoContinua[0];

    /**
     * Constructor de la clase.
     */
    public Proceso() {
    }

    /**
     * Método para cambiar el nombre a este proceso.
     * @param nombreProc El nuevo nombre para el proceso.
     */
    public void setNombre(String nombreProc) {
        this.nombre = nombreProc;
    }

    /**
     * Método para obtener el nombre de este proceso.
     * @return El nombre de este proceso.
     */
    public String getNombre() {
        return this.nombre;
    }
    
    /**
     * Método para cambiar la descripción de este proceso.
     * @param descripcionProc La nueva descripción para este proceso.
     */
    public void setDescripcion(String descripcionProc) {
        this.descripcion = descripcionProc;
    }

    /**
     * Método para obtener la descripción de este proceso.
     * @return La descripción de este proceso.
     */
    public String getDescripcion() {
        return this.descripcion;
    }
    
    /**
     * Dado un tiempo, este método devuelve la definición continua que
     * corresponda de entre las especificadas en sus tramos.
     * @param tiempo El tiempo cuya definición se quiere averiguar.
     * @return La definición continua que corresponde.
     */
    public String getDefinicionContinua(double tiempo) {
        for (int i = 0; i < this.tramosContinua.length-1; i++) {
            if (tiempo >= this.getTramoContinua(i).getTiempoInicio() && tiempo < this.getTramoContinua(i+1).getTiempoInicio()) {
                if (this.nombre.equals("aaa")) {
                    System.out.println(tiempo + " : " + this.getTramoContinua(i).getDefinicionContinua());
                }
                return this.getTramoContinua(i).getDefinicionContinua();
            }
        }
        if (this.nombre.equals("aaa")) {
            System.out.println(tiempo + " : " + this.getTramoContinua(this.tramosContinua.length-1).getDefinicionContinua());
        }
        return this.getTramoContinua(this.tramosContinua.length-1).getDefinicionContinua();
    }

    /**
     * Método para cambiar el conjunto de procesos que dependen de éste.
     * @param procesosVinculadosProc Los nombres del nuevo conjunto de procesos
     *                               vinculados.
     */
    public void setProcesosVinculados(String[] procesosVinculadosProc) {
        this.procesosVinculados = new Vector();
        for (int i = 0; i < procesosVinculadosProc.length; i++) {
            if (!this.procesosVinculados.contains(procesosVinculadosProc[i])) {
                this.procesosVinculados.add(procesosVinculadosProc[i]);
            }
        }
    }

    /**
     * Método para obtener los procesos que dependen de éste.
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
     * proceso.
     * @param compartimentosVinculadosProc Los nombres del nuevo conjunto de
     *                                     compartimentos.
     */
    public void setCompartimentosVinculados(String[] compartimentosVinculadosProc) {
        this.compartimentosVinculados = new Vector();
        for (int i = 0; i < compartimentosVinculadosProc.length; i++) {
            if (!this.compartimentosVinculados.contains(compartimentosVinculadosProc[i])) {
                this.compartimentosVinculados.add(compartimentosVinculadosProc[i]);
            }
        }
    }

    /**
     * Método para obtener el conjunto de compartimentos que dependen de este
     * proceso.
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
     * dependen de este proceso. Si ya estuviera contenido en ella, no se hace
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
     * dependen de este proceso. Si no estuviera contenido en ella, no se hace
     * nada.
     * @param compVincEliminado El nombre del compartimento a eliminar.
     */
    public void eliminarCompartimentoVinculado(String compVincEliminado) {
        this.compartimentosVinculados.remove(compVincEliminado);
    }

    /**
     * Método para cambiar los tramos en que se divide la definición continua
     * de este proceso.
     * @param tramosProc Nuevos tramos para este proceso.
     */
    public void setTramosContinua(TramoContinua[] tramosProc) {
        this.tramosContinua = tramosProc;
    }

    /**
     * Método para obtener los tramos en que se divide la definición continua
     * de este proceso.
     * @return Los tramos de este proceso.
     */
    public TramoContinua[] getTramosContinua() {
        return this.tramosContinua;
    }

    /**
     * Método para cambiar el tramo de la definición continua de este proceso
     * identificado por el índice pasado como parámetro.
     * @param tramoProc El nuevo tramo que ocupará el lugar del viejo.
     * @param indice El índice del tramo.
     */
    public void setTramoContinua(TramoContinua tramoProc, int indice) {
        this.tramosContinua[indice] = tramoProc;
    }

    /**
     * Método para obtener el tramo de la definición continua de este proceso
     * identificado por el índice pasado como parámetro.
     * @param indice El índice del tramo.
     * @return El tramo.
     */
    public TramoContinua getTramoContinua(int indice) {
        return this.tramosContinua[indice];
    }

    /**
     * Método para cargar los datos contenidos en un objeto de tipo
     * <CODE>org.dom4j.Element</CODE> en este objeto.
     * @param elementoProceso El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public void cargarDesdeXML(Element elementoProceso) {
        this.setNombre(elementoProceso.attributeValue("nombre"));
        this.setDescripcion(elementoProceso.elementText("descripcion"));

        String[] procVinculados = new String[elementoProceso.elements("procesoVinculado").size()];
        int indice = 0;
        for (Iterator i = elementoProceso.elementIterator("procesoVinculado"); i.hasNext();) {
            Element elementoProcesoVinculado = (Element) i.next();
            procVinculados[indice++] = elementoProcesoVinculado.attributeValue("nombre");
        }
        this.setProcesosVinculados(procVinculados);

        String[] compVinculados = new String[elementoProceso.elements("compartimentoVinculado").size()];
        indice = 0;
        for (Iterator i = elementoProceso.elementIterator("compartimentoVinculado"); i.hasNext();) {
            Element elementoCompartimentoVinculado = (Element) i.next();
            compVinculados[indice++] = elementoCompartimentoVinculado.attributeValue("nombre");
        }
        this.setCompartimentosVinculados(compVinculados);

        Element elementoDefinicionContinua = elementoProceso.element("definicionContinua");
        if (elementoDefinicionContinua != null) {
            this.tramosContinua = new TramoContinua[elementoDefinicionContinua.elements("tramo").size()];
            indice = 0;
            for (Iterator i = elementoDefinicionContinua.elementIterator("tramo"); i.hasNext();) {
                Element elementoTramo = (Element) i.next();
                this.tramosContinua[indice] = new TramoContinua();
                this.tramosContinua[indice++].cargarDesdeXML(elementoTramo);
            }
        }
    }

    /**
     * Método para volcar los datos de este objeto en uno de tipo
     * <CODE>org.dom4j.Element</CODE>.
     * @return El objeto <CODE>org.dom4j.Element</CODE>.
     */
    public Element volcarAXML() {
        Element elementoProceso = new DefaultElement("proceso");

        elementoProceso.addAttribute("nombre", this.nombre);
        if (this.descripcion != null) {
            if (!this.descripcion.equals("")) {
                Element elementoDescripcion = elementoProceso.addElement("descripcion");
                elementoDescripcion.setText(this.descripcion);
            }
        }

        for (int i = 0; i < this.procesosVinculados.size(); i++) {
            Element elementoProcesoVinculado = elementoProceso.addElement("procesoVinculado");
            elementoProcesoVinculado.addAttribute("nombre",(String) this.procesosVinculados.get(i));
        }
        for (int i = 0; i < this.compartimentosVinculados.size(); i++) {
            Element elementoCompartimentoVinculado = elementoProceso.addElement("compartimentoVinculado");
            elementoCompartimentoVinculado.addAttribute("nombre",(String) this.compartimentosVinculados.get(i));
        }

        if (this.tramosContinua != null && this.tramosContinua.length > 0) {
            Element elementoDefinicionContinua = elementoProceso.addElement("definicionContinua");
            for (int i = 0; i < this.tramosContinua.length; i++) {
                elementoDefinicionContinua.add(this.getTramoContinua(i).volcarAXML());
            }
        }

        return elementoProceso;
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Proceso clone() {
        int numTramos = this.getTramosContinua().length;
        Proceso clon = new Proceso();
        clon.setNombre(this.getNombre());
        clon.setDescripcion(this.getDescripcion());
        clon.setProcesosVinculados(this.getProcesosVinculados());
        clon.setCompartimentosVinculados(this.getCompartimentosVinculados());
        clon.setTramosContinua(new TramoContinua[numTramos]);
        for (int i = 0; i < numTramos; i++) {
            clon.setTramoContinua(this.getTramoContinua(i).clone(), i);
        }
        return clon;
    }
}
