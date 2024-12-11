package org.example.server.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String object, String name) {
        super(object + " with name " + name + " not found");
    }
}
