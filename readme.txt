/* *****************************************************************************
 *  Name:     Kenar Vyas
 *  NetID:    kvyas
 *  Precept:  PO3A
 *
 *  Partner Name:    N/A
 *  Partner NetID:   N/A
 *  Partner Precept: N/A
 *
 *  Hours to complete assignment (optional):
 *
 **************************************************************************** */

Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

To find a vertical seam, I used a modified Dijkstra's shortest path algorithm
to find the shortest path from the top of the image to the bottom of the image.
The "weight" of all the edges(v to w) is the energy of the pixel w and each
pixel has edges pointing to the three pixels directly underneath it. The
algorithm starts by inserting all the pixels in the top row of the image
into the minPQ as if there were a supernode. Then the algorithm just
progresses like Dijkstra's algorithm until reaching a pixel in the bottom row
at which point it has finished.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

An image is suitable for the seam-carving approach if the relations between
its features are not that important (i.e. pictures of a landscape). However
images where the relations between features and ratios are important
don't work as well. An example of this would be an image of a face - or
pictures of humans in general.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 1000
 multiplicative factor (for H) = 3.16

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
200         .267
632         .679                2.54        .81
1997        1.752               2.58        .82
6310        5.779               3.298       1.03
19942       26.6                4.605       1.32


(keep H constant)
 H = 1000
 multiplicative factor (for W) = 3.55

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
150         .259
533.4       .596                2.3            .65
1896        1.702               2.86           .827
6744        6.04                3.5            .99
23984       31.972              5.29           1.31



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~  5.75*10^-5 * W^1.31 * H^1.32
       _______________________________________




/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe whatever help (if any) that you received.
 *  Don't include readings, lectures, and precepts, but do
 *  include any help from people (including course staff, lab TAs,
 *  classmates, and friends) and attribute them by name.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */


/* *****************************************************************************
 *  If you worked with a partner, assert below that you followed
 *  the protocol as described on the assignment page. Give one
 *  sentence explaining what each of you contributed.
 **************************************************************************** */


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
