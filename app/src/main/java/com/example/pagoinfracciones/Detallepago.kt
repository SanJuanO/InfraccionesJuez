package com.example.pagoinfracciones

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.pagoinfracciones.permission.PermissionsActivity
import com.example.pagoinfracciones.permission.PermissionsActivity.PERMISSION_REQUEST_CODE
import com.example.pagoinfracciones.permission.PermissionsChecker
import com.example.pagoinfracciones.permission.PermissionsChecker.REQUIRED_PERMISSION
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import kotlinx.android.synthetic.main.activity_descuento.*
import kotlinx.android.synthetic.main.activity_infracciones_detalle.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class Detallepago : AppCompatActivity() {
    var nombreoficial= String()
    var numeroparquimetro= String()
    var tarifa= String()
    var placa= String()
    var evidencia= String()
    var faltao= String()
    var garantiao= String()
    var folio= String()
    var fecha= String()
    var mContext: Context? = null
    var checker: PermissionsChecker? = null

    var hora= String()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detallepago)
        mContext = getApplicationContext()
        checker = PermissionsChecker(this)
        nombreoficial=  nombreoficia.text.toString()
        placa=  plac.text.toString()
        numeroparquimetro=  nparquimetro.text.toString()
        tarifa =  "200"
        folio="1233455"
        faltao=  ""
        garantiao= ""
        button4.setOnClickListener(View.OnClickListener {
            if (checker!!.lacksPermissions(*REQUIRED_PERMISSION)) {
                PermissionsActivity.startActivityForResult(
                    this,
                    PERMISSION_REQUEST_CODE,
                    *REQUIRED_PERMISSION
                )
            } else {
                createPdf(FileUtils.getAppPath(mContext).toString() + "pago.pdf")

            }

        })
    }


    fun createPdf(dest: String?) {
        if (File(dest).exists()) {
            File(dest).delete()
        }
        try {
            /**
             * Creating Document
             */
            val document = Document()

            // Location to save
            PdfWriter.getInstance(document, FileOutputStream(dest))

            // Open to write
            document.open()

            // Document Settings
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("Apizaco")
            document.addCreator("Gobierno de apizaco")
            /***
             * Variables for further use....
             */
            val mColorAccent = BaseColor(255, 153, 204, 255)
            val mHeadingFontSize = 20.0f
            val mValueFontSize = 26.0f

            /**
             * How to USE FONT....
             */
            val urName =
                BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED)

            // LINE SEPARATOR
            val lineSeparator =
                LineSeparator()
            lineSeparator.lineColor = BaseColor(0, 0, 0, 68)

            // Title Order Details...
            // Adding Title....
            val mOrderDetailsTitleFont = Font(
                urName,
                36.0f,
                Font.NORMAL,
                BaseColor.RED
            )
            val mOrderDetailsTitleChunk =
                Chunk("Pago de infraccion", mOrderDetailsTitleFont)
            val mOrderDetailsTitleParagraph = Paragraph(mOrderDetailsTitleChunk)
            mOrderDetailsTitleParagraph.alignment = Element.ALIGN_CENTER
            document.add(mOrderDetailsTitleParagraph)

            // Fields of Order Details...
            // Adding Chunks for Title and value
            val mOrderIdFont = Font(
                urName,
                mHeadingFontSize,
                Font.NORMAL,
                mColorAccent
            )
            val mOrderIdChunk =
                Chunk("Folio No:", mOrderIdFont)
            val mOrderIdParagraph = Paragraph(mOrderIdChunk)
            document.add(mOrderIdParagraph)
            val mOrderIdValueFont = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderIdValueChunk =
                Chunk(folio, mOrderIdValueFont)
            val mOrderIdValueParagraph = Paragraph(mOrderIdValueChunk)
            document.add(mOrderIdValueParagraph)

            // Adding Line Breakable Space....
            document.add(Paragraph(""))
            // Adding Horizontal Line...
            document.add(Chunk(lineSeparator))
            // Adding Line Breakable Space....
            document.add(Paragraph(""))

            // Fields of Order Details...
            val mOrderDateFont = Font(
                urName,
                mHeadingFontSize,
                Font.NORMAL,
                mColorAccent
            )
            val mOrderDateChunk =
                Chunk("Fecha:", mOrderDateFont)
            val mOrderDateParagraph = Paragraph(mOrderDateChunk)
            document.add(mOrderDateParagraph)
            val mOrderDateValueFont = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderDateValueChunk =
                Chunk(fecha+" "+hora, mOrderDateValueFont)
            val mOrderDateValueParagraph = Paragraph(mOrderDateValueChunk)
            document.add(mOrderDateValueParagraph)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            // Fields of Order Details...
            val mOrderAcNameFont = Font(
                urName,
                mHeadingFontSize,
                Font.NORMAL,
                mColorAccent
            )
            val mOrderAcNameChunk0 =
                Chunk("Nombre del oficial:", mOrderAcNameFont)
            val mOrderAcNameParagraph0 = Paragraph(mOrderAcNameChunk0)
            document.add(mOrderAcNameParagraph0)
            val mOrderAcNameValueFont0 = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk0 =
                Chunk(nombreoficial, mOrderAcNameValueFont0)
            val mOrderAcNameValueParagraph0 = Paragraph(mOrderAcNameValueChunk0)
            document.add(mOrderAcNameValueParagraph0)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mOrderAcNameChunk =
                Chunk("Motivo:", mOrderAcNameFont)
            val mOrderAcNameParagraph = Paragraph(mOrderAcNameChunk)
            document.add(mOrderAcNameParagraph)
            val mOrderAcNameValueFont = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk =
                Chunk(faltao, mOrderAcNameValueFont)
            val mOrderAcNameValueParagraph = Paragraph(mOrderAcNameValueChunk)
            document.add(mOrderAcNameValueParagraph)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mOrderAcNameChunk2 =
                Chunk("Garantia:", mOrderAcNameFont)
            val mOrderAcNameParagraph2 = Paragraph(mOrderAcNameChunk2)
            document.add(mOrderAcNameParagraph2)
            val mOrderAcNameValueFont2 = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk2 =
                Chunk(garantiao, mOrderAcNameValueFont2)
            val mOrderAcNameValueParagraph2 = Paragraph(mOrderAcNameValueChunk2)
            document.add(mOrderAcNameValueParagraph2)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mOrderAcNameChunk3 =
                Chunk("Placa:", mOrderAcNameFont)
            val mOrderAcNameParagraph3 = Paragraph(mOrderAcNameChunk3)
            document.add(mOrderAcNameParagraph3)
            val mOrderAcNameValueFont3 = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk3 =
                Chunk(placa, mOrderAcNameValueFont3)
            val mOrderAcNameValueParagraph3 = Paragraph(mOrderAcNameValueChunk3)
            document.add(mOrderAcNameValueParagraph3)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mOrderAcNameChunk4 =
                Chunk("Numero de Parquimetro:", mOrderAcNameFont)
            val mOrderAcNameParagraph4 = Paragraph(mOrderAcNameChunk4)
            document.add(mOrderAcNameParagraph4)
            val mOrderAcNameValueFont4 = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk4 =
                Chunk(numeroparquimetro, mOrderAcNameValueFont4)
            val mOrderAcNameValueParagraph4 = Paragraph(mOrderAcNameValueChunk4)
            document.add(mOrderAcNameValueParagraph4)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mOrderAcNameChunk5 =
                Chunk("Costo 2.4 UMAS:", mOrderAcNameFont)
            val mOrderAcNameParagraph5 = Paragraph(mOrderAcNameChunk5)
            document.add(mOrderAcNameParagraph5)
            val mOrderAcNameValueFont5 = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk5 =
                Chunk("$"+tarifa, mOrderAcNameValueFont5)
            val mOrderAcNameValueParagraph5 = Paragraph(mOrderAcNameValueChunk5)
            document.add(mOrderAcNameValueParagraph5)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            val mOrderAcNameChunk6 =
                Chunk("Descuento", mOrderAcNameFont)
            val mOrderAcNameParagraph6 = Paragraph(mOrderAcNameChunk6)
            document.add(mOrderAcNameParagraph5)
            val mOrderAcNameValueFont6 = Font(
                urName,
                mValueFontSize,
                Font.NORMAL,
                BaseColor.BLACK
            )
            val mOrderAcNameValueChunk6 =
                Chunk("$10%", mOrderAcNameValueFont5)
            val mOrderAcNameValueParagraph6 = Paragraph(mOrderAcNameValueChunk6)
            document.add(mOrderAcNameValueParagraph6)
            document.add(Paragraph(""))
            document.add(Chunk(lineSeparator))
            document.add(Paragraph(""))

            document.close()
            Toast.makeText(mContext, "Creando", Toast.LENGTH_SHORT).show()
            FileUtils.openFile(mContext, File(dest))
        } catch (ie: IOException) {

        } catch (ie: DocumentException) {

        } catch (ae: ActivityNotFoundException) {
            Toast.makeText(mContext, "No encontro una aplicacion para crear el pdf.", Toast.LENGTH_SHORT)
                .show()
        }
    }

}
