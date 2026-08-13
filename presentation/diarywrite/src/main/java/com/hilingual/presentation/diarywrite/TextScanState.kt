package com.hilingual.presentation.diarywrite

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Stable
internal class TextScanState(
    val launchCamera: () -> Unit,
    val launchGallery: () -> Unit,
)

@Composable
internal fun rememberTextScanState(
    onImageSelected: (imageUri: Uri, tempImageFile: File?) -> Unit,
): TextScanState {
    val context = LocalContext.current
    val currentOnImageSelected by rememberUpdatedState(onImageSelected)

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraImageFile by remember { mutableStateOf<File?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
    ) { isCaptured ->
        val imageUri = cameraImageUri
        if (isCaptured && imageUri != null) {
            currentOnImageSelected(imageUri, cameraImageFile)
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isGranted ->
        if (isGranted) {
            val (imageUri, imageFile) = createTempImageFile(context)
            cameraImageUri = imageUri
            cameraImageFile = imageFile
            cameraLauncher.launch(imageUri)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { imageUri ->
        imageUri?.let { currentOnImageSelected(it, null) }
    }

    return remember {
        TextScanState(
            launchCamera = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) },
            launchGallery = {
                galleryLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly),
                )
            },
        )
    }
}

private fun createTempImageFile(context: Context): Pair<Uri, File> {
    val imageFile = File.createTempFile("camera_", ".jpg", context.cacheDir)
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        imageFile,
    )
    return uri to imageFile
}
