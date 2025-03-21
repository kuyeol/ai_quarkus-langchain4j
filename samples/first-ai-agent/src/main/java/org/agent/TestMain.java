package org.agent;

import dev.langchain4j.data.message.UserMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TestMain
{
  public static Stack<UserMessage> stack = new Stack<UserMessage>();

  private final static int MAX_MEM = 10;

  public static void main(String[] args) {

    List<UserMessage> list         = new ArrayList<UserMessage>();

    for (int i = 0; i < 15; i++) {
      list.add(UserMessage.from("dd" + i));
    }

    for (UserMessage msg : list) {
      messageTemp(msg);
    }

    for (UserMessage msg : stack) {
      System.out.println(msg);
    }

  }


  private static void messageTemp(UserMessage message) {

    if (stack.size() == MAX_MEM) {
      stack.;
    } else {
      stack.push(message);
    }

  }
}
