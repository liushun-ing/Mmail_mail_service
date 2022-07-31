package com.example.smtpserver.exception;

public class EmailInvalidException extends RuntimeException{

  public EmailInvalidException() {
    super("Error: email address invalid");
  }
}
