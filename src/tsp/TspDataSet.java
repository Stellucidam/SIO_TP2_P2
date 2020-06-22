package tsp;

import java.io.InputStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Class storing data of an instance of the TSP.
 */
public final class TspDataSet implements TspData {

    private final City[] cities;
    private final int[][] distanceMatrix;

    /**
     * Creates a new TspData instance from an InputStream containing cities' data.
     *
     * @param inputStream InputStream to read from.
     * @throws TspParsingException If inputStream content does not conform to expected format.
     * @throws OutOfMemoryError    If the number of cities is too large.
     */
    public TspDataSet(final InputStream inputStream) throws TspParsingException {

        // Checks that inputStream is open and not empty
        Scanner scanner = new Scanner(inputStream);
        try {
            if (!scanner.hasNext()) {
                throw new TspParsingException("Invalid data. Empty data.");
            }
        } catch (IllegalStateException e) {
            throw new TspParsingException("Invalid data. Unable to read data.");
        }

        // Reads the number of cities
        int numberOfCities;
        try {
            numberOfCities = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new TspParsingException("Invalid data value. Invalid number of cities in first line of data file.");
        } catch (NoSuchElementException e) {
            throw new TspParsingException("Invalid data format. Empty data file.");
        }
        if (numberOfCities < 3) {
            throw new TspParsingException("Invalid data value. Number of cities should be at least 3.");
        }

        // Allocates the array storing the XY coordinates of the cities
        try {
            cities = new City[numberOfCities];
        } catch (OutOfMemoryError e) {
            throw new OutOfMemoryError("Out of memory error. Number of cities is too large.");
        }

        // Reads the coordinates of each city
        for (int cityReadCount = 0; cityReadCount < numberOfCities; cityReadCount++) {
            try {
                int cityNumber = scanner.nextInt();
                if (cityNumber != cityReadCount) {
                    throw new TspParsingException(
                            String.format("Invalid city number: %s expected, %s read.", cityNumber, cityReadCount));
                }
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                cities[cityNumber] = new City(x, y);
            } catch (InputMismatchException e) {
                throw new TspParsingException("Invalid data value. City numbers and coordinates should be non negative integers.");
            } catch (NoSuchElementException e) {
                throw new TspParsingException(
                        "Incomplete line : should follow format \"<city number> <x> <y>\""
                );
            } catch (IllegalStateException e) {
                throw new TspParsingException("Invalid data. Unable to read data.");
            }
        }

        // Try to allocate the distance matrix between cities.
        // If not enough space is available, set distanceMatrix to null (distances will have to be recomputed
        // each time in getDistance(i,j)).
        int[][] tmpDistanceMatrix;
        try {
            tmpDistanceMatrix = new int[numberOfCities][numberOfCities];
        } catch (OutOfMemoryError e) {
            tmpDistanceMatrix = null;
        }
        distanceMatrix = tmpDistanceMatrix;
        if (distanceMatrix != null) {
            computeDistanceMatrix();
        }
    }

    /**
     * Returns the distance between two cities.
     *
     * @param i First city index.
     * @param j Second city index.
     * @return Distance between the two cities.
     * @throws IndexOutOfBoundsException If i or j are out of bounds.
     */
    @Override
    public int getDistance(int i, int j) {
        // Check for out of bounds indices
        if (i < 0 || i >= cities.length || j < 0 || j >= cities.length) {
            throw new IndexOutOfBoundsException("City index out of bounds.");
        }

        // If distanceMatrix was not allocated, computes distance from i to j.
        if (distanceMatrix == null) {
            return (int) Math.round(Math.hypot(cities[i].x - cities[j].x, cities[i].y - cities[j].y));
        } else {
            return distanceMatrix[i][j];
        }
    }

    /**
     * Computes distances between all cities and populates distanceMatrix attribute.
     */
    private void computeDistanceMatrix() {
        for (int i = 0; i < cities.length; i++) {
            distanceMatrix[i][i] = 0;
            for (int j = 0; j < i; j++) {
                distanceMatrix[i][j] = distanceMatrix[j][i] =
                        (int) Math.round(Math.hypot(cities[i].x - cities[j].x, cities[i].y - cities[j].y));
            }
        }
    }

    /**
     * Returns the number of cities of this problem instance.
     *
     * @return Number of cities.
     */
    @Override
    public final int getNumberOfCities() {
        return cities.length;
    }

    /**
     * Returns X coordinate of a city
     *
     * @param i City index
     * @return X coordinate of the city
     * @throws IndexOutOfBoundsException If i is out of bounds.
     */
    @Override
    public final int getXCoordinateForCity(int i) {
        // Check for out of bounds index
        if (i < 0 || i >= cities.length) {
            throw new IndexOutOfBoundsException("City index out of bounds.");
        }

        return cities[i].x;
    }

    /**
     * Returns Y coordinate of a city
     *
     * @param i City index
     * @return Y coordinate of the city
     * @throws IndexOutOfBoundsException If i is out of bounds.
     */
    @Override
    public final int getYCoordinateForCity(int i) {
        // Check for out of bounds index
        if (i < 0 || i >= cities.length) {
            throw new IndexOutOfBoundsException("City index out of bounds.");
        }

        return cities[i].y;
    }

    /**
     * Static nested class storing cities' XY coordinates
     */
    private static class City {
        private final int x;
        private final int y;

        private City(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
