package org.example.server.exception;

public class ResourceAlreadyExistsException extends RuntimeException{

    public ResourceAlreadyExistsException(String object, String name) {
        super(object + " with name " + name + " already exists");
    }


}
