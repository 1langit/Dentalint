package com.cemerlang.dentalint.presentation.ui.home

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cemerlang.dentalint.R
import com.cemerlang.dentalint.data.model.blog.BlogData
import com.cemerlang.dentalint.presentation.common.CheckupCardComponent
import com.cemerlang.dentalint.presentation.common.HorizontalCardComponent
import com.cemerlang.dentalint.presentation.ui.capture.CaptureCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    onCaptureClick: () -> Unit = {},
    onNotesClick: () -> Unit = {},
    onCheckupHistoryClick: () -> Unit = {},
    onSearchBlogClick: () -> Unit = {},
    onBlogClick: () -> Unit = {},
    onReadBlogClick: (String) -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel()
) {

    val blogs by viewModel.newestBlogs.collectAsState()
    val defaultModifier = Modifier.padding(20.dp, 0.dp)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(0.dp, 8.dp)
    ) {
        item { Carousel() }
        item {
            QuickAccess(defaultModifier) {
                MenuButton("Capture\n", painterResource(R.drawable.ic_capture)) { onCaptureClick() }
                MenuButton("Add Note\n", painterResource(R.drawable.ic_note)) { onNotesClick() }
                MenuButton("Checkup\nHistory", painterResource(R.drawable.ic_checkup)) { onCheckupHistoryClick() }
                MenuButton("Search\nBlog", painterResource(R.drawable.ic_blog)) { onSearchBlogClick() }
            }
        }
        item { ToothbrushAR(defaultModifier) }
//        item { UpcomingCheckup(defaultModifier) }
//        item { LatestCapture(defaultModifier) }
        item { NewestBlog(blogs, defaultModifier, onBlogClick, onReadBlogClick) }
    }
}

@Composable
fun Carousel(modifier: Modifier = Modifier) {

    val carouselImages = listOf(
        R.drawable.img_carousel_1,
        R.drawable.img_carousel_2,
        R.drawable.img_carousel_3
    )
    val pagerState = rememberPagerState(pageCount = { carouselImages.size })

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged.not()) {
        with(pagerState) {
            var currentPageKey by remember { mutableIntStateOf(0) }
            LaunchedEffect(key1 = currentPageKey) {
                launch {
                    delay(timeMillis = 5000L)
                    val nextPage = (currentPage + 1).mod(pageCount)
                    animateScrollToPage(page = nextPage)
                    currentPageKey = nextPage
                }
            }
        }
    }

    HorizontalPager(state = pagerState) { page ->
        Image(
            painter = painterResource(carouselImages[page]),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.padding(8.dp, 0.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    DotIndicators(pagerState)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicators(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        repeat(pagerState.pageCount) { iteration ->
            if (pagerState.currentPage == iteration) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .size(20.dp, 8.dp)
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                        .size(8.dp)
                )
            }

        }
    }
}

@Composable
fun QuickAccess(
    modifier: Modifier = Modifier,
    menus: @Composable () -> Unit = {}
) {
    Column(modifier = modifier) {
        Text(
            text = "Quick Access",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            menus()
        }
    }
}

@Composable
fun ToothbrushAR(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        Text(
            text = "Interactive AR Toothbrush",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        TextButton(
            onClick = {
                Toast.makeText(context, "Coming Soon", Toast.LENGTH_SHORT).show()
            },
            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource(R.drawable.img_ar_banner),
                contentDescription = "start ar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun UpcomingCheckup(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Upcoming Checkup",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary
        )
//        CheckupCardComponent()
    }
}

@Composable
fun LatestCapture(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Latest Capture",
            style = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 3
        )
//        CaptureCard()
    }
}

@Composable
fun NewestBlog(
    blogs: List<BlogData>,
    modifier: Modifier = Modifier,
    onBlogClick: () -> Unit = {},
    onBlogItemClick: (String) -> Unit = {}
) {

    val pagerState = rememberPagerState(pageCount = { blogs.size })

    Column {
        TextButton(
            onClick = { onBlogClick() },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "Newest Blog",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(20.dp, 0.dp)
        ) { page ->
            blogs.forEach {
                HorizontalCardComponent(
                    image = blogs[page].image,
                    cropImage = true,
                    subtitle = blogs[page].created_at,
                    title = blogs[page].title,
                    content = blogs[page].content
                ) {
                    onBlogItemClick(blogs[page].id)
                }
            }
        }
    }
}

@Composable
fun MenuButton(name: String, icon: Painter, onClick: () -> Unit = {}) {
    TextButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = icon,
                contentDescription = name,
                modifier = Modifier.size(52.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}