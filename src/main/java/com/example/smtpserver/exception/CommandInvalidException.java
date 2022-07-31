package com.example.smtpserver.exception;

public class CommandInvalidException extends RuntimeException{

  public CommandInvalidException() {
    super("Error: command invalid");
  }

}
