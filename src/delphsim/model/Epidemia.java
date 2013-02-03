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

import delphsim.util.*;
import delphsim.util.random.*;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashSet;

import javax.swing.DefaultListModel;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;

import org.nfunk.jep.FunctionTable;
import org.nfunk.jep.JEP;
import org.nfunk.jep.function.*;

import org.xml.sax.SAXException;

/**
 * Clase representa la epidemia que se está modelando y se va a simular.
 * Contiene referencias a todos los elementos que la componen: parámetros,
 * procesos, población, compartimentos generados, palabras reservadas, etc.
 * También se encarga de cargar/guardar su información desde/a archivos XML.
 * @author Víctor E. Tamames Gómez
 */
public class Epidemia implements Cloneable {
    
    /**
     * Tiempo máximo de simulación.
     */
    private float tiempoSimulacion = 0;
    
    /**
     * Unidad de tiempo en la que se ha definido el modelo.
     */
    private String unidadTiempo;
    
    /**
     * Conjunto de parámetros definidos para este modelo.
     */
    private Parametro[] parametros = new Parametro[0];
    
    /**
     * Conjunto de procesos definidos para este modelo.
     */
    private Proceso[] procesos = new Proceso[0];
    
    /**
     * Población modelada para esta epidemia.
     */
    private Poblacion poblacion;
    
    /**
     * Conjunto de compartimentos entre los que se distribuye la población.
     */
    private Compartimento[] compartimentos = new Compartimento[0];
    
    /**
     * Conjunto de palabras clave o 'atajos' que permiten al usuario hacer
     * referencia a varios compartimentos al mismo tiempo.
     */
    private Atajo[] atajos = new Atajo[0];
    
    /**
     * Conjunto de resultados a obtener al término de una simulación.
     */
    private Resultado[] resultados = new Resultado[0];
    
    /**
     * Palabras reservadas: contiene los nombres de los parámetros, de los
     * procesos y de las categorías, los cuales no pueden repetirse.
     */
    private HashSet palabrasReservadas = new HashSet();
    
    /**
     * Los nombres de los atajos para poder crearlos.
     */
    private Vector palabrasAtajos = new Vector();

    /**
     * Constructor de la clase. Crea un nuevo objeto y reserva por defecto las
     * funciones estándar disponibles.
     */
    public Epidemia() {
        // Crear un nuevo JEP de DelphSim y reservar palabras
        JEP jep = Epidemia.CrearDelphSimJEP();
        FunctionTable ft = jep.getFunctionTable();
        for (Object e : ft.keySet()) {
            this.palabrasReservadas.add(e.toString());
        }
    }

    /**
     * Método que permite cambiar el tiempo de simulación de la epidemia.
     * @param tiempoSimulacionEpi El nuevo tiempo de simulación.
     */
    public void setTiempoSimulacion(float tiempoSimulacionEpi) {
        this.tiempoSimulacion = tiempoSimulacionEpi;
    }

    /**
     * Método que permite cambiar el tiempo de simulación, transformándolo a las
     * unidades indicadas como parámetro.
     * @param tiempoSimulacionEpi Cuánto tiempo debe simularse la epidemia.
     * @param unidadTiempo La unidad de tiempo en que se da el otro parámetro.
     *                     Es un número de 0 a 7 para Segundos, Minutos, Horas,
     *                     Días, Semanas, Meses o Años en ese orden.
     */
    public void setTiempoSimulacion(float tiempoSimulacionEpi, int unidadTiempo) {
        if (this.unidadTiempo.equals("Segundos")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi*60;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi*3600;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi*86400;
                    break;
                case 4:
                    this.tiempoSimulacion = tiempoSimulacionEpi*604800;
                    break;
                case 5:
                    this.tiempoSimulacion = tiempoSimulacionEpi*2592000;
                    break;
                case 6:
                    this.tiempoSimulacion = tiempoSimulacionEpi*31536000;
                    break;
                default:
                    return;
            }
        } else if (this.unidadTiempo.equals("Minutos")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi/60;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi*60;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi*1440;
                    break;
                case 4:
                    this.tiempoSimulacion = tiempoSimulacionEpi*10080;
                    break;
                case 5:
                    this.tiempoSimulacion = tiempoSimulacionEpi*43200;
                    break;
                case 6:
                    this.tiempoSimulacion = tiempoSimulacionEpi*525600;
                    break;
                default:
                    return;
            }
        } else if (this.unidadTiempo.equals("Horas")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi/360;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi/60;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi*24;
                    break;
                case 4:
                    this.tiempoSimulacion = tiempoSimulacionEpi*168;
                    break;
                case 5:
                    this.tiempoSimulacion = tiempoSimulacionEpi*720;
                    break;
                case 6:
                    this.tiempoSimulacion = tiempoSimulacionEpi*8760;
                    break;
                default:
                    return;
            }
        } else if (this.unidadTiempo.equals("Días")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi/86400;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi/1440;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi/24;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                case 4:
                    this.tiempoSimulacion = tiempoSimulacionEpi*7;
                    break;
                case 5:
                    this.tiempoSimulacion = tiempoSimulacionEpi*30;
                    break;
                case 6:
                    this.tiempoSimulacion = tiempoSimulacionEpi*365;
                    break;
                default:
                    return;
            }
        } else if (this.unidadTiempo.equals("Semanas")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi/604800;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi/10080;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi/168;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi/7;
                    break;
                case 4:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                case 5:
                    this.tiempoSimulacion = (tiempoSimulacionEpi/7)*30;
                    break;
                case 6:
                    this.tiempoSimulacion = (tiempoSimulacionEpi/7)*365;
                    break;
                default:
                    return;
            }
        } else if (this.unidadTiempo.equals("Meses")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi/2592000;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi/43200;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi/720;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi/30;
                    break;
                case 4:
                    this.tiempoSimulacion = (tiempoSimulacionEpi/30)*7;
                    break;
                case 5:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                case 6:
                    this.tiempoSimulacion = (tiempoSimulacionEpi/30)*365;
                    break;
                default:
                    return;
            }
        } else if (this.unidadTiempo.equals("Años")) {
            switch (unidadTiempo) {
                case 0:
                    this.tiempoSimulacion = tiempoSimulacionEpi/31536000;
                    break;
                case 1:
                    this.tiempoSimulacion = tiempoSimulacionEpi/525600;
                    break;
                case 2:
                    this.tiempoSimulacion = tiempoSimulacionEpi/8760;
                    break;
                case 3:
                    this.tiempoSimulacion = tiempoSimulacionEpi/365;
                    break;
                case 4:
                    this.tiempoSimulacion = (tiempoSimulacionEpi/365)*7;
                    break;
                case 5:
                    this.tiempoSimulacion = (tiempoSimulacionEpi/365)*30;
                    break;
                case 6:
                    this.tiempoSimulacion = tiempoSimulacionEpi;
                    break;
                default:
                    return;
            }
        }
    }

    /**
     * Método que permite obtener el tiempo de simulación de la epidemia.
     * @return El tiempo total de simulación.
     */
    public float getTiempoSimulacion() {
        return this.tiempoSimulacion;
    }

    /**
     * Método que permite cambiar la unidad de tiempo en que están especificados
     * los elementos del modelo.
     * @param unidadTiempoEpi La nueva unidad de tiempo.
     */
    public void setUnidadTiempo(String unidadTiempoEpi) {
        this.unidadTiempo = unidadTiempoEpi;
    }

    /**
     * Método que permite obtener la unidad de tiempo en que están especificados
     * los elementos del modelo.
     * @return La unidad de tiempo en una cadena de texto.
     */
    public String getUnidadTiempo() {
        return this.unidadTiempo;
    }

    /**
     * Método para cambiar la población de la epidemia.
     * @param poblacionEpi La nueva población.
     */
    public void setPoblacion(Poblacion poblacionEpi) {
        this.poblacion = poblacionEpi;
    }

    /**
     * Método para obtener la población de la epidemia.
     * @return La población de la epidemia.
     */
    public Poblacion getPoblacion() {
        return this.poblacion;
    }

    /**
     * Método para cambiar los compartimentos de la población.
     * @param compartimentosEpi El nuevo conjunto de compartimentos.
     */
    public void setCompartimentos(Compartimento[] compartimentosEpi) {
        this.compartimentos = compartimentosEpi;
    }

    /**
     * Método para cambiar un compartimento de la población.
     * @param compartimentosEpi El nuevo compartimento.
     * @param indice El índice del compartimento a sustituir por el nuevo.
     */
    public void setCompartimento(Compartimento compartimentosEpi, int indice) {
        this.compartimentos[indice] = compartimentosEpi;
    }

    /**
     * Método para obtener el conjunto de compartimentos de la población.
     * @return El conjunto de compartimentos de la población.
     */
    public Compartimento[] getCompartimentos() {
        return this.compartimentos;
    }

    /**
     * Método para obtener un compartimento.
     * @param indice El índice del compartimento.
     * @return El compartimento.
     */
    public Compartimento getCompartimento(int indice) {
        return this.compartimentos[indice];
    }

    /**
     * Método para obtener un compartimento.
     * @param nombre El nombre del compartimento.
     * @return El compartimento.
     */
    public Compartimento getCompartimento(String nombre) {
        for (int i = 0; i < this.compartimentos.length; i++) {
            if (this.compartimentos[i].getNombre().equals(nombre))
                return this.compartimentos[i];
        }
        return null;
    }

    /**
     * Método para cambiar el conjunto de parámetros del modelo.
     * @param parametrosEpi El nuevo conjunto de parámetros.
     */
    public void setParametros(Parametro[] parametrosEpi) {
        this.parametros = parametrosEpi;
    }

    /**
     * Método para cambiar un parámetro del modelo.
     * @param parametroEpi El nuevo parámetro.
     * @param indice El índice del parámetro a reemplazar.
     */
    public void setParametro(Parametro parametroEpi, int indice) {
        this.parametros[indice] = parametroEpi;
    }

    /**
     * Método para obtener el conjunto de parámetros del modelo.
     * @return El conjunto de parámetros del modelo.
     */
    public Parametro[] getParametros() {
        return this.parametros;
    }

    /**
     * Método para obtener un parámetro del modelo.
     * @param indice El índice del parámetro a obtener.
     * @return El parámetro.
     */
    public Parametro getParametro(int indice) {
        return this.parametros[indice];
    }

    /**
     * Método para obtener un parámetro del modelo.
     * @param nombre El nombre del parámetro a obtener.
     * @return El parámetro.
     */
    public Parametro getParametro(String nombre) {
        for (int i = 0; i < this.parametros.length; i++) {
            if (this.parametros[i].getNombre().equals(nombre))
                return this.parametros[i];
        }
        return null;
    }

    /**
     * Añade un parámetro nuevo a la lista actual de parámetros.
     * @param param El parámetro a añadir.
     */
    public void anadirParametro(Parametro param) {
        Parametro[] nuevaLista = new Parametro[this.parametros.length+1];
        for (int i = 0; i < this.parametros.length; i++) {
            nuevaLista[i] = this.parametros[i];
        }
        nuevaLista[this.parametros.length] = param;
        this.parametros = nuevaLista;
    }

    /**
     * Elimina el parámetro indicado por el índice de la lista de parámetros.
     * @param indice La posición en la que se encuentra el parámetro.
     */
    public void eliminarParametro(int indice) {
        // Rehacer la lista, pero sin el parámetro eliminado
        String nombreParEliminado = this.parametros[indice].getNombre();
        Parametro[] nuevosParametros = new Parametro[this.parametros.length-1]; 
        for (int i = 0; i < this.parametros.length; i++) {
            if (i < indice) {
                nuevosParametros[i] = this.parametros[i];
            } else if (i > indice) {
                nuevosParametros[i-1] = this.parametros[i];
            }
        }

        // Este parametro podría depender de otros, eliminar esas dependencias
        for (Parametro par : nuevosParametros) {
            par.eliminarParametroVinculado(nombreParEliminado);
        }
        
        // El nombre de este parámetro deja de estar reservado
        this.palabrasReservadas.remove(nombreParEliminado);

        // Asignar la nueva lista
        this.parametros = nuevosParametros;
    }

    /**
     * Intercambia el parámetro de la posición posInicial con el de la posición
     * posFinal.
     * @param posInicial Posición origen del parámetro.
     * @param posFinal Posición destino del parámetro.
     */
    public void moverParametro(int posInicial, int posFinal) {
        Parametro origen = this.getParametro(posInicial);
        Parametro destino = this.getParametro(posFinal);
        this.parametros[posInicial] = destino;
        this.parametros[posFinal] = origen;
    }

    /**
     * Método para cambiar el conjunto de procesos del modelo.
     * @param procesosEpi El nuevo conjunto de procesos.
     */
    public void setProcesos(Proceso[] procesosEpi) {
        this.procesos = procesosEpi;
    }

    /**
     * Método para cambiar un proceso del modelo.
     * @param procesoEpi El nuevo proceso.
     * @param indice El índice del proceso a reemplazar.
     */
    public void setProceso(Proceso procesoEpi, int indice) {
        this.procesos[indice] = procesoEpi;
    }

    /**
     * Método para obtener el conjunto de procesos del modelo.
     * @return El conjunto de procesos.
     */
    public Proceso[] getProcesos() {
        return this.procesos;
    }

    /**
     * Método para obtener un proceso del modelo.
     * @param indice El índice del proceso a obtener.
     * @return El proceso.
     */
    public Proceso getProceso(int indice) {
        return this.procesos[indice];
    }

    /**
     * Método para obtener un proceso del modelo.
     * @param nombre El nombre del proceso a obtener.
     * @return El proceso.
     */
    public Proceso getProceso(String nombre) {
        for (int i = 0; i < this.procesos.length; i++) {
            if (this.procesos[i].getNombre().equals(nombre))
                return this.procesos[i];
        }
        return null;
    }

    /**
     * Añade un proceso nuevo a la lista actual de procesos.
     * @param proc El proceso a añadir.
     */
    public void anadirProceso(Proceso proc) {
        Proceso[] nuevaLista = new Proceso[this.procesos.length+1];
        for (int i = 0; i < this.procesos.length; i++) {
            nuevaLista[i] = this.procesos[i];
        }
        nuevaLista[this.procesos.length] = proc;
        this.procesos = nuevaLista;
    }

    /**
     * Elimina el proceso indicado por el índice de la lista de procesos.
     * @param indice La posición en la que se encuentra el proceso.
     */
    public void eliminarProceso(int indice) {
        // Rehacer la lista, pero sin el proceso eliminado
        String nombreProcEliminado = this.procesos[indice].getNombre();
        Proceso[] nuevosProcesos = new Proceso[this.procesos.length-1]; 
        for (int i = 0; i < this.procesos.length; i++) {
            if (i < indice) {
                nuevosProcesos[i] = this.procesos[i];
            } else if (i > indice) {
                nuevosProcesos[i-1] = this.procesos[i];
            }
        }

        // Este proceso podría depender de otros parametros, procesos o
        // compartimentos; eliminar esas dependencias
        for (Parametro par : this.parametros) {
            par.eliminarProcesoVinculado(nombreProcEliminado);
        }
        for (Proceso proc : this.procesos) {
            proc.eliminarProcesoVinculado(nombreProcEliminado);
        }
        for (Compartimento comp : this.compartimentos) {
            comp.eliminarProcesoVinculado(nombreProcEliminado);
        }
        
        // El nombre de este proceso deja de estar reservado
        this.palabrasReservadas.remove(nombreProcEliminado);

        // Asignar la nueva lista
        this.procesos = nuevosProcesos;
    }

    /**
     * Intercambia el proceso de la posición posInicial con el de la posición
     * posFinal.
     * @param posInicial Posición origen del proceso.
     * @param posFinal Posición destino del proceso.
     */
    public void moverProceso(int posInicial, int posFinal) {
        Proceso origen = this.getProceso(posInicial);
        Proceso destino = this.getProceso(posFinal);
        this.procesos[posInicial] = destino;
        this.procesos[posFinal] = origen;
    }
    
    /**
     * Método para cambiar el conjunto de atajos del modelo.
     * @param atajosEpi El nuevo conjunto de atajos.
     */
    public void setAtajos(Atajo[] atajosEpi) {
        this.atajos = atajosEpi;
    }
    
    /**
     * Método para cambiar un atajo del modelo.
     * @param ataj El nuevo atajo.
     * @param indice El índice del atajo a reemplazar.
     */
    public void setAtajo(Atajo ataj, int indice) {
        this.atajos[indice] = ataj;
    }
    
    /**
     * Método para obtener el conjunto de atajos del modelo.
     * @return El conjunto de atajos.
     */
    public Atajo[] getAtajos() {
        return this.atajos;
    }
    
    /**
     * Método para obtener un atajo del modelo.
     * @param indice El índice del atajo a obtener.
     * @return El atajo.
     */
    public Atajo getAtajo(int indice) {
        return this.atajos[indice];
    }

    /**
     * Método para obtener un atajo del modelo.
     * @param nombre El nombre del atajo a obtener.
     * @return El atajo.
     */
    public Atajo getAtajo(String nombre) {
        for (int i = 0; i < this.atajos.length; i++) {
            if (this.atajos[i].getNombre().equals(nombre))
                return this.atajos[i];
        }
        return null;
    }
    
    /**
     * Método para cambiar el conjunto de resultados del modelo.
     * @param res El nuevo conjunto de resultados.
     */
    public void setResultados(Resultado[] res) {
        if (resultados != null) {
            for (int i = 0; i < this.resultados.length; i++) {
                this.resultados[i].eliminarFunciones();
            }
        }
        this.resultados = res;
    }
    
    /**
     * Método para cambiar un resultado del modelo.
     * @param res El nuevo resultado.
     * @param indice El índice del resultado a reemplazar.
     */
    public void setResultado(Resultado res, int indice) {
        if (this.resultados[indice] != null) {
            this.resultados[indice].eliminarFunciones();
        }
        this.resultados[indice] = res;
    }
    
    /**
     * Método para obtener el conjunto de resultados del modelo.
     * @return El conjunto de resultados.
     */
    public Resultado[] getResultados() {
        return this.resultados;
    }
    
    /**
     * Método para obtener un resultado del modelo.
     * @param indice El índice del resultado a obtener.
     * @return El resultado.
     */
    public Resultado getResultado(int indice) {
        return this.resultados[indice];
    }

    /**
     * Añade un resultado nuevo a la lista actual de resultados.
     * @param res El resultado a añadir.
     */
    public void anadirResultado(Resultado res) {
        Resultado[] nuevaLista = new Resultado[this.resultados.length+1];
        for (int i = 0; i < this.resultados.length; i++) {
            nuevaLista[i] = this.resultados[i];
        }
        nuevaLista[this.resultados.length] = res;
        this.resultados = nuevaLista;
    }

    /**
     * Elimina el resultado indicado por el índice de la lista de resultados.
     * @param indice La posición en la que se encuentra el resultado.
     */
    public void eliminarResultado(int indice) {
        // Rehacer la lista, pero sin el resultado eliminado
        Resultado[] nuevosResultados = new Resultado[this.resultados.length-1]; 
        for (int i = 0; i < this.resultados.length; i++) {
            if (i < indice) {
                nuevosResultados[i] = this.resultados[i];
            } else if (i > indice) {
                nuevosResultados[i-1] = this.resultados[i];
            }
        }
        // Asignar la nueva lista
        this.resultados = nuevosResultados;
    }
    
    /**
     * Vacía la lista de resultados.
     */
    public void eliminarResultados() {
        if (this.resultados != null) {
            for (int i = 0; i < this.resultados.length; i++) {
                this.resultados[i].eliminarFunciones();
            }
        }
        File f = new File(new File(System.getProperty("java.class.path")).getParent() + File.separator + "temp" + File.separator + "tiempo_.temp");
        if (f.exists()) {
            if (!f.delete()) {
                f.deleteOnExit();
            }
        }
        this.resultados = new Resultado[0];
    }
    
    /**
     * Método para cambiar el conjunto de palabras reservadas.
     * @param pRes El nuevo conjunto de palabras reservadas.
     */
    public void setPalabrasReservadas(HashSet pRes) {
        this.palabrasReservadas = pRes;
    }
    
    /**
     * Método para obtener el conjunto de palabras reservadas.
     * @return El conjunto de palabras reservadas.
     */
    public HashSet getPalabrasReservadas() {
        return this.palabrasReservadas;
    }
    
    /**
     * Método para cambiar el conjunto de nombres de atajos.
     * @param pAjs El nuevo conjunto de nombres de atajos.
     */
    public void setPalabrasAtajos(Vector pAjs) {
        this.palabrasAtajos = pAjs;
    }
    
    /**
     * Método para obtener el conjunto de nombres de atajos.
     * @return El conjunto de nombres de atajos.
     */
    public Vector getPalabrasAtajos() {
        return this.palabrasAtajos;
    }

    /**
     * Comprueba si el elemento indicado ya está definido, bien sea como
     * parámetro, bien como proceso, bien como compartimento, o como atajo.
     * @param elemento  El elemento a comprobar, de tipo Parametro, Proceso,
     *                  Compartimento o String (el nombre directamente).
     * @return Si ya está definido, el tipo del objeto existente. Si no, null.
     */
    public Class estaDefinido(Object elemento) {
        String nombre = "";
        if (elemento.getClass() == Parametro.class) {
            nombre = ((Parametro) elemento).getNombre();
        } else if (elemento.getClass() == Proceso.class) {
            nombre = ((Proceso) elemento).getNombre();
        } else if (elemento.getClass() == Compartimento.class) {
            nombre = ((Compartimento) elemento).getNombre();
        } else {
            nombre = elemento.toString();
        }
        for (int i = 0; i < this.parametros.length; i++) {
            if (this.parametros[i].getNombre().equals(nombre)) {
                return Parametro.class;
            }
        }
        for (int i = 0; i < this.procesos.length; i++) {
            if (this.procesos[i].getNombre().equals(nombre)) {
                return Proceso.class;
            }
        }
        for (int i = 0; i < this.compartimentos.length; i++) {
            if (this.compartimentos[i].getNombre().equals(nombre)) {
                return Compartimento.class;
            }
        }
        if (this.palabrasAtajos.contains(nombre)) {
            return Atajo.class;
        }
        JEP jep = Epidemia.CrearDelphSimJEP();
        FunctionTable functionTable = jep.getFunctionTable();
        if (functionTable.containsKey(elemento)) {
            return PostfixMathCommand.class;
        }
        return null;
    }

    /**
     * Analiza sintácticamente el archivo XML pasado como parámetro y devuelve
     * un objeto tipo árbol.
     * @param archivoOrigen El archivo XML a analizar.
     * @return Un objeto tipo árbol para poder trabajar con la información.
     * @throws org.dom4j.DocumentException Si hay algún problema al leer el archivo.
     */
    private Document analizar(File archivoOrigen) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(archivoOrigen);
        return document;
    }

    /**
     * Abre un archivo XML y, si es correcto y válido, carga su información en
     * esta epidemia.
     * @param XMLSchema Ruta relativa al archivo .xsd contra el que hay que
     *                  validar el fichero que se quiere abrir.
     * @param archivoOrigen El fichero que se quiere abrir.
     * @return Una cadena con los errores detectados. Vacía si no hay ninguno.
     * @throws org.dom4j.DocumentException Si hay algún problema al analizar el archivo.
     * @throws org.xml.sax.SAXException Si hay algún problema al validar el archivo.
     * @throws java.io.IOException Si hay algún problema al leer el archivo.
     */
    public String abrirXML(String XMLSchema,File archivoOrigen) throws DocumentException,SAXException,IOException {
        // Comprobar que el archivo se ajusta al esquema XML de DelphSim
        SchemaValidator validadorEsquemas = new SchemaValidator();
        validadorEsquemas.setXMLSchemaURL(new File(System.getProperty("java.class.path")).getParent() + XMLSchema);
        String errores = validadorEsquemas.validar(archivoOrigen);
        if (errores.equals("")) {
            // Abrir el archivo XML de la ruta especificada y coger el elemento "epidemia" (raíz)
            Document archivoXML = this.analizar(archivoOrigen);
            Element elementoEpidemia = archivoXML.getRootElement();
            if (elementoEpidemia.attributeValue("unidadTiempo") != null) {
                this.unidadTiempo = elementoEpidemia.attributeValue("unidadTiempo");
            }

            // Iterar para recuperar todos los elementos "parámetro" y crear los objetos
            Vector vectorParametros = new Vector();
            this.parametros = new Parametro[elementoEpidemia.elements("parametro").size()];
            int indice = 0;
            for (Iterator i = elementoEpidemia.elementIterator("parametro"); i.hasNext();) {
                Element elementoParametro = (Element) i.next();
                this.parametros[indice] = new Parametro();
                this.parametros[indice].cargarDesdeXML(elementoParametro);
                vectorParametros.add(this.parametros[indice].getNombre());
                this.palabrasReservadas.add(this.parametros[indice].getNombre());
                indice++;
            }

            // Iterar para recuperar todos los elementos "proceso" y crear los objetos
            Vector vectorProcesos = new Vector();
            this.procesos = new Proceso[elementoEpidemia.elements("proceso").size()];
            indice = 0;
            for (Iterator i = elementoEpidemia.elementIterator("proceso"); i.hasNext();) {
                Element elementoProceso = (Element) i.next();
                this.procesos[indice] = new Proceso();
                this.procesos[indice].cargarDesdeXML(elementoProceso);
                vectorProcesos.add(this.procesos[indice].getNombre());
                this.palabrasReservadas.add(this.procesos[indice].getNombre());
                indice++;
            }

            // Comprobar que, dentro de los parámetros, parametrosVinculados y
            // procesosVinculados usen nombres definidos y además que
            // parametrosVinculados estén en el orden apropiado.
            for (int i = 0; i < this.parametros.length; i++) {
                String nombrePar = this.parametros[i].getNombre();
                String[] parVinc = this.parametros[i].getParametrosVinculados();
                String[] procVinc = this.parametros[i].getProcesosVinculados();
                for (int j = 0; j < parVinc.length; j++) {
                    int posicion = vectorParametros.indexOf(parVinc[j]);
                    if (posicion == -1) {
                        errores += String.format(
                                "<Error> El parámetro '%s' se utiliza en la definición del parámetro '%s', " +
                                "pero éste último no se ha definido realmente.\n\n", nombrePar, parVinc[j]);
                    } else if (posicion == i) {
                        errores += String.format("<Error> El parámetro '%s' no puede depender de sí mismo.\n\n", 
                                nombrePar);
                    } else if (posicion < i) {
                        errores += String.format("<Advertencia> El parámetro '%s' depende del parámetro '%s', " +
                                "por lo que debe definirse después de él.\n\n", parVinc[j], nombrePar);
                    }
                }
                for (int j = 0; j < procVinc.length; j++) {
                    if (!vectorProcesos.contains(procVinc[j])) {
                        errores += String.format(
                                "<Error> El parámetro '%s' se utiliza en la definición del proceso '%s', " +
                                "pero éste último no se ha definido realmente.\n\n", nombrePar, procVinc[j]);
                    }
                }
            }
            // Comprobar que, dentro de los procesos, procesosVinculados usen
            // nombres definidos y estén en el orden apropiado.
            for (int i = 0; i < this.procesos.length; i++) {
                String nombreProc = this.procesos[i].getNombre();
                String[] procVinc = this.procesos[i].getProcesosVinculados();
                for (int j = 0; j < procVinc.length; j++) {
                    int posicion = vectorProcesos.indexOf(procVinc[j]);
                    if (posicion == -1) {
                        errores += String.format(
                                "<Error> El proceso '%s' se utiliza en la definición del proceso '%s', " +
                                "pero éste último no se ha definido realmente.\n\n", nombreProc, procVinc[j]);
                    } else if (posicion == i) {
                        errores += String.format("<Error> El proceso '%s' no puede depender de sí mismo.\n\n", 
                                nombreProc);
                    } else if (posicion < i) {
                        errores += String.format("<Advertencia> El proceso '%s' depende del proceso '%s', " +
                                "por lo que debe definirse después de él.\n\n", procVinc[j], nombreProc);
                    }
                }
            }

            // Recuperar el elemento "poblacion" y crear el objeto correspondiente
            Element elementoPoblacion = elementoEpidemia.element("poblacion");
            this.poblacion = new Poblacion();
            this.poblacion.cargarDesdeXML(elementoPoblacion);

            // Iterar para recuperar todos los elementos "compartimento" y crear los objetos
            // Comprobar que el número, nombre y orden de los compartimentos corresponda con las categorías
            this.compartimentos = new Compartimento[elementoEpidemia.elements("compartimento").size()];
            String[] segunDivisiones = this.combinarCategorias(null);
            if (segunDivisiones.length != this.compartimentos.length) {
                errores += "<Error> Los compartimentos definidos en el archivo abierto no se corresponden " +
                        "con la verdadera combinación de las distintas categorías definidas.";
                return errores;
            }
            indice = 0;
            for (Iterator i = elementoEpidemia.elementIterator("compartimento"); i.hasNext();) {
                Element elementoCompartimento = (Element) i.next();
                this.compartimentos[indice] = new Compartimento();
                this.compartimentos[indice].cargarDesdeXML(elementoCompartimento);
                if (!this.compartimentos[indice].getNombre().equals(segunDivisiones[indice])) {
                    errores += "<Error> Los compartimentos definidos en el archivo abierto no se corresponden " +
                            "con la verdadera combinación de las distintas categorías definidas.";
                    return errores;
                }
                this.palabrasReservadas.add(this.compartimentos[indice].getNombre());
                indice++;
            }
            
            // Generar los atajos correspondientes a los compartimentos cargados
            this.generarAtajos();
        }

        // Devolver los errores producidos, cadena vacía si no ha habido
        return errores;
    }

    /**
     * Guarda la información de esta epidemia en el archivo pasado como
     * parámetro, siguiendo el esquema del .xsd de la aplicación.
     * @param archivoDestino El archivo destino donde se guardará el modelo.
     * @throws java.io.IOException Si hay problemas al escribir en disco.
     * @throws org.dom4j.DocumentException Si hay problemas al crear el objeto de tipo árbol.
     * @throws java.lang.Exception Si se produce algún otro problema.
     */
    public void guardarXML(File archivoDestino) throws IOException, DocumentException, Exception {
        // Primero crear el documento dom4j con la información del modelo
        Document documento = DocumentHelper.createDocument();
        // Elemento raíz epidemia
        Element elementoEpidemia = new DefaultElement("epidemia");
        documento.setRootElement(elementoEpidemia);
        elementoEpidemia.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        elementoEpidemia.addAttribute("xsi:noNamespaceSchemaLocation", "DelphSim1.18.xsd");
        elementoEpidemia.addAttribute("unidadTiempo", this.unidadTiempo);

        // Elementos parámetros
        if (this.parametros != null) {
            for (Parametro param : this.parametros) {
                Element elementoParametro = param.volcarAXML();
                elementoEpidemia.add(elementoParametro);
            }
        }

        // Elementos procesos
        if (this.procesos != null) {
            for (Proceso proc : this.procesos) {
                Element elementoProceso = proc.volcarAXML();
                elementoEpidemia.add(elementoProceso);
            }
        }

        // Elemento población
        Element elementoPoblacion = this.poblacion.volcarAXML();
        elementoEpidemia.add(elementoPoblacion);

        // Elementos compartimentos
        for (Compartimento comp : this.compartimentos) {
            Element elementoCompartimento = comp.volcarAXML();
            elementoEpidemia.add(elementoCompartimento);
        }

        // Luego crear el formato, stream y escritor de la salida
        OutputFormat formato = OutputFormat.createPrettyPrint();
        formato.setEncoding("UTF-16");
        formato.setIndent("\t");
        formato.setNewLineAfterDeclaration(false);
        formato.setPadText(false);
        formato.setTrimText(true);
        formato.setXHTML(true);
        java.io.OutputStreamWriter salida = new java.io.OutputStreamWriter(
                new java.io.FileOutputStream(archivoDestino), "UTF-16");
        XMLWriter escritor = new XMLWriter(salida, formato);
        // Y escribir
        escritor.write(documento);
        escritor.close();
    }

    /**
     * Construye el modelo del JList de la interfaz en base a los compartimentos
     * de esta epidemia.
     * @return El modelo de la lista de los compartimentos.
     */
    public DefaultListModel construirListaCompartimentos() {
        DefaultListModel listaCompartimentos = new DefaultListModel();
        for (Compartimento comp : this.getCompartimentos()) {
            listaCompartimentos.addElement(comp.getNombre());
        }
        return listaCompartimentos;
    }

    /**
     * Construye el modelo del JList de la interfaz en base a los parámetros
     * de esta epidemia.
     * @return El modelo de la lista de los parámetros.
     */
    public DefaultListModel construirListaParametros() {
        DefaultListModel listaParametros = new DefaultListModel();
        if (this.getParametros() != null) {
            for (Parametro par : this.getParametros()) {
                listaParametros.addElement(par.getNombre());
            }
        }
        return listaParametros;
    }

    /**
     * Construye el modelo del JList de la interfaz en base a los procesos
     * de esta epidemia.
     * @return El modelo de la lista de los procesos.
     */
    public DefaultListModel construirListaProcesos() {
        DefaultListModel listaProcesos = new DefaultListModel();
        if (this.getProcesos() != null) {
            for (Proceso proc : this.getProcesos()) {
                listaProcesos.addElement(proc.getNombre());
            }
        }
        return listaProcesos;
    }

    /**
     * Construye el modelo del JList de la interfaz en base a los resultados
     * de esta epidemia.
     * @return El modelo de la lista de los resultados.
     */
    public DefaultListModel construirListaResultados() {
        DefaultListModel listaResultados = new DefaultListModel();
        if (this.getResultados() != null) {
            for (Resultado res : this.getResultados()) {
                listaResultados.addElement(res.getTitulo());
            }
        }
        return listaResultados;
    }

    /**
     * Método que genera todas las combinaciones de categorías, una por cada
     * división, obteniendo todos los compartimentos posibles.
     * @param divisiones Parámetro opcional con las divisiones a combinar. Si
     *                   fuera <i>null</i>, se combinarían las categorías de la
     *                   epidemia que invoca este método.
     * @return Un vector de cadenas de texto, cada elemento representa uno de
     *         los compartimentos generados por combinación.
     */
    public String[] combinarCategorias(Division[] divisiones) {
        // Si divs es null, se usan las de la epidemia
        if (divisiones == null) {
            divisiones = this.getPoblacion().getDivisiones();
        }
        // Cálculo del número de secciones a generar por combinación
        int numeroClasificaciones = 1;
        for (Division division : divisiones) {
            numeroClasificaciones *= division.getCategorias().length;
        }
        String clasificaciones[] = new String[numeroClasificaciones];
        // Combinar una categoría de cada división
        int repetirNVeces = numeroClasificaciones;
        int repetirMVeces = 1;
        Categoria[] categorias;
        for (int i = 0; i < divisiones.length; i++) {
            categorias = divisiones[i].getCategorias();
            repetirNVeces = repetirNVeces / categorias.length;
            for (int j = 0; j < categorias.length; j++) {
                for (int l = 0; l < repetirMVeces; l++) {
                    for (int k = 0; k < repetirNVeces; k++) {
                        if (clasificaciones[l * (numeroClasificaciones / repetirMVeces) + repetirNVeces * j + k] == 
                                null) {
                            clasificaciones[l * (numeroClasificaciones / repetirMVeces) + repetirNVeces * j + k] = 
                                    categorias[j].getNombre();
                        } else {
                            clasificaciones[l * (numeroClasificaciones / repetirMVeces) + repetirNVeces * j + k] += 
                                    "_" + categorias[j].getNombre();
                        }
                    }
                }
            }
            repetirMVeces = repetirMVeces * categorias.length;
        }
        return clasificaciones;
    }

    /**
     * Patrón 'factory' aplicado a la creación de analizadores sintácticos
     * matemáticos (JEP). Este método crea una nueva instancia del objeto JEP,
     * iniciada con las funciones que estarán disponibles en la aplicación.
     * Todas las instancias de JEP deben crearse con este método.
     */
    public static JEP CrearDelphSimJEP() {
        // Creamos un JEP vacío
        JEP jep = new JEP();
        
        // Le añadimos las funciones predefinidas en la librería solicitadas
        jep.addFunction("sin", new Sine());
        jep.addFunction("cos", new Cosine());
        jep.addFunction("tan", new Tangent());
        jep.addFunction("asin", new ArcSine());
        jep.addFunction("acos", new ArcCosine());
        jep.addFunction("atan", new ArcTangent());
        jep.addFunction("atan2", new ArcTangent2());
        jep.addFunction("sinh", new SineH());
        jep.addFunction("cosh", new CosineH());
        jep.addFunction("tanh", new TanH());
        jep.addFunction("asinh", new ArcSineH());
        jep.addFunction("acosh", new ArcCosineH());
        jep.addFunction("atanh", new ArcTanH());
        jep.addFunction("log", new Logarithm());
        jep.addFunction("ln", new NaturalLogarithm());
        jep.addFunction("exp", new Exp());
        jep.addFunction("pow", new Power());
        jep.addFunction("sqrt",new SquareRoot());
        jep.addFunction("abs", new Abs());
        jep.addFunction("mod", new Modulus());
        jep.addFunction("sum", new Sum());
        jep.addFunction("rand", new org.nfunk.jep.function.Random());
        jep.addFunction("round",new Round());
        jep.addFunction("floor",new Floor());
        jep.addFunction("ceil",new Ceil());
        
        // Distribuciones de probabilidad discretas
        jep.addFunction("Binomial", new JEPBinomial());
        jep.addFunction("BinomialNegativa", new JEPBinomialNegativa());
        jep.addFunction("Hipergeometrica", new JEPHipergeometrica());
        jep.addFunction("Poisson", new JEPPoisson());
        
        // Distribuciones de probabilidad continuas
        jep.addFunction("Beta", new JEPBeta());
        jep.addFunction("JiCuadrado", new JEPJiCuadrado());
        jep.addFunction("Exponencial", new JEPExponencial());
        jep.addFunction("Gamma", new JEPGamma());
        jep.addFunction("Normal", new JEPNormal());
        jep.addFunction("TStudent", new JEPTStudent());
        jep.addFunction("Uniforme", new JEPUniforme());
        
        return jep;
    }

    /**
     * Método recursivo que calcula los nombres identificativos de los atajos
     * en base a los compartimentos de esta epidemia. La recursividad comienza
     * con la cadena vacía, división cero.
     * @param cadena El nombre del atajo calculado hasta el momento.
     * @param divisionMeLlego División por la que me llego añadiendo categorías
     *                        al atajo.
     */
    private void calcularAtajos(String cadena, int divisionMeLlego) {
        // Desde la división por la que llegue hasta la última
        String nuevaCadena = "";
        for (int i = divisionMeLlego; i < this.getPoblacion().getDivisiones().length; i++) {
            // Para cada categoría de esa división
            for (int j = 0; j < this.getPoblacion().getDivisiones()[i].getCategorias().length; j++) {
                // Combino su nombre con la cadena pasada y se añade como atajo
                nuevaCadena = cadena + this.getPoblacion().getDivisiones()[i].getCategorias()[j].getNombre();
                if (nuevaCadena.split("_").length < this.getPoblacion().getDivisiones().length) {
                    if (!this.palabrasAtajos.contains(nuevaCadena)) {
                        this.palabrasAtajos.add(nuevaCadena);
                    }
                }
                // Como no es la última, separamos de la siguiente con un guión
                nuevaCadena += "_";
                // Y llamamos recursivamente a esta función para cada división siguiente
                for (int k = i+1; k < this.getPoblacion().getDivisiones().length; k++) {
                    this.calcularAtajos(nuevaCadena, k);
                }
            }
        }
    }

    /**
     * Genera los objetos Atajo usando los nombres calculados previamente.
     * @see #calcularAtajos(java.lang.String, int) 
     */
    public void generarAtajos() {
        // Vaciamos el vector, lo recalculamos y creamos los objetos Atajo
        this.palabrasAtajos.clear();
        this.calcularAtajos("", 0);
        this.atajos = new Atajo[this.palabrasAtajos.size()];
        // Para cada uno de ellos, creamos un Atajo con su nombre y pasándole
        // los compartimentos para que sepa calcular su definición
        for (int i = 0; i < this.palabrasAtajos.size(); i++) {
            this.atajos[i] = new Atajo(this.palabrasAtajos.get(i).toString(), this.getCompartimentos());
        }
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Epidemia clone() {
        int numPars = this.getParametros().length;
        int numProcs = this.getProcesos().length;
        int numComps = this.getCompartimentos().length;
        int numAtaj = this.getAtajos().length;
        int numResu = this.getResultados().length;
        Epidemia clon = new Epidemia();
        clon.setUnidadTiempo(this.getUnidadTiempo());
        clon.setTiempoSimulacion(this.getTiempoSimulacion());
        clon.setPoblacion(this.getPoblacion().clone());
        clon.setParametros(new Parametro[numPars]);
        for (int i = 0; i < numPars; i++) {
            clon.setParametro(this.getParametro(i).clone(), i);
        }
        clon.setProcesos(new Proceso[numProcs]);
        for (int i = 0; i < numProcs; i++) {
            clon.setProceso(this.getProceso(i).clone(), i);
        }
        clon.setCompartimentos(new Compartimento[numComps]);
        for (int i = 0; i < numComps; i++) {
            clon.setCompartimento(this.getCompartimento(i).clone(), i);
        }
        clon.setAtajos(new Atajo[numAtaj]);
        for (int i = 0; i < numAtaj; i++) {
            clon.setAtajo(this.getAtajo(i).clone(), i);
        }
        clon.setResultados(new Resultado[numResu]);
        for (int i = 0; i < numResu; i++) {
            clon.setResultado(this.getResultado(i).clone(), i);
        }
        clon.setPalabrasReservadas((HashSet) this.getPalabrasReservadas().clone());
        clon.setPalabrasAtajos((Vector) this.getPalabrasAtajos().clone());
        return clon;
    }
}
