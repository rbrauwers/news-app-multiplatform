package com.rbrauwers.newsapp.designsystem

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NewsDefaultTopBar(title: String) {
    Text(text = title)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgedTopBar(title: String, count: Int?) {
    Row {
        NewsDefaultTopBar(title = title)
        count?.let {
            Spacer(modifier = Modifier.width(4.dp))
            Badge {
                Text(text = count.toString())
            }
        }
    }
}