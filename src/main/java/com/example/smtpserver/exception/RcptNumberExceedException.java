package com.example.smtpserver.exception;

public class RcptNumberExceedException extends RuntimeException{

  public RcptNumberExceedException() {
    super("Error: rcpt number exceed");
  }
}
