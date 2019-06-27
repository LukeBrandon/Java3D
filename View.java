import java.awt.Canvas;
import javax.swing.JFrame;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;

public class View extends Canvas implements Runnable {
    public static final int WIDTH = 1500;
    public static final int HEIGHT = 2000;
    public static final String TITLE = "Minecraft-Clone";

    private Thread thread;
    private boolean running = false;
    private BufferedImage image;
    private Screen screen;
    private int pixels[];

    public View() {
        screen = new Screen(WIDTH, HEIGHT);
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
        System.out.println("Thread has started.");
    }

    public void stop() {
        if (!running)
            return;
        running = false;

        try {
            thread.join(); // end thread
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0); // exit program
        }
    }

    public void run() {
        while (running) {
            tick();
            render();
        }
    }

    private void tick() {
        System.out.println("Tick");

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render();

        for (int i = 0; i < WIDTH * HEIGHT - 1; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();

    }

    public static void main(String[] args) {
        View game = new View();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // clicking exit stops the code
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setSize(HEIGHT, WIDTH);
        frame.setLocationRelativeTo(null); // puts in the center of screen

        game.start();
    }
}