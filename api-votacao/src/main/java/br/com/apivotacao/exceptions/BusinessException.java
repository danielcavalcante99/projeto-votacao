package br.com.apivotacao.exceptions;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = -1925864414820105155L;

    public BusinessException(String msg) {
        super(msg);
    }
}
