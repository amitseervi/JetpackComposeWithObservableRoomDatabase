package com.amit.radiuscompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amit.radiuscompose.ui.components.FacilitiesFilterUiComponent
import com.amit.radiuscompose.ui.theme.RadiusComposeTheme
import com.amit.radiuscompose.ui.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RadiusComposeTheme {
                val homeViewModel: HomeViewModel = hiltViewModel()
                val uiState by homeViewModel.uiState.collectAsStateWithLifecycle()
                FacilitiesFilterUiComponent(
                    modifier = Modifier.fillMaxSize(),
                    state = uiState,
                    onFilterItemSelect = homeViewModel::selectFacilityOption
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RadiusComposeTheme {
        Greeting("Android")
    }
}