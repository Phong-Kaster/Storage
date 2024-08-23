package com.example.storage.ui.fragment.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.storage.R
import com.example.storage.core.CoreDialog
import com.example.storage.domain.model.Video
import com.example.storage.util.FileUtil

@Composable
fun VideoRemoveDialog(
    video: Video,
    enable: Boolean,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    CoreDialog(
        enable = enable,
        onDismissRequest = onDismissRequest,
        content = {
            VideoRemoveLayout(
                video = video,
                onDismissRequest = onDismissRequest,
                onConfirm = onConfirm
            )
        }
    )
}

@Composable
fun VideoRemoveLayout(
    video: Video,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    val trueName: String by remember(video) { mutableStateOf(FileUtil.getTrueName(video.name)) }
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
                text = stringResource(id = R.string.delete),
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight(500)
                ),
                textAlign = TextAlign.Center
            )
        }

        Text(
            text = stringResource(R.string.are_you_sure_want_to_delete_this_file),
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(400),
                color = Color.Black
            )
        )

        Text(
            text = "\"$trueName$trueExtension\"",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )


        Button(
            enabled = true,
            onClick = {
                onDismissRequest()
                onConfirm()
            },
            content = {
                Text(
                    text = stringResource(R.string.confirm),
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
                containerColor = Color(0xFFFF7E62),
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
private fun PreviewVideoRemoveLayout() {
    VideoRemoveLayout(
        video = Video(
            id = 0,
            contentUri = Uri.parse(""),
            name = "Phong_Kaster.mp4",
            duration = 0,
            size = 0
        ),
    )
}