package multi_image_selector.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Bimp {
	public static int max = 0;

	public static Bitmap revitionImageSize(String path) throws IOException {
		int widthSize = 800;
		int heightSize = 800;
		Bitmap bitmap = null;
		try {
			if (new File(path).exists()) {
				BufferedInputStream in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, options);
				in.close();
				int i = 0;
				while (true) {
					if ((options.outWidth >> i <= widthSize)
							&& (options.outHeight >> i <= heightSize)) {
						in = new BufferedInputStream(new FileInputStream(
								new File(path)));
						options.inSampleSize = (int) Math.pow(2.0D, i);
						options.inJustDecodeBounds = false;
						bitmap = BitmapFactory.decodeStream(in, null, options);
						break;
					}
					i += 1;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			String message = e.getMessage();
			Log.d("ddd", message);
		}

		return bitmap;
	}
}
