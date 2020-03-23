package ch.epfl.rigel.astronomy;

import ch.epfl.rigel.coordinates.EquatorialCoordinates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public enum HygDatabaseLoader implements StarCatalogue.Loader {

    INSTANCE;


    /**
     * Loads from input stream the HYG Database to the builder star catalogue.
     * @param inputStream with file containing hyg database.
     * @param builder of the star catalogue.
     * @throws IOException
     */
    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

        InputStreamReader inStrReader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
        BufferedReader buffReader = new BufferedReader(inStrReader);
        //Skip the first line
        buffReader.readLine();


        //Star parameters;
        int hipparcosId;
        String name;
        EquatorialCoordinates eqCoord;
        float magnitude;
        float colorInd;


        String line = buffReader.readLine();
        String[] lineTable;
        while (line != null) {
            lineTable = line.split(",");

            hipparcosId = parseInteger(lineTable[COLUMNS.HIP.ordinal()], 0);
            name = parseStarName(lineTable[COLUMNS.PROPER.ordinal()], lineTable[COLUMNS.BAYER.ordinal()], lineTable[COLUMNS.CON.ordinal()]);
            magnitude = parseFloat(lineTable[COLUMNS.MAG.ordinal()], 0);
            colorInd = parseFloat(lineTable[COLUMNS.CI.ordinal()], 0);
            eqCoord = EquatorialCoordinates.of(parseDouble(lineTable[COLUMNS.RARAD.ordinal()]), parseDouble(lineTable[COLUMNS.DECRAD.ordinal()]));

            Star star = new Star(hipparcosId, name, eqCoord, magnitude, colorInd);

            builder.addStar(star);
            line = buffReader.readLine();
        }
    }

    /**
     * Get's star name if proper name exists. Otherwisse use bayer ( "?" default) and WHITESPACE
     * and Con.
     * @param properName database csv String corresponding to the proper name.
     * @param bayer database csv String corresponding to the bayer.
     * @param con database csv String corresponding to the con column.
     * @return the star name.
     */
    private static String parseStarName(String properName, String bayer, String con) {

        //Check if proper name is non empty.
        if (!properName.isEmpty()) {
            if (properName.equals("Rigel")) {
                System.out.println("Heu");
            }
            return properName;
        } else if (!bayer.isEmpty()) {
            return (bayer + " " + con);
        }
        return ("?" + " " + con);

    }


    /**
     * Parse's a string to integer. If string is empty, return default parameter.
     * @param s string to be parsed.
     * @param def default value.
     * @return parsed string to integer or default value.
     */
    private static int parseInteger(String s, int def) {

        if (!s.isEmpty()) {
            return Integer.parseInt(s);
        }
        return def;

    }

    /**
     * Parses a string to double.
     * @param s
     * @return
     */
    private static double parseDouble(String s) {
        return Double.parseDouble(s);
    }


    /**
     * Parse's a string to flaot. If string is empty, return default parameter.
     * @param s string to be parsed.
     * @param def default value.
     * @return parsed string to float or default value.
     */
    private static float parseFloat(String s, float def) {

        if (!s.isEmpty()) {
            return (float) Double.parseDouble(s);
        }
        return def;

    }


    /**
     * Columns indices. Intended to be used with .ordinal() giving the correpsonding index.
     */
    public enum COLUMNS {
        ID, HIP, HD, HR, GL, BF, PROPER, RA, DEC, DIST, PMRA, PMDEC,
        RV, MAG, ABSMAG, SPECT, CI, X, Y, Z, VX, VY, VZ,
        RARAD, DECRAD, PMRARAD, PMDECRAD, BAYER, FLAM, CON,
        COMP, COMP_PRIMARY, BASE, LUM, VAR, VAR_MIN, VAR_MAX;
    }


}


