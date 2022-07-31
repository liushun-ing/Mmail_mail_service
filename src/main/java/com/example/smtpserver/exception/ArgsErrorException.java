package com.example.smtpserver.exception;

public class ArgsErrorException extends RuntimeException {

  public ArgsErrorException() {
    super("Error: args error");
  }
}
