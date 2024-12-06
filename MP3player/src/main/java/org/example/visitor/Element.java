package org.example.visitor;

import java.util.List;

public interface Element {
    List<String> accept(Visitor visitor);
}
