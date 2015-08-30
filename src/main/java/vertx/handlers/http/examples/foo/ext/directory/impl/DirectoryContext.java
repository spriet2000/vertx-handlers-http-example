package vertx.handlers.http.examples.foo.ext.directory.impl;

import vertx.handlers.http.examples.foo.ext.directory.Directory;

import java.util.ArrayList;
import java.util.List;

public class DirectoryContext implements Directory {

    private final List<String> contents = new ArrayList<>();

    @Override
    public List<String> contents() {
        return contents;
    }

}
