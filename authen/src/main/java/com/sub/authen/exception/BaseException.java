package com.sub.authen.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 128763123L;

    private int status = 0;
    private String code = "";
    private String message = "";
}