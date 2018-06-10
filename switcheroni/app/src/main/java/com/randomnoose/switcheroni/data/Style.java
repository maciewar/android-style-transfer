package com.randomnoose.switcheroni.data;

public enum Style {
  MINIMALISM("Minimalism"),
  IMPRESSIONISM("Impressionism"),
  POPART("Popart"),
  BRUTALISM("Brutalism"),
  CUBISM("Cubism"),
  EXPRESSIONISM("Expressionism"),
  DEFAULT("Default");

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
