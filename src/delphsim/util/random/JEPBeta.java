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

import cern.jet.random.Beta;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad beta
 * definida por dos parámetros:</p>
 * <p>1. alpha, mayor que cero<br/>
 * 2. beta, mayor que cero</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPBeta extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con dos
     * parámetros.
     */
    public JEPBeta() {
        numberOfParameters = 2;
    }
    
    /**
     * Método a ejecutar al evaluar la función. Toma ambos parámetros de la
     * pila, genera el resultado a partir de ellos, y coloca éste en la pila.
     * @param inStack La pila del analizador.
     * @throws org.nfunk.jep.ParseException Si la función no se ha usado correctamente.
     */
    @Override
    public void run(Stack inStack) throws ParseException {
        checkStack(inStack); // check the stack
        
        Object param2 = inStack.pop();
        Object param1 = inStack.pop();
        
        inStack.push(beta(param1, param2));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * beta. Utiliza el método estático de la clase <CODE>Beta</CODE> de la
     * librería COLT.
     * @param param1 Un objeto de tipo <CODE>Number</CODE> mayor que cero.
     * @param param2 Un objeto de tipo <CODE>Number</CODE> mayor que cero.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si alguno de los dos parámetros no
     *                                      es de tipo <CODE>Number</CODE> o no
     *                                      son mayores que cero.
     */
    public Object beta(Object param1, Object param2) throws ParseException {
        // Todos los parámetros deben ser números reales
        if (param1 instanceof Number && param2 instanceof Number) {
            double alpha = ((Number)param1).doubleValue();
            double beta = ((Number)param2).doubleValue();
            if (alpha > 0 && beta > 0) {
                return Beta.staticNextDouble(alpha, beta);
            }
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
