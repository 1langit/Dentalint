package com.cemerlang.dentalint.presentation.ui.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.presentation.ui.profile.ProfileViewModel

@Composable
fun DentalintSplashScreen(
    onNavigate: (String) -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.isLoggedIn { login, message ->
            if (message.isNotBlank()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
            if (login) onNavigate("main") else onNavigate("login")
        }
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(240.dp))
        Image(
            painter = painterResource(R.drawable.img_text),
            contentDescription = "dentalint",
            contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your dental care companion",
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashPreview() {
    DentalintSplashScreen({})
}