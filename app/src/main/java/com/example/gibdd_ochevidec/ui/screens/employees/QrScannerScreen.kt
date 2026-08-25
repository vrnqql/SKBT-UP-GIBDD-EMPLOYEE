package com.example.gibdd_ochevidec.ui.screens.employees

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.gibdd_ochevidec.ui.theme.Commissioner
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.UUID
import java.util.concurrent.Executors
import androidx.camera.core.ExperimentalGetImage

@Composable
fun QrScannerScreen(
    onBackClick: () -> Unit = {},
    onQrScanned: (String) -> Unit = {},
    onInvalidQr: () -> Unit = {}
) {

    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }


    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasCameraPermission = granted
        }


    LaunchedEffect(Unit) {

        if (!hasCameraPermission) {

            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }


    val backgroundColor = Color(0xFFF5F8FD)
    val darkText = Color(0xFF090B22)


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .safeDrawingPadding()
    ) {

        Spacer(
            modifier = Modifier.height(34.dp)
        )


        // ШАПКА
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 18.dp,
                    end = 24.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onBackClick
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,

                    contentDescription =
                        "Назад",

                    tint = darkText,

                    modifier =
                        Modifier.size(26.dp)
                )
            }


            Spacer(
                modifier = Modifier.width(8.dp)
            )


            Text(
                text = "Сканирование QR-кода",
                fontFamily = Commissioner,
                fontWeight = FontWeight.SemiBold,
                fontSize = 23.sp,
                color = darkText
            )
        }


        Spacer(
            modifier = Modifier.height(28.dp)
        )


        if (hasCameraPermission) {

            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 28.dp
                    )
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(
                        RoundedCornerShape(12.dp)
                    )
            ) {

                CameraQrPreview(
                    modifier = Modifier.fillMaxSize(),
                    onQrScanned = onQrScanned,
                    onInvalidQr = onInvalidQr
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color.Black.copy(
                                alpha = 0.18f
                            )
                        )
                )


                // РАМКА СКАНЕРА
                ScannerFrame(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(170.dp)
                )


                Text(
                    text =
                        "Наведите камеру на QR-код\n" +
                                "устройства сотрудника",

                    fontFamily = Commissioner,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,

                    modifier = Modifier
                        .align(
                            Alignment.Center
                        )
                        .padding(
                            top = 250.dp
                        )
                )
            }

        } else {

            Box(
                modifier = Modifier
                    .padding(
                        horizontal = 28.dp
                    )
                    .fillMaxWidth()
                    .weight(1f)
                    .background(
                        color =
                            Color(0xFFC7C7C7),

                        shape =
                            RoundedCornerShape(12.dp)
                    ),

                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text =
                        "Для сканирования QR-кода\n" +
                                "нужен доступ к камере",

                    fontFamily = Commissioner,
                    fontSize = 14.sp,
                    lineHeight = 19.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }


        Spacer(
            modifier = Modifier.height(34.dp)
        )
    }
}

@androidx.annotation.OptIn(
    markerClass = [ExperimentalGetImage::class]
)
@Composable
private fun CameraQrPreview(
    modifier: Modifier,
    onQrScanned: (String) -> Unit,
    onInvalidQr: () -> Unit
) {

    val context =
        LocalContext.current

    val lifecycleOwner =
        LocalLifecycleOwner.current


    val cameraExecutor =
        remember {
            Executors
                .newSingleThreadExecutor()
        }


    val scanner =
        remember {
            BarcodeScanning
                .getClient()
        }


    var alreadyScanned by remember {
        mutableStateOf(false)
    }


    DisposableEffect(Unit) {

        onDispose {

            cameraExecutor.shutdown()
            scanner.close()
        }
    }


    AndroidView(
        modifier = modifier,

        factory = { androidContext ->

            val previewView =
                PreviewView(androidContext)


            val cameraProviderFuture =
                ProcessCameraProvider
                    .getInstance(
                        androidContext
                    )


            cameraProviderFuture
                .addListener({

                    val cameraProvider =
                        cameraProviderFuture.get()


                    val preview =
                        Preview.Builder()
                            .build()
                            .also {

                                it.surfaceProvider =
                                    previewView
                                        .surfaceProvider
                            }


                    val imageAnalysis =
                        ImageAnalysis.Builder()
                            .setBackpressureStrategy(
                                ImageAnalysis
                                    .STRATEGY_KEEP_ONLY_LATEST
                            )
                            .build()


                    imageAnalysis
                        .setAnalyzer(
                            cameraExecutor
                        ) { imageProxy ->

                            val mediaImage =
                                imageProxy.image


                            if (
                                mediaImage == null ||
                                alreadyScanned
                            ) {

                                imageProxy.close()
                                return@setAnalyzer
                            }


                            val image =
                                InputImage
                                    .fromMediaImage(
                                        mediaImage,
                                        imageProxy
                                            .imageInfo
                                            .rotationDegrees
                                    )


                            scanner.process(image)
                                .addOnSuccessListener { barcodes ->

                                    if (alreadyScanned) {
                                        return@addOnSuccessListener
                                    }

                                    val qrValue =
                                        barcodes
                                            .firstOrNull {
                                                it.format ==
                                                        Barcode.FORMAT_QR_CODE
                                            }
                                            ?.rawValue
                                            ?.trim()


                                    if (
                                        qrValue != null &&
                                        !alreadyScanned
                                    ) {

                                        alreadyScanned = true

                                        if (isValidUuid(qrValue)) {

                                            onQrScanned(
                                                qrValue
                                            )

                                        } else {

                                            onInvalidQr()
                                        }
                                    }
                                }
                                .addOnCompleteListener {

                                    imageProxy.close()
                                }
                        }


                    try {

                        cameraProvider
                            .unbindAll()


                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_BACK_CAMERA,
                            preview,
                            imageAnalysis
                        )

                    } catch (
                        exception: Exception
                    ) {

                        exception.printStackTrace()
                    }

                },
                    ContextCompat
                        .getMainExecutor(
                            androidContext
                        )
                )


            previewView
        }
    )
}


@Composable
private fun ScannerFrame(
    modifier: Modifier
) {

    androidx.compose.foundation.Canvas(
        modifier = modifier
    ) {

        val cornerLength =
            32.dp.toPx()

        val radius =
            12.dp.toPx()

        val strokeWidth =
            2.dp.toPx()


        val width =
            size.width

        val height =
            size.height


        val path =
            androidx.compose.ui.graphics.Path()


        // ЛЕВЫЙ ВЕРХ
        path.moveTo(
            0f,
            cornerLength
        )

        path.lineTo(
            0f,
            radius
        )

        path.quadraticBezierTo(
            0f,
            0f,
            radius,
            0f
        )

        path.lineTo(
            cornerLength,
            0f
        )


        // ПРАВЫЙ ВЕРХ
        path.moveTo(
            width - cornerLength,
            0f
        )

        path.lineTo(
            width - radius,
            0f
        )

        path.quadraticBezierTo(
            width,
            0f,
            width,
            radius
        )

        path.lineTo(
            width,
            cornerLength
        )


        // ПРАВЫЙ НИЗ
        path.moveTo(
            width,
            height - cornerLength
        )

        path.lineTo(
            width,
            height - radius
        )

        path.quadraticBezierTo(
            width,
            height,
            width - radius,
            height
        )

        path.lineTo(
            width - cornerLength,
            height
        )


        // ЛЕВЫЙ НИЗ
        path.moveTo(
            cornerLength,
            height
        )

        path.lineTo(
            radius,
            height
        )

        path.quadraticBezierTo(
            0f,
            height,
            0f,
            height - radius
        )

        path.lineTo(
            0f,
            height - cornerLength
        )


        drawPath(
            path = path,
            color = Color.White,
            style = Stroke(
                width = strokeWidth
            )
        )
    }
}


private fun isValidUuid(
    value: String
): Boolean {

    return runCatching {

        UUID.fromString(value)

    }.isSuccess
}