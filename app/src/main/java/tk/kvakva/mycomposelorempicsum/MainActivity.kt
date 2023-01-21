package tk.kvakva.mycomposelorempicsum

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tk.kvakva.mycomposelorempicsum.datalevel.Repository
import tk.kvakva.mycomposelorempicsum.ui.theme.MyComposeLoremPicsumTheme
import kotlin.math.min

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            urlsListLiveData.postValue(Repository().gUrlsList())
        }

        setContent {
            PicsumLoremApp()
//            MyComposeLoremPicsumTheme {
            // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colors.background
//                ) {
//                    Greeting("Android")
//                }
//            }
        }
    }
}

val urlsListLiveData = MutableLiveData(
    listOf(
        "qwe", "asd", "zxc",
        "qwe", "asd", "zxc",
        "qwe", "asd", "zxc"
    )
)

@Composable
fun PicsumLoremApp() {

    //val picsList = (1..300).map { it.toString() }
    val picsListLiveDataObser = urlsListLiveData.observeAsState(listOf())

    MyComposeLoremPicsumTheme {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Compose Lorem Picsum")
                    },
                    actions = {
                        IconButton(onClick = {
                            Log.v(TAG, "menu list clicked")
                        }) {
                            Icon(Icons.Default.List, "list")
                        }
                    }
                )
            }
        ) { paddingValues: PaddingValues ->
            val bigPicture = rememberSaveable { mutableStateOf("") }
            LazyVerticalGrid(
                GridCells.Fixed(4),
                //LazyColumn(
                Modifier.padding(paddingValues),

                ) {
                items(
                    //picsList,
                    picsListLiveDataObser.value,
                    itemContent = { d: String ->

                        val secondIndex = d.lastIndexOf('/')
                        val u = if (secondIndex > 8) {
                            val firstIndex = d.lastIndexOf('/', secondIndex - 1)
                            if (firstIndex > 8) {
                                //d.replaceRange(firstIndex, d.length, "/400/200")
                                val w = d.substring(min(firstIndex+1,d.length),min(d.length,secondIndex))
                                val h = d.substring(min(secondIndex+1,d.length-1),d.length)
                                val nh=320*(h.toIntOrNull()?:1)/(w.toIntOrNull()?:1)
                                //Log.v(TAG,"w=$w,h=$h,nh=$nh")
                                //d.replaceRange(firstIndex, d.length, "/480/320")
                                d.replaceRange(firstIndex, d.length, "/320/$nh")
                            } else {
                                d
                            }
                        } else {
                            d
                        }
                        Log.v(TAG,"u = $u")
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                //.data("https://picsum.photos/seed/$it/160/90")
                                //.data("https://picsum.photos/seed/$it/160/90")
                                //.data(d)
                                .data(u)
                                .error(android.R.drawable.stat_notify_error)
                                .size(coil.size.Size.ORIGINAL)
                                //.size(Size(2000,1000))
                                .scale(Scale.FIT)
                                .placeholder(android.R.drawable.ic_menu_gallery)
                                .build()
                        )


                        Image(
                            painter = painter,
                            //contentDescription = "https://picsum.photos/seed/$it/480/320",
                            contentDescription = d,
                            modifier = Modifier
                                .padding(4.dp)
                                .clickable {
                                    //bigPicture.value = "https://picsum.photos/seed/$it/1920/1080"
                                    bigPicture.value = d
                                },
                            //contentScale = ContentScale.Fit
                        )
//                        AsyncImage(
//                            model = "https://picsum.photos/seed/$it/480/320",
//                            contentDescription = "https://picsum.photos/seed/$it/480/320",
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .clickable {
//                                    bigPicture.value = "https://picsum.photos/seed/$it/1920/1080"
//                                }
//                        )
                    })
            }
            if (bigPicture.value.length > 22 && bigPicture.value.substring(
                    0,
                    22
                ) == "https://picsum.photos/"
            ) {
                Box(
                    Modifier
                        .padding(paddingValues)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        bigPicture.value,
                        bigPicture.value,
                        Modifier
                            .padding(paddingValues)
                            .clickable {
                                bigPicture.value = ""
                            })
                }
            }


        }
    }
}

