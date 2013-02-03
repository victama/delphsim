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
package delphsim.simulation;

import delphsim.model.Epidemia;
import delphsim.model.Resultado;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

/**
 * Esta clase implementa el método de simulación continua más simple de la
 * aplicación. Se trata del método de Euler explícito para la resolución de
 * Ecuaciones Diferenciales Ordinarias (EDOs).
 * @author Víctor E. Tamames Gómez
 */
public class EulerExplicito extends SimulationTask {
    
    /**
     * Constructor de la tarea que toma referencia de los parámetros en sus
     * propios atributos para poder trabajar con ellos. El primer parámetro es
     * necesario para tener referencia a la aplicación que ha invocado la tarea,
     * el segundo contiene la epidemia que tenemos que simular, y los cuatro
     * últimos son elementos del GUI que tenemos que actualizar cuando la tarea
     * termine.
     * @param app   La aplicación que invoca esta tarea.
     * @param epi   La epidemia a simular.
     * @param aMap  El mapa de acciones para poder desactivar las prohibidas.
     * @param jtabbed   Panel de pestañas donde añadir los resultados.
     * @param jmenu  Submenú donde añadir los elementos de menú de resultados.
     * @param bgroup   Grupo de botones para añadirle los elementos de menú.
     */
    public EulerExplicito(org.jdesktop.application.Application app,
                               Epidemia epi, javax.swing.ActionMap aMap,
                               javax.swing.JTabbedPane jtabbed,
                               javax.swing.JMenu jmenu,
                               javax.swing.ButtonGroup bgroup) {
        // Llamamos al constructor de su ancestro
        super(app, epi, aMap, jtabbed, jmenu, bgroup);
    }
    
    /**
     * Este método implementa propiamente dicho el método de Euler explícito.
     * Crea los archivos temporales de salida, comprueba que todos los elementos
     * del modelo se hayan definido, realiza la simulación actualizando los
     * tiempos y la barra de progreso y recoge y muestra los errores producidos.
     * El código de la tarea. Este método corre en un hilo en background,
     * así que no hay que referenciar al GUI de Swing.
     * @return En realidad no devuelve nada, ya que los resultados se almacenan
     *         en el modelo de DelphSim.
     * @throws java.io.IOException Si hay algún problema escribiendo en disco
     *                             los valores que se van calculando.
     */
    @Override
    protected Object doInBackground() throws IOException {
        this.setMessage("Comprobando que todos los elementos hayan sido definidos...");
        
        /*******************************************************
         * CREAR UN FICHERO TEMPORAL PARA CADA FUNCION DE CADA *
         * RESULTADO Y OTRO PARA EL TIEMPO                     *
         *******************************************************/
        File rutaTemp = new File(new File(System.getProperty("java.class.path")).getParent() + File.separator + "temp");
        if (!rutaTemp.exists()) {
            if (!rutaTemp.mkdir()) {
                this.setMessage("No se pueden guardar los archivos temporales.");
            }
        }
        String archivoTiempo = rutaTemp.getAbsolutePath() + File.separator + "tiempo_.temp";
        PrintWriter tiempo_ = new PrintWriter(new BufferedWriter(new FileWriter(archivoTiempo)));
        PrintWriter[][] resFiles = new PrintWriter[this.epidemia.getResultados().length][];
        for (int i = 0; i < this.epidemia.getResultados().length; i++) {
            this.epidemia.getResultado(i).setFileTiempo(archivoTiempo);
            resFiles[i] = new PrintWriter[this.epidemia.getResultado(i).getNumFunciones()];
            for (int j = 0; j < this.epidemia.getResultado(i).getNumFunciones(); j++) {
                String f = rutaTemp.getAbsolutePath() + File.separator + "r" + i + "f" + j + ".temp";
                resFiles[i][j] = new PrintWriter(new BufferedWriter(new FileWriter(f)));
                this.epidemia.getResultado(i).setFileFuncion(f, j);
            }
        }
        
        /*************************************
         * CÓDIGO DE DEFINICIÓN DE VARIABLES *
         *************************************/
        // Nuevo Java Expression Parser
        JEP jep = Epidemia.CrearDelphSimJEP();
        double temp;
        int indice = 0;
        
        /**********************************************************************
         * Definimos tantas variables locales como funciones analizadas vamos *
         * a tener que guardar para no tener que volver a analizarlas.        *
         **********************************************************************/
        // Para cada parámetro, cada compartimento, cada atajo y cada función
        // de cada resultado. Para cada proceso no, porque pueden ir por tramos.
        Node[] funcPars = new Node[this.epidemia.getParametros().length];
        Node[] funcComps = new Node[this.epidemia.getCompartimentos().length];
        Node[] funcAtajos = new Node[this.epidemia.getAtajos().length];
        Node[][] funcRes = new Node[this.epidemia.getResultados().length][];
        for (int i = 0; i < this.epidemia.getResultados().length; i++) {
            funcRes[i] = new Node[this.epidemia.getResultado(i).getNumFunciones()];
        }
        
        // 1º - Parámetros, ya que sólo dependen de parámetros anteriores
        try {
            for (int i = 0; i < this.epidemia.getParametros().length; i++) {
                indice = i;
                funcPars[i] = jep.parseExpression(this.epidemia.getParametro(i).getDefinicionContinua());
                temp = (Double) jep.evaluate(funcPars[i]);
                jep.addVariable(this.epidemia.getParametro(i).getNombre(), temp);
            }
        } catch (ParseException pex) {
            this.failed(new Exception(String.format("<html>Definición incorrecta del parámetro <b>%s</b></html>", 
                    this.epidemia.getParametro(indice).getNombre()), pex.getCause()));
            return null;
        } catch (NullPointerException npex) {
            this.failed(new Exception(String.format("<html>Falta por definir el parámetro <b>%s</b></html>", 
                    this.epidemia.getParametro(indice).getNombre()), npex.getCause()));
            return null;
        }
        // 2º - Condiciones Iniciales de los Compartimentos -> Números
        try {
            for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                indice = i;
                temp = (Double) jep.evaluate(jep.parseExpression(
                        String.valueOf(this.epidemia.getCompartimento(i).getHabitantes())
                        ));
                jep.addVariable(this.epidemia.getCompartimento(i).getNombre(), temp);
            }
        } catch (ParseException pex) {
            this.failed(new Exception(String.format(
                    "<html>Número de personas inicial incorrecto del compartimento <b>%s</b></html>", 
                    this.epidemia.getCompartimento(indice).getNombre()), pex.getCause()));
            return null;
        } catch (NullPointerException npex) {
            this.failed(new Exception(String.format(
                    "<html>Falta por definir el número de personas inicial del compartimento <b>%s</b></html>", 
                    this.epidemia.getCompartimento(indice).getNombre()), npex.getCause()));
            return null;
        }
        // 3º - Atajos, que sólo dependen de los compartimentos (2)
        try {
            for (int i = 0; i < this.epidemia.getAtajos().length; i++) {
                indice = i;
                funcAtajos[i] = jep.parseExpression(this.epidemia.getAtajo(i).getDefinicionContinua());
                temp = (Double) jep.evaluate(funcAtajos[i]);
                jep.addVariable(this.epidemia.getAtajo(i).getNombre(), temp);
            }
        } catch (ParseException pex) {
            this.failed(new Exception(String.format("<html>Definición incorrecta del atajo <b>%s</b></html>", 
                    this.epidemia.getAtajo(indice).getNombre()), pex.getCause()));
            return null;
        } catch (NullPointerException npex) {
            this.failed(new Exception(String.format("<html>Falta por definir el atajo <b>%s</b></html>", 
                    this.epidemia.getAtajo(indice).getNombre()), npex.getCause()));
            return null;
        }
        // 4º - Procesos, dependen de los parámetros (1), compartimentos (2), atajos (3)
        // y procesos anteriores. Nota: se coge el primer tramo (primero bien <-> todos bien)
        try {
            for (int i = 0; i < this.epidemia.getProcesos().length; i++) {
                indice = i;
                temp = (Double) jep.evaluate(jep.parseExpression(
                        this.epidemia.getProceso(i).getDefinicionContinua(0.0)
                        ));
                jep.addVariable(this.epidemia.getProceso(i).getNombre(), temp);
            }
        } catch (ParseException pex) {
            this.failed(new Exception(String.format("<html>Definición incorrecta del proceso <b>%s</b></html>", 
                    this.epidemia.getProceso(indice).getNombre()), pex.getCause()));
            return null;
        } catch (Exception ex) {
            // Un poco decisión salomónica coger TODAS las demás excepciones, pero así va bien
            this.failed(new Exception(String.format("<html>Falta por definir el proceso <b>%s</b></html>", 
                    this.epidemia.getProceso(indice).getNombre()), ex.getCause()));
            return null;
        }
        // 5º - Compartimentos, su definición. Ahora no es necesaria, pero hay
        // que comprobarla y de paso también queda analizada.
        // Dependen de parámetros (1), compartimentos (2), atajos (3) y procesos (4).
        try {
            for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                indice = i;
                funcComps[i] = jep.parseExpression(this.epidemia.getCompartimento(i).getDefinicionContinua());
                temp = (Double) jep.evaluate(funcComps[i]);
                jep.addVariable(this.epidemia.getCompartimento(i).getNombre() + "_", temp);
            }
            // Como sólo es para comprobar, luego hay que eliminarlas
            for (int i = 0; i < this.epidemia.getCompartimentos().length; i++) {
                jep.removeVariable(this.epidemia.getCompartimento(i).getNombre() + "_");
            }
        } catch (ParseException pex) {
            this.failed(new Exception(String.format(
                    "<html>Definición incorrecta del compartimento <b>%s</b></html>", 
                    this.epidemia.getCompartimento(indice).getNombre()), pex.getCause()));
            return null;
        } catch (NullPointerException npex) {
            this.failed(new Exception(String.format(
                    "<html>Falta por definir el compartimento <b>%s</b></html>", 
                    this.epidemia.getCompartimento(indice).getNombre()), npex.getCause()));
            return null;
        }
        // 6º - Escribir ya el primer elemento de los resultados
        try {
            for (int m = 0; m < resFiles.length; m++) {
                indice = m;
                for (int n = 0; n < resFiles[m].length; n++) {
                    funcRes[m][n] = jep.parseExpression(this.epidemia.getResultado(m).getFuncion(n)[1].toString());
                    temp = (Double) jep.evaluate(funcRes[m][n]);
                    resFiles[m][n].println(temp);
                    this.epidemia.getResultado(m).anadirPuntoFuncion(temp, n);
                }
                this.epidemia.getResultado(m).anadirPuntoTiempo(0.0d);
            }
        } catch (ParseException pex) {
            // Nota: esto no debería pasar nunca
            this.failed(new Exception(String.format(
                    "<html>Ha fallado el cálculo de uno de los resultados</html>", 
                    this.epidemia.getResultado(indice).getTitulo()), pex.getCause()));
            return null;
        } catch (NullPointerException npex) {
            // Nota: esto no debería pasar nunca
            this.failed(new Exception(String.format(
                    "<html>Falta por definir uno de los resultados</html>", 
                    this.epidemia.getResultado(indice).getTitulo()), npex.getCause()));
            return null;
        }

        /************************************************************************
         * OTROS ELEMENTOS NECESARIOS como tmax, h, numIteraciones, renovarCada *
         ************************************************************************/
        // Nota, máximos soportados: segundos -> 8 meses, minutos -> 40 años, días -> 2400 años
        double tmax_ = this.epidemia.getTiempoSimulacion();
        long numIteraciones_ = Math.round(tmax_ / h_);
        for (int i = 0; i < this.epidemia.getResultados().length; i++) {
            this.epidemia.getResultado(i).setNumPuntosTotal(numIteraciones_+1);
        }
        long guardarPuntoGraficaCada_ = Math.round(numIteraciones_ / Resultado.NUM_PUNTOS_GRAFICAS);
        long contadorGuardarPuntosGraficas_ = 1;
        long renovarCada_ = Math.round(1 / h_);
        tiempo_.println(0.0d);
        
        this.setMessage("Iniciando simulación...");
        this.tiempoComienzo = System.currentTimeMillis();
        
        /******************************
         * CÓDIGO DEL MÉTODO DE EULER *
         ******************************/
        for (int i = 0; i < numIteraciones_; i++) {
            // Parar la simulación si se ha cancelado
            if (this.isCancelled()) {
                break;
            }
            try {
                /** 
                 * CUERPO del método de Euler explícito:
                 * X(t+h) = X(t) + h*derX;
                 */
                // 1º Actualizar variables de los compartimentos por el método de Euler
                double[] derComps = new double[this.epidemia.getCompartimentos().length];
                for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                    derComps[n] = (Double) jep.evaluate(funcComps[n]);
                }
                for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                    temp = (Double) jep.getVarValue(this.epidemia.getCompartimento(n).getNombre()) + h_*derComps[n];
                    jep.addVariable(this.epidemia.getCompartimento(n).getNombre(), temp);
                }
                // 2º Actualizar variables de los atajos
                for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                    temp = (Double) jep.evaluate(funcAtajos[n]);
                    jep.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                }
                // 3º Actualizar variables de los procesos
                for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                    temp = (Double) jep.evaluate(jep.parseExpression(
                            this.epidemia.getProceso(n).getDefinicionContinua((i+1)*h_)
                            ));
                    jep.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                }
                // 4º Calcular los nuevos valores de los resultados y escribir
                for (int m = 0; m < resFiles.length; m++) {
                    for (int n = 0; n < resFiles[m].length; n++) {
                        temp = (Double) jep.evaluate(funcRes[m][n]);
                        resFiles[m][n].println(temp);
                        if (contadorGuardarPuntosGraficas_ == guardarPuntoGraficaCada_ || guardarPuntoGraficaCada_ == 0) {
                            this.epidemia.getResultado(m).anadirPuntoFuncion(temp, n);
                        }
                    }
                    if (contadorGuardarPuntosGraficas_ == guardarPuntoGraficaCada_ || guardarPuntoGraficaCada_ == 0) {
                        this.epidemia.getResultado(m).anadirPuntoTiempo((i+1)*h_);
                    }
                }
                // 5º Actualizar los parámetros, ya que pueden ser aleatorios:
                // funciones random, distribuciones probabilísticas, etc.
                for (int n = 0; n < this.epidemia.getParametros().length; n++) {
                    temp = (Double) jep.evaluate(funcPars[n]);
                    jep.addVariable(this.epidemia.getParametro(n).getNombre(), temp);
                }
            } catch (ParseException pex) {
                System.err.println("Error dentro del bucle en tiempo = " + (i+1)*h_);
                pex.printStackTrace();
                return null;
            }
            
            // Actualizar el archivo del tiempo
            tiempo_.println((i+1)*h_);
            
            // Actualizar barra de estado si toca
            // (no se hace siempre para no tardar más en actualizar que en simular)
            if (i % renovarCada_ == 0) {
                this.setProgress(i, 0, numIteraciones_);
                this.setMessage("Tiempo transcurrido: " + this.elapsedTime() +
                        "         Tiempo restante estimado: " + this.estimatedTime(i, numIteraciones_, 1));
            }
            if (contadorGuardarPuntosGraficas_ == guardarPuntoGraficaCada_ || guardarPuntoGraficaCada_ == 0) {
                contadorGuardarPuntosGraficas_ = 0;
            }
            contadorGuardarPuntosGraficas_++;
        }
        
        // Cerramos todos los ficheros
        for (int m = 0; m < resFiles.length; m++) {
            for (int n = 0; n < resFiles[m].length; n++) {
                resFiles[m][n].flush();
                resFiles[m][n].close();
            }
        }
        tiempo_.flush();
        tiempo_.close();
        
        // Los metadatos de los resultados los tiene la epidemia,
        // los datos están en los archivos
        return "";  
    }
}
