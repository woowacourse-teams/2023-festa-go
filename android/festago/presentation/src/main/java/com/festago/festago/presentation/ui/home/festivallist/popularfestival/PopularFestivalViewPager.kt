package com.festago.festago.presentation.ui.home.festivallist.popularfestival

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.festago.festago.presentation.R
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopularFestivalScreen(uiState: PopularFestivalUiState) {
    val firstPage = (100 / 2) - (100 / 2) % uiState.festivals.size
    val rememberCoroutineScope = rememberCoroutineScope()
    val foregroundPagerState = rememberPagerState { 100 }
    val backgroundPagerState = rememberPagerState { uiState.festivals.size }
    val pageSize = 220.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidth - pageSize) / 2
    val dateFormatter = DateTimeFormatter.ofPattern(
        stringResource(R.string.festival_list_tv_date_format)
    )

    LaunchedEffect(key1 = Unit) {
        foregroundPagerState.apply {
            scroll {
                updateCurrentPage(firstPage)
            }
        }
    }

    LaunchedEffect(foregroundPagerState.currentPage) {
        rememberCoroutineScope.launch {
            backgroundPagerState.scrollToPage(
                foregroundPagerState.currentPage % uiState.festivals.size,
                0f
            )
        }
    }

    PopularFestivalScreen(
        uiState,
        backgroundPagerState,
        pageSize,
        dateFormatter,
        foregroundPagerState,
        horizontalPadding
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PopularFestivalScreen(
    uiState: PopularFestivalUiState,
    backgroundPagerState: PagerState,
    pageSize: Dp,
    dateFormatter: DateTimeFormatter?,
    foregroundPagerState: PagerState,
    horizontalPadding: Dp
) {
    Box(modifier = Modifier.fillMaxSize()) {
        BackgroundPopularFestivals(
            uiState = uiState,
            modifier = Modifier.fillMaxWidth(),
            backgroundPagerState = backgroundPagerState,
            pageSize = pageSize,
            dateFormatter = dateFormatter
        )
        ForegroundPopularFestivalsPager(
            uiState = uiState,
            foregroundPagerState = foregroundPagerState,
            horizontalPadding = horizontalPadding
        )

        HorizontalPagerIndicator(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            indicatorCornerRadius = 10.dp,
            unselectedIndicatorSize = 4.dp,
            selectedIndicatorSize = 8.dp,
            pageCount = uiState.festivals.size,
            currentPage = foregroundPagerState.currentPage % uiState.festivals.size,
            targetPage = foregroundPagerState.targetPage % uiState.festivals.size,
            currentPageOffsetFraction = foregroundPagerState.currentPageOffsetFraction
        )
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun ForegroundPopularFestivalsPager(
    foregroundPagerState: PagerState,
    horizontalPadding: Dp,
    uiState: PopularFestivalUiState
) {
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = foregroundPagerState,
        pageSpacing = 24.dp,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 70.dp),
    ) { page ->
        val calculatedPage = page % uiState.festivals.size
        val pageOffset =
            (foregroundPagerState.currentPage - page + foregroundPagerState.currentPageOffsetFraction).absoluteValue

        ForegroundPopularFestivalPage(
            modifier = Modifier.fillMaxHeight(),
            pageOffset = pageOffset,
            uiState = uiState,
            calculatedPage = calculatedPage
        )
    }
}

@Composable
private fun ForegroundPopularFestivalPage(
    modifier: Modifier = Modifier,
    pageOffset: Float,
    uiState: PopularFestivalUiState,
    calculatedPage: Int
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    scaleY = lerp(
                        start = 0.81f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                }
        ) {
            NetworkImage(
                modifier = Modifier
                    .aspectRatio(1f),
                url = uiState.festivals[calculatedPage].imageUrl,
            )
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun BackgroundPopularFestivals(
    modifier: Modifier = Modifier,
    backgroundPagerState: PagerState,
    uiState: PopularFestivalUiState,
    pageSize: Dp,
    dateFormatter: DateTimeFormatter?
) {
    HorizontalPager(
        modifier = modifier,
        state = backgroundPagerState,
        beyondBoundsPageCount = backgroundPagerState.pageCount,
        userScrollEnabled = false,
    ) { page ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            BackgroundFestivalImg(
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(0.8f)
                    .blur(5.dp),
                uiState = uiState,
                page = page
            )

            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
                text = uiState.title,
                maxLines = 2,
                minLines = 2,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            FestivalInfo(
                modifier = Modifier
                    .padding(top = 300.dp)
                    .align(Alignment.TopCenter)
                    .width(pageSize),
                pageSize = pageSize,
                uiState = uiState,
                page = page,
                dateFormatter = dateFormatter,
            )
        }
    }
}

@Composable
private fun FestivalInfo(
    modifier: Modifier = Modifier,
    pageSize: Dp,
    uiState: PopularFestivalUiState,
    page: Int,
    dateFormatter: DateTimeFormatter?
) {
    Column(
        modifier = modifier
    ) {
        val startDateString = uiState.festivals[page].startDate.format(dateFormatter)
        val endDateString = uiState.festivals[page].endDate.format(dateFormatter)
        val dateRangeString = stringResource(
            id = R.string.festival_list_tv_date_range_format,
            startDateString,
            endDateString
        )

        Text(
            modifier = Modifier
                .fillMaxSize(),
            text = uiState.festivals[page].name,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            minLines = 2,
            overflow = TextOverflow.Ellipsis,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            modifier = Modifier
                .fillMaxSize()
                .width(width = pageSize)
                .padding(top = 8.dp),
            text = dateRangeString,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
        )

        Text(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp)
                .width(width = pageSize),
            text = uiState.festivals[page].artists.joinToString(", ") { it.name },
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 2,
            minLines = 2,
            fontWeight = FontWeight.Medium,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun BackgroundFestivalImg(
    modifier: Modifier = Modifier,
    uiState: PopularFestivalUiState,
    page: Int
) {
    Box(
        modifier = modifier,
    ) {
        NetworkImage(
            modifier = Modifier.fillMaxSize(),
            url = uiState.festivals[page].imageUrl,
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NetworkImage(
    modifier: Modifier,
    url: String,
    filter: Color = Color.Transparent,
) {
    GlideImage(
        model = url,
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier.fillMaxSize(),
        loading = placeholder(ColorPainter(Color(0xD9FFFFFF))),
        failure = placeholder(ColorPainter(Color(0xD9FFFFFF))),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(filter)
    )
}
