package com.example.smtpserver.exception;

public class UserNotFoundException extends RuntimeException{

  private static final String msg = "user not found";

  public UserNotFoundException() {
    super(msg);
  }
}
