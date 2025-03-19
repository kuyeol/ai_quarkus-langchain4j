package org.agent.modelcatalog;

public enum GoogleModels
{
  GEMINI2_PRO( "gemini-2.0-pro-exp-02-05" ),

 GEMINI2_IMG("gemini-2.0-flash-exp-image-generation"),

  GEMMA3_27B( "gemma-3-27b-it" ),
  ;

  private final String modelCode;


  GoogleModels(String modelCode)
  {
    this.modelCode = modelCode;
  }


  public String getModelCode()
  {
    return modelCode;
  }
}
