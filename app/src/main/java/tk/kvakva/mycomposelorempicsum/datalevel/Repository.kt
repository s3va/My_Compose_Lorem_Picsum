package tk.kvakva.mycomposelorempicsum.datalevel

import android.util.Log
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val TAG = "Repository"
private const val BASE_URL = "https://picsum.photos/"

class Repository {
    suspend fun gUrlsList(): List<String> {
        val lorList = loremService.listRepos().body()
        Log.v(TAG, "lorList: $lorList")
        val strList = lorList?.map {
            //val d=it.downloadUrl
            //d.replaceRange(d.lastIndexOf('/',d.lastIndexOf('/')-1), d.length ,"/300/200")
            it.downloadUrl
        }
        Log.v(TAG, "strList: $strList")

        return strList ?: listOf()
    }
}

//@JsonClass(generateAdapter = true)
data class Lorem(
    val l: MutableList<LoremItem>
)

data class LoremItem(
    // @Json(name = "author")
    val author: String,
    @SerializedName("download_url")
    val downloadUrl: String,
    //@Json(name = "height")
    val height: Int,
    //@Json(name = "id")
    val id: String,
    //@Json(name = "url")
    val url: String,
    //@Json(name = "width")
    val width: Int
)

interface LoremService {
    @GET("v2/list?page=2&limit=100")
    suspend fun listRepos(): Response<List<LoremItem>>
}

val retrofit: Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        GsonConverterFactory.create()
    )
    .build();

val loremService: LoremService = retrofit.create(LoremService::class.java)
