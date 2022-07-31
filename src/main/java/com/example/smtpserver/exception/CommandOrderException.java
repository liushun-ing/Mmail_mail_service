package com.example.smtpserver.exception;

public class CommandOrderException extends RuntimeException{

  public CommandOrderException() {
    super("Error: command order error");
  }
}
