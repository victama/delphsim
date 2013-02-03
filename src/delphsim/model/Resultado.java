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
package delphsim.model;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Vector;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultElement;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * Los objetos de esta clase representan resultados que el usuario espera
 * obtener si se completa una simulación satisfactoriamente.
 * @author Víctor E. Tamames Gómez
 */
public class Resultado implements Cloneable {
    
    /**
     * Número de puntos que se guardarán para dibujar la gráfica. Es una
     * aproximación, no tienen por qué guardarse exactamente el número que
     * se marca con este atributo.
     */
    public static int NUM_PUNTOS_GRAFICAS = 1000;
    
    /**
     * Resoluciones disponibles para que el usuario exporte las gráficas en
     * formato de imagen. El primer valor indica el ancho, el segundo el alto
     * y el último, entre corchetes, la proporción.
     */
    public static Object[] RESOLUCIONES_DISPONIBLES = {"640, 480 [4:3]", "800, 600 [4:3]", "1024, 768 [4:3]", "1280, 960 [4:3]", "1600, 1200 [4:3]", "2048, 1536 [4:3]", "640, 360 [16:9]", "800, 450 [16:9]", "1024, 576 [16:9]", "1280, 720 [16:9]", "1600, 900 [16:9]", "2048, 1152 [16:9]"};
    
    /**
     * De las resoluciones disponibles, el índice de la que aparece marcada
     * por defecto.
     */
    public static int RESOLUCION_POR_DEFECTO = 2;
    
    /**
     * El título del resultado.
     */
    private String titulo;
    
    /**
     * La etiqueta del eje horizontal, de abcisas, o de las 'x'.
     */
    private String xLabel;
    
    /**
     * La etiqueta del eje vertical, de ordenadas, o de las 'y'.
     */
    private String yLabel;
    
    /**
     * Funciones que se calculan para este resultado. Hacen referencia a una
     * clase interna.
     */
    private Funcion[] funciones = new Funcion[0];
    
    /**
     * La gráfica a generar cuando se complete la simulación.
     */
    private JFreeChart grafica;
    
    /**
     * Los valores del tiempo para los puntos que se van a usar para generar
     * la gráfica.
     */
    private Vector puntosTiempo = new Vector();
    
    /**
     * Archivo donde se almacenan los valores de la simulación correspondientes 
     * al tiempo.
     */
    private File archivoTiempo;
    
    /**
     * Número de puntos total calculados en la simulación.
     */
    private long numPuntosTotal;
    
    /**
     * Constructor de la clase.
     */
    public Resultado() {
    }
    
    /**
     * Método que añade una nueva función a este resultado con la información
     * pasada en los parámetros.
     * @param n El nombre de la función (para la leyenda).
     * @param d La definición de la función.
     * @param c El color de la línea de la función.
     * @param g El grosor de la línea de la función.
     */
    public void anadirFuncion(String n, String d, Color c, int g) {
        Funcion[] fs = new Funcion[this.funciones.length+1];
        for (int i = 0; i < this.funciones.length; i++) {
            fs[i] = this.funciones[i];
        }
        fs[this.funciones.length] = new Funcion(n, d, c, g);
        this.funciones = fs;
    }
    
    /**
     * Añade un nuevo valor al vector de valores del tiempo.
     * @param pTiempo El nuevo valor a añadir.
     */
    public void anadirPuntoTiempo(double pTiempo) {
        this.puntosTiempo.add(pTiempo);
    }
    
    /**
     * Añade un nuevo valor al vector de valores de una función.
     * @param pFuncion El nuevo valor a añadir.
     * @param indice El índice de la función a la que añadirle el valor.
     */
    public void anadirPuntoFuncion(double pFuncion, int indice) {
        this.funciones[indice].addPunto(pFuncion);
    }
    
    /**
     * Método para cambiar el título de este resultado.
     * @param t El nuevo título para este resultado.
     */
    public void setTitulo(String t) {
        this.titulo = t;
    }
    
    /**
     * Método para obtener el título de este resultado.
     * @return El título de este resultado.
     */
    public String getTitulo() {
        return this.titulo;
    }
    
    /**
     * Método para cambiar la etiqueta del eje horizontal.
     * @param x La nueva etiqueta.
     */
    public void setXLabel(String x) {
        this.xLabel = x;
    }
    
    /**
     * Método para obtener la etiqueta del eje horizontal.
     * @return La etiqueta.
     */
    public String getXLabel() {
        return this.xLabel;
    }
    
    /**
     * Método para cambiar la etiqueta del eje vertical.
     * @param y La nueva etiqueta.
     */
    public void setYLabel(String y) {
        this.yLabel = y;
    }
    
    /**
     * Método para obtener la etiqueta del eje vertical.
     * @return La etiqueta.
     */
    public String getYLabel() {
        return this.yLabel;
    }
    
    /**
     * Método para cambiar el archivo que contiene los valores de la simulación
     * referidos al tiempo.
     * @param f El nuevo fichero.
     */
    public void setFileTiempo(String f) {
        this.archivoTiempo = new File(f);
    }
    
    /**
     * Método para obtener el archivo que contiene los valores de la simulación
     * referidos al tiempo.
     * @return El fichero.
     */
    public File getFileTiempo() {
        return this.archivoTiempo;
    }
    
    /**
     * Método para cambiar el número de puntos total generados por la simulación.
     * @param t El nuevo número de puntos total.
     */
    public void setNumPuntosTotal(long t) {
        this.numPuntosTotal = t;
    }
    
    /**
     * Método para cambiar el número de puntos total generados por la simulación.
     * @return El número de puntos total.
     */
    public long getNumPuntosTotal() {
        return this.numPuntosTotal;
    }
    
    /**
     * Método para obtener el número de funciones que tiene este resultado.
     * @return El número de funciones.
     */
    public int getNumFunciones() {
        return this.funciones.length;
    }
    
    /**
     * Método para cambiar la información de una función en concreto.
     * @param funcion Un array que contiene el nombre de la función, la
     *                definición, el color y el grosor, en ese orden.
     * @param indice El índice de la función a cambiar.
     */
    public void setFuncion(Object[] funcion, int indice) {
        if (this.funciones[indice] != null) {
            this.funciones[indice].eliminar();
        }
        this.funciones[indice] = new Funcion((String)funcion[0], (String)funcion[1], (Color)funcion[2], (Integer)funcion[3]);
    }
    
    /**
     * Método para obtener la información de una función en concreto.
     * @param indice El índice de la función.
     * @return Un array que contiene el nombre de la función, la definición,
     *         el color y el grosor, en ese orden.
     */
    public Object[] getFuncion(int indice) {
        Object[] o = new Object[4];
        o[0] = this.funciones[indice].getNombre();
        o[1] = this.funciones[indice].getDefinicion();
        o[2] = this.funciones[indice].getColor();
        o[3] = this.funciones[indice].getGrosor();
        return o;
    }
    
    /**
     * Método para cambiar las funciones de este resultado.
     * @param fs Las nuevas funciones.
     */
    private void setFunciones(Funcion[] fs) {
        this.funciones = fs;
    }
    
    /**
     * Método para cambiar el archivo que contiene los valores de una función.
     * @param f El nuevo fichero.
     * @param indice El índice de la función.
     */
    public void setFileFuncion(String f, int indice) {
        this.funciones[indice].setFichero(f);
    }
    
    /**
     * Método para obtener el archivo que contiene los valores de una función.
     * @param indice El índice de la función.
     * @return El fichero.
     */
    public File getFileFuncion(int indice) {
        return this.funciones[indice].getFichero();
    }
    
    /**
     * Método que elimina las funciones de este resultado una por una.
     */
    public void eliminarFunciones() {
        for (int i = 0; i < this.funciones.length; i++) {
            this.funciones[i].eliminar();
        }
        this.funciones = new Funcion[0];
    }
    
    /**
     * Método para construir una gráfica de tipo <CODE>JFreeChart</CODE> con
     * los valores de este resultado y sus funciones y obtener como resultado
     * el panel de tipo <CODE>ChartPanel</CODE> que la contiene.
     * @return Un panel <CODE>ChartPanel</CODE> con la nueva gráfica.
     */
    public ChartPanel construirPanelResultado() {
        DefaultXYDataset data = new DefaultXYDataset();
        for (int i = 0; i < this.getNumFunciones(); i++) {
            String nombre = this.funciones[i].getNombre();
            double[][] puntos = new double[2][this.puntosTiempo.size()];
            for (int j = 0; j < this.puntosTiempo.size(); j++) {
                puntos[0][j] = Double.valueOf(this.puntosTiempo.get(j).toString());
                puntos[1][j] = this.funciones[i].getPunto(j);
            }
            data.addSeries(nombre, puntos);
        }
        JFreeChart chart = ChartFactory.createXYLineChart(
            this.getTitulo(), // título laaargo
            this.getXLabel(),    // xLabel
            this.getYLabel(),         // yLabel
            data,        // datos
            PlotOrientation.VERTICAL,
            true, // legenda
            true, // tooltips
            false // URLs
        );
        for (int i = 0; i < this.getNumFunciones(); i++) {
            chart.getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(this.funciones[i].getGrosor()));
            chart.getXYPlot().getRenderer().setSeriesPaint(i, this.funciones[i].getColor());
        }
        ChartPanel panel = new ChartPanel(
                                chart, // gráfica
                                true,  // propiedades
                                false, // guardar
                                false, // imprimir
                                true,  // zoom
                                true   // tooltips
                            );
        this.grafica = chart;
        return panel;
    }
    
    /**
     * Método para exportar la gráfica de este resultado a un formato de imagen.
     * Para ello emplea los métodos estáticos de la clase <CODE>Resultado</CODE>.
     * @param destino El fichero donde exportar la imagen.
     * @param resolucion El tamaño de la imagen.
     * @param formato El formato de la imagen.
     * @throws java.io.IOException Si hay algún problema al guardar la imagen en disco.
     * @see delphsim.model.Resultado#exportarComoPNG(java.io.File, org.jfree.chart.JFreeChart, int, int) exportarComoPNG
     * @see delphsim.model.Resultado#exportarComoSVG(java.io.File, org.jfree.chart.JFreeChart, int, int) exportarComoSVG
     */
    public void exportarImagen(File destino, Object resolucion, String formato) throws IOException {
        // Primero transformamos la resolución a anchura y altura
        int anchura = Integer.valueOf(resolucion.toString().split(", ")[0]); // NOI18N
        int altura = Integer.valueOf(resolucion.toString().split(", ")[1].split(" ")[0]); // NOI18N
        if (formato.equals("png")) {
            Resultado.exportarComoPNG(destino, this.grafica, anchura, altura);
        } else if (formato.equals("svg")) {
            Resultado.exportarComoSVG(destino, this.grafica, anchura, altura);
        } else {
            System.err.println("Error: formato de imagen desconocido.");
        }
    }
    
    /**
     * Método para exportar los datos de este resultado a un formato de texto.
     * Para ello emplea los métodos estáticos de la clase <CODE>Resultado</CODE>.
     * @param destino El fichero donde exportar los datos.
     * @param numDatos El número de datos (puntos, filas) que se quieren exportar.
     * @param formato El formato de exportación.
     * @throws java.io.IOException Si hay algún problema al crear el fichero.
     * @see delphsim.model.Resultado#exportarComoCSV(java.io.File, java.lang.String, java.io.File[], long, long) exportarComoCSV
     * @see delphsim.model.Resultado#exportarComoXML(java.io.File, java.lang.String[], java.lang.String[], java.io.File[], long, long) exportarComoXML
     */
    public void exportarDatos(File destino, int numDatos, String formato) throws IOException {
        // Primero recogemos los archivos de todas las funciones
        File[] temps = new File[this.getNumFunciones()+1];
        temps[0] = this.archivoTiempo;
        for (int i = 0; i < this.getNumFunciones(); i++) {
            temps[i+1] = this.getFileFuncion(i);
        }
        if (formato.equals("csv")) {
            // Construimos la cabecera con datos privados
            String cabecera = "\"Tiempo\"";
            for (int i = 0; i < this.getNumFunciones(); i++) {
                cabecera += ",\"" + this.funciones[i].getNombre() + " {" + this.funciones[i].getDefinicion() + "}\"";
            }
            if (numDatos == 0 || numDatos >= this.numPuntosTotal) {
                Resultado.exportarComoCSV(destino, cabecera, temps, this.numPuntosTotal, this.numPuntosTotal);
            } else {
                Resultado.exportarComoCSV(destino, cabecera, temps, this.numPuntosTotal, numDatos);
            }
        } else if (formato.equals("xml")) {
            // Recogemos los datos privados en dos arrays para pasarlos
            String[] nombres = new String[this.getNumFunciones()+1];
            String[] definiciones = new String[this.getNumFunciones()+1];
            nombres[0] = null;
            definiciones[0] = null;
            for (int i = 0; i < this.getNumFunciones(); i++) {
                nombres[i+1] = this.funciones[i].getNombre();
                definiciones[i+1] = this.funciones[i].getDefinicion();
            }
            if (numDatos == 0 || numDatos >= this.numPuntosTotal) {
                Resultado.exportarComoXML(destino, nombres, definiciones, temps, this.numPuntosTotal, this.numPuntosTotal);
            } else {
                Resultado.exportarComoXML(destino, nombres, definiciones, temps, this.numPuntosTotal, numDatos);
            }
        } else {
            System.err.println("Error: formato de texto desconocido.");
        }
    }
    
    /**
     * Método estático que exporta una gráfica <CODE>JFreeChart</CODE> al
     * formato de imagen PNG.
     * @param destino El fichero de destino.
     * @param chart La gráfica a exportar.
     * @param anchura Anchura de la imagen final.
     * @param altura Altura de la imagen final.
     * @throws java.io.IOException Si hubiera algún problema al crear el archivo en disco.
     */
    public static void exportarComoPNG(File destino, JFreeChart chart, int anchura, int altura) throws IOException {
        // Para PNG utilizamos las herramientas que ofrece JFreeChart
        ChartUtilities.saveChartAsPNG(destino, chart, anchura, altura);
    }
    
    /**
     * Método estático que exporta una gráfica <CODE>JFreeChart</CODE> al
     * formato de imagen vectorial SVG.
     * @param destino El fichero de destino.
     * @param chart La gráfica a exportar.
     * @param anchura Anchura de la imagen final.
     * @param altura Altura de la imagen final.
     * @throws java.io.IOException Si hubiera algún problema al crear el archivo en disco.
     */
    public static void exportarComoSVG(File destino, JFreeChart chart, int anchura, int altura) throws IOException {
        // Para SVG utilizamos la librería Batik
        // Obtener un DOMImplementation
        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        // Crear una instancia de org.w3c.dom.Document
        String svgNS = "http://www.w3.org/2000/svg";
        Document document = domImpl.createDocument(svgNS, "svg", null);
        // Crear una instancia del Generador de SVGs
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
        // Dibujar la gráfica en el Generador de SVGs
        chart.draw(svgGenerator, new Rectangle(anchura, altura));
        // Escribir el archivo SVG
        OutputStream outputStream = new FileOutputStream(destino);
        Writer out = new OutputStreamWriter(outputStream, "UTF-8");
        svgGenerator.stream(out, true /* utilizar CSS */);
        outputStream.flush();
        outputStream.close();
    }
    
    /**
     * Método estático que exporta los valores obtenidos tras la simulación al
     * formato textual CSV.
     * @param destino El archivo de destino.
     * @param cabecera La cabecera del archivo CSV (primera fila del documento).
     * @param temps Array con los archivos temporales de los cuales obtener los
     *              datos a exportar.
     * @param numPuntosTotal El número de puntos total que contienen los
     *                       archivos temporales.
     * @param numPuntosExportar El número de puntos que quiere obtener el usuario.
     * @throws java.io.IOException Si hubiera algún problema al crear el archivo en disco.
     */
    public static void exportarComoCSV(File destino, String cabecera, File[] temps, long numPuntosTotal, long numPuntosExportar) throws IOException {
        // Creamos el archivo de salida y escribimos la cabecera en él
        PrintWriter salida = new PrintWriter(new BufferedWriter(new FileWriter(destino)));
        salida.println(cabecera);
        // Creamos los búfers de lectura para leer los temporales
        BufferedReader[] buffers = new BufferedReader[temps.length];
        for (int i = 0; i < temps.length; i++) {
            buffers[i] = new BufferedReader(new FileReader(temps[i]));
        }
        // Calculamos cada cuanto tenemos que guardar un punto
        double cadaCuanto;
        if (numPuntosTotal == numPuntosExportar) {
            cadaCuanto = 1.0d;
        } else {
            cadaCuanto = new Double(numPuntosTotal) / new Double(numPuntosExportar-1);
        }
        long siguientePuntoExportar = 0;
        long contadorNumPuntoLeido = 0;
        long contadorNumPuntosExportados = 0;
        // Comenzamos a leer los temporales escribiendo en el CSV de salida
        String nuevaLinea = "";
        String leido = null;
        for (int i = 0; i < buffers.length; i++) {
            leido = buffers[i].readLine();
            nuevaLinea += leido + ",";
        }
        // En el momento en que se lee un null, se termina
        while (leido != null) {
            nuevaLinea = nuevaLinea.substring(0, nuevaLinea.length()-1);
            if (siguientePuntoExportar == contadorNumPuntoLeido) {
                salida.println(nuevaLinea);
                contadorNumPuntosExportados++;
                siguientePuntoExportar = Math.round(cadaCuanto*contadorNumPuntosExportados);
                if (siguientePuntoExportar >= numPuntosTotal) {
                    siguientePuntoExportar = numPuntosTotal-1;
                }
            }
            nuevaLinea = "";
            for (int i = 0; i < buffers.length; i++) {
                leido = buffers[i].readLine();
                nuevaLinea += leido + ",";
            }
            contadorNumPuntoLeido++;
        }
        // Cerramos los búfers y el archivo de salida
        for (int i = 0; i < buffers.length; i++) {
            buffers[i].close();
        }
        salida.flush();
        salida.close();
    }
    
    /**
     * Método estático que exporta los valores obtenidos tras la simulación al
     * formato XML.
     * @param destino El archivo de destino.
     * @param nombres Los nombres de las distintas columnas/funciones.
     * @param definiciones La definición de cada columna/función.
     * @param temps Array con los archivos temporales de los cuales obtener los
     *              datos a exportar.
     * @param numPuntosTotal El número de puntos total que contienen los
     *                       archivos temporales.
     * @param numPuntosExportar El número de puntos que quiere obtener el usuario.
     * @throws java.io.IOException Si hubiera algún problema al crear el archivo en disco.
     */
    public static void exportarComoXML(File destino, String[] nombres, String[] definiciones, File[] temps, long numPuntosTotal, long numPuntosExportar) throws IOException {
        // Crear el documento, el elemento 'raiz' y asignarlo
        org.dom4j.Document documento = DocumentHelper.createDocument();
        DefaultElement elementoResultado = new DefaultElement("resultado");
        documento.setRootElement(elementoResultado);
        
        // Creamos los búfers de lectura para leer los temporales
        BufferedReader[] buffers = new BufferedReader[temps.length];
        for (int i = 0; i < temps.length; i++) {
            buffers[i] = new BufferedReader(new FileReader(temps[i]));
        }
        // Calculamos cada cuanto tenemos que guardar un punto
        double cadaCuanto;
        if (numPuntosTotal == numPuntosExportar) {
            cadaCuanto = 1.0d;
        } else {
            cadaCuanto = new Double(numPuntosTotal) / new Double(numPuntosExportar-1);
        }
        long siguientePuntoExportar = 0;
        long contadorNumPuntoLeido = 0;
        long contadorNumPuntosExportados = 0;
        // Comenzamos a leer los temporales añadiendo elementos al documento
        String[] valores = new String[buffers.length];
        for (int i = 0; i < buffers.length; i++) {
            valores[i] = buffers[i].readLine();
        }
        // En el momento en que se lee un null, se termina
        while (valores[0] != null) {
            // Para cada punto que haya que exportar
            if (siguientePuntoExportar == contadorNumPuntoLeido) {
                DefaultElement nuevaFila = new DefaultElement("fila");
                // Para el tiempo, nuevo elemento, su valor y añadirlo a la fila
                DefaultElement elementoTiempo = new DefaultElement("Tiempo");
                elementoTiempo.setText(valores[0]);
                nuevaFila.add(elementoTiempo);
                // Lo mismo para cada linea, pero ademas con nombre y definición
                for (int i = 1; i < valores.length; i++) {
                    DefaultElement elementoLinea = new DefaultElement("Línea"+i);
                    elementoLinea.add(new DefaultAttribute("nombre", nombres[i]));
                    elementoLinea.add(new DefaultAttribute("definicion", definiciones[i]));
                    elementoLinea.setText(valores[i]);
                    nuevaFila.add(elementoLinea);                    
                }
                // Y añadimos la nueva fila
                elementoResultado.add(nuevaFila);
                // Calculamos el siguiente punto a exportar
                contadorNumPuntosExportados++;
                siguientePuntoExportar = Math.round(cadaCuanto*contadorNumPuntosExportados);
                if (siguientePuntoExportar >= numPuntosTotal) {
                    siguientePuntoExportar = numPuntosTotal-1;
                }
            }
            // Leemos la siguiente línea de los ficheros
            for (int i = 0; i < buffers.length; i++) {
                valores[i] = buffers[i].readLine();
            }
            contadorNumPuntoLeido++;
        }
        // Cerramos los búfers y el archivo de salida
        for (int i = 0; i < buffers.length; i++) {
            buffers[i].close();
        }
        // Imprimimos el documento como XML
        OutputFormat formato = OutputFormat.createPrettyPrint();
        formato.setEncoding("UTF-16");
        formato.setIndent("\t");
        formato.setNewLineAfterDeclaration(false);
        formato.setPadText(false);
        formato.setTrimText(true);
        formato.setXHTML(true);
        OutputStreamWriter salida = new OutputStreamWriter(
                new FileOutputStream(destino), "UTF-16");
        XMLWriter escritor = new XMLWriter(salida, formato);
        escritor.write(documento);
        escritor.flush();
        escritor.close();
    }
    
    /**
     * Implementación de la interfaz Cloneable.
     * @return Un clon idéntico a este objeto.
     */
    @Override
    public Resultado clone() {
        Resultado clon = new Resultado();
        clon.setTitulo(this.getTitulo());
        clon.setXLabel(this.getXLabel());
        clon.setYLabel(this.getYLabel());
        Funcion[] clones = new Funcion[this.getNumFunciones()];
        for (int i = 0; i < clones.length; i++) {
            clones[i] = this.funciones[i].clone();
        }
        clon.setFunciones(clones);
        return clon;
    }
    
    /**
     * Clase interna que representa cada una de las líneas/funciones que puede
     * contener un resultado.
     */
    private class Funcion implements Cloneable {
        
        /**
         * El nombre de la función (para la leyenda, sobre todo).
         */
        private String nombre;
        
        /**
         * La definición de la función.
         */
        private String definicion;
        
        /**
         * El color de la línea que representará a esta función en la gráfica.
         */
        private Color color;
        
        /**
         * El grosor de la línea que representará a esta función en la gráfica.
         */
        private int grosor;
        
        /**
         * Los valores de la función para los puntos que se van a usar para
         * generar la gráfica.
         */
        private Vector puntosDibujar = new Vector();
        
        /**
         * Archivo donde se almacenan los valores de la simulación
         * correspondientes a esta función.
         */
        private File fichero;
        
        /**
         * Constructor de la clase.
         * @param n El nombre de la nueva función.
         * @param d La definición de la nueva función.
         * @param c El color de la nueva función.
         * @param g El grosor de la nueva función.
         */
        public Funcion(String n, String d, Color c, int g) {
            this.nombre = n;
            this.definicion = d;
            this.color = c;
            this.grosor = g;
        }
        
        /**
         * Método para cambiar el nombre de esta función.
         * @param n El nuevo nombre.
         */
        public void setNombre(String n) {
            this.nombre = n;
        }
        
        /**
         * Método para obtener el nombre de esta función.
         * @return El nombre de la función.
         */
        public String getNombre() {
            return this.nombre;
        }
        
        /**
         * Método para cambiar la definición de esta función.
         * @param d La nueva definición.
         */
        public void setDefinicion(String d) {
            this.definicion = d;
        }
        
        /**
         * Método para obtener la definición de esta función.
         * @return La definición de esta función.
         */
        public String getDefinicion() {
            return this.definicion;
        }
        
        /**
         * Método para cambiar el color de la línea de esta función.
         * @param c El nuevo color.
         */
        public void setColor(Color c) {
            this.color = c;
        }
        
        /**
         * Método para obtener el color de la línea de esta función.
         * @return El color de la línea.
         */
        public Color getColor() {
            return this.color;
        }
        
        /**
         * Método para cambiar el grosor de la línea de esta función.
         * @param g El nuevo grosor.
         */
        public void setGrosor(int g) {
            this.grosor = g;
        }
        
        /**
         * Método para obtener el grosor de la línea de esta función.
         * @return El grosor de la línea.
         */
        public int getGrosor() {
            return this.grosor;
        }
        
        /**
         * Añade un nuevo valor al vector de valores de esta función.
         * @param punto El nuevo valor a añadir.
         */
        public void addPunto(double punto) {
            this.puntosDibujar.add(punto);
        }
        
        /**
         * Método para obtener el valor marcado por el índice pasado como
         * parámetro del vector de valores de esta función.
         * @param indice El índice del valor a obtener.
         * @return El valor.
         */
        public double getPunto(int indice) {
            return Double.valueOf(this.puntosDibujar.get(indice).toString());
        }
        
        /**
         * Método para cambiar el fichero relacionado con esta función.
         * @param f Cadena de texto con la ruta del nuevo fichero.
         */
        public void setFichero(String f) {
            this.fichero = new File(f);
        }
        
        /**
         * Método para cambiar el fichero relacionado con esta función.
         * @param f El nuevo fichero.
         */
        public void setFichero(File f) {
            this.fichero = f;
        }
        
        /**
         * Método para obtener el fichero relacionado con esta función.
         * @return El fichero.
         */
        public File getFichero() {
            return this.fichero;
        }
        
        /**
         * Método para eliminar esta función. Es responsable de eliminar el
         * fichero asociado a ella o, si no se pudiera, indicar al sistema que
         * lo borre al salir de la aplicación.
         */
        public void eliminar() {
            if (this.fichero != null && this.fichero.exists()) {
                if (!this.fichero.delete()) {
                    this.fichero.deleteOnExit();
                }
                this.fichero = null;
            }
        }
    
        /**
         * Implementación de la interfaz Cloneable.
         * @return Un clon idéntico a este objeto.
         */
        @Override
        public Funcion clone() {
            Funcion clon = new Funcion(this.getNombre(), this.getDefinicion(), this.getColor(), this.getGrosor());
            clon.setFichero(this.getFichero());
            return clon;
        }
    }
}
