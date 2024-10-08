package com.example.storage.ui.fragment.component

import android.net.Uri
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpack.configuration.Constant
import com.example.storage.R
import com.example.storage.core.CoreDialog
import com.example.storage.domain.model.Video
import com.example.storage.util.FileUtil

@Composable
fun VideoRenameDialog(
    video: Video,
    enable: Boolean,
    onDismissRequest: () -> Unit = {},
    onConfirm: (String) -> Unit = {},
) {
    CoreDialog(
        enable = enable,
        onDismissRequest = onDismissRequest,
        content = {
            VideoRenameLayout(
                video = video,
                onDismissRequest = onDismissRequest,
                onConfirm = onConfirm
            )
        }
    )
}

@Composable
fun VideoRenameLayout(
    video: Video,
    onDismissRequest: () -> Unit = {},
    onConfirm: (String) -> Unit = {}
) {
    var trueName: String by remember(video) { mutableStateOf(FileUtil.getTrueName(video.name)) }
    val trueExtension: String by remember(video) { mutableStateOf(FileUtil.getTrueExtension(video.name)) }

    val isUnacceptable: Int by remember(trueName) {
        derivedStateOf {
            FileUtil.isNameUnacceptable(
                trueName,
                trueExtension
            )
        }
    }


    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .imePadding()
            .requiredWidth(300.dp)
            .background(color = Color.White, shape = RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = CircleShape)
                    .clickable { onDismissRequest() }
                    .align(BiasAlignment(1f, 0f))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .size(15.dp)
                )
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Rename",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                ),
                textAlign = TextAlign.Center
            )
        }

        BasicTextField(
            value = trueName,
            onValueChange = { trueName = it },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight(400)
            ),
            decorationBox = { innerTextField ->
                if (trueName.isEmpty()) {
                    Text(
                        text = "Enter new name",
                        style = TextStyle(
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight(400)
                        ),
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFFFF7E62),
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = Color.White.copy(0.05f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )

        AnimatedVisibility(
            visible = isUnacceptable != Constant.FALSE,
            content = {
                Text(
                    text =
                    if (isUnacceptable == Constant.FALSE) ""
                    else
                        stringResource(id = isUnacceptable),
                    color = Color.Red,
                    style = TextStyle(color = Color.White, fontSize = 11.sp),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )


        Button(
            enabled = (isUnacceptable == Constant.FALSE),
            onClick = {
                onDismissRequest()
                onConfirm("${trueName}${trueExtension}")
            },
            content = {
                Text(
                    text = "Save",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight(500)
                    ),
                    modifier = Modifier
                )
            },
            colors = ButtonColors(
                contentColor = Color(0xFFFF7E62),
                containerColor =  Color(0xFFFF7E62),
                disabledContentColor = Color.LightGray,
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun PreviewVideoRename() {
    VideoRenameLayout(
        video = Video(id = 0, contentUri = Uri.parse(""), name = "Phong_Kaster.mp4", duration = 0, size = 0),
    )
}