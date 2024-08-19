package com.example.storage.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.viewModels
import com.example.jetpack.core.CoreFragment
import com.example.jetpack.core.CoreLayout
import com.example.storage.domain.model.Song
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
        viewModel.getAllImages()
    }

    @Composable
    override fun ComposeView() {
        super.ComposeView()
        HomeLayout(
            songs = viewModel.songs.collectAsState().value,
            onAddSong = {
//                viewModel.addSong()
                viewModel.getAllImages()
            }
        )
    }
}

@Composable
fun HomeLayout(
    songs: List<Song> = emptyList(),
    onAddSong: () -> Unit = {}
) {
    CoreLayout(
        backgroundColor = Color.Black,
        bottomBar = {
            Button(
                onClick = onAddSong,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
        },
        content = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .statusBarsPadding()
            ) {
                items(
                    items = songs,
                    itemContent = { song ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = Color.Gray, shape = RoundedCornerShape(15.dp)
                                )
                                .padding(vertical = 10.dp, horizontal = 15.dp)
                        ) {
                            Text(
                                text = song.name,
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewHomeLayout() {
    HomeLayout()
}