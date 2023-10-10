package com.example.mystoic.ui

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.mystoic.data.QuoteEntity
import com.example.mystoic.ui.favourites.FavouritesViewModel
import com.example.mystoic.ui.home.HomeScreenViewModel

@Composable
fun SharingBar(
    quoteText: String,
    quoteAuthor: String,
    isDailyQuote: Boolean,
    isFavourite: Boolean,
    homeScreenViewModel: HomeScreenViewModel?,
    favouritesViewModel: FavouritesViewModel?,
    favouritesQuoteEntity: QuoteEntity?,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(modifier = modifier) {
        IconButton(onClick = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "$quoteText\n\n-$quoteAuthor")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            ContextCompat.startActivity(context, shareIntent, null)
        }) {
            Image(imageVector = Icons.Filled.Share, contentDescription = "Share quote")
        }
        IconButton(
            onClick = {
                if (homeScreenViewModel != null) {
                    if (isFavourite) {
                        homeScreenViewModel.deleteFavourite(isDailyQuote)
                    } else {
                        homeScreenViewModel.saveFavourite(isDailyQuote)
                    }
                } else {
                    favouritesViewModel!!.deleteFavourite(quoteEntity = favouritesQuoteEntity!!)
                }
            }
        ) {
            if (isFavourite) {
                Image(imageVector = Icons.Filled.Star, contentDescription = "Favourite")
            } else {
                Image(imageVector = Icons.TwoTone.Star, contentDescription = "Add to favourites")
            }
        }
    }
}