package ch.epfl.rigel.astronomy;

import java.io.IOException;
import java.io.InputStream;

public enum HygDatabaseLoader implements StarCatalogue.Loader{

    INSTANCE;

    @Override
    public void load(InputStream inputStream, StarCatalogue.Builder builder) throws IOException {

    }
}
