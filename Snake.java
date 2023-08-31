
//Clases importadas y usadas
import java.awt.Color;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Snake extends JFrame {
    public static Snake s;
    //Inicializacion de varibales
    int width = 596;
    int height = 399;
    int scoreInicial = 15;
    Point snake;
    Point comida;
    ArrayList<Point> lista;
    int widthPoint = 10, heightPoint = 10;
    ImagenSnake imagenSnake;
    int direccion = KeyEvent.VK_LEFT;
    long frecuencia = 100L;
    boolean GameOver = false;
    int Score;
    boolean Pausa = false;
    Random rnd;
    int r, v, b;
    int velocidad = 10;
    int bScore, lScore;

    //Constructor
    public Snake() {
        setTitle("Snake");
        setSize(width, height);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - width / 2, dim.height / 2 - height / 2);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Teclas teclas = new Teclas();
        this.addKeyListener(teclas);
        setResizable(false);
        StartGame();
        imagenSnake = new ImagenSnake();
        this.getContentPane().add(imagenSnake);
        setVisible(true);
        Momento momento = new Momento();
        Thread trid = new Thread(momento);
        trid.start();
        rnd = new Random();
        ShowScore();
    }

    //Este metodo sirve para mostrar los scores en pantalla desde la base de datos
    public void ShowScore() {
     /*
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_viborita", "root", "");
            PreparedStatement ps = cn.prepareStatement("select bscore, lscore from score where ID = '1'");
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                bScore = rs.getInt("bscore");
                lScore = rs.getInt("lscore");
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Debes encender la base de datos o verifica que no haya problemas");
        }*/
    }

    //Este metodo hace  que el juego inicie
    public void StartGame() {
        comida = new Point(200, 200);
        snake = new Point(200, 200);
        lista = new ArrayList<>();
        lista.add(snake);
        CrearComida();
    }

    //Este metodo crea la comida
    public void CrearComida() {
        Random rnd = new Random();
        comida.x = rnd.nextInt(width - 26);
        if ((comida.x % velocidad) > 0) {
            comida.x = comida.x - (comida.x % 10);
        }
        if (comida.x < 10) {
            comida.x = comida.x + 20;
        }
        comida.y = rnd.nextInt(height - 39);
        if ((comida.y % velocidad) > 0) {
            comida.y = comida.y - (comida.y % 10);
        }
        if (comida.y < 10) {
            comida.y = comida.y + 20;
        }
    }

    //Metodo para reiniciar el juego
    public void ReiniciarJuego() {
        GameOver = false;
        s.dispose();
        s = new Snake();
        UpdateScore();
        ShowScore();

        s.setVisible(true);
        s.StartGame();
    }

    //Metodo main
    public static void main(String[] args) {
        s = new Snake();
    }


    private void CambiarTema() {
        /*
        if(modoD == true){
            getContentPane().setBackground(Color.WHITE);
        } else if(modoN == true){
            getContentPane().setBackground(Color.BLACK);
        }
        */
    }
    //Este metodo es para actualizar la puntuación haciendo una conexion a base de datos
    public void UpdateScore() {
        /*
        try {
            Connection cn = DriverManager.getConnection("jdbc:mysql://localhost/bd_viborita", "root", "");
            PreparedStatement ps = cn.prepareStatement("update score set bscore=?, lscore=? where ID = '1'");
            ps.setInt(1, bScore);h
            ps.setInt(2, Score);
            ps.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            System.err.println("ERROR AL INTENTAR ACTUALIZAR LA PUNTUACION " + e);
        }
        if (Score > bScore) {
            try {
                Connection cn2 = DriverManager.getConnection("jdbc:mysql://localhost/bd_viborita", "root", "");
                PreparedStatement ps2 = cn2.prepareStatement("update score set bscore=?, lscore=? where ID = '1'");
                ps2.setInt(1, Score);
                ps2.setInt(2, Score);
                ps2.executeUpdate();
                cn2.close();
            } catch (SQLException e) {
                System.err.println("ERROR AL INTENTAR ACTUALIZAR LA PUNTUACION MAS ALTA " + e);
            }
        }*/
    }

    //Este metodo repinta la viborita y valida si colisionas contigo mismo con un if
    public void Actualizar() {
        imagenSnake.repaint();
        lista.add(0, new Point(snake.x, snake.y));
        lista.remove(lista.size() - 1);

        for (int i = 1; i < lista.size(); i++) {
            Point point = lista.get(i);
            if (snake.x == point.x && snake.y == point.y) {
                GameOver = true;
                UpdateScore();
            }
        }
        if (Score < scoreInicial){
            for (int i = 0; i < scoreInicial; i++) {
                lista.add(0, new Point(snake.x, snake.y));
                Score++;
            }
        }

        //Esta condicional valida si pasaste por las cordenadas de la comida y aumenta el score
        if ((snake.x > (comida.x - 10)) && (snake.x < (comida.x + 10))
                && (snake.y > (comida.y - 10)) && (snake.y < (comida.y + 10))) {
            lista.add(0, new Point(snake.x, snake.y));
            Score++;
            CrearComida();
        }
        //Esta condicional te avisa si pierdes
        if (GameOver) {
            JOptionPane.showMessageDialog(null, "Fin del juego\nTu puntuación fue " + Score);
        }

        //Esta condicional te avisa si ganaste
        if (Score == 1960) {
            Pausa = true;
            JOptionPane.showMessageDialog(null, "Fin del juego\nGanaste!!!!");
            ReiniciarJuego();
        }
    }

    //Esta clase contiene al metodo para crear la parte grafica como la viborita y la comida
    public class ImagenSnake extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(new Color(0, 0, 255));
            g.fillRect(snake.x, snake.y, widthPoint, heightPoint);
            //Este bucle crea mas cuadritos para hacer crecer la cola
            for (int i = 0; i < lista.size(); i++) {
                 /*r = rnd.nextInt(255);
                   v = rnd.nextInt(255);
                   b = rnd.nextInt(255);
                 //Esto hace que el color rgb sea aleatorio
                 */
                Point point = lista.get(i);
                //g.setColor(new Color(r,v,b)); //ESTE ES PARA CAMBIAR EL COLOR DE LA VIBORITA A ARCOIRIS
                g.fillRect(point.x, point.y, widthPoint, heightPoint);
            }
            //Esto hace que se cree la primer comida
            g.setColor(new Color(255, 0, 0));
            g.fillRect(comida.x, comida.y, widthPoint, heightPoint);

            g.setColor(Color.BLACK);
            g.drawString("Mejor puntuación: " + bScore, 0, 11);
            g.drawString("Última puntuación: " + lScore, 0, 25);
            g.drawString("Score: " + Score, 0, 38);
        }
    }

    /*Clase con metodos para responder a las teclas pulsadas
     *(con KeyAdapter no necesitas importar todos los metodos abstractos)
     */
    public class Teclas extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {

            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                //Condicional para salir
                System.exit(0);
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                Pausa = true;
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                Pausa = false;
            } else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyChar() == 'w' || e.getKeyChar() == 'W') {
                //Condicional movimiento hacia arriba
                if (direccion != KeyEvent.VK_DOWN) {
                    direccion = KeyEvent.VK_UP;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyChar() == 's' || e.getKeyChar() == 'S') {
                //Condicional movimiento hacia abajo
                if (direccion != KeyEvent.VK_UP) {
                    direccion = KeyEvent.VK_DOWN;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyChar() == 'a' || e.getKeyChar() == 'A') {
                //Condicional movimiento hacia la izquierda
                if (direccion != KeyEvent.VK_RIGHT) {
                    direccion = KeyEvent.VK_LEFT;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyChar() == 'd' || e.getKeyChar() == 'D') {
                //Condicional movimiento hacia la derecha
                if (direccion != KeyEvent.VK_LEFT) {
                    direccion = KeyEvent.VK_RIGHT;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                ReiniciarJuego();
            }
        }
    }

    //Clase que hereda a Thread para hacer el hilo y establecer el movimiento
    public class Momento extends Thread {
        long last = 0;

        public void run() {
            //Este primer bucle hace que jamas se detenga
            while (true) {
                //Este bucle hace que sea posible perder o poner pausa
                //bajo la condicion que tiene entre parentesis
                while (!GameOver && !Pausa) {
                    //Esta condicional define la frecuencia de pixeles por milisegundo
                    if ((java.lang.System.currentTimeMillis() - last) > frecuencia) {
                        //System.out.println("Y: " + snake.y);
                        //System.out.println("X: " + snake.x);
                        if (direccion == KeyEvent.VK_UP) {
                            //Condicional para ir hacia arriba
                            snake.y = snake.y - velocidad;
                            if (snake.y >= height - 49) {
                                snake.y = -10;
                            }
                            if (snake.y <= -10) {
                                snake.y = height - 49;
                            }
                        } else if (direccion == KeyEvent.VK_DOWN) {
                            //Condicional para ir hacia abajo
                            snake.y = snake.y + velocidad;
                            if (snake.y >= height - 39) {
                                snake.y = 0;
                            }
                            if (snake.y <= -10) {
                                snake.y = height - 49;
                            }
                        } else if (direccion == KeyEvent.VK_LEFT) {
                            //Condicional para ir hacia la izquierda
                            snake.x = snake.x - velocidad;

                            //Estas condicionales validan que no se pueda bajar si se esta subiendo, etc
                            if (snake.x > width - 26) {
                                snake.x = 0;
                            }
                            if (snake.x < 0) {
                                snake.x = width - 26;
                            }
                        } else if (direccion == KeyEvent.VK_RIGHT) {
                            //Condicional para ir hacia la derecha
                            snake.x = snake.x + velocidad;

                            //Estas condicionales validan que no se pueda bajar si se esta subiendo, etc
                            if (snake.x > width - 26) {
                                snake.x = 0;
                            }
                            if (snake.x < 0) {
                                snake.x = width - 26;
                            }
                        }
                        //llama al metodo actualizar y actualiza el valor de la variable last
                        last = java.lang.System.currentTimeMillis();
                        Actualizar();
                    }
                }
            }
        }
    }

    /*
    public class CambiarTema extends Thread {

        Snake p = new Snake();

        public void run() {
            while (true) {

            }
        }
    }
    */
}