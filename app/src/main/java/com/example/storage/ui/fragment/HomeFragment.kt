package com.example.storage.ui.fragment

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.example.jetpack.core.CoreFragment
import com.example.jetpack.core.CoreLayout
import com.example.storage.domain.model.Video
import com.example.storage.ui.fragment.component.VideoElement
import dagger.hilt.android.AndroidEntryPoint

/***************************************
 * 1. [Storage updates in Android 11](https://developer.android.com/about/versions/11/privacy/storage)
 * 2. [Access media files from shared storage ](https://developer.android.com/training/data-storage/shared/media)
 * 3. [Data and file storage overview](https://developer.android.com/training/data-storage#scoped-storage)
 * 4. [Access media files](https://developer.android.com/training/data-storage/shared/media#access-other-apps-files)
 * - Access your own media files - On devices that run Android 10 or higher, you don't need storage-related permissions
 * to access and modify media files that your app owns
 * - Access other apps' media files - To access media files that other apps create, you must declare the appropriate storage-related permissions
 */
@AndroidEntryPoint
class HomeFragment : CoreFragment() {

    private val viewModel: HomeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getVideos()
    }

    @Composable
    override fun ComposeView() {
        super.ComposeView()
        HomeLayout(
            videos = viewModel.videos.collectAsState().value,
            onAddSong = { viewModel.addVideo() },
            onRefresh = { viewModel.getVideos() },
            onRemove = { video: Video -> viewModel.removeVideo(video) },
            onRename = { video: Video ->

            }
        )
    }
}

@Composable
fun HomeLayout(
    videos: List<Video> = emptyList(),
    onAddSong: () -> Unit = {},
    onRefresh: () -> Unit = {},
    onRename: (Video) -> Unit = {},
    onRemove: (Video) -> Unit = {},
) {


    CoreLayout(
        backgroundColor = Color.Black,
        bottomBar = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    onClick = onAddSong,
                    modifier = Modifier
                        .fillMaxWidth(),
                    content = {
                        Text(
                            text = "Add Song",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                            )
                        )
                    }
                )

                Button(
                    onClick = onRefresh,
                    modifier = Modifier
                        .fillMaxWidth(),
                    content = {
                        Text(
                            text = "Refresh",
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 20.sp,
                            )
                        )
                    }
                )
            }

        },
        content = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .statusBarsPadding()
            ) {
                items(
                    items = videos,
                    key = { video -> video.name },
                    itemContent = { video ->
                        VideoElement(
                            video = video,
                            modifier = Modifier,
                            onRemove = {
                                onRemove(video)
                            },
                            onRename = {
                                onRename(video)
                            },
                        )
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewHomeLayout() {
    HomeLayout(
        videos = listOf(
            Video(contentUri = Uri.parse(""), name = "Song 1", duration = 100, size = 100, id = 1),
            Video(contentUri = Uri.parse(""), name = "Song 2", duration = 100, size = 100, id = 2),
            Video(contentUri = Uri.parse(""), name = "Song 3", duration = 100, size = 100, id = 3),
        )
    )
}