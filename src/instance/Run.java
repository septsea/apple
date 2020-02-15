package instance;

import java.util.Timer;
import java.util.TimerTask;

import aa.AALib;
import aa.Window;

/**
 * The class used to display the ASCII art player.
 */
public class Run {

    public static void main(final String... args) {
        final String title = "Bad Apple";
        final int width = 1150;
        final int height = 1040;
        final boolean invertedColor = true;
        final Window w = new Window(title, width, height, invertedColor);
        final int num = AALib.extCounter("txt_out", "txt");
        // If no music is needed, simply set music to be null; that is,
        // final String music = null;
        final String music = "media/music.wav";
        w.display(oas -> {
            // Pre-loads the txt files.
            // If the txt files are too large, do not pre-load them.
            w.jta.setText("Loading...");
            final String[] tmpStrings = new String[num];
            final String spaces = AALib.stringMultiplication(" ", 64);
            for (int i = 1; i <= num; i++) {
                w.jta.setText(String.format("Loading txt_out/%d.txt ...\n\n", i)
                        + AALib.progressBar('#', 150, 1.0 * i / num) + "\n\n" + (int) (100.0 * i / num) + "% complete");
                tmpStrings[i - 1] = AALib.txtReader(String.format("txt_out/%d.txt", i));
            }
            AALib.sleep(1000);
            w.jta.setText("This is the Bad Apple ASCII Art Player.\nIt will start in 3 seconds.\nAre you ready?");
            AALib.sleep(3000);
            final long startTime = System.currentTimeMillis();
            final Timer timer = new Timer();
            new Thread(() -> AALib.playMusic(music)).start();
            // Period (unit: second, which is 1 over "frames per second")
            final double period = 1.0 / 30;
            timer.schedule(new TimerTask() {
                private int i = 1;

                @Override
                public void run() {
                    if (i <= num) {
                        w.jta.setText(tmpStrings[i - 1] + "\nFrame: " + i + spaces + "Time (ms): "
                                + (System.currentTimeMillis() - startTime));
                        i++;
                    } else {
                        w.jta.setText("Time elapsed (ms): " + (System.currentTimeMillis() - startTime)
                                + "\n\nThanks for watching!\nThe player will exit in 3 seconds.");
                        AALib.sleep(3000);
                        System.exit(0);
                    }
                }

            }, 0, (long) (period * 1000));
        });
    }

}
