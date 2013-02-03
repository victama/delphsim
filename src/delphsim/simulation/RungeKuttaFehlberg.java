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
 * Esta clase implementa el método de simulación continua de Runge-Kutta-Fehlberg.
 * Es un método de paso variable que utiliza el método de Runge-Kutta de 4º orden
 * para estimar el siguiente valor, y el método de Runge-Kutta de 5º orden para
 * estimar el error cometido. El paso de integración se actualiza en función de
 * éste. Ante un error muy grande, la iteración se repite con el nuevo paso; en
 * caso contrario el nuevo paso servirá para la iteración siguiente.
 * @author Víctor E. Tamames Gómez
 */
public class RungeKuttaFehlberg extends SimulationTask {
    
    /**
     * Coeficientes que ponderan el peso de los distintos puntos intermedios
     * calculados en cada aproximación.
     */
    public static double[][] A = {{  1.0d/4.0d                                                                         },
                                  {  3.0d/32.0d,       9.0d/32.0d                                                      },
                                  {  1932.0d/2197.0d, -7200.0d/2197.0d,  7296.0d/2197.0d                               },
                                  {  439.0d/216.0d,   -8.0d,             3680.0d/513.0d,  -845.0d/4104                 },
                                  { -8.0d/27.0d,       2.0d,            -3544.0d/2565.0d,  1859.0d/4104, -11.0d/40.0d  }};
    
    /**
     * Coeficientes de los pesos de cada término para la solución de orden 4.
     */
    public static double[] B4 = { 25.0d/216.0d, 0.0d, 1408.0d/2565.0d,  2197.0d/4104.0d,   -1.0d/5.0d,  0.0d };
    
    /**
     * Coeficientes de los pesos de cada término para la solución de orden 5.
     */
    public static double[] B5 = { 16.0d/135.0d, 0.0d, 6656.0d/12825.0d, 28561.0d/56430.0d, -9.0d/50.0d, 2.0d/55.0d };
    
    /**
     * Coeficientes del punto en el tiempo en el que se calcula cada aproximación.
     */
    public static double[] C = {0, 1.0d/4.0d, 3.0d/8.0d, 12.0d/13.0d, 1.0d, 1.0d/2.0d};
    
    /**
     * Tolerancia permitida para el error global.
     */
    public static double TOLERANCIA_ERROR_GLOBAL = 0.0001d;
    
    /**
     * Cota mínima permitida para el paso de integración.
     * Prefijada con valor 0.001
     */
    public static double H_MIN = 0.001d;
    
    /**
     * Cota máxima permitida para el paso de integración.
     * Prefijada con valor 0.5
     */
    public static double H_MAX = 0.5d;
    
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
    public RungeKuttaFehlberg(org.jdesktop.application.Application app,
                               Epidemia epi, javax.swing.ActionMap aMap,
                               javax.swing.JTabbedPane jtabbed,
                               javax.swing.JMenu jmenu,
                               javax.swing.ButtonGroup bgroup) {
        // Llamamos al constructor de su ancestro
        super(app, epi, aMap, jtabbed, jmenu, bgroup);
    }
    
    /**
     * Este método implementa propiamente dicho el método Runge-Kutta-Fehlberg.
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
        // Y otros dos: uno para RK4 y otro para RK5
        JEP jepRKF45 = Epidemia.CrearDelphSimJEP();
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
                jepRKF45.addVariable(this.epidemia.getParametro(i).getNombre(), temp);
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
                jepRKF45.addVariable(this.epidemia.getCompartimento(i).getNombre(), temp);
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
                jepRKF45.addVariable(this.epidemia.getAtajo(i).getNombre(), temp);
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
                jepRKF45.addVariable(this.epidemia.getProceso(i).getNombre(), temp);
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
        double t_ = 0.0d;
        double q_ = 1.0d;
        long numIteraciones_ = Math.round(tmax_ / h_);
        long numPtosCalculados_ = 1;
        
        long guardarPtoGrafCada_ = Math.round(numIteraciones_ / Resultado.NUM_PUNTOS_GRAFICAS);
        long contadorGuardarPtosGrafs_ = 1;
        long renovarCada_ = Math.round(1 / h_);
        tiempo_.println(t_);
        
        this.setMessage("Iniciando simulación...");
        this.tiempoComienzo = System.currentTimeMillis();
        
        /*********************************************
         * CÓDIGO DEL MÉTODO DE RUNGE-KUTTA-FEHLBERG *
         *********************************************/
        while (t_ < tmax_) {
            // Parar la simulación si se ha cancelado
            if (this.isCancelled()) {
                break;
            }
            try {
                // 1º Actualizar variables de los compartimentos siguiendo
                // el método de Runge-Kutta-Fehlberg.
                // Necesitamos estas variables fuera del bucle para poder usarlas
                // cuando obtengamos una aproximación aceptable
                double[] solRK4 = new double[this.epidemia.getCompartimentos().length];
                do {
                    // Actualizamos el paso de integración con el q calculado
                    h_ = q_*h_;
                    if (h_ < H_MIN) {
                        h_ = H_MIN;
                    } else if (h_ > H_MAX) {
                        h_ = H_MAX;
                    }
                    /** 
                     * CUERPO del método:
                     * X0 = x(t);
                     * derX0 = f(X0, t+C[0]*h);
                     * X1 = X0 + A[0][0]*h*derX0;
                     * derX1 = f(X1, t+C[1]*h);
                     * X2 = X0 + A[1][0]*h*derX0 + A[1][1]*h*derX1;
                     * derX2 = f(X2, t+C[2]*h);
                     * X3 = X0 + A[2][0]*h*derX0 + A[2][1]*h*derX1 + A[2][2]*h*derX2;
                     * derX3 = f(X3, t+C[3]*h);
                     * X4 = X0 + A[3][0]*h*derX0 + A[3][1]*h*derX1 + A[3][2]*h*derX2 + A[3][3]*h*derX3;
                     * derX4 = f(X4, t+C[4]*h);
                     * X5 = X0 + A[4][0]*h*derX0 + A[4][1]*h*derX1 + A[4][2]*h*derX2 + A[4][3]*h*derX3 + A[4][4]*h*derX4;
                     * derX5 = f(X5, t+C[5]*h);
                     * 
                     * x(t+h) = X0 + h*(B4[0]*derX0 + B4[1]*derX1 + B4[2]*derX2 + B4[3]*derX3 + B4[4]*derX4 + B4[5]*derX5);
                     * z(t+h) = X0 + h*(B5[0]*derX0 + B5[1]*derX1 + B5[2]*derX2 + B5[3]*derX3 + B5[4]*derX4 + B5[5]*derX5);
                     * 
                     * Se calcula:
                     * q = ( (E * h) / (2 * |x(t+h) - z(t+h)|) )^(1/4)
                     * Si (q < 1)
                     *      repetir los pasos con h = q*h
                     * Si no
                     *      se guarda este valor y el siguiente se calculará con h = q*h
                     */
                    double[] COMPS0 = new double[this.epidemia.getCompartimentos().length];
                    double[] COMPS1 = new double[this.epidemia.getCompartimentos().length];
                    double[] COMPS2 = new double[this.epidemia.getCompartimentos().length];
                    double[] COMPS3 = new double[this.epidemia.getCompartimentos().length];
                    double[] COMPS4 = new double[this.epidemia.getCompartimentos().length];
                    double[] COMPS5 = new double[this.epidemia.getCompartimentos().length];
                    double[] derCOMPS0 = new double[this.epidemia.getCompartimentos().length];
                    double[] derCOMPS1 = new double[this.epidemia.getCompartimentos().length];
                    double[] derCOMPS2 = new double[this.epidemia.getCompartimentos().length];
                    double[] derCOMPS3 = new double[this.epidemia.getCompartimentos().length];
                    double[] derCOMPS4 = new double[this.epidemia.getCompartimentos().length];
                    double[] derCOMPS5 = new double[this.epidemia.getCompartimentos().length];
                    double[] solRK5 = new double[this.epidemia.getCompartimentos().length];
                    // a. X0 = x(t); derX0 = f(X0, Y0, Z0, ...); X1 = X0 + A[0][0]*h*derX0;
                    for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                        COMPS0[n] = (Double) jep.getVarValue(this.epidemia.getCompartimento(n).getNombre());
                        derCOMPS0[n] = (Double) jep.evaluate(funcComps[n]);
                        COMPS1[n] = COMPS0[n] + h_*A[0][0]*derCOMPS0[n];
                        jepRKF45.addVariable(this.epidemia.getCompartimento(n).getNombre(), COMPS1[n]);
                    }
                    // Además, actualizar atajos y procesos a tiempo [t + C[1]*h]
                    for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(funcAtajos[n]);
                        jepRKF45.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                    }
                    for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(jepRKF45.parseExpression(
                                this.epidemia.getProceso(n).getDefinicionContinua(t_ + C[1]*h_)
                                ));
                        jepRKF45.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                    }
                    // b. derX1 = f(X1, t+C[1]*h); X2 = X0 + A[1][0]*h*derX0 + A[1][1]*h*derX1;
                    for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                        derCOMPS1[n] = (Double) jepRKF45.evaluate(funcComps[n]);
                        COMPS2[n] = COMPS0[n] + h_*(A[1][0]*derCOMPS0[n] + A[1][1]*derCOMPS1[n]);
                        jepRKF45.addVariable(this.epidemia.getCompartimento(n).getNombre(), COMPS2[n]);
                    }
                    // Además, actualizar atajos y procesos a tiempo [t + C[2]*h]
                    for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(funcAtajos[n]);
                        jepRKF45.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                    }
                    for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(jepRKF45.parseExpression(
                                this.epidemia.getProceso(n).getDefinicionContinua(t_ + C[2]*h_)
                                ));
                        jepRKF45.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                    }
                    // c. derX2 = f(X2, t+C[2]*h); X3 = X0 + A[2][0]*h*derX0 + A[2][1]*h*derX1 + A[2][2]*h*derX2;
                    for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                        derCOMPS2[n] = (Double) jepRKF45.evaluate(funcComps[n]);
                        COMPS3[n] = COMPS0[n] + h_*(A[2][0]*derCOMPS0[n] + A[2][1]*derCOMPS1[n] + A[2][2]*derCOMPS2[n]);
                        jepRKF45.addVariable(this.epidemia.getCompartimento(n).getNombre(), COMPS3[n]);
                    }
                    // Además, actualizar atajos y procesos a tiempo [t + C[3]*h]
                    for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(funcAtajos[n]);
                        jepRKF45.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                    }
                    for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(jepRKF45.parseExpression(
                                this.epidemia.getProceso(n).getDefinicionContinua(t_ + C[3]*h_)
                                ));
                        jepRKF45.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                    }
                    // d. derX3 = f(X3, t+C[3]*h);
                    //    X4 = X0 + A[3][0]*h*derX0 + A[3][1]*h*derX1 + A[3][2]*h*derX2 + A[3][3]*h*derX3;
                    for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                        derCOMPS3[n] = (Double) jepRKF45.evaluate(funcComps[n]);
                        COMPS4[n] = COMPS0[n] + h_*(A[3][0]*derCOMPS0[n] + A[3][1]*derCOMPS1[n] + A[3][2]*derCOMPS2[n] + A[3][3]*derCOMPS3[n]);
                        jepRKF45.addVariable(this.epidemia.getCompartimento(n).getNombre(), COMPS4[n]);
                    }
                    // Además, actualizar atajos y procesos a tiempo [t + C[4]*h]
                    for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(funcAtajos[n]);
                        jepRKF45.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                    }
                    for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(jepRKF45.parseExpression(
                                this.epidemia.getProceso(n).getDefinicionContinua(t_ + C[4]*h_)
                                ));
                        jepRKF45.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                    }
                    // e. derX4 = f(X4, t+C[4]*h);
                    //    X5 = X0 + A[4][0]*h*derX0 + A[4][1]*h*derX1 + A[4][2]*h*derX2 + A[4][3]*h*derX3 + A[4][4]*h*derX4;
                    for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                        derCOMPS4[n] = (Double) jepRKF45.evaluate(funcComps[n]);
                        COMPS5[n] = COMPS0[n] + h_*(A[4][0]*derCOMPS0[n] + A[4][1]*derCOMPS1[n] + A[4][2]*derCOMPS2[n] + A[4][3]*derCOMPS3[n] + A[4][4]*derCOMPS4[n]);
                        jepRKF45.addVariable(this.epidemia.getCompartimento(n).getNombre(), COMPS5[n]);
                    }
                    // Además, actualizar atajos y procesos a tiempo [t + C[5]*h]
                    for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(funcAtajos[n]);
                        jepRKF45.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                    }
                    for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                        temp = (Double) jepRKF45.evaluate(jepRKF45.parseExpression(
                                this.epidemia.getProceso(n).getDefinicionContinua(t_ + C[5]*h_)
                                ));
                        jepRKF45.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                    }
                    // f. derX5 = f(X5, t+C[5]*h);
                    //    x(t+h) = X0 + h*(B4[0]*derX0 + B4[1]*derX1 + B4[2]*derX2 + B4[3]*derX3 + B4[4]*derX4 + B4[5]*derX5);
                    //    z(t+h) = X0 + h*(B5[0]*derX0 + B5[1]*derX1 + B5[2]*derX2 + B5[3]*derX3 + B5[4]*derX4 + B5[5]*derX5);
                    for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                        derCOMPS5[n] = (Double) jepRKF45.evaluate(funcComps[n]);
                        solRK4[n] = COMPS0[n] + h_*(B4[0]*derCOMPS0[n] + B4[2]*derCOMPS2[n] + B4[3]*derCOMPS3[n] + B4[4]*derCOMPS4[n]);
                        solRK5[n] = COMPS0[n] + h_*(B5[0]*derCOMPS0[n] + B5[2]*derCOMPS2[n] + B5[3]*derCOMPS3[n] + B5[4]*derCOMPS4[n] + B5[5]*derCOMPS5[n]);
                    }
                    // Tras todo esto, restaurar en el jepRFK45 los valores
                    // anteriores de atajos y procesos por si hubiera que repetir
                    for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                        String atajo = this.epidemia.getAtajo(n).getNombre();
                        temp = (Double) jep.getVarValue(atajo);
                        jepRKF45.addVariable(atajo, temp);
                    }
                    for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                        String proceso = this.epidemia.getProceso(n).getNombre();
                        temp = (Double) jep.getVarValue(proceso);
                        jepRKF45.addVariable(proceso, temp);
                    }
                    
                    // g. Se calcula:
                    // q = ( (E * h) / (2 * |x(t+h) - z(t+h)|) )^(1/4)
                    // Si (q < 1)
                    //      repetir los pasos con h = q*h
                    // Si no
                    //      se guarda este valor y el siguiente se calculará con h = q*h
                    double max = Math.abs(solRK4[0] - solRK5[0]);
                    for (int n = 1; n < solRK4.length; n++) {
                        max = Math.max(max, Math.abs(solRK4[n] - solRK5[n]));
                    }
                    q_ = Math.pow(((TOLERANCIA_ERROR_GLOBAL*h_) / (2*max)), 0.25d);
                } while (q_ < 1);
                
                // Ya hemos obtenido una aproximación aceptable, la guardamos
                for (int n = 0; n < this.epidemia.getCompartimentos().length; n++) {
                    jep.addVariable(this.epidemia.getCompartimento(n).getNombre(), solRK4[n]);
                    jepRKF45.addVariable(this.epidemia.getCompartimento(n).getNombre(), solRK4[n]);
                }                
                
                // 2º Actualizar variables de los atajos
                for (int n = 0; n < this.epidemia.getAtajos().length; n++) {
                    temp = (Double) jep.evaluate(funcAtajos[n]);
                    jep.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                    jepRKF45.addVariable(this.epidemia.getAtajo(n).getNombre(), temp);
                }
                // 3º Actualizar variables de los procesos
                for (int n = 0; n < this.epidemia.getProcesos().length; n++) {
                    temp = (Double) jep.evaluate(jep.parseExpression(
                            this.epidemia.getProceso(n).getDefinicionContinua(t_ + h_)
                            ));
                    jep.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                    jepRKF45.addVariable(this.epidemia.getProceso(n).getNombre(), temp);
                }
                // 4º Calcular los nuevos valores de los resultados y escribir
                for (int m = 0; m < resFiles.length; m++) {
                    for (int n = 0; n < resFiles[m].length; n++) {
                        temp = (Double) jep.evaluate(funcRes[m][n]);
                        resFiles[m][n].println(temp);
                        if (contadorGuardarPtosGrafs_ == guardarPtoGrafCada_ || guardarPtoGrafCada_ == 0) {
                            this.epidemia.getResultado(m).anadirPuntoFuncion(temp, n);
                        }
                    }
                    if (contadorGuardarPtosGrafs_ == guardarPtoGrafCada_ || guardarPtoGrafCada_ == 0) {
                        this.epidemia.getResultado(m).anadirPuntoTiempo(t_ + h_);
                    }
                }
                // 5º Actualizar los parámetros, ya que pueden ser aleatorios:
                // funciones random, distribuciones probabilísticas, etc.
                for (int n = 0; n < this.epidemia.getParametros().length; n++) {
                    temp = (Double) jep.evaluate(funcPars[n]);
                    jep.addVariable(this.epidemia.getParametro(n).getNombre(), temp);
                    jepRKF45.addVariable(this.epidemia.getParametro(n).getNombre(), temp);
                }
            } catch (ParseException pex) {
                System.err.println("Error dentro del bucle en tiempo = " + t_ + h_);
                pex.printStackTrace();
                return null;
            }
            
            // Actualizar el archivo del tiempo, el tiempo, y el paso
            tiempo_.println(t_ + h_);
            t_ = t_ + h_;
            h_ = q_*h_;
            
            // Actualizar barra de estado si toca
            // (no se hace siempre para no tardar más en actualizar que en simular)
            if (numPtosCalculados_ % renovarCada_ == 0) {
                float porcentaje_ = Float.valueOf(Double.toString(t_/tmax_));
                this.setProgress(porcentaje_);
                this.setMessage("Tiempo transcurrido: " + this.elapsedTime() +
                        "         Tiempo restante estimado: " + this.estimatedTime(t_, tmax_));
            }
            if (contadorGuardarPtosGrafs_ == guardarPtoGrafCada_ || guardarPtoGrafCada_ == 0) {
                contadorGuardarPtosGrafs_ = 0;
            }
            contadorGuardarPtosGrafs_++;
            numPtosCalculados_++;
        }
        
        // Establecer en los resultados cuántos puntos se han calculado
        for (int i = 0; i < this.epidemia.getResultados().length; i++) {
            this.epidemia.getResultado(i).setNumPuntosTotal(numPtosCalculados_);
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
    
    /**
     * Método para estimar el tiempo restante para que termine la simulación.
     * Como RKF45 es de paso variable, el tiempo debe estimarse de otra forma.
     */
    protected String estimatedTime(double instanteActual, double instanteFinal) {
        long tiempoTranscurrido = (System.currentTimeMillis() - this.tiempoComienzo) / 1000;
        double tiempoRestante = ((instanteFinal - instanteActual)*tiempoTranscurrido)/instanteActual;
        String tiempo = String.format("%02d", Math.round(tiempoRestante % 60));
        tiempoRestante = tiempoRestante / 60;
        tiempo = String.format("%02d:", Math.round(tiempoRestante % 60)) + tiempo;
        tiempoRestante = tiempoRestante / 60;
        tiempo = String.format("%02d:", Math.round(tiempoRestante)) + tiempo;
        return tiempo;
    }
}
