package org.agent.modelcatalog.tool;

import dev.langchain4j.agent.tool.Tool;
import java.time.LocalDateTime;

public class MainExe
{
  @Tool("twosum")
  double add(int a, int b) {
    System.out.println(a+b);


    return a + b;
  }

  @Tool("현재 시간은")
  LocalDateTime time(){
    System.out.println(LocalDateTime.now());
    return LocalDateTime.now();
  }


  @Tool
  double squareRoot(double x) {
    double sqrt = Math.sqrt(x);
    System.out.println(x+" : "+sqrt);
    return sqrt;
  }

  @Tool("code execute")
  public static void main(String[] args)
  {

    System.out.println(args);
  }

}
