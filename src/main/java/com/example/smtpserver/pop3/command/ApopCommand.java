package com.example.smtpserver.pop3.command;

import com.example.smtpserver.pop3.server.Pop3Data;

public class ApopCommand extends BaseCommand{

  public ApopCommand() {
    super("apop");
  }

  @Override
  public void handle(Pop3Data pop3Data, String commandString) throws Exception {

  }
}
