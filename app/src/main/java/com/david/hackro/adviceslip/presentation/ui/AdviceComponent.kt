package com.david.hackro.adviceslip.presentation.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.*
import com.david.hackro.adviceslip.R
import com.david.hackro.adviceslip.presentation.ui.theme.AdviceSlipTheme
import com.david.hackro.adviceslip.presentation.ui.theme.customTitle
import com.david.hackro.adviceslip.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdviceComponent : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initData()
    }

    private fun initView() {
        setContent {
            AdviceSlipTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    adviceScreen(viewModel) {
                        viewModel.loadAdvice()
                    }
                }
            }
        }
    }

    private fun initData() {
        viewModel.loadAdvice()
    }

}

@Composable
fun adviceScreen(mainViewModel: MainViewModel = viewModel(), onRefreshAdviceListener: () -> Unit) {
    val uiState by mainViewModel.state.collectAsState()
    val listColors = listOf(colorResource(R.color.purple_500), colorResource(R.color.purple_200))

    Box(
        modifier = Modifier.fillMaxSize().background(Brush.horizontalGradient(listColors))
    ) {

        animation()
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            when {
                uiState.isProgress -> CircularProgressIndicator()
                uiState.isError -> showError()
                else -> showAdvice(uiState.response)
            }

            Image(painter = painterResource(R.drawable.refresh_icon),
                contentDescription = "",
                modifier = Modifier.size(40.dp).padding(0.dp, 16.dp, 0.dp, 0.dp)
                    .clickable { onRefreshAdviceListener.invoke() })
        }
    }
}

@Composable
private fun showAdvice(advice: String) {
    textContainer(advice, customTitle)
}

@Composable
private fun showError() {
    Toast.makeText(
        LocalContext.current, stringResource(R.string.toast_error), Toast.LENGTH_SHORT
    ).show()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun textContainer(advice: String, typography: TextStyle) {
    AnimatedContent(targetState = advice) {
        Text(
            text = advice,
            modifier = Modifier.fillMaxWidth().animateContentSize().padding(10.dp),
            style = typography,
        )
    }
}

@Composable
fun animation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.stars))
    val progress by animateLottieCompositionAsState(
        composition, iterations = LottieConstants.IterateForever,
        isPlaying = true,
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxWidth().padding(0.dp, 100.dp, 0.dp, 0.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    adviceScreen {}
}
