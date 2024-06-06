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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.festago.festago.presentation.R
import com.festago.festago.presentation.ui.home.festivallist.uistate.ArtistUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.FestivalItemUiState
import com.festago.festago.presentation.ui.home.festivallist.uistate.PopularFestivalUiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PopularFestivalPager(uiState: PopularFestivalUiState) {

    val firstPage = (100 / 2) - (100 / 2) % uiState.festivals.size
    val rememberCoroutineScope = rememberCoroutineScope()
    val foregroundPagerState = rememberPagerState { 100 }

    LaunchedEffect(key1 = Unit) {
        foregroundPagerState.apply {
            scroll {
                updateCurrentPage(firstPage)
            }
        }
    }
    val backgroundPagerState = rememberPagerState { uiState.festivals.size }
    val pageSize = 220.dp
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val horizontalPadding = (screenWidth - pageSize) / 2
    val dateFormatter =
        DateTimeFormatter.ofPattern(stringResource(R.string.festival_list_tv_date_format))


    Box {
        HorizontalPager(
            modifier = Modifier
                .fillMaxWidth(),
            state = backgroundPagerState,
            beyondBoundsPageCount = backgroundPagerState.pageCount,
            userScrollEnabled = false,
        ) { page ->

            Box(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(0.8f)
                        .blur(5.dp)
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

                Column(
                    modifier = Modifier
                        .padding(top = 300.dp)
                        .align(Alignment.TopCenter)
                        .width(pageSize)
                ) {
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

                    val startDateString = uiState.festivals[page].startDate.format(dateFormatter)
                    val endDateString = uiState.festivals[page].endDate.format(dateFormatter)
                    val dateRangeString = stringResource(
                        id = R.string.festival_list_tv_date_range_format,
                        startDateString,
                        endDateString
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
        }

        LaunchedEffect(foregroundPagerState.currentPage) {
            rememberCoroutineScope.launch {
                backgroundPagerState.scrollToPage(
                    foregroundPagerState.currentPage % uiState.festivals.size,
                    0f
                )
            }
        }

        HorizontalPager(
            state = foregroundPagerState,
            modifier = Modifier
                .fillMaxSize(),
            pageSpacing = 24.dp,
            contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = 70.dp),
        ) { page ->
            val calculatedPage = page % uiState.festivals.size

            Column(modifier = Modifier.fillMaxHeight()) {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val pageOffset =
                                (foregroundPagerState.currentPage - page + foregroundPagerState.currentPageOffsetFraction).absoluteValue

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

        HorizontalPagerIndicator(
            indicatorCornerRadius = 10.dp,
            unselectedIndicatorSize = 4.dp,
            selectedIndicatorSize = 8.dp,
            pageCount = uiState.festivals.size,
            currentPage = foregroundPagerState.currentPage % uiState.festivals.size,
            targetPage = foregroundPagerState.targetPage % uiState.festivals.size,
            currentPageOffsetFraction = foregroundPagerState.currentPageOffsetFraction,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
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

@Preview(showBackground = true)
@Composable
fun PreviewNetworkImage() {
    NetworkImage(
        modifier = Modifier.fillMaxSize(),
        url = "https://www.example.com/image.jpg",
        filter = Color.Black.copy(alpha = 0.4f),
    )
}


@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
fun PreviewPopularFestivalPager() {
    PopularFestivalPager(
        PopularFestivalUiState(
            title = "Popular Festivals",
            festivals = listOf(
                FestivalItemUiState(
                    id = 1,
                    name = "Festival 1",
                    imageUrl = "https://www.example.com/image1.jpg",
                    artists = listOf(
                        ArtistUiState(1, "Artist 1", "https://www.example.com/artist1.jpg"),
                    ),
                    endDate = LocalDate.of(2022, 1, 1),
                    startDate = LocalDate.of(2022, 1, 1),
                    onFestivalDetail = {},
                ),
            )
        )
    )
}
