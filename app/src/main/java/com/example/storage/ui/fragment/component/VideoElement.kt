package com.example.storage.ui.fragment.component

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.VideoFrameDecoder
import coil.disk.DiskCache
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.request.videoFrameMillis
import coil.util.DebugLogger
import com.example.jetpack.util.DateUtil
import com.example.jetpack.util.DateUtil.convertLongToDate
import com.example.storage.R
import com.example.storage.domain.model.Video
import com.example.storage.util.FileUtil.roundTo
import com.example.storage.util.FileUtil.toMegabyte
import com.example.storage.util.FileUtil.toTotalWatchTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VideoElement(
    video: Video,
    modifier: Modifier = Modifier,
    onRename: () -> Unit = {},
    onRemove: () -> Unit = {},
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Video thumbnail
    val imageLoader = ImageLoader.Builder(context)
        .memoryCachePolicy(CachePolicy.ENABLED)
//        .memoryCache {
//            MemoryCache
//                .Builder(context)
//                .maxSizePercent(0.1)
//                .strongReferencesEnabled(true)
//                .build()
//        }
        .diskCachePolicy(CachePolicy.ENABLED)
        .diskCache {
            DiskCache
                .Builder()
                .maxSizePercent(0.03)
                .directory(context.cacheDir)
                .build()
        }
        .logger(DebugLogger())
        .components { add(VideoFrameDecoder.Factory()) }
        .build()

    val model = ImageRequest
        .Builder(context)
        .data(video.contentUri)
        .videoFrameMillis(10000)
        .decoderFactory { result, options, _ ->
            VideoFrameDecoder(result.source, options)
        }
        .build()

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(vertical = 10.dp, horizontal = 15.dp)
    ) {
        Box(modifier = Modifier.size(70.dp)) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_launcher_background),
//                contentDescription = null,
//                modifier = Modifier
//                    .clip(shape = RoundedCornerShape(10.dp))
//                    .matchParentSize()
//            )

            AsyncImage(
                model = model,
                imageLoader = imageLoader,
                contentDescription = "Video Thumbnail",
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(10.dp))
                    .matchParentSize()
            )



            Text(
                text = video.duration.toTotalWatchTime(),
                style = TextStyle(
                    fontSize = 11.sp,
                    color = Color.White
                ),
                modifier = Modifier
                    .padding(start = 5.dp, top = 0.dp, bottom = 5.dp, end = 5.dp)
                    .clip(shape = RoundedCornerShape(10.dp))
                    .background(color = Color(0xFF545454))
                    .padding(horizontal = 6.dp, vertical = 1.dp)
                    .align(Alignment.BottomStart)
            )
        }


        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = video.name,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(500),
                        color = Color.Black
                    ),
                    maxLines = 1,
                    modifier = Modifier.basicMarquee(Int.MAX_VALUE)
                )

                Spacer(modifier = Modifier.weight(1F))

                Box(
                    modifier = Modifier
                        .clip(shape = CircleShape)
                        .clickable { expanded = true }
                        .padding(0.dp),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier
                        )
                        MaterialTheme(
                            shapes = MaterialTheme.shapes.copy(
                                extraSmall = RoundedCornerShape(15.dp),
                            )
                        ) {
                            DropdownMenu(
                                expanded = expanded,
                                modifier = Modifier.background(color = Color.White),
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Row(modifier = Modifier) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_edit),
                                                contentDescription = null,
                                            )
                                            Text("Edit")
                                        }
                                    },
                                    onClick = {
                                        expanded = false
                                        onRename()
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("Remove") },
                                    onClick = {
                                        expanded = false
                                        onRemove()
                                    }
                                )
                            }
                        }
                    }
                )
            }

            Text(
                text = "${video.size.toMegabyte().roundTo(2)} MB",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF545454)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${stringResource(R.string.last_updated)}: ${video.dateModified.convertLongToDate(DateUtil.PATTERN_dd_MM_yyyy)} ",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF545454)
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewVideoElement() {
    VideoElement(
        video = Video(
            contentUri = Uri.parse(""),
            name = "Song 1",
            duration = 100,
            size = 100,
            id = 0
        )
    )
}