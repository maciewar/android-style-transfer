package com.randomnoose.switcheroni.core;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final public class AndroidFileUtils {

  private final Context context;

  @Inject
  public AndroidFileUtils(Context context) {
    this.context = context;
  }

  public File getTempFile() throws IOException {
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String imageFileName = "JPEG_" + timeStamp;
    File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    return File.createTempFile(imageFileName, ".jpg", storageDir);
  }
}
