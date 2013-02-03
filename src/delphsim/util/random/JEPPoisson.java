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

import cern.jet.random.Poisson;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad de
 * poisson definida por un parámetro:</p>
 * <p>1. lambda = la media</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPPoisson extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con un
     * parámetro.
     */
    public JEPPoisson() {
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
        
        inStack.push(poisson(param1));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * poisson con media el primer parámetro.
     * Utiliza el método estático de la clase <CODE>Poisson</CODE> de la librería
     * COLT.
     * @param param1 Un objeto de tipo <CODE>Number</CODE>, la media de la distribución.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si el parámetro no es de tipo <CODE>Number</CODE>.
     */
    public Object poisson(Object param1) throws ParseException {
        // Todos los parámetros deben ser números reales
        if (param1 instanceof Number) {
            double lambda = ((Number)param1).doubleValue();
            
            return Double.valueOf(String.valueOf(Poisson.staticNextInt(lambda)));
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
