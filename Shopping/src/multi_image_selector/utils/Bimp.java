package multi_image_selector.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class Bimp {
	public static int max = 0;

	public static Bitmap revitionImageSize(String path) throws IOException {
		int widthSize = 1000;
		int heightSize = 1000;
		Bitmap bitmap = null;
		try {
			if (new File(path).exists()) {
				// 取得图片
				BufferedInputStream in = new BufferedInputStream(
						new FileInputStream(new File(path)));
				BitmapFactory.Options options = new BitmapFactory.Options();
				// 这个参数代表，不为bitmap分配内存空间，只记录一些该图片的信息（例如图片大小），说白了就是为了内存优化
				options.inJustDecodeBounds = true;
				// 通过创建图片的方式，取得options的内容（这里就是利用了java的地址传递来赋值）
				BitmapFactory.decodeStream(in, null, options);
				in.close();
				// 生成压缩图片
				int i = 0;
				while (true) {
					// 这一步是根据要设置的大小，使宽和高都能满足
					if ((options.outWidth >> i <= widthSize)
							&& (options.outHeight >> i <= heightSize)) {
						// 重新取得流，注意：这里一定要再次加载，不能二次使用之前的流！
						in = new BufferedInputStream(new FileInputStream(
								new File(path)));
						// 这个参数表示新生成的图片为原始图片的几分之一。
						options.inSampleSize = (int) Math.pow(2.0D, i);
						// 这里之前设置为了true，所以要改为false，否则就创建不出图片
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
