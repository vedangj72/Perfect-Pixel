//package com.example.photowardrobe.Utils
//
//import android.content.Context
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.os.Environment
//import android.util.Log
//import java.io.File
//import java.io.FileOutputStream
//import java.io.InputStream
//import java.net.HttpURLConnection
//import java.net.URL
//
//object ImageUtils {
//
//    fun downloadImage(context: Context, imageUrl: String): File? {
//        return try {
//            Log.d("ImageUtils", "Starting download for URL: $imageUrl")
//
//            // Open a connection to the URL
//            val url = URL(imageUrl)
//            val connection = url.openConnection() as HttpURLConnection
//            connection.doInput = true
//            connection.connect()
//
//            val responseCode = connection.responseCode
//            Log.d("ImageUtils", "Response Code: $responseCode")
//
//            // Check for successful response code or throw error
//            if (responseCode != HttpURLConnection.HTTP_OK) {
//                Log.e("ImageUtils", "Failed to connect, response code: $responseCode")
//                return null
//            }
//
//            val inputStream: InputStream = connection.inputStream
//            val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
//
//            if (bitmap == null) {
//                Log.e("ImageUtils", "Failed to decode the bitmap.")
//                return null
//            }
//
//            // Create a file in the app's private external storage directory
//            val file = File(
//                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
//                "downloaded_image.jpg"
//            )
//
//            // Write the bitmap to the file
//            val outputStream = FileOutputStream(file)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
//            outputStream.flush()
//            outputStream.close()
//
//            Log.d("ImageUtils", "Image saved to: ${file.absolutePath}")
//            file
//        } catch (e: java.net.MalformedURLException) {
//            Log.e("ImageUtils", "MalformedURLException: Invalid URL", e)
//            null
//        } catch (e: java.io.FileNotFoundException) {
//            Log.e("ImageUtils", "FileNotFoundException: ${e.message}", e)
//            null
//        } catch (e: java.io.IOException) {
//            Log.e("ImageUtils", "IOException while downloading image: ${e.message}", e)
//            null
//        } catch (e: SecurityException) {
//            Log.e("ImageUtils", "SecurityException: Permission denied", e)
//            null
//        } catch (e: Exception) {
//            Log.e("ImageUtils", "Unexpected error downloading image: ${e.message}", e)
//            null
//        }
//    }
//}

// ImageUtils.kt

package com.example.photowardrobe.Utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

object ImageUtils {

    suspend fun downloadImage(context: Context, imageUrl: String): File? {
        return withContext(Dispatchers.IO) {  // Runs the code block on the IO dispatcher
            try {
                Log.d("ImageUtils", "Starting download for URL: $imageUrl")

                // Open a connection to the URL
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()

                val responseCode = connection.responseCode
                Log.d("ImageUtils", "Response Code: $responseCode")

                // Check for successful response code or throw error
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Log.e("ImageUtils", "Failed to connect, response code: $responseCode")
                    return@withContext null
                }

                val inputStream: InputStream = connection.inputStream
                val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)

                if (bitmap == null) {
                    Log.e("ImageUtils", "Failed to decode the bitmap.")
                    return@withContext null
                }

                // Create a file in the public pictures directory
                val picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                if (!picturesDir.exists()) {
                    picturesDir.mkdirs()  // Make sure the directory exists
                }
                val file = File(picturesDir, "downloaded_image.jpg")

                // Write the bitmap to the file
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                Log.d("ImageUtils", "Image saved to: ${file.absolutePath}")
                file
            } catch (e: Exception) {
                Log.e("ImageUtils", "Unexpected error downloading image: ${e.message}", e)
                null
            }
        }
    }
}
