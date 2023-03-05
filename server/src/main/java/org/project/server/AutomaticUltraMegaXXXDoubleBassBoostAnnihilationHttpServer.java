package org.project.server;

import com.sun.net.httpserver.HttpServer;
import org.project.server.exception.handler.DefaultHandlers;
import org.project.server.exception.handler.ExceptionHandler;
import org.project.server.exception.handler.ExceptionHandlerStore;
import org.project.server.handler.RouteHandler;
import org.project.server.utils.AnnotationUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AutomaticUltraMegaXXXDoubleBassBoostAnnihilationHttpServer {
    private final HttpServer httpServer;

    private final List<RouteHandler<?>> controllers = new ArrayList<>();

    private final ExceptionHandlerStore exceptionHandlerStore = new ExceptionHandlerStore();

    public AutomaticUltraMegaXXXDoubleBassBoostAnnihilationHttpServer(int port, String host)
            throws IOException {
        this.httpServer = HttpServer.create(new InetSocketAddress(host, port), 0);

        initExceptionHandlerStore();
    }

    private void initExceptionHandlerStore() {
        DefaultHandlers.attachBaseHandlers(exceptionHandlerStore);

        exceptionHandlerStore.setDefaultHandler(t -> {
            t.printStackTrace(System.err);
            return null;
        });
    }

    public <E extends Throwable>
    void addExceptionHandler(Class<E> exceptionClass, ExceptionHandler<E> exceptionHandler) {
        this.exceptionHandlerStore.attachHandler(exceptionClass, exceptionHandler);
    }

    public <T extends HttpController> void registerController(T controller) {
        String baseUrl = Objects.requireNonNullElse(AnnotationUtils.getRoute(controller.getClass()), "/");

        RouteHandler<T> handler = new RouteHandler<>(baseUrl, controller);
        handler.setExceptionHandlerStore(this.exceptionHandlerStore);

        this.controllers.add(handler);

        this.httpServer.createContext(baseUrl, handler);
    }

    public void start() {
        httpServer.start();
    }

    public void stop(int delay) {
        httpServer.stop(delay);
    }
}
