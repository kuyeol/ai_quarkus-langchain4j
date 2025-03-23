package org.agent;

import dev.langchain4j.data.message.UserMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TestMain
{
  public static List<UserMessage> msgQue = new LinkedList<UserMessage>();

  private final static int MAX_MEM = 10;

  public static void main(String[] args) {

    List<UserMessage> list = new ArrayList<UserMessage>();

    for (int i = 0; i < 15; i++) {
      list.add(UserMessage.from("dd" + i));
    }

    for (UserMessage msg : list) {
      messageTemp(msg);
    }

    for (UserMessage msg : msgQue) {
      System.out.println(msg);
    }

  }


  private static void messageTemp(UserMessage message) {

    if (msgQue.size() == MAX_MEM) {
      msgQue.remove(0);
      messageTemp(message);
    } else {
      msgQue.add(message);
    }

  }

}
