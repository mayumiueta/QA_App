package jp.techacademy.Mayumi.Ueta.qa_app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_question_send.*

class QuestionSendActivity : AppCompatActivity(), View.OnClickListener, DatabaseReference.CompletionListener {
    companion object {
        private val PERMISSIONS_REQUEST_CODE = 100
        private val CHOOSER_REQUEST_CODE = 100
    }

    private var mGenre: Int = 0
    private val mPictureUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_send)

        //渡ってきたジャンルの番号を保持する
        val extras = intent.extras
        mGenre = extras.getInt("genre")

        //UIの準備
        title = "質問作成"

        sendButton.setOnClickListener(this)
        imageView.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CHOOSER_REQUEST_CODE){
            if (resultCode ! = Activity.RESULT_OK){
                if(mPictureUri ! = null){
                contentResolver.delete(mPictureUri!!, null, null)
                mPictureUri = null
            }
                return
            }

            //画面を取得
            val uri = if (data == null || data.data == null)mPictureUri else data.data

            //URIからBitmapを取得する
            val image: Bitmap
            try {
                val contentResolver = contentResolver
                val inputStream = contentResolver.openInputStream(uri!!)
                image = BitmapFactory.decodeStream(inputStream)
                inputStream!!.close()
            }catch (e: Exception){
                return
            }

            //取得したBitmapの長辺を500ピクセルにリサイズする
            val imageWidth = image.width
            val imageHeight = image.height
            val scale = Math.min(500.toFloat()/imageWidth, 500.toFloat()/imageHeight) // (1)

            val matrix = Matrix()
            matrix.postScale(scale, scale)

            val resizedImage = Bitmap.createBitmap(image, 0, 0, imageWidth, imageHeight, matrix, true)

            //BitmapをImageViewに設定する
            imageView.setImageBitmap(resizedImage)

            mPictureUri = null
        }
    }

    override fun onClick(v: View) {
       if (v === imageView){
           //
       }
    }
}
