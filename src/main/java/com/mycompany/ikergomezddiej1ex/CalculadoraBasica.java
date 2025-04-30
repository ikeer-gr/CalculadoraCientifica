package com.mycompany.ikergomezddiej1ex;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.function.Function;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class CalculadoraBasica extends JFrame implements ActionListener {

    // Definir las variables para el campo de texto y los operandos
    private JTextField ventanaResultado;
    private String operador;
    private String operando1 = "";  // Primer operando
    private String operando2 = "";  // Segundo operando
    private boolean botonEstilo = true;
    private boolean modo = true;

    private double acumulador = 0; // Variable para guardar el resultado acumulado
    private boolean nuevoOperador = false; // Bandera para manejar operaciones encadenadas

    // Constructor de la clase CalculadoraBasica
    public CalculadoraBasica() {
        modoClasico();
        operador = null;  // Inicializar el operador como null
    }

    // Método principal para ejecutar la aplicación
    public static void main(String[] args) {
        new CalculadoraBasica();  // Crear una nueva instancia de la calculadora
    }

    // Método que se ejecuta cada vez que se hace clic en un botón
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        try {
            manejarEntrada(comando); // Procesa la entrada
        } catch (Exception ex) {
            ventanaResultado.setText("ERROR ACTION PERFORMED");
        }
    }

    private void manejarEntrada(String entrada) {
        try {
            if ("0123456789".contains(entrada)) {
                if (nuevoOperador) {
                    ventanaResultado.setText(""); // Limpiar pantalla si es un nuevo operador
                    nuevoOperador = false;
                }
                String textoActual = ventanaResultado.getText();
                ventanaResultado.setText(textoActual.equals("0") ? entrada : textoActual + entrada);
            } else if ("+-x/".contains(entrada)) {
                manejarOperador(entrada);
            } else if ("=".equals(entrada)) {
                calcularResultado();
            } else if ("C".equals(entrada)) {
                limpiar();
            } else if ("⌫".equals(entrada)) {
                borrar();
            } else if ("+/-".equals(entrada)) {
                cambiarSigno();
            } else if ("π".equals(entrada)) {
                manejarValorConstante(Math.PI);
            } else if ("ln".equals(entrada)) {
                manejarOperacionCientifica(Math::log); // Logaritmo natural
            } else if ("√".equals(entrada)) {
                manejarOperacionCientifica(Math::sqrt); // Raíz cuadrada
            } else if ("x²".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.pow(valor, 2)); // Cuadrado
            } else if ("x³".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.pow(valor, 3)); // Cubo
            } else if ("log".equals(entrada)) {
                manejarOperacionCientifica(Math::log10); // Logaritmo base 10
            } else if ("sen".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.sin(Math.toRadians(valor))); // Seno
            } else if ("cos".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.cos(Math.toRadians(valor))); // Coseno
            } else if ("tan".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.tan(Math.toRadians(valor))); // Tangente
            } else if ("sen-1".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.asin(Math.toRadians(valor))); // Arco seno
            } else if ("cos-1".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.acos(Math.toRadians(valor))); // Arco coseno
            } else if ("tan-1".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.atan(Math.toRadians(valor))); // Arco tangente
            } else if ("modoClasico".equals(entrada)) {
                modoClasico(); // Cambiar a modo clásico
                modo = true;
            } else if ("modoCientifico".equals(entrada)) {
                modoCientifico(); // Cambiar a modo científico
                modo = false;
            } else if ("☀".equals(entrada)) {
                botonEstilo = !botonEstilo;
                if (modo) {
                    modoClasico();
                } else {
                    modoCientifico();
                }
            } else if ("e".equals(entrada)) {
                manejarValorConstante(Math.E);
            } else if ("∛".equals(entrada)) {
                manejarOperacionCientifica(Math::cbrt);
            } else if ("ⁿ√".equals(entrada)) {
                while (true) {
                    try {
                        int n = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el índice de la raíz a calcular:", "Índice de la raíz", JOptionPane.QUESTION_MESSAGE));
                        if (n <= 0) {
                            JOptionPane.showMessageDialog(null, "El índice debe ser un número entero positivo.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        } else {
                            manejarOperacionCientifica(valor -> Math.pow(valor, 1.0 / n));
                            break;
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un número entero válido para n.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if ("Rad°".equals(entrada)) {
                manejarOperacionCientifica(valor -> Math.toDegrees(valor));
            } else {
//                ventanaResultado.setText("ERROR ERROR CONDICIONALES");
            }
        } catch (Exception ex) {
//            ventanaResultado.setText("ERROR MANEJAR ENTRADA");
        }
    }

    private void manejarOperacionCientifica(Function<Double, Double> operacion) {
        try {
            double valor = Double.parseDouble(ventanaResultado.getText());
            double resultado = operacion.apply(valor);

            // Actualizamos el acumulador si no hay operador pendiente
            if (operador == null) {
                acumulador = resultado;
            } else {
                // Si hay operador pendiente, lo usamos para el cálculo acumulativo
                acumulador = calcular(acumulador, operador, resultado);
                operador = null; // Limpia el operador después del cálculo
            }

            // Mostramos el resultado y preparamos para un nuevo operando
            ventanaResultado.setText(formato(acumulador));
            nuevoOperador = true; // Indica que el próximo número es nuevo

        } catch (NumberFormatException ex) {
            ventanaResultado.setText("ERROR MOPERACION CIENTIFICA");
        } catch (ArithmeticException ex) {
            ventanaResultado.setText("Math Error");
        }
    }

    private void manejarOperador(String operadorEntrante) {
        try {
            System.out.println(operadorEntrante);
            // Verificamos si hay un error o resultado vacío
            if (ventanaResultado.getText().isEmpty() || ventanaResultado.getText().equals("ERROR")) {
                return;
            }

            System.out.println(ventanaResultado.getText());
            double numeroActual = Double.parseDouble(ventanaResultado.getText());
            System.out.println(numeroActual);

            if (operador != null) {
                // Si ya hay un operador pendiente, realiza el cálculo acumulado
                acumulador = calcular(acumulador, operador, numeroActual);
            } else {
                // Si no hay operador pendiente, inicializa el acumulador
                acumulador = numeroActual;
            }

            // Actualizamos el operador actual y preparamos para el siguiente número
            operador = operadorEntrante;
            System.out.println("ACMU " + acumulador);
            ventanaResultado.setText(formato(acumulador));
            nuevoOperador = true; // Marca que el siguiente número será nuevo

        } catch (NumberFormatException ex) {
            ventanaResultado.setText("ERROR MANEJAR OPERADOR");
        }
    }

    private void limpiar() {
        acumulador = 0;
        operador = null;
        ventanaResultado.setText("0");
    }

    private void calcularResultado() {
        try {
            // Verificar si el campo de texto está vacío o contiene un valor inválido
            if (ventanaResultado.getText().isEmpty()) {
                acumulador = 0; // Si no hay nada en la pantalla, el resultado será 0
                ventanaResultado.setText("0");
                return;
            }

            double numeroActual = Double.parseDouble(ventanaResultado.getText());
            System.out.println("numero actual" + numeroActual);

            if (operador != null) {
                acumulador = calcular(acumulador, operador, numeroActual);
            } else {
                // Si no hay operador, el resultado es el número actual o 0 si no hay número válido
                acumulador = numeroActual != 0 ? numeroActual : 0;
            }
            ventanaResultado.setText(formato(acumulador));
            operador = null; // Limpia el operador
            nuevoOperador = true;

        } catch (NumberFormatException ex) {
            //ventanaResultado.setText("0"); // Si el texto no es un número, simplemente muestra 0
        } catch (ArithmeticException ex) {
            ventanaResultado.setText("Math Error");
        }
    }

    private void manejarValorConstante(double valor) {
        System.out.println("vlaaor pi " + valor);
        ventanaResultado.setText(formato(valor));

        if (operador != null) {
            calcular(acumulador, operador, valor);
        } else {
            // Si no hay operador, el resultado es el número actual o 0 si no hay número 
            acumulador = valor != 0 ? valor : 0;
        }
        nuevoOperador = true;
       
    }

    // Método para borrar el último carácter del operando actual
    public void borrar() {
        String actual = ventanaResultado.getText();

        if (actual.length() > 1) {
            // Eliminar el último carácter
            ventanaResultado.setText(actual.substring(0, actual.length() - 1));

            if (operador == null) {
                // Si no hay operador, actualizar operando1
                operando1 = ventanaResultado.getText();
                acumulador = Double.parseDouble(operando1);
            } else {
                // Si hay operador, actualizar operando2
                operando2 = ventanaResultado.getText();
            }
        } else {
            // Si sólo queda un carácter, resetear a "0"
            ventanaResultado.setText("0");

            if (operador == null) {
                operando1 = "0";
                acumulador = 0; // Resetear acumulador
            } else {
                operando2 = "0";
            }
        }
    }

    private void cambiarSigno() {
        try {
            double valor = Double.parseDouble(ventanaResultado.getText());
            ventanaResultado.setText(formato(valor * -1));
            System.out.println("Cambio de signo valor" + ventanaResultado.getText());
        } catch (NumberFormatException ex) {
            ventanaResultado.setText("ERROR");
        }
    }

    public void actualizador(double resultado) {
        if (operador == null) {
            acumulador = resultado; // Sin operador, el resultado es el acumulador
            operando1 = formato(resultado);
        } else {
            operando2 = formato(resultado); // Con operador, el resultado actualiza operando2
        }
    }

    private double calcular(double num1, String operador, double num2) {
        System.out.println("ENTRANDO EN CALCULAR");
        System.out.println("num1 " + num1);
        System.out.println("op " + operador);
        System.out.println("num2 " + num2);

        switch (operador) {
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "x":
                System.out.println("numero1 " + num1 + "numero2 " + num2);
                return num1 * num2;
            case "/":
                if (num2 == 0) {
                    throw new ArithmeticException("Division entre 0");
                }
                return num1 / num2;
            default:
                throw new IllegalArgumentException("Operador no válido: " + operador);
        }
    }

    public String formato(double numero) {
        // Convertir el número a una cadena para analizar los decimales
        String numeroStr = Double.toString(numero);
        int posicionPunto = numeroStr.indexOf('.');
        String resultado = String.format(Locale.US, "%.4f", numero);
        System.out.println("posicion punto" + posicionPunto);
        if (posicionPunto != -1) {
            // Contar la cantidad de decimales
            int numDecimales = numeroStr.length() - posicionPunto - 1;

            // Si tiene 4 o menos decimales, devolver el número como está
            if (numDecimales <= 4) {
                return numeroStr;
            }
        }
        // Si tiene más de 4 decimales, formatearlo a 4 decimales
        return resultado;
    }

    public void modoCientifico() {
        getContentPane().removeAll();
        // Crear un panel para los botones
        JPanel pBotones = new JPanel();
        pBotones.setLayout(new GridLayout(6, 6, 3, 3)); // Aumentar filas para más botones
        JButton botonGrande = new JButton("CAMBIAR A MODO CLÁSICO");
        botonGrande.setActionCommand("modoClasico"); // Comando único para identificar la acción
        botonGrande.addActionListener(this);
        JPanel panelBotonGrande = new JPanel(new BorderLayout());
        modoClaroInterfaz(pBotones, botonGrande, panelBotonGrande);

        // Definir los botones del modo científico
        String[] numeros = {
            "sen", "cos", "tan", "ln", "☀", "C",
            "sen-1", "cos-1", "tan-1", "x²", "π", "⌫",
            "7", "8", "9", "x³", "+/-", "ⁿ√",
            "4", "5", "6", "-", "√", "∛",
            "1", "2", "3", "+", "log", "e",
            "+/-", "0", "/", "x", "Rad°", "="
        };

        // Crear los botones
        for (String numero : numeros) {
            JButton boton = new JButton(numero);
            estiloClaroBoton(numero, boton);
            pBotones.add(boton);
        }

        // Redibujar el JFrame con los nuevos componentes
        setLayout(new BorderLayout());
        add(ventanaResultado, BorderLayout.NORTH);
        add(pBotones, BorderLayout.CENTER);
        add(panelBotonGrande, BorderLayout.SOUTH);
        // Configuración de la ventana principal
        setTitle("Calculadora de Iker");
        setSize(600, 800);  // Tamaño de la ventana
        setResizable(false);  // No permitir cambiar el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cerrar al presionar la X
        setVisible(true);  // Hacer visible la ventana
        setLocationRelativeTo(null); // Para hacer que la ventana se muestre en el medio al ejecutar el programa
        revalidate();
        repaint();
        operador = null;
        operando1 = "";
        operando2 = "";
    }

    private void estiloClaroBoton(String numero, JButton boton) {

        // Crear un panel para los botones
        JPanel pBotones = new JPanel();
        pBotones.setLayout(new GridLayout(6, 5, 3, 3)); // Aumentar filas para más botones
        pBotones.setBackground(claroOscuroFondo(botonEstilo));

        JButton botonGrande = new JButton("CAMBIAR A MODO CIENTÍFICO");
        botonGrande.setActionCommand("modoClasico"); // Comando único para identificar la acción
        botonGrande.addActionListener(this);
        JPanel panelBotonGrande = new JPanel(new BorderLayout());
        botonGrande.setBackground(claroOscuroFondo(botonEstilo));
        botonGrande.setForeground(claroOscuroLetra(botonEstilo));
        botonGrande.setBorder(BorderFactory.createLineBorder(claroOscuroFondo(botonEstilo), 6));
        panelBotonGrande.setFont(new Font("SansSerif", Font.PLAIN, 40));
        panelBotonGrande.add(botonGrande, BorderLayout.CENTER);

        boton.setFont(new Font("SansSerif", Font.BOLD, 24));  // Fuente de los botones
        boton.addActionListener(this);  // Asociar un ActionListener
        boton.setOpaque(true);  // Hacer el botón opaco
        boton.setBorderPainted(true);  // Pintar el borde
        boton.setBorder(new EmptyBorder(0, 0, 0, 0));  // Borde del botón

        String btnGray = "x-/+√sencostanlogIn^,lnπsen-1tan-1cos-1x³x²ⁿ√e∛Rad°";
        String btnRed = "C⌫";

        // Cambiar el color del botón dependiendo de su tipo
        if (botonEstilo) {
            if (btnGray.contains(numero) || numero.equals("+/-")) {
                boton.setBackground(new Color(173, 216, 230));  // Gris claro suave
                boton.setForeground(Color.BLACK);
                boton.setBorder(new LineBorder(Color.BLACK, 2, false));
                if (numero.equals("1")) {
                    boton.setBackground(Color.WHITE);
                    boton.setBorder(new LineBorder(Color.BLACK, 1, false));
                }
            } else if (btnRed.contains(numero)) {
                boton.setBackground(new Color(255, 51, 51));  // Rojo claro pastel
                boton.setForeground(Color.BLACK);
                boton.setBorder(new LineBorder(Color.BLACK, 2, false));
            } else if (numero.equals("=")) {
                boton.setBackground(new Color(102, 204, 102)); // Rojo claro pastel
                boton.setForeground(Color.BLACK);
                boton.setBorder(new LineBorder(Color.BLACK, 1, false));
            } else if (numero.equals("☀")) {
                boton.setBackground(claroOscuroFondo(botonEstilo)); // Rojo claro pastel
                boton.setForeground(claroOscuroLetra(botonEstilo));
                boton.setBorder(new LineBorder(claroOscuroLetra(botonEstilo), 1, false));
            } else {
                boton.setBackground(Color.WHITE); // Blanco suave con tono gris
                boton.setForeground(Color.BLACK);  // Texto gris oscuro
                boton.setBorder(new LineBorder(Color.BLACK, 1, false));
            }

        } else {
            if (btnGray.contains(numero) || numero.equals("+/-")) {
                boton.setBackground(new Color(169, 169, 169));  // Gris claro suave
                boton.setForeground(Color.BLACK);
                boton.setBorder(new LineBorder(Color.WHITE, 2, false));
                if (numero.equals("1")) {
                    boton.setBackground(Color.WHITE); // Blanco suave con tono gris
                    boton.setForeground(Color.BLACK);
                    boton.setBorder(new LineBorder(Color.WHITE, 2, false));
                }
            } else if (btnRed.contains(numero)) {
                boton.setBackground(new Color(255, 51, 51)); // Rojo claro pastel
                boton.setForeground(Color.WHITE);
                boton.setBorder(new LineBorder(Color.WHITE, 2, false));
            } else if (numero.equals("=")) {
                boton.setBackground(new Color(102, 204, 102)); // Rojo claro pastel
                boton.setForeground(Color.WHITE);
                boton.setBorder(new LineBorder(new Color(144, 238, 144), 2, false));
                boton.setBorder(new LineBorder(Color.WHITE, 2, true));
            } else if (numero.equals("☀")) {
                boton.setBackground(claroOscuroFondo(botonEstilo)); // Rojo claro pastel
                boton.setForeground(claroOscuroLetra(botonEstilo));
                boton.setBorder(new LineBorder(claroOscuroLetra(botonEstilo), 2, false));
                boton.setBorder(new LineBorder(Color.WHITE, 2, false));
            } else {
                boton.setBackground(Color.WHITE); // Blanco suave con tono gris
                boton.setForeground(Color.BLACK);  // Texto gris oscuro
                boton.setBorder(new LineBorder(Color.WHITE, 2, false));
            }
        }

    }

    private void modoClaroInterfaz(JPanel pBotones, JButton botonGrande, JPanel panelBotonGrande) {

        // Configuración inicial del campo de texto para mostrar el resultado
        ventanaResultado = new JTextField("0");
        ventanaResultado.setEditable(false);  // No se puede editar directamente
        ventanaResultado.setHorizontalAlignment(JTextField.RIGHT);  // Alinear a la derecha
        ventanaResultado.setPreferredSize(new Dimension(320, 100));  // Tamaño del campo de texto
        ventanaResultado.setFont(new Font("SansSerif", Font.PLAIN, 40));  // Fuente del texto
        ventanaResultado.setBackground(claroOscuroFondo(botonEstilo));
        ventanaResultado.setForeground(claroOscuroLetra(botonEstilo));
        //      ventanaResultado.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Bordes vacíos
        ventanaResultado.setBorder(BorderFactory.createLineBorder(claroOscuroFondo(botonEstilo), 10));

        // Crear un panel para los botones de la calculadora
        pBotones.setBackground(claroOscuroFondo(botonEstilo));
        pBotones.setBorder(BorderFactory.createLineBorder(claroOscuroFondo(botonEstilo), 10));

        botonGrande.setBackground(claroOscuroFondo(botonEstilo));
        botonGrande.setForeground(claroOscuroLetra(botonEstilo));
        botonGrande.setBorder(BorderFactory.createLineBorder(claroOscuroFondo(botonEstilo), 10));
        panelBotonGrande.setFont(new Font("SansSerif", Font.PLAIN, 40));
        panelBotonGrande.add(botonGrande, BorderLayout.CENTER);

    }

    private Color claroOscuroFondo(Boolean color) {
        if (color) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    private Color claroOscuroLetra(Boolean color) {
        if (color) {
            return Color.BLACK;
        } else {
            return Color.WHITE;
        }
    }

    public void modoClasico() {
        getContentPane().removeAll();
        JPanel pBotones = new JPanel();
        pBotones.setLayout(new GridLayout(5, 4, 3, 3));  // Crear una cuadrícula de botones
        JButton botonGrande = new JButton("CAMBIAR A MODO CIENTÍFICO");
        botonGrande.setActionCommand("modoCientifico"); // Comando único para identificar la acción
        botonGrande.addActionListener(this);
        JPanel panelBotonGrande = new JPanel(new BorderLayout());
        modoClaroInterfaz(pBotones, botonGrande, panelBotonGrande);

        // Definir los botones de la calculadora
        String[] numeros = {
            "☀", "√", "C", "⌫",
            "7", "8", "9", "x",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", "/", "="
        };

        // Crear botones e incluirlos en el panel
        for (String numero : numeros) {
            JButton boton = new JButton(numero);  // Crear un botón con el texto correspondiente
            estiloClaroBoton(numero, boton);
            pBotones.add(boton);  // Añadir el botón al panel
        }

        // Añadir el campo de texto y el panel de botones al JFrame
        setLayout(new BorderLayout());
        add(ventanaResultado, BorderLayout.NORTH);
        add(pBotones, BorderLayout.CENTER);
        add(panelBotonGrande, BorderLayout.SOUTH);

        // Configuración de la ventana principal
        setTitle("Calculadora de Iker");
        setSize(400, 600);  // Tamaño de la ventana
        setResizable(false);  // No permitir cambiar el tamaño de la ventana
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Cerrar al presionar la X
        setVisible(true);  // Hacer visible la ventana
        setLocationRelativeTo(null); // Para hacer que la ventana se muestre en el medio al ejecutar el programa
        revalidate();
        repaint();

        operador = null;
        operando1 = "";
        operando2 = "";
    }

}
