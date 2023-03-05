package org.project.server.exception.http;

public class MethodNotAllowedException extends HttpStatusException {
    public MethodNotAllowedException() {
        super(405, "Method not allowed");
    }
}
