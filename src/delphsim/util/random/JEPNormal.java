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

import cern.jet.random.Normal;

/**
 * <p>Esta clase implementa una función para el analizador sintáctico JEP que
 * genera un número aleatorio siguiendo una distribución de probabilidad normal
 * definida por dos parámetros:</p>
 * <p>1. mu = media<br/>
 * 2. sigma = desviación estándar</p>
 * @author Víctor E. Tamames Gómez
 */
public class JEPNormal extends PostfixMathCommand {
    
    /**
     * Constructor de la clase. Indica que esta función se usa con dos
     * parámetros.
     */
    public JEPNormal() {
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
        
        inStack.push(normal(param1, param2));
    }
    
    /**
     * Genera un número aleatorio siguiendo una distribución de probabilidad
     * normal con media el primer parámetro y desviación estándar el segundo.
     * Utiliza el método estático de la clase <CODE>Normal</CODE> de la librería
     * COLT.
     * @param param1 Un objeto de tipo <CODE>Number</CODE>, la media de la distribución.
     * @param param2 Un objeto de tipo <CODE>Number</CODE>, la desviación estándar de la distribución.
     * @return El resultado obtenido.
     * @throws org.nfunk.jep.ParseException Si alguno de los dos parámetros no
     *                                      es de tipo <CODE>Number</CODE>.
     */
    public Object normal(Object param1, Object param2) throws ParseException {
        // Todos los parámetros deben ser números reales
        if (param1 instanceof Number && param2 instanceof Number) {
            double mu = ((Number)param1).doubleValue();
            double sigma = ((Number)param2).doubleValue();
            
            return Normal.staticNextDouble(mu, sigma);
        }
        
        throw new ParseException("Invalid parameter type");
    }
}
