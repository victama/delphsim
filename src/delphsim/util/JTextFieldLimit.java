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
package delphsim.util;

import javax.swing.text.*;

/**
 * Clase que implementa una manera de limitar un campo de texto a un número
 * determinado de caracteres.
 * @author <a target=blank href="http://www.rgagnon.com/">Real Gagnon</a>
 */
public class JTextFieldLimit extends PlainDocument {

    /**
     * Límite de caracteres.
     */
    private int limit;
    
    /**
     * Conversión a mayúsculas opcional.
     */
    private boolean toUppercase = false;
    
    /**
     * Constructor de la clase.
     * @param limit Límite de caracteres para el <CODE>JTextField</CODE>.
     */
    public JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    
    /**
     * Constructor de la clase.
     * @param limit Límite de caracteres para el <CODE>JTextField</CODE>.
     * @param upper Si convertir a mayúsculas o no.
     */
    public JTextFieldLimit(int limit, boolean upper) {
        super();
        this.limit = limit;
        toUppercase = upper;
    }
    
    /**
     * Este método implementa cómo se modifica el contenido de un <CODE>JTextField</CODE>,
     * impidiéndolo si al hacerlo se sobrepasa el límite impuesto. En caso de
     * que haya que convertir a mayúsculas, también se hace aquí.
     * @param offset La posición a partir de la cual añadir el nuevo texto.
     * @param str El nuevo texto a añadir.
     * @param attr Los atributos del cambio.
     * @throws javax.swing.text.BadLocationException
     */
    @Override
    public void insertString(int offset, String str, AttributeSet attr)
            throws BadLocationException {
        if (str == null) {
            return;
        }
        if ((getLength() + str.length()) <= limit) {
            if (toUppercase) {
                str = str.toUpperCase();
            }
            super.insertString(offset, str, attr);
        }
    }
}

