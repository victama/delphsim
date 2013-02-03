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

import java.io.IOException;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.File;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Esta clase implementa un método para validar un archivo XML dado contra el
 * XML Schema definido para la aplicación.
 * 
 * Se tomó como base una versión de SchemaValidator provista junto con la
 * distribución del paquete JAXP 1.3, modificada por Michael Kay para establecer
 * propiedades del sistema que fuercen el uso de Saxon, para mejorar el informe
 * de errores y los comentarios.
 * 
 * El archivo original no contenía condiciones o términos sobre copyright, pero
 * se asume que se ajusta a las típicas normas de Apache.
 * 
 * @author Víctor E. Tamames Gómez
 */
public class SchemaValidator {

    /**
     * Cadena de texto que contiene la dirección del XML Schema que se va a usar.
     */
    private String XMLSchemaURL;

    /**
     * Constructor de la clase.
     */
    public SchemaValidator() {
    }

    /**
     * Clase interna para personalizar el manejador de errores SAX.
     */
    protected static class Handler implements ErrorHandler {

        /**
         * Cadena de texto que va reuniendo los errores que se han producido.
         */
        private String errors = "";

        /**
         * Informa sobre un error no fatal
         * @param ex información sobre el error
         */
        public void error(SAXParseException ex) {
            this.errors += "<Error> En la línea " + ex.getLineNumber() + " del archivo " + ex.getSystemId() + ":\n" + ex.getMessage() + "\n\n";
        }

        /**
         * Informa sobre un error fatal
         * @param ex información sobre el error
         */
        public void fatalError(SAXParseException ex) {
            this.errors += "<Error fatal> En la línea " + ex.getLineNumber() + " del archivo " + ex.getSystemId() + ":\n" + ex.getMessage() + "\n\n";
        }

        /**
         * Informa sobre una advertencia
         * @param ex información sobre la advertencia
         */
        public void warning(org.xml.sax.SAXParseException ex) {
            this.errors += "<Advertencia> En la línea " + ex.getLineNumber() + " del archivo " + ex.getSystemId() + ":\n" + ex.getMessage() + "\n\n";
        }

        /**
         * Método genérico para dar valor al atributo <i>errors</i>.
         * @param errorsHandler Nueva descripción sobre los errores.
         */
        public void setErrors(String errorsHandler) {
            this.errors = errorsHandler;
        }

        /**
         * Método genérico para obtener el atributo <i>errors</i>.
         * @return  Una cadena de texto que contiene una descripción de los
         *          errores producidos, o la cadena vacía si no se ha producido
         *          ninguno.
         */
        public String getErrors() {
            return this.errors;
        }
    }

    /**
     * Clase interna que implementa la resolución de recursos. Esta versión
     * siempre devuelve <i>null</i>, lo que equivale a no utilizar ninguno.
     * LSResourceResolver es parte del módulo de cargado/guardado del Nivel 3
     * de DOM.
     */
    protected static class Resolver implements LSResourceResolver {

        /**
         * Resuelve una referencia a un recurso
         * @param type El tipo del recurso, por ejemplo un esquema, un documento fuente XML, o una consulta.
         * @param namespace El espacio de nombres del objetivo (en el caso de un documento de esquema).
         * @param publicId El identificador público.
         * @param systemId El identificador de sistema (escrito, posiblemente un <i>URI</i> relativo).
         * @param baseURI El <i>URI</i> base contra el que deberá resolverse el identificador de sistema.
         * @return Un objeto <i>LSInput</i> que normalmente contiene la fuente de caracteres o bytes identificada por
         * los parámetros introducidos. Si la referencia no puede resolverse o se escoge no hacerse, devolverá <i>null</i>.
         */
        public LSInput resolveResource(String type, String namespace, String publicId, String systemId, String baseURI) {
            return null;
        }
    }
    
    /**
     * Método para fijar qué XML Schema se va a usar.
     * @param ruta La dirección del XML Schema.
     */
    public void setXMLSchemaURL(String ruta) {
        this.XMLSchemaURL = ruta;
    }
    
    /**
     * Método para obtener el XML Schema que se va a usar.
     * @return La cadena de texto que contiene la dirección del XML Schema.
     */
    public String getXMLSchemaURL() {
        return this.XMLSchemaURL;
    }

    /**
     * Método principal que recibe como parámetros la dirección del XML Schema
     * y del archivo XML de la aplicación que ha escogido el usuario y comprueba
     * que éste último se ajuste al primero.
     * @param argArchivo    El archivo XML de la aplicación que se va a comprobar.
     * @return Devuelve una cadena vacía si el archivo es válido y con los errores detectados en caso contrario.
     */
    public String validar(File argArchivo) throws SAXException, IOException {
        Handler handler = new Handler();
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        //System.out.println("Cargado proveedor de validaciones de esquemas " + schemaFactory.getClass().getName());
        schemaFactory.setErrorHandler(handler);
        Schema schemaGrammar = schemaFactory.newSchema(new File(this.XMLSchemaURL));
        //System.out.println("Creado un objeto gramática para el esquema: " + this.XMLSchemaURL);

        Resolver resolver = new Resolver();
        Validator schemaValidator = schemaGrammar.newValidator();
        schemaValidator.setResourceResolver(resolver);
        schemaValidator.setErrorHandler(handler);

        //System.out.println("Validando el archivo " + argArchivo + " mediante el esquema " + this.XMLSchemaURL);
        schemaValidator.validate(new StreamSource(argArchivo));
        return handler.getErrors();
    }
}
