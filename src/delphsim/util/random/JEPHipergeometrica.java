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
package delphsim.util.random;

import java.util.Stack;
import org.nfunk.jep.ParseException;
import org.nfunk.jep.function.PostfixMathCommand;

import cern.jet.random.HyperGeometric;
import cern.jet.random.Uniform;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad
 * hipergeométrica definida por tres parámetros:</p>
 * <p>1. N = tamaño de la población<br/>
 * 2. s = número de éxitos en la población<br/>
 * 3. n = número de muestras tomadas</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPHipergeometrica extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con tres
     * parámetros.
     */
    public JEPHipergeometrica() {
        numberOfParameters = 3;
    }
    
    /**
     * Método a ejecutar al evaluar la función. Toma los parámetros de la
     * pila, genera el resultado a partir de ellos, y coloca éste en la pila.
     * @param inStack La pila del analizador.
     * @throws org.nfunk.jep.ParseException Si la función no se ha usado correctamente.
     */
    @Override
    public void run(Stack inStack) throws ParseException {
        checkStack(inStack); // check the stack
        
        Object param3 = inStack.pop();
        Object param2 = inStack.pop();
        Object param1 = inStack.pop();
        
        inStack.push(hipergeometrica(param1, param2, param3));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * hipergeometrica.
     * Utiliza el método estático de la clase <CODE>HyperGeometric</CODE> de
     * la librería COLT.
     * @param param1 Un entero mayor que uno.
     * @param param2 Un entero positivo, menor o igual que param1.
     * @param param3 Un entero positivo, menor o igual que param1.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si alguno de los parámetros no es
     *                                      de tipo <CODE>Number</CODE> o no
     *                                      están en los rangos establecidos.
     */
    public Object hipergeometrica(Object param1, Object param2, Object param3) throws ParseException {
        // Todos los parámetros deben ser números enteros
        if (param1 instanceof Number && param2 instanceof Number && param3 instanceof Number) {
            int N = ((Number)param1).intValue();
            int s = ((Number)param2).intValue();
            int n = ((Number)param3).intValue();
            // Tamaño población >= 2, s y n <= N
            if (N >= 1 &&
                    0 <= s && s <= N &&
                    0 <= n && n <= N) {
                try {
                    // Primero hay que comprobar esto porque el método del CERN falla
                    // Si toda la población son éxitos, devolver el número de pruebas
                    if (N == s) {
                        return Double.valueOf(String.valueOf(n));
                    }
                    // Si el conjunto tomado es la población entera, devolver el número de éxitos
                    if (N == n) {
                        return Double.valueOf(String.valueOf(s));
                    }
                    return HyperGeometric.staticNextInt(N, s, n);
                } catch (Exception e) {
                    // Primero hay que comprobar esto porque el método del CERN falla
                    // Si toda la población son éxitos, devolver el número de pruebas
                    if (N == s) {
                        return Double.valueOf(String.valueOf(n));
                    }
                    // Si el conjunto tomado es la población entera, devolver el número de éxitos
                    if (N == n) {
                        return Double.valueOf(String.valueOf(s));
                    }
                    // Intentarlo de nuevo porque con números grandes, la primera vez falla
                    try {
                        return HyperGeometric.staticNextInt(N, s, n);
                    } catch (Exception ex) {}
                    // Aquí no deberíamos llegar nunca, pero por si acaso
                    double aleatorio = Uniform.staticNextDoubleFromTo(0.0d, 0.7d);
                    // Si hay más éxitos que items tomados como prueba
                    if (s > n) {
                        return Double.valueOf(String.valueOf((n - Math.round(aleatorio*(N-s)))));
                    } else {
                        // Si hay más items tomados como prueba que éxitos
                        return Double.valueOf(String.valueOf((s - Math.round(aleatorio*(N-n)))));
                    }
                }
            }
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
