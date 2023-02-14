package br.com.microservicovotacao.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1925864414820105155L;

    public ResourceNotFoundException(String msg) {
        super(msg);
    }
}
