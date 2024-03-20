package remindmed

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.gradle.models.LoginModel
import com.gradle.ui.views.shared.Login


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainViewModel: LoginModel by viewModels()
        mainViewModel.setContext(this)
        setContent {
            Login(mainViewModel)
        }
    }
}
