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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
    modifier: Modifier = Modifier
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .imePadding()
            .requiredWidth(300.dp)
            .background(color = Color.DarkGray, shape = RoundedCornerShape(20.dp)),
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Rename",
            style = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight(600)
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(24.dp))

        BasicTextField(
            value = trueName,
            onValueChange = { trueName = it },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp)
                .border(
                    width = 0.2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(10.dp)
                )
                .background(
                    color = Color.White.copy(0.05f),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
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
                    style = TextStyle(color = Color.White, fontSize = 12.sp),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 7.dp)
                )
            })


        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
        ) {//Button layout
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F)
            ) {
                Text(
                    text = stringResource(id = R.string.cancel).uppercase(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.White
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .clickable {
                            onDismissRequest()
                        }
                )
            }

            Divider(
                thickness = 1.dp,
                color = Color.Gray,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {

                    Text(
                        text = stringResource(id = R.string.save).uppercase(),
                        style = TextStyle(
                            fontSize = 16.sp,
                            color =
                            if (isUnacceptable != Constant.FALSE)
                                Color.Gray
                            else
                                Color.Green
                        ),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .clickable(
                                enabled = (isUnacceptable == Constant.FALSE),
                                onClick = {
                                    onDismissRequest()
                                    onConfirm("${trueName}${trueExtension}")
                                }
                            )
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun PreviewVideoRename() {
    VideoRenameLayout(
        video = Video(id = 0, contentUri = Uri.parse(""), name = "", duration = 0, size = 0),
    )
}