import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import com.squareup.picasso.Transformation

class CircleTransform : Transformation {
    override fun transform(source: Bitmap): Bitmap {
        val size = Math.min(source.width, source.height)
        val x = (source.width - size) / 2
        val y = (source.height - size) / 2

        // Crea una nueva imagen circular
        val squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)
        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = android.graphics.BitmapShader(squaredBitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        paint.isDither = true

        // Dibuja un círculo
        val radius = size / 2f
        canvas.drawCircle(radius, radius, radius, paint)

        // Libera recursos de la imagen original
        if (squaredBitmap != source) {
            squaredBitmap.recycle()
        }

        return bitmap
    }

    override fun key(): String {
        return "circle"
    }
}
