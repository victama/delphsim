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

import cern.jet.random.Uniform;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad
 * uniforme definida por dos parámetros:</p>
 * <p>1. desde = cota mínima del rango en el que generar el número aleatorio<br/>
 * 2. hasta = cota máxima del rango en el que generar el número aleatorio</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPUniforme extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con dos
     * parámetros.
     */
    public JEPUniforme() {
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
        
        inStack.push(uniforme(param1, param2));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * uniforme cuyo rango viene definido por los dos parámetros.
     * Utiliza el método estático de la clase <CODE>Uniform</CODE> de la librería
     * COLT.
     * @param param1 Un objeto de tipo <CODE>Number</CODE>.
     * @param param2 Un objeto de tipo <CODE>Number</CODE>.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si los parámetros no son de tipo <CODE>Number</CODE>.
     */
    public Object uniforme(Object param1, Object param2) throws ParseException {
        // Todos los parámetros deben ser números reales
        if (param1 instanceof Number && param2 instanceof Number) {
            double desde = ((Number)param1).doubleValue();
            double hasta = ((Number)param2).doubleValue();
            
            return Uniform.staticNextDoubleFromTo(desde, hasta);
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
