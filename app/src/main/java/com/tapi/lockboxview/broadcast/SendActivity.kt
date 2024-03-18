package com.tapi.lockboxview.broadcast

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Path
import android.os.Bundle
import android.os.Environment
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.CycleInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.tapi.lockboxview.IServiceListener
import com.tapi.lockboxview.databinding.SendActivityBinding
import com.tapi.lockboxview.ui.SchoolManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile


class SendActivity : AppCompatActivity() {
    private lateinit var binding: SendActivityBinding

    private lateinit var animation: ValueAnimator
    private val mDuration = 5000L

    private var mFile: File? = null
    private val schoolManager = SchoolManager()
    private var mService: IServiceListener? = null

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mService = IServiceListener.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SendActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createBtn.setOnClickListener {
            /* val intent = Intent()
             intent.setAction("test.sending")
             sendBroadcast(intent)

             startActivity(Intent(this, ReceivedActivity::class.java))*/

            //Test service
            val intent = Intent("com.tapi.lockboxview.service.TestService.BIND")
            intent.setPackage("com.tapi.lockboxview")
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)

            startService(intent)

            /*  mFile = createFile2(fileName = "TestABC.txt")
              Log.d("ManhNQ", "onCreate: ${mFile != null && mFile!!.exists()}")

              setResult("create file $ ${mFile != null && mFile!!.exists()}")
              if (mFile != null) {
                  writeToFile2(
                      mFile!!,
                      "this is content hellllo\nddkl;aks;ldkasdas\n\njdlaskjdljalksdjlka\njdlksajdlasda"
                  )
              }*/


        }

        binding.readBtn.setOnClickListener {
            mService?.sendData("Hello")
        }

        binding.addMoreBtn.setOnClickListener {

        }

        binding.actionBtn.setOnClickListener {
            /*
                        if (animation.isPaused) {
                            animation.resume()
                        } else {
                            animation.pause()
                        }*/

        }


    }

    private fun writeToFile(file: File, content: String) {
        try {
            val outputStream = FileOutputStream(file)
            outputStream.write(content.toByteArray())
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun writeToFile2(file: File, content: String) {
        try {
            val raf = RandomAccessFile(file, "rw")
            raf.write(content.toByteArray())
            raf.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun readFile2(file: File): String {
        val result = StringBuilder()
        try {
            val raf = RandomAccessFile(file, "rw")
            var line: String? = raf.readLine()

            while (line != null) {
                result.append(line).append("\n")
                line = raf.readLine()
            }
            raf.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result.toString()
    }


    private fun readFile(file: File): String {
        var fileInputStream: FileInputStream? = null
        var result = ""
        return try {
            fileInputStream = FileInputStream(file)
            val byteArrayOutputStream = ByteArrayOutputStream()

            val buffer = ByteArray(1024)
            var bytesRead: Int = fileInputStream.read(buffer)

            while (bytesRead != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead)
                result += String(byteArrayOutputStream.toByteArray())
                bytesRead = fileInputStream.read(buffer)
            }

            result
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        } finally {
            fileInputStream?.close()
        }
    }

    private fun createFile(fileName: String): File? {
        try {
            val externalStorageDir = Environment.getExternalStorageDirectory()

            val folder = File(externalStorageDir, "TestFolder")
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val file = File(folder, fileName)
            try {
                file.createNewFile() // Create new file
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        } catch (e: Exception) {
            binding.resultTxt.text = e.toString()
            return null
        }
    }

    private fun createFile2(fileName: String): File? {
        try {
            val downloadFiles =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

            val folder = File(downloadFiles, "TestFolder")
            if (!folder.exists()) {
                folder.mkdirs()
            }

            val file = File(folder, fileName)
            try {
                file.createNewFile() // Create new file
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return file
        } catch (e: Exception) {
            binding.resultTxt.text = e.toString()
            return null
        }
    }


    private fun initCurveAnim() {
        val path = Path()

        path.arcTo(0f, 0f, 1000f, 1000f, 270f, 180f, true)
        val animator: ObjectAnimator =
            ObjectAnimator.ofFloat(binding.viewAnimation3, View.X, View.Y, path)
        animator.setDuration(2000)
        animator.start()
    }

    private fun initValueAnim() {
        animation = ValueAnimator.ofFloat(0f, 1f)
        animation.apply {
            duration = mDuration
            interpolator = CycleInterpolator(1f)

            addUpdateListener { anim ->
                val value = anim.animatedValue as Float

                Log.d("ManhNQ", "value: $value")
                val width = convertValue(0f, 1f, 0f, 100f, value)

                val widthPx = dpToPx(this@SendActivity, width)

                Log.d("ManhNQ", "initAnim: $widthPx")

                val params = LinearLayout.LayoutParams(
                    widthPx.toInt(), widthPx.toInt()
                )

                binding.viewAnimation.layoutParams = params
            }
        }
        animation.start()

    }

    private fun initObjectAnim() {
        binding.viewAnimation2.pivotX = (binding.viewAnimation2.width / 2).toFloat()
        binding.viewAnimation2.pivotY = (binding.viewAnimation2.height / 2).toFloat()

        val animation = ObjectAnimator.ofFloat(binding.viewAnimation2, "rotation", 0f, 180f)
        animation.setDuration(mDuration)
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
    }

    fun convertValue(min1: Float, max1: Float, min2: Float, max2: Float, value: Float): Float {
        return ((value - min1) * ((max2 - min2) / (max1 - min1)) + min2)
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return (dp * context.resources.displayMetrics.density)
    }

    fun pxToDp(context: Context, px: Float): Float {
        return (px / context.resources.displayMetrics.density)
    }

    fun setResult(content: String) {
        binding.resultTxt.text = content
    }
}