package org.example.server.visitor;

import java.util.List;

public interface Element {
    List<String> accept(Visitor visitor);
}
