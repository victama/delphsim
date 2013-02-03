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

import cern.jet.random.ChiSquare;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad chi
 * cuadrado definida por un parámetro:</p>
 * <p>1. libertad = el grado de libertad, mayor que cero.</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPJiCuadrado extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con un
     * parámetro.
     */
    public JEPJiCuadrado() {
        numberOfParameters = 1;
    }
    
    /**
     * Método a ejecutar al evaluar la función. Toma el parámetro de la
     * pila, genera el resultado a partir de él, y coloca éste en la pila.
     * @param inStack La pila del analizador.
     * @throws org.nfunk.jep.ParseException Si la función no se ha usado correctamente.
     */
    @Override
    public void run(Stack inStack) throws ParseException {
        checkStack(inStack); // check the stack
        
        Object param1 = inStack.pop();
        
        inStack.push(chiCuadrado(param1));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * chiCuadrado con el grado de libertad pasado como parámetro.
     * Utiliza el método estático de la clase <CODE>ChiSquare</CODE> de la 
     * librería COLT.
     * @param param1 Un objeto de tipo <CODE>Number</CODE>, el grado de libertad, mayor que cero.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si el parámetro no es de tipo
     *                                      <CODE>Number</CODE> o no está en el
     *                                      rango permitido.
     */
    public Object chiCuadrado(Object param1) throws ParseException {
        // Todos los parámetros deben ser números reales
        if (param1 instanceof Number) {
            double N = ((Number)param1).doubleValue();
            if (N > 0) {
                return ChiSquare.staticNextDouble(N);
            }
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
