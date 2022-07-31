package com.example.smtpserver.exception;

public class IndexExceedException extends RuntimeException{

  public IndexExceedException() {
    super("Error: index exceed");
  }
}
