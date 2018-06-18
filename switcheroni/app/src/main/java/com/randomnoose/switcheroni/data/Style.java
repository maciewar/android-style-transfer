package com.randomnoose.switcheroni.data;

public enum Style {
  MINIMALISM("minimalism"),
  IMPRESSIONISM("impressionism"),
  POPART("popart"),
  BRUTALISM("brutalism"),
  CUBISM("cubism"),
  EXPRESSIONISM("expressionism"),
  DEFAULT("default");

  private final String type;

  Style(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public static Style getByType(String type) {
    for (Style style : values()) {
      if (style.type.equals(type)) {
        return style;
      }
    }
    return DEFAULT;
  }
}
