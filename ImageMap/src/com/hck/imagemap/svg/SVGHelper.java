package com.hck.imagemap.svg;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
public class SVGHelper {
	/**
	 * 通过流导入SVG文件
	 * 
	 * @param is
	 * @throws SVGParseException
	 */
	public static SVG createSVGFormInputStream(InputStream is)
			throws SVGParseException {
		return SVG.getFromInputStream(is);
	}

	/**
	 * 导入Asset目录下的SVG文件
	 * 
	 * @param assetName
	 * @throws SVGParseException
	 * @throws IOException
	 */
	public static SVG createSVGFromAsset(Context context, String assetName)
			throws SVGParseException, IOException {
		return SVG.getFromAsset(context.getAssets(), assetName);
	}

	/**
	 * 导入Asset目录下的SVG文件
	 * 
	 * @param assetName
	 * @throws SVGParseException
	 * @throws IOException
	 */
	public static SVG createSVGFromAsset(AssetManager manager, String assetName)
			throws SVGParseException, IOException {
		return SVG.getFromAsset(manager, assetName);
	}

	/**
	 * 导入Resource中的SVG文件
	 * 
	 * @param Rid
	 * @throws SVGParseException
	 */
	public static SVG createSVGFromResource(Context context, int Rid)
			throws SVGParseException {
		return SVG.getFromResource(context, Rid);
	}

	/**
	 * 导入SVG字符串
	 * 
	 * @param data
	 * @throws SVGParseException
	 */
	public static SVG createSVGFromData(String data) throws SVGParseException {
		return SVG.getFromString(data);
	}

	/**
	 * 生成svg bitmap 图像, 按照svg图片的宽高1:1
	 * 
	 * @param svg
	 * @return
	 */
	public static Bitmap getSVGBitmap(SVG svg) {
		return getSVGBitmap(svg, 1f);
	}

	/**
	 * 生成svg bitmap 图像, 按照svg图片的宽高1:scale
	 * 
	 * @param svg
	 * @param scale
	 * @return
	 */
	public static Bitmap getSVGBitmap(SVG svg, float scale) {
		Bitmap bitmap = Bitmap.createBitmap(
				(int) (svg.getDocumentWidth() * scale),
				(int) (svg.getDocumentHeight() * scale), Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bitmap);
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		c.setMatrix(matrix);
		svg.renderToCanvas(c);
		return bitmap;
	}

	/**
	 * 按照矩阵变换生成svg bitmap 图像
	 * 
	 * @param svg
	 * @param matrix
	 * @return
	 */
	public static Bitmap getSVGBotmapFromMatrix(SVG svg, Matrix matrix) {
		float[] values = new float[9];
		matrix.getValues(values);
		Bitmap bitmap = Bitmap.createBitmap(
				(int) (svg.getDocumentWidth() * values[Matrix.MSCALE_X]),
				(int) (svg.getDocumentHeight() * values[Matrix.MSCALE_Y]),
				Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bitmap);
		c.setMatrix(matrix);
		svg.renderToCanvas(c);
		return bitmap;
	}

	public static BitmapData getMaxBitmap(SVG svg) {
        if (svg == null) {
            return null;
        }
        float sw = svg.getDocumentWidth();
        float sh = svg.getDocumentHeight();
        Log.d("sw-sh", sw+"  "+sh);
        float scale = getScale(sw, sh);
        Bitmap bitmap = getSVGBitmap(svg, scale);
        BitmapData data = new BitmapData();
        data.setWidth(sw);
        data.setHeight(sh);
        data.setScale(scale);
        data.setBitmap(bitmap);
        return data;
    }

	private static float getScale(float sw, float sh) {
			return 2000 / Math.max(sh, sw);
	}

	public static class BitmapData {
		private float width;
		private float height;
		private float scale;
		private Bitmap bitmap;

		public float getWidth() {
			return width;
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public float getHeight() {
			return height;
		}

		public void setHeight(float height) {
			this.height = height;
		}

		public float getScale() {
			return scale;
		}

		public void setScale(float scale) {
			this.scale = scale;
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}

		@Override
		public String toString() {
			return "BitmapData{" + "width=" + width + ", height=" + height
					+ ", scale=" + scale + ", bitmap=" + bitmap + '}';
		}
	}
}
