package com.randomnoose.switcheroni.data;

import com.randomnoose.switcheroni.R;

public enum Style {
  EXPRESSIONISM("expressionism", R.drawable.style_01_expressionism),
  MODERN_ART("modernArt", R.drawable.style_02_modernart),
  INDIAN_ART("indianArt", R.drawable.style_03_indianart),
  THE_SCREAM("theScream", R.drawable.style_04_scream),
  VAN_GOGH("vanGogh", R.drawable.style_05_vangogh),
  BRUEGL("bruegl", R.drawable.style_06_bruegl),
  DEFAULT("default", R.drawable.style_00_default);

  private final String type;
  private final int styleId;

  Style(String type, int styleId) {
    this.type = type;
    this.styleId = styleId;
  }

  public String getType() {
    return type;
  }

  public int getStyleId() {
    return styleId;
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
