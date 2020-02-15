package instance;

import aa.AALib;

/**
 * The class used to "compile" images.
 */
public class Compiler {

    public static void main(final String... args) {
        final int imgWidth = 160;
        final int imgHeight = 120;
        final boolean imgBlackBackground = true;
        final String imgExt = "png";
        AALib.imageCompiler(imgWidth, imgHeight, imgBlackBackground, imgExt);
    }

}
