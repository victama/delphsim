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

import cern.jet.random.Gamma;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad gamma
 * definida por dos parámetros:</p>
 * <p>1. alpha = media * media / varianza<br/>
 * 2. lambda = 1 / (varianza / media)</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPGamma extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con dos
     * parámetros.
     */
    public JEPGamma() {
        numberOfParameters = 2;
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
        
        Object param2 = inStack.pop();
        Object param1 = inStack.pop();
        
        inStack.push(gamma(param1, param2));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * gamma cuya media y varianza vienen definidas por los dos parámetros.
     * Utiliza el método estático de la clase <CODE>Gamma</CODE> de la librería
     * COLT.
     * @param param1 Un objeto de tipo <CODE>Number</CODE>.
     * @param param2 Un objeto de tipo <CODE>Number</CODE>.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si los parámetros no son de tipo <CODE>Number</CODE>.
     */
    public Object gamma(Object param1, Object param2) throws ParseException {
        // Todos los parámetros deben ser números reales
        if (param1 instanceof Number && param2 instanceof Number) {
            double alpha = ((Number)param1).doubleValue();
            double lambda = ((Number)param2).doubleValue();
            if (alpha > 0 && lambda > 0) {
                return Gamma.staticNextDouble(alpha, lambda);
            }
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
