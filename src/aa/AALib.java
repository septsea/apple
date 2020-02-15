package aa;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 * The library used for the ASCII art player.
 */
public class AALib {

    /**
     * Reads a txt file.
     * 
     * @param txtFile the path of the txt file.
     * @return the string of the file.
     */
    public static String txtReader(final String txtFile) {
        final StringBuffer sb = new StringBuffer();
        try {
            final BufferedReader br = new BufferedReader(new FileReader(txtFile));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
            br.close();
        } catch (final Exception e) {
        }
        return sb.toString();
    }

    /**
     * Writes a string.
     * 
     * @param txtFile the txt file to be written to.
     * @param str     the string to be written.
     * @param append  if {@code true}, then data will be written to the end of the
     *                file rather than the beginning.
     */
    public static void txtWriter(final String txtFile, final String str, final boolean append) {
        try {
            final BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile, append));
            bw.write(str);
            bw.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gives a string concentrated by {@code n} copies of {@code str}.
     * 
     * @param str the string to be multiplied.
     * @param n   the number of copies.
     * @return the concentrated string.
     */
    public static String stringMultiplication(final String str, final int n) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    /**
     * Gives the string representation of the progress bar.
     * 
     * @param ch    the character used to fill in the progress bar.
     * @param n     the length of the progress bar when the progress is fully
     *              completed.
     * @param ratio how much the progress is completed.
     * @return the string representation of the progress bar.
     */
    public static String progressBar(final char ch, final int n, final double ratio) {
        return AALib.stringMultiplication(String.valueOf(ch), (int) (n * ratio));
    }

    /**
     * Gives a copy of the Fira Code font ({@code media/fc.ttf}) of size
     * {@code size}.
     * 
     * @param size the specified size.
     * @return a copy of the Fira Code font of size {@code size}.
     */
    public static Font FiraCode(final double size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("media/fc.ttf")).deriveFont((float) size);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Resizes a {@code BufferedImage} object.
     * 
     * @param source  the source {@code BufferedImage} object.
     * @param targetW the width of the target.
     * @param targetH the height of the target.
     * @return a resized {@code BufferedImage} object.
     */
    public static BufferedImage resize(final BufferedImage source, int targetW, int targetH) {
        final int type = source.getType();
        BufferedImage target = null;
        double sx = (double) targetW / source.getWidth();
        double sy = (double) targetH / source.getHeight();
        // Resizes proportionally.
        // The size of the resized object will not exceed the target.
        if (sx > sy) {
            sx = sy;
            targetW = (int) (sx * source.getWidth());
        } else {
            sy = sx;
            targetH = (int) (sy * source.getHeight());
        }
        if (type == BufferedImage.TYPE_CUSTOM) {
            // handmade
            final ColorModel cm = source.getColorModel();
            final WritableRaster wr = cm.createCompatibleWritableRaster(targetW, targetH);
            final boolean ap = cm.isAlphaPremultiplied();
            target = new BufferedImage(cm, wr, ap, null);
        } else
            target = new BufferedImage(targetW, targetH, type);
        final Graphics2D g = target.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawRenderedImage(source, AffineTransform.getScaleInstance(sx, sy));
        g.dispose();
        return target;
    }

    /**
     * Creates an ascii string image of a {@code BufferedImage} object.
     * 
     * @param bi            the source {@code BufferedImage} object.
     * @param invertedColor whether to invert the color.
     * @return the ascii string image.
     */
    public static String createAsciiImg(final BufferedImage bi, final boolean invertedColor) {
        final String base = "MMMMMMM@@@@@@@WWWWWWWWWBBBBBBBB000000008888888ZZZZZZZZZaZaaaaaa2222222SSSSSSSXXXXXXXXXXX7777777rrrrrrr;;;;;;;;iiiiiiiii:::::::,:,,,,,,.........       ";
        final StringBuffer sb = new StringBuffer();
        for (int y = 0; y < bi.getHeight(); y += 2) {
            for (int x = 0; x < bi.getWidth(); x++) {
                final int pixel = bi.getRGB(x, y);
                final int r = (pixel & 0xff0000) >> 16;
                final int g = (pixel & 0xff00) >> 8;
                final int b = pixel & 0xff;
                final float gray = 0.299f * r + 0.587f * g + 0.114f * b;
                final int index = Math.round(gray * base.length() / 256);
                sb.append(String.valueOf(base.charAt(invertedColor ? base.length() - 1 - index : index)));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Creates an ascii string image of an image whose path is given.
     * 
     * @param img           the path of the image.
     * @param invertedColor whether to invert the color.
     * 
     * @return the ascii string image.
     */
    public static String createAsciiImg(final String img, final boolean invertedColor) {
        try {
            return AALib.createAsciiImg(ImageIO.read(new File(img)), invertedColor);
        } catch (final Exception e) {
            return "";
        }
    }

    /**
     * Resizes an image whose path is given and then creates an ascii string image
     * of it.
     * 
     * @param img           the path of the image.
     * @param targetW       the width of the target.
     * @param targetH       the height of the target.
     * @param invertedColor whether to invert the color.
     * @return the ascii string image.
     */
    public static String createAsciiImg(final String img, final int targetW, final int targetH,
            final boolean invertedColor) {
        try {
            return AALib.createAsciiImg(AALib.resize(ImageIO.read(new File(img)), targetW, targetH), invertedColor);
        } catch (final Exception e) {
            return "";
        }
    }

    /**
     * Plays a piece of music, with default volume = 20. If {@code music} is
     * {@code null}, it does nothing.
     * 
     * @param music the music file. It is supposed to be a {@code .wav} file.
     */
    public static void playMusic(final String music) {
        AALib.playMusic(music, 20);
    }

    /**
     * Plays a piece of music, with default volume = 20. If {@code music} is
     * {@code null}, it does nothing.
     * 
     * @param music  the music file. It is supposed to be a {@code .wav} file.
     * @param volume the volume, ranging from 0 to 100.
     */
    public static void playMusic(final String music, final int volume) {
        if (music != null) {
            try {
                final AudioInputStream ais = AudioSystem.getAudioInputStream(new File(music));
                final AudioFormat af = ais.getFormat();
                // System.out.println(af);
                final SourceDataLine sdl;
                final DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
                sdl = (SourceDataLine) AudioSystem.getLine(info);
                sdl.open(af);
                sdl.start();
                final FloatControl fc = (FloatControl) sdl.getControl(FloatControl.Type.MASTER_GAIN);
                // 0 <= volume <= 100; 0 <= vol <= 2
                double vol = 1e-4;
                if (0 < volume && volume <= 100) {
                    vol = 2.0 * volume / 100;
                } else if (volume <= 0) {
                    vol = 1e-4;
                } else {
                    vol = 2;
                }
                final float decibel = (float) (Math.log(vol) / Math.log(10.0) * 20.0);
                fc.setValue(decibel);
                int nByte = 0;
                final int SIZE = 1024 * 64;
                final byte[] buffer = new byte[SIZE];
                while ((nByte = ais.read(buffer, 0, SIZE)) != -1) {
                    sdl.write(buffer, 0, nByte);
                }
                sdl.stop();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Counts the number of txt files that {@code path} contains and that ends with
     * {@code .ext}.
     * 
     * @param path the path.
     * @param ext  the extension name without the leading dot.
     * @return the number of txt files.
     */
    public static int extCounter(final String path, final String ext) {
        return AALib.recursiveFiles(new File(path), ext).size();
    }

    /**
     * Lists all files that {@code obj} contains, subdirectories included.
     * 
     * @param obj a directory or a file.
     * @return an {@code ArrayList<File>} of those files.
     */
    public static ArrayList<File> recursiveFiles(final File obj) {
        return AALib.recursiveFiles(obj, (FileFilter) pathname -> true);
    }

    /**
     * Lists all files that {@code obj} contains and that meets the file filter
     * "files whose name ends with {@code .extensionName}", subdirectories included.
     * 
     * @param obj           a directory or a file.
     * @param extensionName the extension name without the leading dot.
     * @return an {@code ArrayList<File>} of those files.
     */
    public static ArrayList<File> recursiveFiles(final File obj, final String extensionName) {
        return AALib.recursiveFiles(obj, (FileFilter) pathname -> pathname.getName().endsWith("." + extensionName));
    }

    /**
     * Lists all files that {@code obj} contains and that meets the file filter
     * {@code ff}, subdirectories included.
     * 
     * @param obj a directory or a file.
     * @param ff  a file filter.
     * @return an {@code ArrayList<File>} of those files.
     */
    public static ArrayList<File> recursiveFiles(final File obj, final FileFilter ff) {
        final ArrayList<File> result = new ArrayList<>();
        if (obj.isFile()) {
            result.add(obj);
        } else if (obj.isDirectory()) {
            final File[] files = obj.listFiles((FileFilter) pathname -> pathname.isFile() && ff.accept(pathname));
            for (final File file : files) {
                result.add(file);
            }
            final File[] directories = obj.listFiles((FileFilter) pathname -> pathname.isDirectory());
            for (final File directory : directories) {
                result.addAll(AALib.recursiveFiles(directory, ff));
            }
        }
        return result;
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution)
     * for the specified number of milliseconds, subject to the precision and
     * accuracy of system timers and schedulers. The thread does not lose ownership
     * of any monitors. The method is introduced to avoid {@code throws}
     * declarations and {@code try/catch} statements.
     * 
     * @param millis the length of time to sleep in milliseconds.
     */
    public static void sleep(final long millis) {
        try {
            // There is approximately 1-millisecond delay.
            Thread.sleep(millis - 1);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution)
     * for the specified number of milliseconds plus the specified number of
     * nanoseconds, subject to the precision and accuracy of system timers and
     * schedulers. The thread does not lose ownership of any monitors. The method is
     * introduced to avoid {@code throws} declarations and {@code try/catch}
     * statements.
     * 
     * @param millis the length of time to sleep in milliseconds.
     * @param nanos  {@code 0-999999} additional nanoseconds to sleep.
     */
    public static void sleep(final long millis, final int nanos) {
        try {
            // There is approximately 1-millisecond delay.
            Thread.sleep(millis - 1, nanos);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * "Compiles" all images that are named numerally ({@code 1.*, 2.*, ...}, where
     * {@code *} is the extension name) in {@code media/img} to txt files (in
     * {@code txt_out}). A progress window will be displayed. Prior to the
     * compilation, the size will be altered.
     * 
     * @param imgWidth           the width of the target ASCII art image.
     * @param imgHeight          the height of the target ASCII art image.
     * @param imgBlackBackground whether to enable the mode where the background
     *                           color is black. If this is set {@code false}, the
     *                           background color will be white.
     * @param imgExt             the extension name without the leading dot. Only
     *                           one single extension name is allowed; that is, all
     *                           the image files must have the same extension name.
     */
    public static void imageCompiler(final int imgWidth, final int imgHeight, final boolean imgBlackBackground,
            final String imgExt) {
        final int num = AALib.extCounter("media/img", imgExt);
        final Window w = new Window("The Image Compiler", 750, 180, true);
        w.display(oas -> {
            final long startTime = System.currentTimeMillis();
            for (int i = 1; i <= num; i++) {
                AALib.txtWriter(String.format("txt_out/%d.txt", i), AALib.createAsciiImg(
                        String.format("media/img/%d.%s", i, imgExt), imgWidth, imgHeight, imgBlackBackground), false);
                w.jta.setText(String.format("Compiling media/img/%d.%s ...\n\n", i, imgExt)
                        + AALib.progressBar('#', 100, 1.0 * i / num) + "\n\n" + (int) (100.0 * i / num) + "% complete");
            }
            AALib.sleep(1000);
            w.jta.setText("Time elapsed (ms): " + (System.currentTimeMillis() - startTime)
                    + "\n\nThe compilation is finished and will exit in 3 seconds.");
            AALib.sleep(3000);
            System.exit(0);
        });
    }

}
