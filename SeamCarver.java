/* *****************************************************************************
 *  Name:    Kenar Vyas
 *  NetID:   kvyas
 *  Precept: P03A
 *
 *
 *  Description:  Finds seams in image by using energy gradient function
 * and removes seams in order to resize image.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stopwatch;

import java.awt.Color;

public class SeamCarver {

    // instance variable holding picture
    private Picture pic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Bad Argument");
        }
        pic = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        int width = width();
        int height = height();

        // exception handling
        validator(x, y);

        int left;
        int right;
        int top;
        int bot;

        // checks for cases of border pixels and then assigns neighboring pixel
        // values
        if (x - 1 == -1) left = width - 1;
        else left = x - 1;

        if (x + 1 == width) right = 0;
        else right = x + 1;

        if (y - 1 == -1) top = height - 1;
        else top = y - 1;

        if (y + 1 == height) bot = 0;
        else bot = y + 1;

        // calculates actual value of energy using formula
        Color leftc = new Color(pic.getRGB(left, y));
        Color rightc = new Color(pic.getRGB(right, y));
        Color topc = new Color(pic.getRGB(x, top));
        Color botc = new Color(pic.getRGB(x, bot));
        double deltx = Math.pow(leftc.getRed() - rightc.getRed(), 2)
                + Math.pow((leftc.getBlue() - rightc.getBlue()), 2)
                + Math.pow(leftc.getGreen() - rightc.getGreen(), 2);
        double delty = Math.pow(topc.getRed() - botc.getRed(), 2) +
                Math.pow(topc.getBlue() - botc.getBlue(), 2) +
                Math.pow(topc.getGreen() - botc.getGreen(), 2);
        return Math.sqrt(deltx + delty);
    }

    //  helper method that checks validity of arguments
    private void validator(int col, int row) {
        if (col < 0 || row < 0 || col >= width() || row >= height()) {
            throw new IllegalArgumentException("Bad argument");
        }
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;

    }

    // helper method that transposes picture
    private void transpose() {
        int height = height();
        int width = width();
        Picture pic2 = new Picture(height, width);
        // making columns rows
        for (int col = 0; col < width; col++) {
            for (int row = 0; row < height; row++) {
                pic2.setRGB(row, col, pic.getRGB(col, row));
            }
        }
        // copying over to instance variable
        pic = pic2;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int height = pic.height();
        int width = pic.width();
        // for energy values of all pixels
        double[][] energy = new double[height][width];
        // for use in shortest path algorithm
        double[][] distTo = new double[height][width];
        int[][] edgeTo = new int[height][width];

        // assigning energy value and distTo to all pixels
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[row][col] = energy(col, row);
                distTo[row][col] = Double.POSITIVE_INFINITY;
            }
        }


        // Finding min path from top to bottom of image
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(height * width);
        // creating "super node" by adding all of top row to minPQ
        for (int col = 0; col < width; col++) {
            pq.insert(col, energy[0][col]);
            distTo[0][col] = energy[0][col];
        }
        int champ = 0;
        // Finds shortest path using Dijsktra's SPA
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            // converting index value to row/col
            int row = v / width;
            int col = v % width;
            // breaks from loop if it reaches bottom row - as it has found
            // shortest path
            if (row == height - 1) {
                champ = v;
                break;
            }
            for (int i = -1; i < 2; i++) {
                // updating shortest path
                if (col + i >= 0 && col + i < width && distTo[row + 1][col + i]
                        > distTo[row][col] + energy[row + 1][col + i]) {
                    distTo[row + 1][col + i] = distTo[row][col]
                            + energy[row + 1][col + i];
                    edgeTo[row + 1][col + i] = v;
                    int pos = (row + 1) * width + (col + i);
                    if (pq.contains(pos)) {
                        pq.decreaseKey(pos, distTo[row + 1][col + i]);
                    }
                    else {
                        pq.insert(pos, distTo[row + 1][col + i]);
                    }
                }
            }
        }

        // returns sequence of column indices of seam path
        int[] seq = new int[height];
        int v = champ;
        for (int i = height - 1; i >= 0; i--) {
            // converting index value to row/col
            int row = v / width;
            int col = v % width;
            // adding to sequence
            seq[i] = v % width;
            // moving up path one edge
            v = edgeTo[row][col];
        }
        return seq;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        int width = width();
        int height = height();
        // exception handling
        if (width == 1 || seam == null || seam.length != height) {
            throw new IllegalArgumentException("Bad argument");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= width) {
                throw new IllegalArgumentException("Bad argument");
            }
            else if (i != 0 && Math.abs(seam[i - 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("Bad argument");
            }
        }


        Picture pic2 = new Picture(width - 1, height);
        // copying over pixels into new Picture object, unless pixel is in
        // seam array. Goes down row by row.
        for (int row = 0; row < height; row++) {
            int del = seam[row];
            int col;
            for (col = 0; col < del; col++) {
                pic2.setRGB(col, row, pic.getRGB(col, row));
            }
            // skips over deleted pixel
            for (; col < width - 1; col++) {
                pic2.setRGB(col, row, pic.getRGB(col + 1, row));
            }
        }
        // copies back to pic instance variable
        pic = pic2;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        /*
        Picture p = new Picture(args[0]);
        SeamCarver sc = new SeamCarver(p);

        sc.picture().show();
        System.out.println(sc.width());
        System.out.println(sc.height());
        System.out.println(sc.energy(0, 0));
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
        }
        */
        Picture p = new Picture(
                SCUtility.randomPicture(Integer.parseInt(args[0]), Integer.parseInt(args[1])));
        SeamCarver sc = new SeamCarver(p);

        Stopwatch stop = new Stopwatch();
        sc.removeVerticalSeam(sc.findVerticalSeam());
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        System.out.println(stop.elapsedTime());


    }


}
