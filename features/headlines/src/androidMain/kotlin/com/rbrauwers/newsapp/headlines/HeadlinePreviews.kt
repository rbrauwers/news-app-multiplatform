package com.rbrauwers.newsapp.headlines

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun HeadlinePreview() {
    Headline(
        article = ArticleUi(
            id = 1,
            author = "Simpsons",
            title = "Inflation is super high is super high is super high is super high is super high",
            urlToImage = "https://images.pexels.com/photos/34299/herbs-flavoring-seasoning-cooking.jpg?cs=srgb&dl=pexels-pixabay-34299.jpg&fm=jpg&w=640&h=427&_gl=1*1urd5oa*_ga*MzQ2NzQzNzA3LjE2NzU3NTcwNzU.*_ga_8JE65Q40S6*MTY3NTc1NzA3NS4xLjEuMTY3NTc1NzEwNC4wLjAuMA..",
            url = "https://google.com",
            publishedAt = "2023-01-02 10:21:00"
        )
    )
}